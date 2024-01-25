package org.firstinspires.ftc.teamcode.opmodes.auto;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.RobotActions;
import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

import java.util.function.DoublePredicate;

@Autonomous
public class farblue2pluscycle extends EnhancedOpMode {
    int dice =0;
    Pose2d blueFarStart = new Pose2d(-36,61,Math.toRadians(270));
    Robot drive;
    Deposit deposit;
    Intake intake;
    Slides slides;
boolean offset;
    TaskScheduler scheduler;
    RobotActions actions;
    @Override
    public void primaryLoop() {
        drive.primaryLoop();
    }
    public void initLoop()
    {

        if(drive.teamElementDetector.centerY < 0) {
            dice = 3;
        } else if(drive.teamElementDetector.centerY < 107){
            dice = 1;
        } else {
            dice = 2;
        }
        Context.tel.addData("Team red?", Context.isTeamRed);
        Context.tel.addData("element Pos", dice);
        Context.tel.addData("centerY", drive.teamElementDetector.centerY);
        Context.tel.addData("largest area", drive.teamElementDetector.getLargestArea());
        drive.initLoop();
    }
    public void onStart()
    {
        drive.closeCameras();
    }
    public void linearOpMode() {
        offset=false;
        TrajectorySequence rightPurple = drive.trajectorySequenceBuilder(blueFarStart)
                .addTemporalMarker(0.5, () -> intake.setState(Intake.PositionState.DOWN))
                .splineToConstantHeading(new Vector2d(-48, 36),Math.toRadians(270))
                .build();
        TrajectorySequence rightPurpleToTurn = drive.trajectorySequenceBuilder(rightPurple.end())
                .addTemporalMarker(1, () ->  {intake.setState(Intake.PositionState.RAISED);})
                .lineToLinearHeading(new Pose2d(-34,57,Math.toRadians(180)))
                .build();
        TrajectorySequence rightPurpleToBack = drive.trajectorySequenceBuilder(rightPurpleToTurn.end())
                .setReversed(true)
                .lineToConstantHeading(new Vector2d(20,57))
                .splineToConstantHeading(new Vector2d(49, 26), Math.toRadians(0))
                .addSpatialMarker(new Vector2d(-10, 57), () ->  {
                    scheduler.scheduleTaskList(actions.autoRaiseSlides(Slides.SlideState.AUTO_LOW));
                    intake.setState(Intake.PowerState.OFF);
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .addSpatialMarker(new Vector2d(47,26),()->{
                    scheduler.scheduleTaskList(actions.scorePixelDelay());
                })
                .build();
        TrajectorySequence rightBackToTurn = drive.trajectorySequenceBuilder(rightPurpleToBack.end())
                .lineToLinearHeading(new Pose2d(25, 58, Math.toRadians(180)))
                .build();
        TrajectorySequence rightBackToStack = drive.trajectorySequenceBuilder(rightBackToTurn.end())
                .setReversed(false)
                .lineToConstantHeading(new Vector2d(-28,58))
                .splineToConstantHeading(new Vector2d(-55, 34), Math.toRadians(180),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineToConstantHeading(new Vector2d(-58, 34))



                .addSpatialMarker(new Vector2d(-30, 55), () -> {
                    intake.setState(Intake.PositionState.DOWN);
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    scheduler.scheduleTaskList(actions.runSweepersAuto(-56));
                })
                .build();


        TrajectorySequence midPurple = drive.trajectorySequenceBuilder(blueFarStart)
                .addTemporalMarker(0.5, () ->  intake.setState(Intake.PositionState.DOWN))
                .lineTo(new Vector2d(-36, 34))
                .build();
        TrajectorySequence midPurpleToTurn = drive.trajectorySequenceBuilder(midPurple.end())
                .addTemporalMarker(1, () ->  {intake.setState(Intake.PositionState.RAISED);})
                .lineTo(new Vector2d(-38, 37),
                        drive.getVelocityConstraint(30, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .splineToSplineHeading(new Pose2d(-38, 57, Math.toRadians(180)), Math.toRadians(90),
                        drive.getVelocityConstraint(35, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .build();
        TrajectorySequence midPurpleToBack = drive.trajectorySequenceBuilder(midPurpleToTurn.end())
                .lineToConstantHeading(new Vector2d(20,57),
                        drive.getVelocityConstraint(50, 2.4, 15.06),
                        drive.getAccelerationConstraint(40))
                .addSpatialMarker(new Vector2d(-10, 57), () ->  {
                    scheduler.scheduleTaskList(actions.autoRaiseSlides(Slides.SlideState.AUTO_LOW));
                    intake.setState(Intake.PowerState.OFF);
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .addSpatialMarker(new Vector2d(47,26),()->{
                    scheduler.scheduleTaskList(actions.scorePixelDelay());
                })
                .build();
        TrajectorySequence midBackToTurn = drive.trajectorySequenceBuilder(midPurpleToBack.end())
                .lineToLinearHeading(new Pose2d(35, 58, Math.toRadians(175)))
                .build();
        TrajectorySequence midBackToStack = drive.trajectorySequenceBuilder(midBackToTurn.end())
                .setReversed(false)
                .lineToConstantHeading(new Vector2d(-28,63))
                .splineToConstantHeading(new Vector2d(-55, 34), Math.toRadians(180),
                        drive.getVelocityConstraint(30, 2.4, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineTo(new Vector2d(-57.5, 34))
                .addSpatialMarker(new Vector2d(-30, 55), () -> {
                    intake.setState(Intake.PositionState.DOWN);
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    scheduler.scheduleTaskList(actions.runSweepersAuto(-56.5));
                })
                .build();


        TrajectorySequence leftPurple = drive.trajectorySequenceBuilder(blueFarStart)
                .addTemporalMarker(0.5, () ->  intake.setState(Intake.PositionState.DOWN))
                .lineTo(new Vector2d(-36, 45),
                        drive.getVelocityConstraint(40, 2.4, 15.06),
                        drive.getAccelerationConstraint(35))
                .splineToSplineHeading(new Pose2d(-31, 36, Math.toRadians(-45)), Math.toRadians(-45),
                        drive.getVelocityConstraint(40, 1.5, 15.06),
                        drive.getAccelerationConstraint(35))
                .build();
        TrajectorySequence leftPurpleToTurn = drive.trajectorySequenceBuilder(leftPurple.end())
                .addTemporalMarker(1, () ->  {intake.setState(Intake.PositionState.RAISED);})
                .lineTo(new Vector2d(-32, 37),
                        drive.getVelocityConstraint(40, 1.5, 15.06),
                        drive.getAccelerationConstraint(35))
                .splineToSplineHeading(new Pose2d(-38, 59, Math.toRadians(180)), Math.toRadians(90),
                        drive.getVelocityConstraint(40, 1.5, 15.06),
                        drive.getAccelerationConstraint(35))
                .build();
        TrajectorySequence leftPurpleToBack = drive.trajectorySequenceBuilder(leftPurpleToTurn.end())
                .lineToConstantHeading(new Vector2d(20,57))
                .splineToConstantHeading(new Vector2d(50, 38), Math.toRadians(0))
                .addSpatialMarker(new Vector2d(-10, 57), () ->  {
                    scheduler.scheduleTaskList(actions.autoRaiseSlides(Slides.SlideState.AUTO_LOW));
                    intake.setState(Intake.PowerState.OFF);
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .addSpatialMarker(new Vector2d(47,26),()->{
                    scheduler.scheduleTaskList(actions.scorePixelDelay());
                })
                .build();
        TrajectorySequence leftBackToTurn = drive.trajectorySequenceBuilder(leftPurpleToBack.end())
                .lineToLinearHeading(new Pose2d(35, 58, Math.toRadians(178)))
                .build();
        TrajectorySequence leftBackToStack = drive.trajectorySequenceBuilder(leftBackToTurn.end())
                .setReversed(false)
                .lineToConstantHeading(new Vector2d(-28,63))
                .splineToConstantHeading(new Vector2d(-55, 34), Math.toRadians(180),
                        drive.getVelocityConstraint(30, 2.4, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineToConstantHeading(new Vector2d(-57.5, 34))
                .addSpatialMarker(new Vector2d(-30, 55), () -> {
                    intake.setState(Intake.PositionState.DOWN);
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    scheduler.scheduleTaskList(actions.runSweepersAuto(-56.5));
                })
                .build();

        TrajectorySequence stackToTurn = drive.trajectorySequenceBuilder(rightBackToStack.end())
                .setReversed(true)
                .lineToConstantHeading(new Vector2d(-36,57))
                .build();
        TrajectorySequence stackToBack = drive.trajectorySequenceBuilder(stackToTurn.end())
                .setReversed(true)
                .lineToConstantHeading(new Vector2d(20,57))
                .splineToConstantHeading(new Vector2d(49, 34), Math.toRadians(0))
                .lineTo(new Vector2d(51, 34))
                .addSpatialMarker(new Vector2d(-30, 57), () -> {
                    scheduler.scheduleTaskList(actions.slidesOnly(Slides.SlideState.AUTO_LOW));
                    intake.setState(Intake.PositionState.RAISED);
                })
                .addSpatialMarker(new Vector2d(20, 57), () -> {
                    intake.setState(Intake.ConveyorState.OFF);
                    intake.setState(Intake.PowerState.OFF);
                })
                .build();

        drive.setPoseEstimate(blueFarStart);
        waitForStart();

        switch(dice){
            case 1:
                drive.followTrajectorySequence(leftPurple);
                break;
            case 2:
                drive.followTrajectorySequence(midPurple);
                break;
            case 3:
                drive.followTrajectorySequence(rightPurple);
        }

        intake.setState(Intake.SweeperState.ONE_SWEEP);
        die(500);
        intake.setState(Intake.PositionState.RAISED);

        switch(dice){
            case 1:
                drive.followTrajectorySequence(leftPurpleToTurn);
                drive.followTrajectorySequence(leftPurpleToBack);
                break;
            case 2:
                drive.followTrajectorySequence(midPurpleToTurn);
                drive.followTrajectorySequence(midPurpleToBack);
                break;
            case 3:
                drive.followTrajectorySequence(rightPurpleToTurn);
                drive.followTrajectorySequence(rightPurpleToBack);
        }
        switch(dice){
            case 1:
                drive.followTrajectorySequence(leftBackToTurn);
                drive.followTrajectorySequence(leftBackToStack);
                break;
            case 2:
                drive.followTrajectorySequence(midBackToTurn);
                drive.followTrajectorySequence(midBackToStack);
                break;
            case 3:
                drive.followTrajectorySequence(rightBackToTurn);
                drive.followTrajectorySequence(rightBackToStack);
        }
        die(1500);
        intake.setState(Intake.PositionState.RAISED);
        die(500);
        drive.followTrajectorySequence(stackToTurn);
        drive.followTrajectorySequence(stackToBack);
        scheduler.scheduleTaskList(actions.slidesOnly(Slides.SlideState.AUTO_LOW));
    }

    public void die (int milliseconds) {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        while (timer.milliseconds() < milliseconds&&opModeIsActive()) {
            Context.tel.addData("in wait", timer.milliseconds());
        }
    }
    public void waitForDrive(){
        while(drive.isBusy()&&!isStopRequested()){

        }
    }
    @Override
    public void initialize() {

        this.setLoopTimes(10);
        drive=Robot.getInstance();

        Context.isTeamRed=false;
        scheduler=new TaskScheduler();
        actions= RobotActions.getInstance();
        deposit=drive.deposit;
        intake=drive.intake;
        slides=drive.slides;

        intake.init();
        deposit.init();
        slides.init();
        intake.setOperationState(Module.OperationState.PRESET);
        intake.setState(Intake.SweeperState.INIT);
        deposit.setState(Deposit.FlipState.PRIMED);
        deposit.setState(Deposit.ClawState.CLOSED1);
    }
}

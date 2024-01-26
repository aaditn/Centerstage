package org.firstinspires.ftc.teamcode.opmodes.auto.closeAuto;


import static org.firstinspires.ftc.teamcode.roadrunner.drive.Trajectories.blueCloseStart;

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

@Autonomous
public class closeBlue2_6 extends EnhancedOpMode {
    Robot drive;
    Deposit deposit;
    Intake intake;
    Slides slides;
    TaskScheduler scheduler;
    RobotActions actions;
int dice;
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
        //Context.tel.update();
        drive.initLoop();
    }
    public void onStart()
    {
        drive.closeCameras();
    }
    public void linearOpMode() {
        TrajectorySequence rightPurple = drive.trajectorySequenceBuilder(blueCloseStart)
                .lineTo(new Vector2d(12, 45))
                .splineToSplineHeading(new Pose2d(9, 38,Math.toRadians(225)), Math.toRadians(225))
                .build();
        TrajectorySequence rightYellow = drive.trajectorySequenceBuilder(rightPurple.end())
                .lineToLinearHeading(new Pose2d(52, 27, Math.toRadians(180)))
                .addSpatialMarker(new Vector2d(47,26),()->{
                    scheduler.scheduleTaskList(actions.scorePixelDelay());
                })
                .build();
        TrajectorySequence rightBackToStack = drive.trajectorySequenceBuilder(rightYellow.end())
                .setReversed(false)
                .splineToConstantHeading(new Vector2d(10, 58), Math.toRadians(180),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(60))
                .lineToConstantHeading(new Vector2d(-28,58))
                .splineToConstantHeading(new Vector2d(-55, 33), Math.toRadians(180),
                        drive.getVelocityConstraint(35, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .lineToConstantHeading(new Vector2d(-58, 33),
                        drive.getVelocityConstraint(30, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .addSpatialMarker(new Vector2d(-30, 55), () -> {
                    intake.setState(Intake.PositionState.DOWN);
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    scheduler.scheduleTaskList(actions.runSweepersAuto(56));
                })
                .build();


        TrajectorySequence midPurple = drive.trajectorySequenceBuilder(blueCloseStart)
                .lineTo(new Vector2d(12, 34))
                .build();
        TrajectorySequence midYellow = drive.trajectorySequenceBuilder(midPurple.end())
                .lineToLinearHeading(new Pose2d(51, 33, Math.toRadians(180)))
                .addSpatialMarker(new Vector2d(47,26),()->{
                    scheduler.scheduleTaskList(actions.scorePixelDelay());
                })
                .build();
        TrajectorySequence midBackToStack = drive.trajectorySequenceBuilder(midYellow.end())
                .setReversed(false)
                .splineToConstantHeading(new Vector2d(10, 58), Math.toRadians(180),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(60))
                .lineToConstantHeading(new Vector2d(-28,58))
                .splineToConstantHeading(new Vector2d(-55, 33), Math.toRadians(180),
                        drive.getVelocityConstraint(35, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .lineToConstantHeading(new Vector2d(-58, 33),
                        drive.getVelocityConstraint(30, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .addSpatialMarker(new Vector2d(-30, 55), () -> {
                    intake.setState(Intake.PositionState.DOWN);
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    scheduler.scheduleTaskList(actions.runSweepersAuto(56));
                })
                .build();

        TrajectorySequence leftPurple = drive.trajectorySequenceBuilder(blueCloseStart)
                .lineTo(new Vector2d(13, 60))
                .splineToConstantHeading(new Vector2d(25, 37), Math.toRadians(270))
                .build();
        TrajectorySequence leftYellow = drive.trajectorySequenceBuilder(leftPurple.end())
                .lineToLinearHeading(new Pose2d(52, 40, Math.toRadians(180)))
                .addSpatialMarker(new Vector2d(47,26),()->{
                    scheduler.scheduleTaskList(actions.scorePixelDelay());
                })
                .build();
        TrajectorySequence leftBackToStack = drive.trajectorySequenceBuilder(leftYellow.end())
                .setReversed(false)
                .splineToConstantHeading(new Vector2d(10, 58), Math.toRadians(180),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(60))
                .lineToConstantHeading(new Vector2d(-28,58))
                .splineToConstantHeading(new Vector2d(-55, 33), Math.toRadians(180),
                        drive.getVelocityConstraint(35, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .lineToConstantHeading(new Vector2d(-58, 33),
                        drive.getVelocityConstraint(30, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .addSpatialMarker(new Vector2d(-30, 55), () -> {
                    intake.setState(Intake.PositionState.DOWN);
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    scheduler.scheduleTaskList(actions.runSweepersAuto(56));
                })
                .build();

        waitForStart();
        drive.setPoseEstimate(blueCloseStart);
        switch(dice){
            case 1:
                drive.followTrajectorySequenceAsync(leftPurple);
                break;
            case 2:
                drive.followTrajectorySequenceAsync(midPurple);
                break;
            case 3:
                drive.followTrajectorySequenceAsync(rightPurple);
        }
        while(drive.isBusy()){

        }
        intake.setState(Intake.PositionState.DOWN);
        intake.setState(Intake.SweeperState.ONE_SWEEP);
        die(1000);
        scheduler.scheduleTaskList(actions.autoRaiseSlides(Slides.SlideState.AUTO_LOW));
        switch(dice){
            case 1:
                drive.followTrajectorySequence(rightYellow);
                break;
            case 2:
                drive.followTrajectorySequence(midYellow);
                break;
            case 3:
                drive.followTrajectorySequence(leftYellow);
        }
        while (drive.isBusy()) {

        }


      /* drive.followTrajectorySequenceAsync(leftStack);
        while (drive.isBusy()) {

        }
        intake.setState(Intake.SweeperState.FOUR_SWEEP);
        die (1500);
        intake.setState(Intake.PositionState.RAISED);
        die(500);
        drive.followTrajectorySequenceAsync(leftBackFromStack);
        while (drive.isBusy()) {

        }
        die(500);
        scheduler.scheduleTaskList(actions.scorePixels());
        die(500);

       */




//        drive.followTrajectorySequence(leftBackFromStack);
//        die(1000);
//        drive.followTrajectorySequence(leftStackFromBack);
//        die(1000);
//        drive.followTrajectorySequence(leftBackFromStack);
//        die(1000);
//        drive.followTrajectorySequence(leftStackFromBack);
//        die(1000);

    }

    public void die (int milliseconds) {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        while (timer.milliseconds() < milliseconds&&opModeIsActive()) {

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

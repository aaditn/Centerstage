package org.firstinspires.ftc.teamcode.opmodes.auto.closeAuto;


import static org.firstinspires.ftc.teamcode.roadrunner.drive.Trajectories.blueCloseStart;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.DroneLauncher;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.RobotActions;
import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

@Autonomous
public class closeBlue2plus6 extends EnhancedOpMode {
    Robot drive;
    Deposit deposit;
    Intake intake;
    Slides slides;
    DroneLauncher drone;
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
                .addTemporalMarker(1.5,()->
                        intake.setState(Intake.SweeperState.ONE_SWEEP))
                .splineToSplineHeading(new Pose2d(9, 38,Math.toRadians(225)), Math.toRadians(225))
                .addTemporalMarker(0.25, () -> intake.setState(Intake.PositionState.DOWN))
                .build();
        TrajectorySequence rightYellow = drive.trajectorySequenceBuilder(rightPurple.end())
                .lineToLinearHeading(new Pose2d(51, 27, Math.toRadians(180)))
                .addSpatialMarker(new Vector2d(47,26),()->{
                    scheduler.scheduleTaskList(actions.scorePixelDelay());
                })

                .addTemporalMarker(0.5, () ->  {intake.setState(Intake.PositionState.RAISED);})
                .build();
        TrajectorySequence rightBackToStack = drive.trajectorySequenceBuilder(rightYellow.end())

                .splineToConstantHeading(new Vector2d(13,11.5),Math.toRadians(180),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineTo(new Vector2d(-58,11.5))

                .addSpatialMarker(new Vector2d(-30, 11), () -> {
                    intake.setState(Intake.PositionState.DOWN);
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    scheduler.scheduleTaskList(actions.runSweepersAuto(-56));
                })
                .build();
        TrajectorySequence midPurple = drive.trajectorySequenceBuilder(blueCloseStart)

                .addTemporalMarker(1.5,()->
                        intake.setState(Intake.SweeperState.ONE_SWEEP))
                .lineTo(new Vector2d(12, 34))
                .addTemporalMarker(0.25, () -> intake.setState(Intake.PositionState.DOWN))
                .build();
        TrajectorySequence midYellow = drive.trajectorySequenceBuilder(midPurple.end())
                .lineToLinearHeading(new Pose2d(52, 33, Math.toRadians(180)))
                .build();
        TrajectorySequence midBackToStack = drive.trajectorySequenceBuilder(midYellow.end())
                .splineToConstantHeading(new Vector2d(13,11.5),Math.toRadians(180),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineTo(new Vector2d(-58,11.5))

                .addSpatialMarker(new Vector2d(-30, 11), () -> {
                    intake.setState(Intake.PositionState.DOWN);
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    scheduler.scheduleTaskList(actions.runSweepersAuto(-56));
                })
                .build();
        TrajectorySequence leftPurple = drive.trajectorySequenceBuilder(blueCloseStart)

                .lineTo(new Vector2d(13, 60))
                .splineToConstantHeading(new Vector2d(25, 37), Math.toRadians(270))
                .addTemporalMarker(1, () -> intake.setState(Intake.PositionState.DOWN))
                .build();
        TrajectorySequence leftYellow = drive.trajectorySequenceBuilder(leftPurple.end())
                .lineToLinearHeading(new Pose2d(52, 40, Math.toRadians(180)))
                .build();
        TrajectorySequence leftBackToStack = drive.trajectorySequenceBuilder(leftYellow.end())
                .splineToConstantHeading(new Vector2d(13,11.5),Math.toRadians(180),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineTo(new Vector2d(-58,11.5))

                .addSpatialMarker(new Vector2d(-30, 11), () -> {
                    intake.setState(Intake.PositionState.DOWN);
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    scheduler.scheduleTaskList(actions.runSweepersAuto(-56));
                })
                .build();
        TrajectorySequence stackToBack1 = drive.trajectorySequenceBuilder(leftBackToStack.end())
                .setReversed(true)
                .lineTo(new Vector2d(13,11.5))
                .splineToConstantHeading(new Vector2d(51,27),Math.toRadians(0),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .addSpatialMarker(new Vector2d(-30, 11), () -> {
                    scheduler.scheduleTaskList(actions.slidesOnly(Slides.SlideState.AUTO_LOW));
                    intake.setState(Intake.PositionState.RAISED);
                })

                .addSpatialMarker(new Vector2d(49,26),()->{
                    scheduler.scheduleTaskList(actions.scorePixelDelay());
                })
                .addSpatialMarker(new Vector2d(20, 11), () -> {
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .build();
        TrajectorySequence backToStack1 = drive.trajectorySequenceBuilder(stackToBack1.end())

                .splineToConstantHeading(new Vector2d(13,11.5),Math.toRadians(180),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineTo(new Vector2d(-58,11.5))

                .addSpatialMarker(new Vector2d(-30, 11), () -> {
                    intake.setState(Intake.PositionState.DOWN);
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                  //  scheduler.scheduleTaskList(actions.runSweepersAuto(-56,true));
                })
                .build();
        TrajectorySequence stackToBack2 = drive.trajectorySequenceBuilder(leftBackToStack.end())
                .setReversed(true)
                .lineTo(new Vector2d(13,11.5))
                .splineToConstantHeading(new Vector2d(51,27),Math.toRadians(0),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .addSpatialMarker(new Vector2d(-30, 11), () -> {
                    scheduler.scheduleTaskList(actions.slidesOnly(Slides.SlideState.AUTO_TWO));
                    intake.setState(Intake.PositionState.RAISED);
                })

                .addSpatialMarker(new Vector2d(49,26),()->{
                    scheduler.scheduleTaskList(actions.scorePixelDelay());
                })
                .addSpatialMarker(new Vector2d(20, 11), () -> {
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .build();

        TrajectorySequence backToStack2 = drive.trajectorySequenceBuilder(stackToBack1.end())

                .splineToConstantHeading(new Vector2d(13,11.5),Math.toRadians(180),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineTo(new Vector2d(-58,11.5))

                .addSpatialMarker(new Vector2d(-30, 11), () -> {
                    intake.setState(Intake.PositionState.DOWN);
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    scheduler.scheduleTaskList(actions.runSweepersAuto(-56,true,true));
                })
                .build();


        waitForStart();
        drive.setPoseEstimate(blueCloseStart);
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
        scheduler.scheduleTaskList(actions.autoRaiseSlides(Slides.SlideState.AUTO_LOW));
        switch(dice){
            case 1:
                drive.followTrajectorySequence(leftYellow);
                break;
            case 2:
                drive.followTrajectorySequence(midYellow);
                break;
            case 3:
                drive.followTrajectorySequence(rightYellow);
        }
        switch(dice){
            case 1:
                drive.followTrajectorySequence(leftBackToStack);
                break;
            case 2:
                drive.followTrajectorySequence(midBackToStack);
                break;
            case 3:
                drive.followTrajectorySequence(rightBackToStack);
        }
        waitMS(1000);
        intake.setState(Intake.PositionState.RAISED);
        drive.followTrajectorySequence(stackToBack1);
        drive.followTrajectorySequence(backToStack1);
        waitMS(1000);
        intake.setState(Intake.PositionState.RAISED);
        waitMS(250);
        drive.followTrajectorySequence(stackToBack2);

    }

    public void waitMS(int milliseconds) {
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
        drone=drive.droneLauncher;

        intake.init();
        deposit.init();
        slides.init();
        drone.init();
        intake.setOperationState(Module.OperationState.PRESET);
        intake.setState(Intake.SweeperState.INIT);
        deposit.setState(Deposit.FlipState.PRIMED);
        deposit.setState(Deposit.ClawState.CLOSED1);
    }
}

package org.firstinspires.ftc.teamcode.opmodes.auto;


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
public class closeBlue extends EnhancedOpMode {
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
        TrajectorySequence leftPurple = drive.trajectorySequenceBuilder(blueCloseStart)
                .lineTo(new Vector2d(12, 45))
                .splineToSplineHeading(new Pose2d(9, 38,Math.toRadians(225)), Math.toRadians(225))
                .build();
        TrajectorySequence leftYellow = drive.trajectorySequenceBuilder(leftPurple.end())
                .lineToLinearHeading(new Pose2d(52, 27, Math.toRadians(180)))
                .build();
        TrajectorySequence leftStack = drive.trajectorySequenceBuilder(leftYellow.end())

                .addSpatialMarker(new Vector2d(-50, 57), () -> {
                    intake.setState(Intake.PowerState.INTAKE);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    intake.setState(Intake.PositionState.DOWN);
                })
                .addSpatialMarker(new Vector2d(-25, 57), () -> {
                    drive.setPoseEstimate(new Pose2d(drive.getPoseEstimate().getX(),drive.getPoseEstimate().getY()+5,drive.getPoseEstimate().
                            getHeading()));
                })
                .splineToConstantHeading(new Vector2d(30, 57), Math.toRadians(180),
                        drive.getVelocityConstraint(30, 2.4, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineToConstantHeading(new Vector2d(-25, 57))
                .splineToConstantHeading(new Vector2d(-56, 35), Math.toRadians(180),
                        drive.getVelocityConstraint(30, 2.4, 15.06),
                        drive.getAccelerationConstraint(40))
                .build();
        TrajectorySequence leftBack = drive.trajectorySequenceBuilder(leftStack.end())
                .addSpatialMarker(new Vector2d(-10, 57), () ->  {
                    scheduler.scheduleTaskList(actions.raiseSlides(Slides.SlideState.AUTO_LOW));
                    intake.setState(Intake.PowerState.OFF);
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .addSpatialMarker(new Vector2d(40, 48), () ->  {
                    deposit.setState(Deposit.RotateState.ONE_EIGHTY);
                })
                .lineToConstantHeading(new Vector2d(-54, 36))
                .splineToConstantHeading(new Vector2d(-35, 57), Math.toRadians(0),
                        drive.getVelocityConstraint(30, 2.4, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineToConstantHeading(new Vector2d(24, 57))
                .splineToConstantHeading(new Vector2d(51, 29), Math.toRadians(0),
                        drive.getVelocityConstraint(30, 2.4, 15.06),
                        drive.getAccelerationConstraint(40))
                .build();
        TrajectorySequence midPurple = drive.trajectorySequenceBuilder(blueCloseStart)
                .lineTo(new Vector2d(12, 34))
                .build();
        TrajectorySequence midYellow = drive.trajectorySequenceBuilder(midPurple.end())
                .lineToLinearHeading(new Pose2d(51, 33, Math.toRadians(180)))
                .build();
        TrajectorySequence midStack = drive.trajectorySequenceBuilder(midYellow.end())

                .addSpatialMarker(new Vector2d(-50, 57), () -> {
                    intake.setState(Intake.PowerState.INTAKE);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    intake.setState(Intake.PositionState.DOWN);
                })
                .addSpatialMarker(new Vector2d(-25, 57), () -> {
                    drive.setPoseEstimate(new Pose2d(drive.getPoseEstimate().getX(),drive.getPoseEstimate().getY()+5,drive.getPoseEstimate().
                            getHeading()));
                })
                .splineToConstantHeading(new Vector2d(30, 57), Math.toRadians(180),
                        drive.getVelocityConstraint(30, 2.4, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineToConstantHeading(new Vector2d(-25, 57))
                .splineToConstantHeading(new Vector2d(-56, 35), Math.toRadians(180),
                        drive.getVelocityConstraint(30, 2.4, 15.06),
                        drive.getAccelerationConstraint(40))
                .build();
        TrajectorySequence midBack = drive.trajectorySequenceBuilder(midStack.end())
                .addSpatialMarker(new Vector2d(-10, 57), () ->  {
                    scheduler.scheduleTaskList(actions.raiseSlides(Slides.SlideState.AUTO_LOW));
                    intake.setState(Intake.PowerState.OFF);
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .addSpatialMarker(new Vector2d(40, 48), () ->  {
                    deposit.setState(Deposit.RotateState.ONE_EIGHTY);
                })
                .lineToConstantHeading(new Vector2d(-54, 36))
                .splineToConstantHeading(new Vector2d(-35, 57), Math.toRadians(0),
                        drive.getVelocityConstraint(30, 2.4, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineToConstantHeading(new Vector2d(24, 57))
                .splineToConstantHeading(new Vector2d(51, 29), Math.toRadians(0),
                        drive.getVelocityConstraint(30, 2.4, 15.06),
                        drive.getAccelerationConstraint(40))
                .build();
        TrajectorySequence rightPurple = drive.trajectorySequenceBuilder(blueCloseStart)
                .lineTo(new Vector2d(13, 60))
                .splineToConstantHeading(new Vector2d(25, 37), Math.toRadians(270))
                .build();
        TrajectorySequence rightYellow = drive.trajectorySequenceBuilder(rightPurple.end())
                .lineToLinearHeading(new Pose2d(52, 40, Math.toRadians(180)))
                .build();
        TrajectorySequence rightStack = drive.trajectorySequenceBuilder(rightYellow.end())

                .addSpatialMarker(new Vector2d(-50, 57), () -> {
                    intake.setState(Intake.PowerState.INTAKE);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    intake.setState(Intake.PositionState.DOWN);
                })
                .addSpatialMarker(new Vector2d(-25, 57), () -> {
                    drive.setPoseEstimate(new Pose2d(drive.getPoseEstimate().getX(),drive.getPoseEstimate().getY()+5,drive.getPoseEstimate().
                            getHeading()));
                })
                .splineToConstantHeading(new Vector2d(30, 57), Math.toRadians(180),
                        drive.getVelocityConstraint(30, 2.4, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineToConstantHeading(new Vector2d(-25, 57))
                .splineToConstantHeading(new Vector2d(-56, 35), Math.toRadians(180),
                        drive.getVelocityConstraint(30, 2.4, 15.06),
                        drive.getAccelerationConstraint(40))
                .build();
        TrajectorySequence rightBack = drive.trajectorySequenceBuilder(rightStack.end())
                .addSpatialMarker(new Vector2d(-10, 57), () ->  {
                    scheduler.scheduleTaskList(actions.raiseSlides(Slides.SlideState.AUTO_LOW));
                    intake.setState(Intake.PowerState.OFF);
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .addSpatialMarker(new Vector2d(40, 48), () ->  {
                    deposit.setState(Deposit.RotateState.ONE_EIGHTY);
                })
                .lineToConstantHeading(new Vector2d(-54, 36))
                .splineToConstantHeading(new Vector2d(-35, 57), Math.toRadians(0),
                        drive.getVelocityConstraint(30, 2.4, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineToConstantHeading(new Vector2d(24, 57))
                .splineToConstantHeading(new Vector2d(51, 29), Math.toRadians(0),
                        drive.getVelocityConstraint(30, 2.4, 15.06),
                        drive.getAccelerationConstraint(40))
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
        die(500);
        scheduler.scheduleTaskList(actions.scorePixels());
        die(500);


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

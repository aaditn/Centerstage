package org.firstinspires.ftc.teamcode.opmodes.auto;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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
import org.firstinspires.ftc.teamcode.util.AutoSelector;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
import org.opencv.core.Mat;

@Autonomous
public class farBlue extends EnhancedOpMode {
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
        drive.initLoop();
    }
    public void onStart()
    {
        drive.closeCameras();
    }
    public void linearOpMode() {
        offset=false;
        TrajectorySequence leftPurple = drive.trajectorySequenceBuilder(blueFarStart)
                .lineToLinearHeading(new Pose2d(-35, 20, Math.toRadians(180)))
                .build();
        TrajectorySequence leftToStack0 = drive.trajectorySequenceBuilder(leftPurple.end())
                .addTemporalMarker(1, () -> intake.setState(Intake.PositionState.DOWN))
                .lineToConstantHeading(new Vector2d(-31, 33))
                .splineToConstantHeading(new Vector2d(-45, 35), Math.toRadians(180))
                .lineToSplineHeading(new Pose2d(-56, 35, Math.toRadians(180)))
                .build();
        TrajectorySequence leftBackFromStack = drive.trajectorySequenceBuilder(leftToStack0.end())
                .addSpatialMarker(new Vector2d(-10, 57), () ->  {
                    scheduler.scheduleTaskList(actions.raiseSlides(Slides.SlideState.AUTO_LOW));
                    intake.setState(Intake.PowerState.OFF);
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .addSpatialMarker(new Vector2d(-48,48),() ->{
                    if(offset)drive.setPoseEstimate(new Pose2d(drive.getPoseEstimate().getX()-2.5,drive.getPoseEstimate().getY()-5,drive.getPoseEstimate().
                            getHeading()));

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
        TrajectorySequence leftStackFromBack = drive.trajectorySequenceBuilder(leftBackFromStack.end())

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


        TrajectorySequence midPurple = drive.trajectorySequenceBuilder(blueFarStart)
                .lineToLinearHeading(new Pose2d(-36, 35, Math.toRadians(270)))
                .build();
        TrajectorySequence midToStack0 = drive.trajectorySequenceBuilder(midPurple.end())
                .lineTo(new Vector2d(-36, 36))
                .splineToConstantHeading(new Vector2d(-40, 40), Math.toRadians(180))
                .splineToSplineHeading(new Pose2d(-58, 35, Math.toRadians(180)), Math.toRadians(180))
                .build();

        TrajectorySequence midBackFromStack = drive.trajectorySequenceBuilder(midToStack0.end())
                .addSpatialMarker(new Vector2d(0, 57), () ->  deposit.setState(Deposit.ClawState.CLOSED2))
                .addSpatialMarker(new Vector2d(20, 57), () ->  {
                    scheduler.scheduleTaskList(actions.raiseSlides(Slides.SlideState.AUTO_LOW));
                    intake.setState(Intake.PowerState.OFF);
                    intake.setState(Intake.ConveyorState.OFF);
                })

                .lineTo(new Vector2d(-58, 36))
                .splineToConstantHeading(new Vector2d(-35, 57), Math.toRadians(0),
                        drive.getVelocityConstraint(30, 1, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineToConstantHeading(new Vector2d(24, 57))
                .splineToConstantHeading(new Vector2d(51, 35), Math.toRadians(0),
                        drive.getVelocityConstraint(30, 1, 15.06),
                        drive.getAccelerationConstraint(40))
                .build();
        TrajectorySequence midStackFromBack = drive.trajectorySequenceBuilder(midBackFromStack.end())
                .addSpatialMarker(new Vector2d(-50, 57), () ->  {
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                })
                .splineToConstantHeading(new Vector2d(30, 57), Math.toRadians(180),
                        drive.getVelocityConstraint(30, 1, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineTo(new Vector2d(-35, 57))
                .splineToConstantHeading(new Vector2d(-58, 35), Math.toRadians(180),
                        drive.getVelocityConstraint(30, 1, 15.06),
                        drive.getAccelerationConstraint(40))
                .build();
        TrajectorySequence rightPurple = drive.trajectorySequenceBuilder(blueFarStart)
                .lineToLinearHeading(new Pose2d(-36, 35, Math.toRadians(270)))
                .build();
        TrajectorySequence rightToStack0 = drive.trajectorySequenceBuilder(midPurple.end())
                .lineTo(new Vector2d(-36, 38))
                .splineToSplineHeading(new Pose2d(-58, 35, Math.toRadians(180)), Math.toRadians(180))
                .build();

        TrajectorySequence rightBackFromStack = drive.trajectorySequenceBuilder(rightToStack0.end())
                .addSpatialMarker(new Vector2d(0, 57), () ->  deposit.setState(Deposit.ClawState.CLOSED2))
                .addSpatialMarker(new Vector2d(20, 57), () ->  {
                    scheduler.scheduleTaskList(actions.raiseSlides(Slides.SlideState.AUTO_LOW));
                    intake.setState(Intake.PowerState.OFF);
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .lineTo(new Vector2d(-58, 36))
                .splineToConstantHeading(new Vector2d(-35, 57), Math.toRadians(0),
                        drive.getVelocityConstraint(30, 1, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineToConstantHeading(new Vector2d(24, 57))
                .splineToConstantHeading(new Vector2d(51, 41), Math.toRadians(0),
                        drive.getVelocityConstraint(30, 1, 15.06),
                        drive.getAccelerationConstraint(40))
                .build();

        TrajectorySequence rightStackFromBack = drive.trajectorySequenceBuilder(rightBackFromStack.end())
                .addSpatialMarker(new Vector2d(-50, 57), () ->  {
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                })
                .splineToConstantHeading(new Vector2d(30, 57), Math.toRadians(180),
                        drive.getVelocityConstraint(30, 1, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineTo(new Vector2d(-35, 57))
                .splineToConstantHeading(new Vector2d(-58, 35), Math.toRadians(180),
                        drive.getVelocityConstraint(30, 1, 15.06),
                        drive.getAccelerationConstraint(40))
                .build();

        waitForStart();
        drive.setPoseEstimate(blueFarStart);
        drive.followTrajectorySequence(leftPurple);

        intake.setState(Intake.PositionState.DOWN);
        intake.setState(Intake.SweeperState.ONE_SWEEP);

        die(750);
        intake.setState(Intake.PositionState.MID);
        die(750);
        drive.followTrajectorySequence(leftToStack0);

        die(500);
        intake.setState(Intake.PowerState.INTAKE);
        intake.setState(Intake.ConveyorState.INTAKE);
        intake.setState(Intake.SweeperState.TWO_SWEEP);
        die(500);
        intake.setState(Intake.PositionState.RAISED);
        die(500);
        drive.followTrajectorySequence(leftBackFromStack);

        offset=true;
        die(500);
        scheduler.scheduleTaskList(actions.scorePixels());
        die(500);
        drive.followTrajectorySequence(leftStackFromBack);


        intake.setState(Intake.SweeperState.FOUR_SWEEP);
        die (1500);
        intake.setState(Intake.PositionState.RAISED);
        die(500);
        drive.followTrajectorySequence(leftBackFromStack);

        die(500);
        scheduler.scheduleTaskList(actions.scorePixels());
        die(500);

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
            Context.tel.addData("in wait", timer.milliseconds());
        }
    }

    @Override
    public void initialize() {

        this.setLoopTimes(10);
        drive=Robot.getInstance();
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
        deposit.setState(Deposit.ClawState.OPEN);
    }
}

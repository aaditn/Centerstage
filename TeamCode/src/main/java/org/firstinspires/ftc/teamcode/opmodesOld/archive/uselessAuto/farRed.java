package org.firstinspires.ftc.teamcode.opmodesOld.archive.uselessAuto;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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
@Disabled
@Autonomous
public class farRed extends EnhancedOpMode {
    Pose2d redFarStart = new Pose2d(-36,-61,Math.toRadians(90));
    Robot drive;
    Deposit deposit;
    Intake intake;
    Slides slides;
    DroneLauncher drone;
    boolean offset;
    TaskScheduler scheduler;
    RobotActions actions;
    public void linearOpMode()
    {

        TrajectorySequence leftPurple = drive.trajectorySequenceBuilder(redFarStart)
                .lineToLinearHeading(new Pose2d(-36, -30, Math.toRadians(180)))
                .build();
        TrajectorySequence prepTressFar = drive.trajectorySequenceBuilder(leftPurple.end())
                .lineToConstantHeading(new Vector2d(-34, -58))
                .build();
        TrajectorySequence goBackboard = drive.trajectorySequenceBuilder(prepTressFar.end())
                .lineToConstantHeading(new Vector2d(36, -58))
                .splineToConstantHeading(new Vector2d(40, -40), Math.toRadians(180))
                .build();


        TrajectorySequence leftToStack0 = drive.trajectorySequenceBuilder(leftPurple.end())
                .lineTo(new Vector2d(-34, -18))
                .splineToConstantHeading(new Vector2d(-42, -12), Math.toRadians(180))
                .lineToSplineHeading(new Pose2d(-56, -12, Math.toRadians(180)))
                .build();
        TrajectorySequence leftBackFromStack = drive.trajectorySequenceBuilder(leftToStack0.end())
                .lineToConstantHeading(new Vector2d(24, -12))
                .splineToConstantHeading(new Vector2d(48, -29), Math.toRadians(0))
                .build();
        TrajectorySequence leftStackFromBack = drive.trajectorySequenceBuilder(leftBackFromStack.end())
                .splineToConstantHeading(new Vector2d(24, -12), Math.toRadians(180))
                .lineToConstantHeading(new Vector2d(-56, -12))
                .build();

        TrajectorySequence midPurple = drive.trajectorySequenceBuilder(redFarStart)
                .lineToLinearHeading(new Pose2d(-36, -14, Math.toRadians(-90+1e-6)))
                .build();
        TrajectorySequence midToStack0 = drive.trajectorySequenceBuilder(midPurple.end())
                .lineTo(new Vector2d(-37, -13))
                .splineToConstantHeading(new Vector2d(-42, -12), Math.toRadians(180))
                .lineToSplineHeading(new Pose2d(-56, -12, Math.toRadians(180)))
                .build();
        TrajectorySequence midBackFromStack = drive.trajectorySequenceBuilder(midToStack0.end())
                .lineToConstantHeading(new Vector2d(24, -12))
                .splineToConstantHeading(new Vector2d(48, -35), Math.toRadians(0))
                .build();
        TrajectorySequence midStackFromBack = drive.trajectorySequenceBuilder(midBackFromStack.end())
                .splineToConstantHeading(new Vector2d(24, -12), Math.toRadians(180))
                .lineToConstantHeading(new Vector2d(-56, -12))
                .build();

        TrajectorySequence rightPurple = drive.trajectorySequenceBuilder(redFarStart)
                .lineToLinearHeading(new Pose2d(-36, -30, Math.toRadians(0)))
                .build();
        TrajectorySequence rightToStack0 = drive.trajectorySequenceBuilder(rightPurple.end())
                .lineTo(new Vector2d(-40, -22))
                .splineToSplineHeading(new Pose2d(-56, -12, Math.toRadians(180)), Math.toRadians(180))
                .build();
        TrajectorySequence rightBackFromStack = drive.trajectorySequenceBuilder(rightToStack0.end())
                .lineToConstantHeading(new Vector2d(24, -12))
                .splineToConstantHeading(new Vector2d(48, -41), Math.toRadians(0))
                .build();
        TrajectorySequence rightStackFromBack = drive.trajectorySequenceBuilder(rightBackFromStack.end())
                .splineToConstantHeading(new Vector2d(24, -12), Math.toRadians(180))
                .lineToConstantHeading(new Vector2d(-56, -12))
                .build();
        waitForStart();

        drive.setPoseEstimate(redFarStart);
        drive.followTrajectorySequence(leftPurple);

        intake.setState(Intake.PositionState.DOWN);
        intake.setState(Intake.SweeperState.ONE_SWEEP);

        die(500);
        intake.setState(Intake.PositionState.MID);
        die(500);

        drive.followTrajectorySequence(prepTressFar);
        //scheduler.scheduleTaskList(actions.autoRaiseSlides(0, Slides.SlideState.AUTO_LOW, Deposit.RotateState.PLUS_ONE_EIGHTY));
        drive.followTrajectorySequence(goBackboard);

        while(slides.isBusy())
        {

        }
        die(500);

        scheduler.scheduleTaskList(actions.scorePixels());

        /*die(500);
        drive.followTrajectorySequence(leftStackFromBack);

        intake.setState(Intake.SweeperState.FOUR_SWEEP);
        die (1500);
        intake.setState(Intake.PositionState.RAISED);
        die(500);
        drive.followTrajectorySequence(leftBackFromStack);

        die(500);
        scheduler.scheduleTaskList(actions.scorePixels());
        die(500);*/
    }

    public void die (int milliseconds) {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        while (timer.milliseconds() < milliseconds&&opModeIsActive()) {
            Context.tel.addData("in wait", timer.milliseconds());
        }
    }
    @Override
    public void initialize()
    {
        this.setLoopTimes(10);
        drive=Robot.getInstance();
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
        deposit.setState(Deposit.ClawState.OPEN);

    }

    public void onStart()
    {
        drive.closeCameras();
    }
    public void initLoop()
    {
        drive.initLoop();
    }
    @Override
    public void primaryLoop() {
        drive.primaryLoop();
    }
}

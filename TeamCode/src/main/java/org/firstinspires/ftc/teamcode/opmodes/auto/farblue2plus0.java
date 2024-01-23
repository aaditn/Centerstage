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

@Autonomous
public class farblue2plus0 extends EnhancedOpMode {
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
        TrajectorySequence rightPurple = drive.trajectorySequenceBuilder(blueFarStart)
                .splineToConstantHeading(new Vector2d(-44, 35),Math.toRadians(270))
                .build();
        TrajectorySequence rightPurpleToTurn = drive.trajectorySequenceBuilder(rightPurple.end())
                        .lineToLinearHeading(new Pose2d(-36,57,Math.toRadians(0)))
                .build();
        TrajectorySequence rightPurpleToBack = drive.trajectorySequenceBuilder(rightPurple.end())
                .lineToConstantHeading(new Vector2d(20,57))
                .addSpatialMarker(new Vector2d(-10, 57), () ->  {
                    scheduler.scheduleTaskList(actions.raiseSlides(Slides.SlideState.AUTO_LOW));
                    intake.setState(Intake.PowerState.OFF);
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .splineToConstantHeading(new Vector2d(53, 26), Math.toRadians(0))
                .build();


        waitForStart();
        drive.setPoseEstimate(blueFarStart);
        drive.followTrajectorySequenceAsync(rightPurple);
        waitForDrive();
        intake.setState(Intake.PositionState.DOWN);
        intake.setState(Intake.SweeperState.ONE_SWEEP);
        drive.followTrajectorySequence(rightPurpleToTurn);
        waitForDrive();
        drive.followTrajectorySequence(rightPurpleToBack);
        waitForDrive();
        die(500);
        scheduler.scheduleTaskList(actions.scorePixels());
       



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
public void waitForDrive(){
    while(drive.isBusy()&&!isStopRequested()){

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

package org.firstinspires.ftc.teamcode.opmodes.auto;


import static org.firstinspires.ftc.teamcode.Robot.getTaskList;
import static org.firstinspires.ftc.teamcode.auto_paths.door_paths.closeRed2_6.redCloseStart;
import static org.firstinspires.ftc.teamcode.auto_paths.door_paths.closeRed2_6.trajectories;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.auto_paths.Batch;
import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.DroneLauncher;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.RobotActions;
import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.util.NamedTrajectory;
import org.firstinspires.ftc.teamcode.util.enums.Paths;

import java.util.List;

@Autonomous(name = "Close 2+6")
public class closeRed2_6 extends EnhancedOpMode {
    Robot drive;
    TaskScheduler scheduler;
    RobotActions actions;
    Deposit deposit;
    Intake intake;
    Slides slides;
    DroneLauncher drone;

    private List<Task>[] auto_tasks() {
        return getTaskList(
                actions.yellowDrop(53, -40, Slides.SlideState.HALF, true),
                actions.lowerIntake(0, -48, 1),
                actions.yellowDropCycles(46, 0,  Slides.SlideState.HALF, Deposit.RotateState.PLUS_NINETY),
                actions.lowerIntake(0, -48, 2),
                actions.yellowDropCycles(46, 0, Slides.SlideState.R1, Deposit.RotateState.PLUS_NINETY),
                actions.lowerIntake(0, -48, 3),
                actions.yellowDropCycles(46, 10, Slides.SlideState.R2, Deposit.RotateState.MINUS_FOURTY_FIVE)
        );
    }
    @Override
    public void linearOpMode() {
        ElapsedTime timeToInit3 = new ElapsedTime();
        RobotLog.e("Static Force Initialization Begins");
        List<NamedTrajectory[][]> batchInit6 = Batch.allTrajectories;
        RobotLog.e("All Trajectories Loaded in: " + timeToInit3.seconds()
                + ", Mark: " + batchInit6.toString() + ",Key: " + batchInit6.get(0)[0][0].getName());
        List<Pose2d> batchInitPT26 = Batch.allStarts;
        RobotLog.e("All Start Pos Loaded in: " + timeToInit3.seconds() + ", Mark: " + batchInitPT26.toString());
        waitForStart();
        RobotLog.e("Loggy");

        if (opModeIsActive()) {
            RobotLog.e("Just log it");
            drive.setPoseEstimate(redCloseStart);
            RobotLog.e("Just log it twice");
            delayLinear((long) Context.autoWaitTime * 1000);
            RobotLog.e("things not mapped");
            drive.set(trajectories, auto_tasks());
            RobotLog.e("things mapped");
            drive.run(Paths.Purple);
            RobotLog.e("delaying");
            drive.run(Paths.Go_To_Stack);
            drive.run(Paths.Score_First);

            drive.run(Paths.Return_to_Stack);

            drive.run(Paths.Score_Second);

            drive.run(Paths.Return_to_Stack_Again);

            drive.run(Paths.Score_Third);

            waitForEnd();
            RobotLog.e("end");
        }
    }
    public void initLoop()
    {
        drive.initLoop();
    }

    public void onStart() {

        drive.closeCameras();
    }

    @Override
    public void initialize() {
        this.setLoopTimes(10);
        drive = Robot.getInstance();

        Context.isTeamRed = true;
        scheduler = new TaskScheduler();
        actions = RobotActions.getInstance();
        deposit = drive.deposit;
        intake = drive.intake;
        slides = drive.slides;
        drone = drive.droneLauncher;

        intake.init();
        deposit.init();
        slides.init();
        drone.init();
        intake.setOperationState(Module.OperationState.PRESET);
        deposit.setState(Deposit.FlipState.TRANSFER);
        deposit.setState(Deposit.ClawState.CLOSED_AGAIN);
        intake.setState(Intake.SweeperState.ZERO);
        //RobotLog.e("ROLLIT HAHAHAHAHAHAHAHAHAHAHAHA");
    }

    @Override
    public void primaryLoop() {
        drive.primaryLoop();
    }


}

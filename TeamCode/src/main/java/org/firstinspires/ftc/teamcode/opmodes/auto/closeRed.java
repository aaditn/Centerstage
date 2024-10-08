package org.firstinspires.ftc.teamcode.opmodes.auto;


import static org.firstinspires.ftc.teamcode.Robot.getTaskList;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.auto_paths.closeRedPath;
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

@Autonomous(name = "Close Red")
public class closeRed extends EnhancedOpMode {
    public static Robot drive;
    TaskScheduler scheduler;
    RobotActions actions;
    Deposit deposit;
    Intake intake;
    Slides slides;
    DroneLauncher drone;
    private List<Task>[] auto_tasks() {
        return getTaskList(
                actions.deployPurple(1100, 1100, 1100),
                actions.autoDrop(35, -15, Slides.SlideState.R1, Deposit.RotateState.PLUS_NINETY),
                actions.lowerIntake(0, -48.5, 1, true),
                actions.autoDrop(37, -23,  Slides.SlideState.R4, Deposit.RotateState.ZERO),
                actions.lowerIntake(0, -48.5, 2, true),
                actions.autoDrop(37, -23,  Slides.SlideState.R4, Deposit.RotateState.ZERO),
                actions.lowerIntake(0, -48.5, 3, true),
                actions.autoDrop(37, -23,  Slides.SlideState.R4, Deposit.RotateState.ZERO)
//                actions.lowerIntake(-30, -51.5, 2, true),
//                actions.autoDrop(46, -15, Slides.SlideState.R1, Deposit.RotateState.PLUS_NINETY)
//                actions.scorePixels(48.5, TeleOpRewrite.DepositState.RIGHT, -38, Slides.SlideState.ROW2)
        );
    }

    @Override
    public void linearOpMode() {

        ElapsedTime time = new ElapsedTime();

        RobotLog.e("Starting Time Trajectory" + time.milliseconds());
        closeRedPath pathObj = new closeRedPath();
        NamedTrajectory[][] trajectories = pathObj.trajectories;
        RobotLog.e("Ending Time Trajectory" + time.milliseconds());
        RobotLog.e("Static Force Initialization Begins");

// 608.74777
        waitForStart();

        if(opModeIsActive())
        {

           /* for (NamedTrajectory[] nArr: trajectories) {
                for (NamedTrajectory n: nArr) {
                    RobotLog.e("size" + n.getAction().size());
                }
            }*/


            drive.pose = closeRedPath.closeRedStart;
            delayLinear((long)Context.autoWaitTime*1000);
            drive.set(trajectories,auto_tasks());
            RobotLog.e("things mapped");

            drive.run(Paths.Purple);
            drive.run(Paths.Yellow);
            drive.run(Paths.Stack1);
            drive.run(Paths.Back1);
            drive.run(Paths.Stack1);
            drive.run(Paths.Back1);
            drive.run(Paths.Stack1);
            drive.run(Paths.Back1);




            waitForEnd();
            RobotLog.e("end");
        }
    }

    public void initLoop()
    {
        drive.initLoop();
    }

    public void onStart() {
       // drive.closeCameras();
    }


    @Override
    public void initialize() {
        this.setLoopTimes(10);

        drive = Robot.getInstance();

        Context.isTeamRed = true;
        scheduler = new TaskScheduler();
        deposit = drive.deposit;
        intake = drive.intake;
        slides = drive.slides;
        drone = drive.droneLauncher;

        intake.init();
        deposit.init();
        slides.init();
        drone.init();
        intake.setOperationState(Module.OperationState.PRESET);
        intake.setState(Intake.SweeperState.ZERO);
        deposit.setState(Deposit.FlipState.TRANSFER);
        deposit.setState(Deposit.ClawState.OPEN);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        actions = RobotActions.getInstance();

        deposit.setState(Deposit.FlipState.PRELOAD);
        deposit.setState(Deposit.WristState.PRELOAD);
        deposit.setState(Deposit.RotateState.ZERO);
        deposit.setState(Deposit.ClawState.CLOSED1);

        intake.setState(Intake.PositionState.RAISED);




      /*  vision.initAprilTag(hardwareMap);

        int count = 0;

        vision.setEstHeading(drive.pose.heading.toDouble());
        previous.add(new Pose2d(0,0,0));
        previous.add(new Pose2d(0,0,0));
        previous.add(new Pose2d(0,0,0));
        previous.add(new Pose2d(0,0,0));
        previous.add(new Pose2d(0,0,0));


        previous.add(new Pose2d(0,0,0));

       */
        //RobotLog.e("ROLLIT HAHAHAHAHAHAHAHAHAHAHAHA");
    }

    @Override
    public void primaryLoop() {
        drive.primaryLoop();
    }


}

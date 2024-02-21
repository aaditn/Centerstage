package org.firstinspires.ftc.teamcode.opmodes.auto;


import static org.firstinspires.ftc.teamcode.Robot.getTaskList;
import static org.firstinspires.ftc.teamcode.auto_paths.door_paths.farBlueDoor.blueFarStart;
import static org.firstinspires.ftc.teamcode.auto_paths.door_paths.farBlueDoor.trajectories;

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
import org.firstinspires.ftc.teamcode.opmodes.tele.TeleOpRewrite;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.AutoSelector;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.util.NamedTrajectory;
import org.firstinspires.ftc.teamcode.util.enums.Paths;

import java.util.List;

@Autonomous(name = "Far Blue Door")
public class farBlueDoor extends EnhancedOpMode {
    Robot drive;
    TaskScheduler scheduler;
    RobotActions actions;
    Deposit deposit;
    Intake intake;
    Slides slides;
    DroneLauncher drone;

    private List<Task>[] auto_tasks() {
        return getTaskList(
                actions.deployPurple(39, 11, 23),
                actions.yellowDrop(49, -15, Context.autonYellowHeight),
                actions.lowerIntake(0, -52.5, 0),
                actions.scorePixels(49, TeleOpRewrite.DepositState.LEFT),
                actions.lowerIntake(0, -52.5, 1),
                actions.scorePixels(49, TeleOpRewrite.DepositState.LEFT, true)
        );
    }
    @Override
    public void linearOpMode() {
        ElapsedTime timeToInit3 = new ElapsedTime();
        RobotLog.e("Static Force Initialization Begins");
        List<NamedTrajectory[][]> batchInit6  = Batch.allTrajectories;
        RobotLog.e("All Trajectories Loaded in: "+ timeToInit3.seconds()
                +", Mark: " + batchInit6.toString() +",Key: "+batchInit6.get(0)[0][0].getName());
        List<Pose2d> batchInitPT26 = Batch.allStarts;
        RobotLog.e("All Start Pos Loaded in: "+timeToInit3.seconds()+", Mark: " +batchInitPT26.toString());
        waitForStart();

        if(opModeIsActive())
        {
            drive.setPoseEstimate(blueFarStart);
            delayLinear((long)Context.autoWaitTime*1000);
            drive.set(trajectories,auto_tasks());
            RobotLog.e("things mapped");
            drive.run(Paths.Purple);
            RobotLog.e("purple");
            drive.run(Paths.Score_Spike);
            RobotLog.e("yellow");
            delayLinear(550);
            RobotLog.e("delaying");
            if(!Context.autoState.equals(AutoSelector.CyclePixelCount.ZERO) && !intake.pixelsPresent()) {
                RobotLog.e("color sensor "+intake.pixelsPresent());
                drive.run(Paths.Go_To_Stack);
                delayLinear(750);
                drive.run(Paths.Score_First);
                delayLinear(330);
                if(!Context.autoState.equals(AutoSelector.CyclePixelCount.TWO) && !intake.pixelsPresent()) {
                    RobotLog.e("color sensor "+intake.pixelsPresent());
                    drive.run(Paths.Return_to_Stack);
                    delayLinear(750);
                    drive.run(Paths.Score_Second);
                }
            }
            delayLinear(100);
            if (Context.parkSide.equals(AutoSelector.ParkSide.LEFT))
            {
                drive.run(Paths.ParkLeft);
            } else if (Context.parkSide.equals(AutoSelector.ParkSide.RIGHT)) {
                drive.run(Paths.ParkRight);
            }
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

        Context.isTeamRed = false;
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
        intake.setState(Intake.SweeperState.INIT);
        deposit.setState(Deposit.FlipState.TRANSFER);
        deposit.setState(Deposit.ClawState.CLOSED1);

        //RobotLog.e("ROLLIT HAHAHAHAHAHAHAHAHAHAHAHA");
    }

    @Override
    public void primaryLoop() {
        drive.primaryLoop();
    }


}

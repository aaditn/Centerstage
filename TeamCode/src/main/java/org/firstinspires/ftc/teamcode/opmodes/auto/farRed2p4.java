package org.firstinspires.ftc.teamcode.opmodes.auto;


import static org.firstinspires.ftc.teamcode.Robot.getTaskList;
import static org.firstinspires.ftc.teamcode.auto_paths.farRed.*;
import static org.firstinspires.ftc.teamcode.util.WhipTrajectory.map;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.DroneLauncher;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.RobotActions;
import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.opmodes.tele.TeleOpRewrite;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.util.WhipTrajectory;
import org.firstinspires.ftc.teamcode.util.enums.Paths;

import java.util.List;

@Autonomous(name = "Far Red 2+0")
public class farRed2p4 extends EnhancedOpMode {
    Robot drive;
    TaskScheduler scheduler;
    RobotActions actions;
    Deposit deposit;
    Intake intake;
    Slides slides;
    DroneLauncher drone;

    List<WhipTrajectory> trajectories;
    public List<List<Task>> auto_tasks(double purple_y_pos){
        return getTaskList(
                actions.deployPurple(purple_y_pos),
                actions.yellowDrop(49),
                actions.lowerIntake(-50,-56.5,0),
                actions.scorePixels(49, TeleOpRewrite.DepositState.RIGHT),
                actions.lowerIntake(-50,-56.5,1),
                actions.scorePixels(49, TeleOpRewrite.DepositState.RIGHT)
        );
    }
    @Override
    public void linearOpMode() {
        waitForStart();
        drive.setPoseEstimate(redFarStart);
        switch (Context.dice) {
            case MIDDLE:
                drive.trajectories = map(midTrajectories,auto_tasks(35));
                break;
            case RIGHT:
                drive.trajectories = map(rightTrajectories, auto_tasks(47));
                break;
            default:
                drive.trajectories = map(leftTrajectories, auto_tasks(35));
                break;
        }
        drive.run(Paths.Purple);
        drive.run(Paths.Score_Spike);
        delayLinear(250);
        drive.run(Paths.Go_To_Stack);
        delayLinear(750);
        drive.run(Paths.Score_First);
        delayLinear(250);
        drive.run(Paths.Return_to_Stack);
        delayLinear(750);
        drive.run(Paths.Score_Second);
        waitForEnd();

    }

    public void initLoop() {
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
    }

    @Override
    public void primaryLoop() {
        drive.primaryLoop();
    }


}

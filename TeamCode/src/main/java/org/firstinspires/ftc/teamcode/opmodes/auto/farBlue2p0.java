package org.firstinspires.ftc.teamcode.opmodes.auto;


import static org.firstinspires.ftc.teamcode.auto_paths.farBlue.blueFarStart;
import static org.firstinspires.ftc.teamcode.auto_paths.farBlue.leftTrajectories;
import static org.firstinspires.ftc.teamcode.auto_paths.farBlue.midTrajectories;
import static org.firstinspires.ftc.teamcode.auto_paths.farBlue.rightTrajectories;
import static org.firstinspires.ftc.teamcode.util.WhipTrajectory.map;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Robot;
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
import org.firstinspires.ftc.teamcode.util.WhipTrajectory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Autonomous(name = "Far Blue 2+0")
public class farBlue2p0 extends EnhancedOpMode {
    Robot drive;
    TaskScheduler scheduler;
    RobotActions actions;
    Deposit deposit;
    Intake intake;
    Slides slides;
    DroneLauncher drone;

    @Override
    public void linearOpMode() {
        waitForStart();
        drive.setPoseEstimate(blueFarStart);
        List<WhipTrajectory> trajectories;
        switch (Context.dice) {
            case MIDDLE:
                trajectories = map(Arrays.asList(midTrajectories), getTaskList(35, 49));
                break;
            case RIGHT:
                trajectories = map(Arrays.asList(rightTrajectories), getTaskList(35, 49));
                break;
            default:
                trajectories = map(Arrays.asList(leftTrajectories), getTaskList(35, 49));
                break;
        }
        drive.followTrajectory(trajectories.get(0));
        drive.followTrajectory(trajectories.get(1));

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

    public List<List<Task>> getTaskList(double purpleYPos, double yellowXPos) {
        return new ArrayList<List<Task>>() {
            {
                add(actions.deployPurple(purpleYPos));
                add(actions.yellowDrop(yellowXPos));
            }
        };
    }
}

package org.firstinspires.ftc.teamcode.opmodes.auto;


import static org.firstinspires.ftc.teamcode.Robot.getTaskList;
import static org.firstinspires.ftc.teamcode.auto_paths.door_paths.farBlueDoor.blueFarStart;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

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
import org.firstinspires.ftc.teamcode.auto_paths.door_paths.farBlueDoor;

import java.util.List;

@Autonomous(name = "Far Blue Door Test Cause Lazy")
public class farBlueDoorTestCauseLazy extends EnhancedOpMode {
    Robot drive;
    TaskScheduler scheduler;
    RobotActions actions;
    Deposit deposit;
    Intake intake;
    Slides slides;
    DroneLauncher drone;

    private List<Task>[] auto_tasks() {
        return getTaskList(
                actions.deployPurple(35, 46, 35),
                actions.yellowDrop(32),
                actions.lowerIntake(-50, -56.5, 0),
                actions.scorePixels(49, TeleOpRewrite.DepositState.RIGHT),
                actions.lowerIntake(-50, -56.5, 1),
                actions.scorePixels(49, TeleOpRewrite.DepositState.RIGHT)
        );
    }
    @Override
    public void linearOpMode() {

        waitForStart();
        drive.setPoseEstimate(blueFarStart);
        drive.followTrajectorySequenceAsync(farBlueDoor.midPurple);
        wait (1000);
        drive.followTrajectorySequence(farBlueDoor.midPurpleToBack);
        wait (1000);
        drive.followTrajectorySequence(farBlueDoor.midBackToStack);
        wait (1000);
        drive.followTrajectorySequence(farBlueDoor.stackToBack1);
        wait (1000);
        drive.followTrajectorySequence(farBlueDoor.backToStack1);
        wait (1000);
        drive.followTrajectorySequence(farBlueDoor.stackToBack2);
        wait (1000);

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

    public void wait(int mill) {
        while (drive.isBusy() && !isStopRequested()) {}
        ElapsedTime timer = new ElapsedTime();
        while (timer.milliseconds() < mill) {}
    }


}

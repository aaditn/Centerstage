package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.DroneLauncher;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.RobotActions;
import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
@Autonomous(name="Far Blue 2+0")
public class farBlue extends EnhancedOpMode
{
    Robot drive;
    TaskScheduler scheduler;
    RobotActions actions;
    Deposit deposit;
    Intake intake;
    Slides slides;
    DroneLauncher drone;

    @Override
    public void linearOpMode()
    {
        waitForStart();
        drive.setPoseEstimate(org.firstinspires.ftc.teamcode.auto_paths.farBlue.farBlue.blueFarStart);

        delayLinear(Context.autoWaitTime* 1000);

        switch(Context.dice)
        {
            case LEFT:
                drive.followTrajectorySequence(org.firstinspires.ftc.teamcode.auto_paths.farBlue.farBlue.leftPurple,actions.deployPurple(35));
                break;
            case MIDDLE:
                drive.followTrajectorySequence(org.firstinspires.ftc.teamcode.auto_paths.farBlue.farBlue.midPurple,actions.deployPurple(35));

                break;
            case RIGHT:
                drive.followTrajectorySequence(org.firstinspires.ftc.teamcode.auto_paths.farBlue.farBlue.rightPurple,actions.deployPurple(47));

                break;
        }
        switch(Context.dice)
        {
            case LEFT:
                drive.followTrajectorySequence(org.firstinspires.ftc.teamcode.auto_paths.farBlue.farBlue.leftPurpleToBack,actions.yellowDrop(49));

                break;
            case MIDDLE:
                drive.followTrajectorySequence(org.firstinspires.ftc.teamcode.auto_paths.farBlue.farBlue.midPurpleToBack,actions.yellowDrop(49));

                break;
            case RIGHT:
                drive.followTrajectorySequence(org.firstinspires.ftc.teamcode.auto_paths.farBlue.farBlue.rightPurpleToBack ,actions.yellowDrop(49));
                break;
        }

    }
    public void initLoop()
    {
        drive.initLoop();
    }
    public void onStart()
    {
        drive.closeCameras();
    }

    @Override
    public void initialize()
    {
        this.setLoopTimes(10);
        drive= Robot.getInstance();

        Context.isTeamRed=false;
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
        deposit.setState(Deposit.FlipState.TRANSFER);
        deposit.setState(Deposit.ClawState.CLOSED1);
    }

    @Override
    public void primaryLoop()
    {
        drive.primaryLoop();
    }
}

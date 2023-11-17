package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.DroneLauncher;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskListBuilder;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

import java.util.List;

public class ModuleTeleop extends EnhancedOpMode
{
    Robot robot;
    Intake intake;
    Deposit deposit;
    Slides slides;
    DroneLauncher droneLauncher;

    TaskListBuilder builder;
    List<Task> testTaskList;
    TaskScheduler scheduler;

    @Override
    public void linearOpMode()
    {
        waitForStart();

        while(opModeIsActive())
        {
            if(gamepad1.a)
            {
                deposit.setState(Deposit.RotationState.TRANSFER);
            }
            else if(gamepad1.b)
            {
                deposit.setState(Deposit.RotationState.DEPOSIT_HIGH);
            }
            else if(gamepad1.x)
            {
                intake.setState(Intake.PositionState.HIGH);
            }
            else if(gamepad1.y)
            {
                intake.setState(Intake.PositionState.TELE);
            }
            else if(gamepad1.right_bumper)
            {
                deposit.setState(Deposit.PusherState.IN);
            }
            else if(gamepad1.left_bumper)
            {
                deposit.setState(Deposit.PusherState.TWO);
            }

        }
    }

    @Override
    public void initialize()
    {
        this.setLoopTimes(0);

        robot=new Robot(this);
        builder=new TaskListBuilder(this);
        scheduler=new TaskScheduler();

        deposit=robot.deposit;
        intake=robot.intake;
        //slides=robot.slides;
        droneLauncher=robot.droneLauncher;

        intake.init();
        deposit.init();
        intake.setOperationState(Module.OperationState.MANUAL);
    }

    @Override
    public void initLoop()
    {
        robot.initLoop();
    }

    @Override
    public void primaryLoop()
    {
        robot.primaryLoop();
    }
}

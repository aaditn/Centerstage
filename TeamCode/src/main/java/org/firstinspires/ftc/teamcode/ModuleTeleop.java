package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskListBuilder;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

import java.util.List;

public class ModuleTeleop extends EnhancedOpMode
{
    Intake intake;

    Deposit deposit;
    //Slides slides;
    TaskListBuilder builder;
    List<Task> testTaskList;
    TaskScheduler scheduler;

    @Override
    public void linearOpMode()
    {

    }

    @Override
    public void initialize()
    {
        Context.resetValues();
        Context.tel=new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);

        this.setLoopTimes(0);
        builder=new TaskListBuilder(this);
        deposit=new Deposit(hardwareMap);
        intake=new Intake(hardwareMap);

        intake.init();
        deposit.init();
    }

    @Override
    public void primaryLoop()
    {

    }
}

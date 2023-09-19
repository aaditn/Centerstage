package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.modules.HangExample;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.SampleModule;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskList;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskListBuilder;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

import java.util.List;

@TeleOp
public class TestOpMode2 extends EnhancedOpMode
{
    double test=0;
    double test2=0;
    ElapsedTime linearTimer;
    ElapsedTime coroutineTimer;

    TaskListBuilder builder;
    List<Task> testTaskList;
    TaskScheduler scheduler;

    Module Hang;

    @Override
    public void linearOpMode()
    {
        testTaskList=builder.createNew()
                .await(()->opModeIsActive())
                .delay(100)
                .moduleAction(Hang, HangExample.State.IN)
                .delay(2000)
                .moduleAction(Hang, HangExample.State.OUT)
                .delay(1000)
                .moduleAction(Hang, HangExample.State.OFF)
                .build();

        waitForStart();

        scheduler.scheduleTaskList(testTaskList);
    }

    @Override
    public void initialize()
    {
        Context.resetValues();
        Context.tel=new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);
        linearTimer=new ElapsedTime();
        coroutineTimer=new ElapsedTime();

        builder=new TaskListBuilder(this);
        scheduler=new TaskScheduler();

        Hang=new HangExample(hardwareMap, Context.tel);

        this.setLoopTimes(10);
    }

    @Override
    public void initLoop()
    {
        test++;
        Context.tel.addData("help", test);
        Context.tel.update();
    }

    @Override
    public void primaryLoop()
    {
        Context.tel.addData("hang state", Hang.getState());
        Context.tel.addData("hang status", Hang.isBusy());
        Context.tel.update();
        Hang.updateLoop();
        Hang.writeLoop();
    }
}

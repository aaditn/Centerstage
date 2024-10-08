package org.firstinspires.ftc.teamcode.modules.moduleTesting;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.modules.modulesOld.HangExample;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.Builder;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.util.Tel;

import java.util.List;

@TeleOp
public class TestOpMode2 extends EnhancedOpMode
{
    double test=0;
    double test2=0;
    ElapsedTime linearTimer;
    ElapsedTime coroutineTimer;

    Builder builder;
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

        builder=new Builder(this);
        scheduler=new TaskScheduler();

        Hang=new HangExample(hardwareMap, Context.tel);

        this.setLoopTimes(10);
    }

    @Override
    public void initLoop()
    {
        test++;
        Tel.instance().addData("help", test);
        Tel.instance().update();
    }

    @Override
    public void primaryLoop()
    {
        Tel.instance().addData("hang state", Hang.getState());
        Tel.instance().addData("hang status", Hang.isBusy());
        Tel.instance().update();
        Hang.updateLoop();
        Hang.writeLoop();
    }
}

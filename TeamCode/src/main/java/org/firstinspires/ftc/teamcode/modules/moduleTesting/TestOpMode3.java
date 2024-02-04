package org.firstinspires.ftc.teamcode.modules.moduleTesting;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskListBuilder;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.util.Tel;

import java.util.List;


@TeleOp
public class TestOpMode3 extends EnhancedOpMode
{
    ModuleTest test;
    boolean help=false;

    TaskListBuilder builder;
    List<Task> testTaskList;


    TaskScheduler scheduler;

    @Override
    public void linearOpMode()
    {
        testTaskList=builder.createNew()
                .moduleAction(test, ModuleTest.State.FWD)
                //.executeCode(()->test.setState(ModuleTest.State.FWD))
                .delay(1000)
                .moduleAction(test, ModuleTest.State.OFF)
                //.executeCode(()->test.setState(ModuleTest.State.OFF))
                .delay(500)
                .moduleAction(test, ModuleTest.State.FWD, 1000)
                //.executeCode(()->test.setState(ModuleTest.State.FWD, 1000))
                //.await(()->!test.isBusy())
                .awaitPreviousModuleActionCompletion()
                //.await(()->!test.isBusy())
                .moduleAction(test, ModuleTest.State.OFF)
                .executeCode(()->help=true)
                .build();

        waitForStart();

        scheduler.scheduleTaskList(testTaskList);



    }

    @Override
    public void initialize()
    {
        Context.resetValues();
        Context.tel=new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);
        this.setLoopTimes(10);
        test=new ModuleTest(hardwareMap);

        scheduler=new TaskScheduler();
        builder=new TaskListBuilder(this);
    }

    @Override
    public void initLoop()
    {
        test.updateLoop();
        Tel.instance().addData("help", help);
        Tel.instance().addData("task list size", testTaskList.size());
        Tel.instance().update();
    }

    @Override
    public void primaryLoop()
    {
        test.updateLoop();
        test.writeLoop();
        Tel.instance().addData("help", help);
        Tel.instance().addData("motor power", test.currentMotorPower);
        Tel.instance().addData("task list size", testTaskList.size());
        Tel.instance().update();
    }
}

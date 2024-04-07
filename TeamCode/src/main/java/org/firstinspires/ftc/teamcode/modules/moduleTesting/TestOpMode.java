package org.firstinspires.ftc.teamcode.modules.moduleTesting;

import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.*;



import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.modulesOld.SampleModule;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.Builder;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

import java.util.List;

public class TestOpMode extends EnhancedOpMode
{
    SampleModule m;
    Builder builder;
    List<Task> testTaskList;
    TaskScheduler scheduler;

    Robot r;

    @Override
    public void linearOpMode()
    {
        GamepadEx primary=new GamepadEx(gamepad1);
        ButtonReader B=new ButtonReader(primary, GamepadKeys.Button.B);
        scheduler=new TaskScheduler();

        testTaskList=builder.createNew()
                .await(()->opModeIsActive())
                .moduleAction(m, SampleModule.State1.BYE)
                .awaitPreviousModuleActionCompletion()
                .moduleAction(m, SampleModule.State2.HELLO)
                .awaitButtonPress(B)
                .moduleAction(m, SampleModule.State1.HI)
                .delay(100)
                .executeCode(
                        ()->scheduler.scheduleTaskList(testTaskList)
                )
                .build();

        scheduler.scheduleTaskList(testTaskList);
    }

    @Override
    public void initialize()
    {
        this.setLoopTimes(10);
        builder=new Builder(this);
        r=new Robot(this);
    }

    @Override
    public void initLoop()
    {
        r.initLoop();
    }

    public void onStart()
    {
        //do something idk(maybe take signal sleeve snapshot or wtv)
    }

    @Override
    public void primaryLoop()
    {
        r.primaryLoop();
    }

    @Override
    public void onEnd()
    {
        r.onEnd();
    }
}

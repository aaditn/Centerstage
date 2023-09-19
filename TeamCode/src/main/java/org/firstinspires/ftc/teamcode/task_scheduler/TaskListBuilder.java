package org.firstinspires.ftc.teamcode.task_scheduler;

import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class TaskListBuilder
{
    List<Task> tasks=new ArrayList<Task>();
    Module lastModuleCalled;
    ModuleState lastModuleStateCalled;
    LinearOpMode l;

    public TaskListBuilder(LinearOpMode l)
    {
        this.l=l;
    }

    public TaskListBuilder createNew()
    {
        tasks=new ArrayList<Task>();
        return this;
    }

    public TaskListBuilder executeCode(codeExecutable task)
    {
        tasks.add(new ExecutionTask(task));
        return this;
    }

    public TaskListBuilder moduleAction(Module m, ModuleState s)
    {
        tasks.add(new ExecutionTask(()->m.setState(s)));
        return this;
    }

    public TaskListBuilder moduleAction(Module m, ModuleState s, int timeout)
    {
        tasks.add(new ExecutionTask(()->m.setState(s, timeout)));
        return this;
    }

    public TaskListBuilder await(Callable<Boolean> runCondition)
    {
        tasks.add(new AwaitTask(runCondition));
        return this;
    }

    public TaskListBuilder awaitPreviousModuleActionCompletion()
    {
        tasks.add(new AwaitTask(()->!lastModuleCalled.isBusy()&&lastModuleCalled.getState()==lastModuleStateCalled));
        return this;
    }

    public TaskListBuilder delay(long delay)
    {
        tasks.add(new DelayTask(delay));
        return this;
    }

    public TaskListBuilder awaitButtonPress(ButtonReader b)
    {
        tasks.add(new AwaitTask(()->b.wasJustPressed()));
        return this;
    }

    public List<Task> build()
    {
        //tasks.clear();
        return tasks;
    }
}

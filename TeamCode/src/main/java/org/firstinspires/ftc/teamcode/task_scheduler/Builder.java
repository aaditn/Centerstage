package org.firstinspires.ftc.teamcode.task_scheduler;

import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.enums.Compare;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Builder
{
    List<Task> tasks= new ArrayList<>();
    Module lastModuleCalled;
    ModuleState lastModuleStateCalled;
    LinearOpMode l;

    Robot drive;

    public Builder(LinearOpMode l)
    {
        this.l=l;
    }

    public Builder(LinearOpMode l, Robot drive)
    {
        this.l=l;
        this.drive=drive;
    }

    public Builder createNew()
    {
        tasks=new ArrayList<>();
        return this;
    }

    public static Builder create()
    {
        return new Builder(Context.opmode, Robot.getInstance());
    }
    public static Builder create(LinearOpMode l, Robot drive)
    {
        return new Builder(l, drive);
    }

    public Builder executeCode(codeExecutable task)
    {
        tasks.add(new ExecutionTask(task));
        return this;
    }

    public Builder moduleAction(Module m, ModuleState s)
    {
        tasks.add(new ExecutionTask(()->m.setState(s)));
        lastModuleCalled=m;
        lastModuleStateCalled=s;
        return this;
    }

    public Builder moduleAction(Module m, ModuleState s, int timeout)
    {

        tasks.add(new ExecutionTask(()->m.setState(s, timeout)));
        lastModuleCalled=m;
        lastModuleStateCalled=s;
        return this;
    }

    public Builder conditionalModuleAction(Module m, ModuleState s, Callable<Boolean> conditional)
    {
        tasks.add(new ExecutionTask(()->{
            if(conditional.call())
            {
                m.setState(s);
            }
        }));
        lastModuleCalled=m;
        lastModuleStateCalled=s;
        return this;
    }

    public ConditionalBuilder newConditional()
    {
        return new ConditionalBuilder(this);
    }

    public Builder await(Callable<Boolean> runCondition)
    {
        tasks.add(new AwaitTask(runCondition));
        return this;
    }

    public Builder awaitPreviousModuleActionCompletion()
    {
        tasks.add(new AwaitTask(()->!lastModuleCalled.isBusy()/*&&lastModuleCalled.getState()==lastModuleStateCalled*/));
        return this;
    }

    public Builder delay(long delay)
    {
        tasks.add(new DelayTask(delay));
        return this;
    }

    public Builder executeBlockingCode(codeExecutable code)
    {
        tasks.add(new BlockingTask(code));
        return this;
    }

    public Builder awaitButtonPress(ButtonReader b)
    {
        tasks.add(new AwaitTask(()->b.wasJustPressed()));
        return this;
    }
    public Builder awaitDtXPosition(double x, Compare c)
    {
        if(c==Compare.GREATER)
            tasks.add(new AwaitTask(()->drive.pose.position.x>x));
        else if(c==Compare.LESSER)
            tasks.add(new AwaitTask(()->drive.pose.position.x<x));
        return this;
    }
    public Builder runTaskList(List<Task> x){
        tasks.addAll(x);
        return this;
    }

    public Builder awaitDtYPosition(double y, Compare c)
    {
        if(c==Compare.GREATER)
            tasks.add(new AwaitTask(()->drive.pose.position.y>y));
        else if(c==Compare.LESSER)
            tasks.add(new AwaitTask(()->drive.pose.position.y<y));
        return this;
    }

    public Builder awaitDtXWithin(double x, double threshold)
    {
        tasks.add(new AwaitTask(()->Math.abs(drive.pose.position.x-x)<threshold));
        return this;
    }

    public Builder awaitDtYWithin(double y, double threshold)
    {
        tasks.add(new AwaitTask(()->Math.abs(drive.pose.position.y-y)<threshold));
        return this;
    }

    public Builder awaitDrivetrainCompletion()
    {
        tasks.add(new AwaitTask(()->!MecanumDrive.isBusy));
        return this;
    }

    public Builder addTaskList(List<Task> task)
    {
        for(Task t: task)
        {
            addTask(t);
        }
        return this;
    }

    public Builder addTask(Task t)
    {
        tasks.add(t);
        return this;
    }

    public List<Task> build()
    {
        //tasks.clear();
        return tasks;
    }

}

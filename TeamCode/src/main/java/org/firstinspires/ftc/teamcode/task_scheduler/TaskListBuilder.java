package org.firstinspires.ftc.teamcode.task_scheduler;

import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.enums.Compare;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class TaskListBuilder
{
    List<Task> tasks=new ArrayList<Task>();
    Module lastModuleCalled;
    ModuleState lastModuleStateCalled;
    LinearOpMode l;

    Robot drive;

    public TaskListBuilder(LinearOpMode l)
    {
        this.l=l;
    }

    public TaskListBuilder(LinearOpMode l, Robot drive)
    {
        this.l=l;
        this.drive=drive;
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
        lastModuleCalled=m;
        lastModuleStateCalled=s;
        return this;
    }

    public TaskListBuilder moduleAction(Module m, ModuleState s, int timeout)
    {

        tasks.add(new ExecutionTask(()->m.setState(s, timeout)));
        lastModuleCalled=m;
        lastModuleStateCalled=s;
        return this;
    }

    public TaskListBuilder await(Callable<Boolean> runCondition)
    {
        tasks.add(new AwaitTask(runCondition));
        return this;
    }

    public TaskListBuilder awaitPreviousModuleActionCompletion()
    {
        tasks.add(new AwaitTask(()->!lastModuleCalled.isBusy()/*&&lastModuleCalled.getState()==lastModuleStateCalled*/));
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

    public TaskListBuilder executeBlockingCode(codeExecutable code)
    {
        tasks.add(new BlockingTask(code));
        return this;
    }

    public TaskListBuilder awaitDtXPosition(double x, Compare c)
    {
        if(c==Compare.GREATER)
            tasks.add(new AwaitTask(()->drive.getPoseEstimate().getX()>x));
        else if(c==Compare.LESSER)
            tasks.add(new AwaitTask(()->drive.getPoseEstimate().getX()<x));
        return this;
    }
    public TaskListBuilder runTaskList(List<Task> x){
        tasks.addAll(x);
        return this;
    }

    public TaskListBuilder awaitDtYPosition(double y, Compare c)
    {
        if(c==Compare.GREATER)
            tasks.add(new AwaitTask(()->drive.getPoseEstimate().getY()>y));
        else if(c==Compare.LESSER)
            tasks.add(new AwaitTask(()->drive.getPoseEstimate().getY()<y));
        return this;
    }

    public TaskListBuilder awaitDtXWithin(double x, double threshold)
    {
        tasks.add(new AwaitTask(()->Math.abs(drive.getPoseEstimate().getX()-x)<threshold));
        return this;
    }

    public TaskListBuilder awaitDtYWithin(double y, double threshold)
    {
        tasks.add(new AwaitTask(()->Math.abs(drive.getPoseEstimate().getY()-y)<threshold));
        return this;
    }

    public TaskListBuilder driveTrajAsync(TrajectorySequence sequence)
    {
        tasks.add(new ExecutionTask(()->drive.followTrajectorySequenceAsync(sequence)));
        return this;
    }

    public TaskListBuilder awaitDrivetrainCompletion()
    {
        tasks.add(new AwaitTask(()->!drive.isBusy()));
        return this;
    }

    public TaskListBuilder addTaskList(List<Task> task)
    {
        for(Task t: task)
        {
            addTask(t);
        }
        return this;
    }

    public TaskListBuilder addTask(Task t)
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

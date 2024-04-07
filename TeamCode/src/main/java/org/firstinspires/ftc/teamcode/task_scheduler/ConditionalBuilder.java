package org.firstinspires.ftc.teamcode.task_scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ConditionalBuilder
{
    Builder parent;
    List<Callable<Boolean>> triggers;
    List<List<Task>> outcomes;
    public ConditionalBuilder (Builder parent)
    {
        this.parent=parent;
        triggers = new ArrayList<>();
        outcomes = new ArrayList<>();
    }

    public ConditionalBuilder newCase(Callable<Boolean> trigger, List<Task> outcome)
    {
        triggers.add(trigger);
        outcomes.add(outcome);
        return this;
    }

    public Builder finishConditional ()
    {
        parent.addTask(new ConditionalTask(triggers, outcomes));
        return parent;
    }
}

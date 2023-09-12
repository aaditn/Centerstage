package org.firstinspires.ftc.teamcode.task_scheduler;

import java.util.ArrayList;
import java.util.List;


public class TaskList
{
    List<Task> list=new ArrayList<Task>();

    public TaskList(List<Task> list)
    {
        this.list=list;
    }

    public List<Task> getTaskList()
    {
        return list;
    }
}

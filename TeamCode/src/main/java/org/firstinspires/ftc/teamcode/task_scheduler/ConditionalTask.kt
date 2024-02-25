package org.firstinspires.ftc.teamcode.task_scheduler

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.util.concurrent.Callable

class ConditionalTask(private val triggers: List<Callable<Boolean>>, val outcomes: List<List<Task>>): Task()
{
    override suspend fun execute()
    {
        var taskList: List<Task> = listOf();
        val scheduler: TaskScheduler = TaskScheduler()

        for(i in triggers.indices)
        {
            if(triggers[i].call())
            {
                taskList = outcomes[i];
                break;
            }
        }

        scheduler.scheduleTaskListBlocking(taskList)
    }
}
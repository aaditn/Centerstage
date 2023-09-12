package org.firstinspires.ftc.teamcode.task_scheduler

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TaskScheduler
{
    fun scheduleTask(t: Task): Unit
    {
        GlobalScope.launch(Dispatchers.Default)
        {
            t.execute()
        }
    }

    fun scheduleTaskList(t: TaskList): Unit
    {
        val taskList: List<Task> = t.taskList
        GlobalScope.launch(Dispatchers.Default)
        {
            for(task in taskList)
            {
                val job = async(Dispatchers.Default){task.execute()}
                if(task.javaClass==AwaitTask::class.java||task.javaClass==BlockingTask::class.java||task.javaClass==DelayTask::class.java)
                {
                    job.join()
                }
            }
        }
    }
}
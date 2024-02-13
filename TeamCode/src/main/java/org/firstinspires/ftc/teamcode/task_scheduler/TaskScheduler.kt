package org.firstinspires.ftc.teamcode.task_scheduler

import android.os.SystemClock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.firstinspires.ftc.teamcode.util.Context

class TaskScheduler
{
    fun scheduleTask(t: Task): Unit
    {
        GlobalScope.launch(Dispatchers.Default)
        {
            t.execute()
        }
    }

    fun scheduleTaskList(t: List<Task>): Unit
    {
        if(Context.opmode!!.opModeIsActive())
        {
            val taskList: List<Task> = t
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

    fun scheduleTaskListBlocking(t: List<Task>): Unit
    {
        val taskList: List<Task> = t
        runBlocking()
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

    fun scheduleTaskListBlocking(t: List<Task>, timeout: Long): Unit
    {
        val startTime: Long = SystemClock.elapsedRealtime()
        val taskList: List<Task> = t
        GlobalScope.launch(Dispatchers.Default)
        {
            for(task in taskList)
            {
                if(SystemClock.elapsedRealtime()-startTime>timeout)
                    break

                val job = async(Dispatchers.Default){task.execute()}
                if(task.javaClass==AwaitTask::class.java||task.javaClass==BlockingTask::class.java||task.javaClass==DelayTask::class.java)
                {
                    while(!job.isCompleted)
                    {
                        if(SystemClock.elapsedRealtime()-startTime>timeout)
                            job.cancel()
                    }
                }
            }
        }
    }
}
package org.firstinspires.ftc.teamcode.task_scheduler

import kotlinx.coroutines.delay
import java.util.concurrent.Callable

class AwaitTask @JvmOverloads constructor(private val condition: Callable<Boolean>, var loopTime: Long=10): Task()
{
    override suspend fun execute()
    {
        while(!condition.call())
        {
            delay(loopTime)
        }
    }
}
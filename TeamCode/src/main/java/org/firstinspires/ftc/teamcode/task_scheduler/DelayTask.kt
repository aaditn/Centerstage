package org.firstinspires.ftc.teamcode.task_scheduler

import kotlinx.coroutines.delay

class DelayTask(val delay: Long): Task()
{
    override suspend fun execute()
    {
        delay(delay)
    }
}
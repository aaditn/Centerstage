package org.firstinspires.ftc.teamcode.task_scheduler

class BlockingTask(val codeExecutable: codeExecutable): Task()
{
    override suspend fun execute()
    {
        codeExecutable.run()
    }
}
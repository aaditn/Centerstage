package org.firstinspires.ftc.teamcode.task_scheduler

class ExecutionTask constructor(val codeExecutable: codeExecutable): Task()
{
    override suspend fun execute()
    {
        codeExecutable.run()
    }
}
package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.task_scheduler.codeExecutable;

public class OneCallAction implements Action
{
    public OneCallAction(codeExecutable code)
    {
        try
        {
            code.run();
        }
        catch(Exception e)
        {

        }
    }

    @Override
    public void preview(@NonNull Canvas canvas) {

    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        return true;
    }
}

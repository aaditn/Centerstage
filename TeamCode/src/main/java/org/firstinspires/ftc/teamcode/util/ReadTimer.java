package org.firstinspires.ftc.teamcode.util;

import android.os.SystemClock;

public class ReadTimer
{
    double milisPerCycle;
    Runnable function;
    double lastTimeRan;

    public ReadTimer(Runnable function, double maxFrequency)
    {
        this.milisPerCycle = (long) (1000. / maxFrequency);
        this.function=function;
    }

    public ReadTimer(Runnable function)
    {
        milisPerCycle=0;
        this.function=function;
    }

    public void update()
    {
        if(timeSinceLastUpdate()>milisPerCycle)
        {
            function.run();
            lastTimeRan=SystemClock.elapsedRealtime();
        }
    }

    public double timeSinceLastUpdate()
    {
        return SystemClock.elapsedRealtime()-lastTimeRan;
    }
}

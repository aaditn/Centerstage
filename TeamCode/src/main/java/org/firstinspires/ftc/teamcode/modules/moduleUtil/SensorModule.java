package org.firstinspires.ftc.teamcode.modules.moduleUtil;

import android.os.SystemClock;

public class SensorModule
{
    double milisPerCycle;
    Runnable function;
    double lastTimeRan;

    public SensorModule(double maxFrequency, Runnable function)
    {
        this.milisPerCycle = (long) (1000. / maxFrequency);
        this.function=function;
    }

    public SensorModule(Runnable function)
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

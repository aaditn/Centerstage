package org.firstinspires.ftc.teamcode.util;

import android.os.SystemClock;
import android.util.Log;

public class TelEntry
{
    public String tag;
    public Object data;
    public boolean logged=false;
    public int logTimer=500;
    private long lastTimeLogged=0;


    public TelEntry(String tag, Object data)
    {
        this.tag=tag;
        this.data=data;
    }

    public TelEntry(String tag, Object data, boolean logged)
    {
        this.tag=tag;
        this.data=data;
        this.logged=logged;
    }

    public TelEntry(String tag, Object data, boolean logged, int logTimer)
    {
        this.tag=tag;
        this.data=data;
        this.logged=logged;
        this.logTimer=logTimer;
    }

    public void log()
    {
        long currentTime=SystemClock.elapsedRealtime();
        if(currentTime-lastTimeLogged>logTimer)
        {
            Log.d(tag, data.toString());
            lastTimeLogged=currentTime;
        }
    }
}

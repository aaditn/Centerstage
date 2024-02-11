package org.firstinspires.ftc.teamcode.util;

import android.os.SystemClock;
import android.util.Log;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.util.DashboardUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Config
public class Tel
{
    Telemetry dashboardTel;
    Telemetry tel;
    List<List<TelEntry>> entries;
    static Tel telWrapper;
    public static int totalLevels=6;
    public static int logTimer=500;
    private long lastTimeLogged;


    public Tel()
    {
        dashboardTel=FtcDashboard.getInstance().getTelemetry();
        tel=Context.opmode.telemetry;
        entries =new ArrayList<>();

        for(int i=0; i<totalLevels; i++)
        {
            entries.add(new ArrayList<>());
        }
    }

    public static Tel instance()
    {
        if(telWrapper==null)
            telWrapper=new Tel();
        return telWrapper;
    }
    public static void destroyInstance()
    {
        telWrapper=null;
    }

    public void update()
    {
        long currentTime=SystemClock.elapsedRealtime();
        boolean logging=currentTime-lastTimeLogged>logTimer;
        if(logging)
            lastTimeLogged=currentTime;

        for(List<TelEntry> list: entries)
        {
            for(TelEntry entry: list)
            {
                tel.addData(entry.tag, entry.data);
                if(Context.dashTeleEnabled)
                    dashboardTel.addData(entry.tag, entry.data);
                if(entry.logged&&logging)
                    Log.d(entry.tag, entry.data.toString());
            }
            tel.addLine();
            if(Context.dashTeleEnabled)
                dashboardTel.addLine();
        }

        tel.update();
        if(Context.dashTeleEnabled)
            dashboardTel.update();

        for(List<TelEntry> list: entries)
        {
            list.clear();
        }
    }


    public void addData(String tag, Object data)
    {
        this.entries.get(this.entries.size()-1).add(new TelEntry(tag, data));
    }

    public void addData(String tag, Object data, boolean logged)
    {
        this.entries.get(this.entries.size()-1).add(new TelEntry(tag, data, logged));
    }
    public void addData(String tag, Object data, int priority)
    {
        if(priority>this.entries.size()-1)
            this.entries.get(this.entries.size()-1).add(new TelEntry(tag, data));
        else if(priority<0)
            this.entries.get(0).add(new TelEntry(tag, data));
        else
            this.entries.get(priority).add(new TelEntry(tag, data));
    }

    public void addData(String tag, Object data, int priority, boolean logged)
    {
        if(priority>this.entries.size()-1)
            this.entries.get(this.entries.size()-1).add(new TelEntry(tag, data, logged));
        else if(priority<0)
            this.entries.get(0).add(new TelEntry(tag, data, logged));
        else
            this.entries.get(priority).add(new TelEntry(tag, data, logged));
    }
}

package org.firstinspires.ftc.teamcode.util;

import android.util.Log;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tel
{
    FtcDashboard dashboard;
    TelemetryPacket packet;
    Telemetry tel;
    List<List<TelEntry>> entries;
    static Tel telWrapper;
    static int totalLevels=6;

    public Tel()
    {
        packet=new TelemetryPacket();
        dashboard=FtcDashboard.getInstance();
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
        for(List<TelEntry> list: entries)
        {
            for(TelEntry entry: list)
            {
                tel.addData(entry.tag, entry.data);
                if(Context.dashTeleEnabled)
                    packet.put(entry.tag, entry.data);
                if(entry.logged)
                    Log.d(entry.tag, entry.data.toString());
            }
            tel.addLine();
        }

        tel.update();
        if(Context.dashTeleEnabled)
            dashboard.sendTelemetryPacket(packet);

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

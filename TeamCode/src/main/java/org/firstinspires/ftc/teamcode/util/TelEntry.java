package org.firstinspires.ftc.teamcode.util;

public class TelEntry
{
    public String tag;
    public Object data;
    public boolean logged=false;

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
}

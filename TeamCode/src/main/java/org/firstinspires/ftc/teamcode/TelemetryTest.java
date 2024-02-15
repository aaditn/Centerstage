package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.auto_paths.truss_paths.farBlueTruss;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.util.AutoSelector;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.util.Tel;

import java.util.List;

@Config
@Autonomous
public class TelemetryTest extends EnhancedOpMode
{
    Tel tel;
    public static boolean dashEnabled=true;
    List<Task> test;
    @Override
    public void linearOpMode()
    {
        waitForStart();
    }

    @Override
    public void initialize()
    {
        Context.dashTeleEnabled=dashEnabled;
        tel=Tel.instance();
        this.setLoopTimes(1);
    }
    public void initLoop()
    {
        AutoSelector.getInstance().loop();
        tel.addData("Hi", "cry", 2, true);
        for(int i = 0; i< farBlueTruss.leftTrajectories.length; i++)
        {
            tel.addData("weeeeee" + i, farBlueTruss.leftTrajectories[i].duration());
        }
        tel.addData("Help", test.size());
        tel.update();
    }

    @Override
    public void primaryLoop()
    {

    }
}

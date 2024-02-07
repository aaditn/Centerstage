package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.auto_paths.farBlue.farBlue2Plus4;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.AutoSelector;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.util.Tel;
@Config
@Autonomous
public class TelemetryTest extends EnhancedOpMode
{
    Tel tel;
    public static boolean dashEnabled=true;
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
        for(int i=0; i<farBlue2Plus4.trajectorySequences.length; i++)
        {
            tel.addData("weeeeee" + i, farBlue2Plus4.trajectorySequences[i].duration());
        }
        tel.update();
    }

    @Override
    public void primaryLoop()
    {

    }
}

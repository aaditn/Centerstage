package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

@TeleOp
public class TestOpMode2 extends EnhancedOpMode
{
    double test=0;
    ElapsedTime linearTimer;
    ElapsedTime coroutineTimer;

    @Override
    public void linearOpMode()
    {
        waitForStart();
        Context.tel.addData("checkpoint", "1");
        linearTimer.reset();
        while(linearTimer.milliseconds()<3000)
        {

        }
        Context.tel.addData("checkpoint", "2");
        linearTimer.reset();
        while(linearTimer.milliseconds()<3000)
        {

        }
        Context.tel.addData("checkpoint", "3");
    }

    @Override
    public void initialize()
    {
        Context.resetValues();
        Context.tel=new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);
        linearTimer=new ElapsedTime();
        coroutineTimer=new ElapsedTime();

    }

    @Override
    public void initLoop()
    {
        test++;
        Context.tel.addData("help", test);
        Context.tel.update();
    }

    @Override
    public void primaryLoop()
    {
        test++;
        Context.tel.addData("help", test);
        Context.tel.update();
    }
}

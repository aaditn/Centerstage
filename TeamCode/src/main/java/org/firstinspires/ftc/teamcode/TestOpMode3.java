package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.ModuleTest;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.roadrunner.OneCallAction;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;


@TeleOp
public class TestOpMode3 extends EnhancedOpMode
{
    ModuleTest test;
    boolean help=false;

    @Override
    public void linearOpMode()
    {
        waitForStart();

        runBlocking(new ParallelAction(
                test.runFor(ModuleTest.State.FWD, 2000),
                new SequentialAction(
                        new SleepAction(2),
                        new OneCallAction(()->help=true)
                )
        ));
    }

    @Override
    public void initialize()
    {
        this.setLoopTimes(10);
        test=new ModuleTest(hardwareMap, telemetry);
    }

    @Override
    public void initLoop()
    {
        test.updateLoop();
        telemetry.addData("help", help);
        telemetry.update();
    }

    @Override
    public void primaryLoop()
    {
        test.updateLoop();
        test.writeLoop();
        telemetry.addData("help", help);
        telemetry.update();
    }
}

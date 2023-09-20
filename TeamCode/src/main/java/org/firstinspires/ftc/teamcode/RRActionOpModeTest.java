package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.ModuleTest;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.util.OneCallAction;

import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.Dispatchers;

@TeleOp
public class RRActionOpModeTest extends EnhancedOpMode
{
    ModuleTest test;
    boolean help=false;

    @Override
    public void linearOpMode()
    {
        waitForStart();

        Actions.runBlocking(
                new ParallelAction(
                        test.runFor(ModuleTest.State.FWD, 1000),
                        new SequentialAction(
                                new SleepAction(1000),
                                new OneCallAction(()->help=true)
                        )
                )
        );

    }

    @Override
    public void initialize()
    {
        Context.resetValues();
        Context.tel=new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);
        this.setLoopTimes(10);
        test=new ModuleTest(hardwareMap);
    }

    @Override
    public void initLoop()
    {
        Context.tel.update();
    }

    @Override
    public void primaryLoop() {
        //Context.tel.addData("Time spent in state", test.timeSpentInState());
        //test.updateLoop();
        //test.writeLoop();
        Context.tel.addData("Help", help);
        Context.tel.update();
    }
}

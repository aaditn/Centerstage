package org.firstinspires.ftc.teamcode.modules.moduleTesting;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.DroneLauncher;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

@TeleOp
@Config
public class DroneTest extends EnhancedOpMode
{
    DroneLauncher drone;
    @Override
    public void linearOpMode()
    {
        waitForStart();
        while(opModeIsActive()){}
    }

    @Override
    public void initialize()
    {
        drone=new DroneLauncher(hardwareMap);
    }

    @Override
    public void primaryLoop()
    {
        drone.updateLoop();
        drone.writeLoop();

        if(gamepad1.a)
        {
            drone.setState(DroneLauncher.State.RELEASED);
        }
        if (gamepad1.b)
        {
            drone.setState(DroneLauncher.State.LOCKED);
        }
    }
}
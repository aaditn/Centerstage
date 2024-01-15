package org.firstinspires.ftc.teamcode.modules.moduleTesting;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.modulesOld.DepositOld;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
@TeleOp
@Config
public class DepositTest extends EnhancedOpMode
{
    DepositOld d;
    @Override
    public void linearOpMode()
    {
        waitForStart();
        while(opModeIsActive()){}
    }

    @Override
    public void initialize()
    {
        d=new DepositOld(hardwareMap);
    }

    @Override
    public void primaryLoop()
    {
        d.updateLoop();
        d.writeLoop();

        if(gamepad1.a)
        {
            d.setState(DepositOld.RotationState.TRANSFER);
        }
        if (gamepad1.b)
        {
            d.setState(DepositOld.RotationState.DEPOSIT_HIGH);
        }
        if(gamepad1.x)
        {
            d.setState(DepositOld.PusherState.IN);
        }
        if(gamepad1.y)
        {
            d.setState(DepositOld.PusherState.TWO);
        }
    }
}

package org.firstinspires.ftc.teamcode.modules.moduleTesting;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
@TeleOp
public class DepositTest extends EnhancedOpMode
{
    Deposit d;
    @Override
    public void linearOpMode()
    {
        waitForStart();
        while(opModeIsActive()){}
    }

    @Override
    public void initialize()
    {
        d=new Deposit(hardwareMap);
    }

    @Override
    public void primaryLoop()
    {
        d.updateLoop();
        d.writeLoop();

        if(gamepad1.a)
        {
            d.setState(Deposit.RotationState.TRANSFER);
        }
        if (gamepad1.b)
        {
            d.setState(Deposit.RotationState.DEPOSIT);
        }
        if(gamepad1.x)
        {
            d.setState(Deposit.PusherState.IN);
        }
        if(gamepad1.y)
        {
            d.setState(Deposit.PusherState.TWO);
        }
    }
}

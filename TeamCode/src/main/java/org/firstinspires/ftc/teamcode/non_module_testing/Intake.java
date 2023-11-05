package org.firstinspires.ftc.teamcode.non_module_testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

@TeleOp
@Config
public class Intake extends EnhancedOpMode
{
    DcMotor intake;
    public static double intakeSpeed=0.9;

    @Override
    public void linearOpMode()
    {
        waitForStart();

        while(opModeIsActive()){}
    }

    @Override
    public void initialize()
    {
        intake=hardwareMap.get(DcMotor.class, "intake");
    }

    @Override
    public void primaryLoop() {
        if(gamepad1.a)
        {
            intake.setPower(intakeSpeed);
        }
        else if(gamepad1.b)
        {
            intake.setPower(-intakeSpeed);
        }
        else if(gamepad1.x)
        {
            intake.setPower(0);
        }
    }
}

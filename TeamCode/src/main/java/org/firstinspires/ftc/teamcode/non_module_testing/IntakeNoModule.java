package org.firstinspires.ftc.teamcode.non_module_testing;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

@TeleOp
@Config
public class IntakeNoModule extends EnhancedOpMode
{
    DcMotor intake;
    ServoEx intakeLeft, intakeRight;
    public static double intakeSpeed=0.9;
    public static double intakeLeftUp=0.3;

    public static double intakeLeft5=0.5;
    public static double intakeLeft4=0.7;
    public static double intakeLeft3;
    public static double intakeLeft2;
    public static double intakeLeft1;

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
        //intakeLeft=hardwareMap.get(ServoEx.class, "intakeLeft");
        //intakeRight=hardwareMap.get(ServoEx.class, "intakeRight");
    }

    @Override
    public void primaryLoop() {
        if(gamepad1.a)
        {
            //intakeLeft.setPosition(intakeLeftUp);
            intake.setPower(intakeSpeed);
        }
        else if(gamepad1.b)
        {
            //intakeLeft.setPosition(intakeLeft5);
            intake.setPower(-intakeSpeed);
        }
        else if(gamepad1.x)
        {
            //intakeLeft.setPosition(intakeLeft4);
            intake.setPower(0);
        }
    }
}

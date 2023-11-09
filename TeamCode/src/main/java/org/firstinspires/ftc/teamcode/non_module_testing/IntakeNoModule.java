package org.firstinspires.ftc.teamcode.non_module_testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

@TeleOp
@Config
public class IntakeNoModule extends EnhancedOpMode
{
    DcMotor intake;
    Servo intakeLeft, intakeRight;
    public static double intakeSpeed=0.9;
    public static double intakeUp0 =0.03;

    public static double intakeLeft5=0.5;
    public static double intakeLeft4=0.7;
    public static double intakeLeft3;
    public static double intakeLeft2;
    public static double intakeUp1 = 0.52;

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
        intakeLeft=hardwareMap.get(Servo.class, "intake1");
        intakeRight=hardwareMap.get(Servo.class, "intake2");
        intakeRight.setDirection(Servo.Direction.REVERSE);
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
        else if(gamepad1.x) {
            intake.setPower(0);
        }
        else if(gamepad2.a){
            intakeLeft.setPosition(intakeUp0);
            intakeRight.setPosition(intakeUp0);
        }
        else if(gamepad2.b){
            intakeLeft.setPosition(intakeUp1);
            intakeRight.setPosition(intakeUp1);
        }
    }
}

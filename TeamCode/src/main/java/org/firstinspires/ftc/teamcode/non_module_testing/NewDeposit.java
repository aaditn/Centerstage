package org.firstinspires.ftc.teamcode.non_module_testing;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

@TeleOp
@Config
public class NewDeposit extends EnhancedOpMode
{
    public static double clawOpen=0.88, clawClosed1=0.72, clawClosed2=0.68;
    public static double rotateFlat=0.1, rotate90=0.42;
    public static double wristIntake=0.3, wristDeposit=0.6;
    public static double rotator1Intake=0.99, rotator1Deposit=0.01;
    public static double rotator2Intake=0.01, rotator2Deposit=0.99;
    Servo deposit1, deposit2, wrist, rotatewrist, claw;



    @Override
    public void linearOpMode()
    {
        waitForStart();
        while(opModeIsActive())
        {
            if(gamepad1.x)
            {
                claw.setPosition(clawOpen);
            }
            else if(gamepad1.y)
            {
                claw.setPosition(clawClosed2);
            }

            if(gamepad1.a)
            {
                rotatewrist.setPosition(rotateFlat);
            }
            else if(gamepad1.b)
            {
                rotatewrist.setPosition(rotate90);
            }

            if(gamepad1.dpad_up)
            {
                deposit1.setPosition(rotator1Deposit);
                deposit2.setPosition(rotator2Deposit);
                wrist.setPosition(wristDeposit);
            }
            else if(gamepad1.dpad_down)
            {
                deposit1.setPosition(rotator1Intake);
                deposit2.setPosition(rotator2Intake);
                wrist.setPosition(wristIntake);
            }
        }
    }

    @Override
    public void initialize()
    {
        deposit1=hardwareMap.get(Servo.class, "deposit1");
        deposit2=hardwareMap.get(Servo.class, "deposit2");
        wrist=hardwareMap.get(Servo.class, "wrist");
        rotatewrist=hardwareMap.get(Servo.class, "rotatewrist");
        claw=hardwareMap.get(Servo.class, "claw");
    }

    @Override
    public void primaryLoop()
    {

    }
}

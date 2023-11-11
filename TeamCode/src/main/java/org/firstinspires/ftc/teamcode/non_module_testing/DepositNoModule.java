package org.firstinspires.ftc.teamcode.non_module_testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

@TeleOp
@Config
public class DepositNoModule extends EnhancedOpMode
{
    Servo rl, rr, p;

    public static double rotationRightInit=0.22;
    public static double rotationLeftInit=0.83;
    public static double pusherInit=0;

    public static double pusherExtended=0;
    public static double rotationRightUp=1;
    public static double rotationLeftUp=0.05;

    @Override
    public void linearOpMode() {
        waitForStart();
        while(opModeIsActive()){}
    }

    @Override
    public void initialize() {
        rl = hardwareMap.get(Servo.class, "leftRotator");
        rr = hardwareMap.get(Servo.class, "rightRotator");
        p = hardwareMap.get(Servo.class, "pinion");
    }

    @Override
    public void primaryLoop() {
        if(gamepad1.a)
        {
            rr.setPosition(rotationRightInit);
            rl.setPosition(rotationLeftInit);
            p.setPosition(pusherInit);
        }
        else if(gamepad1.b)
        {
            rr.setPosition(rotationRightUp);
            rl.setPosition(rotationLeftUp);
        }
        else if(gamepad1.x)
        {
            p.setPosition(pusherExtended);
        }
    }
}

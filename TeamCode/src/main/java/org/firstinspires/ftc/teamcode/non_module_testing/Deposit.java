package org.firstinspires.ftc.teamcode.non_module_testing;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

public class Deposit extends EnhancedOpMode
{
    Servo rl, rr, p;

    public static double rotationRightInit=0;
    public static double rotationLeftInit=0;
    public static double pusherInit=0;

    public static double pusherExtended=0;
    public static double rotationRightUp;
    public static double rotationLeftUp;

    @Override
    public void linearOpMode() {
        waitForStart();
        while(opModeIsActive()){}
    }

    @Override
    public void initialize() {
        rl = hardwareMap.get(Servo.class, "depositLeft");
        rr = hardwareMap.get(Servo.class, "depositRight");
        p = hardwareMap.get(Servo.class, "depositPusher");
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

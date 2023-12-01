package org.firstinspires.ftc.teamcode.non_module_testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

@TeleOp
@Config
public class DepositNoModule extends EnhancedOpMode
{
    Servo rl, rr, p, wrist;



    public static double pusherInit=0;

    public static double pusherExtended=0.18;
    public static double transfer1=0.99;
    public static double transfer2=0.01;

    public static double deposit1High= 0.01;//0.91;//.83
    public static double deposit2High = 0.99;//0.14;//.22

    public static double wristInit = 0.73;
    public static double wristDeposit = 0.34;

    @Override
    public void linearOpMode() {
        waitForStart();
        while(opModeIsActive()){}
    }

    @Override
    public void initialize() {
        rl = hardwareMap.get(Servo.class, "leftRotator");
        rr = hardwareMap.get(Servo.class, "rightRotator");
        rr.setDirection(Servo.Direction.REVERSE);
        rl.setDirection(Servo.Direction.REVERSE);
        p = hardwareMap.get(Servo.class, "pusher");
        wrist = hardwareMap.get(Servo.class, "wrist");
    }

    @Override
    public void primaryLoop() {
        if(gamepad1.a)
        {
            rr.setPosition(deposit1High);
            rl.setPosition(deposit2High);
            wrist.setPosition(wristDeposit);
        }
        else if(gamepad1.b)
        {
            rr.setPosition(transfer1);
            rl.setPosition(transfer2);
            wrist.setPosition(wristInit);
            p.setPosition(pusherExtended);
        }
        else if(gamepad1.x)
        {
            p.setPosition(pusherExtended);
        }
    }
}

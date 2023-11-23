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

    public static double pusherExtended=0;
    public static double transfer1=1;
    public static double transfer2=0.12;

    public static double deposit1High= 0.2;//0.91;//.83
    public static double deposit2High = 0.85;//0.14;//.22

    public static double wristInit = 0;
    public static double wristDeposit = 0.3;

    @Override
    public void linearOpMode() {
        waitForStart();
        while(opModeIsActive()){}
    }

    @Override
    public void initialize() {
        rl = hardwareMap.get(Servo.class, "leftRotator");
        rr = hardwareMap.get(Servo.class, "rightRotator");
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
            p.setPosition(pusherInit);
            wrist.setPosition(wristInit);
        }
        else if(gamepad1.x)
        {
            p.setPosition(pusherExtended);
        }
    }
}

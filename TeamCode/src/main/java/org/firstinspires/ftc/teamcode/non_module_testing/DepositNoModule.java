package org.firstinspires.ftc.teamcode.non_module_testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

@TeleOp
@Config
public class DepositNoModule extends EnhancedOpMode
{
    Servo rl, rr, pusher, wrist;



    public static double pusherIn =0;
    public static double pusherOne=0.25;
    public static double pusherHalf= 0.265;
    public static double pusherTwo=0.35;
    public static double transfer1=0.96;
    public static double transfer2=0.04;

    public static double deposit1High= 0.01;
    public static double deposit2High = 0.99;

    public static double wristInit = 0.795;
    public static double wristDeposit = 0.47;
    int pusherPos = 0;
    double[] pusherPositions = {pusherIn, pusherOne, pusherHalf, pusherTwo};

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
        pusher = hardwareMap.get(Servo.class, "pusher");
        wrist = hardwareMap.get(Servo.class, "wrist");
    }

    @Override
    public void primaryLoop() {
        if(gamepad2.a)
        {
            rr.setPosition(deposit1High);
            rl.setPosition(deposit2High);
            wrist.setPosition(wristDeposit);
        }
        else if(gamepad2.b)
        {
            rr.setPosition(transfer1);
            rl.setPosition(transfer2);
            wrist.setPosition(wristInit);
        }
        if(gamepad2.x)
        {
            if(pusherPos > pusherPositions.length-1)
            {
                pusherPos = 0;
            }
            else {
                pusherPos++;
            }
            pusher.setPosition(pusherPositions[pusherPos]);
        }
    }
}

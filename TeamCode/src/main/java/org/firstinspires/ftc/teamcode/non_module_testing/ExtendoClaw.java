package org.firstinspires.ftc.teamcode.non_module_testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
@Config

public class ExtendoClaw extends LinearOpMode {
    Servo angle;
    Servo wristAngle;
    Servo clawWrist;
    Servo clawL;
    Servo clawR;
    Servo rot;
   public static double angleInit = 0.89;
    public static double angleOne = 0.835;
    public static double angleTwo = 0.86;

    public static double wristOne = .37;

    public static double wristTwo = .37;
          public static double rotInit =.5;
    public static double rotBack = 0;
    public static double angleTransfer = 0.29;
    public static double wristInit = .37;
    public static double wristTransfer = 1;
    public static double rotLeft = .7;
    public static double rotRight =.3;
    public static double clawOpen = 0;
    public static double clawClose = 1;
    public static double clawWristInit = 0.55;
    public static double clawWristLeft =.33;
    public static double clawWristRight = .73;
    public static double clawWrist90Left =.75;
    public static double clawWrist90Right = .35;
    public void waitT(int ticks)
    {
        ElapsedTime x= new ElapsedTime();
        while(x.milliseconds()<ticks&&opModeIsActive())
        {

        }
    }
    @Override

    public void runOpMode() throws InterruptedException {
        rot = hardwareMap.get(Servo.class,"1");
        wristAngle = hardwareMap.get(Servo.class,"2");
        angle = hardwareMap.get(Servo.class, "3");
        clawWrist = hardwareMap.get(Servo.class,"4");
        clawL = hardwareMap.get(Servo.class,"5");
        clawR = hardwareMap.get(Servo.class,"6");
        while(true){
            if(gamepad1.a){
                angle.setPosition(angleInit);
                rot.setPosition(rotInit);

                wristAngle.setPosition(wristInit);
                waitT(250);
                clawWrist.setPosition(clawWristInit);
            }
            else if(gamepad1.b){
                angle.setPosition(angleTransfer);

                rot.setPosition(rotInit);

                clawWrist.setPosition(clawWristLeft);
                wristAngle.setPosition(wristTransfer);
                waitT(1000);

                clawR.setPosition(clawClose);
                waitT(500);
                clawWrist.setPosition(clawWristRight);
waitT(1000);
                clawL.setPosition(clawOpen);
            }
            else if(gamepad1.dpad_left){
                angle.setPosition(angleOne);
                wristAngle.setPosition(wristOne);

            }
            else if(gamepad1.dpad_right){
                angle.setPosition(angleTwo);

                wristAngle.setPosition(wristTwo);
            }
            else if(gamepad1.x){
                clawL.setPosition(clawClose);
                clawR.setPosition(clawOpen);
            }
            else if(gamepad1.left_trigger>.2){
                clawL.setPosition(clawOpen);
            }
            else if(gamepad1.right_trigger>.2){
                clawR.setPosition(clawClose);
            }
            else if(gamepad1.left_bumper){
                rot.setPosition(rotLeft);
                clawWrist.setPosition(clawWrist90Left);
            }
            else if(gamepad1.right_bumper){
                rot.setPosition(rotRight);
                clawWrist.setPosition(clawWrist90Right);
            }
        }
    }
}

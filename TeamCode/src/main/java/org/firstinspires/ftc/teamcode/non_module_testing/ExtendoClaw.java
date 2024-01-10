package org.firstinspires.ftc.teamcode.non_module_testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
@Config

public class ExtendoClaw extends LinearOpMode {
    Servo angle;
    Servo wristAngle;
    Servo clawWrist;
    Servo clawL;
    Servo clawR;
    Servo rot;
   public static double angleInit = .9;
          public static double rotInit =.5;
    public static double rotBack = 0;
    public static double angleTransfer = .25;
    public static double wristInit = .35;
    public static double wristTransfer = .95;
    public static double rotLeft = .7;
    public static double rotRight =.3;
    public static double clawOpen = 1;
    public static double clawClose = 0;
    public static double clawWristInit = 0.55;
    public static double clawWristLeft =.75;
    public static double clawWristRight = .35;

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
                clawWrist.setPosition(clawWristInit);
            }
            else if(gamepad1.b){
                angle.setPosition(angleTransfer);
                wristAngle.setPosition(wristTransfer);
            }
            else if(gamepad1.dpad_left){
                rot.setPosition(rotLeft);
            }
            else if(gamepad1.dpad_right){
                rot.setPosition(rotRight);
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
                clawWrist.setPosition(clawWristLeft);
            }
            else if(gamepad1.right_bumper){
                clawWrist.setPosition(clawWristRight);
            }
        }
    }
}

package org.firstinspires.ftc.teamcode.non_module_testing;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
@Config
public class IntakeJarrettNoModule extends LinearOpMode {
    public int liftPos = 0;
    public static double rotationRightInit=0.22;
    public static double rotationLeftInit=0.83;
    //public static double rotationRightUp=1; //no usages
    public static double rotationLeftUp=0.05;
    public static double intakePos5 = 0.18;
    public static double intakePos4 = 0.16;
    public static double intakePos3 = 0.13;
    public static double intakePos2 = 0.09;
    public static double intakePos1 = 0.06;
    public static double intakePos0 = 0.03;
    //no usages
    /*
    public static double intakePos = .95 ;
    public static double depositPos =0;
    public static double intakePos1 = 0.43;
    public static double depositPos1 =0.15;
    */
    public static double intakePusher = 0.1;
    public static double pinion0 = 0.22;
    public static double pinion1 = 0.26;
    int state = 0;
    boolean intaking = false;
    DcMotorEx lb, lf, rb, rf;
    IMU imu;
    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor slide1 = hardwareMap.get(DcMotor.class, "slide1");
        DcMotor slide2 = hardwareMap.get(DcMotor.class, "slide2");
        slide1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide2.setDirection(DcMotorSimple.Direction.REVERSE);
        slide1.setTargetPosition(liftPos);
        slide2.setTargetPosition(liftPos);
        slide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        boolean stater = false;

        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        //LEFT & RIGHT NOT CHECKED
        Servo intakeLeft = hardwareMap.get(Servo.class, "intakeLeft");
        Servo intakeRight = hardwareMap.get(Servo.class, "intakeRight");
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeLeft.setDirection(Servo.Direction.REVERSE);

        //LEFT & RIGHT NOT CHECKED
        Servo rotaterLeft = hardwareMap.get(Servo.class, "rotaterLeft");
        Servo rotaterRight = hardwareMap.get(Servo.class, "rotaterRight");
        //rotaterLeft.setDirection(Servo.Direction.REVERSE);
        Servo pinion = hardwareMap.get(Servo.class, "pinion");

        FtcDashboard dashboard = FtcDashboard.getInstance();
        TelemetryPacket packet = new TelemetryPacket();

        lb = hardwareMap.get(DcMotorEx.class, "bl");
        lf = hardwareMap.get(DcMotorEx.class, "fl");
        rb = hardwareMap.get(DcMotorEx.class, "br");
        rf = hardwareMap.get(DcMotorEx.class, "fr");
        lb.setDirection(DcMotorSimple.Direction.REVERSE);
        lf.setDirection(DcMotorSimple.Direction.REVERSE);

        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        imu.initialize(parameters);

        waitForStart();

        while (opModeIsActive()) {
            double x = -gamepad1.left_stick_y/1.2;
            double y = -gamepad1.left_stick_x/1.2;
            double rx = gamepad1.right_stick_x/1.5;

            lf.setPower(y + x + rx);
            lb.setPower(y - x + rx);
            rf.setPower(y - x - rx);
            rb.setPower(y + x - rx);
            if(stater&&!gamepad1.a&&!gamepad1.b&&(gamepad1.right_trigger<0.2)) {
                stater=false;
            }
            else if(stater){

            }
            else if(gamepad1.a){
                state++;
                stater=true;
            }
            else if(gamepad1.b){
                state--;
                stater=true;
            }
            else if(gamepad1.right_trigger>0&& intaking){
                intaking =false;
                stater=true;
            }
            else if(gamepad1.right_trigger>0.2){
                intaking =true;
                stater=true;
            }


            if(intaking){
                intakeMotor.setPower(-1);
            }
            else{
                intakeMotor.setPower(0);
            }
            if(gamepad1.x){
                rotaterLeft.setPosition(rotationLeftInit);
                rotaterRight.setPosition(rotationRightInit);
                pinion.setPosition(intakePusher);
            }
            else if(gamepad1.y){
                rotaterLeft.setPosition(rotationLeftUp);
                rotaterRight.setPosition(rotationRightInit);
            }

            if(gamepad1.left_bumper){

                pinion.setPosition(pinion0);
            }
            if(gamepad1.right_bumper){
                pinion.setPosition(pinion1);

            }

            slide1.setTargetPosition(liftPos);
            slide2.setTargetPosition(liftPos);
            slide1.setPower(1);
            slide2.setPower(1);
            if(gamepad1.dpad_up){
                liftPos+=10;
            }
            if(gamepad1.dpad_down&&liftPos>0){
                liftPos-=10;
            }
          if(state == 0){
              intakeLeft.setPosition(intakePos0);
              intakeRight.setPosition(intakePos0);
          }
          else if(state==1){

              intakeLeft.setPosition(intakePos1);
              intakeRight.setPosition(intakePos1);
          }
          else if(state==2){

              intakeLeft.setPosition(intakePos2);
              intakeRight.setPosition(intakePos2);
          }
          else if(state==3){

              intakeLeft.setPosition(intakePos3);
              intakeRight.setPosition(intakePos3);
          }
          else if(state==4){

              intakeLeft.setPosition(intakePos4);
              intakeRight.setPosition(intakePos4);
          }
          else if(state==5){

              intakeLeft.setPosition(intakePos5);
              intakeRight.setPosition(intakePos5);
          }
telemetry.addData("state",state);
          telemetry.update();
            dashboard.sendTelemetryPacket(packet);
        }







    }




}

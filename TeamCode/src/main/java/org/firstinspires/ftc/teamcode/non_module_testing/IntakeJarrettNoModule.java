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
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
@Config
public class IntakeJarrettNoModule extends LinearOpMode {
// s1: 0.20, 0.6
// s2: 0.34, 0.67
public int liftPos = 0;


    public static double se5 = 0.18;
    public static double se4 = 0.16;
    public static double se3 = 0.13;
    public static double se2 = 0.09;
    public static double se1 = 0.06;
    public static double se0 = 0.03;
    public static double intakePos = .95 ;
    public static double depositPos =0;
    public static double intakePos1 = 0.43;
    public static double depositPos1 =0.15;
    public static double intakePusher = 0.1;

    public static double depositSinglePusher = 0.27;

    public static double depositDoublePusher = 0.30;

    int state = 0;
    boolean intake = false;
    DcMotorEx lb, lf, rb, rf;
    Servo rl,rr,r,p;
    IMU imu;
    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor left = hardwareMap.get(DcMotor.class, "slides1");
        DcMotor right = hardwareMap.get(DcMotor.class, "slides2");
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left.setDirection(DcMotorSimple.Direction.REVERSE);
        left.setTargetPosition(liftPos);
        right.setTargetPosition(liftPos);
        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
boolean stater = false;
        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        Servo s1 = hardwareMap.get(Servo.class, "s1");
        Servo s2 = hardwareMap.get(Servo.class, "s2");
        Servo rl = hardwareMap.get(Servo.class, "depositLeft");
        Servo rr = hardwareMap.get(Servo.class, "depositRight");
        Servo r = hardwareMap.get(Servo.class, "depositRotation");
        Servo p = hardwareMap.get(Servo.class, "depositPusher");
s1.setDirection(Servo.Direction.REVERSE);
        FtcDashboard dashboard = FtcDashboard.getInstance();

        TelemetryPacket packet = new TelemetryPacket();
        lb = hardwareMap.get(DcMotorEx.class, "bl");
        lf = hardwareMap.get(DcMotorEx.class, "fl");
        rb = hardwareMap.get(DcMotorEx.class, "br");
        rf = hardwareMap.get(DcMotorEx.class, "fr");

        lb.setDirection(DcMotorSimple.Direction.REVERSE);
        lf.setDirection(DcMotorSimple.Direction.REVERSE);
        rl.setDirection(Servo.Direction.REVERSE);

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
            else if(gamepad1.right_trigger>0&&intake){
                intake=false;
                stater=true;
            }
            else if(gamepad1.right_trigger>0.2){
                intake=true;
                stater=true;
            }


            if(intake){
                intakeMotor.setPower(-1);
            }
            else{
                intakeMotor.setPower(0);
            }
            if(gamepad1.x){
                rl.setPosition(intakePos);
                rr.setPosition(intakePos);
                r.setPosition(intakePos1);
                p.setPosition(intakePusher);
            }
            else if(gamepad1.y){
                rl.setPosition(depositPos);
                rr.setPosition(depositPos);
                r.setPosition(depositPos1);
            }

            if(gamepad1.left_bumper){

                p.setPosition(depositSinglePusher);
            }
            if(gamepad1.right_bumper){
                p.setPosition(depositDoublePusher);

            }

            left.setTargetPosition(liftPos);
            right.setTargetPosition(liftPos);
            left.setPower(1);
            right.setPower(1);
            if(gamepad1.dpad_up&&liftPos<1400){
                liftPos+=5;
            }
            if(gamepad1.dpad_down){
                liftPos=0;
            }
          if(state == 0){
              s1.setPosition(se0);
              s2.setPosition(se0);
          }
          else if(state==1){

              s1.setPosition(se1);
              s2.setPosition(se1);
          }
          else if(state==2){

              s1.setPosition(se2);
              s2.setPosition(se2);
          }
          else if(state==3){

              s1.setPosition(se3);
              s2.setPosition(se3);
          }
          else if(state==4){

              s1.setPosition(se4);
              s2.setPosition(se4);
          }
          else if(state==5){

              s1.setPosition(se5);
              s2.setPosition(se5);
          }
telemetry.addData("state",state);
          telemetry.update();
            dashboard.sendTelemetryPacket(packet);
        }







    }




}

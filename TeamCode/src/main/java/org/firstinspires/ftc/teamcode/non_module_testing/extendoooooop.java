package org.firstinspires.ftc.teamcode.non_module_testing;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.DualNum;
import com.acmerobotics.roadrunner.MecanumKinematics;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.PoseVelocity2dDual;
import com.acmerobotics.roadrunner.Time;
import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.KeyReader;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.arcrobotics.ftclib.gamepad.TriggerReader;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp
@Config


public class extendoooooop extends LinearOpMode {
    FtcDashboard dashboard;
    GamepadEx g1;

    DcMotorEx fr, fl, br, bl, extend1, extend2;
    Servo depo, candy;
    public static double depoForward = 0.45; //0.45 is 180!, 0.73 is 90
    public static double depoBack = 1;

    public static int in = 0, one = 300, two = 600, three = 900;

    public static double power = 0;
    double[] pos = {0.0045, 0.0175, 0.0315, 0.0445, 0.0575, 0.0725, 0.0840, 0.0960, 0.1130, 0.1250, 0.1386, 0.1520, 0.1675, 0.1810, 0.1950, 0.2085};
    int index = 0; // go through each position in pos
    KeyReader[] keyReaders;
    ButtonReader dpad_down, dpad_left, dpad_up, dpad_right, A, RB, LB;
    TriggerReader RT, LT;
    public static double ninja = 1;
    @Override
    public void runOpMode() throws InterruptedException {

        g1 = new GamepadEx(gamepad1);

        keyReaders = new KeyReader[]{
                dpad_down = new ToggleButtonReader(g1, GamepadKeys.Button.DPAD_DOWN),
                dpad_left = new ToggleButtonReader(g1, GamepadKeys.Button.DPAD_LEFT),
                dpad_right = new ToggleButtonReader(g1, GamepadKeys.Button.DPAD_RIGHT),
                dpad_up = new ToggleButtonReader(g1, GamepadKeys.Button.DPAD_UP),
                A = new ToggleButtonReader(g1, GamepadKeys.Button.A),
                RB = new ToggleButtonReader(g1, GamepadKeys.Button.RIGHT_BUMPER),
                LB = new ToggleButtonReader(g1, GamepadKeys.Button.LEFT_BUMPER),
                RT = new TriggerReader(g1, GamepadKeys.Trigger.RIGHT_TRIGGER),
                LT = new TriggerReader(g1, GamepadKeys.Trigger.LEFT_TRIGGER),

        };

        depo = hardwareMap.get(Servo.class,"depo");
        candy = hardwareMap.get(Servo.class, "candy");

        fr = hardwareMap.get(DcMotorEx.class, "fr");
        fl = hardwareMap.get(DcMotorEx.class, "fl");
        br = hardwareMap.get(DcMotorEx.class, "br");
        bl = hardwareMap.get(DcMotorEx.class, "bl");
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        extend1 = hardwareMap.get(DcMotorEx.class, "extend1");
        extend2 = hardwareMap.get(DcMotorEx.class, "extend2");
        extend1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extend1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extend1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extend2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extend2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extend2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extend2.setDirection(DcMotorSimple.Direction.REVERSE);

        dashboard = FtcDashboard.getInstance();

        //INITIALIZATION
        depo.setPosition(depoBack);
        candy.setPosition(pos[0]);

        waitForStart();

        while(opModeIsActive()){

            for (KeyReader i: keyReaders) {
                i.readValue();
            }
            drive(); // moves drivetrain

            //CLAW OPEN/CLOSE
            if (RB.wasJustPressed()) {
                depo.setPosition(depoBack);
            } else if (LB.wasJustPressed()) {
                depo.setPosition(depoForward);
            }

            if (A.wasJustPressed()) {
                candy.setPosition(pos[index]);
                if (index == pos.length - 1) {
                    index = 0;
                } else {
                    index++;
                }
            }

            if (dpad_down.wasJustPressed()) {
                setExtendoPos(one, power);
            } else if (dpad_left.wasJustPressed()) {
                setExtendoPos(two, power);
            } else if (dpad_up.wasJustPressed()) {
                setExtendoPos(three, power);
            } else if (dpad_right.wasJustPressed()) {
                setExtendoPos(in, power);
            }
        }
    }
    public void drive() {
        double x, y, rx;
        if (Math.abs(gamepad1.left_stick_y) < 0.3) {
            x = ninja * 1.67 * Math.abs(gamepad1.left_stick_y);
        } else {
            x = ninja * (0.33 + 0.6 * Math.pow(Math.abs(gamepad1.left_stick_y), 1.4));
        }

        if (Math.abs(gamepad1.left_stick_x) < 0.3) {
            y = ninja * 1 * Math.abs(gamepad1.left_stick_x);
        } else {
            y = ninja * (0.1412 + 0.6 * Math.pow(Math.abs(gamepad1.left_stick_x), 1.4));
        }
        if (Math.abs(gamepad1.right_stick_x) < 0.3) {
            rx = ninja * 0.5 * Math.pow(Math.abs(gamepad1.right_stick_x), 0.333);
        } else {
            rx = ninja * (0.33 + 0.5 * Math.pow(Math.abs(gamepad1.right_stick_x), 1.4));
        }
        if (x > 0.85 && rx < 0.2) {
            x *= 1.5;
            y /= 1.5;
        }
        x *= Math.signum(gamepad1.left_stick_y);
        y *= Math.signum(gamepad1.left_stick_x);
        rx *= Math.signum(gamepad1.right_stick_x);

        setDrivePowers(new Pose2d(x, y, rx));
    }
    public void setDrivePowers(Pose2d localDrivePowers) {
        PoseVelocity2d poseVelocity2d = new PoseVelocity2d(localDrivePowers.position, localDrivePowers.heading.toDouble());
        MecanumKinematics.WheelVelocities<Time> wheelVels = new MecanumKinematics(1).inverse(
                PoseVelocity2dDual.constant(poseVelocity2d, 1));

        double maxPowerMag = 1;
        for (DualNum<Time> power : wheelVels.all()) {
            maxPowerMag = Math.max(maxPowerMag, power.value());
        }

        fl.setPower(wheelVels.leftFront.get(0) / maxPowerMag);
        bl.setPower(wheelVels.leftBack.get(0) / maxPowerMag);
        br.setPower(wheelVels.rightBack.get(0) / maxPowerMag);
        fr.setPower(wheelVels.rightFront.get(0) / maxPowerMag);
    }

    public void setExtendoPos(int pos, double power) {
        extend1.setPower(power);
        extend2.setPower(power);
        extend1.setTargetPosition(pos);
        extend2.setTargetPosition(pos);
    }


}
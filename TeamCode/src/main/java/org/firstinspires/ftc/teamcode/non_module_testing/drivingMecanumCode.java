package org.firstinspires.ftc.teamcode.non_module_testing;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.roadrunner.util.Encoder;

@TeleOp
public class drivingMecanumCode extends LinearOpMode {

   DcMotorEx lb, lf, rb, rf;
   IMU imu;
    public void runOpMode() {
        lb = hardwareMap.get(DcMotorEx.class, "lb");
        lf = hardwareMap.get(DcMotorEx.class, "lf");
        rb = hardwareMap.get(DcMotorEx.class, "rb");
        rf = hardwareMap.get(DcMotorEx.class, "rf");

        lb.setDirection(DcMotorSimple.Direction.REVERSE);
        lf.setDirection(DcMotorSimple.Direction.REVERSE);

        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        imu.initialize(parameters);

        waitForStart();

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y/1.2;
            double x = gamepad1.left_stick_x/1.2;
            double rx = gamepad1.right_stick_x/1.5;

            lf.setPower(y + x + rx);
            lb.setPower(y - x + rx);
            rf.setPower(y - x - rx);
            rb.setPower(y + x - rx);

            telemetry.addData("heading", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        }
    }
}

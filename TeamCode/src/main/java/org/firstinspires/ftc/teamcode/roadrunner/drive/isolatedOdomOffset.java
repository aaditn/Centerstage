package org.firstinspires.ftc.teamcode.roadrunner.drive;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.TwoTrackingWheelLocalizer;
import com.acmerobotics.roadrunner.util.Angle;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.teamcode.roadrunner.util.Encoder;

import java.util.Arrays;
import java.util.List;

/*
 * Sample tracking wheel localizer implementation assuming the standard configuration:
 *
 *    ^
 *    |
 *    | ( x direction)
 *    |
 *    v
 *    <----( y direction )---->

 *        (forward)
 *    /--------------\
 *    |     ____     |
 *    |     ----     |    <- Perpendicular Wheel
 *    |           || |
 *    |           || |    <- Parallel Wheel
 *    |              |
 *    |              |
 *    \--------------/
 *
 *
 */
@TeleOp
@Config
public class isolatedOdomOffset extends LinearOpMode {
    public static double PARALLEL_X = 0; // X is the up and down direction
    public static double PARALLEL_Y = 0; // Y is the strafe direction

    public static double PERPENDICULAR_X = 0;
    public static double PERPENDICULAR_Y = 0;


    // Parallel/Perpendicular to the forward axis
    // Parallel wheel is parallel to the forward axis
    // Perpendicular is perpendicular to the forward axis

    private Encoder parallelEncoder, perpendicularEncoder;

    private SampleMecanumDrive drive;

    public void runOpMode() {

        FtcDashboard dashboard = FtcDashboard.getInstance();
        TelemetryPacket packet = new TelemetryPacket();

        IMU imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                DriveConstants.LOGO_FACING_DIR, DriveConstants.USB_FACING_DIR));
        imu.initialize(parameters);

        waitForStart();

        while (opModeIsActive()) {
            double d = Math.sqrt(PARALLEL_X * PARALLEL_X + PARALLEL_Y * PARALLEL_Y);
            // pythagorean to find magnitude of offset vector, refresh in while so we can edit in dashboard

            double angle = imu.getRobotAngularVelocity(AngleUnit.RADIANS).zRotationRate;

            double angleSubtractInches = angle * d; // offset subtraction in inches
            double angleSubtractTicks = angleSubtractInches * TwoWheelTrackingLocalizer.encoderInchesToTicks(angleSubtractInches);
            double netDist = perpendicularEncoder.getCurrentPosition() - angleSubtractTicks * parallelEncoder.getMultiplier();

            Pose2d poseEstimate = drive.getPoseEstimate();

            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());

            telemetry.addData("netDistance", netDist);

            telemetry.update();

            packet.put ("PAR_X", PARALLEL_X);
            packet.put ("PAR_Y", PARALLEL_Y);
            packet.put ("PERP_X", PERPENDICULAR_X);
            packet.put ("PERP_Y", PERPENDICULAR_Y);


            dashboard.sendTelemetryPacket(packet);


            /* can change either value to adjust for offset I think...
            I am too lazy to find out how matrix solver converts individual vectors,
            but it makes sense that it would only need magnitude + angle, not x and y individually

            ... if so it might take more guess & check to optimize */









        }

    }
}
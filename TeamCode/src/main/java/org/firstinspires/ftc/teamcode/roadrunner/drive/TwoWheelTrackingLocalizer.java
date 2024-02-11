package org.firstinspires.ftc.teamcode.roadrunner.drive;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.TwoTrackingWheelLocalizer;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Robot;
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
 */
public class TwoWheelTrackingLocalizer extends TwoTrackingWheelLocalizer {
    public static double TICKS_PER_REV_GOBILDA = 2000;

    public static double TICKS_PER_REV_THROUGHBORE = 8192;
    public static double WHEEL_RADIUS_GOBILDA = 0.944882; // in
    public static double WHEEL_RADIUS_THROUGHBORE = 0.6889764; // in
    public static double GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed

    public static double PARALLEL_X = -0.73543307; // X is the up and down direction
    public static double PARALLEL_Y = 6.4515748;

    public static double PERPENDICULAR_X = -5.98188976;
    public static double PERPENDICULAR_Y = -1.25984;
    public static double ParM = 1;
    public static double PerpM = 1;

    // Parallel/Perpendicular to the forward axis
    // Parallel wheel is parallel to the forward axis
    // Perpendicular is perpendicular to the forward axis
    private Encoder parallelEncoder, perpendicularEncoder;

    private Robot drive;

    public TwoWheelTrackingLocalizer(HardwareMap hardwareMap, SampleMecanumDrive drive) {
        super(Arrays.asList(
                new Pose2d(PARALLEL_X, PARALLEL_Y, 0),
                new Pose2d(PERPENDICULAR_X, PERPENDICULAR_Y, Math.toRadians(90))
        ));

        //this.drive = drive;

        parallelEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "fr"));
        // bl is black parallel wheel

        perpendicularEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "bl")); //perp needs to be changed, back perp odom broken

        // TODO: reverse any encoders using Encoder.setDirection(Encoder.Direction.REVERSE)
        parallelEncoder.setDirection(Encoder.Direction.REVERSE);
    }

    public TwoWheelTrackingLocalizer(HardwareMap hardwareMap, Robot drive) {
        super(Arrays.asList(
                new Pose2d(PARALLEL_X, PARALLEL_Y, 0),
                new Pose2d(PERPENDICULAR_X, PERPENDICULAR_Y, Math.toRadians(90))
        ));

        this.drive = drive;

        parallelEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "fr"));
        perpendicularEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "bl")); //perp needs to be changed, back perp odom broken

        // TODO: reverse any encoders using Encoder.setDirection(Encoder.Direction.REVERSE)
        parallelEncoder.setDirection(Encoder.Direction.REVERSE);
    }

    public static double encoderTicksToInches(double ticks, boolean isGobilda) {
        if (isGobilda) {
            return WHEEL_RADIUS_GOBILDA * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV_GOBILDA;
        } else {
            return WHEEL_RADIUS_THROUGHBORE * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV_THROUGHBORE;
        }
    }
    public static double encoderInchesToTicks(double inches, boolean isGobilda) {
        if (isGobilda) {
            return inches * TICKS_PER_REV_GOBILDA / WHEEL_RADIUS_GOBILDA / 2 / Math.PI / GEAR_RATIO;
        } else {
            return inches * TICKS_PER_REV_THROUGHBORE / WHEEL_RADIUS_THROUGHBORE / 2 / Math.PI / GEAR_RATIO;
        }
    }
    @Override
    public void update()
    {
        super.update();

    }

    @Override
    public double getHeading() {
        return drive.getRawExternalHeading();
    }

    @Override
    public Double getHeadingVelocity() {
        return drive.getExternalHeadingVelocity();
    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        return Arrays.asList(
                encoderTicksToInches(parallelEncoder.getCurrentPosition() * ParM, false),
                encoderTicksToInches(perpendicularEncoder.getCurrentPosition() * PerpM, true)
        );
    }

    @NonNull
    @Override
    public List<Double> getWheelVelocities() {
        // TODO: If your encoder velocity can exceed 32767 counts / second (such as the REV Through Bore and other
        //  competing magnetic encoders), change Encoder.getRawVelocity() to Encoder.getCorrectedVelocity() to enable a
        //  compensation method

        return Arrays.asList(
                encoderTicksToInches(parallelEncoder.getCorrectedVelocity() * ParM, false),
                encoderTicksToInches(perpendicularEncoder.getCorrectedVelocity() * PerpM, true)
        );
    }
}
package org.firstinspires.ftc.teamcode.vision;

import android.graphics.Canvas;
import android.os.SystemClock;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.util.Tel;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessorImpl;
import org.opencv.core.Mat;

import java.util.ArrayList;

@Config
public class CustomAprilTagImpl extends AprilTagProcessorImpl
{
    long startTime;
    double loopCount;
    public double loopTime, FPS;
    public static boolean detectionEnabled=true;
    double backBoard=8;
    double sideBackBoard = 22.5;
    double tag1 = 5.5 + 1.125;
    double tag2 = 5.5 + 1.125*3 + 3.5;
    double tag3 = 5.5 + 1.125*5 + 3.5*2;
    double tag4 = 5.5 + 1.125*5 + 3.5*2;
    double tag5 = 5.5 + 1.125*3 + 3.5;
    double tag6 = 5.5 + 1.125;
    double cameraX = 0;
    double cameraY = 10;
    public CustomAprilTagImpl(double fx, double fy, double cx, double cy, DistanceUnit outputUnitsLength, AngleUnit outputUnitsAngle, AprilTagLibrary tagLibrary, boolean drawAxes, boolean drawCube, boolean drawOutline, boolean drawTagID, TagFamily tagFamily, int threads)
    {
        super(fx, fy, cx, cy, outputUnitsLength, outputUnitsAngle, tagLibrary, drawAxes, drawCube, drawOutline, drawTagID, tagFamily, threads);
    }

    public void init(int width, int height, CameraCalibration calibration)
    {
        startTime = SystemClock.elapsedRealtime();
        super.init(width, height, calibration);
    }

    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext)
    {
        //make it useless
    }

    public Object processFrame(Mat input, long captureTimeNanos)
    {
        Pose2d poseAtStart = Robot.getInstance().localizer.getPoseEstimate();
        super.processFrame(input, captureTimeNanos);
        Pose2d poseAtEnd = Robot.getInstance().localizer.getPoseEstimate();

        return 0;
    }
}

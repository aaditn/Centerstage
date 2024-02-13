package org.firstinspires.ftc.teamcode.vision;

import android.graphics.Canvas;
import android.os.SystemClock;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.teamcode.util.Tel;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessorImpl;
import org.opencv.core.Mat;
@Config
public class CustomAprilTagImpl extends AprilTagProcessorImpl
{
    long startTime;
    double loopCount;
    public double loopTime, FPS;
    public static boolean detectionEnabled=true;
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

        loopCount++;
        loopTime=(SystemClock.elapsedRealtime()-startTime)/loopCount;
        FPS=1000.0/loopTime;

        if(detectionEnabled)
        {
            return super.processFrame(input, captureTimeNanos);
        }

        return 0;
    }
}

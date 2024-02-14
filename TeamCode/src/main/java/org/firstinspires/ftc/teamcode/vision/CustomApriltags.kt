package org.firstinspires.ftc.teamcode.vision

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessorImpl
import org.opencv.core.Mat

class CustomApriltags
    (fx: Double, fy: Double, cx: Double, cy: Double,
     outputUnitsLength: DistanceUnit?, outputUnitsAngle: AngleUnit?,
     tagLibrary: AprilTagLibrary?, drawAxes: Boolean, drawCube: Boolean,
     drawOutline: Boolean, drawTagID: Boolean, tagFamily: TagFamily?, threads: Int)
    :
    AprilTagProcessorImpl(fx, fy, cx,
    cy, outputUnitsLength, outputUnitsAngle, tagLibrary, drawAxes, drawCube, drawOutline, drawTagID,
    tagFamily, threads)
{

    override fun processFrame(input: Mat, captureTimeNanos: Long) : Any?
    {

        return super.processFrame(input, captureTimeNanos);
    }
}
package org.firstinspires.ftc.teamcode.vision;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;

public class AprilTagPipeline {
    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera
    private AprilTagProcessor aprilTag;

    VisionPortal visionPortal;
    private final boolean USE_EST_HEADING = true;
double est_heading =0;
List<Pose2d> detections = new ArrayList<>();


public List<Pose2d> getPos(){
    return detections;
}
    public void initAprilTag(HardwareMap hardwareMap) {
        detections.add(new Pose2d(0,0,0));
        detections.add(new Pose2d(0,0,0));
        detections.add(new Pose2d(0,0,0));
        detections.add(new Pose2d(0,0,0));
        detections.add(new Pose2d(0,0,0));
        detections.add(new Pose2d(0,0,0));
        // Create the AprilTag processor.
        aprilTag = new AprilTagProcessor.Builder()
                //.setDrawAxes(false)
                //.setDrawCubeProjection(false)
                //.setDrawTagOutline(true)
                //.setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                //.setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                //.setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)

                // == CAMERA CALIBRATION ==
                // If you do not manually specify calibration parameters, the SDK will attempt
                // to load a predefined calibration for your camera.
                //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)

                // ... these parameters are fx, fy, cx, cy.

                .build();

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableCameraMonitoring(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(aprilTag);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Disable or re-enable the aprilTag processor at any time.
        //visionPortal.setProcessorEnabled(aprilTag, true);

    }   // end method initAprilTag()


    /**
     * Add telemetry about AprilTag detections.
     */
    public void telemetryAprilTag(Telemetry telemetry) {

        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());
        double backBoard=30;
        double sideBackBoard = 22.5;
        double tag1 = 5.5 + 1.125;
        double tag2 = 5.5 + 1.125*3 + 3.5;
        double tag3 = 5.5 + 1.125*5 + 3.5*2;
        double tag4 = 5.5 + 1.125*5 + 3.5*2;
        double tag5 = 5.5 + 1.125*3 + 3.5;
        double tag6 = 5.5 + 1.125;
        double cameraX = 0;
        double cameraY = 10;
        for (AprilTagDetection detection : currentDetections) {
            if(detection.id==1){
                detections.set(0,new Pose2d(
                        72-(getX(cameraX,cameraY,detection)+backBoard),
                        72-(sideBackBoard+tag1-getY(cameraX,cameraY,detection)),
                        est_heading
                ));
                telemetry.addData("TAGX", detections.get(0).getX());
                telemetry.addData("TAGY", detections.get(0).getY());
                telemetry.addData("TAGR", detections.get(0).getHeading());
            }
            if(detection.id==2){
                detections.set(1,new Pose2d(
                        72-(getX(cameraX,cameraY,detection)+backBoard),
                        72-(sideBackBoard+tag2-getY(cameraX,cameraY,detection)),
                        est_heading
                ));
            }
            if(detection.id==3){
                detections.set(2,new Pose2d(
                        72-(getX(cameraX,cameraY,detection)+backBoard),
                        72-(sideBackBoard+tag3-getY(cameraX,cameraY,detection)),
                        est_heading
                ));
            }
            if(detection.id==4){
                detections.set(0,new Pose2d(
                        72-(getX(cameraX,cameraY,detection)+backBoard),
                        -(72-(sideBackBoard+tag4-getY(cameraX,cameraY,detection))),
                        est_heading
                ));
            }
            if(detection.id==5){
                detections.set(1,new Pose2d(
                        72-(getX(cameraX,cameraY,detection)+backBoard),
                        -(72-(sideBackBoard+tag5-getY(cameraX,cameraY,detection))),
                        est_heading
                ));
            }
            if(detection.id==6){
                detections.set(2,new Pose2d(
                        72-(getX(cameraX,cameraY,detection)+backBoard),
                        -(72-(sideBackBoard+tag6-getY(cameraX,cameraY,detection))),
                        est_heading
                ));
            }
        }

    }   // end method telemetryAprilTag()
public void setEstHeading(double heading) {
        est_heading = heading;
}
    public double getX (double h, double v, AprilTagDetection detection){
        double d = Math.sqrt(Math.pow(h, 2) + Math.pow(v, 2));
        //d sin alpha = h (or d cos alpha = v)
        //alpha = inverse sin (h/d) or inverse cos (v/d)
        double alpha = Math.toDegrees(Math.asin(h/d));
        double theta = Math.toRadians(alpha + -detection.ftcPose.yaw);
        if(USE_EST_HEADING) {
            theta = Math.toRadians(alpha)-est_heading;
        }
        double x = detection.ftcPose.y;
        double xAdjusted = x + (d*Math.cos(theta));

        return xAdjusted;
    }
    public double getY (double h, double v, AprilTagDetection detection){
        double d = Math.sqrt(Math.pow(h, 2) + Math.pow(v, 2));
        //d sin alpha = h (or d cos alpha = v)
        //alpha = inverse sin (h/d) or inverse cos (v/d)
        double alpha = Math.toDegrees(Math.asin(h/d));
        double theta = Math.toRadians(alpha + -detection.ftcPose.yaw);
        if(USE_EST_HEADING){
             theta = Math.toRadians(alpha)-est_heading;
        }
        double y = detection.ftcPose.x;
        double yAdjusted = y - (d*Math.sin(theta));

        return yAdjusted;
    }
}
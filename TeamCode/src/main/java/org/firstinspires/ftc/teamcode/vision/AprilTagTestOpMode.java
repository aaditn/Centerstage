package org.firstinspires.ftc.teamcode.vision;

import android.util.Size;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.util.Tel;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@TeleOp
@Config
public class AprilTagTestOpMode extends EnhancedOpMode
{
    OpenCvWebcam camera;
    //AprilTagTestPipeline pipeline;
    public static int cameraWidth=240, cameraHeight=180;
    @Override
    public void linearOpMode() {

    }

    @Override
    public void initialize() {
        int monitorID=hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "camera"), monitorID);
        //pipeline=new AprilTagTestPipeline();
        //camera.setPipeline(pipeline);
        //FtcDashboard.getInstance().startCameraStream(camera, 0);
        camera.setMillisecondsPermissionTimeout(5000);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened()
            {
                //camera.startStreaming(CameraConstants.width, CameraConstants.height, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });;
    }
    public void initLoop()
    {
        //Tel.instance().addData("Loop Time", test.loopTime);

        Tel.instance().update();
        //Tel.instance().addData("Detection Count", pipeline.getDetections().size());
        //Tel.instance().addData("FPS", pipeline.FPS);
        //Tel.instance().addData("Loop Time", pipeline.loopTime);
        //Tel.instance().addData("Running", pipeline.running);
        Tel.instance().addData("Camera FPS", camera.getFps());
    }

    public void onEnd()
    {
        //pipeline.finalize();
    }

    @Override
    public void primaryLoop()
    {

    }
}

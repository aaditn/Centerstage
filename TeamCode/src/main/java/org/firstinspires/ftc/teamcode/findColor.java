package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.vision.teamElementColor;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;
@TeleOp
public class findColor extends EnhancedOpMode {
    OpenCvWebcam test;
    teamElementColor TeamElementColor;

    MultipleTelemetry tel;

    @Override
    public void initialize()
    {
        tel=new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);

        int monitorID=hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        test= OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "test"), monitorID);
        TeamElementColor =new teamElementColor(telemetry);
        test.setPipeline(TeamElementColor);

        test.setMillisecondsPermissionTimeout(5000);
        test.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                test.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });
        FtcDashboard.getInstance().startCameraStream(test, 10);
    }

    @Override
    public void primaryLoop()
    {

    }

    @Override
    public void linearOpMode() {

    }

    public void initLoop()
    {
        tel.addData("value", teamElementColor.getValue());
        tel.update();
    }
}

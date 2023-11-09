package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.vision.teamElementDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;
@TeleOp(name = "teamElement")
public class teamElement extends EnhancedOpMode {
    OpenCvWebcam test;
    teamElementDetection TeamElementDetection;

    MultipleTelemetry tel;
    public int elementPos;

    @Override
    public void initialize()
    {
        Context.resetValues();
        Context.isTeamRed=true;
        tel=new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);

        int monitorID=hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        test= OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "test"), monitorID);
        TeamElementDetection =new teamElementDetection(tel);
        test.setPipeline(TeamElementDetection);

        test.setMillisecondsPermissionTimeout(5000);
        test.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened()
            {
                test.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });
        FtcDashboard.getInstance().startCameraStream(test, 10);

        if(TeamElementDetection.centerX < 0) {
            elementPos = 0;
        } else if(TeamElementDetection.centerX < 107){
            elementPos = 1;
        } else if (TeamElementDetection.centerX < 214) {
            elementPos = 2;
        } else {
            elementPos = 3;
        }
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
//        tel.addData("centerX", TeamElementDetection.centerX);
//        tel.addData("elementPos", elementPos);
//        tel.update();
    }
}

package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.vision.teamElementDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Config
@Autonomous(name= "close blue")
public class auto extends LinearOpMode {

    DcMotor slideLeft,slideRight;
    int slidesPos;
    Servo wrist, pusher;
    public static double pusherIn=0.04;
    public static double pusherPushed=0.18;//what is this for

    public static double pusherPrep=0.12;
    public static double pusherOne=0.265;
    public static double pusherTwo=0.35;
    public static double initwrist =.5;
    public static double depositwrist=0.2;

    Intake intake;
    Deposit deposit;
    SampleMecanumDrive m;
    Pose2d startPos = new Pose2d(20,56,Math.toRadians(270));


    @Override
    public void runOpMode() throws InterruptedException {

        Context.resetValues();
        Context.isTeamRed = false;
        Context.tel=new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);
        m= new SampleMecanumDrive(hardwareMap);
        m.setPoseEstimate(startPos);

        Trajectory placePixel2 = m.trajectoryBuilder(startPos)
                .lineToConstantHeading(new Vector2d(16,29))
                .addTemporalMarker(1.3, () -> {intake.setState(Intake.powerState.LOW); intake.updateLoop(); intake.writeLoop();})
                .build();

        Trajectory placePixel1 = m.trajectoryBuilder(startPos)
                .splineToConstantHeading(new Vector2d(30.5, 37), Math.toRadians(-90),
                        m.getVelocityConstraint(30, 1, 15.06),
                        m.getAccelerationConstraint(40))
                .addTemporalMarker(1.3, () -> {intake.setState(Intake.powerState.LOW); intake.updateLoop(); intake.writeLoop();})
                .build();

        Trajectory placePixel3 = m.trajectoryBuilder(startPos)
                .splineTo(new Vector2d(11, 34), Math.toRadians(-135),
                        m.getVelocityConstraint(30, 1, 15.06),
                        m.getAccelerationConstraint(40))
                .addTemporalMarker(1.3, () -> {intake.setState(Intake.powerState.LOW); intake.updateLoop(); intake.writeLoop();})
                .build();

        Trajectory dropPixel2 = m.trajectoryBuilder(placePixel2.end())
                .lineToLinearHeading(new Pose2d(57.5,31,Math.toRadians(180)),
                        m.getVelocityConstraint(47.5, 1.65, 15.06),
                        m.getAccelerationConstraint(45))
                .build();

        Trajectory dropPixel1 = m.trajectoryBuilder(placePixel2.end())
                .lineToLinearHeading(new Pose2d(57.5,37.5,Math.toRadians(180)),
                        m.getVelocityConstraint(47.5, 1.65, 15.06),
                        m.getAccelerationConstraint(45))
                .addTemporalMarker(0.3, () -> intake.setState(Intake.positionState.HIGH))
                .build();

        Trajectory dropPixel3 = m.trajectoryBuilder(placePixel2.end())
                .lineToLinearHeading(new Pose2d(57.5  ,28,Math.toRadians(180)),
                        m.getVelocityConstraint(47.5, 1.65, 15.06),
                        m.getAccelerationConstraint(45))
                .addTemporalMarker(0.3, () -> intake.setState(Intake.positionState.HIGH))
                .build();

        wrist =hardwareMap.get(Servo.class,"wrist");
        pusher=hardwareMap.get(Servo.class, "pusher");
        deposit=new Deposit(hardwareMap);
        intake=new Intake(hardwareMap);

        slideLeft=hardwareMap.get(DcMotor.class, "slide1");
        slideRight=hardwareMap.get(DcMotor.class, "slide2");
        slideRight.setDirection(DcMotorSimple.Direction.REVERSE);
        slideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideLeft.setTargetPosition(slidesPos);
        slideRight.setTargetPosition(slidesPos);
        slideLeft.setPower(1);
        slideRight.setPower(1);
        slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intake.init();
        deposit.init();
        intake.setState(Intake.positionState.HIGH);

        int monitorID=hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        OpenCvWebcam test = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "test"), monitorID);
        teamElementDetection TeamElementDetection =new teamElementDetection(telemetry);
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
        int elementPos=0;
        while(!opModeIsActive()){
            if(TeamElementDetection.centerY < 0) {
                elementPos = 3;
            } else if(TeamElementDetection.centerY < 107){
                elementPos = 1;
            } else if (TeamElementDetection.centerY < 214) {
                elementPos = 2;
            } else {
                elementPos = -1;
            }
            telemetry.addData("element Pos", elementPos);
            telemetry.addData("centerY",TeamElementDetection.centerY);
            telemetry.addData("largest area", TeamElementDetection.getLargestArea());
            telemetry.update();
        }
        waitForStart();
        wrist.setPosition(initwrist);
        pusher.setPosition(pusherPrep);
        intake.setState(Intake.positionState.PURP);
        waitForStart();
        if(elementPos==1) {
            m.followTrajectory(placePixel1);
        }else if (elementPos==2){
            m.followTrajectory(placePixel2);
        } else{
            m.followTrajectory(placePixel3);
        }



        intake.writeLoop();
        intake.updateLoop();
        waitT(3500);
        intake.setState(Intake.powerState.OFF);
        intake.setState(Intake.positionState.HIGH);
        intake.writeLoop();
        intake.updateLoop();
        intake.setManual(true);

        if(elementPos==1) {
            m.followTrajectory(dropPixel1);
        }else if (elementPos==2){
            m.followTrajectory(dropPixel2);
        } else{
            m.followTrajectory(dropPixel3);
        }

        slidesPos=50;
        slideLeft.setTargetPosition(slidesPos);
        slideRight.setTargetPosition(slidesPos);
        slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        deposit.setState(Deposit.RotationState.DEPOSIT_HIGH);
        deposit.updateLoop();
        deposit.writeLoop();
        wrist.setPosition(depositwrist);
        while(slideLeft.getCurrentPosition()-slideLeft.getTargetPosition() >20){

            slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        waitT(2500);
        pusher.setPosition(pusherTwo);

        deposit.updateLoop();
        deposit.writeLoop();
        waitT(2500);
        slidesPos = 200;
        slideLeft.setTargetPosition(slidesPos);
        slideRight.setTargetPosition(slidesPos);

        slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        waitT(2500);
        slidesPos = 0;
        while(Math.abs(slideLeft.getCurrentPosition()-slideLeft.getTargetPosition() )>20){
                deposit.setState(Deposit.RotationState.TRANSFER);
                wrist.setPosition(initwrist);
                pusher.setPosition(pusherIn);

                deposit.updateLoop();
                deposit.writeLoop();

            slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        sleep(1000);
        pusher.setPosition(pusherIn);
        sleep(1000);



    }
    public void waitT(int ticks){
        ElapsedTime  x= new ElapsedTime();
        while(x.milliseconds()<ticks){
            m.update();

            slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }
    public void slidesRaise(){
    }
}
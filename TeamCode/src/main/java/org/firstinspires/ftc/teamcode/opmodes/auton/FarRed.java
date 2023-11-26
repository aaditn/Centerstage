package org.firstinspires.ftc.teamcode.opmodes.auton;

import static org.firstinspires.ftc.teamcode.roadrunner.drive.Trajectories.blueFarStart;

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

import org.firstinspires.ftc.teamcode.roadrunner.drive.Trajectories;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.vision.teamElementDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Config
@Autonomous(name= "Far Red")
public class FarRed extends LinearOpMode {

    DcMotor slideLeft,slideRight;
    int slidesPos;
    Servo wrist, pusher;
    public static double pusherIn=0.04;
    public static double pusherPushed=0.18;//what is this for

    public static double pusherPrep=0.12;
    public static double pusherOne=0.265;
    public static double pusherTwo=0.35;
    public static double initwrist =.5;
    public static double depositwrist=0.3;

    Intake intake;
    Deposit deposit;
    SampleMecanumDrive m;
    // Pose2d startPos = new Pose2d(20,56,Math.toRadians(270));


    @Override
    public void runOpMode() throws InterruptedException {




        Context.resetValues();
        Context.isTeamRed = false;
        Context.tel=new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);
        m= new SampleMecanumDrive(hardwareMap);
        m.setPoseEstimate(blueFarStart);


        TrajectorySequence placePurple1 = m.trajectorySequenceBuilder(blueFarStart)
                .splineTo(new Vector2d(-35, -40), Math.toRadians(90))
                .splineTo(new Vector2d(-38, -34), Math.toRadians(180))
                .addTemporalMarker(2, () -> {intake.setState(Intake.powerState.LOW); intake.updateLoop(); intake.writeLoop();})
                .build();
        TrajectorySequence firstCycle1 = m.trajectorySequenceBuilder(placePurple1.end())
                .addTemporalMarker(0.3, () -> {intake.setState(Intake.positionState.FIVE); intake.updateLoop(); intake.writeLoop();})
                .lineToConstantHeading(new Vector2d(-41, -20))
                .splineToConstantHeading(new Vector2d(-58, -12), Math.toRadians(180))
                .build();
        TrajectorySequence placeYellow1 = m.trajectorySequenceBuilder(firstCycle1.end())
                .addTemporalMarker(0.3, () -> {intake.setState(Intake.powerState.OFF); intake.updateLoop(); intake.writeLoop();})
                .lineTo(new Vector2d(8, -12))
                .splineToConstantHeading(new Vector2d(50, -28), Math.toRadians(0))
                .addTemporalMarker(0.3, () -> intake.setState(Intake.positionState.HIGH))
                .build();
        TrajectorySequence pickAllianceYellow1 = m.trajectorySequenceBuilder(placeYellow1.end())
                .addTemporalMarker(1, () -> {intake.setState(Intake.powerState.INTAKE); intake.updateLoop(); intake.writeLoop();})
                .addTemporalMarker(1, () -> {intake.setState(Intake.positionState.TELE); intake.updateLoop(); intake.writeLoop();})
                .splineToConstantHeading(new Vector2d(30, -61), Math.toRadians(180))
                .build();
        TrajectorySequence placeAllianceYellow1 = m.trajectorySequenceBuilder(pickAllianceYellow1.end())
                .addTemporalMarker(0.3, () -> {intake.setState(Intake.powerState.OFF); intake.updateLoop(); intake.writeLoop();})
                .addTemporalMarker(0.1, () -> {intake.setState(Intake.positionState.HIGH); intake.updateLoop(); intake.writeLoop();})
                .lineTo(new Vector2d(31, -61))
                .splineToConstantHeading(new Vector2d(50, -28), Math.toRadians(0))
                .build();

        TrajectorySequence secondCycle1 = m.trajectorySequenceBuilder(placeAllianceYellow1.end())
                .splineToConstantHeading(new Vector2d(8, -12), Math.toRadians(180))
                .lineToConstantHeading(new Vector2d(-58, -12))
                .build();
        TrajectorySequence park = m.trajectorySequenceBuilder(secondCycle1.end())
                .lineToConstantHeading(new Vector2d(8, -12))
                .splineToConstantHeading(new Vector2d(58, -12), Math.toRadians(0))
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
        //intake.setState(Intake.positionState.HIGH);

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
        while(!opModeIsActive()&&!isStopRequested()){
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

        wrist.setPosition(initwrist);
        pusher.setPosition(pusherPrep);
        intake.setState(Intake.positionState.PURP);
        intake.updateLoop();
        intake.writeLoop();
        waitT(1000);

        m.followTrajectorySequence(placePurple1);

        intake.writeLoop();
        intake.updateLoop();
        waitT(1000);
        intake.setState(Intake.powerState.OFF);
        intake.setState(Intake.positionState.HIGH);
        intake.writeLoop();
        intake.updateLoop();

        waitT(1000);

        m.followTrajectorySequence(firstCycle1);

        intake.setState(Intake.powerState.INTAKE);
        intake.writeLoop();
        intake.updateLoop();
        waitT(1000);

        m.followTrajectorySequence(placeYellow1);

        slidesPos=100;
        waitT(1000);

        wrist.setPosition(depositwrist);
        waitT(250);

        deposit.setState(Deposit.RotationState.DEPOSIT_HIGH);
        deposit.updateLoop();
        deposit.writeLoop();
        waitT(1500);

        pusher.setPosition(pusherTwo);
        deposit.updateLoop();
        deposit.writeLoop();
        waitT(1000);

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

        sleep(500);
        pusher.setPosition(pusherIn);

        m.followTrajectorySequence(pickAllianceYellow1);

        m.followTrajectorySequence(placeAllianceYellow1);

        slidesPos=100;
        waitT(1000);

        wrist.setPosition(depositwrist);
        waitT(250);

        deposit.setState(Deposit.RotationState.DEPOSIT_HIGH);
        deposit.updateLoop();
        deposit.writeLoop();
        waitT(1500);

        pusher.setPosition(pusherTwo);
        deposit.updateLoop();
        deposit.writeLoop();
        waitT(1000);

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

        m.followTrajectorySequence(secondCycle1);
        m.followTrajectorySequence(park);

        /*if(elementPos==1) {
            m.followTrajectorySequence(sequence[0]);
        }else if (elementPos==2){
            m.followTrajectorySequence(sequence[0]);
        } else{

        }




        intake.writeLoop();
        intake.updateLoop();
        waitT(1000);
        intake.setState(Intake.powerState.OFF);
        intake.setState(Intake.positionState.HIGH);
        intake.writeLoop();
        intake.updateLoop();
        intake.setManual(true);

        if(elementPos==1) {
            m.followTrajectory(s);
        } else if (elementPos==2){
            m.followTrajectory(yellowPixel2);
        } else{
            m.followTrajectory(yellowPixel3);
        }

        slidesPos=100;
        waitT(1000);

        wrist.setPosition(depositwrist);
        waitT(250);

        deposit.setState(Deposit.RotationState.DEPOSIT_HIGH);
        deposit.updateLoop();
        deposit.writeLoop();
        waitT(1500);

        pusher.setPosition(pusherTwo);
        deposit.updateLoop();
        deposit.writeLoop();
        waitT(1000);

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

        sleep(500);
        pusher.setPosition(pusherIn);
        sleep(500);


         */

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
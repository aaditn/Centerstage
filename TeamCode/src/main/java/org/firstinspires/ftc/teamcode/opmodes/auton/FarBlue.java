package org.firstinspires.ftc.teamcode.opmodes.auton;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskListBuilder;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.vision.TeamElementDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.List;

@Config
@Autonomous(name= "Far Blue")
public class FarBlue extends EnhancedOpMode {

    Robot robot;

    Intake intake;
    Deposit deposit;
    Slides slides;
    Pose2d startPos = new Pose2d(-36,61,Math.toRadians(270));
    TaskScheduler scheduler;
    TaskListBuilder builder;
    int elementPos;

    List<Task> slideupbase;
    List<Task> slidedown;
    boolean macroRunning=false;



    public void waitT(int ticks)
    {
        ElapsedTime  x= new ElapsedTime();
        while(x.milliseconds()<ticks)
        {

        }
    }
    public void waitOnDT()
    {
        while(robot.isBusy()&&opModeIsActive())
        {
            //stall
        }
    }

    public void waitOnMacro()
    {
        while(macroRunning&&opModeIsActive())
        {
            //stall
        }
    }

    @Override
    public void linearOpMode() {

        Trajectory placePurple1 = robot.trajectoryBuilder(startPos)
                //CHANGE
                .lineToLinearHeading(new Pose2d(-40, 23, Math.toRadians(140)))
                .addTemporalMarker(2, () -> { intake.updateLoop(); intake.writeLoop();})
                .build();
        Trajectory placePurple2 = robot.trajectoryBuilder(startPos)
                //CHANGE
                .lineToLinearHeading(new Pose2d(-40, 23, Math.toRadians(140)))
                .addTemporalMarker(2, () -> { intake.updateLoop(); intake.writeLoop();})
                .build();
        Trajectory placePurple3 = robot.trajectoryBuilder(startPos)
                .lineToLinearHeading(new Pose2d(-40, 23, Math.toRadians(140)))
                .addTemporalMarker(2, () -> { intake.updateLoop(); intake.writeLoop();})
                .build();

        Trajectory firstCycle3 = robot.trajectoryBuilder(placePurple3.end())
                .lineToConstantHeading(new Vector2d(-40, 8))
                .lineToLinearHeading(new Pose2d(-59, 8,Math.toRadians(180)))
                .build();

        Trajectory placeYellow3 = robot.trajectoryBuilder(firstCycle3.end())
                .addTemporalMarker(2, () -> {intake.setState(Intake.PowerState.OFF);intake.updateLoop(); intake.writeLoop();})
                .lineTo(new Vector2d(8, 12))
                .splineToConstantHeading(new Vector2d(52, 28), Math.toRadians(0))
                .addTemporalMarker(0.3, () -> intake.setState(Intake.PositionState.HIGH))
                .build();
        Trajectory strafeWhite3 = robot.trajectoryBuilder(placeYellow3.end())
                .strafeRight(1)
                .build();

        Trajectory intakeAllianceYellow3 = robot.trajectoryBuilder(placeYellow3.end())
                .addTemporalMarker(1, () -> {intake.setState(Intake.PowerState.INTAKE); intake.updateLoop(); intake.writeLoop();})
                .addTemporalMarker(1, () -> {intake.setState(Intake.PositionState.TELE); intake.updateLoop(); intake.writeLoop();})
                .splineToConstantHeading(new Vector2d(30, 61), Math.toRadians(180))
                .build();
        Trajectory placeAllianceYellow3 = robot.trajectoryBuilder(intakeAllianceYellow3.end())
                .addTemporalMarker(0.3, () -> {intake.setState(Intake.PowerState.OFF); intake.updateLoop(); intake.writeLoop();})
                .addTemporalMarker(0.1, () -> {intake.setState(Intake.PositionState.HIGH); intake.updateLoop(); intake.writeLoop();})
                .lineToConstantHeading(new Vector2d(31, 61))
                .splineToConstantHeading(new Vector2d(54, 28), Math.toRadians(0))
                .build();

        waitForStart();

        //deposit.setState(Deposit.RotationState.TRANSFER);
        deposit.setState(Deposit.WristState.TRANSFER);
        deposit.setState(Deposit.PusherState.EXTENDED);
        intake.setState(Intake.PositionState.PURP);
        waitT(1000);

        if(elementPos==1) {
            robot.followTrajectoryAsync(placePurple1);
        }else if (elementPos==2){
            robot.followTrajectoryAsync(placePurple2);
        } else{
            robot.followTrajectoryAsync(placePurple3);
        }
/*
        intake.writeLoop();
        intake.updateLoop();
        waitT(1000);
        intake.setState(Intake.powerState.OFF);
        intake.setState(Intake.PositionState.HIGH);
        intake.writeLoop();
        intake.updateLoop();

        waitT(1000);

        robot.followTrajectory(firstCycle3);
        intake.setState(Intake.PositionState.FIVE); intake.updateLoop(); intake.writeLoop();
        intake.setState(Intake.powerState.INTAKE);
        intake.writeLoop();
        intake.updateLoop();
        waitT(1000);

        robot.followTrajectory(placeYellow3);



        slidesPos=100;
        waitT(1000);

        wrist.setPosition(depositwrist);
        waitT(250);

        deposit.setState(Deposit.RotationState.DEPOSIT_HIGH);
        deposit.updateLoop();
        deposit.writeLoop();
        waitT(1500);

        pusher.setPosition(pusherOne);
        deposit.updateLoop();
        deposit.writeLoop();

        sleep(300);

        robot.followTrajectory(strafeWhite3);
        pusher.setPosition(pusherTwo);

        waitT(1000);

        slidesPos = 200;
        slideLeft.setTargetPosition(slidesPos);
        slideRight.setTargetPosition(slidesPos);
        waitT(500);
        slidesPos = 0;
        slideLeft.setTargetPosition(slidesPos);
        slideRight.setTargetPosition(slidesPos);

        wrist.setPosition(initwrist);
        waitT(50);
        deposit.setState(Deposit.RotationState.TRANSFER);

        pusher.setPosition(pusherIn);
        intake.setState(Intake.PositionState.TWO);
        intake.updateLoop();
        intake.writeLoop();

        slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        waitT(500);
        pusher.setPosition(pusherIn);

        robot.followTrajectory(intakeAllianceYellow3);

        intake.setState(Intake.PositionState.ONE);
        intake.updateLoop();
        intake.writeLoop();
        robot.followTrajectory(placeAllianceYellow3);


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

        slidesPos = 200;
        slideLeft.setTargetPosition(slidesPos);
        slideRight.setTargetPosition(slidesPos);
        waitT(500);
        slidesPos = 0;
        slideLeft.setTargetPosition(slidesPos);
        slideRight.setTargetPosition(slidesPos);
        deposit.setState(Deposit.RotationState.TRANSFER);
        deposit.updateLoop();
        deposit.writeLoop();
        wrist.setPosition(initwrist);
        pusher.setPosition(pusherIn);

        deposit.updateLoop();
        deposit.writeLoop();

        slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        waitT(500);

 */

    }

    @Override
    public void initialize() {
        this.setLoopTimes(10);

        robot=new Robot(this);
        builder=new TaskListBuilder(this);
        scheduler=new TaskScheduler();

        deposit=robot.deposit;
        intake=robot.intake;
        slides=robot.slides;

        intake.init();
        deposit.init();

        slideupbase=builder.createNew()
                .executeCode(()->macroRunning=true)
                .moduleAction(deposit, Deposit.WristState.CRADLE)
                .delay(100)
                .moduleAction(slides, Slides.SlideState.AUTO_LOW)
                //.moduleAction(deposit, Deposit.WristState.DEPOSIT)
                .awaitPreviousModuleActionCompletion()
                .moduleAction(deposit, Deposit.RotationState.DEPOSIT_HIGH)
                .delay(100)
                .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                .executeCode(()->macroRunning=false)
                .build();

        slidedown=builder.createNew()
                .executeCode(()->macroRunning=true)
                .moduleAction(deposit, Deposit.RotationState.DEPOSIT_MID)
                .delay(300)
                .moduleAction(slides, Slides.SlideState.GROUND)
                .moduleAction(deposit, Deposit.PusherState.IN)
                .await(()->slides.currentPosition()<100)
                .moduleAction(deposit, Deposit.RotationState.TRANSFER)
                .moduleAction(deposit, Deposit.WristState.TRANSFER)
                .await(()->slides.getStatus()== Module.Status.IDLE)
                .executeCode(()->macroRunning=false)
                .build();
    }

    public void initLoop()
    {
        if(robot.teamElementDetector.centerY < 0) {
            elementPos = 3;
        } else if(robot.teamElementDetector.centerY < 107){
            elementPos = 1;
        } else if (robot.teamElementDetector.centerY < 214) {
            elementPos = 2;
        } else {
            elementPos = -1;
        }
        telemetry.addData("element Pos", elementPos);
        telemetry.addData("centerY",robot.teamElementDetector.centerY);
        telemetry.addData("largest area", robot.teamElementDetector.getLargestArea());
        robot.initLoop();
    }
    @Override
    public void primaryLoop() {
        robot.primaryLoop();
    }
}
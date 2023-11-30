package org.firstinspires.ftc.teamcode.opmodes.auton;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskListBuilder;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

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
    public void waitT(int ticks)
    {
        ElapsedTime x= new ElapsedTime();
        while(x.milliseconds()<ticks)
        {

        }
    }

    @Override
    public void linearOpMode() {

        Trajectory placePurple1 = robot.trajectoryBuilder(startPos)
                //CHANGE
                .lineToLinearHeading(new Pose2d(-40, 23, Math.toRadians(180)))
                .build();
        Trajectory placePurple2 = robot.trajectoryBuilder(startPos)
                //CHANGE
                .lineToLinearHeading(new Pose2d(-37, 13, Math.toRadians(90)))
                .build();
        Trajectory placePurple3 = robot.trajectoryBuilder(startPos)
                .lineToLinearHeading(new Pose2d(-37, 23, Math.toRadians(180)))
                .build();

//        Trajectory intakeWhite1 = robot.trajectoryBuilder(placePurple3.end())
//                //CHANGE
////                .lineToConstantHeading(new Vector2d(-40, 8))
////                .lineToLinearHeading(new Pose2d(-59, 8,Math.toRadians(180)))
//                .build();
//        Trajectory intakeWhite2 = robot.trajectoryBuilder(placePurple3.end())
//                //CHANGE
////                .lineToConstantHeading(new Vector2d(-40, 8))
////                .lineToLinearHeading(new Pose2d(-59, 8,Math.toRadians(180)))
//                .build();
        Trajectory intakeWhite3 = robot.trajectoryBuilder(placePurple3.end())
                .lineToConstantHeading(new Vector2d(-40,  22))
//                .splineToConstantHeading(new Vector2d(-46, 13), Math.toRadians(180))
                .splineToSplineHeading(new Pose2d(-65, 6,Math.toRadians(180)), Math.toRadians(180))
                .build();
        Trajectory avoid1 = robot.trajectoryBuilder(placePurple1.end())
                .lineToLinearHeading(new Pose2d(-36,  8,Math.toRadians(180)))

                .build();
        Trajectory avoid3 = robot.trajectoryBuilder(placePurple3.end())
                .lineToLinearHeading(new Pose2d(-36,  8,Math.toRadians(180)))

                .build();
        Trajectory avoid2 = robot.trajectoryBuilder(placePurple2.end())
                .lineToLinearHeading(new Pose2d(-36,  8,Math.toRadians(90)))
                .build();
        Trajectory placeWhite1 = robot.trajectoryBuilder(intakeWhite3.end())
                //CHANGE

                .lineToLinearHeading(new Pose2d(31, 12,Math.toRadians(180)))

                .build();
        Trajectory placeWhite2 = robot.trajectoryBuilder(intakeWhite3.end())
                //CHANGE

                .lineToLinearHeading(new Pose2d(31, 12,Math.toRadians(180)))
                .build();
        Trajectory placeWhite3 = robot.trajectoryBuilder(avoid3.end())

                .lineToLinearHeading(new Pose2d(31, 12,Math.toRadians(180)))
                .build();

        Trajectory strafeYellow1 = robot.trajectoryBuilder(placeWhite3.end())
                //CHANGE
                .splineToLinearHeading(new Pose2d(51, 34,Math.toRadians(180)), Math.toRadians(180))
                .build();
        Trajectory strafeYellow2 = robot.trajectoryBuilder(placeWhite3.end())
                //CHANGE
                .splineToLinearHeading(new Pose2d(51, 30,Math.toRadians(180)), Math.toRadians(180))
                .build();
        Trajectory strafeYellow3 = robot.trajectoryBuilder(placeWhite3.end())
                .splineToLinearHeading(new Pose2d(51, 26,Math.toRadians(180)), Math.toRadians(180))
                .build();

        Trajectory intakeAllianceYellow1 = robot.trajectoryBuilder(placeWhite3.end())
                .splineToConstantHeading(new Vector2d(30, 61), Math.toRadians(180))
                .build();
        Trajectory intakeAllianceYellow2 = robot.trajectoryBuilder(placeWhite3.end())
                .splineToConstantHeading(new Vector2d(30, 61), Math.toRadians(180))
                .build();
        Trajectory intakeAllianceYellow3 = robot.trajectoryBuilder(placeWhite3.end())
                .splineToConstantHeading(new Vector2d(30, 61), Math.toRadians(180))
                .build();

        Trajectory placeAllianceYellow1 = robot.trajectoryBuilder(intakeAllianceYellow3.end())
                .lineToConstantHeading(new Vector2d(31, 61))
                .splineToConstantHeading(new Vector2d(54, 28), Math.toRadians(0))
                .build();
        Trajectory placeAllianceYellow2 = robot.trajectoryBuilder(intakeAllianceYellow3.end())
                .lineToConstantHeading(new Vector2d(31, 61))
                .splineToConstantHeading(new Vector2d(54, 28), Math.toRadians(0))
                .build();
        Trajectory placeAllianceYellow3 = robot.trajectoryBuilder(intakeAllianceYellow3.end())
                .lineToConstantHeading(new Vector2d(31, 61))
                .splineToConstantHeading(new Vector2d(54, 28), Math.toRadians(0))
                .build();

        waitForStart();
        robot.setPoseEstimate(startPos);

        deposit.setState(Deposit.RotationState.TRANSFER);
        deposit.setState(Deposit.WristState.TRANSFER);
        deposit.setState(Deposit.PusherState.EXTENDED);
        intake.setState(Intake.PositionState.PURP);
        waitOnMacro();
        if(elementPos==1) {
            robot.followTrajectoryAsync(placePurple1);
        }else if (elementPos==2){
            robot.followTrajectoryAsync(placePurple2);
        } else{
            robot.followTrajectoryAsync(placePurple3);
        }
        waitOnDT();
        intake.setState(Intake.PositionState.HIGH);
        waitOnMacro();

        //intake white
//        if (elementPos==1){
//            robot.followTrajectory(intakeWhite1);
//        } else if (elementPos==2){
//            robot.followTrajectory(intakeWhite2);
//        } else {
//            robot.followTrajectory(intakeWhite3);
////        }
//        waitOnDT();
//        intake.setState(Intake.PositionState.FIVE);
//        intake.setState(Intake.PowerState.INTAKE);
//
//        waitT(1000);

        if(elementPos==1) {
            robot.followTrajectory(avoid1);
        }else if (elementPos==2){
            robot.followTrajectory(avoid2);
            robot.turn(90);
        } else{
            robot.followTrajectory(avoid3);

        }

        //deposit white pixel
        if(elementPos==1) {
            robot.followTrajectoryAsync(placeWhite1);
        }else if (elementPos==2){
            robot.followTrajectoryAsync(placeWhite2);
        } else{
            robot.followTrajectoryAsync(placeWhite3);
        }
        waitOnDT();

        scheduler.scheduleTaskList(slideupbase);
        waitOnMacro();

        //strafe and deposit yellow
        if(elementPos==1) {
            robot.followTrajectoryAsync(strafeYellow1);
        }else if (elementPos==2){
            robot.followTrajectoryAsync(strafeYellow2);
        } else{
            robot.followTrajectoryAsync(strafeYellow3);
        }
        waitOnDT();
        deposit.setState(Deposit.PusherState.TWO);
        waitT(2000);
        waitOnMacro();
        scheduler.scheduleTaskList(slidedown);
        waitOnMacro();
//
//        //get alliance yellow
//        intake.setState(Intake.PositionState.TWO);
//        if(elementPos==1) {
//            robot.followTrajectoryAsync(intakeAllianceYellow1);
//        }else if (elementPos==2){
//            robot.followTrajectoryAsync(intakeAllianceYellow2);
//        } else{
//            robot.followTrajectoryAsync(intakeAllianceYellow3);
//        }
//        waitOnDT();
//        intake.setState(Intake.PowerState.INTAKE);
//        waitT(2500);
//        intake.setState(Intake.PowerState.OFF);
//
//        //deposit alliance yellow
//        if(elementPos==1) {
//            robot.followTrajectoryAsync(placeAllianceYellow1);
//        }else if (elementPos==2){
//            robot.followTrajectoryAsync(placeAllianceYellow2);
//        } else{
//            robot.followTrajectoryAsync(placeAllianceYellow3);
//        }
//        waitOnDT();
//        scheduler.scheduleTaskList(slideupbase);
//        waitOnMacro();
//        deposit.setState(Deposit.PusherState.TWO);
//        waitT(500);
//        scheduler.scheduleTaskList(slidedown);

    }

    @Override
    public void initialize() {
        this.setLoopTimes(10);

        robot=new Robot(this);
        Context.isTeamRed=false;
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
                .moduleAction(slides, Slides.SlideState.SLIDE_UP)
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
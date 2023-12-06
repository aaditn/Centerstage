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
public class  FarBlue extends EnhancedOpMode {

    Robot robot;

    Intake intake;
    Deposit deposit;
    Slides slides;
    Pose2d startPos = new Pose2d(-36,61,Math.toRadians(270));
    TaskScheduler scheduler;
    TaskListBuilder builder;
    int elementPos;

    List<Task> slideupbase;
    List<Task> slideupbase2;
    List<Task> slidedown;
    boolean macroRunning=false;

    public void waitOnDT()
    {
        while(robot.isBusy()&&opModeIsActive() && !isStopRequested())
        {
            //stall
        }
    }
    public void waitOnMacro()
    {
        while(macroRunning&&opModeIsActive() && !isStopRequested())
        {
            //stall
        }
    }
    public void waitT(int ticks)
    {
        ElapsedTime x= new ElapsedTime();
        while(x.milliseconds()<ticks &&!isStopRequested())
        {

        }
    }

    public void onStart()
    {
        robot.closeCameras();
    }
    @Override
    public void linearOpMode() {


        Trajectory placePurple1Init = robot.trajectoryBuilder(startPos)
                .lineToConstantHeading(new Vector2d(-36, 50))
                .build();
        Trajectory placePurple1 = robot.trajectoryBuilder(placePurple1Init.end())
                .lineToLinearHeading(new Pose2d(-33, 28, Math.toRadians(0)))
                .build();
        Trajectory placePurple2 = robot.trajectoryBuilder(startPos)
                .lineToLinearHeading(new Pose2d(-37, 12.25, Math.toRadians(90)))//90
                .build();
        Trajectory placePurple3 = robot.trajectoryBuilder(startPos)
                .lineToLinearHeading(new Pose2d(-37, 23, Math.toRadians(180)))
                .build();

        Trajectory avoid1 = robot.trajectoryBuilder(placePurple1.end())
                .lineToLinearHeading(new Pose2d(-40, 5, Math.toRadians(180)))
                .build();
        Trajectory avoid2 = robot.trajectoryBuilder(placePurple2.end())
                .lineToLinearHeading(new Pose2d(-36, 5, Math.toRadians(180)))
                .build();
        Trajectory avoid3 = robot.trajectoryBuilder(placePurple3.end())
                .lineToLinearHeading(new Pose2d(-36, 5, Math.toRadians(180)))
                .build();

        Trajectory intakeWhite1 = robot.trajectoryBuilder(avoid1.end())
                .lineToConstantHeading(new Vector2d(-67, 11))
                .build();
        Trajectory intakeWhite2 = robot.trajectoryBuilder(avoid2.end())
                .lineToConstantHeading(new Vector2d(-67, 11))
                .build();
        Trajectory intakeWhite3 = robot.trajectoryBuilder(avoid3.end())
                .lineToConstantHeading(new Vector2d(-65, 12))
                .build();

        Trajectory prepWhite1 = robot.trajectoryBuilder(intakeWhite1.end())
                .lineToLinearHeading(new Pose2d(31, 12, Math.toRadians(180)))
                .build();
        Trajectory prepWhite2 = robot.trajectoryBuilder(intakeWhite2.end())
                .lineToLinearHeading(new Pose2d(31, 12, Math.toRadians(180)))
                .build();
        Trajectory prepWhite3 = robot.trajectoryBuilder(intakeWhite3.end())
                .lineToLinearHeading(new Pose2d(31, 15, Math.toRadians(180)))
                .build();

        Trajectory placeWhite1 = robot.trajectoryBuilder(prepWhite1.end())
                .lineToLinearHeading(new Pose2d(42, 43.5,Math.toRadians(180)),
                        robot.getVelocityConstraint(30, 1.65, 15.06),
                        robot.getAccelerationConstraint(30))
                .build();
        Trajectory placeWhite2 = robot.trajectoryBuilder(prepWhite2.end())
                .lineToLinearHeading(new Pose2d(42, 38,Math.toRadians(180)),
                        robot.getVelocityConstraint(30, 1.65, 15.06),
                        robot.getAccelerationConstraint(30))
                .build();
        Trajectory placeWhite3 = robot.trajectoryBuilder(prepWhite3.end())
                .lineToLinearHeading(new Pose2d(45, 37,Math.toRadians(180)),
                        robot.getVelocityConstraint(30, 1.65, 15.06),
                        robot.getAccelerationConstraint(33))
                .build();

        Trajectory strafeYellow1 = robot.trajectoryBuilder(placeWhite3.end())
                .lineToConstantHeading(new Vector2d(42, 43.5))
                .build();
        Trajectory strafeYellow2 = robot.trajectoryBuilder(placeWhite3.end())
                .lineToConstantHeading(new Vector2d(42, 43.5))
                .build();
        Trajectory strafeYellow3 = robot.trajectoryBuilder(placeWhite3.end())
                .lineToConstantHeading(new Vector2d(45, 27))
                .build();


        waitForStart();
        robot.setPoseEstimate(startPos);

        deposit.setState(Deposit.RotationState.TRANSFER);
        deposit.setState(Deposit.WristState.TRANSFER);
        deposit.setState(Deposit.PusherState.IN);
        intake.setState(Intake.PositionState.PURP);
        waitT(300);
        if (elementPos == 1) {
            robot.followTrajectoryAsync(placePurple1Init);
            waitOnDT();
            robot.followTrajectoryAsync(placePurple1);
        } else if (elementPos == 2) {
            robot.followTrajectoryAsync(placePurple2);
        } else {
            robot.followTrajectoryAsync(placePurple3);
        }
        waitOnDT();

        intake.setState(Intake.PositionState.HIGH);
        waitOnMacro();

        waitT(1000);

        if (elementPos == 1) {
            robot.followTrajectoryAsync(avoid1);
        } else if (elementPos == 2) {
            //robot.followTrajectoryAsync(avoid2);
            robot.turnAsync(Math.toRadians(90));
        } else {
            robot.followTrajectoryAsync(avoid3);
        }
        waitOnDT();

        //intake white
        if (elementPos == 1) {
            robot.followTrajectory(intakeWhite1);
        } else if (elementPos == 2) {
            robot.followTrajectory(intakeWhite2);
        } else {
            robot.followTrajectory(intakeWhite3);
        }
    waitOnDT();
    intake.setState(Intake.PositionState.FIVE);
    waitT(500);
    intake.setState(Intake.PowerState.INTAKE);
    waitT(1700);
    intake.setState(Intake.PowerState.OFF);


    if (elementPos == 1) {
        robot.followTrajectoryAsync(prepWhite1);
        waitOnDT();
        robot.followTrajectoryAsync(placeWhite1);
    } else if (elementPos == 2) {
        robot.followTrajectoryAsync(prepWhite2);
        waitOnDT();
        robot.followTrajectoryAsync(placeWhite2);
    } else {
        robot.followTrajectoryAsync(prepWhite3);
        waitOnDT();
        robot.followTrajectoryAsync(placeWhite3);
    }
    waitOnDT();

    scheduler.scheduleTaskList(slideupbase);
    waitOnMacro(); //buh is this even doing anything T-T
    waitT(2000);
    deposit.setState(Deposit.PusherState.ONE);
    waitT(3000);
    //strafe and deposit yellow
    if (elementPos == 1) {
        robot.followTrajectoryAsync(strafeYellow1);
    } else if (elementPos == 2) {
        robot.followTrajectoryAsync(strafeYellow2);
    } else {
        robot.followTrajectoryAsync(strafeYellow3);
    }
    waitOnDT();

    deposit.setState(Deposit.PusherState.TWO);
    waitT(1000);
    //park

    scheduler.scheduleTaskList(slidedown);
    waitOnMacro();


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
                .delay(200)
                .moduleAction(deposit, Deposit.RotationState.DEPOSIT_MID)
                .delay(300)
                .moduleAction(slides, Slides.SlideState.AUTO_LOW)
                //.awaitPreviousModuleActionCompletion()
                .delay(200)
                .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                .delay(100)
                .moduleAction(deposit, Deposit.RotationState.DEPOSIT_HIGH)
                .delay(300)
                .executeCode(()->macroRunning=false)
                .build();
        slideupbase2=builder.createNew()
                .executeCode(()->macroRunning=true)
                .moduleAction(deposit, Deposit.WristState.CRADLE)
                .delay(200)
                .moduleAction(deposit, Deposit.RotationState.DEPOSIT_MID)
                .delay(300)
                .moduleAction(slides, Slides.SlideState.AUTO_TWO)
                .awaitPreviousModuleActionCompletion()
                //.delay(100)
                .moduleAction(deposit, Deposit.RotationState.DEPOSIT_HIGH)
                .delay(100)
                .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                .delay(300)
                .executeCode(()->macroRunning=false)
                .build();

        slidedown=builder.createNew()
                .executeCode(()->macroRunning=true)
                .moduleAction(deposit, Deposit.RotationState.DEPOSIT_MID)
                .delay(400)
                .moduleAction(slides, Slides.SlideState.GROUND)
                .moduleAction(deposit, Deposit.PusherState.IN)
                .await(()->slides.currentPosition()<120)
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
        } else {
            elementPos = 2;
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
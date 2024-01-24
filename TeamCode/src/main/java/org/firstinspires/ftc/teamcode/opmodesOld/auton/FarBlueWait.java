package org.firstinspires.ftc.teamcode.opmodesOld.auton;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.modulesOld.DepositOld;
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
public class  FarBlueWait extends EnhancedOpMode {

    Robot robot;

    Intake intake;
    DepositOld deposit;
    Slides slides;
    Pose2d startPos = new Pose2d(-36,61,Math.toRadians(270));
    TaskScheduler scheduler;
    TaskListBuilder builder;
    int elementPos;

    List<Task> slideupbase;
    List<Task> slideupbase2;
    List<Task> slidedown;
    List<Task> shiftdeposit;
    List<Task> colorsensorcorrection;
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


        Trajectory prepWhite1 = robot.trajectoryBuilder(avoid1.end())
                .lineToLinearHeading(new Pose2d(31, 12, Math.toRadians(180)))
                .build();
        Trajectory prepWhite2 = robot.trajectoryBuilder(avoid2.end())
                .lineToLinearHeading(new Pose2d(31, 12, Math.toRadians(180)))
                .build();
        Trajectory prepWhite3 = robot.trajectoryBuilder(avoid3.end())
                .lineToLinearHeading(new Pose2d(31, 15, Math.toRadians(180)))
                .build();

        Trajectory placeWhite1 = robot.trajectoryBuilder(prepWhite1.end())
                .lineToLinearHeading(new Pose2d(47, 43,Math.toRadians(180)),
                        robot.getVelocityConstraint(30, 1.65, 15.06),
                        robot.getAccelerationConstraint(30))
                .build();
        Trajectory placeWhite2 = robot.trajectoryBuilder(prepWhite2.end())
                .lineToLinearHeading(new Pose2d(48, 36,Math.toRadians(180)),
                        robot.getVelocityConstraint(30, 1.65, 15.06),
                        robot.getAccelerationConstraint(30))
                .build();
        Trajectory placeWhite3 = robot.trajectoryBuilder(prepWhite3.end())
                .lineToLinearHeading(new Pose2d(54, 31.5,Math.toRadians(180)),
                        robot.getVelocityConstraint(30, 1.65, 15.06),
                        robot.getAccelerationConstraint(33))
                .build();
        Trajectory park1= robot.trajectoryBuilder(placeWhite1.end())
                        .strafeRight(24)
                                .build();

        Trajectory park2= robot.trajectoryBuilder(placeWhite1.end())
                .strafeRight(18)
                .build();
        Trajectory park3= robot.trajectoryBuilder(placeWhite1.end())
                .strafeRight(12)
                .build();





        waitForStart();

        waitT(10000);

        robot.setPoseEstimate(startPos);

        deposit.setState(DepositOld.RotationState.TRANSFER);
        deposit.setState(DepositOld.WristState.TRANSFER);
        deposit.setState(DepositOld.PusherState.IN);
        intake.setState(Intake.OldPositionState.PURP);
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

        intake.setState(Intake.OldPositionState.HIGH);
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


        if (elementPos == 1) {
            robot.followTrajectoryAsync(prepWhite1);
            waitOnDT();

            scheduler.scheduleTaskList(slideupbase);
            robot.followTrajectoryAsync(placeWhite1);
        } else if (elementPos == 2) {
            robot.followTrajectoryAsync(prepWhite2);
            waitOnDT();

            scheduler.scheduleTaskList(slideupbase);
            robot.followTrajectoryAsync(placeWhite2);
        } else {
            robot.followTrajectoryAsync(prepWhite3);
            waitOnDT();

            scheduler.scheduleTaskList(slideupbase);
            robot.followTrajectoryAsync(placeWhite3);
        }
        waitOnDT();
        waitOnMacro(); //buh is this even doing anything T-T

        deposit.setState(DepositOld.PusherState.TWO);
        waitT(1000);
        //park

        scheduler.scheduleTaskList(slidedown);
        intake.setState(Intake.OldPositionState.HIGH);
        deposit.setState(DepositOld.PusherState.IN);
        waitOnMacro();

        waitT(1000);
        if (elementPos == 1) {
            robot.followTrajectoryAsync(park1);

        } else if (elementPos == 2) {
            robot.followTrajectoryAsync(park2);
        } else {
            robot.followTrajectoryAsync(park3);
        }
        waitOnDT();
    }


    @Override
    public void initialize() {
        this.setLoopTimes(10);

        robot=new Robot(this);
        Context.isTeamRed=false;
        builder=new TaskListBuilder(this);
        scheduler=new TaskScheduler();

        //deposit=robot.deposit;
        intake=robot.intake;
        slides=robot.slides;

        intake.init();
        deposit.init();




        shiftdeposit=builder.createNew()
                .await(()->robot.getPoseEstimate().getY()>30)
                .moduleAction(deposit, DepositOld.RotationState.DEPOSIT_HIGH)
                .build();

        slideupbase=builder.createNew()
                //.delay(2000)
                .moduleAction(intake, Intake.PowerState.OFF)
                .executeCode(()->macroRunning=true)
                .moduleAction(deposit, DepositOld.WristState.CRADLE_AUTO)
                .delay(200)
                //.moduleAction(deposit, Deposit.RotationState.DEPOSIT_MID)
                //.delay(300)
                .moduleAction(slides, Slides.SlideState.AUTO_LOW)
                //.awaitPreviousModuleActionCompletion()
                .delay(200)
                .moduleAction(deposit, DepositOld.WristState.DEPOSIT)
                .delay(100)
                .moduleAction(deposit, DepositOld.RotationState.DEPOSIT_HIGH)
                .delay(300)
                .executeCode(()->macroRunning=false)
                .build();
        slideupbase2=builder.createNew()
                .executeCode(()->macroRunning=true)
                .moduleAction(deposit, DepositOld.WristState.CRADLE_AUTO)
                .delay(200)
                //.moduleAction(deposit, Deposit.RotationState.DEPOSIT_MID)
                //.delay(300)
                .moduleAction(slides, Slides.SlideState.AUTO_TWO)
                .awaitPreviousModuleActionCompletion()
                //.delay(100)
                .moduleAction(deposit, DepositOld.RotationState.DEPOSIT_HIGH)
                .delay(100)
                .moduleAction(deposit, DepositOld.WristState.DEPOSIT)
                .delay(300)
                .executeCode(()->macroRunning=false)
                .build();

        slidedown=builder.createNew()
                .executeCode(()->macroRunning=true)
                .moduleAction(deposit, DepositOld.RotationState.DEPOSIT_MID)
                .delay(400)
                .moduleAction(slides, Slides.SlideState.GROUND)
                .moduleAction(deposit, DepositOld.PusherState.IN)
                .await(()->slides.currentPosition()<120)
                .moduleAction(deposit, DepositOld.RotationState.TRANSFER)
                .moduleAction(deposit, DepositOld.WristState.TRANSFER)
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
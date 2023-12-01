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
@Autonomous(name= "Close Blue")
public class CloseBlue extends EnhancedOpMode {

    Robot robot;

    Intake intake;
    Deposit deposit;
    Slides slides;
    Pose2d startPos = new Pose2d(20,56,Math.toRadians(270));
    TaskScheduler scheduler;
    TaskListBuilder builder;
    int elementPos;

    List<Task> slideupbase;
    List<Task> slidedown;
    boolean macroRunning=false;
    int checkpoint;


    public void waitT(int ticks)
    {
        ElapsedTime x= new ElapsedTime();
        while(x.milliseconds()<ticks&&opModeIsActive())
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
    public void linearOpMode()
    {


        Trajectory purplePixel1 = robot.trajectoryBuilder(startPos)
                .splineToConstantHeading(new Vector2d(31.5, 31), Math.toRadians(-90),
                        robot.getVelocityConstraint(30, 1, 15.06),
                        robot.getAccelerationConstraint(40))
                .build();

        Trajectory purplePixel2 = robot.trajectoryBuilder(startPos)
                .lineToConstantHeading(new Vector2d(16,26))
                .build();

        Trajectory purplePixel3 = robot.trajectoryBuilder(startPos)
                .forward(8)
                .splineTo(new Vector2d(14, 32), Math.toRadians(-120),
                        robot.getVelocityConstraint(30, 1, 15.06),
                        robot.getAccelerationConstraint(40))
                .build();


        Trajectory yellowPixel1 = robot.trajectoryBuilder(purplePixel1.end())
                .lineToLinearHeading(new Pose2d(60,35.75,Math.toRadians(180)),
                        robot.getVelocityConstraint(47.5, 1.65, 15.06),
                        robot.getAccelerationConstraint(45))
                .build();

        Trajectory yellowPixel2 = robot.trajectoryBuilder(purplePixel2.end())
                .lineToLinearHeading(new Pose2d(59.5,30.5,Math.toRadians(180)),
                        robot.getVelocityConstraint(47.5, 1.65, 15.06),
                        robot.getAccelerationConstraint(45))
                .build();

        Trajectory yellowPixel3 = robot.trajectoryBuilder(purplePixel3.end())
                .lineToLinearHeading(new Pose2d(60.5  ,21.5,Math.toRadians(180)),
                        robot.getVelocityConstraint(47.5, 1.65, 15.06),
                        robot.getAccelerationConstraint(45))
                .build();

        Trajectory park1 = robot.trajectoryBuilder(yellowPixel2.end())
                .strafeRight(24)
                .build();

        Trajectory park2 = robot.trajectoryBuilder(yellowPixel2.end())
                .strafeRight(24)
                .build();

        Trajectory park3 = robot.trajectoryBuilder(yellowPixel2.end())
                .strafeRight(28)
                .build();

        waitForStart();

        robot.setPoseEstimate(startPos);

        deposit.setState(Deposit.WristState.TRANSFER);
        deposit.setState(Deposit.PusherState.EXTENDED);
        intake.setState(Intake.PositionState.PURP);


        waitT(1000);

        checkpoint++;

        if(elementPos==1) {
            robot.followTrajectoryAsync(purplePixel1);
        }else if (elementPos==2){
            robot.followTrajectoryAsync(purplePixel2);
        } else{
            robot.followTrajectoryAsync(purplePixel3);
        }

        checkpoint++;

        waitOnDT();

        checkpoint++;

        waitT(200);

        intake.setState(Intake.PowerState.OFF);
        intake.setState(Intake.PositionState.HIGH);


        scheduler.scheduleTaskList(slideupbase);

        if(elementPos==1) {
            robot.followTrajectoryAsync(yellowPixel1);
        } else if (elementPos==2){
            robot.followTrajectoryAsync(yellowPixel2);
        } else{
            robot.followTrajectoryAsync(yellowPixel3);
        }

        waitOnMacro();
        waitOnDT();

        deposit.setState(Deposit.PusherState.TWO);

        waitT(1000);

        scheduler.scheduleTaskList(slidedown);

        waitOnMacro();

        if(elementPos==1) {
            robot.followTrajectoryAsync(park1);
        } else if (elementPos==2){
            robot.followTrajectoryAsync(park2);
        } else{
            robot.followTrajectoryAsync(park3);
        }

        waitOnDT();
    }

    public void onStart()
    {
        robot.closeCameras();
    }

    @Override
    public void initialize()
    {
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
                .await(()->slides.getStatus()==Module.Status.IDLE)
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
        Context.tel.addData("Team red?", Context.isTeamRed);
        Context.tel.addData("element Pos", elementPos);
        Context.tel.addData("centerY", robot.teamElementDetector.centerY);
        Context.tel.addData("largest area", robot.teamElementDetector.getLargestArea());
        //Context.tel.update();
        robot.initLoop();
    }
    @Override
    public void primaryLoop()
    {
        Context.tel.addData("checkpoint", checkpoint);
        Context.tel.addData("Debug", Context.debug);
        robot.primaryLoop();
    }
}
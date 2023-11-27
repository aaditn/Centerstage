package org.firstinspires.ftc.teamcode.opmodes.auton;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
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
@Autonomous(name= "Far Red")
public class FarRed extends EnhancedOpMode {

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
    public void linearOpMode()
    {


        Trajectory purplePixel1 = robot.trajectoryBuilder(startPos)
                .splineTo(new Vector2d(-35, -40), Math.toRadians(90))
                .splineTo(new Vector2d(-38, -34), Math.toRadians(180))
                .build();
        Trajectory firstCycle1 = robot.trajectoryBuilder(purplePixel1.end())
                .lineToConstantHeading(new Vector2d(-41, -20))
                .splineToConstantHeading(new Vector2d(-58, -12), Math.toRadians(180))
                .build();
        Trajectory yellowPixel1 = robot.trajectoryBuilder(firstCycle1.end())
                .lineToConstantHeading(new Vector2d(8, -12))
                .splineToConstantHeading(new Vector2d(50, -28), Math.toRadians(0))
                .build();
        Trajectory pickAllianceYellow1 = robot.trajectoryBuilder(yellowPixel1.end())
                .splineToConstantHeading(new Vector2d(30, -61), Math.toRadians(180))
                .build();

        Trajectory placeAllianceYellow1 = robot.trajectoryBuilder(pickAllianceYellow1.end())
                .lineTo(new Vector2d(31, -61))
                .splineToConstantHeading(new Vector2d(50, -28), Math.toRadians(0))
                .build();

        Trajectory secondCycle1 = robot.trajectoryBuilder(placeAllianceYellow1.end())
                .splineToConstantHeading(new Vector2d(8, -12), Math.toRadians(180))
                .lineToConstantHeading(new Vector2d(-58, -12))
                .build();
        Trajectory park = robot.trajectoryBuilder(secondCycle1.end())
                .lineToConstantHeading(new Vector2d(8, -12))
                .splineToConstantHeading(new Vector2d(58, -12), Math.toRadians(0))
                .build();
        waitForStart();



        deposit.setState(Deposit.WristState.TRANSFER);
        deposit.setState(Deposit.PusherState.EXTENDED);
        intake.setState(Intake.PositionState.PURP);

        waitT(1000);

        if(elementPos==1) {
            robot.followTrajectoryAsync(purplePixel1);
        }/*else if (elementPos==2){
            robot.followTrajectoryAsync(purplePixel2);
        } else{
            robot.followTrajectoryAsync(purplePixel3);
        }*/

        waitOnDT();

        waitT(200);

        intake.setState(Intake.PowerState.OFF);
        intake.setState(Intake.PositionState.FIVE);
        waitT(1000);

        robot.followTrajectory(firstCycle1);
        intake.setState(Intake.PowerState.INTAKE);
        waitT(1000);

        intake.setState(Intake.PowerState.OFF);
        intake.setState(Intake.PositionState.HIGH);
        if(elementPos==1) {
            robot.followTrajectoryAsync(yellowPixel1);
        }/* else if (elementPos==2){
            robot.followTrajectoryAsync(yellowPixel2);
        } else{
            robot.followTrajectoryAsync(yellowPixel3);
        }*/
        scheduler.scheduleTaskList(slideupbase);

        waitOnMacro();
        waitOnDT();

        deposit.setState(Deposit.PusherState.TWO);

        waitT(1000);

        scheduler.scheduleTaskList(slidedown);

        waitOnMacro();

        intake.setState(Intake.PositionState.TELE);
        intake.setState(Intake.PowerState.INTAKE);
        robot.followTrajectory(pickAllianceYellow1);
        waitT(1000);

        intake.setState(Intake.PositionState.TELE);
        intake.setState(Intake.PowerState.OFF);
        robot.followTrajectory(placeAllianceYellow1);

        scheduler.scheduleTaskList(slideupbase);

        waitOnMacro();
        waitOnDT();

        deposit.setState(Deposit.PusherState.TWO);

        waitT(1000);

        scheduler.scheduleTaskList(slidedown);

        waitOnMacro();

        intake.setState(Intake.PositionState.FOUR);
        waitT(1000);

        robot.followTrajectory(firstCycle1);
        intake.setState(Intake.PowerState.INTAKE);
        waitT(1000);
        intake.setState(Intake.PositionState.THREE);
        waitT(1000);

        intake.setState(Intake.PowerState.OFF);
        intake.setState(Intake.PositionState.HIGH);
        robot.followTrajectory(secondCycle1);

        scheduler.scheduleTaskList(slideupbase);

        waitOnMacro();
        waitOnDT();

        deposit.setState(Deposit.PusherState.TWO);

        waitT(1000);

        scheduler.scheduleTaskList(slidedown);

        waitOnMacro();

        robot.followTrajectoryAsync(park);

        waitOnDT();
    }

    @Override
    public void initialize()
    {
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
        Context.tel.addData("element Pos", elementPos);
        Context.tel.addData("centerY", robot.teamElementDetector);
        Context.tel.addData("largest area", robot.teamElementDetector.getLargestArea());
        //Context.tel.update();
        robot.initLoop();
    }
    @Override
    public void primaryLoop()
    {
        robot.primaryLoop();
    }
}
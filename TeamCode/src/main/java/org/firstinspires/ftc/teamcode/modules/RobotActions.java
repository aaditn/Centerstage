package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskListBuilder;
import org.firstinspires.ftc.teamcode.util.Context;

import java.util.List;
@Config
public class RobotActions
{
    static RobotActions robotActions;
    Robot robot;
    Slides slides;
    Deposit deposit;
    Intake intake;
    TaskListBuilder builder;
    Intake.SweeperState[] sweepStates = {Intake.SweeperState.INIT,Intake.SweeperState.ONE_SWEEP, Intake.SweeperState.TWO_SWEEP,Intake.SweeperState.THREE_SWEEP,Intake.SweeperState.FOUR_SWEEP};

    public static double slidesDelay=2000;
    public static RobotActions getInstance()
    {
        if (robotActions == null) {
            robotActions = new RobotActions(Robot.getInstance());
        }
        return robotActions;
    }

    public static void deleteActionsInstance()
    {
        robotActions=null;
    }

    public RobotActions(Robot robot)
    {
        this.robot=robot;
        builder=new TaskListBuilder(Context.opmode, robot);

        deposit=robot.deposit;
        slides=robot.slides;
        intake=robot.intake;
    }
    public List<Task> autoRaiseSlides(Slides.SlideState row)
    {
        if(slides.getState()==Slides.SlideState.GROUND)
        {
            return builder.createNew()
                    .executeCode(()->slides.macroRunning=true)
                    .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                    .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                    .delay(50)
                    .moduleAction(slides, row)
                    .delay(200)
                    .moduleAction(deposit, Deposit.WristState.HOVER)
                    .await(()->slides.getStatus()==Module.Status.IDLE)
                    .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                    .executeCode(()->slides.macroRunning=false)
                    .build();
        }

        return builder.createNew()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(slides, row)
                .awaitPreviousModuleActionCompletion()
                .executeCode(()->slides.macroRunning=false)
                .build();
    }

    public List<Task> runSweepersAuto(double xPos,boolean x)
    {
        return builder.createNew()
                .await(()->robot.getPoseEstimate().getX()<xPos)
                .moduleAction(intake, Intake.SweeperState.FOUR_SWEEP)
                .delay(750)
                .moduleAction(intake, Intake.SweeperState.FIVE_SWEEP)
                .build();
    }
    public List<Task> runSweepersAuto(double xPos,boolean x, int sweeperIndex)
    {
        return builder.createNew()
                .await(()->robot.getPoseEstimate().getX()<xPos)
                .moduleAction(intake, sweepStates[sweeperIndex])
                .delay(750)
                .moduleAction(intake, sweepStates[sweeperIndex+1])
                .build();
    }
    public List<Task> runSweepersAuto(double xPos,boolean x,boolean y)
    {
        return builder.createNew()
                .await(()->robot.getPoseEstimate().getX()<xPos)
                .moduleAction(intake, Intake.SweeperState.SEVEN_SWEEP)
                .build();
    }

    public List<Task> runSweepersAuto(double xPos)
    {
        return builder.createNew()
                .await(()->robot.getPoseEstimate().getX()<xPos)
                .moduleAction(intake,Intake.SweeperState.TWO_SWEEP)
                .delay(750)
                .moduleAction(intake,Intake.SweeperState.THREE_SWEEP)
                .build();
    }

    public List<Task> slidesOnly(Slides.SlideState row)
    {
        if(slides.getState()==Slides.SlideState.GROUND)
        {
            return builder.createNew()
                    .executeCode(()->slides.macroRunning=true)/*
                    .moduleAction(deposit, Deposit.ClawState.PRIMED)
                    .moduleAction(slides, Slides.SlideState.HALF)
                    .await(()->slides.getStatus()==Module.Status.IDLE)
                    .delay(500)
                    .moduleAction(deposit, Deposit.WristState.TRANSFER)
                    .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                    .delay(500)
                    .moduleAction(slides, Slides.SlideState.GROUND)
                    .await(()->slides.getStatus()==Module.Status.IDLE)
                    .delay(1000)*/
                    .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                    .delay(1000)
                    .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                    .delay(150)
                    .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                    .delay(50)
                    .moduleAction(slides, row)
                    //.delay(50)
                    .delay(300)
                    .moduleAction(deposit, Deposit.WristState.HOVER)
                    .delay(200)
                    //.await(()->slides.getStatus()==Module.Status.IDLE)
                    .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                    .executeCode(()->slides.macroRunning=false)
                    .build();

            /*return builder.createNew()
                    .executeCode(()->slides.macroRunning=true)
                    .moduleAction(deposit, Deposit.ClawState.PRIMED)
                    .moduleAction(slides, Slides.SlideState.HALF)
                    .await(()->slides.getStatus()==Module.Status.IDLE)
                    .delay(500)
                    .moduleAction(deposit, Deposit.WristState.TRANSFER)
                    .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                    .delay(500)
                    .moduleAction(slides, Slides.SlideState.GROUND)
                    .await(()->slides.getStatus()==Module.Status.IDLE)
                    .delay(1000)
                    .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                    .delay(1000)
                    .moduleAction(slides, row)
                    .moduleAction(deposit, Deposit.WristState.HOVER)
                    .delay(200)
                    .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                    //.delay(50)
                    .delay(300)
                    .delay(200)
                    //.await(()->slides.getStatus()==Module.Status.IDLE)
                    .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                    .executeCode(()->slides.macroRunning=false)
                    .build();*/
        }

        return builder.createNew()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(slides, row)
                .awaitPreviousModuleActionCompletion()
                .executeCode(()->slides.macroRunning=false)
                .build();
    }
    public List<Task> updatedSlidesMacro(Slides.SlideState row)
    {
        if(slides.getState()==Slides.SlideState.GROUND)
        {
            return builder.createNew()
                    .executeCode(()->slides.macroRunning=true)
                    .moduleAction(slides, Slides.SlideState.HALF)
                    .await(()->slides.getStatus()==Module.Status.IDLE)
                    .delay(400)
                    .moduleAction(deposit, Deposit.ClawState.PRIMED)
                    .moduleAction(deposit, Deposit.WristState.TRANSFER)
                    .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                    .delay(1000)
                    .moduleAction(slides, Slides.SlideState.GROUND)
                    .await(()->slides.getStatus()==Module.Status.IDLE)
                    .delay(100)
                    .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                    .delay(1000)
                    .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                    .delay(150)
                    .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                    .delay(50)
                    .moduleAction(slides, row)
                    //.delay(50)
                    .delay(300)
                    .moduleAction(deposit, Deposit.WristState.HOVER)
                    .delay(200)
                    //.await(()->slides.getStatus()==Module.Status.IDLE)
                    .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                    .executeCode(()->slides.macroRunning=false)
                    .build();
        }

        return builder.createNew()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(slides, row)
                .awaitPreviousModuleActionCompletion()
                .executeCode(()->slides.macroRunning=false)
                .build();
    }
    public List<Task> raiseSlides(Slides.SlideState row)
    {
        if(!Context.isTele&&slides.getState()==Slides.SlideState.GROUND){
            return builder.createNew()
                    .moduleAction(deposit, Deposit.WristState.TRANSFER)
                    .delay(50)
                    .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                    .delay(500)
                    .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                    .delay(200)
                    .await(()->robot.getPoseEstimate().getX()>-10)
                    .executeCode(()->slides.macroRunning=true)
                    .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                    .delay(150)
                    .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                    .delay(50)
                    .moduleAction(slides, row)
                    //.delay(50)
                    .delay(300)
                    .moduleAction(deposit, Deposit.WristState.HOVER)
                    .delay(200)
                    //.await(()->slides.getStatus()==Module.Status.IDLE)
                    .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                    .executeCode(()->slides.macroRunning=false)
                    .build();
        }
        else if(slides.getState()==Slides.SlideState.GROUND)
        {
            return builder.createNew()
                    .moduleAction(deposit, Deposit.WristState.TRANSFER)
                    .delay(50)
                    .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                    .delay(500)
                    .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                    .delay(2000)
                    .executeCode(()->slides.macroRunning=true)
                    .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                    .delay(150)
                    .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                    .delay(50)
                    .moduleAction(slides, row)
                    //.delay(50)
                    .delay(300)
                    .moduleAction(deposit, Deposit.WristState.HOVER)
                    .delay(200)
                    //.await(()->slides.getStatus()==Module.Status.IDLE)
                    .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                    .executeCode(()->slides.macroRunning=false)
                    .build();
        }

        return builder.createNew()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(slides, row)
                .awaitPreviousModuleActionCompletion()
                .executeCode(()->slides.macroRunning=false)
                .build();
    }

    public List<Task> lowerSlides()
    {

        return builder.createNew()
                //.executeCode(()->claw.setPosition(clawOpen))
                //.delay(500)
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.WristState.HOVER)
                .moduleAction(deposit, Deposit.RotateState.ZERO)
                .delay(300)
                .moduleAction(slides, Slides.SlideState.GROUND)
                .delay(100)
                .moduleAction(deposit, Deposit.FlipState.PRIMED)
                .await(()->slides.currentPosition()<100)
                .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                .moduleAction(deposit, Deposit.ClawState.PRIMED)
                .await(()->slides.getStatus()==Module.Status.IDLE)
                .executeCode(()->slides.macroRunning=false)
                .build();
    }

    public List<Task> scorePixels()
    {
        /*
        return builder.createNew()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .delay(300)
                .addTaskList(lowerSlides())
                .build();*/
        return builder.createNew()
                //.executeCode(()->claw.setPosition(clawOpen))
                //.delay(500)
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .delay(300)
                .moduleAction(deposit, Deposit.RotateState.ONE_EIGHTY)
                .delay(200)
                .moduleAction(deposit, Deposit.WristState.HOVER)
                .delay(200)
                .moduleAction(slides, Slides.SlideState.GROUND)
                .delay(100)
                .moduleAction(deposit, Deposit.FlipState.PRIMED)
                .await(()->slides.currentPosition()<150)
                .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .await(()->slides.getStatus()==Module.Status.IDLE)
                .executeCode(()->slides.macroRunning=false)
                .build();
    }

    public List<Task> scorePixelDelay()
    {
        /*
        return builder.createNew()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .delay(300)
                .addTaskList(lowerSlides())
                .build();*/
        return builder.createNew()
                //.executeCode(()->claw.setPosition(clawOpen))
                //.delay(500)
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .delay(200)
                .moduleAction(deposit, Deposit.RotateState.ONE_EIGHTY)
                .delay(500)
                .moduleAction(deposit, Deposit.WristState.HOVER)
                .delay(300)
                .moduleAction(slides, Slides.SlideState.GROUND)
                .delay(100)
                .moduleAction(deposit, Deposit.FlipState.PRIMED)
                .await(()->slides.currentPosition()<100)
                .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .await(()->slides.getStatus()==Module.Status.IDLE)
                .executeCode(()->slides.macroRunning=false)
                .build();
    }
    public List<Task> raiseIntake()
    {
        return builder.createNew()
                .moduleAction(intake, Intake.PositionState.RAISED)
                .moduleAction(intake, Intake.SweeperState.ZERO)
                .moduleAction(intake, Intake.PowerState.OFF)
                .moduleAction(intake, Intake.ConveyorState.OFF)
                .build();
    }

    public List<Task> lowerIntake()
    {
        return builder.createNew()
                .moduleAction(intake, Intake.PositionState.DOWN)
                .moduleAction(intake, Intake.PowerState.INTAKE)
                .moduleAction(intake, Intake.ConveyorState.INTAKE)
                .build();
    }

    public List<Task> grabAndHold()
    {
        return builder.createNew()
                .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                .delay(300)
                .moduleAction(deposit, Deposit.WristState.HOVER)
                .build();
    }

    public List<Task> autoRaiseSlides(double xThreshold, Slides.SlideState level, Deposit.RotateState rotation)
    {
        return builder.createNew()
                .await(()->robot.getPoseEstimate().getX()>xThreshold)
                .addTaskList(raiseSlides(level))
                .moduleAction(deposit, rotation)
                .build();
    }

}

package org.firstinspires.ftc.teamcode.modules;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskListBuilder;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;

import java.util.List;

public class RobotActions
{
    static RobotActions robotActions;
    Robot robot;
    Slides slides;
    Deposit deposit;
    Intake intake;
    TaskListBuilder builder;
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

    public List<Task> raiseSlides(Slides.SlideState row)
    {
        if(slides.getState()==Slides.SlideState.GROUND)
        {
            return builder.createNew()
                    .moduleAction(deposit, Deposit.WristState.TRANSFER)
                    .delay(50)
                    .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                    .delay(500)
                    .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                    .delay(500)
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
                .moduleAction(deposit, Deposit.RotateState.ZERO)
                .delay(500)
                .moduleAction(deposit, Deposit.WristState.HOVER)
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

    public List<Task> raiseIntake()
    {
        return builder.createNew()
                .moduleAction(intake, Intake.PositionState.RAISED)
                .moduleAction(intake, Intake.SweeperState.ZERO)
                .moduleAction(intake, Intake.PowerState.OFF)
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


}

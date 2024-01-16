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
                    //.moduleAction(deposit, Deposit.ClawState.CLOSED2)
                    //.delay(500)
                    .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                    .delay(50)
                    .moduleAction(slides, row)
                    .delay(200)
                    .moduleAction(deposit, Deposit.WristState.HOVER)
                    .await(()->slides.getStatus()== Module.Status.IDLE)
                    .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                    .build();
        }

        return builder.createNew()
                .moduleAction(slides, row)
                .awaitPreviousModuleActionCompletion()
                .build();
    }

    public List<Task> lowerSlides()
    {

        return builder.createNew()
                //.executeCode(()->claw.setPosition(clawOpen))
                //.delay(500)
                .moduleAction(deposit, Deposit.WristState.HOVER)
                .delay(300)
                .moduleAction(slides, Slides.SlideState.GROUND)
                .delay(100)
                .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                .await(()->slides.currentPosition()<100)
                .moduleAction(deposit, Deposit.WristState.TRANSFER)
                .await(()->slides.getStatus()==Module.Status.IDLE)
                .build();
    }

    public List<Task> raiseIntake()
    {
        return builder.createNew()
                .moduleAction(intake, Intake.PositionState.RAISED)
                .moduleAction(intake, Intake.SweeperState.ZERO)
                .moduleAction(intake, Intake.ConveyorState.OFF)
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


}

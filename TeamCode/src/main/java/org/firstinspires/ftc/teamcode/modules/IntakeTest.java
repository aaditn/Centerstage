package org.firstinspires.ftc.teamcode.modules;

import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskListBuilder;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

import java.util.List;

public class IntakeTest extends EnhancedOpMode
{
    Intake intake;
    TaskListBuilder builder;
    List<Task> testTaskList;
    TaskScheduler scheduler;
    Robot r;

    @Override
    public void linearOpMode()
    {
        GamepadEx primary=new GamepadEx(gamepad1);
        ButtonReader B=new ButtonReader(primary, GamepadKeys.Button.B);
        ButtonReader A=new ButtonReader(primary, GamepadKeys.Button.A);
        scheduler=new TaskScheduler();

        testTaskList=builder.createNew()
                .await(()->opModeIsActive())
                .moduleAction(intake, Intake.State.MOTOR_ON)
                .await(()->!intake.isBusy())
                .delay(1000)
                .moduleAction(intake, Intake.State.MOTOR_OFF)
                .awaitButtonPress(B)
                .moduleAction(intake, Intake.State.ANGLE_HIGH)
                .moduleAction(intake, Intake.State.MOTOR_ON)
                .awaitButtonPress(A)
                .moduleAction(intake, Intake.State.ANGLE_HIGH)
                .moduleAction(intake, Intake.State.ANGLE_HIGH)
                .delay(1000)
                .moduleAction(intake, Intake.State.ANGLE_LOW)
                .moduleAction(intake, Intake.State.ANGLE_LOW)
                .delay(100)
                .executeCode(
                        ()->scheduler.scheduleTaskList(testTaskList)
                )
                .build();

        scheduler.scheduleTaskList(testTaskList);
    }

    @Override
    public void initialize()
    {
        this.setLoopTimes(10);
        builder=new TaskListBuilder(this);
        r=new Robot(this);
    }

    @Override
    public void initLoop()
    {
        r.initLoop();
    }

    public void onStart()
    {
        //do something idk(maybe take signal sleeve snapshot or wtv)
    }

    @Override
    public void primaryLoop()
    {
        r.primaryLoop();
    }

    @Override
    public void onEnd()
    {
        r.onEnd();
    }
}

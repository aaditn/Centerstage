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

    Deposit deposit;
    TaskListBuilder builder;
    List<Task> testTaskList;
    TaskScheduler scheduler;

    @Override
    public void linearOpMode()
    {
        GamepadEx primary=new GamepadEx(gamepad1);
        ButtonReader B=new ButtonReader(primary, GamepadKeys.Button.B);
        ButtonReader A=new ButtonReader(primary, GamepadKeys.Button.A);
        scheduler=new TaskScheduler();

        waitForStart();
        while(opModeIsActive()){}
        /*testTaskList=builder.createNew()
                .await(()->opModeIsActive())
                .moduleAction(intake, Intake.powerState.MOTOR_ON)
                .await(()->!intake.isBusy())
                .delay(1000)
                .moduleAction(intake, Intake.powerState.MOTOR_OFF)
                .awaitButtonPress(B)
                .moduleAction(intake, Intake.powerState.ANGLE_HIGH)
                .moduleAction(intake, Intake.powerState.MOTOR_ON)
                .awaitButtonPress(A)
                .moduleAction(intake, Intake.powerState.ANGLE_HIGH)
                .moduleAction(intake, Intake.powerState.ANGLE_HIGH)
                .delay(1000)
                .moduleAction(intake, Intake.powerState.ANGLE_LOW)
                .moduleAction(intake, Intake.powerState.ANGLE_LOW)
                .delay(100)
                .executeCode(
                        ()->scheduler.scheduleTaskList(testTaskList)
                )
                .build();

        scheduler.scheduleTaskList(testTaskList);*/
    }

    @Override
    public void initialize()
    {
        this.setLoopTimes(10);
        builder=new TaskListBuilder(this);
        deposit=new Deposit(hardwareMap);
        intake=new Intake(hardwareMap);


    }

    @Override
    public void initLoop()
    {
        intake.updateLoop();
        deposit.updateLoop();
        intake.writeLoop();
        deposit.writeLoop();
    }

    public void onStart()
    {
        //do something idk(maybe take signal sleeve snapshot or wtv)
    }

    @Override
    public void primaryLoop()
    {
        intake.updateLoop();
        deposit.updateLoop();
        intake.writeLoop();
        deposit.writeLoop();

        if(gamepad1.a)
        {
            intake.setState(Intake.positionState.TELE);
        }
        else if(gamepad1.b)
        {
            intake.setState(Intake.positionState.HIGH);
        }
        else if(gamepad1.x)
        {
            intake.setState(Intake.powerState.INTAKE);
        }
        else if(gamepad1.y)
        {
            intake.setState(Intake.powerState.OFF);
        }

        if(gamepad2.a)
        {
            deposit.setState(Deposit.RotationState.DEPOSIT);
        }
        if(gamepad2.b)
        {
            deposit.setState(Deposit.RotationState.TRANSFER);
        }
        if(gamepad2.y)
        {
            deposit.setState(Deposit.PusherState.TWO);
        }
        if(gamepad2.x)
        {
            deposit.setState(Deposit.PusherState.IN);
        }
    }

    @Override
    public void onEnd()
    {
    }
}

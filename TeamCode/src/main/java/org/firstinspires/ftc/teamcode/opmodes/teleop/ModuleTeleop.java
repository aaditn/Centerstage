package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.KeyReader;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.DroneLauncher;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskList;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskListBuilder;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

import java.util.List;

@Config
@TeleOp

public class ModuleTeleop extends EnhancedOpMode
{
    Robot robot;
    Intake intake;
    Deposit deposit;
    Slides slides;
    DroneLauncher droneLauncher;

    TaskListBuilder builder;
    List<Task> testTaskList;
    TaskScheduler scheduler;

    boolean slidesMoving=false;

    KeyReader[] keyReaders;
    GamepadEx g1, g2;
    ButtonReader slideUp, slideDown;
    List<Task> slideup, slidedown;
    double ifconditionmet;
    double ifcondition2;
    double ifcondition3;


    @Override
    public void linearOpMode()
    {
        waitForStart();

        while(opModeIsActive())
        {
            for (KeyReader reader : keyReaders)
            {
                reader.readValue();
            }

            if(gamepad1.a)
            {
                deposit.setState(Deposit.RotationState.TRANSFER);
            }
            else if(gamepad1.b)
            {
                deposit.setState(Deposit.RotationState.DEPOSIT_HIGH);
            }
            else if(gamepad1.x)
            {
                intake.setState(Intake.PositionState.HIGH);
            }
            else if(gamepad1.y)
            {
                intake.setState(Intake.PositionState.TELE);
            }
            else if(gamepad1.right_bumper)
            {
                deposit.setState(Deposit.PusherState.IN);
            }
            else if(gamepad1.left_bumper)
            {
                deposit.setState(Deposit.PusherState.TWO);
            }

            if(slideUp.wasJustPressed()&&slides.getState()==Slides.SlideState.GROUND&&!slidesMoving)
            {
                ifconditionmet++;
                scheduler.scheduleTaskList(slideup);
            }
            else if(slideDown.wasJustPressed()&&slides.getState()==Slides.SlideState.RAISED&&!slidesMoving)
            {
                ifconditionmet++;
                scheduler.scheduleTaskList(slidedown);
            }

            if(slides.getState()==Slides.SlideState.RAISED&&!slidesMoving)
            {
                slides.setOperationState(Module.OperationState.MANUAL);
                
            }

            intake.manualChange(-gamepad1.left_stick_y);
        }
    }

    @Override
    public void initialize()
    {
        this.setLoopTimes(0);

        robot=new Robot(this);
        builder=new TaskListBuilder(this);
        scheduler=new TaskScheduler();

        deposit=robot.deposit;
        intake=robot.intake;
        slides=robot.slides;
        droneLauncher=robot.droneLauncher;

        intake.init();
        deposit.init();
        intake.setOperationState(Module.OperationState.MANUAL);

        slidesMoving=false;

        g1=new GamepadEx(gamepad1);
        g2=new GamepadEx(gamepad2);
        keyReaders= new KeyReader[]{
                slideUp = new ToggleButtonReader(g2, GamepadKeys.Button.DPAD_UP),
                slideDown = new ToggleButtonReader(g2, GamepadKeys.Button.DPAD_DOWN)
        };

        slideup=builder.createNew()
                .executeCode(()->slidesMoving=true)
                .moduleAction(slides, Slides.SlideState.RAISED)
                .await(()->slides.currentPosition()>100)
                .moduleAction(deposit, Deposit.RotationState.DEPOSIT_MID)
                .await(()->slides.getStatus()==Module.Status.IDLE)
                .executeCode(()->slidesMoving=false)
                .build();

        slidedown=builder.createNew()
                .executeCode(()->slidesMoving=true)
                .moduleAction(slides, Slides.SlideState.GROUND)
                .moduleAction(deposit, Deposit.PusherState.IN)
                .await(()->slides.currentPosition()<100)
                .moduleAction(deposit, Deposit.RotationState.TRANSFER)
                .await(()->slides.getStatus()==Module.Status.IDLE)
                .executeCode(()->slidesMoving=false)
                .build();


    }

    @Override
    public void initLoop()
    {
        robot.initLoop();
    }

    @Override
    public void primaryLoop()
    {
        Context.tel.addData("SLide Moving", slidesMoving);
        Context.tel.addData("If condition met", ifconditionmet);

        robot.primaryLoop();
    }
}

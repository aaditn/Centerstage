package org.firstinspires.ftc.teamcode.non_module_testing;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.KeyReader;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskListBuilder;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

import java.util.List;

@TeleOp
@Config
public class NewDeposit extends EnhancedOpMode
{
    public static double clawOpen=0.88, clawClosed1=0.72, clawClosed2=0.72;
    public static double rotateFlat=0.36, rotate90=0.03; //0.69, 1
    public static double wristIntake=0.21, wristDeposit=0.54, wristFloaty=0.44;
    public static double rotator1Intake=0.99, rotator1Deposit=0.22;
    public static double rotator2Intake=0.01, rotator2Deposit=0.78;
    Servo deposit1, deposit2, wrist, rotatewrist, claw;

    //wrist 0:

    TaskListBuilder builder;
    TaskScheduler scheduler;

    List<Task> slideup, slidedown;
    Slides slides;
    KeyReader[] keyReaders;
    ButtonReader slideupb, slidedownb;
    MultipleTelemetry tel;
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

            if(gamepad1.x)
            {
                claw.setPosition(clawOpen);
            }
            else if(gamepad1.y)
            {
                claw.setPosition(clawClosed2);
            }

            if(gamepad1.a)
            {
                rotatewrist.setPosition(rotateFlat);
            }
            else if(gamepad1.b)
            {
                rotatewrist.setPosition(rotate90);
            }

            if(gamepad1.dpad_up)
            {
                deposit1.setPosition(rotator1Deposit);
                deposit2.setPosition(rotator2Deposit);
                wrist.setPosition(wristDeposit);
            }
            else if(gamepad1.dpad_down)
            {
                deposit1.setPosition(rotator1Intake);
                deposit2.setPosition(rotator2Intake);
                wrist.setPosition(wristIntake);
            }
            if(slideupb.wasJustPressed())
            {
                slides.setOperationState(Module.OperationState.PRESET);
                scheduler.scheduleTaskList(slideup);
            }
            else if(slidedownb.wasJustPressed())
            {
                slides.setOperationState(Module.OperationState.PRESET);
                scheduler.scheduleTaskList(slidedown);
            }

        }
    }

    @Override
    public void initialize()
    {
        deposit1=hardwareMap.get(Servo.class, "deposit1");
        deposit2=hardwareMap.get(Servo.class, "deposit2");
        wrist=hardwareMap.get(Servo.class, "wrist");
        rotatewrist=hardwareMap.get(Servo.class, "rotatewrist");
        claw=hardwareMap.get(Servo.class, "claw");

        tel = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Context.resetValues();
        Context.tel=tel;
        Context.updateValues();

        slides=new Slides(hardwareMap);

        builder=new TaskListBuilder(this);
        scheduler=new TaskScheduler();

        GamepadEx g1=new GamepadEx(gamepad1);

        keyReaders= new KeyReader[]{
                slideupb= new ToggleButtonReader(g1, GamepadKeys.Button.DPAD_LEFT),
                slidedownb= new ToggleButtonReader(g1, GamepadKeys.Button.DPAD_RIGHT),
        };

        slideup=builder.createNew()
                .executeCode(()->claw.setPosition(clawClosed2))
                .delay(500)
                .executeCode(()->deposit1.setPosition(rotator1Deposit))
                .executeCode(()->deposit2.setPosition(rotator2Deposit))
                .moduleAction(slides, Slides.SlideState.ROW2)
                .delay(200)
                .executeCode(()->wrist.setPosition(wristFloaty))
                .await(()->slides.getStatus()== Module.Status.IDLE)
                .executeCode(()->wrist.setPosition(wristDeposit))
                .build();

        slidedown=builder.createNew()
                .executeCode(()->claw.setPosition(clawOpen))
                .delay(500)
                //.executeCode(()->slidesMoving=true)
                .executeCode(()->wrist.setPosition(wristFloaty))
                .delay(300)
                .moduleAction(slides, Slides.SlideState.GROUND)
                .await(()->slides.currentPosition()<100)
                .executeCode(()->deposit1.setPosition(rotator1Intake))
                .executeCode(()->deposit2.setPosition(rotator2Intake))
                .await(()->slides.getStatus()==Module.Status.IDLE)
                .executeCode(()->wrist.setPosition(wristIntake))
                .delay(300)
                .build();

    }

    @Override
    public void initLoop()
    {
        Context.tel.update();
    }

    @Override
    public void primaryLoop()
    {
        slides.updateLoop();
        slides.writeLoop();
        Context.tel.update();
    }
}

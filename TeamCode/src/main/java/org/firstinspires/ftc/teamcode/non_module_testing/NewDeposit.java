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
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskListBuilder;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

import java.util.List;

@TeleOp(name="A - NewDeposit")
@Config
public class NewDeposit extends EnhancedOpMode
{
    public static double clawOpen=0.99, clawClosed1=0.72, clawClosed2=0.68;
    public static double rotateZero =0.435, rotate90=0.49;
    public static double wristTransfer =0.99, wristDeposit=0.63, wristFloaty=0.83;
    public static double rotatorTransfer=0.94, rotatorDeposit=0.22;
    public static double raised = 0, down = 0;
    Servo leftArm, rightArm, wrist, rotatewrist, claw, anglerLeft, anglerRight, sweeperLeft, sweeperRight;
    CRServo conveyorLeft, conveyorRight;
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
                rotatewrist.setPosition(rotateZero);
            }
            else if(gamepad1.b)
            {
                rotatewrist.setPosition(rotate90);
            }

            if(gamepad1.dpad_up)
            {
                leftArm.setPosition(rotatorDeposit);
                rightArm.setPosition(rotatorDeposit);
                wrist.setPosition(wristDeposit);
            }
            else if(gamepad1.dpad_down)
            {
                leftArm.setPosition(rotatorTransfer);
                rightArm.setPosition(rotatorTransfer);
                wrist.setPosition(wristTransfer);
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

            if(gamepad1.left_bumper)
            {
                anglerLeft.setPosition(0);
                anglerRight.setPosition(0);
            }

            if(gamepad1.right_bumper)
            {
                anglerLeft.setPosition(0.29);
                anglerRight.setPosition(0.29);
            }
        }
    }

    @Override
    public void initialize()
    {
        leftArm=hardwareMap.get(Servo.class, "leftArm");
        rightArm=hardwareMap.get(Servo.class, "rightArm");
        rightArm.setDirection(Servo.Direction.REVERSE);

        wrist=hardwareMap.get(Servo.class, "wrist");
        rotatewrist=hardwareMap.get(Servo.class, "rotatewrist");
        claw=hardwareMap.get(Servo.class, "claw");

        anglerLeft = hardwareMap.get(Servo.class, "anglerLeft");
        anglerRight = hardwareMap.get(Servo.class, "anglerRight");
        anglerRight.setDirection(Servo.Direction.REVERSE);

        sweeperLeft = hardwareMap.get(Servo.class, "sweeperLeft");
        sweeperRight = hardwareMap.get(Servo.class, "sweeperRight");
        sweeperRight.setDirection(Servo.Direction.REVERSE);

        conveyorLeft = hardwareMap.get(CRServo.class, "conveyorLeft");
        conveyorRight = hardwareMap.get(CRServo.class, "conveyorRight");
        conveyorRight.setDirection(DcMotorSimple.Direction.REVERSE);

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
                .executeCode(()->leftArm.setPosition(rotatorDeposit))
                .executeCode(()->rightArm.setPosition(rotatorDeposit))
                .delay(50)
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
                .delay(100)
                .executeCode(()->leftArm.setPosition(rotatorTransfer))
                .executeCode(()->rightArm.setPosition(rotatorTransfer))
                .await(()->slides.currentPosition()<100)
                .executeCode(()->wrist.setPosition(wristTransfer))
                .await(()->slides.getStatus()==Module.Status.IDLE)
                .delay(300)
                .build();

        anglerLeft.setPosition(0.29);
        anglerRight.setPosition(0.29);

        sweeperLeft.setPosition(0.035);
        sweeperRight.setPosition(0.035);

        //conveyorLeft.setPower(1);
        //conveyorRight.setPower(1);
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

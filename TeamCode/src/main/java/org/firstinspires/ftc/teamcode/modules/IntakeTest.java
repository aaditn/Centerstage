package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskListBuilder;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

import java.util.List;
@TeleOp
public class IntakeTest extends EnhancedOpMode
{
    Intake intake;

    Deposit deposit;
    //Slides slides;
    TaskListBuilder builder;
    List<Task> testTaskList;
    TaskScheduler scheduler;

    int slidesPos;
    DcMotor slideLeft, slideRight;

    DcMotorEx lb, lf, rb, rf;

    @Override
    public void linearOpMode()
    {
        Context.resetValues();
        Context.tel=new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);
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
        slideLeft=hardwareMap.get(DcMotor.class, "slide1");
        slideRight=hardwareMap.get(DcMotor.class, "slide2");
        slideRight.setDirection(DcMotorSimple.Direction.REVERSE);
        slideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideLeft.setTargetPosition(slidesPos);
        slideRight.setTargetPosition(slidesPos);
        slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lb = hardwareMap.get(DcMotorEx.class, "bl");
        lf = hardwareMap.get(DcMotorEx.class, "fl");
        rb = hardwareMap.get(DcMotorEx.class, "br");
        rf = hardwareMap.get(DcMotorEx.class, "fr");
        lb.setDirection(DcMotorSimple.Direction.REVERSE);
        lf.setDirection(DcMotorSimple.Direction.REVERSE);
        //slides=new Slides(hardwareMap);
        //slides.setManual(true);
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
        //slides.setPositionManual(slidesPos);
        //slides.updateLoop();
        intake.updateLoop();
        deposit.updateLoop();
        //slides.writeLoop();
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

        if(gamepad1.left_bumper)
        {
            deposit.setState(Deposit.RotationState.DEPOSIT);
        }
        if(gamepad1.right_bumper)
        {
            deposit.setState(Deposit.RotationState.TRANSFER);
        }
        if(gamepad2.y)
        {
            deposit.setState(Deposit.PusherState.TWO);
        }
        if(gamepad1.dpad_left)
        {
            deposit.setState(Deposit.PusherState.IN);
        }
        if(gamepad1.dpad_down)
        {
            slidesPos-=10;
        }
        if(gamepad1.dpad_up)
        {
            slidesPos+=10;
        }
        slideLeft.setTargetPosition(slidesPos);
        slideRight.setTargetPosition(slidesPos);
        slideLeft.setPower(1);
        slideLeft.setPower(1);

        double x = -gamepad1.left_stick_y/1.2;
        double y = -gamepad1.left_stick_x/1.2;
        double rx = gamepad1.right_stick_x/1.5;

        lf.setPower(y + x + rx);
        lb.setPower(y - x + rx);
        rf.setPower(y - x - rx);
        rb.setPower(y + x - rx);

        /*telemetry.addData("Slides Pos", slides.targetPosition);
        telemetry.addData("Slides Power", slides.motorPower);
        telemetry.addData("Slides state", slides.opstate);
        telemetry.update();*/
        telemetry.addData("liftPos", slidesPos);
        telemetry.addData("currentPos", slideLeft.getCurrentPosition());

        telemetry.update();
    }

    @Override
    public void onEnd()
    {

    }
}

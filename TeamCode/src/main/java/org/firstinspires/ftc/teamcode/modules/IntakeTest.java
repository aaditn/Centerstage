package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskListBuilder;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

import java.util.List;
@TeleOp
@Config
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
    Servo pusher;

    public static double pusherIn=0.04;
    //public static double pusherPushed=0.18;
    public static double pusherOne=0.26;
    public static double pusherTwo=0.31;

    public static int pusherState = 0;
    public static boolean isPusher = true;
    double[] pusherArr = {pusherIn, pusherOne, pusherTwo};
    @Override
    public void linearOpMode()
    {

        pusher=hardwareMap.get(Servo.class, "pinion");
        GamepadEx primary=new GamepadEx(gamepad1);
        //GamepadEx secondary=new GamepadEx(gamepad2);
        ButtonReader B=new ButtonReader(primary, GamepadKeys.Button.B);
        ButtonReader A=new ButtonReader(primary, GamepadKeys.Button.A);
        scheduler=new TaskScheduler();

        pusher.setPosition(pusherIn);


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
        Context.resetValues();
        Context.tel=new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);

        this.setLoopTimes(0);
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
        rf.setDirection(DcMotorSimple.Direction.REVERSE);
        lf.setDirection(DcMotorSimple.Direction.REVERSE);
        //slides=new Slides(hardwareMap);
        //slides.setManual(true);
        intake.init();
        deposit.init();
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
        if (slideLeft.getCurrentPosition() > 300) {
            deposit.setState(Deposit.RotationState.DEPOSIT);
            deposit.setState(Deposit.RotationState.DEPOSIT);
        } else {
            deposit.setState(Deposit.RotationState.TRANSFER);
            deposit.setState(Deposit.PusherState.IN);
        }

        if(gamepad2.a)
        {
            intake.setState(Intake.positionState.TELE);
        }
        else if(gamepad2.b)
        {
            intake.setState(Intake.positionState.HIGH);
        }
        else if(gamepad2.right_stick_y < -0.5)
        {
            intake.setState(Intake.powerState.INTAKE);
        }
        else if(gamepad2.right_stick_y > -0.5)
        {
            intake.setState(Intake.powerState.OFF);
        }


        if(gamepad2.left_bumper)
        {
            deposit.setState(Deposit.RotationState.DEPOSIT);
        }
        if(gamepad2.right_bumper)
        {
            deposit.setState(Deposit.RotationState.TRANSFER);
            deposit.setState(Deposit.PusherState.IN);
        }
        if(gamepad2.x && isPusher)
        {
            isPusher=false;
            if (pusherState == 2) {
                pusherState = 0;
            } else {
                pusherState++;
            }

            pusher.setPosition(pusherArr[pusherState]);
            //deposit.setState(Deposit.PusherState.TWO);
        } else if (!isPusher) {
            isPusher=!gamepad2.x;
        }

        if(gamepad2.dpad_down&&slidesPos>0)
        {
            slidesPos-=10;
        }
        if(gamepad2.dpad_up&&slidesPos<1150)
        {
            slidesPos+=10;
        }
        slideLeft.setTargetPosition(slidesPos);
        slideRight.setTargetPosition(slidesPos);
        slideLeft.setPower(1);
        slideRight.setPower(1);

        double x = -gamepad1.left_stick_y/1.2;
        double y = gamepad1.left_stick_x/1.2;
        double rx = -gamepad1.right_stick_x/1.5;

        lf.setPower(y + x - rx);
        lb.setPower(y - x + rx);
        rf.setPower(y - x - rx);
        rb.setPower(y + x + rx);

        /*telemetry.addData("Slides Pos", slides.targetPosition);
        telemetry.addData("Slides Power", slides.motorPower);
        telemetry.addData("Slides state", slides.opstate);
        telemetry.update();*/
        telemetry.addData("liftPos", slidesPos);
        telemetry.addData("currentPosLeft", slideLeft.getCurrentPosition());
        telemetry.addData("currentPosRight", slideRight.getCurrentPosition());

        telemetry.addData("tPosLeft", slideLeft.getTargetPosition());
        telemetry.addData("tPosRight", slideRight.getTargetPosition());


        telemetry.update();
    }

    @Override
    public void onEnd()
    {

    }
}

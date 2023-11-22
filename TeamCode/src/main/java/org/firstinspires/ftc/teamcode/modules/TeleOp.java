package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskListBuilder;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

import java.util.List;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "1 - DRIVER CONTROL (2 PLAYER)")
@Config
public class TeleOp extends EnhancedOpMode
{
    Intake intake;
    DroneLauncher droneLauncher;
    ElapsedTime timer = new ElapsedTime();
    Boolean threshold1 =false;
    boolean threshold2 = false;
    Deposit deposit;
    //Slides slides;
    TaskListBuilder builder;
    List<Task> testTaskList;
    TaskScheduler scheduler;

    int slidesPos;
    DcMotor slideLeft, slideRight;
    DcMotorEx lb, lf, rb, rf,hang;
    Servo pusher;
    ElapsedTime pusherTimer = new ElapsedTime();
    boolean pusherBool = false;//what is this for
    public static double pusherIn=0.04;
    public static double pusherPushed=0.18;//what is this for
    public static double pusherOne=0.24;
    public static double pusherTwo=0.33;
    public static double initwrist =0.65;
    public static double depositwrist=0.30;
    public static double intakeHeight=0;
    public static int pusherState = 0;
    public static boolean isPusher = true;
    public static boolean isIntake = false;
    double[] pusherArr = {pusherIn, pusherOne, pusherTwo};
    boolean ninjaBool = true;
    boolean isPusherDone = true;
    Servo wrist;
    double ninja = 1;
    Gamepad.RumbleEffect customRumbleEffect0;    // Use to build a custom rumble sequence.
    @Override
    public void linearOpMode()
    {
        wrist =hardwareMap.get(Servo.class,"wrist");
        pusher=hardwareMap.get(Servo.class, "pusher");
        hang=hardwareMap.get(DcMotorEx.class, "hang");
        GamepadEx primary=new GamepadEx(gamepad1);
        //GamepadEx secondary=new GamepadEx(gamepad2);
        ButtonReader B=new ButtonReader(primary, GamepadKeys.Button.B);
        ButtonReader A=new ButtonReader(primary, GamepadKeys.Button.A);
        scheduler=new TaskScheduler();

        pusher.setPosition(pusherIn);
        customRumbleEffect0 = new Gamepad.RumbleEffect.Builder()
                .addStep(1.0, 1.0, 200)
                .addStep(0.0, 0.0, 1000) //  Rumble right motor 100% for 500 mSec
                .build();

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
        droneLauncher=new DroneLauncher(hardwareMap);
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
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setDirection(DcMotorSimple.Direction.REVERSE);
        lf.setDirection(DcMotorSimple.Direction.REVERSE);
        //slides=new Slides(hardwareMap);
        //slides.setManual(true);
        intake.init();
        deposit.init();
        droneLauncher.init();
        intake.setManual(true);
    }

    @Override
    public void initLoop()
    {
        intake.updateLoop();
        deposit.updateLoop();
        droneLauncher.updateLoop();
        intake.writeLoop();
        deposit.writeLoop();
        droneLauncher.writeLoop();
    }

    public void onStart()
    {
        //do something idk(maybe take signal sleeve snapshot or wtv)
    }

    @Override
    public void primaryLoop()
    {
        telemetry.addData("Intake Pos", intakeHeight);
        telemetry.addData("Drone", droneLauncher.getState());
        //slides.setPositionManual(slidesPos);
        //slides.updateLoop();
        intake.updateLoop();
        deposit.updateLoop();
        droneLauncher.updateLoop();
        //slides.writeLoop();
        intake.writeLoop();
        deposit.writeLoop();
        droneLauncher.writeLoop();
        if (slideLeft.getCurrentPosition() < 100) {
            deposit.setState(Deposit.RotationState.TRANSFER);
            deposit.setState(Deposit.PusherState.IN);
            wrist.setPosition(initwrist);
            threshold2 =true;
            threshold1 =false;
        }
        else if (slideLeft.getCurrentPosition() > 100 ) {
            if(threshold2 == true){

                deposit.setState(Deposit.RotationState.DEPOSIT_MID);
                deposit.setState(Deposit.RotationState.DEPOSIT_MID);
                pusher.setPosition(pusherPushed);
                threshold1 =true;
                threshold2 = false;
                timer.reset();
            }

        }
        telemetry.addData("ok", threshold1);
        telemetry.addData("ok2", threshold2);
        telemetry.addData("matthewmiliseconds", timer.milliseconds());
        if(threshold1 ==true&& timer.milliseconds()>600){

            deposit.setState(Deposit.RotationState.DEPOSIT_HIGH);
            deposit.setState(Deposit.RotationState.DEPOSIT_HIGH);
            wrist.setPosition(depositwrist);

        }

        if(gamepad2.y){
            droneLauncher.setState(DroneLauncher.State.RELEASED);
        }

        if(gamepad2.a)
        {
            intake.setState(Intake.positionState.TELE);
        }
        else if(gamepad2.b)
        {
            intake.setState(Intake.positionState.HIGH);
        }
        intake.setPowerManual(-gamepad2.right_stick_y);

        if(isIntake==false&&gamepad2.right_bumper)
        {
            isIntake=true;
            intakeHeight=(intakeHeight+1)%5;
        }else if(isIntake==false&&gamepad2.left_bumper)
        {
            isIntake=true;
            intakeHeight=(intakeHeight+4)%5;
        }else if(!gamepad2.left_bumper&&!gamepad2.right_bumper){
            isIntake=false;
        }
        if(intakeHeight==0){
            intake.setState(Intake.positionState.TELE);
        }else if(intakeHeight==1){
            intake.setState(Intake.positionState.TWO);
        }else if(intakeHeight==2){
            intake.setState(Intake.positionState.THREE);
        }else if(intakeHeight==3){
            intake.setState(Intake.positionState.FOUR);
        }else if(intakeHeight==4){
            intake.setState(Intake.positionState.FIVE);
        }

        if((gamepad1.right_bumper||gamepad2.x) && isPusher)
        {
            isPusher=false;
            if (pusherState == 2) {
                pusherState = 0;
            } else {
                pusherState++;
            }
            gamepad1.runRumbleEffect(customRumbleEffect0);
            gamepad2.runRumbleEffect(customRumbleEffect0);

            pusher.setPosition(pusherArr[pusherState]);
            //deposit.setState(Deposit.PusherState.TWO);
        } else if (!isPusher) {
            isPusher=!(gamepad2.x||gamepad1.right_bumper);
        }



        if(gamepad2.left_stick_y < -0.3 && slidesPos<1150)
        {
            slidesPos+=10;
        }
        if(gamepad2.left_stick_y > 0.3 &&slidesPos>50)
        {
            slidesPos-=50;
        } else if (gamepad2.left_stick_y > 0.3 && slidesPos > 0) {
            slidesPos -= 10;
        }

        if (slidesPos < 50) {
            pusherState = 0;
            pusher.setPosition(pusherArr[pusherState]);
        }
        if(gamepad2.dpad_up){
            hang.setPower(1);
        }
        else if(gamepad2.dpad_down){
            hang.setPower(-1);
        }
        else{
            hang.setPower(0);
        }
        telemetry.addData("hangPos",hang.getCurrentPosition());

        /*if (gamepad1.x && isXClicked) {
            isXClicked = false;

            ninjaBool = !ninjaBool;
            if (ninjaBool) {
                ninja = 1;
            } else {
                ninja = 0.5;
            }

        } else if (!isXClicked) {
            isXClicked = !gamepad1.x;
        }
        */

        if (gamepad1.left_trigger > 0.3) {
            ninja = 0.5;
        } else {
            ninja = 1;
        }
        slideLeft.setTargetPosition(slidesPos);
        slideRight.setTargetPosition(slidesPos);
        slideLeft.setPower(1);
        slideRight.setPower(1);

        double x =gamepad1.left_stick_y * ninja;
        double y = -gamepad1.left_stick_x * ninja;
        double rx = -gamepad1.right_stick_x * ninja;

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
package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.KeyReader;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.DroneLauncher;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
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
    ButtonReader slideUpBase, slideUpRow1, slideUpRow2, slideDown, pusher1, pusher2,
            strafeLeft, strafeRight, intake1, intake2;
    List<Task> slideupbase, slideup1, slideup2, slidedown, slideup1raised, slideup2raised;
    int intakeposition;
    double ninja;
    Intake.PositionState[] intakepositions;

    DcMotor hang;


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

            //pusher buttons
            if(pusher1.wasJustPressed()||pusher2.wasJustPressed())
            {
                if(deposit.getState(Deposit.PusherState.class)==Deposit.PusherState.EXTENDED)
                {
                    deposit.setState(Deposit.PusherState.ONE);
                }
                else if(deposit.getState(Deposit.PusherState.class)==Deposit.PusherState.ONE)
                {
                    deposit.setState(Deposit.PusherState.TWO);
                }
            }

            //cycle through intake positions
            if(intake1.wasJustPressed())
            {
                intakeposition--;
                if(intakeposition<0)
                {
                    intakeposition=6;
                }
                intake.setState(intakepositions[intakeposition]);
            }
            else if(intake2.wasJustPressed())
            {
                intakeposition++;
                if(intakeposition>6)
                {
                    intakeposition=0;
                }
                intake.setState(intakepositions[intakeposition]);
            }

            //set intake to high or low
            if(gamepad2.b)
            {
                intakeposition=6;
                intake.setState(intakepositions[intakeposition]);
            }
            else if(gamepad2.a)
            {
                intakeposition=0;
                intake.setState(intakepositions[intakeposition]);
            }


            //slides macro
            if(slideUpBase.wasJustPressed()&&slides.getState()==Slides.SlideState.GROUND&&!slidesMoving)
            {
                slides.setOperationState(Module.OperationState.PRESET);
                slidesMoving=true;
                scheduler.scheduleTaskList(slideupbase);
            }
            else if(slideUpRow1.wasJustPressed()&&slides.getState()==Slides.SlideState.GROUND&&!slidesMoving)
            {
                slides.setOperationState(Module.OperationState.PRESET);
                slidesMoving=true;
                scheduler.scheduleTaskList(slideup1);
            }
            else if(slideUpRow2.wasJustPressed()&&slides.getState()==Slides.SlideState.GROUND&&!slidesMoving)
            {
                slides.setOperationState(Module.OperationState.PRESET);
                slidesMoving=true;
                scheduler.scheduleTaskList(slideup2);
            }
            else if(slideUpRow1.wasJustPressed()&&slides.getState()!=Slides.SlideState.GROUND&&!slidesMoving)
            {
                slides.setOperationState(Module.OperationState.PRESET);
                slidesMoving=true;
                scheduler.scheduleTaskList(slideup1raised);
            }
            else if(slideUpRow2.wasJustPressed()&&slides.getState()!=Slides.SlideState.GROUND&&!slidesMoving)
            {
                slides.setOperationState(Module.OperationState.PRESET);
                slidesMoving=true;
                scheduler.scheduleTaskList(slideup2raised);
            }
            else if(slideDown.wasJustPressed()&&slides.getState()!=Slides.SlideState.GROUND&&!slidesMoving)
            {
                slides.setOperationState(Module.OperationState.PRESET);
                slidesMoving=true;
                scheduler.scheduleTaskList(slidedown);
            }



            //slides manual
            if(slides.getState()!=Slides.SlideState.GROUND&&!slidesMoving&&Math.abs(gamepad2.left_stick_y)>0.3)
            {
                slides.setOperationState(Module.OperationState.MANUAL);
                double newTarget=slides.currentPosition() + (30*Math.signum(gamepad2.left_stick_y)*-1);
                if(newTarget<200)
                {
                    newTarget=200;
                }
                slides.manualChange(newTarget);
            }

            //intake power
            intake.manualChange(-gamepad2.right_stick_y);


            //ninja mode
            if (gamepad1.left_trigger > 0.3)
            {
                ninja = 0.5;
            } else
            {
                ninja = 1;
            }

            //normal dt movement
            if(!robot.isBusy())
            {
                double x = gamepad1.left_stick_y * ninja;
                double y = -gamepad1.left_stick_x * ninja;
                double rx = -gamepad1.right_stick_x * ninja;

                robot.setDrivePower(new Pose2d(x, y, rx));
            }

            //dt strafing
            if(strafeLeft.wasJustPressed()&&!robot.isBusy())
            {
                Trajectory strafeLeft=robot.trajectoryBuilder(robot.getPoseEstimate())
                        .strafeLeft(1.5)
                        .build();

                robot.followTrajectoryAsync(strafeLeft);
            }
            else if(strafeRight.wasJustPressed()&&!robot.isBusy())
            {
                Trajectory strafeRight=robot.trajectoryBuilder(robot.getPoseEstimate())
                        .strafeRight(1.5)
                        .build();

                robot.followTrajectoryAsync(strafeRight);
            }

            //cancel strafe in case borken
            if(gamepad1.x)
            {
                //robot cancel trajectory function(oop I forgor)
            }

            //hang manual
            if(gamepad2.left_trigger>0.5)
            {
                hang.setPower(1);
            }
            else if(gamepad2.right_trigger>0.5)
            {
                hang.setPower(-1);
            }
            else
            {
                hang.setPower(0);
            }
        }
    }

    @Override
    public void initialize()
    {
        this.setLoopTimes(0);

        hang=hardwareMap.get(DcMotor.class, "hang");

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
                slideUpBase = new ToggleButtonReader(g2, GamepadKeys.Button.DPAD_LEFT),
                slideUpRow1= new ToggleButtonReader(g2, GamepadKeys.Button.DPAD_UP),
                slideUpRow2=new ToggleButtonReader(g2, GamepadKeys.Button.DPAD_RIGHT),
                slideDown = new ToggleButtonReader(g2, GamepadKeys.Button.DPAD_DOWN),
                pusher1=new ToggleButtonReader(g1, GamepadKeys.Button.RIGHT_BUMPER),
                pusher2=new ToggleButtonReader(g2, GamepadKeys.Button.X),
                intake1=new ToggleButtonReader(g2, GamepadKeys.Button.LEFT_BUMPER),
                intake2=new ToggleButtonReader(g2, GamepadKeys.Button.RIGHT_BUMPER)
        };

        intakepositions=new Intake.PositionState[]
                {
                        Intake.PositionState.TELE, Intake.PositionState.ONE, Intake.PositionState.TWO,
                        Intake.PositionState.THREE, Intake.PositionState.FOUR, Intake.PositionState.FIVE,
                        Intake.PositionState.HIGH
                };

        slideupbase=builder.createNew()
                //.executeCode(()->slidesMoving=true)
                .moduleAction(deposit, Deposit.WristState.CRADLE)
                .delay(200)
                .moduleAction(slides, Slides.SlideState.RAISED)
                .await(()->slides.currentPosition()>100)
                .moduleAction(deposit, Deposit.RotationState.DEPOSIT_MID)
                .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                .moduleAction(deposit, Deposit.PusherState.EXTENDED)
                .await(()->slides.getStatus()==Module.Status.IDLE)
                .executeCode(()->slidesMoving=false)
                .moduleAction(deposit, Deposit.RotationState.DEPOSIT_HIGH)
                .build();

        slideup1=builder.createNew()
                //.executeCode(()->slidesMoving=true)
                .moduleAction(deposit, Deposit.WristState.CRADLE)
                .delay(200)
                .moduleAction(slides, Slides.SlideState.ROW1)
                .await(()->slides.currentPosition()>100)
                .moduleAction(deposit, Deposit.RotationState.DEPOSIT_MID)
                .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                .moduleAction(deposit, Deposit.PusherState.EXTENDED)
                .await(()->slides.getStatus()==Module.Status.IDLE)
                .executeCode(()->slidesMoving=false)
                .moduleAction(deposit, Deposit.RotationState.DEPOSIT_HIGH)
                .build();

        slideup2=builder.createNew()
                //.executeCode(()->slidesMoving=true)
                .moduleAction(deposit, Deposit.WristState.CRADLE)
                .delay(200)
                .moduleAction(slides, Slides.SlideState.ROW2)
                .await(()->slides.currentPosition()>100)
                .moduleAction(deposit, Deposit.RotationState.DEPOSIT_MID)
                .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                .moduleAction(deposit, Deposit.PusherState.EXTENDED)
                .await(()->slides.getStatus()==Module.Status.IDLE)
                .executeCode(()->slidesMoving=false)
                .moduleAction(deposit, Deposit.RotationState.DEPOSIT_HIGH)
                .build();

        slideup1raised=builder.createNew()
                .moduleAction(slides, Slides.SlideState.ROW1)
                .awaitPreviousModuleActionCompletion()
                .executeCode(()->slidesMoving=false)
                .build();

        slideup2raised=builder.createNew()
                .moduleAction(slides, Slides.SlideState.ROW2)
                .awaitPreviousModuleActionCompletion()
                .executeCode(()->slidesMoving=false)
                .build();

        slidedown=builder.createNew()
                //.executeCode(()->slidesMoving=true)
                .moduleAction(deposit, Deposit.RotationState.DEPOSIT_MID)
                .delay(300)
                .moduleAction(slides, Slides.SlideState.GROUND)
                .moduleAction(deposit, Deposit.PusherState.IN)
                .await(()->slides.currentPosition()<100)
                .moduleAction(deposit, Deposit.RotationState.TRANSFER)
                .moduleAction(deposit, Deposit.WristState.TRANSFER)
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

        robot.primaryLoop();
        robot.update();
    }
}

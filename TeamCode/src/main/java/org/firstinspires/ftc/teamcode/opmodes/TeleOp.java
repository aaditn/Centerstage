package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.KeyReader;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.DroneLauncher;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.RobotActions;
import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

public class TeleOp extends EnhancedOpMode
{
    DcMotor hang;
    Robot robot;
    TaskScheduler scheduler;
    RobotActions actions;
    Deposit deposit;
    Intake intake;
    DroneLauncher drone;
    Slides slides;
    boolean slidesMoving;
    ElapsedTime slidestimer;
    GamepadEx g1, g2;
    Gamepad.RumbleEffect customRumbleEffect0;
    Gamepad.RumbleEffect customRumbleEffect1;
    KeyReader[] keyReaders;
    ButtonReader droneButton1, intakeToggle, sweeperIncrement;
    double ninja;
    int sweeperCounter;
    Intake.SweeperState[] sweeperPositions;

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

            //HANG
            if(gamepad2.left_trigger>0.5)
                hang.setPower(1);
            else if(gamepad2.right_trigger>0.5)
                hang.setPower(-1);
            else
                hang.setPower(0);

            //NORMAL DT MOVEMENT
            if(!robot.isBusy())
            {
                double x = gamepad1.left_stick_y * ninja;
                double y = gamepad1.left_stick_x * ninja;
                double rx = -gamepad1.right_stick_x * ninja;

                robot.setLocalDrivePowers(new Pose2d(x, y, rx));
            }

            //NINJA
            if (gamepad1.right_trigger > 0.3)
                ninja = 0.5;
            else
                ninja = 1;

            //INTAKE MANUAL POWER
            if(Math.abs(gamepad2.right_stick_y)>0.3)
            {
                intake.setOperationState(Module.OperationState.MANUAL);
                intake.manualChange(-gamepad2.right_stick_y);
            }
            //INTAKE TOGGLE
            if(intakeToggle.wasJustPressed())
            {
                intake.setOperationState(Module.OperationState.PRESET);

                if(intake.getState(Intake.PositionState.class)==Intake.PositionState.RAISED)
                    scheduler.scheduleTaskList(actions.lowerIntake());
                else if(intake.getState(Intake.PositionState.class)==Intake.PositionState.DOWN)
                {
                    scheduler.scheduleTaskList(actions.raiseIntake());
                    sweeperCounter=0;
                }
            }
            //INTAKE SWEEPERS
            if(sweeperIncrement.wasJustPressed())
            {
                if(sweeperCounter<3)
                    sweeperCounter++;
                intake.setState(sweeperPositions[sweeperCounter-1]);
            }

            //GRAB AND HOLD



            //DRONE
            if (droneButton1.wasJustPressed())
            {
                if(drone.getState()==DroneLauncher.State.LOCKED)
                    drone.setState(DroneLauncher.State.RELEASED);
                else if(drone.getState()==DroneLauncher.State.RELEASED)
                    drone.setState(DroneLauncher.State.LOCKED);
            }
        }
    }

    @Override
    public void initialize()
    {
        this.setLoopTimes(1);
        hang=hardwareMap.get(DcMotor.class, "hang");
        robot=Robot.getInstance();
        scheduler=new TaskScheduler();
        actions=RobotActions.getInstance();

        deposit=robot.deposit;
        intake=robot.intake;
        slides=robot.slides;
        drone=robot.droneLauncher;

        intake.init();
        deposit.init();
        drone.init();
        intake.setOperationState(Module.OperationState.MANUAL);

        slidesMoving=false;
        slidestimer=new ElapsedTime();
        g1=new GamepadEx(gamepad1);
        g2=new GamepadEx(gamepad2);

        customRumbleEffect0 = new Gamepad.RumbleEffect.Builder()
                .addStep(1.0, 1.0, 200)
                .addStep(0.0, 0.0, 1000) //  Rumble right motor 100% for 500 mSec
                .build();

        customRumbleEffect1 = new Gamepad.RumbleEffect.Builder()
                .addStep(1.0, 1.0, 200)
                .addStep(0.0, 0.0, 1000)
                .addStep(1.0, 1.0, 200)
                .addStep(0.0, 0.0, 1000)//  Rumble right motor 100% for 500 mSec
                .build();

        keyReaders= new KeyReader[]{
                droneButton1=new ToggleButtonReader(g1, GamepadKeys.Button.Y),
                intakeToggle=new ToggleButtonReader(g1, GamepadKeys.Button.A),
                sweeperIncrement=new ToggleButtonReader(g1, GamepadKeys.Button.RIGHT_BUMPER)
        };

        sweeperPositions=new Intake.SweeperState[]{
                Intake.SweeperState.ONE_SWEEP,
                Intake.SweeperState.TWO_SWEEP,
                Intake.SweeperState.THREE_SWEEP
        };
    }

    public void initLoop()
    {
        robot.initLoop();
    }
    @Override
    public void primaryLoop()
    {
        robot.primaryLoop();
    }
}

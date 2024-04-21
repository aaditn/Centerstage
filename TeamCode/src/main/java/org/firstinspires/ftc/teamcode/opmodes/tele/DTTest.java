package org.firstinspires.ftc.teamcode.opmodes.tele;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.KeyReader;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.arcrobotics.ftclib.gamepad.TriggerReader;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.DroneLauncher;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.RobotActions;
import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.util.Tel;

@TeleOp(name = "A - DT LOL")
@Config
public class DTTest extends EnhancedOpMode
{
    DcMotorEx hang;
    Robot robot;
    TaskScheduler scheduler;
    RobotActions actions;
    Deposit deposit;
    Intake intake;
    DroneLauncher drone;
    Slides slides;
    GamepadEx g1, g2;
    double x,y,rx;
    Gamepad.RumbleEffect customRumbleEffect0;
    Gamepad.RumbleEffect customRumbleEffect1;
    KeyReader[] keyReaders;

    double[] positions;
    ButtonReader droneButton1, intakeToggle, presetMacro, slidesLower, flipRotator, tiltRotator, slideDown, slideUp, lala;
    TriggerReader intakeUp_rotateLeft, intakeDown_rotateRight;
    double ninja = 1;
    double ninjaStrafe = 1;
    @Override
    public void linearOpMode() {
        waitForStart();



        while (opModeIsActive()) {
            if (Math.abs(gamepad1.left_stick_y) < 0.3) {
                x = ninja * 1.67 * Math.abs(gamepad1.left_stick_y);
            } else {
                x = ninja * (0.33 + 0.6 * Math.pow(Math.abs(gamepad1.left_stick_y), 1.4));
            }

            if (Math.abs(gamepad1.left_stick_x) < 0.3) {
                y = ninjaStrafe * 1 * Math.abs(gamepad1.left_stick_x);
            } else {
                y = ninjaStrafe * (0.1412 + 0.6 * Math.pow(Math.abs(gamepad1.left_stick_x), 1.4));
            }
            if (Math.abs(gamepad1.right_stick_x) < 0.3) {
                rx = ninja * 0.5 * Math.pow(Math.abs(gamepad1.right_stick_x), 0.333);
            } else {
                rx = ninja * (0.33 + 0.5 * Math.pow(Math.abs(gamepad1.right_stick_x), 1.4));
            }
            if (x > 0.85 && rx < 0.2) {
                x *= 1.5;
                y /= 1.5;

            }
            if (x < 0.2 && rx > 0.7) {
                x /= 1.5;
                y *= 1.5;
                rx *= 1.5;
            }
            if (y > 0.5) {
                x = 0;
                y *= 1 + (y - 0.5) * 3;
                rx *= 1.5;
            }
            x *= Math.signum(gamepad1.left_stick_y);
            y *= Math.signum(gamepad1.left_stick_x);
            rx *= Math.signum(gamepad1.right_stick_x);


            robot.setLocalDrivePowers(new Pose2d(x, y, -rx));


        }

    }

    @Override
    public void initialize() {
        this.setLoopTimes(1);
        robot = Robot.getInstance();

        g1 = new GamepadEx(gamepad1);
        g2 = new GamepadEx(gamepad2);

        gamepad1.setLedColor(1, 1, 1, 1000);

        customRumbleEffect0 = new Gamepad.RumbleEffect.Builder()
                .addStep(1.0, 1.0, 200)
                .addStep(0.0, 0.0, 1000) //  Rumble right motor 100% for 500 mSec
                .build();

        customRumbleEffect1 = new Gamepad.RumbleEffect.Builder()
                .addStep(1.0, 1.0, 200)
                .addStep(0.0, 0.0, 300)
                .addStep(1.0, 1.0, 200)
                .addStep(0.0, 0.0, 1000)//  Rumble right motor 100% for 500 mSec
                .build();

        keyReaders = new KeyReader[]{
                droneButton1 = new ToggleButtonReader(g1, GamepadKeys.Button.DPAD_LEFT),
                intakeToggle = new ToggleButtonReader(g1, GamepadKeys.Button.A),
                intakeDown_rotateRight = new TriggerReader(g1, GamepadKeys.Trigger.RIGHT_TRIGGER),
                intakeUp_rotateLeft = new TriggerReader(g1, GamepadKeys.Trigger.LEFT_TRIGGER),
                presetMacro = new ToggleButtonReader(g1, GamepadKeys.Button.RIGHT_BUMPER),
                slidesLower = new ToggleButtonReader(g1, GamepadKeys.Button.LEFT_BUMPER),
                tiltRotator = new ToggleButtonReader(g1, GamepadKeys.Button.B),
                flipRotator = new ToggleButtonReader(g1, GamepadKeys.Button.A),
                slideDown = new ToggleButtonReader(g2, GamepadKeys.Button.DPAD_DOWN),
                slideUp = new ToggleButtonReader(g2, GamepadKeys.Button.DPAD_UP),
                lala = new ToggleButtonReader(g2, GamepadKeys.Button.RIGHT_STICK_BUTTON)
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
        double dt = robot.getTotalCurrent();
        Tel.instance().addData("current", dt);
        Tel.instance().addData("x", x);
        Tel.instance().addData("x", y);
        Tel.instance().addData("x", rx);
    }
}

//flick
//heights


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
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.DroneLauncher;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.RobotActions;
import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.util.PresetPackage;
import org.firstinspires.ftc.teamcode.util.Tel;

import java.util.Arrays;
import java.util.List;

@TeleOp(name = "A - Ethan Opp")
@Config
public class EthanOpp extends EnhancedOpMode
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
    ButtonReader droneButton1, intakeToggle, presetMacro, slidesLower, flipRotator, tiltRotator, slideDown, slideUp, lala, intakePos;
    TriggerReader intakeUp_rotateLeft, intakeDown_rotateRight;
    double ninja = 1;
    double ninjaStrafe = 1;
    int slidesIndex = 0;
    int sweeperCounter;
    ElapsedTime extakeTimer = new ElapsedTime();
    Intake.SweeperState[] sweeperPositions;
    Slides.SlideState[] slideStates;
    List<Deposit.RotateState> rotateStates;
    PresetPackage activePreset = new PresetPackage(Slides.SlideState.R1, Deposit.RotateState.PLUS_NINETY, Deposit.WristState.HOVER);
    boolean isHang = false;
    boolean isChanged = false;
    boolean lastTouchpad, touchpadVal;
    Deposit.WristState state = Deposit.WristState.HOVER;
    boolean isStopped;
    ElapsedTime hangWait = new ElapsedTime();

    boolean isSweeperDown = false;
    public static double purple = 0;

    public enum DriveType implements ModuleState {
        NORMAL, EXIT_BACKSTAGE
    }

    public static DriveType driveType = DriveType.NORMAL;
    @Override
    public void linearOpMode() {
        waitForStart();

        hangWait.reset();

        intake.setState(Intake.PositionState.MID);

        driveType = DriveType.EXIT_BACKSTAGE;


        while (opModeIsActive()) {
            for (KeyReader reader : keyReaders) {
                reader.readValue();
                if ((reader.stateJustChanged() && (!slides.getState().equals(Slides.SlideState.GROUND))) || presetMacro.stateJustChanged()) {
                    isChanged = true;
                }
            }

            if (driveType.equals(DriveType.NORMAL)) {
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
                x *= Math.signum(gamepad1.left_stick_y);
                y *= Math.signum(gamepad1.left_stick_x);
                rx *= Math.signum(gamepad1.right_stick_x);


                robot.setLocalDrivePowers(new Pose2d(x, y, -rx));
            } else if (driveType.equals(DriveType.EXIT_BACKSTAGE)) {
                ElapsedTime timer = new ElapsedTime();
                while (opModeIsActive() && !isStopRequested() && timer.milliseconds() < 100) { // teehee loop times go brrr intake.setState(Intake.PowerState.OFF);robot.setLocalDrivePowers(new Pose2d(1, 0, -0.3));
                    // robot.setLocalDrivePowers(new Pose2d(1, 0, -0.3));
                }
                driveType = DriveType.NORMAL;
            }

            if (gamepad2.left_trigger > 0.5) {
                hang.setPower(-1);
                isHang = true;
            } else if (gamepad2.right_trigger > 0.5) {
                hang.setPower(1);
                isHang = false;
            } else {
                hang.setPower(0);
            }


            if (Slides.SlideState.GROUND.equals(slides.getState(Slides.SlideState.class))) {
                isStopped = true;
            }
            //INTAKE SWEEPERS
            if (slides.getState().equals(Slides.SlideState.GROUND)) {
                if (intakeDown_rotateRight.wasJustPressed()) {
                    if (!isSweeperDown) {
                        intake.setState(Intake.PositionState.DOWN);
                        isSweeperDown = true;
                    } else {
                        if (sweeperCounter < 6) {
                            sweeperCounter++;
                        }
                        intake.setState(sweeperPositions[sweeperCounter - 1]);
                    }
                }
                if (intakeUp_rotateLeft.wasJustPressed()) {
                    intake.setState(Intake.PositionState.MID);
                    sweeperCounter = 0;
                    intake.setState(Intake.SweeperState.ZERO);
                    isSweeperDown = false;
                }
            }
            // deposit angle offsets
            else {
                if (intakeDown_rotateRight.isDown() && intakeDown_rotateRight.stateJustChanged()) {
                    activePreset.rotateState = Deposit.RotateState.MINUS_FOURTY_FIVE;
                    if (slidesIndex < slideStates.length - 1) {
                        slidesIndex++;
                        activePreset.slideState = slideStates[slidesIndex];
                    }
                } else if (intakeDown_rotateRight.wasJustReleased()) {
                    activePreset.rotateState = Deposit.RotateState.PLUS_NINETY;
                    if (slidesIndex > 0) {
                        slidesIndex--;
                    }
                    activePreset.slideState = slideStates[slidesIndex];
                }
                if (intakeUp_rotateLeft.isDown() && intakeUp_rotateLeft.stateJustChanged()) {
                    activePreset.rotateState = Deposit.RotateState.PLUS_FOURTY_FIVE;
                    if (slidesIndex < slideStates.length - 1) {
                        slidesIndex++;
                        activePreset.slideState = slideStates[slidesIndex];
                    }
                } else if (intakeUp_rotateLeft.wasJustReleased()) {
                    activePreset.rotateState = Deposit.RotateState.PLUS_NINETY;
                    if (slidesIndex > 0) {
                        slidesIndex--;
                    }
                    activePreset.slideState = slideStates[slidesIndex];
                }
            }

            if ((slidesLower.isDown())) {
                ninja = 0.5;
                ninjaStrafe = 0.7;
            }
            if (slidesLower.wasJustReleased()) {
                scheduler.scheduleTaskList(actions.scorePixels());
                ninja = 1;
                ninjaStrafe = 1;
            }
            if (presetMacro.wasJustReleased()) {
                isStopped = false;
                activePreset.wristState = Deposit.WristState.HOVER;
                activePreset.rotateState = Deposit.RotateState.PLUS_NINETY;
                slidesIndex++;
                activePreset.slideState = slideStates[slidesIndex];
            } else if (presetMacro.isDown() && presetMacro.stateJustChanged()) {
                ninja = 0.5;
                ninjaStrafe = 0.7;
                activePreset.rotateState = Deposit.RotateState.ZERO;
                activePreset.wristState = Deposit.WristState.ADJUST;
                if (slidesIndex > 0) {
                    slidesIndex--;
                    activePreset.slideState = slideStates[slidesIndex];
                }
            }

            //DRONE
            if (droneButton1.wasJustPressed()) {
                if (drone.getState() == DroneLauncher.State.LOCKED)
                    drone.setState(DroneLauncher.State.RELEASED);
                else if (drone.getState() == DroneLauncher.State.RELEASED)
                    drone.setState(DroneLauncher.State.LOCKED);
            }

           if (intakeToggle.wasJustPressed()) {
                if (intake.getState(Intake.PowerState.class).equals(Intake.PowerState.OFF)) {
                    intake.setState(Intake.PowerState.INTAKE);
                    intake.setState(Intake.PowerState.OFF);
                } else {
                    intake.setState(Intake.PowerState.OFF);
                }
            }
            if (gamepad2.touchpad && !lastTouchpad) {
                if (touchpadVal) {
                    intake.setState(Intake.PowerState.OFF);
                    intake.setState(Intake.ConveyorState.OFF);
                }
                lastTouchpad = true;
                touchpadVal = !touchpadVal;
            } else if (lastTouchpad) {
                lastTouchpad = gamepad2.touchpad;
            }

           if (intake.isUnderCurrent(20)) {
                if (!touchpadVal) {
                    if (Math.abs(gamepad2.right_stick_x + gamepad2.right_stick_y) > 0.2) {
                        boolean isUp = -Math.signum(gamepad2.right_stick_y) > 0;
                        boolean isRight = Math.signum(gamepad2.right_stick_x) > 0;
                        if (isUp && isRight) { // quadrant 1
                            intake.setState(Intake.PowerState.INTAKE);
                            intake.setState(Intake.ConveyorState.INTAKE);
                        } else if (!isUp && isRight) { // quadrant 4
                            intake.setState(Intake.PowerState.EXTAKE);
                            intake.setState(Intake.ConveyorState.INTAKE);
                        } else {
                            intake.setState(Intake.PowerState.EXTAKE);
                            intake.setState(Intake.ConveyorState.EXTAKE);
                        }
                    } else {
                        intake.setState(Intake.PowerState.INTAKE);
                        intake.setState(Intake.ConveyorState.INTAKE);
                    }
                } else {
                    intake.setState(Intake.PowerState.OFF);
                    intake.setState(Intake.ConveyorState.OFF);
                }
                extakeTimer.reset();
            } else if (extakeTimer.milliseconds() < 500 || intake.isUnderCurrent(20)) {
                gamepad1.rumble(500);
                gamepad2.rumble(500);
                intake.setState(Intake.PowerState.EXTAKE);
                intake.setState(Intake.ConveyorState.EXTAKE);
            }

           if (intakePos.wasJustPressed()) {
               if (intake.getState(Intake.PositionState.class).equals(Intake.PositionState.MID)) {
                   intake.setState(Intake.PositionState.DOWN);
               } else {
                   intake.setState(Intake.PositionState.MID);
               }
           }

            if (slideUp.wasJustPressed() && slidesIndex < slideStates.length - 1) {
                slidesIndex++;
                activePreset.slideState = slideStates[slidesIndex];
            }
            if (slideDown.wasJustPressed() && slidesIndex > 0) {
                slidesIndex--;
                activePreset.slideState = slideStates[slidesIndex];
            }

            if (tiltRotator.wasJustPressed() && deposit.getState(Deposit.WristState.class).equals(Deposit.WristState.ADJUST)) {
                if (slidesIndex > 0) {
                    slidesIndex--;
                }
                activePreset.slideState = slideStates[slidesIndex];
            } else if (tiltRotator.wasJustPressed()) {
                if (deposit.getState(Deposit.RotateState.class).equals(Deposit.RotateState.MINUS_FIFTEEN)) {
                    activePreset.rotateState = Deposit.RotateState.PlUS_FIFTEEN;
                } else {
                    activePreset.rotateState = Deposit.RotateState.MINUS_FIFTEEN;
                }
            }
            if (flipRotator.wasJustPressed() && deposit.getState(Deposit.WristState.class).equals(Deposit.WristState.ADJUST)) {
                if (slidesIndex < slideStates.length - 1) {
                    slidesIndex++;
                }
                activePreset.slideState = slideStates[slidesIndex];
            } else if (flipRotator.wasJustPressed()) {
                int index = rotateStates.indexOf(deposit.getState(Deposit.RotateState.class));
                if (index != -1) {
                    if (index <= 3) {
                        index += 4;
                    } else {
                        index -= 4;
                    }
                    activePreset.rotateState = rotateStates.get(index);
                } else {
                    activePreset.rotateState = Deposit.RotateState.MINUS_ONE_EIGHTY;
                }

            }
            if (isChanged) {
                scheduler.scheduleTaskList(actions.slidesOnly(activePreset));
                isChanged = false;
            }

        }

    }

    @Override
    public void initialize() {
        this.setLoopTimes(1);
        robot = Robot.getInstance();
        hang = hardwareMap.get(DcMotorEx.class, "hang");
        scheduler = new TaskScheduler();
        actions = RobotActions.getInstance();

        deposit = robot.deposit;
        intake = robot.intake;
        slides = robot.slides;
        drone = robot.droneLauncher;

        intake.init();
        deposit.init();
        drone.init();
        intake.setOperationState(Module.OperationState.PRESET);


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
                lala = new ToggleButtonReader(g2, GamepadKeys.Button.RIGHT_STICK_BUTTON),
                intakePos = new ToggleButtonReader(g2, GamepadKeys.Button.A)
        };

        sweeperPositions = new Intake.SweeperState[]{
                Intake.SweeperState.ONE_SWEEP,
                Intake.SweeperState.TWO_SWEEP,
                Intake.SweeperState.THREE_SWEEP,
                Intake.SweeperState.FOUR_SWEEP,
                Intake.SweeperState.FIVE_SWEEP,
                Intake.SweeperState.SIX_SWEEP
        };

        slideStates = new Slides.SlideState[]{
                Slides.SlideState.HALF,
                Slides.SlideState.R1,
                Slides.SlideState.R2,
                Slides.SlideState.R3,
                Slides.SlideState.R4,
                Slides.SlideState.R5,
                Slides.SlideState.R6,
                Slides.SlideState.R7,
                Slides.SlideState.R8,
                Slides.SlideState.R9
        };
        rotateStates = Arrays.asList(
            Deposit.RotateState.MINUS_ONE_EIGHTY,
            Deposit.RotateState.MINUS_ONE_THREE_FIVE,
            Deposit.RotateState.MINUS_NINETY,
            Deposit.RotateState.MINUS_FOURTY_FIVE,
            Deposit.RotateState.ZERO,
            Deposit.RotateState.PLUS_FOURTY_FIVE,
            Deposit.RotateState.PLUS_NINETY,
            Deposit.RotateState.PLUS_ONE_THREE_FIVE
        );


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
    }
}

//flick
//heights


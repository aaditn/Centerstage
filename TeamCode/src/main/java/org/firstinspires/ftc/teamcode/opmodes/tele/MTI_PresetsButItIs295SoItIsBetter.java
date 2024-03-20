package org.firstinspires.ftc.teamcode.opmodes.tele;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.KeyReader;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
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

@TeleOp(name = "A - Tele_Presets 295")
public class MTI_PresetsButItIs295SoItIsBetter extends EnhancedOpMode
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
    ButtonReader droneButton1, intakeToggle, sweeperIncrement, presetMacro, slidesOverride, slidesLower, addToArray, subtractToArray, switchWrist;
    double ninja = 1;
    double ninjaStrafe = 1;
    int sweeperCounter;
    Intake.SweeperState[] sweeperPositions;
    PresetPackage[] presetsOld;

    PresetPackage[] presets;

    boolean runOnce = true, isHang = false;

    Deposit.WristState state = Deposit.WristState.HOVER;
    String stateName = "FLICK";
    boolean isStopped;

    int arrayIndex = 0;

    double multiplier;
    ElapsedTime hangWait = new ElapsedTime();

    public static enum DriveType implements ModuleState {
        NORMAL, EXIT_BACKSTAGE
    }

    public static DriveType driveType;
    @Override
    public void linearOpMode()
    {
        waitForStart();

        hangWait.reset();
        hang.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        hang.setTargetPosition(0);
//        hang.setPower(0);
        hang.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        intake.setState(Intake.PositionState.MID);

        while(opModeIsActive())
        {
            for (KeyReader reader : keyReaders)
            {
                reader.readValue();
            }
            if (driveType.equals(DriveType.NORMAL)) {
                if (Math.abs(gamepad1.left_stick_y) < 0.3) {
                    x = ninja * 1.67 * Math.abs(gamepad1.left_stick_y);
                } else {
                    x = ninja *  ( 0.33 +  0.6 * Math.pow(Math.abs(gamepad1.left_stick_y), 1.4));
                }

                if (Math.abs(gamepad1.left_stick_x) < 0.3) {
                    y = ninjaStrafe * 1 * Math.abs(gamepad1.left_stick_x);
                } else {
                    y = ninjaStrafe *  (0.1412 +  0.6 * Math.pow(Math.abs(gamepad1.left_stick_x), 1.4));
                }
                if (Math.abs(gamepad1.right_stick_x) < 0.3) {
                    rx = ninja * 0.5 * Math.pow(Math.abs(gamepad1.right_stick_x), 0.333);
                } else {
                    rx = ninja *  (0.33 +  0.5 * Math.pow(Math.abs(gamepad1.right_stick_x), 1.4));
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
                while (opModeIsActive() && !isStopRequested() && timer.milliseconds() < 300) { // teehee loop times go brrr
                    robot.setLocalDrivePowers(new Pose2d(0.8, 0, -0.6));
                }
                driveType = DriveType.NORMAL;
            }

            if(gamepad2.left_trigger>0.5 || (gamepad1.left_trigger>0.5 && hangWait.seconds() > 90)) {
                hang.setPower(-1);
                isHang = true;
            } else if(gamepad1.right_trigger>0.5 && hangWait.seconds() > 90) {
                hang.setPower(1);
                isHang = false;
            } else {
                hang.setPower(0);
            }


            if (Slides.SlideState.GROUND.equals(slides.getState(Slides.SlideState.class))) {
                isStopped = true;
            }
            //INTAKE SWEEPERS
            if(sweeperIncrement.wasJustPressed()&&intake.getState(Intake.PositionState.class)==Intake.PositionState.DOWN)
            {
                if(sweeperCounter<6)
                    sweeperCounter++;
                intake.setState(sweeperPositions[sweeperCounter-1]);
            }

            if((slidesLower.isDown())) {
                ninja = 0.4;
                ninjaStrafe = 0.3;
            }
            if (slidesLower.wasJustReleased()) {
                scheduler.scheduleTaskList(actions.scorePixels());
                ninja = 1;
                ninjaStrafe = 1;
                arrayIndex++;
                presets[arrayIndex].returnLEDs(gamepad1);
                presets[arrayIndex].returnLEDs(gamepad2);
            }
            if (presetMacro.wasJustPressed()) {

                if (arrayIndex < presets.length) {
                    PresetPackage presetPackage = presets[arrayIndex];
                    isStopped = false;
                    scheduler.scheduleTaskList(actions.slidesOnly(presetPackage.slideState, presetPackage.rotateState, state));
                }
            }

            if (switchWrist.wasJustPressed()) {
                if (state == Deposit.WristState.FLICK) {
                    state = Deposit.WristState.HOVER;
                    stateName = "HOVER";
                } else {
                    state = Deposit.WristState.FLICK;
                    stateName = "FLICK";
                }

                if (!slides.getState().equals(Slides.SlideState.GROUND)) {
                    scheduler.scheduleTaskList(actions.slidesOnly(null, null, state));
                }
            }

            if (addToArray.wasJustPressed()) {
                arrayIndex++;
            }
            if (subtractToArray.wasJustPressed()) {
                arrayIndex--;
            }

            //DRONE
            if (droneButton1.wasJustPressed())
            {
                if(drone.getState()==DroneLauncher.State.LOCKED)
                    drone.setState(DroneLauncher.State.RELEASED);
                else if(drone.getState()==DroneLauncher.State.RELEASED)
                    drone.setState(DroneLauncher.State.LOCKED);
            }

            if (intakeToggle.wasJustPressed()) {
                if (intake.getState(Intake.PowerState.class).equals(Intake.PowerState.OFF)) {
                    intake.setState(Intake.PowerState.INTAKE);
                } else {
                    intake.setState(Intake.PowerState.OFF);
                }
            }
            if (gamepad1.x && !isHang) {
                intake.setState(Intake.PowerState.EXTAKE);
                intake.setState(Intake.ConveyorState.EXTAKE);
            } else if (Slides.SlideState.GROUND.equals(slides.getState(Slides.SlideState.class)) && isStopped && !isHang) {
                intake.setState(Intake.PowerState.INTAKE);
                intake.setState(Intake.ConveyorState.INTAKE);
            } else {
                intake.setState(Intake.PowerState.OFF);
                intake.setState(Intake.ConveyorState.OFF);
            }

        }
    }

    @Override
    public void initialize()
    {
        this.setLoopTimes(1);
        robot=Robot.getInstance();
        hang = hardwareMap.get(DcMotorEx.class, "hang");
        scheduler=new TaskScheduler();
        actions=RobotActions.getInstance();

        deposit=robot.deposit;
        intake=robot.intake;
        slides=robot.slides;
        drone=robot.droneLauncher;

        intake.init();
        deposit.init();
        drone.init();
        intake.setOperationState(Module.OperationState.PRESET);



        g1=new GamepadEx(gamepad1);
        g2=new GamepadEx(gamepad2);

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

        keyReaders= new KeyReader[]{
                droneButton1=new ToggleButtonReader(g1, GamepadKeys.Button.Y),
                intakeToggle=new ToggleButtonReader(g1, GamepadKeys.Button.A),
                sweeperIncrement=new ToggleButtonReader(g1, GamepadKeys.Button.X),
                presetMacro=new ToggleButtonReader(g1, GamepadKeys.Button.RIGHT_BUMPER),
                slidesLower=new ToggleButtonReader(g1, GamepadKeys.Button.LEFT_BUMPER),
                addToArray=new ToggleButtonReader(g1, GamepadKeys.Button.DPAD_UP),
                subtractToArray=new ToggleButtonReader(g1, GamepadKeys.Button.DPAD_DOWN),
                switchWrist=new ToggleButtonReader(g1, GamepadKeys.Button.B)
        };

        sweeperPositions=new Intake.SweeperState[]{
                Intake.SweeperState.ONE_SWEEP,
                Intake.SweeperState.TWO_SWEEP,
                Intake.SweeperState.THREE_SWEEP,
                Intake.SweeperState.FOUR_SWEEP,
                Intake.SweeperState.FIVE_SWEEP,
                Intake.SweeperState.SIX_SWEEP
        };
        presets = new PresetPackage[]{
                new PresetPackage(0, "yellow", "yellow", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.R1),
                new PresetPackage(1,"purple", "purple", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.HALF),
                new PresetPackage(2, "black", "black", Deposit.RotateState.MINUS_NINETY, Slides.SlideState.R1),
                new PresetPackage(3, "green", "green", Deposit.RotateState.PLUS_FOURTY_FIVE, Slides.SlideState.R35),
                new PresetPackage(4, "white", "white", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.R2),
                new PresetPackage(5, "white", "white", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.R2),
                new PresetPackage(6, "green", "white", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.R3),
                new PresetPackage(7, "white","white", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.R4),
                new PresetPackage(8,"yellow", "purple", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.R5),
                new PresetPackage(9,"green", "yellow", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.R6),
                new PresetPackage(10,"green", "purple", Deposit.RotateState.PLUS_FOURTY_FIVE, Slides.SlideState.R45),
                new PresetPackage(11, "white", "white", Deposit.RotateState.PlUS_FIFTEEN, Slides.SlideState.R8),
                new PresetPackage(12, "white","white", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.R4),
                new PresetPackage(13, "white","white", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.R4),
                new PresetPackage(14, "white","white", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.R5),

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
        Tel.instance().addData("Total Current Draw", robot.getTotalCurrent());
        Tel.instance().addData("Preset", presets[arrayIndex].color1 + ", " + presets[arrayIndex].color2);
        Tel.instance().addData("Hang", hang.getCurrentPosition());
        Tel.instance().addData("Wrist State", stateName);
        Tel.instance().addData("x", x);
        Tel.instance().addData("y", y);
        Tel.instance().addData("rx", rx);


    }
}

//flick
//heights


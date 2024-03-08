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
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.util.PresetPackage;
import org.firstinspires.ftc.teamcode.util.Tel;

@TeleOp(group = "MTI", name = "A - TelePresets")
public class MTI_Presets extends EnhancedOpMode
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
    Gamepad.RumbleEffect customRumbleEffect0;
    Gamepad.RumbleEffect customRumbleEffect1;
    KeyReader[] keyReaders;
    ButtonReader droneButton1, intakeToggle, sweeperIncrement, presetMacro, slidesOverride, flush, slidesLower;
    double ninja;
    int sweeperCounter;
    Intake.SweeperState[] sweeperPositions;
    Deposit.RotateState[] wristRotatePositions;
    Slides.SlideState[] slideHeights;

    PresetPackage[] presets;

    int arrayIndex = 0;
    ElapsedTime hangWait = new ElapsedTime();
    @Override
    public void linearOpMode()
    {
        waitForStart();

        hangWait.reset();
        while(opModeIsActive())
        {
            for (KeyReader reader : keyReaders)
            {
                reader.readValue();
            }


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


            if(gamepad2.left_trigger>0.5 && hangWait.seconds() > 90)
                hang.setPower(1);
            else if(gamepad2.right_trigger>0.5 && hangWait.seconds() > 90)
                hang.setPower(-1);
            else
                hang.setPower(0);

            //INTAKE TOGGLE
            if(intakeToggle.wasJustPressed())
            {
                intake.setOperationState(Module.OperationState.PRESET);

                if(intake.getState(Intake.PositionState.class)==Intake.PositionState.MID || intake.getState(Intake.PositionState.class)==Intake.PositionState.RAISED)
                {
                    scheduler.scheduleTaskList(actions.lowerIntake());
                }
                else if(intake.getState(Intake.PositionState.class)==Intake.PositionState.DOWN)
                {
                    scheduler.scheduleTaskList(actions.raiseIntake());
                    sweeperCounter=0;
                }
            }
            //INTAKE SWEEPERS
            if(sweeperIncrement.wasJustPressed()&&intake.getState(Intake.PositionState.class)==Intake.PositionState.DOWN)
            {
                if(sweeperCounter<6)
                    sweeperCounter++;
                intake.setState(sweeperPositions[sweeperCounter-1]);
            }
            //INTAKE FLUSH
            if(flush.isDown())
            {
                intake.setOperationState(Module.OperationState.PRESET);
                intake.setState(Intake.ConveyorState.INTAKE);
                intake.setState(Intake.PowerState.EXTAKE);
            }
            else if(flush.wasJustReleased())
            {
                intake.setOperationState(Module.OperationState.PRESET);
                intake.setState(Intake.PowerState.OFF);
                intake.setState(Intake.ConveyorState.OFF);
            }
            //INTAKE MANUAL POWER
            else if(Math.abs(gamepad2.right_stick_y)>0.5)
            {
                intake.setOperationState(Module.OperationState.MANUAL);
                intake.manualChange(-gamepad2.right_stick_y);
            }
            else
            {
                //intake.setState(Intake.ConveyorState.OFF);
                intake.setOperationState(Module.OperationState.PRESET);
            }

            if((slidesLower.wasJustPressed()) && !slides.getState().equals(Slides.SlideState.GROUND)) {
                scheduler.scheduleTaskList(actions.scorePixels());
                arrayIndex++;
            }
            if (presetMacro.wasJustPressed()) {

                PresetPackage presetPackage = presets[arrayIndex];
                scheduler.scheduleTaskList(actions.slidesOnly(presetPackage.slideState, presetPackage.rotateState));
            }

            //SLIDE RESET
            if(slidesOverride.wasJustReleased())
            {
                slides.setMotorRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }
            else if(slides.getMotorRunMode()==DcMotor.RunMode.STOP_AND_RESET_ENCODER)
            {
                slides.setState(Slides.SlideState.GROUND);
                slides.setOperationState(Module.OperationState.PRESET);
                slides.setMotorRunMode(DcMotor.RunMode.RUN_TO_POSITION);
            }


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
        intake.setOperationState(Module.OperationState.MANUAL);

        g1=new GamepadEx(gamepad1);
        g2=new GamepadEx(gamepad2);

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
                slidesLower=new ToggleButtonReader(g1, GamepadKeys.Button.RIGHT_BUMPER),
                flush=new ToggleButtonReader(g2, GamepadKeys.Button.X),
                slidesOverride=new ToggleButtonReader(g2, GamepadKeys.Button.B)
        };

        sweeperPositions=new Intake.SweeperState[]{
                Intake.SweeperState.ONE_SWEEP,
                Intake.SweeperState.TWO_SWEEP,
                Intake.SweeperState.THREE_SWEEP,
                Intake.SweeperState.FOUR_SWEEP,
                Intake.SweeperState.FIVE_SWEEP,
                Intake.SweeperState.SIX_SWEEP
        };
        wristRotatePositions=new Deposit.RotateState[]{
                Deposit.RotateState.MINUS_ONE_EIGHTY,
                Deposit.RotateState.MINUS_ONE_THREE_FIVE,
                Deposit.RotateState.MINUS_NINETY,
                Deposit.RotateState.MINUS_FOURTY_FIVE,
                Deposit.RotateState.ZERO,
                Deposit.RotateState.PLUS_FOURTY_FIVE,
                Deposit.RotateState.PLUS_NINETY,
                Deposit.RotateState.PLUS_ONE_THREE_FIVE,
        };
        slideHeights=new Slides.SlideState[]{
                Slides.SlideState.GROUND,

                Slides.SlideState.HALF,
                Slides.SlideState.R1,
                Slides.SlideState.R2,

                Slides.SlideState.R3,
                Slides.SlideState.R4,
                Slides.SlideState.R5,

                Slides.SlideState.R6,
                Slides.SlideState.R7,
                Slides.SlideState.R8,

                Slides.SlideState.R9,
        };

        presets = new PresetPackage[]{
                new PresetPackage(0, "Yellow Finish Mosaic", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.R1),
                new PresetPackage(1,"Start Purple Mosaic", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.HALF),
                new PresetPackage(2, "Finish Purple Mosaic", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.R1),
                new PresetPackage(3, "White Fill Mosaic Gap", Deposit.RotateState.MINUS_FOURTY_FIVE, Slides.SlideState.R2),
                new PresetPackage(4, "Blanket White Right", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.R2),
                new PresetPackage(5, "Blanket White Left", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.R2),
                new PresetPackage(6, "Yellow Colored Mosaic", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.R3),
                new PresetPackage(7, "Start Green Mosaic", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.R3),
                new PresetPackage(8,"Finish Green Mosaic", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.R4),
                new PresetPackage(9,"Purple Colored Mosaic", Deposit.RotateState.PLUS_FOURTY_FIVE, Slides.SlideState.R5),
                new PresetPackage(10, "Green Finish Mosaic", Deposit.RotateState.PLUS_FOURTY_FIVE, Slides.SlideState.R4),
                new PresetPackage(11,"Blanket White Right", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.R5),
                new PresetPackage(12,"Yellow Purple Colored Mosaic", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.R6),
                new PresetPackage(13,"Green Finish Colored Mosaic", Deposit.RotateState.PLUS_NINETY, Slides.SlideState.R7),
                new PresetPackage(14, "Reach 3rd Set Line", Deposit.RotateState.PLUS_FOURTY_FIVE, Slides.SlideState.R9)
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
        Tel.instance().addData("hang", hang.getCurrentPosition());

    }
}

//flick
//heights


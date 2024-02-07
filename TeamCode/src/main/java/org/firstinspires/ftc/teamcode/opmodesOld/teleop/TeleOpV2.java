package org.firstinspires.ftc.teamcode.opmodesOld.teleop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.KeyReader;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
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
import org.firstinspires.ftc.teamcode.util.Tel;
@Disabled
@TeleOp(name="A - Teleop")
public class TeleOpV2 extends EnhancedOpMode
{
    DcMotor hang;
    Robot robot;
    TaskScheduler scheduler;
    RobotActions actions;
    Deposit deposit;
    Intake intake;
    DroneLauncher drone;
    Slides slides;
    ElapsedTime slidestimer;
    GamepadEx g1, g2;
    Gamepad.RumbleEffect customRumbleEffect0;
    Gamepad.RumbleEffect customRumbleEffect1;
    KeyReader[] keyReaders;
    ButtonReader droneButton1, intakeToggle, sweeperIncrement, slidesBottomRow, slidesSetLine1, slidesSetLine2,
    slidesSetLine3, slidesOverride, depositMacro, depositMacro2, grabPixel, flush, CCW45, CW45, clawManual, pickOne;
    double ninja;
    int sweeperCounter;
    int wristRotateCounter=4;
    Intake.SweeperState[] sweeperPositions;
    Deposit.RotateState[] wristRotatePositions;
boolean pickState = false;
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


            //INTAKE TOGGLE
            if(intakeToggle.wasJustPressed())
            {
                intake.setOperationState(Module.OperationState.PRESET);

                if(intake.getState(Intake.PositionState.class)==Intake.PositionState.RAISED)
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
                if(sweeperCounter<3)
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
            else if(Math.abs(gamepad2.right_stick_y)>0.25)
            {
                intake.setOperationState(Module.OperationState.MANUAL);
                intake.manualChange(-gamepad2.right_stick_y);
            }
            else
            {
                //intake.setState(Intake.ConveyorState.OFF);
                intake.setOperationState(Module.OperationState.PRESET);
            }


            //GRAB AND HOLD
//            if(grabPixel.wasJustPressed()&&slides.getState()==Slides.SlideState.GROUND&&
//                    deposit.getState(Deposit.FlipState.class)==Deposit.FlipState.TRANSFER)
//            {
//                scheduler.scheduleTaskList(actions.grabAndHold());
//                //TODO tune floaty position(get it from Ethan)
//            }
            //MANUAL CLAW OPEN CLOSE
            if(clawManual.wasJustPressed())
            {
                if(deposit.getState(Deposit.ClawState.class)==Deposit.ClawState.OPEN)
                    deposit.setState(Deposit.ClawState.CLOSED2);
                else if(deposit.getState(Deposit.ClawState.class)==Deposit.ClawState.CLOSED2)
                    deposit.setState(Deposit.ClawState.OPEN);
            }


            //SPIN THE ROTATE WRIST
            if(CW45.wasJustPressed()&&wristRotateCounter<wristRotatePositions.length-1)
            {
                wristRotateCounter++;
                deposit.setState(wristRotatePositions[wristRotateCounter]);
            }
            else if(CCW45.wasJustPressed()&&wristRotateCounter>0)
            {
                wristRotateCounter--;
                deposit.setState(wristRotatePositions[wristRotateCounter]);
            }

if(pickOne.wasJustPressed()){
    pickState= !pickState;
    if(pickState) gamepad2.runRumbleEffect(customRumbleEffect0);
    else gamepad2.runRumbleEffect(customRumbleEffect1);
}

            //SLIDES
            if(slidesBottomRow.wasJustPressed()&&!slides.macroRunning)
            {
                slides.setOperationState(Module.OperationState.PRESET);
                if(pickState){

                    scheduler.scheduleTaskList(actions.slidesOnly(Slides.SlideState.RAISED,true));
                }
                else scheduler.scheduleTaskList(actions.slidesOnly(Slides.SlideState.RAISED));
            }
            else if(slidesSetLine1.wasJustPressed()&&!slides.macroRunning)
            {
                slides.setOperationState(Module.OperationState.PRESET);
                if(pickState){

                    scheduler.scheduleTaskList(actions.slidesOnly(Slides.SlideState.ROW1,true));
                }
                else scheduler.scheduleTaskList(actions.slidesOnly(Slides.SlideState.ROW1));
            }
            else if(slidesSetLine2.wasJustPressed()&&!slides.macroRunning)
            {
                slides.setOperationState(Module.OperationState.PRESET);
                if(pickState){

                    scheduler.scheduleTaskList(actions.slidesOnly(Slides.SlideState.ROW2,true));
                }
                else scheduler.scheduleTaskList(actions.slidesOnly(Slides.SlideState.ROW2));
            }
            else if(slidesSetLine3.wasJustPressed()&&!slides.macroRunning)
            {
                slides.setOperationState(Module.OperationState.PRESET);
                if(pickState){

                    scheduler.scheduleTaskList(actions.slidesOnly(Slides.SlideState.ROW3,true));
                }
                else scheduler.scheduleTaskList(actions.slidesOnly(Slides.SlideState.ROW3));
            }
            //SLIDES MANUAL
            if((slides.getState()!=Slides.SlideState.GROUND||slidesOverride.isDown())&&!slides.macroRunning&&Math.abs(gamepad2.left_stick_y)>0.3)
            {
                slides.setOperationState(Module.OperationState.MANUAL);

                if(slidestimer.milliseconds()>30)
                {
                    double newTarget=slides.getTargetPosition() + (15*Math.signum(gamepad2.left_stick_y)*-1);
//                    if(newTarget<300)
//                    {
//                        newTarget=300;
//                    }
                    slides.manualChange(newTarget);
                    slidestimer.reset();
                }
            }
            //DEPOSIT AND RESET
            if((sweeperIncrement.wasJustPressed() || depositMacro2.wasJustPressed() ||depositMacro.wasJustPressed())&&!slides.macroRunning&&slides.getState()!=Slides.SlideState.GROUND)
            {
                //slides.setOperationState(Module.OperationState.PRESET);
                scheduler.scheduleTaskList(actions.scorePixels());
                wristRotateCounter=4;
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

        slidestimer=new ElapsedTime();
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
                sweeperIncrement=new ToggleButtonReader(g1, GamepadKeys.Button.RIGHT_BUMPER),
                slidesBottomRow=new ToggleButtonReader(g2, GamepadKeys.Button.DPAD_DOWN),
                slidesSetLine1=new ToggleButtonReader(g2, GamepadKeys.Button.DPAD_LEFT),
                slidesSetLine2=new ToggleButtonReader(g2, GamepadKeys.Button.DPAD_UP),
                slidesSetLine3=new ToggleButtonReader(g2, GamepadKeys.Button.DPAD_RIGHT),
                depositMacro=new ToggleButtonReader(g1, GamepadKeys.Button.LEFT_BUMPER),
                pickOne=new ToggleButtonReader(g2, GamepadKeys.Button.A),
                flush=new ToggleButtonReader(g2, GamepadKeys.Button.X),
                CCW45=new ToggleButtonReader(g2, GamepadKeys.Button.LEFT_BUMPER),
                CW45=new ToggleButtonReader(g2, GamepadKeys.Button.RIGHT_BUMPER),
                clawManual=new ToggleButtonReader(g1, GamepadKeys.Button.X),
                depositMacro2= new ToggleButtonReader(g2, GamepadKeys.Button.Y),
                slidesOverride=new ToggleButtonReader(g2, GamepadKeys.Button.B)
        };

        sweeperPositions=new Intake.SweeperState[]{
                Intake.SweeperState.ONE_SWEEP,
                Intake.SweeperState.TWO_SWEEP,
                Intake.SweeperState.THREE_SWEEP
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
    }

    public void initLoop()
    {
        robot.initLoop();
    }

    @Override
    public void primaryLoop()
    {
        robot.primaryLoop();
        Tel.instance().addData("intake stick", gamepad2.right_stick_y);
    }
}

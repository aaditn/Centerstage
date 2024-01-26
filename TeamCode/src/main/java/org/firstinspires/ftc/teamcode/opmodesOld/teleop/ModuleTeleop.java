package org.firstinspires.ftc.teamcode.opmodesOld.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
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

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.modulesOld.DepositOld;
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
@Disabled
@Config
public class ModuleTeleop extends EnhancedOpMode
{
    Robot robot;
    Intake intake;
    DepositOld deposit;
    Slides slides;
    DroneLauncher droneLauncher;

    TaskListBuilder builder;
    List<Task> testTaskList;
    TaskScheduler scheduler;

    boolean slidesMoving=false;

    KeyReader[] keyReaders;
    GamepadEx g1, g2;
    ButtonReader slideUpBase, slideUpRow1, slideUpRow2, slideDown, pusher1, pusher2,
            strafeLeft, strafeRight, intake1, intake2, intake3, intakeFive, intakeFour, intakeThree, intakeTwo,
            droneButton1, slideReset, slidesOverride;
    List<Task> slideupbase, slideup1, slideup2, slidedown, slideupbaseraised, slideup1raised, slideup2raised, slideuphalf;
    int intakeposition;
    double ninja;
    Intake.OldPositionState[] intakepositions;

    DcMotor hang;
    ElapsedTime slidestimer;

    Gamepad.RumbleEffect customRumbleEffect0;
    Gamepad.RumbleEffect customRumbleEffect1;

    double driverLoopCount = 0;
    double hardwareLoopCount=0;
    ElapsedTime opmodetimer;

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
                if (deposit.getState(DepositOld.PusherState.class)== DepositOld.PusherState.EXTENDED)
                {
                    deposit.setState(DepositOld.PusherState.ONE);
                }
                else if(deposit.getState(DepositOld.PusherState.class)== DepositOld.PusherState.IN)
                {
                    deposit.setState(DepositOld.PusherState.ONE);
                }
                else if(deposit.getState(DepositOld.PusherState.class)== DepositOld.PusherState.ONE)
                {
                    deposit.setState(DepositOld.PusherState.HALF);
                }
                else if(deposit.getState(DepositOld.PusherState.class)== DepositOld.PusherState.HALF)
                {
                    deposit.setState(DepositOld.PusherState.TWO);
                }
                else if(deposit.getState(DepositOld.PusherState.class)== DepositOld.PusherState.TWO)
                {
                    deposit.setState(DepositOld.PusherState.IN);
                }

                gamepad1.runRumbleEffect(customRumbleEffect0);
                gamepad2.runRumbleEffect(customRumbleEffect0);

            }

            if(intake1.wasJustPressed())
            {
                if(intakeposition==0)
                {
                    intakeposition=intakepositions.length-1;
                }
                else {
                    intakeposition=0;
                }
                intake.setState(intakepositions[intakeposition]);
            }
            else if(intake2.wasJustPressed())
            {
                intakeposition++;
                if(intakeposition>intakepositions.length-1)
                {
                    intakeposition=0;
                }
                intake.setState(intakepositions[intakeposition]);
            } else if(intake3.wasJustPressed())
            {
                intakeposition--;
                if(intakeposition<0)
                {
                    intakeposition=intakepositions.length-1;
                }
                intake.setState(intakepositions[intakeposition]);
            }

            if (intakeFive.wasJustPressed())
            {
                intake.setState(Intake.OldPositionState.FIVE);
            }
            else if (intakeFour.wasJustPressed())
            {
                intake.setState(Intake.OldPositionState.FOUR);
            }
            else if (intakeThree.wasJustPressed())
            {
                intake.setState(Intake.OldPositionState.THREE);
            }
            else if (intakeTwo.wasJustPressed())
            {
                intake.setState(Intake.OldPositionState.TWO);
            }



            /*//set intake to high or low
            if(gamepad2.a) {
                if (intakeposition != 0) {
                    intakeposition = 0;
                } else if (intakeposition != 6) {
                    intakeposition = 6;
                }
            }

             */

            if (droneButton1.wasJustPressed()) {
                if(droneLauncher.getState()==DroneLauncher.State.LOCKED) {
                    droneLauncher.setState(DroneLauncher.State.RELEASED);
                }
                else if(droneLauncher.getState()==DroneLauncher.State.RELEASED)
                {
                    droneLauncher.setState(DroneLauncher.State.LOCKED);
                }
            }


            //slides macro
            if(slideUpBase.wasJustPressed()&&(slides.getState()==Slides.SlideState.GROUND||slides.getState()==Slides.SlideState.HALF)&&!slidesMoving)
            {
                //deposit.funnimode=true;
                slides.setOperationState(Module.OperationState.PRESET);
                slidesMoving=true;
                scheduler.scheduleTaskList(slideupbase);
            }
//drivers say its unnecessary
//            else if(gamepad1.b&&(slides.getState()==Slides.SlideState.GROUND||slides.getState()==Slides.SlideState.SLIDE_UP)&&!slidesMoving)
//            {
//                slides.setOperationState(Module.OperationState.PRESET);
//                slidesMoving=true;
//                scheduler.scheduleTaskList(slideshow);
//            }
            else if(slideUpRow1.wasJustPressed()&&(slides.getState()==Slides.SlideState.GROUND||slides.getState()==Slides.SlideState.HALF)&&!slidesMoving)
            {
                slides.setOperationState(Module.OperationState.PRESET);
                slidesMoving=true;
                scheduler.scheduleTaskList(slideup1);
            }
            else if(slideUpRow2.wasJustPressed()&&(slides.getState()==Slides.SlideState.GROUND||slides.getState()==Slides.SlideState.HALF)&&!slidesMoving)
            {
                slides.setOperationState(Module.OperationState.PRESET);
                slidesMoving=true;
                scheduler.scheduleTaskList(slideup2);
            }
            else if(slideUpBase.wasJustPressed()&&slides.getState()!=Slides.SlideState.GROUND&&!slidesMoving)
            {
                slides.setOperationState(Module.OperationState.PRESET);
                slidesMoving=true;
                scheduler.scheduleTaskList(slideupbaseraised);
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
                //deposit.funnimode=false;
                slides.setOperationState(Module.OperationState.PRESET);
                slidesMoving=true;
                scheduler.scheduleTaskList(slidedown);
            }


            //slides manual
            if((slides.getState()!=Slides.SlideState.GROUND||slidesOverride.isDown())&&!slidesMoving&&Math.abs(gamepad2.left_stick_y)>0.3)
            {
                slides.setOperationState(Module.OperationState.MANUAL);

                if(slidestimer.milliseconds()>150)
                {
                    double newTarget=slides.getTargetPosition() + (30*Math.signum(gamepad2.left_stick_y)*-1);
//                    if(newTarget<300)
//                    {
//                        newTarget=300;
//                    }
                    slides.manualChange(newTarget);

                    slidestimer.reset();
                }
            }

            //intake power
            intake.manualChange(-gamepad2.right_stick_y);

//            if(deposit.firstPixel.getDistance(DistanceUnit.MM)<10&&deposit.secondPixel.getDistance(DistanceUnit.MM)<10)
//            {
////                gamepad1.runRumbleEffect(customRumbleEffect1);
////                gamepad2.runRumbleEffect(customRumbleEffect1);
//            }
//            telemetry.addData("1st pixel distance", deposit.firstPixel.getDistance(DistanceUnit.MM));
//            telemetry.addData("2nd pixel distance", deposit.secondPixel.getDistance(DistanceUnit.MM));

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
                double y = gamepad1.left_stick_x * ninja;
                double rx = -gamepad1.right_stick_x * ninja;

                robot.setLocalDrivePowers(new Pose2d(x, y, rx));
            }

            //dt strafing
            if(strafeLeft.wasJustPressed()&&!robot.isBusy())
            {
                Trajectory strafeLeft=robot.trajectoryBuilder(robot.getPoseEstimate())
                        .strafeLeft(3)
                        .build();

                robot.followTrajectoryAsync(strafeLeft);
            }
            else if(strafeRight.wasJustPressed()&&!robot.isBusy())
            {
                Trajectory strafeRight=robot.trajectoryBuilder(robot.getPoseEstimate())
                        .strafeRight(3)
                        .build();

                robot.followTrajectoryAsync(strafeRight);
            }

            //cancel strafe in case borken
            if(gamepad1.x)
            {
                //robot cancel trajectory function(oop I forgor)
            }

            if(gamepad1.y)
            {
                Context.statusError="Broken";
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

            if(slideReset.isDown())
            {
                slides.setMotorRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }
            else if(slides.getMotorRunMode()== DcMotor.RunMode.STOP_AND_RESET_ENCODER)
            {
                slides.setMotorRunMode(DcMotor.RunMode.RUN_TO_POSITION);
            }


            driverLoopCount++;
        }
    }

    public void onStart()
    {
        driverLoopCount=0;
        hardwareLoopCount=0;
        opmodetimer.reset();
    }

    @Override
    public void initialize()
    {
        this.setLoopTimes(1);

        hang=hardwareMap.get(DcMotor.class, "hang");

        robot=new Robot(this);
        builder=new TaskListBuilder(this);
        scheduler=new TaskScheduler();

        //deposit=robot.deposit;
        intake=robot.intake;
        slides=robot.slides;
        droneLauncher=robot.droneLauncher;

        intake.init();
        intake.setState(Intake.OldPositionState.TELE);
        deposit.init();
        droneLauncher.init();
        intake.setOperationState(Module.OperationState.MANUAL);

        slidesMoving=false;
        slidestimer=new ElapsedTime();

        opmodetimer=new ElapsedTime();

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
                slideUpBase= new ToggleButtonReader(g2, GamepadKeys.Button.DPAD_LEFT),
                slideUpRow1= new ToggleButtonReader(g2, GamepadKeys.Button.DPAD_UP),
                slideUpRow2=new ToggleButtonReader(g2, GamepadKeys.Button.DPAD_RIGHT),
                slideDown= new ToggleButtonReader(g2, GamepadKeys.Button.DPAD_DOWN),
                pusher1=new ToggleButtonReader(g1, GamepadKeys.Button.RIGHT_BUMPER),
                pusher2=new ToggleButtonReader(g2, GamepadKeys.Button.X),
                intake1=new ToggleButtonReader(g2, GamepadKeys.Button.A),
                intake2=new ToggleButtonReader(g2, GamepadKeys.Button.RIGHT_BUMPER),
                intake3=new ToggleButtonReader(g2, GamepadKeys.Button.LEFT_BUMPER),
                intakeFive=new ToggleButtonReader(g1, GamepadKeys.Button.Y),
                intakeFour=new ToggleButtonReader(g1, GamepadKeys.Button.B),
                intakeThree=new ToggleButtonReader(g1, GamepadKeys.Button.X),
                intakeTwo=new ToggleButtonReader(g1, GamepadKeys.Button.A),
                strafeLeft=new ToggleButtonReader(g1, GamepadKeys.Button.DPAD_RIGHT),
                strafeRight=new ToggleButtonReader(g1, GamepadKeys.Button.DPAD_LEFT),
                droneButton1=new ToggleButtonReader(g1, GamepadKeys.Button.DPAD_DOWN),
                slideReset=new ToggleButtonReader(g2, GamepadKeys.Button.B),
                slidesOverride=new ToggleButtonReader(g2, GamepadKeys.Button.Y)

        };

     //   intake.setState(intakepositions[0]);
        intakepositions=new Intake.OldPositionState[]
                {
                        Intake.OldPositionState.TELE, Intake.OldPositionState.TWO,
                        Intake.OldPositionState.THREE, Intake.OldPositionState.FOUR, Intake.OldPositionState.FIVE,
                        Intake.OldPositionState.HIGH
                };

        slideupbase=builder.createNew()
                //.executeCode(()->slidesMoving=true)
                .moduleAction(deposit, DepositOld.WristState.CRADLE)
                .delay(200)
                .moduleAction(deposit, DepositOld.RotationState.DEPOSIT_MID)
//                .moduleAction(deposit, Deposit.PusherState.EXTENDED)
                .delay(250)
                .moduleAction(slides, Slides.SlideState.RAISED)
                .await(()->slides.getStatus()==Module.Status.IDLE)
                .executeCode(()->slidesMoving=false)
                .moduleAction(deposit, DepositOld.WristState.DEPOSIT)
                .delay(100)
                .moduleAction(deposit, DepositOld.RotationState.DEPOSIT_HIGH)
                .build();

        slideuphalf=builder.createNew()
                //.executeCode(()->slidesMoving=true)
                .moduleAction(slides, Slides.SlideState.HALF)
                .await(()->slides.currentPosition()>200)
                .build();

        slideup1=builder.createNew()
                //.executeCode(()->slidesMoving=true)
                .moduleAction(deposit, DepositOld.WristState.CRADLE)
                .delay(300)
                .moduleAction(deposit, DepositOld.RotationState.DEPOSIT_MID)
//                .moduleAction(deposit, Deposit.PusherState.EXTENDED)
                .delay(250)
                .moduleAction(slides, Slides.SlideState.ROW1)
                .await(()->slides.getStatus()==Module.Status.IDLE)
                .executeCode(()->slidesMoving=false)
                .moduleAction(deposit, DepositOld.WristState.DEPOSIT)
                .delay(100)
                .moduleAction(deposit, DepositOld.RotationState.DEPOSIT_HIGH)
                .build();

        slideup2=builder.createNew()
                //.executeCode(()->slidesMoving=true)
                .moduleAction(deposit, DepositOld.WristState.CRADLE)
                .delay(300)
                .moduleAction(deposit, DepositOld.RotationState.DEPOSIT_MID)
//                .moduleAction(deposit, Deposit.PusherState.EXTENDED)
                .delay(250)
                .moduleAction(slides, Slides.SlideState.ROW2)
                .await(()->slides.getStatus()==Module.Status.IDLE)
                .executeCode(()->slidesMoving=false)
                .moduleAction(deposit, DepositOld.WristState.DEPOSIT)
                .delay(100)
                .moduleAction(deposit, DepositOld.RotationState.DEPOSIT_HIGH)
                .build();

        slideupbaseraised=builder.createNew()
                .moduleAction(slides, Slides.SlideState.RAISED)
                .awaitPreviousModuleActionCompletion()
                .executeCode(()->slidesMoving=false)
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
                .moduleAction(deposit, DepositOld.RotationState.DEPOSIT_MID)
                .delay(300)
                .moduleAction(slides, Slides.SlideState.GROUND)
                .moduleAction(deposit, DepositOld.PusherState.IN)
                .await(()->slides.currentPosition()<100)
                .moduleAction(deposit, DepositOld.RotationState.TRANSFER)
                .moduleAction(deposit, DepositOld.WristState.TRANSFER)
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
        hardwareLoopCount++;

        robot.primaryLoop();
        robot.update();

        Context.tel.addData("Slide Moving", slidesMoving);
        Context.tel.addData("Driver Loop Times", opmodetimer.milliseconds()/driverLoopCount);
        Context.tel.addData("Hardware Loop Times", opmodetimer.milliseconds()/hardwareLoopCount);
        Context.tel.addData("HangPow", hang.getPower());
        Context.tel.addData("HangPos", hang.getCurrentPosition());
        Context.tel.addData("Drone pos", droneLauncher.getCurrentPosition());
        Context.tel.addData("CS1",deposit.firstPixel.getDistance(DistanceUnit.MM));
        //Context.tel.addData("Robot Current", robot.getCurrent());
    }

}

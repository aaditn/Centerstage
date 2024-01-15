package org.firstinspires.ftc.teamcode.modules.moduleTesting;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
@Config
public class IntakeTest extends EnhancedOpMode
{
    Intake intake;
    @Override
    public void linearOpMode()
    {
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

        intake=new Intake(hardwareMap);
        intake.init();
        intake.setOperationState(Module.OperationState.MANUAL);
    }

    @Override
    public void initLoop()
    {
        intake.updateLoop();
        intake.writeLoop();
    }

    public void onStart()
    {
        //do something idk(maybe take signal sleeve snapshot or wtv)
    }

    @Override
    public void primaryLoop()
    {
        intake.updateLoop();

        intake.writeLoop();


       /* if(gamepad2.a)
        {
            intake.setState(Intake.PositionState.TELE);
        }
        else if(gamepad2.b)
        {
            intake.setState(Intake.positionState.HIGH);
        }*/
        if(gamepad2.a)
        {
            intake.setState(Intake.PositionState.FIVE);
        }
        else if(gamepad2.b)
        {
            intake.setState(Intake.PositionState.FOUR);
        }
        else if(gamepad2.x)
        {
            intake.setState(Intake.PositionState.THREE);
        }
        else if(gamepad2.y)
        {
            intake.setState(Intake.PositionState.TWO);
        }
        //intake.setPowerManual(gamepad2.right_stick_y);

    }

    @Override
    public void onEnd()
    {

    }
}
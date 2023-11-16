package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;

public class Intake extends Module {
    DcMotorEx intake;
    Servo angler1, angler2;
    double threshold = 0.02; // you might need one because power is inconsistent
    public static enum PowerState implements ModuleState
    {
        INTAKE(0.9), EXTAKE(-0.9), OFF(0), LOW(-0.5);
        double power;
        PowerState(double power)
        {
            this.power=power;
        }

        @Override
        public double getOutput(int... index)
        {
            return power;
        }
    }

    public static enum PositionState implements ModuleState
    {
        TELE(0.03), HIGH(0.52), FIVE(0), FOUR(0), THREE(0), TWO(0), ONE(0);

        double position;

        PositionState(double position)
        {
            this.position=position;
        }

        @Override
        public double getOutput(int... index)
        {
            return position;
        }
    }



    double currentPower;
    double currentPosition;

    PowerState pwstate;
    PositionState poState;

    public Intake(HardwareMap hardwareMap)
    {
        super(false);
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        angler1 = hardwareMap.get(Servo.class, "intake1");
        angler2 = hardwareMap.get(Servo.class, "intake2");
        angler2.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void manualChange(double power)
    {
        super.manualChange(power);
        currentPower=power;
    }

    @Override
    protected void write()
    {
        intake.setPower(currentPower);
        angler1.setPosition(currentPosition);
        angler2.setPosition(currentPosition);
    }

    @Override
    protected void internalUpdate()
    {
        currentPower=getState(PowerState.class).getOutput();
        currentPosition=getState(PositionState.class).getOutput();
    }

    @Override
    protected void internalUpdateManual()
    {
        currentPosition=getState(PositionState.class).getOutput();
    }


    @Override
    protected void initInternalStates()
    {
        poState= PositionState.HIGH;
        pwstate= PowerState.OFF;
        setInternalStates(poState, pwstate);
    }

    @Override
    protected void updateInternalStatus() {
        status=Status.IDLE;
    }
}

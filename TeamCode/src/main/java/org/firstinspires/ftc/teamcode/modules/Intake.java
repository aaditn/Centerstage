package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;

public class Intake extends Module {
    DcMotorEx intake;
    Servo angler1, angler2;
    double threshold = 0.02; // you might need one because power is inconsistent
    public enum powerState implements ModuleState
    {
        INTAKE(0.9), EXTAKE(-0.9), OFF(0), LOW(-0.3);

        double power;
        powerState(double power)
        {
            this.power=power;
        }

        @Override
        public double getOutput(int... index)
        {
            return power;
        }
    }

    public enum positionState implements ModuleState
    {
        TELE(0.03), HIGH(0.52), FIVE(0), FOUR(0), THREE(0), TWO(0), ONE(0);

        double position;

        positionState(double position)
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

    powerState pwstate;
    positionState poState;

    public Intake(HardwareMap hardwareMap)
    {
        super(false);
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        angler1 = hardwareMap.get(Servo.class, "angler1");
        angler2 = hardwareMap.get(Servo.class, "angler2");
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
        currentPower=getState(powerState.class).getOutput();
        currentPosition=getState(positionState.class).getOutput();
    }

    @Override
    protected void initInternalStates()
    {
        poState=positionState.HIGH;
        pwstate=powerState.OFF;
        setInternalStates(poState, pwstate);
    }

    @Override
    protected void updateInternalStatus() {
        status=Status.IDLE;
    }
}

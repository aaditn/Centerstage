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
    public enum State implements ModuleState
    {
        MOTOR_OFF(0), MOTOR_ON(0.7), ANGLE_LOW(0.2), ANGLE_HIGH(0.7);

        double power;
        double position;
        State(double position)
        {
            this.power=power;
            this.position = position;
        }

        @Override
        public double getOutput(int... index)
        {
            return power;
        }
    }

    double currentPower;
    double currentPosition;

  State state;

    public Intake(HardwareMap hardwareMap, Telemetry tel)
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
        currentPower = state.power;
        currentPosition = state.position;
    }

    @Override
    protected void initInternalStates()
    {
        setInternalStates(state);
    }

    @Override
    protected void updateInternalStatus() {
        if (intake.getPower() <= State.MOTOR_OFF.power + threshold || intake.getPower() >= State.MOTOR_ON.power - threshold) {
            status = Status.IDLE;
        } else {
            status = Status.TRANSITIONING;
        }

    }
}

package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;

public class Intake extends Module {
    DcMotorEx intake;
    Servo anglerLeft, anglerRight, sweeperLeft, sweeperRight;
    CRServo conveyorLeft, conveyorRight;
    public static boolean telemetryToggle=true;

    public enum PowerState implements ModuleState
    {
        INTAKE_AUTO(1.0), INTAKE(0.9), INTAKE_LOW(0.7), EXTAKE(-0.9), OFF(0), LOW(-0.3);
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
    public enum PositionState implements ModuleState
    {
        RAISED(0.05), MID (.2),DOWN(0.31);
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
    public enum ConveyorState implements  ModuleState
    {
        INTAKE(1), EXTAKE(-1), OFF(0);
        double power;
        ConveyorState(double power)
        {
            this.power=power;
        }
        @Override
        public double getOutput(int... index)
        {
            return power;
        }
    }

    public enum SweeperState implements ModuleState
    {
        ZERO(0.035), INIT(0.09), ONE_SWEEP(0.15), TWO_SWEEP(0.265), THREE_SWEEP(0.38),FOUR_SWEEP(.495);
        double position;
        SweeperState(double position)
        {
            this.position=position;
        }
        @Override
        public double getOutput(int... index) {
            return position;
        }
    }



    double currentPower, currentPosition, conveyorPower, sweeperPos;

    PowerState pwstate;
    PositionState poState;
    ConveyorState cstate;
    SweeperState sstate;

    public Intake(HardwareMap hardwareMap)
    {
        super(false, telemetryToggle);
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        conveyorLeft = hardwareMap.get(CRServo.class, "conveyorLeft");
        conveyorRight = hardwareMap.get(CRServo.class, "conveyorRight");
        conveyorRight.setDirection(DcMotorSimple.Direction.REVERSE);

        sweeperLeft = hardwareMap.get(Servo.class, "sweeperLeft");
        sweeperRight = hardwareMap.get(Servo.class, "sweeperRight");
        sweeperRight.setDirection(Servo.Direction.REVERSE);

        anglerLeft = hardwareMap.get(Servo.class, "anglerLeft");
        anglerRight = hardwareMap.get(Servo.class, "anglerRight");
        anglerRight.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void manualChange(double power)
    {
        super.manualChange(power);
        currentPower=power;
        conveyorPower=Math.signum(power);
    }

    @Override
    protected void write()
    {
        intake.setPower(currentPower);

        anglerLeft.setPosition(currentPosition);
        anglerRight.setPosition(currentPosition);

        sweeperLeft.setPosition(sweeperPos);
        sweeperRight.setPosition(sweeperPos);

        conveyorLeft.setPower(conveyorPower);
        conveyorRight.setPower(conveyorPower);
    }

    @Override
    protected void internalUpdate()
    {

        currentPower=getState(PowerState.class).getOutput();
        conveyorPower =getState(ConveyorState.class).getOutput();
        sweeperPos=getState(SweeperState.class).getOutput();
        currentPosition=getState(PositionState.class).getOutput();
    }

    @Override
    protected void internalUpdateManual()
    {
        currentPosition=getState(PositionState.class).getOutput();
        sweeperPos=getState(SweeperState.class).getOutput();
    }


    @Override
    protected void initInternalStates()
    {
        poState=PositionState.RAISED;
        pwstate=PowerState.OFF;
        cstate=ConveyorState.OFF;
        sstate=SweeperState.ZERO;
        setInternalStates(poState, pwstate, cstate, sstate);
    }

    @Override
    protected void updateInternalStatus()
    {
        status=Status.IDLE;
    }

    public enum OldPositionState implements ModuleState
    {
        HOLD_AUTO (0), TELE(0.03), HIGH(0.52), FIVE(0.19), FOUR(0.18), THREE(0.13), TWO(0.08), ONE(0.03), PURP(0.04);

        double position;

        OldPositionState(double position)
        {
            this.position=position;
        }

        @Override
        public double getOutput(int... index)
        {
            return position;
        }
    }
}

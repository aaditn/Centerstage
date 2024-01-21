package org.firstinspires.ftc.teamcode.modules;

import static org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.MM;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;
@Config
public class Intake extends Module {
    DcMotorEx intake;
    Servo anglerLeft, anglerRight, sweeperLeft, sweeperRight;
    CRServo conveyorLeft, conveyorRight;
    ColorRangeSensor cs1, cs2, cs3;
    public static boolean telemetryToggle=true;

    public enum PowerState implements ModuleState
    {
        INTAKE_AUTO, INTAKE, INTAKE_LOW, EXTAKE, OFF, LOW;
    }
    public static double POWER_INTAKE_AUTO=1, POWER_INTAKE=0.9, POWER_INTAKE_LOW=0.7, POWER_EXTAKE=-0.9, POWER_OFF=0, POWER_LOW=-0.3;
    double[] powerValues={POWER_INTAKE_AUTO, POWER_INTAKE, POWER_INTAKE_LOW, POWER_EXTAKE, POWER_OFF, POWER_LOW};


    public enum PositionState implements ModuleState
    {
        RAISED, MID,DOWN;
    }
    public static double POSITION_RAISED=0.05, POSITION_MID=0.2, POSITION_DOWN=0.31;
    double[] positionValues={POSITION_RAISED, POSITION_MID, POSITION_DOWN};


    public enum ConveyorState implements  ModuleState
    {
        INTAKE, EXTAKE, OFF;
    }
    public static double CONVEYOR_INTAKE=1, CONVEYOR_EXTAKE=-1, CONVEYOR_OFF=0;
    double[] conveyorValues={CONVEYOR_INTAKE, CONVEYOR_EXTAKE, CONVEYOR_OFF};


    public enum SweeperState implements ModuleState
    {
        ZERO, INIT, ONE_SWEEP, TWO_SWEEP, THREE_SWEEP,FOUR_SWEEP;
    }
    public static double SWEEPER_ZERO=0.035, SWEEPER_INIT=0.09, SWEEPER_ONE=0.15, SWEEPER_TWO=0.265, SWEEPER_THREE=0.38, SWEEPER_FOUR=0.495;
    double[] sweeperValues={SWEEPER_ZERO, SWEEPER_INIT, SWEEPER_ONE, SWEEPER_TWO, SWEEPER_THREE, SWEEPER_FOUR};


    public enum ColorSensorState
    {
        ZERO, ONE, TWO
    }

    double currentPower, currentPosition, conveyorPower, sweeperPos;
    int s1Alpha = 1500, s1Dist = 75, s2Alpha = 1500, s2Dist = 75;

    PowerState pwstate;
    PositionState poState;
    ConveyorState cstate;
    SweeperState sstate;
    ColorSensorState csstate;


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

        cs1 = hardwareMap.get(ColorRangeSensor.class, "cs1");
        cs2 = hardwareMap.get(ColorRangeSensor.class, "cs2");
        cs3 = hardwareMap.get(ColorRangeSensor.class, "cs3");
    }

    public void colorSensorUpdate()
    {
        //TODO(this is just copy pasted lmao)

        boolean pixel1 = cs1.getDistance(MM) < s1Dist && cs1.alpha() > s1Alpha;
        boolean pixel2 = cs2.getDistance(MM) < s2Dist && cs2.alpha() > s2Alpha;

        if(pixel1&&pixel2)
            csstate=ColorSensorState.TWO;
        else if(pixel1||pixel2)
            csstate=ColorSensorState.ONE;
        else
            csstate=ColorSensorState.ZERO;
    }

    public ColorSensorState getColorSensorState()
    {
        return csstate;
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
        currentPower=converter.getOutput(getState(PowerState.class));
        conveyorPower=converter.getOutput(getState(ConveyorState.class));
        sweeperPos=converter.getOutput(getState(SweeperState.class));
        currentPosition=converter.getOutput(getState(PositionState.class));
    }

    @Override
    protected void internalUpdateManual()
    {
        sweeperPos=converter.getOutput(getState(SweeperState.class));
        currentPosition=converter.getOutput(getState(PositionState.class));
    }


    @Override
    protected void initInternalStates()
    {
        poState=PositionState.RAISED;
        pwstate=PowerState.OFF;
        cstate=ConveyorState.OFF;
        sstate=SweeperState.ZERO;
        csstate=ColorSensorState.ZERO;
        setInternalStates(poState, pwstate, cstate, sstate);
    }

    @Override
    protected void updateInternalStatus()
    {
        status=Status.IDLE;
    }

    @Override
    protected void mapToKey()
    {
        converter.add(PowerState.values(), powerValues);
        converter.add(PositionState.values(), positionValues);
        converter.add(ConveyorState.values(), conveyorValues);
        converter.add(SweeperState.values(), sweeperValues);
    }

    public enum OldPositionState implements ModuleState
    {
        HOLD_AUTO (0), TELE(0.03), HIGH(0.52), FIVE(0.19), FOUR(0.18), THREE(0.13), TWO(0.08), ONE(0.03), PURP(0.04);

        double position;

        OldPositionState(double position)
        {
            this.position = position;
        }
    }
}

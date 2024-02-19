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
    Servo  anglerRight, sweeperLeft, sweeperRight;
    CRServo conveyorLeft, conveyorRight;
    ColorRangeSensor cs1, cs2, cs3;
    public static boolean telemetryToggle=false;

    public enum PowerState implements ModuleState
    {
        INTAKE_AUTO, INTAKE, EXTAKE, OFF;
    }
    public static double POWER_INTAKE_AUTO=-.8, POWER_INTAKE=-0.9, POWER_EXTAKE=0.9, POWER_OFF=0;
    public static double[] powerValues={POWER_INTAKE_AUTO, POWER_INTAKE, POWER_EXTAKE, POWER_OFF};


    public enum PositionState implements ModuleState
    {
        RAISED, MID, DOWN, AUTO;
    }
    public static double POSITION_RAISED=0,  POSITION_MID=0.13, POSITION_DOWN=0.27, AUTO=POSITION_DOWN;
    public static double[] positionValues={POSITION_RAISED, POSITION_MID, POSITION_DOWN, AUTO};


    public enum ConveyorState implements  ModuleState
    {
        INTAKE, EXTAKE, OFF;
    }
    public static double CONVEYOR_INTAKE=-1, CONVEYOR_EXTAKE=1, CONVEYOR_OFF=0;
    public static double[] conveyorValues={CONVEYOR_INTAKE, CONVEYOR_EXTAKE, CONVEYOR_OFF};


    public enum SweeperState implements ModuleState
    {
        ZERO, INIT, ONE_SWEEP, TWO_SWEEP, THREE_SWEEP,FOUR_SWEEP,FIVE_SWEEP,SIX_SWEEP,SEVEN_SWEEP,EIGHT_SWEEP;
    }
    public static double SWEEPER_ZERO=0.1, SWEEPER_INIT=0.1625, SWEEPER_ONE=0.215, SWEEPER_TWO=0.33,
            SWEEPER_THREE=0.445, SWEEPER_FOUR=0.56,SWEEPER_FIVE=.655,SWEEPER_SIX=.755,SWEEPER_SEVEN = .84,SWEEPER_EIGHT=.955;
    public static double[] sweeperValues={SWEEPER_ZERO, SWEEPER_INIT, SWEEPER_ONE, SWEEPER_TWO, SWEEPER_THREE,
            SWEEPER_FOUR,SWEEPER_FIVE,SWEEPER_SIX,SWEEPER_SEVEN,SWEEPER_EIGHT};


    public enum ColorSensorState
    {
        ZERO, ONE, TWO
    }

    double currentPower, currentPosition, conveyorLeftPower,conveyorRightPower, conveyorPower, sweeperPos;
    int s1Alpha = 1500, s1Dist = 75, s2Alpha = 1500, s2Dist = 75;

    PowerState pwstate;
    PositionState poState;
    ConveyorState cstate;
    SweeperState sstate;
    ColorSensorState csstate;
    public static double sweeperOffset=0.06;


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
        sweeperLeft.setDirection(Servo.Direction.REVERSE);

        anglerRight = hardwareMap.get(Servo.class, "anglerRight");
        anglerRight.setDirection(Servo.Direction.REVERSE);

//        cs1 = hardwareMap.get(ColorRangeSensor.class, "cs1");
//        cs2 = hardwareMap.get(ColorRangeSensor.class, "cs2");
//        cs3 = hardwareMap.get(ColorRangeSensor.class, "cs3");
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
    public void closeColorSensors()
    {
        cs1.close();
        cs2.close();
        cs3.close();
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
        conveyorLeftPower=0;
        conveyorRightPower=0;
    }

    public void manualChange(double power,double conveyerP)
    {
        super.manualChange(Math.signum(power));
        currentPower=Math.signum(power);
        conveyorPower=Math.signum(conveyerP);
        conveyorLeftPower = 0;
        conveyorRightPower = 0;
    }
    public void manualChange(double power,double conveyerP, boolean disable)
    {
        super.manualChange(Math.signum(power));
        currentPower=Math.signum(power);
        conveyorPower=Math.signum(conveyerP);
        conveyorLeftPower = 0;
        conveyorRightPower = 1.5;
    }

    @Override
    protected void write()
    {
        intake.setPower(currentPower);

        anglerRight.setPosition(currentPosition);

        sweeperLeft.setPosition(sweeperPos);
        sweeperRight.setPosition(sweeperPos+sweeperOffset);

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

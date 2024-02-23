package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;
@Config
public class Intake extends Module {
    DcMotorEx intake;
    Servo  anglerRight, sweeperLeft, sweeperRight;
    CRServo conveyorLeft, conveyorRight;
    ColorRangeSensor cs1;
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
    public static double POSITION_RAISED=0,  POSITION_MID=0.13, POSITION_DOWN=0.28, AUTO=POSITION_DOWN;
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
    public static double SWEEPER_ZERO=0.1, SWEEPER_INIT=.1625, SWEEPER_ONE=0.215, SWEEPER_TWO=0.33,
            SWEEPER_THREE=0.445, SWEEPER_FOUR=0.56,SWEEPER_FIVE=.655,SWEEPER_SIX=.755,SWEEPER_SEVEN = .84,SWEEPER_EIGHT=.955;
    public static double[] sweeperValues={SWEEPER_ZERO, SWEEPER_INIT, SWEEPER_ONE, SWEEPER_TWO, SWEEPER_THREE,
            SWEEPER_FOUR,SWEEPER_FIVE,SWEEPER_SIX,SWEEPER_SEVEN,SWEEPER_EIGHT};


    double currentPower, currentPosition, conveyorLeftPower,conveyorRightPower, conveyorPower, sweeperPos;
    int s1Alpha = 1000, s1Dist = 75, s2Alpha = 1500, s2Dist = 75;
public static double initOffset = 0.06;
    PowerState pwstate;
    PositionState poState;
    ConveyorState cstate;
    SweeperState sstate;
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

        cs1 = hardwareMap.get(ColorRangeSensor.class, "cs1");

//        cs1 = hardwareMap.get(ColorRangeSensor.class, "cs1");
//        cs2 = hardwareMap.get(ColorRangeSensor.class, "cs2");
//        cs3 = hardwareMap.get(ColorRangeSensor.class, "cs3");
    }



    public boolean pixelsPresent()
    {
        double colorSensorAlpha=cs1.alpha();
        RobotLog.e("cs alpha: "+ colorSensorAlpha);
        if(colorSensorAlpha > s1Alpha)
        {
            return true;
        }
        return false;
    }

    public boolean pixelsPresentBlocking()
    {
        double presentCounter=0;
        double absentCounter=0;

        for(int i=0; i<10; i++)
        {
            if(pixelsPresent())
            {
                presentCounter++;
            }
            else
            {
                absentCounter++;
            }
        }
        RobotLog.e("Present Counter " +presentCounter);
        RobotLog.e("Absent Counter "+ absentCounter);

        if(presentCounter>absentCounter)
            return true;
        else
            return false;
    }

    public ColorRangeSensor getCs()
    {
        return cs1;
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
if(getState(SweeperState.class).equals(SweeperState.INIT)){

    sweeperLeft.setPosition(sweeperPos+initOffset);
    sweeperRight.setPosition(sweeperPos+sweeperOffset-initOffset);
}
else {
    sweeperLeft.setPosition(sweeperPos);
    sweeperRight.setPosition(sweeperPos + sweeperOffset);
}

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

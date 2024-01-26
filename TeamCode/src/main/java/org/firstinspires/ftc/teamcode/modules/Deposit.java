package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;
@Config
public class Deposit extends Module
{
    Servo leftArm, rightArm, wrist, rotatewrist, claw;

    public static boolean telemetryToggle;

    public enum FlipState implements ModuleState
    {
        TRANSFER, DEPOSIT,PRIMED,LOAD;
    }
    public static double FLIP_TRANSFER =0.92, FLIP_DEPOSIT=0.22, FLIP_PRIMED=0.91,FLIP_LOAD=.5;
    public static double[] flipValues={FLIP_TRANSFER, FLIP_DEPOSIT, FLIP_PRIMED,FLIP_LOAD};


    public enum WristState implements ModuleState
    {
        TRANSFER, DEPOSIT, HOVER, TELESCOPE;
    }
    public static double WRIST_TRANSFER=0.67, WRIST_DEPOSIT=0.25, WRIST_HOVER=0.53, WRIST_SCOPE=0.71;
    public static double[] wristValues={WRIST_TRANSFER, WRIST_DEPOSIT, WRIST_HOVER, WRIST_SCOPE};


    public enum ClawState implements ModuleState
    {
        OPEN, CLOSED_AUTO, CLOSED1, CLOSED2,PRIMED, HALF;
    }
    public static double CLAW_OPEN=0.85, CLAW_CLOSED_1=0.7, CLAW_CLOSED_2=0.6, CLOSED_AUTO=0.74, CLAW_PRIMED=.75, CLAW_HALF = .68;
    public static double[] clawValues={CLAW_OPEN, CLOSED_AUTO, CLAW_CLOSED_1, CLAW_CLOSED_2, CLAW_PRIMED, CLAW_HALF};


    public enum RotateState implements ModuleState {
        ZERO, NINETY, ONE_EIGHTY,TWO_SEVENTY;
    }
    public static double ROTATE_0=0.5475, ROTATE_90=0.6075, ROTATE_180=0.4345, ROTATE_270=0.49;
    public static double[] rotateValues={ROTATE_0, ROTATE_90, ROTATE_180, ROTATE_270};


    double flip1Pos, flip2Pos, clawPos, wristPos, rotatePos;
    FlipState fstate;
    WristState wstate;
    RotateState wrstate;
    ClawState cstate;

    public Deposit(HardwareMap hardwareMap) {
        super(false, telemetryToggle);
        leftArm=hardwareMap.get(Servo.class, "leftArm");
        rightArm=hardwareMap.get(Servo.class, "rightArm");
        rightArm.setDirection(Servo.Direction.REVERSE);

        wrist=hardwareMap.get(Servo.class, "wrist");
        rotatewrist=hardwareMap.get(Servo.class, "rotatewrist");
        claw=hardwareMap.get(Servo.class, "claw");
    }

    @Override
    protected void write()
    {
        leftArm.setPosition(flip1Pos);
        rightArm.setPosition(flip2Pos);
        wrist.setPosition(wristPos);
        claw.setPosition(clawPos);
        rotatewrist.setPosition(rotatePos);
    }

    @Override
    protected void internalUpdate()
    {
        flip1Pos=converter.getOutput(getState(FlipState.class));
        flip2Pos=converter.getOutput(getState(FlipState.class));
        wristPos=converter.getOutput(getState(WristState.class));
        clawPos=converter.getOutput(getState(ClawState.class));
        rotatePos=converter.getOutput(getState(RotateState.class));
    }

    @Override
    protected void initInternalStates() {
        fstate=FlipState.PRIMED;
        wstate=WristState.TELESCOPE;
        wrstate= RotateState.ONE_EIGHTY;
        cstate=ClawState.OPEN;
        setInternalStates(fstate, wstate, wrstate, cstate);
    }

    @Override
    protected void updateInternalStatus()
    {
        //TODO
    }

    @Override
    protected void mapToKey()
    {
        converter.add(FlipState.values(), flipValues);
        converter.add(WristState.values(), wristValues);
        converter.add(ClawState.values(), clawValues);
        converter.add(RotateState.values(), rotateValues);
    }
}

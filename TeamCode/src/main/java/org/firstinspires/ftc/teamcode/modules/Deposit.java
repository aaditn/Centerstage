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
    public static double WRIST_TRANSFER=0.67, WRIST_DEPOSIT=0.3, WRIST_HOVER=0.53, WRIST_SCOPE=0.71;
    public static double[] wristValues={WRIST_TRANSFER, WRIST_DEPOSIT, WRIST_HOVER, WRIST_SCOPE};


    public enum ClawState implements ModuleState
    {
        OPEN, CLOSED_AUTO, CLOSED1, CLOSED2,PRIMED, HALF, CLOSED_EDGE;
    }
    public static double CLAW_OPEN=0.72, CLAW_CLOSED_1=0.5, CLAW_CLOSED_2=0.16, CLOSED_AUTO=0.74, CLAW_PRIMED=0.72, CLAW_HALF = .68,CLOSED_EDGE = .7;
    public static double[] clawValues={CLAW_OPEN, CLOSED_AUTO, CLAW_CLOSED_1, CLAW_CLOSED_2, CLAW_PRIMED, CLAW_HALF,CLOSED_EDGE};


    public enum RotateState implements ModuleState {
        ZERO, PLUS_FOURTY_FIVE, PLUS_NINETY, PLUS_ONE_THREE_FIVE, PLUS_ONE_EIGHTY, PLUS_TWO_TWO_FIVE, PLUS_TWO_SEVENTY,
        MINUS_FOURTY_FIVE, MINUS_NINETY, MINUS_ONE_THREE_FIVE, MINUS_ONE_EIGHTY, MINUS_TWO_TWO_FIVE, MINUS_TWO_SEVENTY
    }
    public static double ROTATE_0=0.4345, ROTATE_PLUS_45=0.4623, ROTATE_PLUS_90=0.49, ROTATE_PLUS_135=0.5210, ROTATE_PLUS_180=0.5475, ROTATE_PLUS_225=0.5775, ROTATE_PLUS_270=0.6075;
    public static double ROTATE_MINUS_45=0.4067, ROTATE_MINUS_90=0.3789, ROTATE_MINUS_135=0.3511, ROTATE_MINUS_180=0.3233, ROTATE_MINUS_225=0.2955, ROTATE_MINUS_270=0.2677;

    public static double[] rotateValues={ROTATE_0, ROTATE_PLUS_45, ROTATE_PLUS_90, ROTATE_PLUS_135, ROTATE_PLUS_180, ROTATE_PLUS_225, ROTATE_PLUS_270,
    ROTATE_MINUS_45, ROTATE_MINUS_90, ROTATE_MINUS_135, ROTATE_MINUS_180, ROTATE_MINUS_225, ROTATE_MINUS_270};


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
        wrstate= RotateState.ZERO;
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

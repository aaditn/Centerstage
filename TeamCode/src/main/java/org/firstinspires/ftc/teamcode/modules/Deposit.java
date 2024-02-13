package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;
import org.firstinspires.ftc.teamcode.util.Tel;

@Config
public class Deposit extends Module
{
    Servo leftArm, rightArm, wrist, rotatewrist, claw, extension;

    public static boolean telemetryToggle;
    public static double transferOffset = 0.004;

    public static double depositOffset = -0.008;

    public enum ExtensionState implements ModuleState
    {
        TRANSFER, DEPOSIT, TRANSFER_PARTIAL;
    }
    public static double EXT_TRANSFER = .1, EXT_DEPOSIT = .5, EXT_TRANSFER_PARTIAL=0.05;
    public static double[] extValues = {EXT_TRANSFER,EXT_DEPOSIT, EXT_TRANSFER_PARTIAL};


    public enum FlipState implements ModuleState
    {
        TRANSFER, DEPOSIT,PRIMED, LEFT,RIGHT, DOWN;
    }
    public static double ROTATE_MOD = 0.05;
    public static double FLIP_TRANSFER =0.4875, FLIP_DEPOSIT=0.55,FLIP_PRIMED = FLIP_TRANSFER,FLIP_LEFT = FLIP_DEPOSIT,FLIP_RIGHT = FLIP_DEPOSIT,FLIP_DOWN=0.63;
    public static double[] flipValues={FLIP_TRANSFER, FLIP_DEPOSIT,FLIP_PRIMED,FLIP_LEFT,FLIP_RIGHT,FLIP_DOWN};


    public enum WristState implements ModuleState
    {
        TRANSFER, DEPOSIT, HOVER, TELESCOPE, PARTIAL, PARTIAL2;
    }
    public static double WRIST_TRANSFER=0.94, WRIST_DEPOSIT=0.2, WRIST_HOVER=0.5, WRIST_SCOPE=0.94, WRIST_PARTIAL=0.84, WRIST_PARTIAL2=0.88;
    public static double[] wristValues={WRIST_TRANSFER, WRIST_DEPOSIT, WRIST_HOVER, WRIST_SCOPE, WRIST_PARTIAL, WRIST_PARTIAL2};


    public enum ClawState implements ModuleState
    {
        OPEN, CLOSED_AUTO, CLOSED1, CLOSED2,PRIMED, HALF, CLOSED_EDGE;
    }
    public static double CLAW_OPEN=.70, CLAW_CLOSED_1=0.5+.3, CLAW_CLOSED_2=0.16+.2, CLOSED_AUTO=CLAW_CLOSED_2, CLAW_PRIMED=0.70, CLAW_HALF = .68+.3,CLOSED_EDGE = .11+.3;
    public static double[] clawValues={CLAW_OPEN, CLOSED_AUTO, CLAW_CLOSED_1, CLAW_CLOSED_2, CLAW_PRIMED, CLAW_HALF,CLOSED_EDGE};


    public enum RotateState implements ModuleState {
        ZERO, PLUS_FOURTY_FIVE, PLUS_NINETY, PLUS_ONE_THREE_FIVE, PLUS_ONE_EIGHTY, PLUS_TWO_TWO_FIVE, PLUS_TWO_SEVENTY,
        MINUS_FOURTY_FIVE, MINUS_NINETY, MINUS_ONE_THREE_FIVE, MINUS_ONE_EIGHTY, MINUS_TWO_TWO_FIVE, MINUS_TWO_SEVENTY
    }
    public static double ROTATE_0=.47, ROTATE_PLUS_45=0.4623, ROTATE_PLUS_90=.83, ROTATE_PLUS_135=0.5210, ROTATE_PLUS_180=0.5475, ROTATE_PLUS_225=0.5775, ROTATE_PLUS_270=0.6075;
    public static double ROTATE_MINUS_45=0.4067, ROTATE_MINUS_90=0.3789, ROTATE_MINUS_135=0.3511, ROTATE_MINUS_180=0.3233, ROTATE_MINUS_225 =0.2955, ROTATE_MINUS_270=0.2677;

    public static double[] rotateValues={ROTATE_0, ROTATE_PLUS_45, ROTATE_PLUS_90, ROTATE_PLUS_135, ROTATE_PLUS_180, ROTATE_PLUS_225, ROTATE_PLUS_270,
    ROTATE_MINUS_45, ROTATE_MINUS_90, ROTATE_MINUS_135, ROTATE_MINUS_180, ROTATE_MINUS_225, ROTATE_MINUS_270};


    double flip1Pos, flip2Pos, clawPos, wristPos, rotatePos,extPos;
    FlipState fstate;
    WristState wstate;
    RotateState wrstate;
    ClawState cstate;

    ExtensionState estate;
    public Deposit(HardwareMap hardwareMap) {
        super(false, telemetryToggle);
        leftArm=hardwareMap.get(Servo.class, "leftArm");
        rightArm=hardwareMap.get(Servo.class, "rightArm");
        rightArm.setDirection(Servo.Direction.REVERSE);
extension = hardwareMap.get(Servo.class, "extension");
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
        extension.setPosition(extPos);
    }

    protected void telemetryUpdate()
    {
        super.telemetryUpdate();
        Tel.instance().addData("Flip 1 Pos", leftArm.getPosition(), 0);
        Tel.instance().addData("Flip 2 Pos", rightArm.getPosition(), 0);
        Tel.instance().addData("Deposit Offset", depositOffset);
        Tel.instance().addData("Transfer Offset", transferOffset);
        Tel.instance().addData("Pos without offset", converter.getOutput(getState(FlipState.class)));
    }

    @Override
    protected void internalUpdate()
    {
        ModuleState state = getState(FlipState.class);
        if (state.equals(FlipState.LEFT)) {
            flip1Pos = converter.getOutput(getState(FlipState.class)) + ROTATE_MOD + depositOffset - transferOffset;
            flip2Pos = converter.getOutput(getState(FlipState.class)) - ROTATE_MOD;
        } else if (state.equals(FlipState.RIGHT)) {
            flip1Pos = converter.getOutput(getState(FlipState.class)) - ROTATE_MOD + depositOffset - transferOffset;
            flip2Pos = converter.getOutput(getState(FlipState.class)) + ROTATE_MOD;
        } else if(state.equals(FlipState.DEPOSIT)||state.equals(FlipState.DOWN)){
            flip1Pos = converter.getOutput(getState(FlipState.class))+ depositOffset - transferOffset;
            flip2Pos = converter.getOutput(getState(FlipState.class));
        }else{
            flip1Pos = converter.getOutput(getState(FlipState.class))- transferOffset;
            flip2Pos = converter.getOutput(getState(FlipState.class));
        }
        wristPos=converter.getOutput(getState(WristState.class));
        clawPos=converter.getOutput(getState(ClawState.class));
        rotatePos=converter.getOutput(getState(RotateState.class));
        extPos = converter.getOutput(getState(ExtensionState.class));
    }

    @Override
    protected void initInternalStates() {
        fstate=FlipState.TRANSFER;
        wstate=WristState.TRANSFER;
        wrstate= RotateState.ZERO;
        cstate=ClawState.OPEN;
        estate = ExtensionState.TRANSFER;
        setInternalStates(fstate, wstate, wrstate, cstate,estate);
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
        converter.add(ExtensionState.values(), extValues);
    }
}

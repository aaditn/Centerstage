package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;

public class Deposit extends Module
{
    Servo leftArm, rightArm, wrist, rotatewrist, claw;
    public static double clawOpen=0.99, clawClosed1=0.72, clawClosed2=0.68,clawPrimed = .75;
    public static double rotateZero=0.435, rotateNinety=0.49, rotateOneEighty=0.5475, rotateTwoSeventy=0.6075;
    public static double wristTransfer=0.64, wristDeposit=0.33, wristFloaty=0.53,wristScope=.74;
    public static double rotatorTransfer=0.94,rotatorDepositPrimed = .80, rotatorDeposit=0.22;

    public static boolean telemetryToggle;

    public enum FlipState implements ModuleState
    {
        TRANSFER(rotatorTransfer), DEPOSIT(rotatorDeposit),PRIMED(rotatorDepositPrimed);
        double pos1;
        FlipState(double pos1)
        {
            this.pos1=pos1;
        }
        @Override
        public double getOutput(int... index) {
            return pos1;
        }
    }

    public enum WristState implements ModuleState
    {
        TRANSFER(wristTransfer), DEPOSIT(wristDeposit), HOVER(wristFloaty), TELESCOPE(wristScope);
        double pos1;
        WristState(double pos1)
        {
            this.pos1=pos1;
        }
        @Override
        public double getOutput(int... index) {
            return pos1;
        }
    }
    public enum ClawState implements ModuleState
    {
        OPEN(clawOpen), CLOSED1(clawClosed1), CLOSED2(clawClosed2),PRIMED(clawPrimed);
        double pos1;
        ClawState(double pos1)
        {
            this.pos1=pos1;
        }
        @Override
        public double getOutput(int... index) {
            return pos1;
        }
    }

    public enum WristRotateState implements ModuleState {
        ZERO(rotateZero), NINETY(rotateNinety), ONE_EIGHTY(rotateOneEighty),TWO_SEVENTY(rotateTwoSeventy);
        double pos1;
        WristRotateState(double pos1)
        {
            this.pos1=pos1;
        }
        @Override
        public double getOutput(int... index) {
            return pos1;
        }
    }
    double rotator1Pos, rotator2Pos, clawPos, wristPos, wristRotatePos;
    FlipState fstate;
    WristState wstate;
    WristRotateState wrstate;
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
        leftArm.setPosition(rotator1Pos);
        rightArm.setPosition(rotator2Pos);
        wrist.setPosition(wristPos);
        claw.setPosition(clawPos);
        rotatewrist.setPosition(wristRotatePos);
    }

    @Override
    protected void internalUpdate() {
        rotator1Pos=getState(Deposit.FlipState.class).getOutput(1);
        rotator2Pos=getState(Deposit.FlipState.class).getOutput(2);
        wristPos=getState(Deposit.WristState.class).getOutput();
        clawPos=getState(Deposit.ClawState.class).getOutput();
        wristRotatePos=getState(Deposit.WristRotateState.class).getOutput();
    }

    @Override
    protected void initInternalStates() {
        fstate=FlipState.PRIMED;
        wstate=WristState.TELESCOPE;
        wrstate=WristRotateState.ZERO;
        cstate=ClawState.OPEN;
        setInternalStates(fstate, wstate, wrstate, cstate);
    }

    @Override
    protected void updateInternalStatus()
    {
        //TODO
    }
}

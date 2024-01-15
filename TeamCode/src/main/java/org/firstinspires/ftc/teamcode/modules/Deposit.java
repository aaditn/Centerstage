package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;

public class Deposit extends Module
{
    Servo leftArm, rightArm, wrist, rotatewrist, claw;
    public static double clawOpen=0.88, clawClosed1=0.72, clawClosed2=0.72;
    public static double rotateNinety=0.49, rotateZero=0.435, rotateOneEighty=0.5475, rotateTwoSeventy=1;
    public static double wristTransfer =0.21, wristDeposit=0.54, wristFloaty=0.44;
    public static double rotator1Transfer =0.99, rotator1Deposit=0.22;
    public static double rotator2Transfer =0.01, rotator2Deposit=0.78;

    public enum FlipState implements ModuleState
    {
        TRANSFER(rotator1Transfer, rotator2Transfer), DEPOSIT(rotator1Deposit, rotator2Deposit);
        double pos1, pos2;
        FlipState(double pos1, double pos2)
        {
            this.pos1=pos1;
            this.pos2=pos2;
        }
        @Override
        public double getOutput(int... index) {
            if(index[0]==1)
                return pos1;
            else if(index[0]==2)
                return pos2;
            else
                return 0;
        }
    }

    public enum WristState implements ModuleState
    {
        TRANSFER(wristTransfer), DEPOSIT(wristDeposit), HOVER(wristFloaty);
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
        OPEN(clawOpen), CLOSED1(clawClosed1), CLOSED2(clawClosed2);
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
        super(false);
        leftArm=hardwareMap.get(Servo.class, "leftArm");
        rightArm=hardwareMap.get(Servo.class, "rightArm");
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
        fstate=FlipState.TRANSFER;
        wstate=WristState.TRANSFER;
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

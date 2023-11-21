package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;
import org.firstinspires.ftc.teamcode.util.Context;

@Config
public class Deposit extends Module
{
    public static double transfer1=1;
    public static double transfer2=.12;

    public static double deposit1High= 0.05;//0.91;//.83
    public static double deposit2High = 1;//0.14;//.22
    public static double deposit1Mid = (1+0.5)/2;//0.94;//.83
    public static double deposit2Mid = (.12+1)/2;//0.11;//.22

    public static double deposit1Low= 1;//0.98;//.83
    public static double deposit2Low= 0.05;//0.07;//.22

    public static double initwrist =.65;
    public static double depositwrist=0.3;

    public static double pusherIn=0.04;
    public static double pusherPushed=0.18;
    public static double pusherOne=0.24;
    public static double pusherTwo=0.33;
    int debugCounter=0;

    public static enum RotationState implements ModuleState
    {
        TRANSFER(transfer1, transfer2, initwrist), DEPOSIT_LOW(deposit1Low, deposit2Low, depositwrist), DEPOSIT_MID(deposit1Mid, deposit2Mid, depositwrist), DEPOSIT_HIGH(deposit1High, deposit2High, depositwrist);

        double pos1, pos2, pos3;
        RotationState(double pos1, double pos2, double pos3)
        {
            this.pos1=pos1;
            this.pos2=pos2;
            this.pos3=pos3;
        }

        @Override
        public double getOutput(int... index) {
            if(index[0]==1)
            {
                return pos1;
            }
            else if(index[0]==2)
            {
                return pos2;
            }
            else if(index[0]==3)
            {
                return pos3;
            }
            else
            {
                return 0;
            }
        }
    }


    public static enum PusherState implements  ModuleState
    {
        IN(pusherIn),EXTENDED(pusherPushed), ONE(pusherOne), TWO(pusherTwo);

        double position;
        PusherState(double position)
        {
            this.position=position;
        }
        @Override
        public double getOutput(int... index) {
            return position;
        }
    }

    double leftRotatorPos, rightRotatorPos, pusherPos, wristPos;
    RotationState rstate;
    PusherState pstate;

    Servo leftRotator, rightRotator, pusher, wrist;

    public Deposit(HardwareMap hardwareMap)
    {
        super(false);
        leftRotator =hardwareMap.get(Servo.class, "leftRotator");
        rightRotator=hardwareMap.get(Servo.class, "rightRotator");
        pusher=hardwareMap.get(Servo.class, "pusher");
        wrist =hardwareMap.get(Servo.class,"wrist");
    }

    @Override
    protected void write()
    {
        leftRotator.setPosition(leftRotatorPos);
        rightRotator.setPosition(rightRotatorPos);
        wrist.setPosition(wristPos);
        pusher.setPosition(pusherPos);
    }

    @Override
    protected void telemetryUpdate()
    {
        super.telemetryUpdate();
        Context.tel.addData("Wrist position", wristPos);
        Context.tel.addData("internal update called counter", debugCounter);
    }

    @Override
    protected void internalUpdate()
    {
        debugCounter++;
        leftRotatorPos=getState(RotationState.class).getOutput(1);
        rightRotatorPos=getState(RotationState.class).getOutput(2);
        wristPos=getState(RotationState.class).getOutput(3);
        pusherPos=getState(PusherState.class).getOutput(1);
    }

    @Override
    protected void initInternalStates()
    {
        pstate=PusherState.IN;
        rstate=RotationState.TRANSFER;
        setInternalStates(pstate, rstate);
    }

    @Override
    protected void updateInternalStatus()
    {
        //TODO
    }
}

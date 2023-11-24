package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;

@Config
public class Deposit extends Module
{
    public static double transfer1=0.93;
    public static double transfer2=0.20;
    public static double deposit1High= 0.11;//0.91;//.83
    public static double deposit2High = 0.94;//0.14;//.22
    public static double deposit1Mid = 0.5;
    public static double deposit2Mid = 0.55;

    public static double deposit1Mid2 = 0.30;
    public static double deposit2Mid2 = 0.;
    public static double deposit1Mid3 = 0.21;
    public static double deposit2Mid3 = 0.84;





    public static double deposit1Low= 1;//0.98;//.83
    public static double deposit2Low= 0.05;//0.07;//.22



    public static double pusherIn=0.1;
    public static double pusherPushed=0.18;
    public static double pusherOne=0.23;

    public static double pusherOnePointFive = 0.28;
    public static double pusherTwo=0.32;

    public enum RotationState implements ModuleState
    {
        TRANSFER(transfer1, transfer2), DEPOSIT_MID(deposit1Mid, deposit2Mid), DEPOSIT_MID2(deposit1Mid2, deposit2Mid2), DEPOSIT_MID3(deposit1Mid3, deposit2Mid3), DEPOSIT_HIGH(deposit1High, deposit2High);

        double pos1, pos2;
        RotationState(double pos1, double pos2)
        {
            this.pos1=pos1;
            this.pos2=pos2;
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
            else
            {
                return 0;
            }
        }
    }


    public enum PusherState implements  ModuleState
    {
        IN(pusherIn),EXTENDED(pusherPushed), ONE(pusherOne), ONE_FIVE (pusherOnePointFive), TWO(pusherTwo);

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

    double leftRotatorPos, rightRotatorPos, pusherPos;
    RotationState rstate;
    PusherState pstate;

    Servo leftRotator, rightRotator, pusher;

    public Deposit(HardwareMap hardwareMap)
    {
        super(false);
        leftRotator =hardwareMap.get(Servo.class, "leftRotator");
        rightRotator=hardwareMap.get(Servo.class, "rightRotator");
        pusher=hardwareMap.get(Servo.class, "pusher");
    }

    @Override
    protected void write()
    {
        leftRotator.setPosition(leftRotatorPos);
        rightRotator.setPosition(rightRotatorPos);
        //pusher.setPosition(pusherPos);
    }

    @Override
    protected void internalUpdate()
    {
        leftRotatorPos=getState(RotationState.class).getOutput(1);
        rightRotatorPos=getState(RotationState.class).getOutput(2);
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

package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;

@Config
public class Deposit extends Module
{
    public static double transfer1=0.25;
    public static double transfer2=0.8;

    public static double deposit1=0.83;
    public static double deposit2=0.22;

    public static double pusherIn=0.1;
    public static double pusherPushed=0.18;
    public static double pusherOne=0.22;
    public static double pusherTwo=0.26;

    public enum RotationState implements ModuleState
    {
        TRANSFER(transfer1, transfer2), DEPOSIT(deposit1, deposit2);

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

    double rotater1Pos, rotater2Pos, pusherPos;
    RotationState rstate;
    PusherState pstate;

    Servo rotater1, rotater2, pusher;

    public Deposit(HardwareMap hardwareMap)
    {
        super(false);
        rotater1=hardwareMap.get(Servo.class, "rotater1");
        rotater2=hardwareMap.get(Servo.class, "rotater2");
        pusher=hardwareMap.get(Servo.class, "pinion");
    }

    @Override
    protected void write()
    {
        rotater1.setPosition(rotater1Pos);
        rotater2.setPosition(rotater2Pos);
        pusher.setPosition(pusherPos);
    }

    @Override
    protected void internalUpdate()
    {
        rotater1Pos=getState(RotationState.class).getOutput(1);
        rotater2Pos=getState(RotationState.class).getOutput(2);
        pusherPos=getState(PusherState.class).getOutput();
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

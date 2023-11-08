package org.firstinspires.ftc.teamcode.modules;

import com.arcrobotics.ftclib.hardware.ServoEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;

public class Deposit extends Module
{
    public enum RotationState implements ModuleState
    {
        TRANSFER(0.0, 1.0), DEPOSIT(0.5, 0.5);

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
        IN(0.5), OUT(0.3);

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

    ServoEx rotater1, rotater2, pusher;

    public Deposit(HardwareMap hardwareMap)
    {
        super(false);
        rotater1=hardwareMap.get(ServoEx.class, "rotater1");
        rotater2=hardwareMap.get(ServoEx.class, "rotater2");
        pusher=hardwareMap.get(ServoEx.class, "pusher");
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

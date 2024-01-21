package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;

public class DroneLauncher extends Module
{
    Servo lock;
    public static boolean telemetryToggle=false;

    public static enum State implements ModuleState
    {
        RELEASED(0.1), LOCKED(0.62);

        double position;
        State(double position)
        {
            this.position=position;
        }

        @Override
        public double getOutput(int... index)
        {
            return position;
        }
    }

    double currentPosition;

    State state=State.LOCKED;

    public DroneLauncher(HardwareMap hardwareMap)
    {
        super(false, telemetryToggle);
        lock=hardwareMap.get(Servo.class, "droneLock");
    }

    public double getCurrentPosition()
    {
        return lock.getPosition();
    }

    @Override
    protected void write()
    {
        lock.setPosition(currentPosition);
    }

    @Override
    protected void internalUpdate()
    {
        currentPosition=getState().getOutput();
    }

    @Override
    protected void initInternalStates()
    {
        state=State.LOCKED;
        setInternalStates(state);
    }

    @Override
    protected void updateInternalStatus()
    {
        if(timeSpentInState()<2000)
        {
            status=Status.TRANSITIONING;
        }
        else
        {
            status=Status.IDLE;
        }
    }
}
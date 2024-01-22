package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;
@Config
public class DroneLauncher extends Module
{
    Servo lock;
    public static boolean telemetryToggle=false;
    static double RELEASED=0.1, LOCKED =0.62;

    public enum State implements ModuleState
    {
        RELEASED, LOCKED;
    }
    public static double[] stateValues={RELEASED, LOCKED};

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
        currentPosition=converter.getOutput(getState());
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

    @Override
    protected void mapToKey()
    {
        converter.add(State.values(), stateValues);
    }
}
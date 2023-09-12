package org.firstinspires.ftc.teamcode.modules.moduleUtil;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class Module
{
    //Is an array for the modules that have multiple states to them
    private ModuleState[] states;
    private HardwareMap hardwareMap;
    boolean constantUpdate;
    boolean stateChanged;

    Telemetry tel;
    private ElapsedTime timer;

    public enum Status
    {
        TRANSITIONING, IDLE
    }

    Status status=Status.IDLE;

    public Module(HardwareMap hardwareMap, Telemetry tel, boolean constantUpdate)
    {
        this.constantUpdate=constantUpdate;
        this.hardwareMap=hardwareMap;
        this.tel=tel;
        timer=new ElapsedTime();
        initInternalStates();
    }

    protected void setInternalStates(ModuleState... states)
    {
        this.states=states;
    }

    public void setState(ModuleState state)
    {
        for(int i=0; i<states.length; i++)
        {
            if(state.getClass()==states[i].getClass())
            {
                states[i]=state;
            }
        }
        onStateChange();
    }

    public ModuleState getState(Class e)
    {
        for(int i=0; i<states.length; i++)
        {
            if(e==states[i].getClass())
            {
                return states[i];
            }
        }
        return null;
    }

    public ModuleState getState()
    {
        return states[0];
    }

    private void onStateChange()
    {
        timer.reset();
        status=Status.TRANSITIONING;
        stateChanged();
        readUpdate();
        stateChanged=true;
    }

    private void readUpdate()
    {
        telemetryUpdate();
        updateInternalStatus();
        internalUpdate();
    }

    public void updateLoop()
    {
        if(constantUpdate)
        {
            readUpdate();
        }
    }

    public void writeLoop()
    {
        if(constantUpdate||stateChanged)
        {
            write();
            stateChanged=false;
        }
    }

    protected abstract void write();


    public double timeSpentInState()
    {
        return timer.milliseconds();
    }

    protected void stateChanged(){}
    protected void telemetryUpdate()
    {
        tel.addData(this.getClass()+": time spent in state", timeSpentInState());
        for(ModuleState s: states)
        {
            tel.addData(this.getClass()+": "+ s.getClass()+" state", s);
        }
    }
    protected abstract void internalUpdate();
    protected abstract void initInternalStates();
    protected abstract void updateInternalStatus();

    public boolean isBusy()
    {
        if(status==Status.TRANSITIONING)
            return true;
        return false;
    }
}

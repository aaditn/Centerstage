package org.firstinspires.ftc.teamcode.modules.moduleUtil;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.Context;

public abstract class Module
{
    //Is an array for the modules that have multiple states to them
    private ModuleState[] states;
    boolean constantUpdate;
    boolean stateChanged;

    public String telIdentifier;

    protected ElapsedTime timer;
    protected int currentTimeout;

    public enum Status
    {
        TRANSITIONING, IDLE
    }

    protected Status status=Status.IDLE;

    //constructor. gives module all information to do tasks. constantUpdate is used for modules with PID
    // or some other controller that needs constant updating
    public Module(boolean constantUpdate)
    {
        this.constantUpdate=constantUpdate;
        timer=new ElapsedTime();
        initInternalStates();
        telIdentifier=this.getClass().toString().substring(this.getClass().toString().lastIndexOf('.')+1);
    }

    //needed to store states of a module within this class(array to store if same class has multiple states.
    //ie: multiple servo deposit)
    protected void setInternalStates(ModuleState... states)
    {
        this.states=states;
    }


    //weird code is for modules w/ multiple states(looking for state that mtaches)
    public void setState(ModuleState state)
    {
        for(int i=0; i<states.length; i++)
        {
            if(state.getClass()==states[i].getClass())
            {
                states[i]=state;
            }
        }
        //resets timeout to 0 if none specified
        currentTimeout=0;
        //triggers a sequence that occurs once when state is changed(this is how powers change for stuff with constantUpdate off)
        onStateChange();
    }

    //same code but with timeout
    public void setState(ModuleState state, int timeout)
    {
        for(int i=0; i<states.length; i++)
        {
            if(state.getClass()==states[i].getClass())
            {
                states[i]=state;
            }
        }
        currentTimeout=timeout;
        onStateChange();
    }

    //gets the specific state you want
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

    //default behavior returns first state in the array
    public ModuleState getState()
    {
        return states[0];
    }

    //triggered once when state is changed
    private void onStateChange()
    {
        timer.reset();
        updateInternalStatus();
        stateChanged();
        readUpdate();
        stateChanged=true;
    }

    //runs once on state change and permanently if constantUpdate is enabled
    private void readUpdate()
    {
        telemetryUpdate();
        updateInternalStatus();
        internalUpdate();
    }


    //way to call update loop from opMode
    public void updateLoop()
    {
        if(constantUpdate)
        {
            readUpdate();
        }
        //updateInternalStatus always running to account for timeout conditions and telemetry for fun
        else
        {
            updateInternalStatus();
            telemetryUpdate();
        }
    }

    //way to call writes from opMode. Separate from update to make write calls all at the same time.
    public void writeLoop()
    {
        if(constantUpdate||stateChanged)
        {
            write();
            stateChanged=false;
        }
    }

    public void init()
    {
        onStateChange();
    }

    //implement your write function
    protected abstract void write();

    //way to get state times from outside class/in opMode
    public double timeSpentInState()
    {
        return timer.milliseconds();
    }

    //called once on stateChange(useful for things that only need to be called once when state changes
    //but also part of a module that is constantUpdate)
    protected void stateChanged(){}

    //prints telemetry stuff. can rewrite implementation for it(if you want less or more info)
    protected void telemetryUpdate()
    {

        Context.tel.addData(telIdentifier+": time spent in state", timeSpentInState());
        Context.tel.addData(telIdentifier+" status", status);
        for(ModuleState s: states)
        {
            Context.tel.addData(telIdentifier+": "+ s.getClass()+" state", s);
        }
    }



    //write implementation for updating internally(setting powers to something)
    protected abstract void internalUpdate();

    //need implementation to set the states so this module can access all states
    protected abstract void initInternalStates();

    //used for specifically updating module status like busy/not busy
    //(can be used to check timeouts since constantly runs regardless
    //of if constantUpdate or not
    protected abstract void updateInternalStatus();

    public boolean isBusy()
    {
        if(status==Status.TRANSITIONING)
            return true;
        return false;
    }
}

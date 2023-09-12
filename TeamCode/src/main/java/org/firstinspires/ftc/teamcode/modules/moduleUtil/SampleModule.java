package org.firstinspires.ftc.teamcode.modules.moduleUtil;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SampleModule extends Module
{
    public enum State1 implements ModuleState
    {
        HI, BYE;
    }

    public enum State2 implements ModuleState
    {
        HELLO, GOODBYE;
    }

    State1 state1=State1.BYE;
    State2 state2=State2.GOODBYE;

    public SampleModule(HardwareMap hardwareMap, Telemetry tel)
    {
        super(hardwareMap, tel, true);
    }

    @Override
    public void write()
    {
        //actually write the powers to the motor
    }

    @Override
    public void internalUpdate()
    {
        //update the internal powers and stuff
    }

    @Override
    protected void initInternalStates()
    {
        //SET THE INTERNAL STATES
        setInternalStates(state1, state2);
    }

    //gets called repeatedly(meant for changing idle/transitioning)
    @Override
    protected void updateInternalStatus()
    {
        //if something
        status=Status.IDLE;
        //or
        status=Status.TRANSITIONING;
    }

    //gets called repeatedly(meant for updating telemetry)
    public void telemetryUpdate()
    {
        super.telemetryUpdate();
    }

    //calls whenever state changes. DO NOT ADD ANY WRITE CALLS HERE. Only meant for changing some other values.
    public void stateChanged()
    {

    }
}

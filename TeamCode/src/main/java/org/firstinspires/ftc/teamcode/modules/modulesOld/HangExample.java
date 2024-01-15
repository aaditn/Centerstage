package org.firstinspires.ftc.teamcode.modules.modulesOld;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;

public class HangExample extends Module
{
    DcMotorEx h1, h2;

    public enum State implements ModuleState
    {
        OUT(1), IN(-1), OFF(0);

        double power;

        State(double power)
        {
            this.power=power;
        }

        @Override
        public double getOutput(int... index)
        {
            return power;
        }
    }

    double currentPower;

  State state;

    public HangExample(HardwareMap hardwareMap, Telemetry tel)
    {
        super(false);
        h1=hardwareMap.get(DcMotorEx.class, "h1");
        h2=hardwareMap.get(DcMotorEx.class, "h2");

        h1.setDirection(DcMotorSimple.Direction.REVERSE);
    }


    @Override
    protected void write()
    {
        h1.setPower(currentPower);
        h2.setPower(currentPower);
    }

    @Override
    protected void internalUpdate()
    {
        currentPower=state.power;
    }

    @Override
    protected void initInternalStates()
    {
        setInternalStates(state);
    }

    @Override
    protected void updateInternalStatus()
    {
        if(state!=State.OFF)
        {
            status=Status.TRANSITIONING;
        }
        else
        {
            status=Status.TRANSITIONING;
        }
    }
}

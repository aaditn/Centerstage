package org.firstinspires.ftc.teamcode.modules;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;

public class ModuleTest extends Module
{
    DcMotorEx motor;
    public double currentMotorPower;

    public enum State implements ModuleState
    {
        FWD(1), REV(-1), OFF(0);

        double power;

        State(double pow)
        {
            power=pow;
        }

        @Override
        public double getOutput(int... index)
        {
            return power;
        }
    }

    State state;

    public ModuleTest(HardwareMap hardwareMap)
    {
        super(true);
        motor=hardwareMap.get(DcMotorEx.class, "motor");
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    protected void write()
    {
        motor.setPower(currentMotorPower);
    }

    @Override
    protected void internalUpdate()
    {
        currentMotorPower=getState().getOutput();
    }

    @Override
    protected void initInternalStates()
    {
        state=State.OFF;
        setInternalStates(state);
    }


    @Override
    protected void updateInternalStatus()
    {
        if(getState()==State.OFF||(currentTimeout>0&&timer.milliseconds()>currentTimeout))
        {
            status=Status.IDLE;
        }
        else
        {
            status=Status.TRANSITIONING;
        }
    }


    //Actions here


    public Action runFor(State state, int miliseconds)
    {
        return new runFor(state, miliseconds);
    }

    public class runFor implements Action
    {
        int miliseconds;
        public runFor(State state, int miliseconds)
        {
            this.miliseconds=miliseconds;
            setState(state, miliseconds);
        }

        @Override
        public void preview(@NonNull Canvas canvas)
        {

        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket)
        {
            if(!isBusy())
            {
                setState(State.OFF);
                return false;
            }
            else
            {
                return true;
            }
        }
    }

}

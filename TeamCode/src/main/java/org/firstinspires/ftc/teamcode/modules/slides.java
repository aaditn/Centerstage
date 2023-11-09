package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.*;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.BPIDFController;

public class slides extends Module
{
    DcMotorEx right;
    DcMotorEx left;

    BPIDFController controller;
    double targetPosition, motorPower;
    public static double kp, ki, kd;
    public enum SlideState implements ModuleState
    {
        GROUND(0), ROW1(0), ROW2(0), ROW3(0);

        double position;
        SlideState(double position)
        {
            this.position=position;
        }

        @Override
        public double getOutput(int... index) {
            return position;
        }
    }

    SlideState state=SlideState.GROUND;
    Telemetry tel;
    public slides(HardwareMap hardwareMap, Telemetry tel)
    {
        super(true);
        right=hardwareMap.get(DcMotorEx.class, "right");
        left=hardwareMap.get(DcMotorEx.class, "left");
        right.setDirection(DcMotorSimple.Direction.REVERSE);
        controller=new BPIDFController(new PIDCoefficients(kp, ki, kd));
        this.tel=tel;
    }

    @Override
    public void write()
    {
        right.setPower(motorPower);
        left.setPower(motorPower);
        //actually write the powers to the motor
    }

    @Override
    public void internalUpdate()
    {
        targetPosition=getState().getOutput();
        motorPower=controller.update(right.getCurrentPosition(), targetPosition);
        //update the internal powers and stuff
    }

    @Override
    protected void initInternalStates()
    {
        //SET THE INTERNAL STATES
        setInternalStates(state);
    }

    //gets called repeatedly(meant for changing idle/transitioning)
    @Override
    protected void updateInternalStatus()
    {
        if (Math.abs(targetPosition - right.getCurrentPosition())<10){
            status=Status.IDLE;
        }
        else{
            status=Status.TRANSITIONING;
        }
    }

    //gets called repeatedly(meant for updating telemetry)
    public void telemetryUpdate()
    {
        super.telemetryUpdate();
        tel.addData("Target Position", targetPosition);
    }

    //calls whenever state changes. DO NOT ADD ANY WRITE CALLS HERE. Only meant for changing some other values.
    public void stateChanged()
    {

    }
}
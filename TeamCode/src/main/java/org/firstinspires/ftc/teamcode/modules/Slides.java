package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.*;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.BPIDFController;
import org.firstinspires.ftc.teamcode.util.Context;

public class Slides extends Module
{
    DcMotorEx slide1;
    DcMotorEx slide2;

    BPIDFController controller;
    public double targetPosition, motorPower;
    public static double kp=0.5, ki=0, kd=0;
    public static enum SlideState implements ModuleState
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


    SlideState state;

    public Slides(HardwareMap hardwareMap)
    {
        super(true);
        slide1 =hardwareMap.get(DcMotorEx.class, "slide1");
        slide2 =hardwareMap.get(DcMotorEx.class, "slide2");
        slide1.setDirection(DcMotorSimple.Direction.REVERSE);
        slide1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        controller=new BPIDFController(new PIDCoefficients(kp, ki, kd));
    }

    @Override
    protected void write()
    {
        if (slide1.getCurrentPosition() > 1125) {
            slide1.setPower(0);
            slide2.setPower(0);
        } else {
            slide1.setPower(motorPower);
            slide2.setPower(motorPower);
        }

        //actually write the powers to the motor
    }


    @Override
    public void internalUpdate()
    {
        targetPosition=getState().getOutput();
        motorPower=controller.update(slide1.getCurrentPosition(), targetPosition);
        //update the internal powers and stuff
    }

    @Override
    public void internalUpdateManual()
    {
        motorPower=controller.update(slide1.getCurrentPosition(), targetPosition);
    }
    @Override
    public void manualChange(double position)
    {
        super.manualChange(position);
        targetPosition=position;
    }

    @Override
    protected void initInternalStates()
    {
        //SET THE INTERNAL STATES
        state=SlideState.GROUND;
        setInternalStates(state);
    }

    //gets called repeatedly(meant for changing idle/transitioning)
    @Override
    protected void updateInternalStatus()
    {
        if (Math.abs(targetPosition - slide1.getCurrentPosition())<10){
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
        Context.tel.addData("Target Position", targetPosition);
    }
}
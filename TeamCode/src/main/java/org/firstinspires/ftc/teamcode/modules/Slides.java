package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.*;
import org.firstinspires.ftc.teamcode.util.BPIDFController;
import org.firstinspires.ftc.teamcode.util.Context;

@Config
public class Slides extends Module
{
    DcMotorEx slide1;
    DcMotorEx slide2;

    BPIDFController controller;
    public double targetPosition, motorPower;
    public double debugValue=0;
    public static double slideCap=1100;
    public static double kp=0.007, ki=0, kd=0.0003;
    public static double kp2=0.0005, ki2=0, kd2=0.0001;
    public static double kp3=0.002, ki3, kd3=0.0001;
    PIDCoefficients standardcoeff, closecoeff, downcoeff;
    public static enum SlideState implements ModuleState
    {
        GROUND(0), RAISED(300), ROW1(600), ROW2(900), ROW3(0);

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
        //super(true);
        super(false);
        slide1 =hardwareMap.get(DcMotorEx.class, "slide1");
        slide2 =hardwareMap.get(DcMotorEx.class, "slide2");
        slide2.setDirection(DcMotorSimple.Direction.REVERSE);
        slide1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide1.setTargetPosition((int)getState().getOutput());
        slide2.setTargetPosition((int)getState().getOutput());
        //slide1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //slide2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        /*standardcoeff =new PIDCoefficients(kp, ki, kd);
        closecoeff =new PIDCoefficients(kp2, ki2, kd2);
        downcoeff=new PIDCoefficients(kp3, ki3, kd3);

        controller=new BPIDFController(standardcoeff);*/
    }

    @Override
    protected void write()
    {
        //slide1.setPower(motorPower);
        //slide2.setPower(motorPower);
        slide1.setTargetPosition((int) targetPosition);
        slide2.setTargetPosition((int) targetPosition);
        slide1.setPower(1);
        slide2.setPower(1);
        //actually write the powers to the motor
    }


    @Override
    public void internalUpdate()
    {
        targetPosition=getState().getOutput();

        if(targetPosition>slideCap)
        {
            targetPosition=slideCap;
        }

        /*if(Math.abs(targetPosition-slide1.getCurrentPosition())<10)
        {
            controller.gainSchedule(closecoeff);
        }
        else if(targetPosition<slide1.getCurrentPosition())
        {
            controller.gainSchedule(downcoeff);
        }
        else
        {
            controller.gainSchedule(standardcoeff);
        }

        targetPosition=getState().getOutput();
        controller.setTargetPosition(targetPosition);

        motorPower=controller.update(slide1.getCurrentPosition());*/

        //update the internal powers and stuff
    }

    @Override
    public void internalUpdateManual()
    {
        if(targetPosition>slideCap)
        {
            targetPosition=slideCap;
        }

        /*if(Math.abs(targetPosition-slide1.getCurrentPosition())<10)
        {
            controller.gainSchedule(closecoeff);
        }
        else if(targetPosition<slide1.getCurrentPosition())
        {
            controller.gainSchedule(downcoeff);
        }
        else
        {
            controller.gainSchedule(standardcoeff);
        }

        controller.setTargetPosition(targetPosition);
        motorPower=controller.update(slide1.getCurrentPosition());*/
    }
    @Override
    public void manualChange(double position)
    {
        super.manualChange(position);
        targetPosition=position;
        if(targetPosition>slideCap)
        {
            targetPosition=slideCap;
        }
    }

    public double currentPosition()
    {
        return slide1.getCurrentPosition();
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
    public Status getStatus()
    {
        return status;
    }

    public double getTargetPosition()
    {
        return targetPosition;
    }

    //gets called repeatedly(meant for updating telemetry)
    public void telemetryUpdate()
    {
        super.telemetryUpdate();
        Context.tel.addData("Target Position", targetPosition);
        Context.tel.addData("Attempted power", motorPower);
        Context.tel.addData("Slide 1", slide1.getCurrentPosition());
    }
}
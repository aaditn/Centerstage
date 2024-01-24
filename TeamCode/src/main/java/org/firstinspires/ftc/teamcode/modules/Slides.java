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
    public static boolean telemetryToggle=false;


    PIDCoefficients standardcoeff, closecoeff, downcoeff;
    public enum SlideState implements ModuleState
    {
        GROUND, HALF, AUTO_LOW,AUTO_TWO, RAISED, ROW1, ROW2, ROW3;
    }
    public static double GROUND=0, HALF=150, AUTO_LOW=90, AUTO_TWO=250, RAISED=300, ROW1=700, ROW2=1000, ROW3=1300;
    public static double[] stateValues={GROUND, HALF, AUTO_LOW, AUTO_TWO, RAISED, ROW1, ROW2, ROW3};
    SlideState state;

    public Slides(HardwareMap hardwareMap)
    {   
        //super(true);
        super(false, telemetryToggle);
        slide1 =hardwareMap.get(DcMotorEx.class, "slide1");
        slide2 =hardwareMap.get(DcMotorEx.class, "slide2");
        slide1.setDirection(DcMotorSimple.Direction.REVERSE);
        slide1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide1.setTargetPosition((int)converter.getOutput(getState()));
        slide2.setTargetPosition((int)converter.getOutput(getState()));
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
        targetPosition=converter.getOutput(getState());

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

    public void setMotorRunMode(DcMotor.RunMode runmode)
    {
        slide1.setMode(runmode);
        slide2.setMode(runmode);
    }

    public DcMotor.RunMode getMotorRunMode()
    {
        return slide1.getMode();
    }


    //gets called repeatedly(meant for changing idle/transitioning)
    @Override
    protected void updateInternalStatus()
    {
        if (Math.abs(targetPosition - slide1.getCurrentPosition())<10 ||opstate==OperationState.MANUAL){
            status=Status.IDLE;
        }
        else{
            status=Status.TRANSITIONING;
        }
    }

    @Override
    protected void mapToKey()
    {
        converter.add(SlideState.values(), stateValues);
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
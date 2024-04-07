package org.firstinspires.ftc.teamcode.modules;

import android.util.Log;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;
import org.firstinspires.ftc.teamcode.util.Tel;

@Config
public class Slides extends Module
{
    public DcMotorEx slide1;
    public DcMotorEx slide2;
    public TouchSensor slidesLimit;
    PIDFController controller;
    public double targetPosition, motorPower;
    public double debugValue=0;
    public static double slideCap=1500;
    public static double limitTimeout=300;
    public static boolean telemetryToggle=true;
    private boolean resetRequired;

    PIDCoefficients standardcoeff, closecoeff, downcoeff;
    public enum SlideState implements ModuleState
    {
        GROUND_UNTIL_LIMIT, GROUND, HALF, AUTO_LOW,AUTO_TWO, RAISED, ROW1, ROW2, ROW3, R1, R1_5, R2, R3, R35, R4, R45, R5, R6, R7, R8, R9;
    }
    public static double GROUND_UNTIL_LIMIT=-200, GROUND=0,  RAISED=300, AUTO_LOW=350, AUTO_TWO=500, ROW1=700, ROW2=1000, ROW3=1400;

    static double offset = -95;
    public static double HALF=150, R1=280, R1_5=350, R2=460, R3=600, R35=470, R4=740, R45 = 610, R5=880, R6=1020, R7=1190, R8=1190, R9 = 1400;
    public static double[] stateValues={GROUND_UNTIL_LIMIT, GROUND, HALF, AUTO_LOW, AUTO_TWO, RAISED, ROW1, ROW2, ROW3,
    R1, R1_5,R2, R3, R35, R4, R45, R5, R6, R7, R8, R9};
    SlideState state;



    public Slides(HardwareMap hardwareMap)
    {   
        //super(true);
        super(true, telemetryToggle);
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
        resetRequired=false;

        slidesLimit=hardwareMap.get(TouchSensor.class, "slidesLimit");

        /*standardcoeff =new PIDCoefficients(kp, ki, kd);
        closecoeff =new PIDCoefficients(kp2, ki2, kd2);
        downcoeff=new PIDCoefficients(kp3, ki3, kd3);

        controller=new BPIDFController(standardcoeff);*/
    }

    @Override
    protected void write()
    {
        if(resetRequired)
        {
            Log.d("Limit Switch", "Reset Triggered");
            slide1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            slide2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            slide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            resetRequired=false;
        }
        //slide1.setPower(motorPower);
        //slide2.setPower(motorPower);
        if(getState() == SlideState.GROUND_UNTIL_LIMIT)
        {
            slide1.setPower(0.5);
            slide2.setPower(0.5);
        }
        else
        {
            slide1.setPower(1);
            slide2.setPower(1);
        }
        slide1.setTargetPosition((int) targetPosition);
        slide2.setTargetPosition((int) targetPosition);

        //actually write the powers to the motor
    }
    @Override
    public void internalUpdate()
    {

        //RobotLog.e("cancer");
        if(getState()==SlideState.GROUND_UNTIL_LIMIT&&(slidesLimit.isPressed()||super.timeSpentInState()>limitTimeout))
        {
            resetRequired=true;
            setState(SlideState.GROUND);
        }

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

    public boolean isIdle()
    {
        if(status==Status.IDLE)
            return true;
        else
            return false;
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
        Tel.instance().addData("Target Position", slide1.getTargetPosition());
        Tel.instance().addData("Attempted power", motorPower);
        Tel.instance().addData("Slide 1", slide1.getCurrentPosition(), 2);
        Tel.instance().addData("Slide 2", slide2.getCurrentPosition(), 2);

        Tel.instance().addData("Slide 1",  slide2.getCurrent(CurrentUnit.AMPS), 2);
        Tel.instance().addData("Slide 2 amps", slide2.getCurrent(CurrentUnit.AMPS), 2);
        Tel.instance().addData("Limit", slidesLimit.isPressed());
        Tel.instance().addData("matthew", resetRequired);
        Tel.instance().addData("timelocal",super.timeSpentInState() );
        Tel.instance().addData("boolean", slidesLimit.isPressed()||super.timeSpentInState()>limitTimeout);
    }
}
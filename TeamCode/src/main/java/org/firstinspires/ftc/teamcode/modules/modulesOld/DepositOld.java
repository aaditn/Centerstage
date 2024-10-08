package org.firstinspires.ftc.teamcode.modules.modulesOld;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;
import org.firstinspires.ftc.teamcode.util.Tel;

@Config
public class DepositOld extends Module
{
    public static double transfer1=0.92;
    public static double transfer2=0.08;

    public static double deposit1High= 0;//0.91;//.83
    public static double deposit2High = 1;//0.14;//.22

    public static double deposit1Quarter1=(transfer1+deposit1High)/4;
    public static double deposit2Quarter1=(transfer2+deposit2High)/4;
    public static double deposit1Mid = (transfer1+deposit1High)*3/8;//0.94;//.83
    public static double deposit2Mid = (transfer2+deposit2High)*3/8;//0.11;//.22

    public static double deposit1Quarter3=0.1;
    public static double deposit2Quarter3=0.9;
    public static double deposit1Low= 1;//0.98;//.83
    public static double deposit2Low= 0.05;//0.07;//.22

    public static double initwrist =0.82;
    public static double cradle = 0.92;
    public static double cradle_auto=0.98;
    public static double depositwrist=0.49;

    public static double pusherIn=0;
    public static double pusherExtendedAuto=0.12;
    public static double pusherExtended =0.16;
    public static double pusherOne=0.2;//.25
    public static double pusherOneAuto=0.2;//.28
    public static double pusherTwo=0.29;//.35
    public static double pusherHalf= 0.25;//.265
    int debugCounter=0;

    public boolean funnimode=true;
    @Config
    public static enum RotationState implements ModuleState
    {

        TRANSFER(transfer1, transfer2), DEPOSIT_LOW(deposit1Low, deposit2Low), DEPOSIT_Q1(deposit1Quarter1, deposit2Quarter1), DEPOSIT_MID(deposit1Mid, deposit2Mid), DEPOSIT_Q3(deposit1Quarter3, deposit2Quarter3), DEPOSIT_HIGH(deposit1High, deposit2High);

        double pos1, pos2;
        RotationState(double pos1, double pos2)
        {
            this.pos1=pos1;
            this.pos2=pos2;
        }

    }

    public enum WristState implements ModuleState
    {
        TRANSFER(initwrist), CRADLE(cradle), CRADLE_AUTO(cradle_auto), DEPOSIT(depositwrist);

        double pos1;
        WristState(double pos1)
        {
            this.pos1=pos1;
        }


    }


    public static enum PusherState implements  ModuleState
    {
        IN(pusherIn),EXTENDED(pusherExtended), EXTENDED_AUTO(pusherExtendedAuto), ONE(pusherOne), TWO(pusherTwo), ONE_AUTO(pusherOneAuto), HALF(pusherHalf);

        double position;
        PusherState(double position)
        {
            this.position=position;
        }

    }

    double leftRotatorPos, rightRotatorPos, pusherPos, wristPos;
    RotationState rstate;
    PusherState pstate;
    WristState wstate;

    Servo leftRotator, rightRotator;
    public ColorRangeSensor firstPixel, secondPixel;
    Servo pusher, wrist;

    public DepositOld(HardwareMap hardwareMap)
    {
        super(true);
        leftRotator=hardwareMap.get(Servo.class, "leftRotator");
        rightRotator=hardwareMap.get(Servo.class, "rightRotator");
        //leftRotator.setDirection(Servo.Direction.REVERSE);
        //rightRotator.setDirection(Servo.Direction.REVERSE);
        pusher=hardwareMap.get(Servo.class, "pusher");
        wrist =hardwareMap.get(Servo.class,"wrist");
        firstPixel = hardwareMap.get(ColorRangeSensor.class, "firstCS");
        secondPixel = hardwareMap.get(ColorRangeSensor.class, "secondCS");
    }


    @Override
    protected void write()
    {
        /*
            if(Math.abs(leftRotator.getPosition()-leftRotatorPos)>=0.01)
            {
                if(leftRotator.getPosition()<leftRotatorPos){
                    leftRotator.setPosition(leftRotator.getPosition()+0.01);
                }
                else if(leftRotator.getPosition()>leftRotatorPos){
                    leftRotator.setPosition(leftRotator.getPosition()-0.01);
                }
            }
            if(Math.abs(rightRotator.getPosition()-rightRotatorPos)>=0.01)
            {
                if(rightRotator.getPosition()<rightRotatorPos){
                    rightRotator.setPosition(rightRotator.getPosition()+0.01);
                }
                else if(leftRotator.getPosition()>leftRotatorPos) {
                    rightRotator.setPosition(rightRotator.getPosition() - 0.01);
                }
            }
        */
        rightRotator.setPosition(rightRotatorPos);
        leftRotator.setPosition(leftRotatorPos);

        wrist.setPosition(wristPos);
        pusher.setPosition(pusherPos);
    }

    @Override
    protected void telemetryUpdate()
    {
        super.telemetryUpdate();
        //TelemetryWrapper.getInstance().addData("left current", leftRotator.);
        Tel.instance().addData("Left position", leftRotator.getPosition());
        Tel.instance().addData("pusher pos", pusherPos);
        Tel.instance().addData("internal update called counter", debugCounter);
    }

    @Override
    protected void internalUpdate()
    {
        //leftRotatorPos=getState(RotationState.class).getOutput(1);
        //rightRotatorPos=getState(RotationState.class).getOutput(2);
        //wristPos=getState(WristState.class).getOutput();
        //pusherPos=getState(PusherState.class).getOutput();
    }

    @Override
    protected void initInternalStates()
    {
        pstate=PusherState.IN;
        rstate=RotationState.TRANSFER;
        wstate=WristState.TRANSFER;
        setInternalStates(pstate, rstate, wstate);
    }

    @Override
    protected void updateInternalStatus()
    {
        //TODO
    }

    @Override
    protected void mapToKey() {

    }
}

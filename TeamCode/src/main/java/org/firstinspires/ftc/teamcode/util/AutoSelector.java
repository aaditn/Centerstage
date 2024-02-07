package org.firstinspires.ftc.teamcode.util;

import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.KeyReader;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;

public class AutoSelector
{
    KeyReader[] keyReaders;
    ButtonReader increase, decrease, swapLeft, swapRight;
    GamepadEx g1;
    static AutoSelector selector;
    int localAutoState;
    CyclePixelCount[] states={CyclePixelCount.ZERO, CyclePixelCount.TWO, CyclePixelCount.FOUR,
            CyclePixelCount.FIVE, CyclePixelCount.SIX};
    public AutoSelector()
    {
        g1=new GamepadEx(Context.opmode.gamepad1);

        keyReaders= new KeyReader[]{
                increase=new ToggleButtonReader(g1, GamepadKeys.Button.DPAD_UP),
                decrease=new ToggleButtonReader(g1, GamepadKeys.Button.DPAD_DOWN),
                swapLeft=new ToggleButtonReader(g1, GamepadKeys.Button.DPAD_LEFT),
                swapRight=new ToggleButtonReader(g1, GamepadKeys.Button.DPAD_RIGHT)
        };
    }

    public static AutoSelector getInstance()
    {
        if(selector==null)
            selector=new AutoSelector();
        return selector;
    }

    public static void destroyInstance()
    {
        selector=null;
    }

    public void loop()
    {
        for (KeyReader reader : keyReaders)
        {
            reader.readValue();
        }

        if(increase.wasJustPressed()&&Context.autoWaitTime<10)
            Context.autoWaitTime++;

        if(decrease.wasJustPressed()&&Context.autoWaitTime>0)
            Context.autoWaitTime--;

        if(swapLeft.wasJustPressed())
        {
            if(localAutoState>0)
                localAutoState--;
            else
                localAutoState=4;
        }
        if (swapRight.wasJustPressed())
        {
            if(localAutoState<4)
                localAutoState++;
            else
                localAutoState=0;
        }
        Context.autoState=states[localAutoState];

        Tel.instance().addData("Auto Wait Time", Context.autoWaitTime, 1);
        Tel.instance().addData("Auto State Thing", Context.autoState, 1);
    }

    public enum CyclePixelCount
    {
        ZERO, TWO, FOUR, FIVE, SIX
    }
}

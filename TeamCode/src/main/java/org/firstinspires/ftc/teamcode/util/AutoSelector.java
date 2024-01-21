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

    public static void destroySelectorInstance()
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

        if(swapLeft.wasJustPressed()){}
            //do something to manipulate state
        if (swapRight.wasJustPressed()) {}
            //do something to manipulate state

        Context.tel.addData("Auto Wait Time", Context.autoWaitTime);
        Context.tel.addData("Auto State Thing", Context.autoState);
    }

    public enum Auto_State
    {
        TEMP
        //idk whatever u want lol
    }
}

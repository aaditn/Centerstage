package org.firstinspires.ftc.teamcode.modules.moduleUtil;

import org.firstinspires.ftc.teamcode.util.Context;

import java.util.HashMap;

public class ModuleStateConverter
{
    HashMap<ModuleState, Double> key;
    static ModuleStateConverter converter;
    public ModuleStateConverter()
    {
        key=new HashMap<>();
    }

    public static ModuleStateConverter getInstance()
    {
        if(converter==null)
            converter=new ModuleStateConverter();
        return converter;
    }

    public static void deleteInstance()
    {
        converter=null;
    }

    public void add(ModuleState[] states, double[] values)
    {
        if(states.length!=values.length)
        {
            Context.statusError="Failed to map states to values";
        }
        else
        {
            for(int i=0; i<states.length; i++)
            {
                key.put(states[i], values[i]);
            }
        }
    }
    public double getOutput(ModuleState m)
    {
        return key.get(m);
    }
}

package org.firstinspires.ftc.teamcode.modules.moduleUtil;

public interface ModuleState
{
    //leave overload index for if there is an array to each state(ie multiple servo positions
    abstract double getOutput(int... index);
}

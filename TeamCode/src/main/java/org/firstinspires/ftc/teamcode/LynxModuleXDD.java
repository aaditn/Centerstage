package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.util.Tel;

import java.util.ArrayList;
import java.util.List;
@TeleOp

public class LynxModuleXDD extends EnhancedOpMode
{
    List<LynxModule> modules;
    @Override
    public void linearOpMode() {

    }

    @Override
    public void initialize() {
        modules=new ArrayList<>();



        for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            modules.add(module);
        }
    }

    public void initLoop()
    {
        for(LynxModule module:modules)
        {
            Tel.instance().addData(module.getDeviceName(), module.getImuType());
        }
        Tel.instance().update();

    }

    @Override
    public void primaryLoop() {

    }
}

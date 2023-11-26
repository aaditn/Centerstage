package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;

public class Hang extends Module
{
    DcMotor hang;

    public enum HangState implements ModuleState
    {
        INIT(0, 0), EXTENDED(1000, 1), PULLED_UP(500, -1);

        double position, power;
        HangState(double position, double power)
        {
            this.position=position;
            this.power=power;
        }
        @Override
        public double getOutput(int... index) {
            return 0;
        }
    }
    public Hang(HardwareMap hardwareMap)
    {
        super(false);
        hang=hardwareMap.get(DcMotor.class, "hang");
    }

    @Override
    protected void write()
    {

    }

    @Override
    protected void internalUpdate() {

    }

    @Override
    protected void initInternalStates() {

    }

    @Override
    protected void updateInternalStatus() {

    }
}

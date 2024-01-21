package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
@Config
public class NavxWrapper
{
    AHRS navx;
    double cachedYaw;
    private final byte NAVX_DEVICE_UPDATE_RATE_HZ = 100;
    public NavxWrapper(HardwareMap hardwareMap)
    {
        navx = AHRS.getInstance(hardwareMap.get(NavxMicroNavigationSensor.class, "navx"),
                AHRS.DeviceDataType.kProcessedData,
                NAVX_DEVICE_UPDATE_RATE_HZ);
    }

    public double getHeading()
    {
        return cachedYaw;
    }
    public void update()
    {
        cachedYaw=Math.toRadians(-navx.getYaw());
    }

    public double getVelocity()
    {
        //TODO
        return 0;
    }
}

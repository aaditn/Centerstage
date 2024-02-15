package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.List;

@Config
public class NavxWrapper
{
    AHRS navx;
    double cachedYaw, cachedVelocity;
    private final byte NAVX_DEVICE_UPDATE_RATE_HZ = 100;
    private List<Double> headingTrack = new ArrayList<>();
    ElapsedTime x;

    public NavxWrapper(HardwareMap hardwareMap)
    {
        navx = AHRS.getInstance(hardwareMap.get(NavxMicroNavigationSensor.class, "navx"),
                AHRS.DeviceDataType.kProcessedData,
                NAVX_DEVICE_UPDATE_RATE_HZ);
        x = new ElapsedTime();

    }

    public double getHeading()
    {
        return cachedYaw;
    }
    public void update()
    {
        cachedYaw=Math.toRadians(-navx.getYaw());

        headingTrack.add(Math.toRadians(-navx.getYaw()));
        if(headingTrack.size()>1){
            double k = headingTrack.get(0);
            headingTrack.remove(0);
            x.reset();
             cachedVelocity = (k-headingTrack.get(0))/x.seconds();
        }
        x.reset();
    }

    public double getVelocity()
    {
        return cachedVelocity;
    }
    public void close()
    {
        navx.close();
    }
}
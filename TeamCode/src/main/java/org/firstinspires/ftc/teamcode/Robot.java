package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.util.Context;

import java.util.List;

import javax.annotation.concurrent.GuardedBy;

public class Robot
{
    LinearOpMode l;
    HardwareMap hardwareMap;
    Telemetry tel;

    private final Object imuLock = new Object();
    @GuardedBy("imuLock")
    public BNO055IMU imu;
    private Thread imuThread;
    private double imuAngle = 0;
    private double imuOffset = 0;
    List<LynxModule> modules;
    List<Module> hardwareModules;

    public Robot(LinearOpMode l)
    {
        this.l=l;
        tel = new MultipleTelemetry(l.telemetry, FtcDashboard.getInstance().getTelemetry());
        Context.updateValues();

        hardwareMap=l.hardwareMap;
        modules=hardwareMap.getAll(LynxModule.class);

        synchronized (imuLock) {
            imu = hardwareMap.get(BNO055IMU.class, "imu");
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
            imu.initialize(parameters);
        }

    }



    public void startImuThread()
    {
        imuThread = new Thread(() -> {
            while (!l.isStopRequested() && l.opModeIsActive()) {
                synchronized (imuLock) {
                    imuAngle = imu.getAngularOrientation().firstAngle;
                }
            }
        });
        imuThread.start();
    }

    public void initLoop()
    {
        tel.update();

        //loop whatever else u want
    }
    public void primaryLoop()
    {
        tel.update();
        read();
        write();
        //loop whatever else u want
    }

    public void read()
    {
        for(Module m: hardwareModules)
        {
            m.updateLoop();
        }
    }

    public void write()
    {
        for(Module m: hardwareModules)
        {
            m.writeLoop();
        }
    }

    public void onEnd()
    {
        Context.resetValues();
    }

    public double getBatteryVoltage()
    {
        double result = Double.POSITIVE_INFINITY;
        for (VoltageSensor sensor : hardwareMap.voltageSensor)
        {
            double voltage = sensor.getVoltage();
            if (voltage > 0)
            {
                result = Math.min(result, voltage);
            }
        }
        return result;
    }

    public double getCurrent()
    {
        double current=0;
        for(LynxModule l: modules)
        {
            current+=l.getCurrent(CurrentUnit.AMPS);
        }
        return current;
    }
}

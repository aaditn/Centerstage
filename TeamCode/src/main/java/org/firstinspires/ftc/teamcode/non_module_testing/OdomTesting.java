package org.firstinspires.ftc.teamcode.non_module_testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.ftc.Encoder;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.util.Tel;

@TeleOp
public class OdomTesting extends EnhancedOpMode
{
    Encoder frontRightEncoder, backLeftEncoder, backRightEncoder, frontLeftEncoder;

    MultipleTelemetry tel;
    @Override
    public void linearOpMode()
    {
        waitForStart();
        while(opModeIsActive()){}
    }

    @Override
    public void initialize()
    {
        tel = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        Context.resetValues();
        Context.tel=tel;
        frontRightEncoder = hardwareMap.get(Encoder.class, "fr");
        frontLeftEncoder = hardwareMap.get(Encoder.class, "fl");
        backRightEncoder = hardwareMap.get(Encoder.class, "br");
        backLeftEncoder = hardwareMap.get(Encoder.class, "bl");

    }

    @Override
    public void primaryLoop()
    {
        Tel.instance().addData("front right", frontRightEncoder.getPositionAndVelocity().position);
        Tel.instance().addData("back left", backLeftEncoder.getPositionAndVelocity().position);
        Tel.instance().addData("back right", backRightEncoder.getPositionAndVelocity().position);
        Tel.instance().addData("front left", frontLeftEncoder.getPositionAndVelocity().position);
        Tel.instance().update();
    }
}

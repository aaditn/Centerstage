package org.firstinspires.ftc.teamcode.non_module_testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

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
        frontRightEncoder = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "fr")));
        frontLeftEncoder = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "fl")));
        backRightEncoder = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "br")));
        backLeftEncoder = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "bl")));

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

package org.firstinspires.ftc.teamcode.non_module_testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.roadrunner.util.Encoder;
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
        frontRightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "fr"));
        backLeftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "bl"));
        backRightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "br"));
        frontLeftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "fl"));

    }

    @Override
    public void primaryLoop()
    {
        Tel.instance().addData("front right", frontRightEncoder.getCurrentPosition());
        Tel.instance().addData("back left", backLeftEncoder.getCurrentPosition());
        Tel.instance().addData("back right", backRightEncoder.getCurrentPosition());
        Tel.instance().addData("front left", frontLeftEncoder.getCurrentPosition());
        Tel.instance().update();
    }
}

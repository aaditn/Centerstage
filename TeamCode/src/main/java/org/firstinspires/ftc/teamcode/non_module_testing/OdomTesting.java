package org.firstinspires.ftc.teamcode.non_module_testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.roadrunner.util.Encoder;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

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
        Context.tel.addData("perpendicular encoder", frontRightEncoder.getCurrentPosition());
        Context.tel.addData("front left", frontLeftEncoder.getCurrentPosition());
        Context.tel.addData("back right", backRightEncoder.getCurrentPosition());
        Context.tel.addData("parallel encoder", frontLeftEncoder.getCurrentPosition());
        Context.tel.update();
    }
}

package org.firstinspires.ftc.teamcode.non_module_testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.roadrunner.util.Encoder;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

@TeleOp
public class OdomTesting extends EnhancedOpMode
{
    Encoder frontRightEncoder, backLeftEncoder, backRightEncoder, frontLeftEncoder;

    @Override
    public void linearOpMode()
    {
        waitForStart();
        while(opModeIsActive()){}
    }

    @Override
    public void initialize()
    {
        frontRightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "fr"));
        backLeftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "bl"));
        backRightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "br"));
        frontLeftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "fl"));

    }

    @Override
    public void primaryLoop()
    {
        telemetry.addData("perpendicular encoder", frontRightEncoder.getCurrentPosition());
        //telemetry.addData("front left", frontLeftEncoder.getCurrentPosition());
        //telemetry.addData("back right", backRightEncoder.getCurrentPosition());
        telemetry.addData("parallel encoder", frontLeftEncoder.getCurrentPosition());
        telemetry.update();
    }
}

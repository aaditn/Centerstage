package org.firstinspires.ftc.teamcode.non_module_testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "slidesTest")

public class slidesTest extends LinearOpMode{
    public int liftPos = 0;
    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor left = hardwareMap.get(DcMotor.class, "slide1");
        DcMotor right = hardwareMap.get(DcMotor.class, "slide2");
        left.setDirection(DcMotorSimple.Direction.REVERSE);

        left.setTargetPosition(liftPos);
        right.setTargetPosition(liftPos);
        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftPos = left.getCurrentPosition();
        waitForStart();
        while (opModeIsActive()){


            if(gamepad1.a){
                liftPos += 5;
            }
            if(gamepad1.b){
                liftPos -= 5;
            }

            left.setTargetPosition(liftPos);
            right.setTargetPosition(liftPos);
            left.setPower(1);
            right.setPower(1);

            telemetry.addData("liftPos", liftPos);
            telemetry.addData("currentPos", left.getCurrentPosition());

            telemetry.update();
        }
    }
}

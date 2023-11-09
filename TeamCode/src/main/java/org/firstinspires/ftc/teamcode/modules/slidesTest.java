package org.firstinspires.ftc.teamcode.modules;

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

        DcMotor left = hardwareMap.get(DcMotor.class, "left");
        DcMotor right = hardwareMap.get(DcMotor.class, "right");
        //left.setDirection(DcMotorSimple.Direction.REVERSE);

        left.setTargetPosition(liftPos);
        right.setTargetPosition(liftPos);
        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        waitForStart();
        while (opModeIsActive()){
            liftPos = left.getCurrentPosition();

            if(gamepad1.a){
                liftPos += 1;
            }
            if(gamepad1.b){
                liftPos -= 1;
            }

            left.setTargetPosition(liftPos);
            right.setTargetPosition(liftPos);
            left.setPower(0.1);
            right.setPower(0.1);

            telemetry.addData("liftPos", liftPos);
            telemetry.addData("currentPos", left.getCurrentPosition());

            telemetry.update();
        }
    }
}

package org.firstinspires.ftc.teamcode.non_module_testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@Config
@TeleOp(name = "slidesTest")
public class slidesTest extends LinearOpMode{
    public static int liftPos = 0;
    public static double motorPower=0.01;
    @Override
    public void runOpMode() throws InterruptedException {

        DcMotorEx left = hardwareMap.get(DcMotorEx.class, "slide1");
        DcMotorEx right = hardwareMap.get(DcMotorEx.class, "slide2");
        right.setDirection(DcMotorSimple.Direction.REVERSE);

        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        /*left.setTargetPosition(liftPos);
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
            telemetry.addData("currentPos", left.getCurrent(CurrentUnit.AMPS));

            telemetry.update();
        }*/
        while(opModeInInit())
        {
            telemetry.addData("Current Pos", left.getCurrentPosition());
            telemetry.update();
        }

        waitForStart();
        while (opModeIsActive())
        {
            left.setPower(motorPower);
            right.setPower(motorPower);

            telemetry.addData("Current Pos", left.getCurrentPosition());
            telemetry.addData("Current Power", motorPower);
            telemetry.update();
        }
    }
}

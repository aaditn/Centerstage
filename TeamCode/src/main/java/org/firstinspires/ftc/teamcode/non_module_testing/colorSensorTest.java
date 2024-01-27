package org.firstinspires.ftc.teamcode.non_module_testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp
public class colorSensorTest extends LinearOpMode {

    ColorRangeSensor cs3;
    ColorRangeSensor cs2;
    ColorRangeSensor cs1;

    public void runOpMode() {
        cs3 = hardwareMap.get(ColorRangeSensor.class, "cs3");

        cs2 = hardwareMap.get(ColorRangeSensor.class, "cs2");

        cs1 = hardwareMap.get(ColorRangeSensor.class, "cs1");

        int noPixels = 0;
        int onePixel = 0;
        int twoPixels = 0;
        waitForStart();
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        while (opModeIsActive()) {
            while (timer.milliseconds() < 3000) {
                if (isPixel(cs1) && isPixel(cs2)) {
                    twoPixels++;
                } else if (isPixel(cs1) && !isPixel(cs2)) {
                    onePixel++;
                } else {
                    noPixels++;
                }
                telemetry.addData("cs1_dist", cs1.getDistance(DistanceUnit.MM));
                telemetry.addData("cs1_alpha", cs1.alpha());

                telemetry.addData("cs2_dist", cs2.getDistance(DistanceUnit.MM));
                telemetry.addData("cs2_alpha", cs2.alpha());

                telemetry.addData("cs3_dist", cs3.getDistance(DistanceUnit.MM));
                telemetry.addData("cs3_alpha", cs3.alpha());
                telemetry.update();
            }
            if (twoPixels > onePixel && twoPixels > noPixels) {
                telemetry.addData("sweeper", 0);
            } else if (onePixel > twoPixels && onePixel > noPixels) {
                telemetry.addData("sweeper", 1);
            } else if (noPixels > onePixel && noPixels > twoPixels) {
                telemetry.addData("sweeper", 2);
            }
            telemetry.update();
        }



    }
    public boolean isPixel(ColorRangeSensor sensor) {
        int distance = 75;
        int alpha = 500;
        if (sensor.getDistance(DistanceUnit.MM) < distance && sensor.alpha() > alpha ) {
            return true;
        }
        return false;
    }
}

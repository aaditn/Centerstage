
package org.firstinspires.ftc.teamcode.non_module_testing;


import static org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.MM;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp
public class rumbleImplement extends LinearOpMode {

    ColorRangeSensor sensor1, sensor2;
    String output;
    ElapsedTime timer1 = new ElapsedTime(), timer2 = new ElapsedTime();


    @Override
    public void runOpMode() throws InterruptedException {
        sensor1 = hardwareMap.get(ColorRangeSensor.class, "s1");
        sensor2 = hardwareMap.get(ColorRangeSensor.class, "s2");

        boolean pixel1 = false;
        boolean pixel2 = false;

        boolean lastPixel1 = false;
        boolean lastPixel2 = false;

        int s1Alpha = 1500;
        int s1Dist = 75;
        int s2Alpha = 1500;
        int s2Dist = 75;

        Gamepad.RumbleEffect effect1 = new Gamepad.RumbleEffect.Builder()
                .addStep(1.0, 1.0, 500)
                .build();

        Gamepad.RumbleEffect effectBoth = new Gamepad.RumbleEffect.Builder()
                .addStep(1.0, 1.0, 300)
                .addStep(0.0, 0.0, 200)
                .addStep(1.0, 1.0, 300)
                .build();

        waitForStart();
        while (opModeIsActive()) {

            pixel1 = sensor1.getDistance(MM) < s1Dist && sensor1.alpha() > s1Alpha;
            pixel2 = sensor2.getDistance(MM) < s2Dist && sensor2.alpha() > s2Alpha;

            if (lastPixel1 && pixel1) { // if there is already a pixel in first
                telemetry.addData("hehehe", "hehehehe");
                if (!lastPixel2 && pixel2) { // if a pixel is added in 2
                    telemetry.addData("hahahaha", "hahahahaha");
                    gamepad1.runRumbleEffect(effectBoth);
                }
            } else if (!lastPixel1 && pixel1) { // if pixel is just added in first
                gamepad1.runRumbleEffect(effect1);
            }

            lastPixel1 = pixel1;
            lastPixel2 = pixel2;

            telemetry.addData("distance1", sensor1.getDistance(MM));
            telemetry.addData("alpha1", sensor1.alpha());
            telemetry.addData("distance2", sensor2.getDistance(MM));
            telemetry.addData("alpha2", sensor2.alpha());
            telemetry.addData("firstPixel:", pixel1);
            telemetry.addData("secondPixel:", pixel2);
            telemetry.update();
        }
    }
}
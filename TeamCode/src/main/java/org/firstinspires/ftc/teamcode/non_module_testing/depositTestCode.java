package org.firstinspires.ftc.teamcode.non_module_testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
@Config
public class depositTestCode extends LinearOpMode {
    Servo pinion;
    public static double reset = 0.1;
    public static double first = .22;
    public static double second = .26;
    public void runOpMode() {
        pinion = hardwareMap.get(Servo.class, "pinion");

        FtcDashboard dashboard = FtcDashboard.getInstance();

        TelemetryPacket packet = new TelemetryPacket();

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                pinion.setPosition(reset);
            }
            if (gamepad1.b) {
                pinion.setPosition(first);
            }
            if (gamepad1.x) {
                pinion.setPosition(second);
            }
            packet.put("reset", reset);
            packet.put("first", first);
            packet.put("second", second);
            dashboard.sendTelemetryPacket(packet);
        }

    }


}

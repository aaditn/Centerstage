package org.firstinspires.ftc.teamcode.non_module_testing;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
@Config
public class IntakeJarrettNoModule extends LinearOpMode {
// s1: 0.20, 0.6
// s2: 0.34, 0.67
    public static double s1down = 0.6;
    public static double s1Mid = 0.37;
    public static double s1Up = 0;


    public static double s2down = 0.34;
    public static double s2Mid = 0.5;
    public static double s2Up = 0.67;

    @Override
    public void runOpMode() throws InterruptedException {

        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        Servo s1 = hardwareMap.get(Servo.class, "s1");
        Servo s2 = hardwareMap.get(Servo.class, "s2");

        FtcDashboard dashboard = FtcDashboard.getInstance();

        TelemetryPacket packet = new TelemetryPacket();

        waitForStart();

        while (opModeIsActive()) {
            intakeMotor.setPower(gamepad1.right_trigger);
            if (gamepad1.a) {
                s1.setPosition(s1Up);
                s2.setPosition(s2Up);
            }
            if (gamepad1.y) {
                s1.setPosition(s1Mid);
                s2.setPosition(s2Mid);
            }
            if (gamepad1.b) {
                s1.setPosition(s1down);
                s2.setPosition(s2down);
            }

            packet.put("s1 pos", s1Mid);
            packet.put("s2 pos", s2Mid);
            packet.put("s1 pos", s1down);
            packet.put("s2 pos", s2down);
            packet.put("s1 pos", s1Up);
            packet.put("s2 pos", s2Up);
            dashboard.sendTelemetryPacket(packet);
        }







    }




}

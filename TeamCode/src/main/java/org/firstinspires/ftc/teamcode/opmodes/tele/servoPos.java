package org.firstinspires.ftc.teamcode.opmodes.tele;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

@TeleOp(name = "A - servo LMAO")
@Config
public class servoPos extends EnhancedOpMode
{
    @Override
    public void linearOpMode() {

        Servo s = hardwareMap.get(Servo.class, "servo");
        double[] pos = new double[3]; // initialize ur actual positions here
        int index = 0; // go through each position in pos
        boolean isA = false; // used for not registering more than once when it is already held down

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a && !isA) {
                s.setPosition(index);
                if (index == pos.length - 1) {
                    index = 0;
                } else {
                    index++;
                }
            }
            isA = gamepad1.a;
        }

    }

    @Override
    public void initialize() {

    }
    public void initLoop()
    {
    }

    @Override
    public void primaryLoop()
    {
    }
}

//flick
//heights


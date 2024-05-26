package org.firstinspires.ftc.teamcode.non_module_testing;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.util.Tel;

/**
 * This is a simple teleop routine for testing localization. Drive the robot around like a normal
 * teleop routine and make sure the robot's estimated pose matches the robot's actual pose (slight
 * errors are not out of the ordinary, especially with sudden drive motions). The goal of this
 * exercise is to ascertain whether the localizer has been configured properly (note: the pure
 * encoder localizer heading may be significantly off if the track width has not been tuned).
 */
@Config
@TeleOp
public class accelTest extends LinearOpMode {
    public static final double SECONDS = 3;
    public static final boolean forwardTest = true;
    PoseVelocity2d velo = new PoseVelocity2d(new Vector2d(0,0), 0);
    PoseVelocity2d lastVelo = new PoseVelocity2d(new Vector2d(0,0), 0);
    PoseVelocity2d accel = new PoseVelocity2d(new Vector2d(0,0), 0);
    double lastTime = 0;
    @Override
    public void runOpMode() throws InterruptedException {

        Robot drive = Robot.getInstance();

        waitForStart();

        ElapsedTime timer = new ElapsedTime();

        if (forwardTest) {
            drive.setLocalDrivePowers(new Pose2d(1,0,0));
        } else {
            drive.setLocalDrivePowers(new Pose2d(0,1,0));
        }
        while (!isStopRequested()) {
            if (timer.seconds() > SECONDS) {
                drive.setLocalDrivePowers(new Pose2d(0,0,0));
            }
            drive.primaryLoop();
            drive.update();
            velo = drive.updatePoseEstimate();

            accel = velo.minus(lastVelo);
            Vector2d c = accel.component1();
            double h = accel.component2();
            c = c.div(timer.seconds() - lastTime);
            h = h / (timer.seconds() - lastTime);
            accel = new PoseVelocity2d(c, h);

            Tel.instance().addData("velocity", velo);
            Tel.instance().addData("velocity magnitude", getMag(velo));
            Tel.instance().addData("acceleration pose", accel);
            Tel.instance().addData("acceleration magnitude", getMag(accel));
            Tel.instance().addData("time", timer.seconds());


            lastVelo = velo;
            lastTime = timer.seconds();
        }


    }

    public double getMag(PoseVelocity2d v) {
        return Math.sqrt(Math.pow(v.linearVel.y, 2) + Math.pow(v.linearVel.x, 2));
    }

}

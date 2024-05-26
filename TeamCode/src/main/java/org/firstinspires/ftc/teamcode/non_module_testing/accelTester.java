package org.firstinspires.ftc.teamcode.non_module_testing;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.util.Tel;

@TeleOp(name="accel tester")
@Config
public class accelTester extends EnhancedOpMode
{
    public static double SECONDS = 2.5;
    public static boolean forwardTest = true;
    PoseVelocity2d velo = new PoseVelocity2d(new Vector2d(0,0), 0);
    PoseVelocity2d lastVelo = new PoseVelocity2d(new Vector2d(0,0), 0);
    PoseVelocity2d accel = new PoseVelocity2d(new Vector2d(0,0), 0);
    int current100Interval = 0;
    Robot drive;
    double lastTime = 0;
    @Override
    public void linearOpMode()
    {

        waitForStart();

        ElapsedTime timer = new ElapsedTime();

        while(opModeIsActive())
        {
            if (timer.seconds() > 5) {
                if (forwardTest) {
                    drive.setLocalDrivePowers(new Pose2d(1,0,0));
                } else {
                    drive.setLocalDrivePowers(new Pose2d(0,1,0));
                }
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

            if ((int)(timer.milliseconds() / 500) != current100Interval) {
                current100Interval+=5;
                RobotLog.e("speed" + getMag(velo));
                RobotLog.e("time" + (timer.seconds() - 5));
                RobotLog.e("accel" + getMag(velo)/(timer.seconds() - 5));
            }


            if (timer.seconds() > SECONDS + 5) {
                drive.setLocalDrivePowers(new Pose2d(0,0,0));
                break;
            }

            Tel.instance().addData("velocity", velo);
            Tel.instance().addData("velocity magnitude", getMag(velo));
            Tel.instance().addData("acceleration pose", accel);
            Tel.instance().addData("acceleration magnitude", getMag(accel));
            Tel.instance().addData("time", timer.seconds());


            lastVelo = velo;
            lastTime = timer.seconds();
        }
    }

    @Override
    public void initialize()
    {
        drive = Robot.getInstance();
    }

    public double getMag(PoseVelocity2d v) {
        return Math.sqrt(Math.pow(v.linearVel.y, 2) + Math.pow(v.linearVel.x, 2));
    }
    @Override
    public void initLoop()
    {
        Tel.instance().update();
    }

    @Override
    public void primaryLoop()
    {
        Tel.instance().update();
    }
}

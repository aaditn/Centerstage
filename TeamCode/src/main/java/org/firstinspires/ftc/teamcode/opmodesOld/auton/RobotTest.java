package org.firstinspires.ftc.teamcode.opmodesOld.auton;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

public class RobotTest extends EnhancedOpMode
{
    SampleMecanumDrive m;
    @Override
    public void linearOpMode() {

        Trajectory purplePixel1 = m.trajectoryBuilder(new Pose2d(0,0,0))
                .splineToConstantHeading(new Vector2d(31.5, 31), Math.toRadians(-90),
                        m.getVelocityConstraint(30, 1, 15.06),
                        m.getAccelerationConstraint(40))
                .build();
        waitForStart();

        m.followTrajectoryAsync(purplePixel1);

        while(m.isBusy()){}

    }

    @Override
    public void initialize()
    {
        setLoopTimes(10);
        m=new SampleMecanumDrive(hardwareMap);
    }

    @Override
    public void primaryLoop()
    {
        //m.primaryLoop();
        m.update();
    }
}

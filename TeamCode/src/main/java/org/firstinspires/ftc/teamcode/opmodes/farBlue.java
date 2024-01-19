package org.firstinspires.ftc.teamcode.opmodes;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.Context;

@Autonomous
public class farBlue extends LinearOpMode {
    Pose2d blueFarStart = new Pose2d(-36,61,Math.toRadians(270));
    Robot drive;
    public void runOpMode() {
        drive = new Robot(this);

        TrajectorySequence leftPurple = drive.trajectorySequenceBuilder(blueFarStart)
                .lineToLinearHeading(new Pose2d(-36, 30, Math.toRadians(180)))
                .build();
        TrajectorySequence leftToStack0 = drive.trajectorySequenceBuilder(leftPurple.end())
                .lineTo(new Vector2d(-34, 18))
                .splineToConstantHeading(new Vector2d(-42, 12), Math.toRadians(180))
                .lineToSplineHeading(new Pose2d(-56, 12, Math.toRadians(180)))
                .build();
        TrajectorySequence leftBackFromStack = drive.trajectorySequenceBuilder(leftToStack0.end())
                .lineToConstantHeading(new Vector2d(24, 12))
                .splineToConstantHeading(new Vector2d(48, 29), Math.toRadians(0))
                .build();
        TrajectorySequence leftStackFromBack = drive.trajectorySequenceBuilder(leftBackFromStack.end())
                .splineToConstantHeading(new Vector2d(24, 12), Math.toRadians(180))
                .lineToConstantHeading(new Vector2d(-56, 12))
                .build();

        TrajectorySequence midPlacePurple = drive.trajectorySequenceBuilder(blueFarStart)
                .lineToLinearHeading(new Pose2d(-36, 14, Math.toRadians(90)))
                .build();
        TrajectorySequence midToStack0 = drive.trajectorySequenceBuilder(midPlacePurple.end())
                .lineTo(new Vector2d(-37, 13))
                .splineToConstantHeading(new Vector2d(-42, 12), Math.toRadians(180))
                .lineToSplineHeading(new Pose2d(-56, 12, Math.toRadians(180)))
                .build();

        TrajectorySequence midBackFromStack = drive.trajectorySequenceBuilder(midToStack0.end())
                .lineToConstantHeading(new Vector2d(24, 12))
                .splineToConstantHeading(new Vector2d(48, 35), Math.toRadians(0))
                .build();
        TrajectorySequence midStackFromBack = drive.trajectorySequenceBuilder(midBackFromStack.end())
                .splineToConstantHeading(new Vector2d(24, 12), Math.toRadians(180))
                .lineToConstantHeading(new Vector2d(-56, 12))
                .build();

        TrajectorySequence rightPurple = drive.trajectorySequenceBuilder(blueFarStart)
                .lineToLinearHeading(new Pose2d(-36, 30, Math.toRadians(0)))
                .build();
        TrajectorySequence rightToStack0 = drive.trajectorySequenceBuilder(rightPurple.end())
                .lineTo(new Vector2d(-40, 22))
                .splineToSplineHeading(new Pose2d(-56, 12, Math.toRadians(180)), Math.toRadians(180))
                .build();
        TrajectorySequence rightBackFromStack = drive.trajectorySequenceBuilder(rightToStack0.end())
                .lineToConstantHeading(new Vector2d(24, 12))
                .splineToConstantHeading(new Vector2d(48, 41), Math.toRadians(0))
                .build();
        TrajectorySequence rightStackFromBack = drive.trajectorySequenceBuilder(rightBackFromStack.end())
                .splineToConstantHeading(new Vector2d(24, 12), Math.toRadians(180))
                .lineToConstantHeading(new Vector2d(-56, 12))
                .build();

        waitForStart();

        drive.followTrajectorySequence(leftPurple);
        die(1000);
        drive.followTrajectorySequence(leftToStack0);
        die(1000);
        drive.followTrajectorySequence(leftBackFromStack);
        die(1000);
        drive.followTrajectorySequence(leftStackFromBack);
        die(1000);
        drive.followTrajectorySequence(leftBackFromStack);
        die(1000);
        drive.followTrajectorySequence(leftStackFromBack);
        die(1000);

    }

    public void die (int milliseconds) {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        while (timer.milliseconds() < milliseconds) {
            Context.tel.addData("in wait", timer.milliseconds());
        }
    }
}

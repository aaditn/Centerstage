package org.firstinspires.ftc.teamcode.opmodes.auto;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

public class farRed extends LinearOpMode {
    Pose2d redFarStart = new Pose2d(-36,-61,Math.toRadians(90));
    Robot drive;
    public void runOpMode() {
        drive = new Robot(this);

        TrajectorySequence leftPurple = drive.trajectorySequenceBuilder(redFarStart)
                .lineToLinearHeading(new Pose2d(-36, -30, Math.toRadians(180)))
                .build();
        TrajectorySequence leftToStack0 = drive.trajectorySequenceBuilder(leftPurple.end())
                .lineTo(new Vector2d(-34, -18))
                .splineToConstantHeading(new Vector2d(-42, -12), Math.toRadians(180))
                .lineToSplineHeading(new Pose2d(-56, -12, Math.toRadians(180)))
                .build();
        TrajectorySequence leftBackFromStack = drive.trajectorySequenceBuilder(leftToStack0.end())
                .lineToConstantHeading(new Vector2d(24, -12))
                .splineToConstantHeading(new Vector2d(48, -29), Math.toRadians(0))
                .build();
        TrajectorySequence leftStackFromBack = drive.trajectorySequenceBuilder(leftBackFromStack.end())
                .splineToConstantHeading(new Vector2d(24, -12), Math.toRadians(180))
                .lineToConstantHeading(new Vector2d(-56, -12))
                .build();

        TrajectorySequence midPurple = drive.trajectorySequenceBuilder(redFarStart)
                .lineToLinearHeading(new Pose2d(-36, -14, Math.toRadians(-90+1e-6)))
                .build();
        TrajectorySequence midToStack0 = drive.trajectorySequenceBuilder(midPurple.end())
                .lineTo(new Vector2d(-37, -13))
                .splineToConstantHeading(new Vector2d(-42, -12), Math.toRadians(180))
                .lineToSplineHeading(new Pose2d(-56, -12, Math.toRadians(180)))
                .build();
        TrajectorySequence midBackFromStack = drive.trajectorySequenceBuilder(midToStack0.end())
                .lineToConstantHeading(new Vector2d(24, -12))
                .splineToConstantHeading(new Vector2d(48, -35), Math.toRadians(0))
                .build();
        TrajectorySequence midStackFromBack = drive.trajectorySequenceBuilder(midBackFromStack.end())
                .splineToConstantHeading(new Vector2d(24, -12), Math.toRadians(180))
                .lineToConstantHeading(new Vector2d(-56, -12))
                .build();

        TrajectorySequence rightPurple = drive.trajectorySequenceBuilder(redFarStart)
                .lineToLinearHeading(new Pose2d(-36, -30, Math.toRadians(0)))
                .build();
        TrajectorySequence rightToStack0 = drive.trajectorySequenceBuilder(rightPurple.end())
                .lineTo(new Vector2d(-40, -22))
                .splineToSplineHeading(new Pose2d(-56, -12, Math.toRadians(180)), Math.toRadians(180))
                .build();
        TrajectorySequence rightBackFromStack = drive.trajectorySequenceBuilder(rightToStack0.end())
                .lineToConstantHeading(new Vector2d(24, -12))
                .splineToConstantHeading(new Vector2d(48, -41), Math.toRadians(0))
                .build();
        TrajectorySequence rightStackFromBack = drive.trajectorySequenceBuilder(rightBackFromStack.end())
                .splineToConstantHeading(new Vector2d(24, -12), Math.toRadians(180))
                .lineToConstantHeading(new Vector2d(-56, -12))
                .build();
        waitForStart();
    }
}

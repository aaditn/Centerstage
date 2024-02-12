package com.example.meepmeeptesting.weirdStuff;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;



public class Trajectories {

    public static SampleMecanumDrive Robot = new SampleMecanumDrive();
    public static Pose2d blueFarStart = new Pose2d(-35, 61, Math.toRadians(270));

    public static TrajectorySequence leftPurple = Robot.trajectorySequenceBuilder(blueFarStart)
            .splineToConstantHeading(new Vector2d(-46.5, 34), Math.toRadians(270))
            .build();
    public static TrajectorySequence leftPurpleToBack = Robot.trajectorySequenceBuilder(leftPurple.end())
            .setReversed(true)
            .lineTo(new Vector2d(-46.5, 37))
            .splineToSplineHeading(new Pose2d(-34, 57, Math.toRadians(180)), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(20, 57))
            .splineToConstantHeading(new Vector2d(50.5, 26), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence leftBackToStack = Robot.trajectorySequenceBuilder(leftPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(10, 58), Math.toRadians(180),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(60))
            .lineToConstantHeading(new Vector2d(-28, 58))
            .splineToConstantHeading(new Vector2d(-55, 33), Math.toRadians(180),
                    Robot.getVelocityConstraint(35, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .lineToConstantHeading(new Vector2d(-58, 33),
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .build();
    public static TrajectorySequence midPurple = Robot.trajectorySequenceBuilder(blueFarStart)
            .lineTo(new Vector2d(-36, 34))
            .build();
    public static TrajectorySequence midPurpleToBack = Robot.trajectorySequenceBuilder(midPurple.end())
            .lineTo(new Vector2d(-36, 35))
            .splineToSplineHeading(new Pose2d(-29, 57, Math.toRadians(180)), Math.toRadians(0),
                    Robot.getVelocityConstraint(50, 2.4, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(20, 57))
            .splineToConstantHeading(new Vector2d(49.5, 31), Math.toRadians(0),
                    Robot.getVelocityConstraint(50, 2.4, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence midBackToStack = Robot.trajectorySequenceBuilder(midPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(10, 58), Math.toRadians(180),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(60))
            .lineToConstantHeading(new Vector2d(-28, 58))
            .splineToConstantHeading(new Vector2d(-55, 33), Math.toRadians(180),
                    Robot.getVelocityConstraint(35, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .lineToConstantHeading(new Vector2d(-58, 33),
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .build();
    public static TrajectorySequence rightPurple = Robot.trajectorySequenceBuilder(blueFarStart)
            .lineTo(new Vector2d(-36, 45),
                    Robot.getVelocityConstraint(40, 2.4, 15.06),
                    Robot.getAccelerationConstraint(35))
            .splineToSplineHeading(new Pose2d(-31, 36, Math.toRadians(-45)), Math.toRadians(-45),
                    Robot.getVelocityConstraint(40, 2.4, 15.06),
                    Robot.getAccelerationConstraint(35))
            .build();
    public static TrajectorySequence rightPurpleToBack = Robot.trajectorySequenceBuilder(rightPurple.end())
            .lineTo(new Vector2d(-32, 37),
                    Robot.getVelocityConstraint(40, 2.4, 15.06),
                    Robot.getAccelerationConstraint(35))
            .splineToSplineHeading(new Pose2d(-32, 59, Math.toRadians(180)), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2.4, 15.06),
                    Robot.getAccelerationConstraint(35))
            .lineToConstantHeading(new Vector2d(20, 59))
            .splineToConstantHeading(new Vector2d(50, 38), Math.toRadians(0))
            .build();
    public static TrajectorySequence rightBackToStack = Robot.trajectorySequenceBuilder(rightPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(10, 58), Math.toRadians(180),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(60))
            .lineToConstantHeading(new Vector2d(-28, 58))
            .splineToConstantHeading(new Vector2d(-55, 33), Math.toRadians(180),
                    Robot.getVelocityConstraint(35, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .lineToConstantHeading(new Vector2d(-58, 33),
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .build();

    public static TrajectorySequence stackToBack1 = Robot.trajectorySequenceBuilder(leftBackToStack.end())
            .setReversed(true)
            .splineToConstantHeading(new Vector2d(-36, 57), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(49, 57),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence backToStack1 = Robot.trajectorySequenceBuilder(stackToBack1.end())

            .setReversed(false)
            .lineToConstantHeading(new Vector2d(-28, 57))
            .splineToConstantHeading(new Vector2d(-53, 33), Math.toRadians(180),
                    Robot.getVelocityConstraint(35, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .lineToConstantHeading(new Vector2d(-57.5, 33),
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .build();
    public static TrajectorySequence stackToBack2 = Robot.trajectorySequenceBuilder(backToStack1.end())
            .setReversed(true)
            .splineToConstantHeading(new Vector2d(-36, 57), Math.toRadians(0),
                    Robot.getVelocityConstraint(35, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(49, 57),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();

    public static TrajectorySequence[] left = {leftPurple, leftPurpleToBack, leftBackToStack, stackToBack1, backToStack1, stackToBack2};





}

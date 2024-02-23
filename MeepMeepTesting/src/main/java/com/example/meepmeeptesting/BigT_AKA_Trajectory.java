package com.example.meepmeeptesting;

import static com.example.meepmeeptesting.notWeirdStuff.NamedTrajectory.map;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.example.meepmeeptesting.notWeirdStuff.NamedTrajectory;
import com.example.meepmeeptesting.notWeirdStuff.Paths;
import com.example.meepmeeptesting.notWeirdStuff.Robot;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;

public class BigT_AKA_Trajectory {
    public static com.example.meepmeeptesting.notWeirdStuff.Robot Robot = new Robot();


    // please don't delete me,
    // I am here to serve the purpose of telling you don't delete me,
    // I am here to serve the purpose of telling you don't delete me,
    // I am here to serve the purpose of telling you don't delete me,
    // ehh im gonna loop it
    // while (true) {
    //     I am here to serve the purpose of telling you don't delete me,
    // }

    public static Pose2d redFarStart = new Pose2d(-32.5, -61, Math.toRadians(90)); // Flipped starting pose
    public static TrajectorySequence leftPurple = Robot.trajectorySequenceBuilder(redFarStart)
            .splineToConstantHeading(new Vector2d(-46.5, -34), Math.toRadians(90)) // Flipped waypoint
            .build();

    public static TrajectorySequence leftPurpleToBack = Robot.trajectorySequenceBuilder(leftPurple.end())
            .setReversed(true)
            .lineTo(new Vector2d(-46.5, -37)) // Flipped waypoint
            .splineToSplineHeading(new Pose2d(-34, -57, Math.toRadians(180)), Math.toRadians(0), // Flipped pose and heading
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(20, -57),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))// Flipped waypoint
            .splineToConstantHeading(new Vector2d(42.5, -26), Math.toRadians(0), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(50.5, -26),
                    Robot.getVelocityConstraint(15, 2, 15.06),
                    Robot.getAccelerationConstraint(20))

            .build();

    public static TrajectorySequence leftBackToStack = Robot.trajectorySequenceBuilder(leftPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(10, -58), Math.toRadians(180), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(60))
            .lineToConstantHeading(new Vector2d(-28, -58),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))// Flipped waypoint
            .splineToConstantHeading(new Vector2d(-55, -33), Math.toRadians(180), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(35, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .lineToConstantHeading(new Vector2d(-58, -33), // Flipped waypoint
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .build();

    public static TrajectorySequence midPurple = Robot.trajectorySequenceBuilder(redFarStart)
            .lineTo(new Vector2d(-35, -34)) // Flipped waypoint
            .build();

    public static TrajectorySequence midPurpleToBack = Robot.trajectorySequenceBuilder(midPurple.end())
            .lineTo(new Vector2d(-35, -35)) // Flipped waypoint
            .splineToSplineHeading(new Pose2d(-29, -57, Math.toRadians(180)), Math.toRadians(0), // Flipped pose and heading
                    Robot.getVelocityConstraint(50, 2.4, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(20, -57)) // Flipped waypoint
            .splineToConstantHeading(new Vector2d(42.5, -31), Math.toRadians(0), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(50.5, -31),
                    Robot.getVelocityConstraint(15, 2, 15.06),
                    Robot.getAccelerationConstraint(20))
            .build();

    public static TrajectorySequence midBackToStack = Robot.trajectorySequenceBuilder(midPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(10, -58), Math.toRadians(180), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(60))
            .lineToConstantHeading(new Vector2d(-28, -58),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))// Flipped waypoint
            .splineToConstantHeading(new Vector2d(-55, -33), Math.toRadians(180), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(35, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .lineToConstantHeading(new Vector2d(-58, -33), // Flipped waypoint
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .build();

    public static TrajectorySequence rightPurple = Robot.trajectorySequenceBuilder(redFarStart)
            .lineTo(new Vector2d(-35, -45), // Flipped waypoint
                    Robot.getVelocityConstraint(40, 1, 15.06),
                    Robot.getAccelerationConstraint(35))
            .splineToSplineHeading(new Pose2d(-31, -35, Math.toRadians(45)), Math.toRadians(45), // Flipped pose and heading
                    Robot.getVelocityConstraint(30, 1, 15.06),
                    Robot.getAccelerationConstraint(25))
            .build();

    public static TrajectorySequence rightPurpleToBack = Robot.trajectorySequenceBuilder(rightPurple.end())
            .lineTo(new Vector2d(-32, -37), // Flipped waypoint
                    Robot.getVelocityConstraint(40, 2.4, 15.06),
                    Robot.getAccelerationConstraint(35))
            .splineToSplineHeading(new Pose2d(-40, -57, Math.toRadians(180)), Math.toRadians(0), // Flipped pose and heading
                    Robot.getVelocityConstraint(40, 2.4, 15.06),
                    Robot.getAccelerationConstraint(35))
            .splineToConstantHeading(new Vector2d(20, -59), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))/// Flipped waypoint
            .splineToConstantHeading(new Vector2d(42.5, -42.5), Math.toRadians(0), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(50.5, -42.5),
                    Robot.getVelocityConstraint(15, 2, 15.06),
                    Robot.getAccelerationConstraint(20)) // Flipped waypoint
            .build();

    public static TrajectorySequence rightBackToStack = Robot.trajectorySequenceBuilder(rightPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(10, -58), Math.toRadians(180), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(60))
            .lineToConstantHeading(new Vector2d(-28, -58),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))/// Flipped waypoint
            .splineToConstantHeading(new Vector2d(-45, -33), Math.toRadians(180), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(35, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .lineToConstantHeading(new Vector2d(-52.5, -33), // Flipped waypoint
                    Robot.getVelocityConstraint(25, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .build();

    public static TrajectorySequence stackToBack1 = Robot.trajectorySequenceBuilder(leftBackToStack.end())
            .setReversed(true)
            .lineTo(new Vector2d(-50, -33))
            .splineToConstantHeading(new Vector2d(-35, -57), Math.toRadians(0), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(20, -57), // Flipped waypoint
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .splineToConstantHeading(new Vector2d(49, -44), Math.toRadians(0), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(35, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineTo(new Vector2d(51, -44)) // Flipped waypoint
            .build();

    public static TrajectorySequence backToStack1 = Robot.trajectorySequenceBuilder(stackToBack1.end())

            .setReversed(false)
            .splineToConstantHeading(new Vector2d(25, -57), Math.toRadians(180), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(45))
            .lineToConstantHeading(new Vector2d(-28, -57),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))/// Flipped waypoint
            .splineToConstantHeading(new Vector2d(-45, -33), Math.toRadians(180), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(35, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .lineToConstantHeading(new Vector2d(-52.5, -33), // Flipped waypoint
                    Robot.getVelocityConstraint(25, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .build();

    public static TrajectorySequence stackToBack2 = Robot.trajectorySequenceBuilder(backToStack1.end())
            .setReversed(true)
            .lineTo(new Vector2d(-50, -33))
            .splineToConstantHeading(new Vector2d(-35, -57), Math.toRadians(0), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(35, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(20, -57),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))/// Flipped waypoint
            .splineToConstantHeading(new Vector2d(49, -44), Math.toRadians(0)) // Flipped waypoint
            .lineTo(new Vector2d(50.5, -44)) // Flipped waypoint
            .build();

    public static Paths[] trajectoryNames = {
            Paths.Purple,
            Paths.Score_Spike,
            Paths.Go_To_Stack,
            Paths.Score_First,
            Paths.Return_to_Stack,
            Paths.Score_Second,
    };


    public static NamedTrajectory[] leftTrajectories = map(
            new TrajectorySequence[]{leftPurple, leftPurpleToBack, leftBackToStack, stackToBack1, backToStack1, stackToBack2},
            trajectoryNames);
    public static NamedTrajectory[] midTrajectories = map(
            new TrajectorySequence[]{midPurple, midPurpleToBack, midBackToStack, stackToBack1, backToStack1, stackToBack2},
            trajectoryNames);
    public static NamedTrajectory[] rightTrajectories = map(
            new TrajectorySequence[]{rightPurple, rightPurpleToBack, rightBackToStack, stackToBack1, backToStack1, stackToBack2},
            trajectoryNames);
    public static NamedTrajectory[][] trajectories = {leftTrajectories,midTrajectories,rightTrajectories};
}

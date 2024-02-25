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
    public static Pose2d blueFarStart = new Pose2d(-35, 61, Math.toRadians(270)); // Flipped starting pose

    public static TrajectorySequence leftPurple = Robot.trajectorySequenceBuilder(blueFarStart)
//            .lineTo(new Vector2d(-35, -45), // Flipped waypoint
//                    Robot.getVelocityConstraint(40, 1, 15.06),
//                    Robot.getAccelerationConstraint(35))
            .splineToSplineHeading(new Pose2d(-35.5, 35, Math.toRadians(-45)), Math.toRadians(-45), // Flipped pose and heading
                    Robot.getVelocityConstraint(50, 2.4, 15.06),
                    Robot.getAccelerationConstraint(45))
            .build();

    public static TrajectorySequence leftPurpleToBack = Robot.trajectorySequenceBuilder(leftPurple.end())
            .lineTo(new Vector2d(-36, 37), // Flipped waypoint
                    Robot.getVelocityConstraint(50, 2.4, 15.06),
                    Robot.getAccelerationConstraint(45))
            .splineToSplineHeading(new Pose2d(-26, 57, Math.toRadians(180)), Math.toRadians(0), // Flipped pose and heading
                    Robot.getVelocityConstraint(45, 2.4, 15.06),
                    Robot.getAccelerationConstraint(35))
            .lineToConstantHeading(new Vector2d(10, 57), // Flipped pose and heading
                    Robot.getVelocityConstraint(50, 2.4, 15.06),
                    Robot.getAccelerationConstraint(40)) // Flipped waypoint
            .splineToConstantHeading(new Vector2d(43.5, 41), Math.toRadians(0), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(35, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .build();

    public static TrajectorySequence leftBackToStack = Robot.trajectorySequenceBuilder(leftPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(10, 58), Math.toRadians(180), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(45, 2, 15.06),
                    Robot.getAccelerationConstraint(25))
            .lineToConstantHeading(new Vector2d(-28, 58),
                    Robot.getVelocityConstraint(45, 2, 15.06),
                    Robot.getAccelerationConstraint(40))/// Flipped waypoint
            .splineToConstantHeading(new Vector2d(-48, 33), Math.toRadians(180), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(25))
            .lineToConstantHeading(new Vector2d(-55.5, 33), // Flipped waypoint
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(30))
            .build();

    public static TrajectorySequence midPurple = Robot.trajectorySequenceBuilder(blueFarStart)
            .lineTo(new Vector2d(-35, 37), // Flipped pose and heading
                    Robot.getVelocityConstraint(50, 2.4, 15.06),
                    Robot.getAccelerationConstraint(40))// Flipped waypoint
            .build();

    public static TrajectorySequence midPurpleToBack = Robot.trajectorySequenceBuilder(midPurple.end())
            .lineTo(new Vector2d(-35, 38)) // Flipped waypoint
            .splineToSplineHeading(new Pose2d(-29, 57, Math.toRadians(180)), Math.toRadians(0), // Flipped pose and heading
                    Robot.getVelocityConstraint(50, 2.4, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(10, 57), // Flipped pose and heading
                    Robot.getVelocityConstraint(50, 2.4, 15.06),
                    Robot.getAccelerationConstraint(40)) // Flipped waypoint
            .splineToConstantHeading(new Vector2d(42.5, 33), Math.toRadians(0), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(35, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .build();

    public static TrajectorySequence midBackToStack = Robot.trajectorySequenceBuilder(midPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(10, 58), Math.toRadians(180), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(45, 2, 15.06),
                    Robot.getAccelerationConstraint(25))
            .lineToConstantHeading(new Vector2d(-28, 58),
                    Robot.getVelocityConstraint(45, 2, 15.06),
                    Robot.getAccelerationConstraint(40))/// Flipped waypoint
            .splineToConstantHeading(new Vector2d(-48, 33), Math.toRadians(180), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(25))
            .lineToConstantHeading(new Vector2d(-55.5, 33), // Flipped waypoint
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(30))
            .build();


    public static TrajectorySequence rightPurple = Robot.trajectorySequenceBuilder(blueFarStart)
            .splineToConstantHeading(new Vector2d(-46.5, 38), Math.toRadians(270), // Flipped pose and heading
                    Robot.getVelocityConstraint(50, 2.4, 15.06),
                    Robot.getAccelerationConstraint(40))// Flipped waypoint // Flipped waypoint
            .build();

    public static TrajectorySequence rightPurpleToBack = Robot.trajectorySequenceBuilder(rightPurple.end())
            .setReversed(true)
            .lineTo(new Vector2d(-46.5, 42)) // Flipped waypoint
            .splineToSplineHeading(new Pose2d(-28, 57, Math.toRadians(180)), Math.toRadians(0), // Flipped pose and heading
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(10, 57), // Flipped pose and heading
                    Robot.getVelocityConstraint(50, 2.4, 15.06),
                    Robot.getAccelerationConstraint(40)) // Flipped waypoint
            .splineToConstantHeading(new Vector2d(43.5, 26), Math.toRadians(0), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(35, 2, 15.06),
                    Robot.getAccelerationConstraint(35))

            .build();

    public static TrajectorySequence rightBackToStack = Robot.trajectorySequenceBuilder(rightPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(10, 58), Math.toRadians(180), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(45, 2, 15.06),
                    Robot.getAccelerationConstraint(25))
            .lineToConstantHeading(new Vector2d(-28, 58),
                    Robot.getVelocityConstraint(45, 2, 15.06),
                    Robot.getAccelerationConstraint(40))/// Flipped waypoint
            .splineToConstantHeading(new Vector2d(-48, 32.25), Math.toRadians(180), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(30))
            .lineToConstantHeading(new Vector2d(-55.5, 32.25), // Flipped waypoint
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(25))
            .build();

    public static TrajectorySequence stackToBack1 = Robot.trajectorySequenceBuilder(rightBackToStack.end())
            .setReversed(true)
            .lineTo(new Vector2d(-50, 32.25))
            .splineToConstantHeading(new Vector2d(-35, 57), Math.toRadians(0), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(20, 57), // Flipped waypoint
                    Robot.getVelocityConstraint(45, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .splineToConstantHeading(new Vector2d(55, 50), Math.toRadians(0),
                    Robot.getVelocityConstraint(35, 2, 15.06),
                    Robot.getAccelerationConstraint(45))/// Flipped waypoint
            .lineTo(new Vector2d(50.5, 50),
                    Robot.getVelocityConstraint(25, 2, 15.06),
                    Robot.getAccelerationConstraint(25))/// Flipped waypoint
            .build();

    public static TrajectorySequence backToStack1 = Robot.trajectorySequenceBuilder(stackToBack1.end())
            .splineToConstantHeading(new Vector2d(25, 57), Math.toRadians(180), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(45, 2, 15.06),
                    Robot.getAccelerationConstraint(25))
            .lineToConstantHeading(new Vector2d(0, 57),
                    Robot.getVelocityConstraint(45, 2, 15.06),
                    Robot.getAccelerationConstraint(40))/// Flipped
            .lineToConstantHeading(new Vector2d(-28, 57),
                    Robot.getVelocityConstraint(45, 2, 15.06),
                    Robot.getAccelerationConstraint(40))/// Flipped waypoint
            .splineToConstantHeading(new Vector2d(-48, 31.5), Math.toRadians(180), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(30))
            .lineToConstantHeading(new Vector2d(-55.5, 31.5), // Flipped waypoint
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(25))
            .build();

    public static TrajectorySequence stackToBack2 = Robot.trajectorySequenceBuilder(backToStack1.end())
            .setReversed(true)
            .lineTo(new Vector2d(-50, 31.5))
            .splineToConstantHeading(new Vector2d(-35, 56), Math.toRadians(0), // Flipped waypoint and heading
                    Robot.getVelocityConstraint(35, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(20, 56),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))/// Flipped waypoint
            .splineToConstantHeading(new Vector2d(55, 50), Math.toRadians(0),
                    Robot.getVelocityConstraint(35, 2, 15.06),
                    Robot.getAccelerationConstraint(45))/// Flipped waypoint
            .lineTo(new Vector2d(50.5, 50),
                    Robot.getVelocityConstraint(25, 2, 15.06),
                    Robot.getAccelerationConstraint(25))/// Flipped waypoint
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
    public static NamedTrajectory[][] trajectories = {leftTrajectories, midTrajectories, rightTrajectories};
}

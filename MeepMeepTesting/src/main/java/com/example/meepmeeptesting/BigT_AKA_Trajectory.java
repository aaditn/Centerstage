package com.example.meepmeeptesting;

import static com.example.meepmeeptesting.notWeirdStuff.NamedTrajectory.map;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.example.meepmeeptesting.notWeirdStuff.NamedTrajectory;
import com.example.meepmeeptesting.notWeirdStuff.Paths;
import com.example.meepmeeptesting.notWeirdStuff.Robot;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;

public class BigT_AKA_Trajectory {
    public static Pose2d redCloseStart = new Pose2d(15,-61,Math.toRadians(-90));

    public static TrajectorySequence midPurple = Robot.trajectorySequenceBuilder(redCloseStart)
            .lineTo(new Vector2d(16, -40),
                    Robot.getVelocityConstraint(50, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .splineToConstantHeading(new Vector2d(25, -34), Math.toRadians(0),
                    Robot.getVelocityConstraint(50, 0.5, 15.06),
                    Robot.getAccelerationConstraint(30))
            .splineToSplineHeading(new Pose2d(55, -38.5, Math.toRadians(180)), Math.toRadians(0),
                    Robot.getVelocityConstraint(50, 2, 15.06),
                    Robot.getAccelerationConstraint(30))
            .build();
    public static TrajectorySequence midBackToStack = Robot.trajectorySequenceBuilder(midPurple.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(25, -36), Math.toRadians(180),
                    Robot.getVelocityConstraint(50, 0.5, 15.06),
                    Robot.getAccelerationConstraint(30))
            .splineToConstantHeading((new Vector2d(-54,-35.5)), Math.toRadians(180),
                    Robot.getVelocityConstraint(55, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .build();

    public static TrajectorySequence stackToBack1 = Robot.trajectorySequenceBuilder(midBackToStack.end())
            .setReversed(true)
            .lineTo(new Vector2d(20,-35),
                    Robot.getVelocityConstraint(60, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .splineToConstantHeading(new Vector2d(51.5,-29), Math.toRadians(0),
                    Robot.getVelocityConstraint(60, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .build();
    public static TrajectorySequence backToStack1 = Robot.trajectorySequenceBuilder(stackToBack1.end())

            .setReversed(false)
            .splineToConstantHeading(new Vector2d(25, -34), Math.toRadians(180),
                    Robot.getVelocityConstraint(60, 0.5, 15.06),
                    Robot.getAccelerationConstraint(30))
            .splineToConstantHeading(new Vector2d(-54, -35.5), Math.toRadians(180),
                    Robot.getVelocityConstraint(60, 2, 15.06),
                    Robot.getAccelerationConstraint(30))
            .build();
    public static TrajectorySequence stackToBack2 = Robot.trajectorySequenceBuilder(backToStack1.end())
            .setReversed(true)
            .lineTo(new Vector2d(20,-35),
                    Robot.getVelocityConstraint(60, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .splineToConstantHeading(new Vector2d(51.5,-26.5), Math.toRadians(0),
                    Robot.getVelocityConstraint(60, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .build();

    public static TrajectorySequence backToStack2 = Robot.trajectorySequenceBuilder(stackToBack2.end())
            .splineToConstantHeading(new Vector2d(20,-11), Math.toRadians(180),
                    Robot.getVelocityConstraint(50, 2, 15.06),
                    Robot.getAccelerationConstraint(30))
            .lineTo(new Vector2d(-45,-11),
                    Robot.getVelocityConstraint(50, 2, 15.06),
                    Robot.getAccelerationConstraint(30))
            .splineToConstantHeading((new Vector2d(-54,-11)), Math.toRadians(180),
                    Robot.getVelocityConstraint(20, 2, 15.06),
                    Robot.getAccelerationConstraint(20))
            .build();
    public static TrajectorySequence stackToBack3 = Robot.trajectorySequenceBuilder(backToStack2.end())
            .setReversed(true)
            .lineTo(new Vector2d(25,-11),
                    Robot.getVelocityConstraint(60, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .splineToConstantHeading(new Vector2d(51.5,-28), Math.toRadians(0),
                    Robot.getVelocityConstraint(60, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .build();

    public static TrajectorySequence parkLeft = Robot.trajectorySequenceBuilder(midPurple.end())
            .lineToConstantHeading(new Vector2d(52, -55))
            .build();
    public static TrajectorySequence parkRight = Robot.trajectorySequenceBuilder(midPurple.end())
            .lineToConstantHeading(new Vector2d(52, -25))
            .build();


    public static Paths[] trajectoryNames = {
            Paths.Purple,
            Paths.Go_To_Stack,
            Paths.Score_First,
            Paths.Return_to_Stack,
            Paths.Score_Second,
            Paths.Return_to_Stack_Again,
            Paths.Score_Third

    };

    public static NamedTrajectory[] leftTrajectories = map(
            new TrajectorySequence[]{midPurple, midBackToStack,stackToBack1, backToStack1,stackToBack2, backToStack2},
            trajectoryNames);
    public static NamedTrajectory[] midTrajectories = map(
            new TrajectorySequence[]{midPurple, midBackToStack,stackToBack1, backToStack1,stackToBack2, backToStack2, stackToBack3},
            trajectoryNames);
    public static NamedTrajectory[] rightTrajectories = map(
            new TrajectorySequence[]{midPurple, midBackToStack, stackToBack1, backToStack1,stackToBack2, backToStack2, stackToBack3},
            trajectoryNames);
    public static NamedTrajectory[][] trajectories = {leftTrajectories,midTrajectories,rightTrajectories};



}

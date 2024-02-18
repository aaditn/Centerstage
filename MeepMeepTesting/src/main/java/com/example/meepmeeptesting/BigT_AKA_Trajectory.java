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


    public static Pose2d blueFarStart = new Pose2d(-35, 61, Math.toRadians(270));
    public static TrajectorySequence leftPurple = Robot.trajectorySequenceBuilder(blueFarStart)
            .splineToLinearHeading(new Pose2d(-35,27,Math.toRadians(30)),Math.toRadians(30))
            .build();

    public static TrajectorySequence leftPurpleToBack = Robot.trajectorySequenceBuilder(leftPurple.end())
            .setReversed(true)
            .splineTo(new Vector2d(-29,9),Math.toRadians(0))
            .lineTo(new Vector2d(20,9))
            .splineToConstantHeading(new Vector2d(49.5,41), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence leftBackToStack = Robot.trajectorySequenceBuilder(leftPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(20,10), Math.toRadians(-180),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))

            .lineTo(new Vector2d(-40,10))
            .lineTo(new Vector2d(-53.5,10),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence midPurple = Robot.trajectorySequenceBuilder(blueFarStart)
            .lineToLinearHeading(new Pose2d(-37, 11, Math.toRadians(90)))
            .build();
    public static TrajectorySequence midPurpleToBack = Robot.trajectorySequenceBuilder(midPurple.end())
            .lineTo(new Vector2d(-37, 10.9))
            .splineToSplineHeading(new Pose2d(-18, 6, Math.toRadians(180)), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(20, 6))
            .splineToConstantHeading(new Vector2d(49.5, 35), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence midBackToStack = Robot.trajectorySequenceBuilder(midPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(20,8), Math.toRadians(-180))

            .lineTo(new Vector2d(-52,8))
            .lineTo(new Vector2d(-55.5,8),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence rightPurple = Robot.trajectorySequenceBuilder(blueFarStart)
            .splineToConstantHeading(new Vector2d(-39, 40), Math.toRadians(270))
            .lineToSplineHeading(new Pose2d(-39, 18, Math.toRadians(150)))
            .build();
    public static TrajectorySequence rightPurpleToBack = Robot.trajectorySequenceBuilder(rightPurple.end())
            .setReversed(true)

            .splineToSplineHeading(new Pose2d(-12, 8, Math.toRadians(180)), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(20, 8))
            .splineToConstantHeading(new Vector2d(49.5, 30), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence rightBackToStack = Robot.trajectorySequenceBuilder(rightPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(20,8), Math.toRadians(-180))

            .lineTo(new Vector2d(-52,8))
            .lineTo(new Vector2d(-56.5,8),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();

    public static TrajectorySequence stackToBack1 = Robot.trajectorySequenceBuilder(leftBackToStack.end())
            .setReversed(true)
            .lineTo(new Vector2d(25,12))
            .splineToConstantHeading(new Vector2d(51,16), Math.toRadians(0))
            .build();
    public static TrajectorySequence backToStack1 = Robot.trajectorySequenceBuilder(stackToBack1.end())

            .setReversed(false)
            .splineToConstantHeading(new Vector2d(25,10), Math.toRadians(-180))

            .lineTo(new Vector2d(-40,10))
            .lineTo(new Vector2d(-53.5,10),
                    Robot.getVelocityConstraint(35, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence stackToBack2 = Robot.trajectorySequenceBuilder(backToStack1.end())
            .setReversed(true)
            .lineTo(new Vector2d(25,10))
            .splineToConstantHeading(new Vector2d(5116), Math.toRadians(0))
            .build();

    public static Paths[] trajectoryNames = {
            Paths.Purple,
            Paths.Score_Spike,
            Paths.Go_To_Stack,
            Paths.Score_First,
            Paths.Return_to_Stack,
            Paths.Score_Second
    };

    public static NamedTrajectory[] leftTrajectories = map(
            new TrajectorySequence[]{leftPurple, leftPurpleToBack, leftBackToStack,stackToBack1, backToStack1,stackToBack2},
            trajectoryNames);
    public static NamedTrajectory[] midTrajectories = map(
            new TrajectorySequence[]{midPurple, midPurpleToBack, midBackToStack,stackToBack1, backToStack1,stackToBack2},
            trajectoryNames);
    public static NamedTrajectory[] rightTrajectories = map(
            new TrajectorySequence[]{rightPurple, rightPurpleToBack, rightBackToStack, stackToBack1, backToStack1,stackToBack2},
            trajectoryNames);
    public static NamedTrajectory[][] trajectories = {leftTrajectories,midTrajectories,rightTrajectories};




}

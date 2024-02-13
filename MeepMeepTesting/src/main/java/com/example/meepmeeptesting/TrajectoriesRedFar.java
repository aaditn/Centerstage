package com.example.meepmeeptesting;

import static com.example.meepmeeptesting.notWeirdStuff.NamedTrajectory.map;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.example.meepmeeptesting.notWeirdStuff.NamedTrajectory;
import com.example.meepmeeptesting.notWeirdStuff.Paths;
import com.example.meepmeeptesting.notWeirdStuff.Robot;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;

public class TrajectoriesRedFar {
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
            .splineToLinearHeading(new Pose2d(-32,36,Math.toRadians(-45)),Math.toRadians(-45))
            .build();

    public static TrajectorySequence leftPurpleToBack = Robot.trajectorySequenceBuilder(leftPurple.end())
            .setReversed(true)
            .splineTo(new Vector2d(-39,40),Math.toRadians(180))
            .splineTo(new Vector2d(-29,11),Math.toRadians(0))
            .lineTo(new Vector2d(25,11))
            .splineToConstantHeading(new Vector2d(50,38), Math.toRadians(0))
            .build();
    public static TrajectorySequence leftBackToStack = Robot.trajectorySequenceBuilder(leftPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(25,11), Math.toRadians(-180))
            .lineTo(new Vector2d(-60,11))
            .build();
    public static TrajectorySequence midPurple = Robot.trajectorySequenceBuilder(blueFarStart)
            .lineToLinearHeading(new Pose2d(-38, 14, Math.toRadians(90)))
            .build();
    public static TrajectorySequence midPurpleToBack = Robot.trajectorySequenceBuilder(midPurple.end())
            .lineTo(new Vector2d(-38, 13))
            .splineToSplineHeading(new Pose2d(-26, 8, Math.toRadians(180)), Math.toRadians(0))
            .lineToConstantHeading(new Vector2d(20, 8))
            .splineToConstantHeading(new Vector2d(50.5, 26), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence midBackToStack = Robot.trajectorySequenceBuilder(midPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(25,11), Math.toRadians(-180))
            .lineTo(new Vector2d(-60,11))
            .build();
    public static TrajectorySequence rightPurple = Robot.trajectorySequenceBuilder(blueFarStart)
            .setReversed(true)
            .lineToLinearHeading(new Pose2d(((blueFarStart.getX()+(-39.0))/2.0),(blueFarStart.getY()+(20.0))/2.0,Math.toRadians((270.0+112.5))))
            .lineToLinearHeading(new Pose2d(-39, 20,Math.toRadians(135)))
            //270 135
            //225
            //112.5
            .build();
    public static TrajectorySequence rightPurpleToBack = Robot.trajectorySequenceBuilder(rightPurple.end())
            .setReversed(true)
            .lineTo(new Vector2d(-36, 17))
            .splineToSplineHeading(new Pose2d(-12, 8, Math.toRadians(180)), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(20, 8))
            .splineToConstantHeading(new Vector2d(50.5, 26), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence rightBackToStack = Robot.trajectorySequenceBuilder(rightPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(25,11), Math.toRadians(-180))
            .lineTo(new Vector2d(-60,11))
            .build();

    public static TrajectorySequence stackToBack1 = Robot.trajectorySequenceBuilder(leftBackToStack.end())
            .setReversed(true)
            .lineTo(new Vector2d(25,11))
            .splineToConstantHeading(new Vector2d(48,16), Math.toRadians(0))
            .build();
    public static TrajectorySequence backToStack1 = Robot.trajectorySequenceBuilder(stackToBack1.end())

            .setReversed(false)
            .splineToConstantHeading(new Vector2d(25,11), Math.toRadians(-180))

            .lineTo(new Vector2d(-60,11))
            .build();
    public static TrajectorySequence stackToBack2 = Robot.trajectorySequenceBuilder(backToStack1.end())
            .setReversed(true)
            .lineTo(new Vector2d(25,11))
            .splineToConstantHeading(new Vector2d(48,16), Math.toRadians(0))
            .build();

    public static Paths[] trajectoryNames = {
            Paths.Purple,
            Paths.Score_Spike,
            Paths.Go_To_Stack,
            Paths.Score_First,
            Paths.Score_Second,
            Paths.Return_to_Stack
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

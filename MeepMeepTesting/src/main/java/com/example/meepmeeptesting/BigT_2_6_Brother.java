package com.example.meepmeeptesting;

import static com.example.meepmeeptesting.notWeirdStuff.NamedTrajectory.map;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.example.meepmeeptesting.notWeirdStuff.NamedTrajectory;
import com.example.meepmeeptesting.notWeirdStuff.Paths;
import com.example.meepmeeptesting.notWeirdStuff.Robot;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;

public class BigT_2_6_Brother {
    public static com.example.meepmeeptesting.notWeirdStuff.Robot Robot = new Robot();


    // please don't delete me,
    // I am here to serve the purpose of telling you don't delete me,
    // I am here to serve the purpose of telling you don't delete me,
    // I am here to serve the purpose of telling you don't delete me,
    // ehh im gonna loop it
    // while (true) {
    //     I am here to serve the purpose of telling you don't delete me,
    // }
    //okay.
    public static Pose2d redCloseStart = new Pose2d(12,-61,Math.toRadians(-90));

    public static TrajectorySequence rightPurple = Robot.trajectorySequenceBuilder(redCloseStart)
            .lineTo(new Vector2d(12, -60),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .splineToConstantHeading(new Vector2d(24, -40), Math.toRadians(-270),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence rightPurpleToBack = Robot.trajectorySequenceBuilder(rightPurple.end())
            .lineToLinearHeading(new Pose2d(50, -38, Math.toRadians(180)),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence rightBackToStack = Robot.trajectorySequenceBuilder(rightPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(20,-8), Math.toRadians(180))

            .lineTo(new Vector2d(-52,-8))
            .lineTo(new Vector2d(-58,-8),
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(30))
            .build();

    public static TrajectorySequence midPurple = Robot.trajectorySequenceBuilder(redCloseStart)
            .lineTo(new Vector2d(10, -38),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .splineToConstantHeading(new Vector2d(14, -34), Math.toRadians(0),
                    Robot.getVelocityConstraint(20, 0.5, 15.06),
                    Robot.getAccelerationConstraint(20))

            .lineToSplineHeading(new Pose2d(50, -34, Math.toRadians(180)),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence midPurpleToBack = Robot.trajectorySequenceBuilder(midPurple.end())
            .splineToSplineHeading(new Pose2d(50, -34.1, Math.toRadians(180)),Math.toRadians(180),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence midBackToStack = Robot.trajectorySequenceBuilder(midPurpleToBack.end())
            .setReversed(false)
//            .splineToConstantHeading(new Vector2d(20,-9.5), Math.toRadians(180),
//                    Robot.getVelocityConstraint(45, 2, 15.06),
//                    Robot.getAccelerationConstraint(30))
//            .lineTo(new Vector2d(-20,-9.5),
//                    Robot.getVelocityConstraint(30, 2, 15.06),
//                    Robot.getAccelerationConstraint(30))
            .splineToConstantHeading((new Vector2d(-53,-33.5)), Math.toRadians(180),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .build();


    public static  TrajectorySequence leftPurple = Robot.trajectorySequenceBuilder(redCloseStart)
            .lineTo(new Vector2d(12, -45),
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(30))
            .splineToSplineHeading(new Pose2d(10.5, -37,Math.toRadians(-225)), Math.toRadians(-225),
                    Robot.getVelocityConstraint(35, 0.75, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence leftPurpleToBack = Robot.trajectorySequenceBuilder(leftPurple.end())
            .lineToLinearHeading(new Pose2d(50, -25, Math.toRadians(180)),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence leftBackToStack = Robot.trajectorySequenceBuilder(leftPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(20,-8), Math.toRadians(180))

            .lineTo(new Vector2d(-52,-8))
            .lineTo(new Vector2d(-57.5,-8),
                    Robot.getVelocityConstraint(55, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();

    public static TrajectorySequence stackToBack1 = Robot.trajectorySequenceBuilder(midBackToStack.end())
            .setReversed(true)
//            .lineTo(new Vector2d(12,-10),
//                    Robot.getVelocityConstraint(50, 2, 15.06),
//                    Robot.getAccelerationConstraint(35))
            .splineToConstantHeading(new Vector2d(52,-33.5), Math.toRadians(0),
                    Robot.getVelocityConstraint(50, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .build();
    public static TrajectorySequence backToStack1 = Robot.trajectorySequenceBuilder(stackToBack1.end())

            .setReversed(false)
//            .splineToConstantHeading(new Vector2d(20,-9.5), Math.toRadians(180),
//                    Robot.getVelocityConstraint(30, 2, 15.06),
//                    Robot.getAccelerationConstraint(30))
//            .lineTo(new Vector2d(-20,-9.5),
//                    Robot.getVelocityConstraint(30, 2, 15.06),
//                    Robot.getAccelerationConstraint(30))
            .splineToConstantHeading((new Vector2d(-53,-33.5)), Math.toRadians(180),
                    Robot.getVelocityConstraint(50, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .build();
    public static TrajectorySequence stackToBack2 = Robot.trajectorySequenceBuilder(backToStack1.end())
            .setReversed(true)
//            .lineTo(new Vector2d(12,-10),
//                    Robot.getVelocityConstraint(50, 2, 15.06),
//                    Robot.getAccelerationConstraint(35))
            .splineToConstantHeading(new Vector2d(51.5,-33.5), Math.toRadians(0),
                    Robot.getVelocityConstraint(50, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .build();
    public static TrajectorySequence parkLeft = Robot.trajectorySequenceBuilder(leftPurpleToBack.end())
            .lineToConstantHeading(new Vector2d(52, -55),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence parkRight = Robot.trajectorySequenceBuilder(leftPurpleToBack.end())
            .lineToConstantHeading(new Vector2d(52, -25),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
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
            new TrajectorySequence[]{leftPurple, leftPurpleToBack, leftBackToStack, stackToBack1, backToStack1,stackToBack2},
            trajectoryNames);
    public static NamedTrajectory[] midTrajectories = map(
            new TrajectorySequence[]{midPurple, midBackToStack, stackToBack1, backToStack1,stackToBack2},
            trajectoryNames);
    public static NamedTrajectory[] rightTrajectories = map(
            new TrajectorySequence[]{rightPurple, rightPurpleToBack, rightBackToStack, stackToBack1, backToStack1,stackToBack2},
            trajectoryNames);
    public static NamedTrajectory[][] trajectories = {leftTrajectories,midTrajectories,rightTrajectories};
}

package org.firstinspires.ftc.teamcode.auto_paths.door_paths;

import static org.firstinspires.ftc.teamcode.util.NamedTrajectory.map;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.NamedTrajectory;
import org.firstinspires.ftc.teamcode.util.enums.Paths;

public class farRedDoor {
    public static Pose2d redFarStart = new Pose2d(-32.5, -61, Math.toRadians(90));
    public static TrajectorySequence leftPurple = Robot.trajectorySequenceBuilder(redFarStart)
            .splineToConstantHeading(new Vector2d(-35, -40), Math.toRadians(90))
            .lineToSplineHeading(new Pose2d(-35, -18, Math.toRadians(225)))
            .build();
    public static TrajectorySequence leftPurpleToBack = Robot.trajectorySequenceBuilder(leftPurple.end())
            .setReversed(true)

            .splineToSplineHeading(new Pose2d(-12, -8, Math.toRadians(180)), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(20, -8))
            .splineToConstantHeading(new Vector2d(40, -28), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineTo(new Vector2d(46.5, -28),
                    Robot.getVelocityConstraint(25, 2, 15.06),
                    Robot.getAccelerationConstraint(20))
            .build();
    public static TrajectorySequence leftBackToStack = Robot.trajectorySequenceBuilder(leftPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(20,-9.5), Math.toRadians(180))

            .lineTo(new Vector2d(-35,-9.5),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineTo(new Vector2d(-54.5,-9.5),
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence midPurple = Robot.trajectorySequenceBuilder(redFarStart)
            .lineToLinearHeading(new Pose2d(-37, -9, Math.toRadians(270.01)))
            .build();
    public static TrajectorySequence midPurpleToBack = Robot.trajectorySequenceBuilder(midPurple.end())
            .lineTo(new Vector2d(-37, -8.9))
            .splineToSplineHeading(new Pose2d(-18, -7, Math.toRadians(180)), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(20, -7))
            .splineToConstantHeading(new Vector2d(40, -32), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineTo(new Vector2d(46.5, -32),
                    Robot.getVelocityConstraint(25, 2, 15.06),
                    Robot.getAccelerationConstraint(20))
            .build();
    public static TrajectorySequence midBackToStack = Robot.trajectorySequenceBuilder(midPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(20,-10), Math.toRadians(180))

            .lineTo(new Vector2d(-35,-10),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineTo(new Vector2d(-54.5,-10),
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence rightPurple = Robot.trajectorySequenceBuilder(redFarStart)
            .splineToLinearHeading(new Pose2d(-37,-26,Math.toRadians(-25)),Math.toRadians(-25))
            .build();

    public static TrajectorySequence rightPurpleToBack = Robot.trajectorySequenceBuilder(rightPurple.end())
            .setReversed(true)
            .splineTo(new Vector2d(-29,-11),Math.toRadians(0))
            .lineTo(new Vector2d(20,-11))
            .splineToConstantHeading(new Vector2d(40, -38), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineTo(new Vector2d(46.5, -38),
                    Robot.getVelocityConstraint(25, 2, 15.06),
                    Robot.getAccelerationConstraint(15))
            .build();
    public static TrajectorySequence rightBackToStack = Robot.trajectorySequenceBuilder(rightPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(20,-10), Math.toRadians(180),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineTo(new Vector2d(-35,-10),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineTo(new Vector2d(-54.5,-10),
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();

    public static TrajectorySequence stackToBack1 = Robot.trajectorySequenceBuilder(leftBackToStack.end())
            .setReversed(true)
            .lineTo(new Vector2d(25,-14))
            .splineToConstantHeading(new Vector2d(52.5,-14), Math.toRadians(0))
            .build();
    public static TrajectorySequence backToStack1 = Robot.trajectorySequenceBuilder(stackToBack1.end())

            .setReversed(false)
            .splineToConstantHeading(new Vector2d(25,-10), Math.toRadians(180))

            .lineTo(new Vector2d(-35,-10),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineTo(new Vector2d(-54.5,-10),
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence stackToBack2 = Robot.trajectorySequenceBuilder(backToStack1.end())
            .setReversed(true)
            .lineTo(new Vector2d(25,-14))
            .splineToConstantHeading(new Vector2d(52.5,-14), Math.toRadians(0))
            .build();
    public static TrajectorySequence parkLeft = Robot.trajectorySequenceBuilder(leftPurpleToBack.end())
            .lineToConstantHeading(new Vector2d(52, -55))
            .build();
    public static TrajectorySequence parkRight = Robot.trajectorySequenceBuilder(leftPurpleToBack.end())
            .lineToConstantHeading(new Vector2d(52, -25))
            .build();


    public static Paths[] trajectoryNames = {
            Paths.Purple,
            Paths.Score_Spike,
            Paths.Go_To_Stack,
            Paths.Score_First,
            Paths.Return_to_Stack,
            Paths.Score_Second,
            Paths.ParkLeft,
            Paths.ParkRight
    };

    public static NamedTrajectory[] leftTrajectories = map(
            new TrajectorySequence[]{leftPurple, leftPurpleToBack, leftBackToStack,stackToBack1, backToStack1,stackToBack2, parkLeft, parkRight},
            trajectoryNames);
    public static NamedTrajectory[] midTrajectories = map(
            new TrajectorySequence[]{midPurple, midPurpleToBack, midBackToStack,stackToBack1, backToStack1,stackToBack2, parkLeft, parkRight},
            trajectoryNames);
    public static NamedTrajectory[] rightTrajectories = map(
            new TrajectorySequence[]{rightPurple, rightPurpleToBack, rightBackToStack, stackToBack1, backToStack1,stackToBack2, parkLeft, parkRight},
            trajectoryNames);
    public static NamedTrajectory[][] trajectories = {leftTrajectories,midTrajectories,rightTrajectories};


}

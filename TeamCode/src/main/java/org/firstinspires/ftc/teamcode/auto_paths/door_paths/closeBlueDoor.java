package org.firstinspires.ftc.teamcode.auto_paths.door_paths;

import static org.firstinspires.ftc.teamcode.util.NamedTrajectory.map;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.NamedTrajectory;
import org.firstinspires.ftc.teamcode.util.enums.Paths;

public class closeBlueDoor {
    public static Pose2d blueCloseStart = new Pose2d(12,61,Math.toRadians(270));
    public static  TrajectorySequence leftPurple = Robot.trajectorySequenceBuilder(blueCloseStart)
            .lineTo(new Vector2d(12, 60),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .splineToConstantHeading(new Vector2d(25, 37), Math.toRadians(270),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence leftPurpleToBack = Robot.trajectorySequenceBuilder(leftPurple.end())
            .lineToLinearHeading(new Pose2d(47, 37, Math.toRadians(180)),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence leftBackToStack = Robot.trajectorySequenceBuilder(leftPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(20,8), Math.toRadians(-180))

            .lineTo(new Vector2d(-52,8))
            .lineTo(new Vector2d(-57.5,8),
                    Robot.getVelocityConstraint(55, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();

    public static TrajectorySequence midPurple = Robot.trajectorySequenceBuilder(blueCloseStart)
            .lineTo(new Vector2d(12, 34),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence midPurpleToBack = Robot.trajectorySequenceBuilder(midPurple.end())
            .lineToLinearHeading(new Pose2d(47, 33, Math.toRadians(180)),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence midBackToStack = Robot.trajectorySequenceBuilder(midPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(20,8), Math.toRadians(-180))

            .lineTo(new Vector2d(-52,8))
            .lineTo(new Vector2d(-58.5,8),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();

    public static TrajectorySequence rightPurple = Robot.trajectorySequenceBuilder(blueCloseStart)
            .lineTo(new Vector2d(12, 45),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .splineToSplineHeading(new Pose2d(10, 34,Math.toRadians(225)), Math.toRadians(225),
                    Robot.getVelocityConstraint(35, 0.75, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence rightPurpleToBack = Robot.trajectorySequenceBuilder(rightPurple.end())
            .lineToLinearHeading(new Pose2d(47, 27, Math.toRadians(180)),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence rightBackToStack = Robot.trajectorySequenceBuilder(rightPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(20,8), Math.toRadians(-180))

            .lineTo(new Vector2d(-52,8))
            .lineTo(new Vector2d(-58,8),
                    Robot.getVelocityConstraint(55, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();

    public static TrajectorySequence stackToBack1 = Robot.trajectorySequenceBuilder(leftBackToStack.end())
            .setReversed(true)
            .lineTo(new Vector2d(25,8))
            .splineToConstantHeading(new Vector2d(48,16), Math.toRadians(0))
            .build();
    public static TrajectorySequence backToStack1 = Robot.trajectorySequenceBuilder(stackToBack1.end())

            .setReversed(false)
            .splineToConstantHeading(new Vector2d(25,8), Math.toRadians(-180))

            .lineTo(new Vector2d(-45,8))
            .lineTo(new Vector2d(-58.5,8),
                    Robot.getVelocityConstraint(35, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence stackToBack2 = Robot.trajectorySequenceBuilder(backToStack1.end())
            .setReversed(true)
            .lineTo(new Vector2d(25,8))
            .splineToConstantHeading(new Vector2d(48,16), Math.toRadians(0))
            .build();
    public static TrajectorySequence parkLeft = Robot.trajectorySequenceBuilder(leftPurpleToBack.end())
            .lineToConstantHeading(new Vector2d(52, 55),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence parkRight = Robot.trajectorySequenceBuilder(leftPurpleToBack.end())
            .lineToConstantHeading(new Vector2d(52, 25),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();

    public static Paths[] trajectoryNames = {
            Paths.Purple,
            Paths.Score_Spike,
            Paths.Go_To_Stack,
            Paths.Score_First,
            Paths.Score_Second,
            Paths.Return_to_Stack,
    };

    public static NamedTrajectory[] leftTrajectories = map(
            new TrajectorySequence[]{leftPurple, leftPurpleToBack, leftBackToStack, stackToBack1, backToStack1,stackToBack2},
            trajectoryNames);
    public static NamedTrajectory[] midTrajectories = map(
            new TrajectorySequence[]{midPurple, midPurpleToBack, midBackToStack, stackToBack1, backToStack1,stackToBack2},
            trajectoryNames);
    public static NamedTrajectory[] rightTrajectories = map(
            new TrajectorySequence[]{rightPurple, rightPurpleToBack, rightBackToStack, stackToBack1, backToStack1,stackToBack2},
            trajectoryNames);
    public static NamedTrajectory[][] trajectories = {leftTrajectories,midTrajectories,rightTrajectories};

}

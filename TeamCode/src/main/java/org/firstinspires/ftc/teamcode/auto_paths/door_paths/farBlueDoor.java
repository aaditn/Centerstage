package org.firstinspires.ftc.teamcode.auto_paths.door_paths;

import static org.firstinspires.ftc.teamcode.util.NamedTrajectory.map;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.NamedTrajectory;
import org.firstinspires.ftc.teamcode.util.enums.Paths;

public class farBlueDoor {

    public static Pose2d blueFarStart = new Pose2d(-35, 61, Math.toRadians(270));
    public static TrajectorySequence leftPurple = Robot.trajectorySequenceBuilder(blueFarStart)
            .splineToLinearHeading(new Pose2d(-34,30,Math.toRadians(30)),Math.toRadians(30))
            .build();

    public static TrajectorySequence leftPurpleToBack = Robot.trajectorySequenceBuilder(leftPurple.end())
            .setReversed(true)
            //      .splineTo(new Vector2d(-40,38),Math.toRadians(180))
            .splineTo(new Vector2d(-29,9),Math.toRadians(0))
            .lineTo(new Vector2d(20,9))
            .splineToConstantHeading(new Vector2d(48,40), Math.toRadians(0))
            .build();
    public static TrajectorySequence leftBackToStack = Robot.trajectorySequenceBuilder(leftPurpleToBack.end())
            .setReversed(false)
            .splineToConstantHeading(new Vector2d(20,8), Math.toRadians(-180))

            .lineTo(new Vector2d(-52,8))
            .lineTo(new Vector2d(-57.5,8),
                    Robot.getVelocityConstraint(55, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    public static TrajectorySequence midPurple = Robot.trajectorySequenceBuilder(blueFarStart)
            .lineToLinearHeading(new Pose2d(-38, 12, Math.toRadians(90)))
            .build();
    public static TrajectorySequence midPurpleToBack = Robot.trajectorySequenceBuilder(midPurple.end())
            .lineTo(new Vector2d(-38, 11.5))
            .splineToSplineHeading(new Pose2d(-26, 6, Math.toRadians(180)), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .lineToConstantHeading(new Vector2d(20, 6))
            .splineToConstantHeading(new Vector2d(49, 32), Math.toRadians(0),
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
    public static TrajectorySequence rightPurple = Robot.trajectorySequenceBuilder(blueFarStart)
            .lineTo(new Vector2d(-35,40.5))
            .lineToSplineHeading(new Pose2d(-35, 20,Math.toRadians(135)))
            .build();
    public static TrajectorySequence rightPurpleToBack = Robot.trajectorySequenceBuilder(rightPurple.end())
            .setReversed(true)
            .lineTo(new Vector2d(-35, 17))
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

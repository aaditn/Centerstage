package org.firstinspires.ftc.teamcode.auto_paths;

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
            .splineToConstantHeading(new Vector2d(-46.5, 34), Math.toRadians(270))
            .build();
    public static TrajectorySequence rightPurpleToBack = Robot.trajectorySequenceBuilder(rightPurple.end())
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
            new TrajectorySequence[]{leftPurple, leftPurpleToBack, leftBackToStack, stackToBack1, stackToBack2, backToStack1},
                    trajectoryNames);
    public static NamedTrajectory[] midTrajectories = map(
            new TrajectorySequence[]{midPurple, midPurpleToBack, midBackToStack, stackToBack1, stackToBack2, backToStack1},
                    trajectoryNames);
    public static NamedTrajectory[] rightTrajectories = map(
            new TrajectorySequence[]{rightPurple, rightPurpleToBack, rightBackToStack, stackToBack1, stackToBack2, backToStack1},
                    trajectoryNames);
    public static NamedTrajectory[][] trajectories = {leftTrajectories,midTrajectories,rightTrajectories};

}

package org.firstinspires.ftc.teamcode.auto_paths.door_paths;

import static org.firstinspires.ftc.teamcode.util.NamedTrajectory.map;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.NamedTrajectory;
import org.firstinspires.ftc.teamcode.util.enums.Paths;

public class middleRed {
    public static Pose2d redFarStart = new Pose2d(-34.5, -61, Math.toRadians(-90));

    public static TrajectorySequence midPurple = Robot.trajectorySequenceBuilder(redFarStart)
            .lineToConstantHeading(new Vector2d(-35.5, -33))
            .lineToLinearHeading(new Pose2d(-48, -36, Math.toRadians(-180)),
                    Robot.getVelocityConstraint(30, 2, 15.06),
                    Robot.getAccelerationConstraint(30))
            .lineTo(new Vector2d(-55.5, -36), Robot.getVelocityConstraint(15, 2, 15.06),
                    Robot.getAccelerationConstraint(15))
            .build();
    public static TrajectorySequence midPurpleToBack = Robot.trajectorySequenceBuilder(midPurple.end())
//            .lineTo(new Vector2d(-37, -7.5))
//            .splineToSplineHeading(new Pose2d(-10, -5, Math.toRadians(180)), Math.toRadians(0),
//                    Robot.getVelocityConstraint(40, 2, 15.06),
//                    Robot.getAccelerationConstraint(40))
//            .splineToConstantHeading(new Vector2d(20, -7), Math.toRadians(0))
//            .splineToConstantHeading(new Vector2d(40, -33.5), Math.toRadians(0),
//                    Robot.getVelocityConstraint(40, 2, 15.06),
//                    Robot.getAccelerationConstraint(40))
            .lineTo(new Vector2d(10, -35),
                    Robot.getVelocityConstraint(45, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .splineToConstantHeading(new Vector2d(52.75, -36.25), Math.toRadians(0),
                    Robot.getVelocityConstraint(45, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .build();
    public static TrajectorySequence midBackToStack = Robot.trajectorySequenceBuilder(midPurpleToBack.end())
            .setReversed(false)
//            .splineToConstantHeading(new Vector2d(20,-9.5), Math.toRadians(180),
//                    Robot.getVelocityConstraint(45, 2, 15.06),
//                    Robot.getAccelerationConstraint(30))
//            .lineTo(new Vector2d(-20,-9.5),
//                    Robot.getVelocityConstraint(30, 2, 15.06),
//                    Robot.getAccelerationConstraint(30))
            .splineToConstantHeading((new Vector2d(-45,-35.5)), Math.toRadians(180),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .lineTo(new Vector2d(-58, -35.5),
                    Robot.getVelocityConstraint(20, 2, 15.06),
                    Robot.getAccelerationConstraint(20))

            .build();

    public static TrajectorySequence stackToBack1 = Robot.trajectorySequenceBuilder(midBackToStack.end())
            .setReversed(true)
            .lineTo(new Vector2d(5, -35),
                    Robot.getVelocityConstraint(50, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .splineToConstantHeading(new Vector2d(45,-29.5), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .lineTo(new Vector2d(52.25,-29.5),
                    Robot.getVelocityConstraint(25, 2, 15.06),
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
            .lineTo(new Vector2d(30, -33),
                    Robot.getVelocityConstraint(50, 2, 15.06),
                    Robot.getAccelerationConstraint(30))
            .splineToConstantHeading((new Vector2d(-45,-35)), Math.toRadians(180),
                    Robot.getVelocityConstraint(50, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .lineTo(new Vector2d(-61, -35),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(30))
            .build();
    public static TrajectorySequence stackToBack2 = Robot.trajectorySequenceBuilder(backToStack1.end())
            .setReversed(true)
            .lineTo(new Vector2d(5, -35),
                    Robot.getVelocityConstraint(50, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .splineToConstantHeading(new Vector2d(45,-29.5), Math.toRadians(0),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(35))
            .lineTo(new Vector2d(52.25,-29.5),
                    Robot.getVelocityConstraint(25, 2, 15.06),
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
            Paths.Score_Spike,
            Paths.Go_To_Stack,
            Paths.Score_First,
            Paths.Return_to_Stack,
            Paths.Score_Second,

    };

    public static NamedTrajectory[] leftTrajectories = map(
            new TrajectorySequence[]{midPurple, midPurpleToBack, midBackToStack,stackToBack1, backToStack1,stackToBack2},
            trajectoryNames);
    public static NamedTrajectory[] midTrajectories = map(
            new TrajectorySequence[]{midPurple, midPurpleToBack, midBackToStack,stackToBack1, backToStack1,stackToBack2},
            trajectoryNames);
    public static NamedTrajectory[] rightTrajectories = map(
            new TrajectorySequence[]{midPurple, midPurpleToBack, midBackToStack, stackToBack1, backToStack1,stackToBack2},
            trajectoryNames);
    public static NamedTrajectory[][] trajectories = {leftTrajectories,midTrajectories,rightTrajectories};

}

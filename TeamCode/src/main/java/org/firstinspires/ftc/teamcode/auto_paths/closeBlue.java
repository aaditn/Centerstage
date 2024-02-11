package org.firstinspires.ftc.teamcode.auto_paths;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

public class closeBlue {
    public static Pose2d blueCloseStart = new Pose2d(12,61,Math.toRadians(270));
    TrajectorySequence rightPurple = Robot.trajectorySequenceBuilder(blueCloseStart)
            .lineTo(new Vector2d(12, 45),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .splineToSplineHeading(new Pose2d(9, 38,Math.toRadians(225)), Math.toRadians(225),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
//            .addTemporalMarker(1.5, () -> intake.setState(Intake.PositionState.AUTO))
            .build();
    TrajectorySequence rightYellow = Robot.trajectorySequenceBuilder(rightPurple.end())
            .lineToLinearHeading(new Pose2d(51, 27, Math.toRadians(180)),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    TrajectorySequence midPurple = Robot.trajectorySequenceBuilder(blueCloseStart)
            .lineTo(new Vector2d(12, 34),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
//            .addTemporalMarker(1.5, () -> intake.setState(Intake.PositionState.AUTO))
            .build();
    TrajectorySequence midYellow = Robot.trajectorySequenceBuilder(midPurple.end())
            .lineToLinearHeading(new Pose2d(52, 33, Math.toRadians(180)),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    TrajectorySequence leftPurple = Robot.trajectorySequenceBuilder(blueCloseStart)
            .lineTo(new Vector2d(13, 60),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .splineToConstantHeading(new Vector2d(25, 37), Math.toRadians(270),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
//            .addTemporalMarker(1.5, () -> intake.setState(Intake.PositionState.AUTO))
            .build();
    TrajectorySequence leftYellow = Robot.trajectorySequenceBuilder(leftPurple.end())
            .lineToLinearHeading(new Pose2d(52, 40, Math.toRadians(180)),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();

    TrajectorySequence parkLeft = Robot.trajectorySequenceBuilder(leftYellow.end())
            .lineToConstantHeading(new Vector2d(52, 55),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();
    TrajectorySequence parkRight = Robot.trajectorySequenceBuilder(leftYellow.end())
            .lineToConstantHeading(new Vector2d(52, 25),
                    Robot.getVelocityConstraint(40, 2, 15.06),
                    Robot.getAccelerationConstraint(40))
            .build();

}

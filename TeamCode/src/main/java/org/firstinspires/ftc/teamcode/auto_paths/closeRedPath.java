package org.firstinspires.ftc.teamcode.auto_paths;

import static org.firstinspires.ftc.teamcode.opmodes.auto.closeRed.drive;
import static org.firstinspires.ftc.teamcode.util.NamedTrajectory.map;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.util.NamedTrajectory;
import org.firstinspires.ftc.teamcode.util.enums.Paths;

import java.util.Arrays;

public class closeRedPath {
    public Action purple;
    public Action yellow;
    public Action stack1;
    public Action back1;

    NamedTrajectory[] leftTrajectories, midTrajectories, rightTrajectories;
    public NamedTrajectory[][] trajectories;
    public closeRedPath() {

        purple = drive.actionBuilder(closeRedStart)
                .splineToConstantHeading(new Vector2d(closeRedStart.position.x, -50), closeRedStart.heading.toDouble())
                .splineToSplineHeading(new Pose2d(10, -38, Math.toRadians(150)), Math.toRadians(100))
                .build();
        yellow = drive.actionBuilder(new Pose2d(10, -38, Math.toRadians(150)))
                .strafeTo(new Vector2d(15, -37))
                .splineToSplineHeading(new Pose2d(35, -26, Math.toRadians(180)), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(48, -26, Math.toRadians(180)), Math.toRadians(0))
                .build();
        stack1 = drive.actionBuilder(new Pose2d(48, -26, Math.toRadians(180)))
                .splineToSplineHeading(new Pose2d(20, -9, Math.toRadians(180)), Math.toRadians(180),
                        Robot.getVelocityConstraint(MecanumDrive.PARAMS.maxWheelVel, MecanumDrive.PARAMS.maxAngVel),
                        Robot.getAccelerationConstraint(-25, 25))
                .lineToXSplineHeading(-58, Math.toRadians(180),
                        Robot.getVelocityConstraint(MecanumDrive.PARAMS.maxWheelVel , MecanumDrive.PARAMS.maxAngVel),
                        Robot.getAccelerationConstraint(-25, 25))
                .build();
        back1 = drive.actionBuilder(new Pose2d(-58, -9, Math.toRadians(180)))
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(0, -9), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(48, -18, Math.toRadians(170)), Math.toRadians(-10))
                .build();

        leftTrajectories = map(
                Arrays.asList(purple, yellow, stack1, back1, stack1, back1, stack1, back1),
                trajectoryNames);
        midTrajectories = map(
                Arrays.asList(purple, yellow, stack1, back1, stack1, back1, stack1, back1),
                trajectoryNames);
        rightTrajectories = map(
                Arrays.asList(purple, yellow, stack1, back1, stack1, back1, stack1, back1),
                trajectoryNames);
        trajectories = new NamedTrajectory[][]{leftTrajectories, midTrajectories, rightTrajectories};
    }
    public static Pose2d
            closeRedStart = new Pose2d(12, -61, Math.toRadians(90));
    public static Paths[] trajectoryNames = {
            Paths.Purple,
            Paths.Yellow,
            Paths.Stack1,
            Paths.Back1,
            Paths.Stack2,
            Paths.Back2,
            Paths.Stack3,
            Paths.Back3
    };

}
package org.firstinspires.ftc.teamcode.auto_paths;

import static org.firstinspires.ftc.teamcode.util.NamedTrajectory.map;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.util.NamedTrajectory;
import org.firstinspires.ftc.teamcode.util.enums.Paths;

public class closeRedPath {
    Robot robot;
    public closeRedPath(Robot robot) {
        this.robot = robot;
    }
    public static Pose2d closeRedStart = new Pose2d(12, -61, Math.toRadians(270));
    public Action purple_yellow = robot.actionBuilder(closeRedStart)
            .lineToY(-50)
            .splineToSplineHeading(new Pose2d(10, -38, Math.toRadians(150)), Math.toRadians(100))
            .splineToConstantHeading(new Vector2d(30, -30), Math.toRadians(0))
            .splineToSplineHeading(new Pose2d(48, -30, Math.toRadians(180)), Math.toRadians(0))
            .build();
    public Action stack1 = robot.actionBuilder(new Pose2d(48, -30, Math.toRadians(180)))
            .splineToConstantHeading(new Vector2d(46, -30), Math.toRadians(180))
            .splineToConstantHeading(new Vector2d(10, -12), Math.toRadians(180))
            .lineToX(-58)
            .build();

    public Action back1 = robot.actionBuilder(new Pose2d(-58, -12, Math.toRadians(180)))
            .lineToX(-56)
            .splineToConstantHeading(new Vector2d(0, -12), Math.toRadians(0))
            .splineToConstantHeading(new Vector2d(48, -30), Math.toRadians(0))
            .build();

    public static Paths[] trajectoryNames = {
            Paths.Purple_Yellow,
            Paths.Stack1,
            Paths.Back1,
    };

    public NamedTrajectory[] leftTrajectories = map(
            new Action[]{purple_yellow, stack1, back1},
            trajectoryNames);
    public NamedTrajectory[] midTrajectories = map(
            new Action[]{purple_yellow, stack1, back1},
            trajectoryNames);
    public NamedTrajectory[] rightTrajectories = map(
            new Action[]{purple_yellow, stack1, back1},
            trajectoryNames);
    public NamedTrajectory[][] trajectories = {leftTrajectories,midTrajectories,rightTrajectories};
}
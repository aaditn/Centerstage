package org.firstinspires.ftc.teamcode.auto_paths;

import static org.firstinspires.ftc.teamcode.util.NamedTrajectory.map;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.util.NamedTrajectory;
import org.firstinspires.ftc.teamcode.util.enums.Paths;

import java.util.Arrays;
import java.util.List;

public class closeRedPath {
    Robot robot;
    public List<Trajectory> purple_yellow, stack1, back1;

    NamedTrajectory[] leftTrajectories, midTrajectories, rightTrajectories;


    public NamedTrajectory[][] trajectories;
    public closeRedPath(Robot robot) {
        this.robot = robot;
        purple_yellow = robot.trajectoryBuilder(closeRedStart)
                .lineToY(-50)
                .splineToSplineHeading(new Pose2d(10, -38, Math.toRadians(150)), Math.toRadians(100))
                .splineToConstantHeading(new Vector2d(30, -30), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(48, -30, Math.toRadians(180)), Math.toRadians(0))
                .build();
        stack1 = robot.trajectoryBuilder(new Pose2d(48, -30, Math.toRadians(180)))
                .splineToConstantHeading(new Vector2d(46, -30), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(10, -12), Math.toRadians(180))
                .lineToX(-58)
                .build();

        back1 = robot.trajectoryBuilder(new Pose2d(-58, -12, Math.toRadians(180)))
                .lineToX(-56)
                .splineToConstantHeading(new Vector2d(0, -12), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(48, -30), Math.toRadians(0))
                .build();

        leftTrajectories = map(
                Arrays.asList(purple_yellow, stack1, back1),
                        trajectoryNames);
        leftTrajectories = map(
                Arrays.asList(purple_yellow, stack1, back1),
                trajectoryNames);
        leftTrajectories = map(
                Arrays.asList(purple_yellow, stack1, back1),
                trajectoryNames);
        trajectories = new NamedTrajectory[][]{leftTrajectories, midTrajectories, rightTrajectories};
    }
    public static Pose2d closeRedStart = new Pose2d(12, -61, Math.toRadians(270));
    public static Paths[] trajectoryNames = {
            Paths.Purple_Yellow,
            Paths.Stack1,
            Paths.Back1,
    };

}
package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import org.jetbrains.annotations.NotNull;

public class MeepMeepTesting {

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(700);

        Pose2d blueFarStart = new Pose2d(-36,61,Math.toRadians(270));
        Pose2d blueCloseStart = new Pose2d(12,61,Math.toRadians(270));
        Pose2d redFarStart = new Pose2d(-36,-61,Math.toRadians(90));
        Pose2d redCloseStart = new Pose2d(12,-61,Math.toRadians(90));

        Pose2d blueBackLeft = new Pose2d(52,26,Math.toRadians(180));
        Pose2d blueBackMid = new Pose2d(52,31,Math.toRadians(180));
        Pose2d blueBackRight = new Pose2d(52,36,Math.toRadians(180));

        Pose2d blueMiddleStack = new Pose2d(-60, 12, Math.toRadians(180));
        Pose2d blueEdgeStack = new Pose2d(-60, 30, Math.toRadians(180));

        RoadRunnerBotEntity myBotTank = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(55,50, Math.toRadians(150), Math.toRadians(150), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(blueBackMid)
                                .splineToSplineHeading(new Pose2d(20, 16, Math.toRadians(200)), Math.toRadians(200))
                                .splineToSplineHeading(new Pose2d(-20, 12, Math.toRadians(180)), Math.toRadians(180))
                                .splineTo(Vec(blueMiddleStack), Head(blueMiddleStack))
                                .build()
                );

        RoadRunnerBotEntity myBotSpline = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(55,50, Math.toRadians(150), Math.toRadians(150), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(blueBackMid)
                                .splineToConstantHeading(new Vector2d(20, 12), Math.toRadians(180), V(40), A(40))
                                .lineTo(Vec(blueMiddleStack))
                                .build()
                );


        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBotTank)
                .addEntity(myBotSpline)
                .start();
    }

    public static Vector2d Vec(Pose2d P) {
        return new Vector2d(P.getX(), P.getY());
    }
    public static double Head(Pose2d P) {
        return P.getHeading();
    }
    public static Pose2d offset(Pose2d P, double offsetX, double offsetY, double offsetHeading) {
        return new Pose2d(P.getX() + offsetX, P.getY() + offsetY, P.getHeading() + offsetHeading);
    }
    public static TrajectoryAccelerationConstraint A(double num) {
        TrajectoryAccelerationConstraint sA = new TrajectoryAccelerationConstraint() {
            @Override
            public double get(double v, @NotNull Pose2d pose2d, @NotNull Pose2d pose2d1, @NotNull Pose2d pose2d2) {
                return num;
            }
        };
        return sA;
    }
    public static TrajectoryVelocityConstraint V(double num) {
        TrajectoryVelocityConstraint sV = new TrajectoryVelocityConstraint() {
            @Override
            public double get(double v, @NotNull Pose2d pose2d, @NotNull Pose2d pose2d1, @NotNull Pose2d pose2d2) {
                return num;
            }
        };
        return sV;
    }
}
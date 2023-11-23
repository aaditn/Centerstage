package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.SampleMecanumDrive;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequenceBuilder;

public class MeepMeepTesting {

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        Pose2d blueFarStart = new Pose2d(-36,61,Math.toRadians(270));
        Pose2d blueCloseStart = new Pose2d(12,61,Math.toRadians(270));
        Pose2d redFarStart = new Pose2d(-36,-61,Math.toRadians(90));
        Pose2d redCloseStart = new Pose2d(12,-61,Math.toRadians(270));

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(redFarStart)
                                .splineTo(new Vector2d(-35, -40), Math.toRadians(90))
                                .splineTo(new Vector2d(-38, -34), Math.toRadians(180))
                                .waitSeconds(1)
                                .lineToConstantHeading(new Vector2d(-41, -20))
                                .splineToConstantHeading(new Vector2d(-58, -12), Math.toRadians(180))
                                .waitSeconds(1)
                                .lineTo(new Vector2d(8, -12))
                                .splineToConstantHeading(new Vector2d(50, -28), Math.toRadians(0))
                                .waitSeconds(1)
                                .splineToConstantHeading(new Vector2d(30, -61), Math.toRadians(180))
                                .waitSeconds(1)
                                .lineToConstantHeading(new Vector2d(31, -61))
                                .waitSeconds(1)

                                .splineToConstantHeading(new Vector2d(50, -28), Math.toRadians(90))
                                .waitSeconds(1)
                                .splineToConstantHeading(new Vector2d(22, -12), Math.toRadians(180))
                                .lineToConstantHeading(new Vector2d(-58, -12))
                                .waitSeconds(1)

                                .splineTo(new Vector2d(50, -28), Math.toRadians(0))
                                .waitSeconds(1)

                                .lineTo(new Vector2d(50, -26))
                                .splineToConstantHeading(new Vector2d(58, -12), Math.toRadians(0))


                                .build()
                );



        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
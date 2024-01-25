package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.SampleMecanumDrive;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequenceBuilder;

import java.util.Vector;

public class MeepMeepTesting {

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(700);

        Pose2d blueFarStart = new Pose2d(-36,61,Math.toRadians(270));
        Pose2d blueCloseStart = new Pose2d(12,61,Math.toRadians(270));
        Pose2d redFarStart = new Pose2d(-36,-61,Math.toRadians(90));
        Pose2d redCloseStart = new Pose2d(12,-61,Math.toRadians(90));


      /*
        TrajectorySequence midPurple = drive.trajectorySequenceBuilder(blueCloseStart)
                .lineTo(new Vector2d(12, -34))
                .build();
        TrajectorySequence midYellow = drive.trajectorySequenceBuilder(midPurple.end())
                .lineToLinearHeading(new Pose2d(51, -33, Math.toRadians(180)))
                .build();
        TrajectorySequence midStack = drive.trajectorySequenceBuilder(midYellow.end())
                .splineToConstantHeading(new Vector2d(30, -57), Math.toRadians(180))
                .lineToConstantHeading(new Vector2d(-25, -57))
                .splineToConstantHeading(new Vector2d(-56, 35), Math.toRadians(180))
                .build();
        TrajectorySequence midBack = drive.trajectorySequenceBuilder(midStack.end())
                .lineToConstantHeading(new Vector2d(-54, -36))
                .splineToConstantHeading(new Vector2d(-35, -57), Math.toRadians(0))
                .lineToConstantHeading(new Vector2d(24, -57))
                .splineToConstantHeading(new Vector2d(51, -29), Math.toRadians(0))
                .build();
        TrajectorySequence rightPurple = drive.trajectorySequenceBuilder(blueCloseStart)
                .lineTo(new Vector2d(13, -60))
                .splineToConstantHeading(new Vector2d(25, -37), Math.toRadians(270))
                .build();
        TrajectorySequence rightYellow = drive.trajectorySequenceBuilder(rightPurple.end())
                .lineToLinearHeading(new Pose2d(52, -40, Math.toRadians(180)))
                .build();

       */
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(redCloseStart)
                                .lineTo(new Vector2d(12, -45))
                                .splineToSplineHeading(new Pose2d(9, -38,Math.toRadians(135)), Math.toRadians(135))
                                .lineToLinearHeading(new Pose2d(52, -27, Math.toRadians(180)))
                                .build()
                );
        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
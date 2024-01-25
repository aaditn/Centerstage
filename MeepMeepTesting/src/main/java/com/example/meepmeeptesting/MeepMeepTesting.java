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


        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-48, -36, Math.toRadians(90)))
                                .lineTo(new Vector2d(-48, -37))
                                .splineToSplineHeading(new Pose2d(-34,-57, Math.toRadians(180)), Math.toRadians(0))
                                .lineToConstantHeading(new Vector2d(20,-57))
                                .splineToConstantHeading(new Vector2d(49, -26), Math.toRadians(0))
                                .build()
                );



        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
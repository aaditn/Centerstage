package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.example.meepmeeptesting.notWeirdStuff.BotBuilder;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(700);

//        RoadRunnerBotEntity rightRedFar = new BotBuilder(meepMeep, new ColorSchemeBlueLight())
//                .followTrajectorySequence(BigT_AKA_Trajectory.rightTrajectories);
//        RoadRunnerBotEntity leftRedFar = new BotBuilder(meepMeep, new ColorSchemeRedDark())
//                .followTrajectorySequence(BigT_AKA_Trajectory.leftTrajectories);

        RoadRunnerBotEntity midRedFar = new BotBuilder(meepMeep, new ColorSchemeRedDark())
                .followTrajectorySequence(BigT_AKA_Trajectory.midTrajectories);
        RoadRunnerBotEntity midRedClose = new BotBuilder(meepMeep, new ColorSchemeRedDark())
                .followTrajectorySequence(BigT_2_6_Brother.midTrajectories);



        RoadRunnerBotEntity WHYAMIHERE = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setColorScheme(new ColorSchemeRedDark())
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(0, 0, 0))
                                .lineTo(new Vector2d(5, 0))
                                .turn(Math.toRadians(-45))

                                .build()
                );



        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(midRedClose)
//                .addEntity(midRedClose)

//               .addEntity(WHYAMIHERE)
                .start();
    }


}
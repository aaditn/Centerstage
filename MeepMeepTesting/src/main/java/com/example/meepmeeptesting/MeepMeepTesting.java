package com.example.meepmeeptesting;

import com.example.meepmeeptesting.notWeirdStuff.BotBuilder;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
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







        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(midRedFar)

//               .addEntity(WHYAMIHERE)
                .start();
    }


}
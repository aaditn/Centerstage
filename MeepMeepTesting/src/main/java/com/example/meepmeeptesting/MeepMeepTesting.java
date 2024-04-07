package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.example.meepmeeptesting.WE_ARE_NOT_JUST_TELLING_STORIES_WE_ARE_CHANGING_LIVES.NamedTrajectory;
import com.example.meepmeeptesting.WE_ARE_NOT_JUST_TELLING_STORIES_WE_ARE_CHANGING_LIVES.storeStuff;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.util.ArrayList;
import java.util.List;

public class MeepMeepTesting {

    public static void main(String[] args) throws InterruptedException {
        MeepMeep meepMeep = new MeepMeep(800);

        BigT t = new BigT();

        List<Action> actions = new ArrayList<>();

        for (NamedTrajectory i: t.leftTrajectories) {
            actions.add(i.getAction());
        }

        Action seqAction = new SequentialAction(actions);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(storeStuff.MAX_VEL, storeStuff.MAX_ACCEL, storeStuff.MAX_ANGLE_VEL, storeStuff.MAX_ANGLE_ACCEL, storeStuff.TRACK_WIDTH)
                .build();
        myBot.runAction(seqAction);
        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
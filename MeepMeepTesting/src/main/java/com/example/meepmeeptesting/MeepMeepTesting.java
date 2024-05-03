package com.example.meepmeeptesting;

import static com.example.meepmeeptesting.WE_ARE_NOT_JUST_TELLING_STORIES_WE_ARE_CHANGING_LIVES.storeStuff.robot;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.example.meepmeeptesting.WE_ARE_NOT_JUST_TELLING_STORIES_WE_ARE_CHANGING_LIVES.storeStuff;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {

    public static void main(String[] args) throws InterruptedException {
        MeepMeep meepMeep = new MeepMeep(800);





        Action back1 = robot.actionBuilder(new Pose2d(-58, -8, Math.toRadians(180)))
                .setTangent(Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(0, -8, Math.toRadians(180)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(48, -30), Math.toRadians(0))
                .build();

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(storeStuff.MAX_VEL, storeStuff.MAX_ACCEL, storeStuff.MAX_ANGLE_VEL, storeStuff.MAX_ANGLE_ACCEL, storeStuff.TRACK_WIDTH)
                .build();
        myBot.runAction(back1);
        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
package com.example.meepmeeptesting;

import static com.example.meepmeeptesting.BigT.closeRedStart;
import static com.example.meepmeeptesting.WE_ARE_NOT_JUST_TELLING_STORIES_WE_ARE_CHANGING_LIVES.storeStuff.robot;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.example.meepmeeptesting.WE_ARE_NOT_JUST_TELLING_STORIES_WE_ARE_CHANGING_LIVES.storeStuff;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {

    public static void main(String[] args) throws InterruptedException {
        MeepMeep meepMeep = new MeepMeep(800);




        Action purple = robot.actionBuilder(closeRedStart)
                .splineToConstantHeading(new Vector2d(closeRedStart.position.x, -50), closeRedStart.heading.toDouble())
                .splineToSplineHeading(new Pose2d(10, -38, Math.toRadians(150)), Math.toRadians(100))
                .build();
        Action yellow = robot.actionBuilder(new Pose2d(10, -38, Math.toRadians(150)))
                .strafeTo(new Vector2d(15, -37))
                .splineToSplineHeading(new Pose2d(35, -26, Math.toRadians(180)), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(48, -26, Math.toRadians(180)), Math.toRadians(0))
                .build();
        Action stack1 = robot.actionBuilder(new Pose2d(48, -26, Math.toRadians(180)))
                .splineToSplineHeading(new Pose2d(30, -9, Math.toRadians(180)), Math.toRadians(180))
                .lineToXSplineHeading(-58, Math.toRadians(180))
                .build();
        Action back1 = robot.actionBuilder(new Pose2d(-58, -9, Math.toRadians(180)))
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(0, -9), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(48, -26, Math.toRadians(150)), Math.toRadians(-30))
                .build();

        SequentialAction seq = new SequentialAction(
                purple, yellow, stack1, back1
        );

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(storeStuff.MAX_VEL, storeStuff.MAX_ACCEL, storeStuff.MAX_ANGLE_VEL, storeStuff.MAX_ANGLE_ACCEL, storeStuff.TRACK_WIDTH)
                .build();
        myBot.runAction(seq);
        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
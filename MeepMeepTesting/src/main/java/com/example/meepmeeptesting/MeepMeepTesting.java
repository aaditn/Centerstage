package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(700);

        Pose2d blueFarStart = new Pose2d(-36,61,Math.toRadians(270));
        Pose2d blueCloseStart = new Pose2d(12,61,Math.toRadians(270));
        Pose2d redFarStart = new Pose2d(-36,-61,Math.toRadians(90));
        Pose2d redCloseStart = new Pose2d(12,-61,Math.toRadians(90));
//        TrajectorySequence midPurple = drive.trajectorySequenceBuilder(redFarStart)
//                .lineTo(new Vector2d(-36, 34))
//        TrajectorySequence midPurpleToTurn = drive.trajectorySequenceBuilder(midPurple.end())
//                .lineTo(new Vector2d(-38, 37),
//                        drive.getVelocityConstraint(30, 2, 15.06),
//                        drive.getAccelerationConstraint(35))
//                .splineToSplineHeading(new Pose2d(-38, 57, Math.toRadians(180)), Math.toRadians(90),
//                        drive.getVelocityConstraint(35, 2, 15.06),
//                        drive.getAccelerationConstraint(40))
//
//        TrajectorySequence midPurpleToBack = drive.trajectorySequenceBuilder(midPurpleToTurn.end())
//                .lineToConstantHeading(new Vector2d(20,57),
//                        drive.getVelocityConstraint(50, 2.4, 15.06),
//                        drive.getAccelerationConstraint(40))

//        TrajectorySequence midBackToTurn = drive.trajectorySequenceBuilder(midPurpleToBack.end())
//                .lineToLinearHeading(new Pose2d(35, 58, Math.toRadians(175)))
//        TrajectorySequence midBackToStack = drive.trajectorySequenceBuilder(midBackToTurn.end())
//                .lineTo(new Vector2d(-36, 34))
//                .waitSeconds(1)
//                .lineTo(new Vector2d(-38, 37))
//                .splineToSplineHeading(new Pose2d(-38, 57, Math.toRadians(180)), Math.toRadians(90))
//                .waitSeconds(1)
//                .lineToConstantHeading(new Vector2d(20,57))
//                .waitSeconds(1)
//                .lineToLinearHeading(new Pose2d(35, 58, Math.toRadians(175)))
//                .setReversed(false)
//                .lineToConstantHeading(new Vector2d(-28,63))
//                .splineToConstantHeading(new Vector2d(-55, 34), Math.toRadians(180))
//                .lineTo(new Vector2d(-57.5, 34))
//                .build()



        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(blueFarStart)

                                .splineToConstantHeading(new Vector2d(-46.5, 36),Math.toRadians(270))

                                .setReversed(true)
                                .lineTo(new Vector2d(-46.5, 37))
                                .splineToSplineHeading(new Pose2d(-34,57, Math.toRadians(180)), Math.toRadians(0))
                                .lineToConstantHeading(new Vector2d(20,57))
                                .splineToConstantHeading(new Vector2d(49.5, 26), Math.toRadians(0))
                                .build()
                );


        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
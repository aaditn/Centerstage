package com.example.meepmeeptesting.weirdStuff;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.ColorScheme;
import com.noahbres.meepmeep.roadrunner.Constraints;
import com.noahbres.meepmeep.roadrunner.DriveTrainType;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.sequencesegment.SequenceSegment;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

public class BotBuilder {

    private MeepMeep meepMeep = new MeepMeep(700);

    private Constraints constraints = new Constraints(
            30.0,
            30.0,
            Math.toRadians(60.0),
            Math.toRadians(60.0),
            15.0);

    private double width = 18.0;
    private double height = 18.0;
    private ColorScheme colorScheme;
    private double  opacity = 0.8;

    private DriveTrainType driveTrainType = DriveTrainType.MECANUM;

    public BotBuilder(MeepMeep meepmeep, ColorScheme colorScheme) {
        this.meepMeep = meepmeep;
        this.colorScheme = colorScheme;
    }
    public RoadRunnerBotEntity followTrajectorySequence(NamedTrajectory[] trajectorySequence) {
        TrajectorySequence seq = makeListToSequence(trajectorySequence);
        RoadRunnerBotEntity bot = this.build(seq.start());
        bot.followTrajectorySequence(seq);
        return bot;
    }

    public static TrajectorySequence makeListToSequence(NamedTrajectory[] sequence) {
        ArrayList<SequenceSegment> segmentList = new ArrayList<>();
        for (int i = 0; i < sequence.length; i++) {
            for (int j = 0; j < sequence[i].size(); j++) {
                segmentList.add(sequence[i].get(j));
            }
        }
        return new TrajectorySequence(segmentList);
    }
    @Contract(" -> new")
    private RoadRunnerBotEntity build(Pose2d startPose) {
        return new RoadRunnerBotEntity(
                meepMeep,
                constraints,
                width, height,
                startPose,
                colorScheme,
                opacity,
                driveTrainType, false
        );
    }

}

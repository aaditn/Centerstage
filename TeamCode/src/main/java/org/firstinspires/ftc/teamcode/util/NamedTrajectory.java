package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.sequencesegment.SequenceSegment;

import java.util.ArrayList;
import java.util.List;

public class NamedTrajectory extends TrajectorySequence{
    String name;
    NamedTrajectory(List<SequenceSegment>sequence, String name){
        super(sequence);
        this.name =name;
    }
    public String getName(){
        return name;
    }
    public static NamedTrajectory[] map(TrajectorySequence[] trajs,  String[] names){
        List<NamedTrajectory> added = new ArrayList<>();
        for(int i =0; i<trajs.length;i++){
            added.add(new NamedTrajectory(trajs[i].getList(),names[i]));
        }
        return added.toArray(new NamedTrajectory[trajs.length]);
    }

}

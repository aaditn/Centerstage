package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.sequencesegment.SequenceSegment;
import org.firstinspires.ftc.teamcode.util.enums.Paths;

import java.util.ArrayList;
import java.util.List;

public class NamedTrajectory extends TrajectorySequence{
    Paths name;
    NamedTrajectory(List<SequenceSegment>sequence, Paths path){
        super(sequence);
        this.name =path;
    }
    public Paths getName(){
        return name;
    }
    public static NamedTrajectory[] map(TrajectorySequence[] trajs,  Paths[] paths){
        List<NamedTrajectory> added = new ArrayList<>();
        for(int i =0; i<trajs.length;i++){
            added.add(new NamedTrajectory(trajs[i].getList(),paths[i]));
        }
        return added.toArray(new NamedTrajectory[trajs.length]);
    }

}

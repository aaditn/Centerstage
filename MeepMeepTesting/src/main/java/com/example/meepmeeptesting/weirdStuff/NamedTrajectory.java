package com.example.meepmeeptesting.weirdStuff;


import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.sequencesegment.SequenceSegment;

import java.util.ArrayList;
import java.util.List;

public class NamedTrajectory extends TrajectorySequence {
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
            added.add(new NamedTrajectory(getList(trajs[i]),paths[i]));
        }
        return added.toArray(new NamedTrajectory[trajs.length]);
    }

    public static List<SequenceSegment> getList(TrajectorySequence s) {
        ArrayList<SequenceSegment> res = new ArrayList<>();
        for (int i = 0; i < s.size(); i++) {
            res.add(s.get(i));
        }
        return res;
    }


}

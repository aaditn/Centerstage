package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.roadrunner.TimeTrajectory;
import com.acmerobotics.roadrunner.Trajectory;

import org.firstinspires.ftc.teamcode.util.enums.Paths;

import java.util.ArrayList;
import java.util.List;

public class NamedTrajectory {
    Paths name;
    List<Trajectory> action;
    NamedTrajectory(List<Trajectory> action, Paths path){
        this.action = action;
        this.name =path;
    }
    public Paths getName(){
        return name;
    }

    public List<TimeTrajectory> getAction() {
        List<TimeTrajectory> list = new ArrayList<>();
        for (Trajectory t: action) {
            list.add(new TimeTrajectory(t));
        }
        return list;
    }
    public static NamedTrajectory[] map(List<List<Trajectory>> trajs, Paths[] paths){
        List<NamedTrajectory> added = new ArrayList<>();
        for(int i =0; i<trajs.size();i++){
            added.add(new NamedTrajectory(trajs.get(i),paths[i]));
        }
        return added.toArray(new NamedTrajectory[trajs.size()]);
    }
}

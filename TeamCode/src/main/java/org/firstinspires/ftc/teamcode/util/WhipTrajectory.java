package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;

import java.util.ArrayList;
import java.util.List;

public class WhipTrajectory {
    TrajectorySequence trajectory;
    List<Task> tasks;
    String name;
    WhipTrajectory(NamedTrajectory trajectory,List<Task>tasks){
        this.trajectory = trajectory;
        this.tasks = tasks;
        this.name =trajectory.name;
    }
    public TrajectorySequence getTrajectory(){
        return trajectory;
    }
    public List<Task> getTasks(){
        return tasks;
    }
    public String getName(){
        return name;
    }
    public static List<WhipTrajectory> map(NamedTrajectory[] trajs, List<List<Task>> taskLists){
        List<WhipTrajectory> added = new ArrayList<>();
        for(int i =0; i<trajs.length;i++){
            added.add(new WhipTrajectory(trajs[i],taskLists.get(i)));
        }
        return added;
    }

}

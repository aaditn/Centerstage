package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;

import java.util.ArrayList;
import java.util.List;

public class WhipTrajectory {
    TrajectorySequence trajectory;
    List<Task> tasks;
    WhipTrajectory(TrajectorySequence trajectory,List<Task>tasks){
        this.trajectory = trajectory;
        this.tasks = tasks;
    }
    public TrajectorySequence getTrajectory(){
        return trajectory;
    }
    public List<Task> getTasks(){
        return tasks;
    }
    public static List<WhipTrajectory> map(List<TrajectorySequence> trajs, List<List<Task>> taskLists){
        List<WhipTrajectory> added = new ArrayList<>();
        for(int i =0; i<trajs.size();i++){
            added.add(new WhipTrajectory(trajs.get(i),taskLists.get(i)));
        }
        return added;
    }

}

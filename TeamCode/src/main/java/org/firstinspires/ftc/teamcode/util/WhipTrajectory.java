package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.util.enums.Paths;

import java.util.ArrayList;
import java.util.List;

public class WhipTrajectory {
    TrajectorySequence trajectory;
    List<Task> tasks;
    Paths path;
    WhipTrajectory(NamedTrajectory trajectory,List<Task>tasks){
        this.trajectory = trajectory;
        this.tasks = tasks;
        this.path =trajectory.name;
    }
    public TrajectorySequence getTrajectory(){
        return trajectory;
    }
    public List<Task> getTasks(){
        return tasks;
    }
    public Paths getPath(){
        return path;
    }
    public static List<WhipTrajectory> map(NamedTrajectory[] trajs, List<Task>[] taskLists){
        List<WhipTrajectory> added = new ArrayList<>();
        if(Context.opmode.opModeIsActive())
        {
            for(int i =0; i<trajs.length;i++){
                if(i<taskLists.length) {
                    added.add(new WhipTrajectory(trajs[i], taskLists[i]));
                }
                else added.add(new WhipTrajectory(trajs[i], new ArrayList<>()));
            }
        }
        return added;
    }

}

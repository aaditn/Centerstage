package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.util.enums.Paths;

import java.util.ArrayList;
import java.util.List;

public class NamedTrajectory {
    Paths name;
    Action action;
    NamedTrajectory(Action action, Paths path){
        this.action = action;
        this.name =path;
    }
    public Paths getName(){
        return name;
    }

    public Action getAction() {
        return action;
    }
    public static NamedTrajectory[] map(Action[] trajs,  Paths[] paths){
        List<NamedTrajectory> added = new ArrayList<>();
        for(int i =0; i<trajs.length;i++){
            added.add(new NamedTrajectory(trajs[i],paths[i]));
        }
        return added.toArray(new NamedTrajectory[trajs.length]);
    }
}

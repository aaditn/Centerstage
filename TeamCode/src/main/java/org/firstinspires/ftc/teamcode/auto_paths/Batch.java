package org.firstinspires.ftc.teamcode.auto_paths;

import org.firstinspires.ftc.teamcode.auto_paths.door_paths.closeBlueDoor;
import org.firstinspires.ftc.teamcode.auto_paths.door_paths.closeRedDoor;
import org.firstinspires.ftc.teamcode.auto_paths.door_paths.farBlueDoor;
import org.firstinspires.ftc.teamcode.auto_paths.door_paths.farRedDoor;
import org.firstinspires.ftc.teamcode.auto_paths.truss_paths.closeBlueTruss;
import org.firstinspires.ftc.teamcode.auto_paths.truss_paths.closeRedTruss;
import org.firstinspires.ftc.teamcode.auto_paths.truss_paths.farBlueTruss;
import org.firstinspires.ftc.teamcode.auto_paths.truss_paths.farRedTruss;
import org.firstinspires.ftc.teamcode.util.NamedTrajectory;

import java.util.ArrayList;
import java.util.List;

public class Batch {
    public static List<NamedTrajectory[][]> allTrajectories = new ArrayList<NamedTrajectory[][]>(){
        {
            add(farRedTruss.trajectories);
            add(farBlueTruss.trajectories);
            add(closeRedTruss.trajectories);
            add(closeBlueTruss.trajectories);
            add(farRedDoor.trajectories);
            add(farBlueDoor.trajectories);
            add(closeRedDoor.trajectories);
            add(closeBlueDoor.trajectories);
        }
    };
}

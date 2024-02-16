package org.firstinspires.ftc.teamcode.auto_paths;

import com.acmerobotics.roadrunner.geometry.Pose2d;

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
    public static List<Pose2d> allStarts = new ArrayList<Pose2d>(){{
        add(farRedTruss.redFarStart);
        add(farBlueTruss.blueFarStart);
        add(closeRedTruss.redCloseStart);
        add(closeBlueTruss.blueCloseStart);
        add(farRedDoor.redFarStart);
        add(farBlueDoor.blueFarStart);
        add(closeRedDoor.redCloseStart);
        add(closeBlueDoor.blueCloseStart);
    }
    };
}

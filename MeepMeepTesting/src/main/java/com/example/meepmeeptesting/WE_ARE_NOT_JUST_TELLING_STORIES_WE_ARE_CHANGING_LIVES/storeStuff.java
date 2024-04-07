package com.example.meepmeeptesting.WE_ARE_NOT_JUST_TELLING_STORIES_WE_ARE_CHANGING_LIVES;

import com.acmerobotics.roadrunner.Pose2d;
import com.noahbres.meepmeep.roadrunner.Constraints;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.DriveTrainType;

public class storeStuff {
    public static double MAX_VEL = 60;
    public static double MAX_ACCEL = 50;
    public static double MAX_ANGLE_VEL = 3;
    public static double MAX_ANGLE_ACCEL = 3;
    public static double TRACK_WIDTH = 12;
    public static DriveShim robot = new DriveShim(
            DriveTrainType.MECANUM,
            new Constraints(MAX_VEL, MAX_ACCEL, MAX_ANGLE_VEL, MAX_ANGLE_ACCEL, TRACK_WIDTH),
            new Pose2d(0,0,0)
    );
}

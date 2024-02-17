package org.firstinspires.ftc.teamcode;

import android.content.Context;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.ftccommon.external.OnCreate;
import org.firstinspires.ftc.robotcore.internal.opmode.RegisteredOpModes;
import org.firstinspires.ftc.teamcode.auto_paths.Batch;
import org.firstinspires.ftc.teamcode.util.NamedTrajectory;

import java.util.List;

public class staticCreate implements OpModeManagerImpl.Notifications {

    @OnCreate
    public static void start(Context context) {
        ElapsedTime timeToInit = new ElapsedTime();
        RobotLog.e("Static Force Initialization Begins");
        List<NamedTrajectory[][]> batchInit  = Batch.allTrajectories;
        RobotLog.e("All Trajectories Loaded in: "+ timeToInit.seconds()
                +", Mark: " + batchInit.toString());
        List<Pose2d> batchInitPT2 = Batch.allStarts;
        RobotLog.e("All Start Pos Loaded in: "+timeToInit.seconds()+", Mark: " +batchInitPT2.toString());
        while(true){

            if(RegisteredOpModes.getInstance().getExternalLibrariesChanged()){
                ElapsedTime timeToInit3 = new ElapsedTime();
                RobotLog.e("Static Force Initialization Begins");
                List<NamedTrajectory[][]> batchInit6  = Batch.allTrajectories;
                RobotLog.e("All Trajectories Loaded in: "+ timeToInit.seconds()
                        +", Mark: " + batchInit.toString());
                List<Pose2d> batchInitPT26 = Batch.allStarts;
                RobotLog.e("All Start Pos Loaded in: "+timeToInit.seconds()+", Mark: " +batchInitPT2.toString());
            }
            ElapsedTime x  = new ElapsedTime();
            while(x.seconds()<1){
                
            }
        }
    }

    @Override
    public void onOpModePreInit(OpMode opMode) {

    }

    @Override
    public void onOpModePreStart(OpMode opMode) {

    }

    @Override
    public void onOpModePostStop(OpMode opMode) {

    }
}
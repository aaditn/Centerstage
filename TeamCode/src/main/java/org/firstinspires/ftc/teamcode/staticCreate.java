package org.firstinspires.ftc.teamcode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.ftccommon.external.OnCreate;
import org.firstinspires.ftc.ftccommon.external.OnDestroy;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.auto_paths.Batch;
import org.firstinspires.ftc.teamcode.util.NamedTrajectory;

import java.util.List;

public class staticCreate {

    static boolean malding=false;
    static ElapsedTime suffering;
    private static final BroadcastReceiver fastReloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            malding=true;
            suffering.reset();
        }
    };
    @OnCreate
    public static void start(Context context) {
        ElapsedTime timeToInit = new ElapsedTime();
        suffering=new ElapsedTime();
        RobotLog.e("Static Force Initialization Begins");
        List<NamedTrajectory[][]> batchInit  = Batch.allTrajectories;
        RobotLog.e("All Trajectories Loaded in: "+ timeToInit.seconds()
                +", Mark: " + batchInit.toString());
        List<Pose2d> batchInitPT2 = Batch.allStarts;
        RobotLog.e("All Start Pos Loaded in: "+timeToInit.seconds()+", Mark: " +batchInitPT2.toString());
        AppUtil.getDefContext().registerReceiver(fastReloadReceiver, new IntentFilter("team11260.RELOAD_FAST_LOAD"));
        //RegisteredOpModes.getInstance().register("Z", RegistrationOpModeTest.class);

        (new Thread()
        {
            @Override public void run()
            {
                OpModeManagerImpl thing = OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getRootActivity());
                while(!isInterrupted())
                {
                    RobotLog.e("Running in bg" + malding);

                        if(malding &&suffering.milliseconds()>10000){
                        ElapsedTime timeToInit3 = new ElapsedTime();
                        RobotLog.e("Static Force Initialization Begins");
                        List<NamedTrajectory[][]> batchInit6  = Batch.allTrajectories;
                        RobotLog.e("All Trajectories Loaded in: "+ timeToInit3.seconds()
                                +", Mark: " + batchInit.toString());
                        List<Pose2d> batchInitPT26 = Batch.allStarts;
                        RobotLog.e("All Start Pos Loaded in: "+timeToInit3.seconds()+", Mark: " +batchInitPT2.toString());
                        malding=false;
                        }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }).start();

    }
    @OnDestroy
    public static void stop(Context context)
    {
        AppUtil.getDefContext().unregisterReceiver(fastReloadReceiver);
    }
}
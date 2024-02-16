package org.firstinspires.ftc.teamcode;

import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.ftccommon.external.OnCreate;
import org.firstinspires.ftc.teamcode.auto_paths.Batch;
import org.firstinspires.ftc.teamcode.util.NamedTrajectory;

import java.util.List;

public class staticCreate implements OpModeManagerImpl.Notifications {

    @OnCreate
    public static void start(Context context) {
        ElapsedTime timeToInit = new ElapsedTime();
        RobotLog.e("kai was here");
        List<NamedTrajectory[][]> batchInit  = Batch.allTrajectories;
        RobotLog.e(batchInit.toString()+ timeToInit.seconds());
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
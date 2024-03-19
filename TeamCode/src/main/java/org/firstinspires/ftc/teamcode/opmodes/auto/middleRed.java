package org.firstinspires.ftc.teamcode.opmodes.auto;


import static org.firstinspires.ftc.teamcode.Robot.getTaskList;
import static org.firstinspires.ftc.teamcode.auto_paths.door_paths.middleRed.redFarStart;
import static org.firstinspires.ftc.teamcode.auto_paths.door_paths.middleRed.trajectories;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.auto_paths.Batch;
import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.DroneLauncher;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.RobotActions;
import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.util.NamedTrajectory;
import org.firstinspires.ftc.teamcode.util.enums.Paths;

import java.util.List;

@Autonomous(name = "Middle Red")
public class middleRed extends EnhancedOpMode {
    Robot drive;
    TaskScheduler scheduler;
    RobotActions actions;
    Deposit deposit;
    Intake intake;
    Slides slides;
    DroneLauncher drone;

   /* double aprilTagConfidence =.1;
    private AprilTagPipeline vision = new AprilTagPipeline();

    */


   // List<Pose2d> previous = new ArrayList<>();

    private List<Task>[] auto_tasks() {
        return getTaskList(
                actions.deployPurple(-49, -40),
                actions.yellowMiddleDrop(48, -15, Slides.SlideState.HALF),
                actions.lowerIntake(0, -51.5, 1, true),
                actions.yellowMiddleDrop(46, -15,  Slides.SlideState.R1),
                actions.lowerIntake(0, -51.5, 2, true),
                actions.yellowMiddleDrop(46, -15, Slides.SlideState.R2)
//                actions.scorePixels(48.5, TeleOpRewrite.DepositState.RIGHT, -38, Slides.SlideState.ROW2)
        );
    }
    @Override
    public void linearOpMode() {
        ElapsedTime timeToInit3 = new ElapsedTime();
        RobotLog.e("Static Force Initialization Begins");
        List<NamedTrajectory[][]> batchInit6  = Batch.allTrajectories;
        RobotLog.e("All Trajectories Loaded in: "+ timeToInit3.seconds()
                +", Mark: " + batchInit6.toString() +",Key: "+batchInit6.get(0)[0][0].getName());
        List<Pose2d> batchInitPT26 = Batch.allStarts;
        RobotLog.e("All Start Pos Loaded in: "+timeToInit3.seconds()+", Mark: " +batchInitPT26.toString());
        waitForStart();

        if(opModeIsActive())
        {
            drive.setPoseEstimate(redFarStart);
            delayLinear((long)Context.autoWaitTime*1000);
            drive.set(trajectories,auto_tasks());
            RobotLog.e("things mapped");
            drive.run(Paths.Purple);
            RobotLog.e("purple");
            drive.run(Paths.Score_Spike);
            RobotLog.e("yellow");
            delayLinear(400);
            RobotLog.e("delaying");
            drive.run(Paths.Go_To_Stack);
            delayLinear(600);
            drive.run(Paths.Score_First);
            delayLinear(250);
            drive.run(Paths.Return_to_Stack);
            delayLinear(600);
            drive.run(Paths.Score_Second);
            waitForEnd();
            RobotLog.e("end");
        }
    }

    public void initLoop()
    {
        drive.initLoop();
    }

    public void onStart() {
       // drive.closeCameras();
    }

    @Override
    public void initialize() {
        this.setLoopTimes(10);
        drive = Robot.getInstance();

        Context.isTeamRed = true;
        scheduler = new TaskScheduler();
        actions = RobotActions.getInstance();
        deposit = drive.deposit;
        intake = drive.intake;
        slides = drive.slides;
        drone = drive.droneLauncher;

        intake.init();
        deposit.init();
        slides.init();
        drone.init();
        intake.setOperationState(Module.OperationState.PRESET);
        intake.setState(Intake.SweeperState.ZERO);
        deposit.setState(Deposit.FlipState.TRANSFER);
        deposit.setState(Deposit.ClawState.OPEN);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

      /*  vision.initAprilTag(hardwareMap);

        int count = 0;

        vision.setEstHeading(drive.getPoseEstimate().getHeading());
        previous.add(new Pose2d(0,0,0));
        previous.add(new Pose2d(0,0,0));
        previous.add(new Pose2d(0,0,0));
        previous.add(new Pose2d(0,0,0));
        previous.add(new Pose2d(0,0,0));


        previous.add(new Pose2d(0,0,0));

       */
        //RobotLog.e("ROLLIT HAHAHAHAHAHAHAHAHAHAHAHA");
    }

    @Override
    public void primaryLoop() {
        drive.primaryLoop();
    }


}

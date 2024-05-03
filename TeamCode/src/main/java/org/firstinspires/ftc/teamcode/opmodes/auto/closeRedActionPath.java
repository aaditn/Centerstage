package org.firstinspires.ftc.teamcode.opmodes.auto;


import static org.firstinspires.ftc.teamcode.auto_paths.closeRedPath.closeRedStart;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.DroneLauncher;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.RobotActions;
import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

@Autonomous(name = "Close Red Actions")
public class closeRedActionPath extends EnhancedOpMode {
    public static Robot drive;

    Action purple, yellow, stack1, back1;
    TaskScheduler scheduler;
    RobotActions actions;
    Deposit deposit;
    Intake intake;
    Slides slides;
    DroneLauncher drone;

    @Override
    public void linearOpMode() {

        ElapsedTime time = new ElapsedTime();

        RobotLog.e("Starting Time Trajectory" + time.milliseconds());
        RobotLog.e("Ending Time Trajectory" + time.milliseconds());
        RobotLog.e("Static Force Initialization Begins");

        purple = drive.actionBuilder(closeRedStart)
                .splineToConstantHeading(new Vector2d(closeRedStart.position.x, -50), closeRedStart.heading.toDouble())
                .splineToSplineHeading(new Pose2d(10, -38, Math.toRadians(150)), Math.toRadians(100))
                .build();
//        yellow = drive.actionBuilder(drive.pose)
//                .splineToConstantHeading(new Vector2d(30, -30), Math.toRadians(0))
//                .splineToSplineHeading(new Pose2d(48, -30, Math.toRadians(180)), Math.toRadians(0))
//                .build();
//        stack1 = drive.actionBuilder(drive.pose)
//                .splineToConstantHeading(new Vector2d(30, -8), Math.toRadians(180))
//                .splineToConstantHeading(new Vector2d(-58, -8), Math.toRadians(180))
//                .build();
//        back1 = drive.actionBuilder(drive.pose)
//                .splineToConstantHeading(new Vector2d(0, -8), Math.toRadians(0))
//                .splineToConstantHeading(new Vector2d(48, -30), Math.toRadians(0))
//                .build();


// 608.74777
        waitForStart();

        if(opModeIsActive())
        {
            drive.pose = closeRedStart;
            Actions.runBlocking(purple);
            yellow = drive.actionBuilder(drive.pose)
                    .strafeTo(new Vector2d(15, -37))
                    .splineToSplineHeading(new Pose2d(35, -30, Math.toRadians(180)), Math.toRadians(0))
                    .splineToSplineHeading(new Pose2d(48, -30, Math.toRadians(180)), Math.toRadians(0))
                    .build();
            Actions.runBlocking(yellow);
            stack1 = drive.actionBuilder(drive.pose)
                    .splineToSplineHeading(new Pose2d(20, -8, Math.toRadians(180)), Math.toRadians(180))
                    .splineToConstantHeading(new Vector2d(-58, -8), Math.toRadians(180))
                    .build();
            Actions.runBlocking(stack1);

            back1 = drive.actionBuilder(drive.pose)
                    .setTangent(Math.toRadians(0))
                    .splineToSplineHeading(new Pose2d(0, -8, Math.toRadians(180)), Math.toRadians(0))
                    .splineToConstantHeading(new Vector2d(48, -30), Math.toRadians(0))
                    .build();
            Actions.runBlocking(back1);



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

    public void turnOffMotors() {
        drive.leftFront.setPower(0);
        drive.leftBack.setPower(0);
        drive.rightBack.setPower(0);
        drive.rightFront.setPower(0);
        MecanumDrive.isBusy = false;
    }
    @Override
    public void initialize() {
        this.setLoopTimes(10);

        drive = Robot.getInstance();

        Context.isTeamRed = true;
        scheduler = new TaskScheduler();
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

        actions = RobotActions.getInstance();





      /*  vision.initAprilTag(hardwareMap);

        int count = 0;

        vision.setEstHeading(drive.pose.heading.toDouble());
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

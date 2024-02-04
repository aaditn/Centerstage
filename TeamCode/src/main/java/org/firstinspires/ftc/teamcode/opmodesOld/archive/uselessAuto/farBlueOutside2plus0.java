package org.firstinspires.ftc.teamcode.opmodesOld.archive.uselessAuto;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.DroneLauncher;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.RobotActions;
import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;
@Disabled
@Autonomous
public class farBlueOutside2plus0 extends EnhancedOpMode {
    int dice =0;
    Pose2d blueFarStart = new Pose2d(-36,61,Math.toRadians(270));
    Robot drive;
    Deposit deposit;
    Intake intake;
    Slides slides;
    DroneLauncher drone;
    boolean offset;
    TaskScheduler scheduler;
    RobotActions actions;

    @Override
    public void primaryLoop() {
        drive.primaryLoop();
    }
    public void initLoop()
    {

        if(drive.teamElementDetector.centerY < 0) {
            dice = 3;
        } else if(drive.teamElementDetector.centerY < 107){
            dice = 1;
        } else {
            dice = 2;
        }
        Context.tel.addData("Team red?", Context.isTeamRed);
        Context.tel.addData("element Pos", dice);
        Context.tel.addData("centerY", drive.teamElementDetector.centerY);
        Context.tel.addData("largest area", drive.teamElementDetector.getLargestArea());
        //Context.tel.update();
        drive.initLoop();
    }
    public void onStart()
    {
        drive.closeCameras();
    }
    public void linearOpMode() {
        offset=false;
        TrajectorySequence rightPurple = drive.trajectorySequenceBuilder(blueFarStart)
                .splineToLinearHeading(new Pose2d(-40, 38, Math.toRadians(180)),Math.toRadians(180))
                .addTemporalMarker(0.5, () ->  intake.setState(Intake.PositionState.DOWN))
                .build();
        TrajectorySequence rightPurpleToTurn = drive.trajectorySequenceBuilder(rightPurple.end())

                .lineTo(new Vector2d(-38, 37))
                .splineToLinearHeading((new Pose2d(-31,12, Math.toRadians(180))), Math.toRadians(0))
                .build();
        TrajectorySequence rightPurpleToBack = drive.trajectorySequenceBuilder(rightPurpleToTurn.end())
                .lineToConstantHeading(new Vector2d(20,12))
                .addSpatialMarker(new Vector2d(-20, 57), () ->  {
                    scheduler.scheduleTaskList(actions.autoRaiseSlides(Slides.SlideState.AUTO_LOW));
                    intake.setState(Intake.PowerState.OFF);
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .splineToConstantHeading(new Vector2d(51, 23), Math.toRadians(0))
                .build();
        TrajectorySequence leftPurple = drive.trajectorySequenceBuilder(blueFarStart)
                .lineTo(new Vector2d(-36, 45),
                        drive.getVelocityConstraint(30, 2.4, 15.06),
                        drive.getAccelerationConstraint(25))
                .splineToSplineHeading(new Pose2d(-31, 36, Math.toRadians(-45)), Math.toRadians(-45),
                        drive.getVelocityConstraint(30, 1.5, 15.06),
                        drive.getAccelerationConstraint(25))
                .addTemporalMarker(0.5, () ->  intake.setState(Intake.PositionState.DOWN))
                .build();
        TrajectorySequence leftPurpleToTurn = drive.trajectorySequenceBuilder(leftPurple.end())
                .lineTo(new Vector2d(-32, 37),
                        drive.getVelocityConstraint(30, 1.5, 15.06),
                        drive.getAccelerationConstraint(25))
                .splineToSplineHeading(new Pose2d(-38, 59, Math.toRadians(180)), Math.toRadians(90),
                        drive.getVelocityConstraint(30, 1.5, 15.06),
                        drive.getAccelerationConstraint(25))
                .build();
        TrajectorySequence leftPurpleToBack = drive.trajectorySequenceBuilder(leftPurpleToTurn.end())
                .lineToConstantHeading(new Vector2d(20,57))
                .addSpatialMarker(new Vector2d(-20, 57), () ->  {
                    scheduler.scheduleTaskList(actions.autoRaiseSlides(Slides.SlideState.AUTO_LOW));
                    intake.setState(Intake.PowerState.OFF);
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .splineToConstantHeading(new Vector2d(51, 35), Math.toRadians(0))

                .build();



        TrajectorySequence midPurple = drive.trajectorySequenceBuilder(blueFarStart)
                .lineTo(new Vector2d(-36, 34))
                .addTemporalMarker(0.5, () ->  intake.setState(Intake.PositionState.DOWN))
                .build();
        TrajectorySequence midPurpleToTurn = drive.trajectorySequenceBuilder(midPurple.end())
                .lineTo(new Vector2d(-38, 37),
                        drive.getVelocityConstraint(30, 2, 15.06),
                        drive.getAccelerationConstraint(35))

                .splineToSplineHeading(new Pose2d(-38, 57, Math.toRadians(180)), Math.toRadians(90),
                        drive.getVelocityConstraint(35, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .build();
        TrajectorySequence midPurpleToBack = drive.trajectorySequenceBuilder(midPurpleToTurn.end())
                .lineToConstantHeading(new Vector2d(20,57),
                        drive.getVelocityConstraint(50, 2.4, 15.06),
                        drive.getAccelerationConstraint(40))
                .addSpatialMarker(new Vector2d(-20, 57), () ->  {
                    scheduler.scheduleTaskList(actions.autoRaiseSlides(Slides.SlideState.AUTO_LOW));
                    intake.setState(Intake.PowerState.OFF);
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .splineToConstantHeading(new Vector2d(52, 32), Math.toRadians(0),
                        drive.getVelocityConstraint(40, 2.4, 15.06),
                        drive.getAccelerationConstraint(35))
                .build();

      

        intake.setState(Intake.PositionState.MID);
        waitForStart();
        drive.setPoseEstimate(blueFarStart);
        switch(dice){
            case 1:
                drive.followTrajectorySequenceAsync(leftPurple);
                break;
            case 2:
                drive.followTrajectorySequenceAsync(midPurple);
                break;
            case 3:
                drive.followTrajectorySequenceAsync(rightPurple);
        }
        waitForDrive();
        intake.setState(Intake.PositionState.DOWN);
        die(500);
        intake.setState(Intake.SweeperState.ONE_SWEEP);
        die(1000);
        intake.setState(Intake.PositionState.MID);


        switch(dice){
            case 1:
                drive.followTrajectorySequence(leftPurpleToTurn);
                waitForDrive();
                drive.followTrajectorySequence(leftPurpleToBack);
                break;
            case 2:
                drive.followTrajectorySequence(midPurpleToTurn);
                waitForDrive();
                drive.followTrajectorySequence(midPurpleToBack);
                break;
            case 3:
                drive.followTrajectorySequence(rightPurpleToTurn);
                waitForDrive();
                drive.followTrajectorySequence(rightPurpleToBack);
        }
        waitForDrive();
        die(500);
        scheduler.scheduleTaskList(actions.scorePixels());




//        drive.followTrajectorySequence(leftBackFromStack);
//        die(1000);
//        drive.followTrajectorySequence(leftStackFromBack);
//        die(1000);
//        drive.followTrajectorySequence(leftBackFromStack);
//        die(1000);
//        drive.followTrajectorySequence(leftStackFromBack);
//        die(1000);

    }

    public void die (int milliseconds) {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        while (timer.milliseconds() < milliseconds&&!isStopRequested()) {
        }
    }
    public void waitT(int milliseconds) {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        while (timer.milliseconds() < milliseconds) {
        }
    }
    public void waitForDrive(){
        while(drive.isBusy()&&!isStopRequested()){

        }
    }
    @Override
    public void initialize() {

        this.setLoopTimes(10);
        drive=Robot.getInstance();

        Context.isTeamRed=false;
        scheduler=new TaskScheduler();
        actions= RobotActions.getInstance();
        deposit=drive.deposit;
        intake=drive.intake;
        slides=drive.slides;
        drone=drive.droneLauncher;

        intake.init();
        deposit.init();
        slides.init();
        drone.init();
        intake.setOperationState(Module.OperationState.PRESET);
        intake.setState(Intake.SweeperState.INIT);
        deposit.setState(Deposit.FlipState.PRIMED);
        deposit.setState(Deposit.ClawState.CLOSED_AUTO);
    }
}

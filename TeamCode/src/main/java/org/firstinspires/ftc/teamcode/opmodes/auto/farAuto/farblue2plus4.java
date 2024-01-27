package org.firstinspires.ftc.teamcode.opmodes.auto.farAuto;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.DroneLauncher;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.RobotActions;
import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

import java.util.List;

@Autonomous(name="* Far Blue 2+4")
public class farblue2plus4 extends EnhancedOpMode {
    int dice =0;
    Pose2d blueFarStart = new Pose2d(-35 ,61,Math.toRadians(270));
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
            dice = 1;
        } else if(drive.teamElementDetector.centerY < 107){
            dice = 3    ;
        } else {
            dice = 2;
        }
        Context.tel.addData("element Pos", dice);
        Context.tel.addData("Traj Status", Context.trajStatus);
        drive.initLoop();
    }
    public void onStart()
    {
        drive.closeCameras();
    }
    public void linearOpMode() {
        offset=false;
        Context.trajStatus="0% In Progress";
        TrajectorySequence leftPurple = drive.trajectorySequenceBuilder(blueFarStart)
                .addTemporalMarker(0.25, () -> intake.setState(Intake.PositionState.DOWN))
                .splineToConstantHeading(new Vector2d(-46.5, 34),Math.toRadians(270))
                .addTemporalMarker(1,()->
                        intake.setState(Intake.SweeperState.ONE_SWEEP))
                .build();
        Context.trajStatus="7% In Progress";
        TrajectorySequence leftPurpleToBack = drive.trajectorySequenceBuilder(leftPurple.end())
                .setReversed(true)
                .lineTo(new Vector2d(-46.5, 37))
                .splineToSplineHeading(new Pose2d(-34,57, Math.toRadians(180)), Math.toRadians(0),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineToConstantHeading(new Vector2d(20,57))
                .splineToConstantHeading(new Vector2d(50.5, 26), Math.toRadians(0),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .addSpatialMarker(new Vector2d(-10, 57), () ->  {
                    scheduler.scheduleTaskList(actions.autoRaiseSlides(Slides.SlideState.AUTO_LOW));
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .addSpatialMarker(new Vector2d(47,26),()->{
                    scheduler.scheduleTaskList(actions.scorePixelDelay());
                })
                .addTemporalMarker(1, () ->  {intake.setState(Intake.PositionState.RAISED);})
                .build();
        Context.trajStatus="11% In Progress";
        TrajectorySequence leftBackToStack = drive.trajectorySequenceBuilder(leftPurpleToBack.end())
                .setReversed(false)
                .splineToConstantHeading(new Vector2d(10, 58), Math.toRadians(180),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(60))
                .lineToConstantHeading(new Vector2d(-28,58))
                .splineToConstantHeading(new Vector2d(-55, 33), Math.toRadians(180),
                        drive.getVelocityConstraint(35, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .lineToConstantHeading(new Vector2d(-58, 33),
                        drive.getVelocityConstraint(30, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .addSpatialMarker(new Vector2d(-30, 55), () -> {
                    intake.setState(Intake.PositionState.DOWN);
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    scheduler.scheduleTaskList(actions.runSweepersAuto(-56));
                })
                .build();
        Context.trajStatus="17% In Progress";
        TrajectorySequence midPurple = drive.trajectorySequenceBuilder(blueFarStart)
                .addTemporalMarker(0.25, () ->  intake.setState(Intake.PositionState.DOWN))
                .lineTo(new Vector2d(-36, 34))
                .addTemporalMarker(1,()->
                        intake.setState(Intake.SweeperState.ONE_SWEEP))
                .build();
        Context.trajStatus="25% In Progress";
        TrajectorySequence midPurpleToBack = drive.trajectorySequenceBuilder(midPurple.end())
                .lineTo(new Vector2d(-36, 35))
                .splineToSplineHeading(new Pose2d(-29, 57, Math.toRadians(180)), Math.toRadians(0),
                        drive.getVelocityConstraint(50, 2.4, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineToConstantHeading(new Vector2d(20,57))
                .splineToConstantHeading(new Vector2d(49.5, 31), Math.toRadians(0),
                        drive.getVelocityConstraint(50, 2.4, 15.06),
                        drive.getAccelerationConstraint(40))
                .addSpatialMarker(new Vector2d(-10, 57), () ->  {
                    scheduler.scheduleTaskList(actions.autoRaiseSlides(Slides.SlideState.AUTO_LOW));
                    intake.setState(Intake.PowerState.OFF);
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .addSpatialMarker(new Vector2d(47,31),()->{
                    scheduler.scheduleTaskList(actions.scorePixelDelay());
                })
                .addTemporalMarker(1, () ->  {intake.setState(Intake.PositionState.RAISED);})
                .build();
        Context.trajStatus="35% In Progress";
        TrajectorySequence midBackToStack = drive.trajectorySequenceBuilder(midPurpleToBack.end())
                .setReversed(false)
                .splineToConstantHeading(new Vector2d(10, 58), Math.toRadians(180),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(60))
                .lineToConstantHeading(new Vector2d(-28,58))
                .splineToConstantHeading(new Vector2d(-55, 33), Math.toRadians(180),
                        drive.getVelocityConstraint(35, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .lineToConstantHeading(new Vector2d(-58, 33),
                        drive.getVelocityConstraint(30, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .addSpatialMarker(new Vector2d(-30, 55), () -> {
                    intake.setState(Intake.PositionState.DOWN);
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    scheduler.scheduleTaskList(actions.runSweepersAuto(-56));
                })
                .build();
        Context.trajStatus="44% In Progress";
        TrajectorySequence rightPurple = drive.trajectorySequenceBuilder(blueFarStart)
                .addTemporalMarker(0.25, () ->  intake.setState(Intake.PositionState.DOWN))
                .addTemporalMarker(1,()->
                        intake.setState(Intake.SweeperState.ONE_SWEEP))
                .lineTo(new Vector2d(-36, 45),
                        drive.getVelocityConstraint(40, 2.4, 15.06),
                        drive.getAccelerationConstraint(35))
                .splineToSplineHeading(new Pose2d(-31, 36, Math.toRadians(-45)), Math.toRadians(-45),
                        drive.getVelocityConstraint(40, 2.4, 15.06),
                        drive.getAccelerationConstraint(35))
                .build();
        Context.trajStatus="56% In Progress";
        TrajectorySequence rightPurpleToBack = drive.trajectorySequenceBuilder(rightPurple.end())
                .addTemporalMarker(1, () ->  {intake.setState(Intake.PositionState.RAISED);})
                .lineTo(new Vector2d(-32, 37),
                        drive.getVelocityConstraint(40, 2.4, 15.06),
                        drive.getAccelerationConstraint(35))
                .splineToSplineHeading(new Pose2d(-32, 59, Math.toRadians(180)), Math.toRadians(0),
                        drive.getVelocityConstraint(40, 2.4, 15.06),
                        drive.getAccelerationConstraint(35))
                .lineToConstantHeading(new Vector2d(20,59))
                .splineToConstantHeading(new Vector2d(50, 38), Math.toRadians(0))
                .addSpatialMarker(new Vector2d(-10, 57), () ->  {
                    scheduler.scheduleTaskList(actions.autoRaiseSlides(Slides.SlideState.AUTO_LOW));
                    intake.setState(Intake.PowerState.OFF);
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .addSpatialMarker(new Vector2d(47,26),()->{
                    scheduler.scheduleTaskList(actions.scorePixelDelay());
                })
                .build();
        Context.trajStatus="66% In Progress";
        TrajectorySequence rightBackToStack = drive.trajectorySequenceBuilder(rightPurpleToBack.end())
                .setReversed(false)
                .splineToConstantHeading(new Vector2d(10, 58), Math.toRadians(180),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(60))
                .lineToConstantHeading(new Vector2d(-28,58))
                .splineToConstantHeading(new Vector2d(-55, 33), Math.toRadians(180),
                        drive.getVelocityConstraint(35, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .lineToConstantHeading(new Vector2d(-58, 33),
                        drive.getVelocityConstraint(30, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .addSpatialMarker(new Vector2d(-30, 55), () -> {
                    intake.setState(Intake.PositionState.DOWN);
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    scheduler.scheduleTaskList(actions.runSweepersAuto(-56));
                })
                .build();

        Context.trajStatus="75% In Progress";
        TrajectorySequence stackToBack1 = drive.trajectorySequenceBuilder(leftBackToStack.end())
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(-36,57),Math.toRadians(0),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineToConstantHeading(new Vector2d(20,57),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .splineToConstantHeading(new Vector2d(49, 38), Math.toRadians(0),
                        drive.getVelocityConstraint(35, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineTo(new Vector2d(51, 38))
                .addSpatialMarker(new Vector2d(-30, 57), () -> {
                    scheduler.scheduleTaskList(actions.slidesOnly(Slides.SlideState.AUTO_LOW));
                    intake.setState(Intake.PositionState.RAISED);
                })

                .addSpatialMarker(new Vector2d(49,41),()->{
                    scheduler.scheduleTaskList(actions.scorePixelDelay());
                })
                .addSpatialMarker(new Vector2d(20, 57), () -> {
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .build();
        Context.trajStatus="83% In Progress";
        TrajectorySequence backToStack1 = drive.trajectorySequenceBuilder(stackToBack1.end())

                .setReversed(false)
                .splineToConstantHeading(new Vector2d(25, 57), Math.toRadians(180),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(45))
                .lineToConstantHeading(new Vector2d(-28,57))
                .splineToConstantHeading(new Vector2d(-53, 33), Math.toRadians(180),
                        drive.getVelocityConstraint(35, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .lineToConstantHeading(new Vector2d(-57.5, 33),
                        drive.getVelocityConstraint(30, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .addSpatialMarker(new Vector2d(-30, 55), () -> {
                    intake.setState(Intake.PositionState.DOWN);
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    scheduler.scheduleTaskList(actions.runSweepersAuto(-56.5,true));
                })
                .build();
        Context.trajStatus="91% In Progress";
        TrajectorySequence stackToBack2 = drive.trajectorySequenceBuilder(backToStack1.end())
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(-36,57),Math.toRadians(0),
                        drive.getVelocityConstraint(35, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineToConstantHeading(new Vector2d(20,57))
                .splineToConstantHeading(new Vector2d(49, 38), Math.toRadians(0))
                .lineTo(new Vector2d(50.5, 38))
                .addSpatialMarker(new Vector2d(-30, 57), () -> {
                    scheduler.scheduleTaskList(actions.slidesOnly(Slides.SlideState.AUTO_TWO));
                    intake.setState(Intake.PositionState.RAISED);
                })

                .addSpatialMarker(new Vector2d(49,41),()->{
                    scheduler.scheduleTaskList(actions.scorePixelDelay());
                })
                .addSpatialMarker(new Vector2d(20, 57), () -> {
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .build();

        Context.trajStatus="100% Autonomous Ready";

        waitForStart();

        drive.setPoseEstimate(blueFarStart);

        switch(dice){
            case 1:
                drive.followTrajectorySequence(leftPurple);
                break;
            case 2:
                drive.followTrajectorySequence(midPurple);
                break;
            case 3:
                drive.followTrajectorySequence(rightPurple);
        }
        intake.setState(Intake.PositionState.RAISED);

        switch(dice){
            case 1:
                drive.followTrajectorySequence(leftPurpleToBack);
                break;
            case 2:
                drive.followTrajectorySequence(midPurpleToBack);
                break;
            case 3:
                drive.followTrajectorySequence(rightPurpleToBack);
        }
        switch(dice){
            case 1:
                drive.followTrajectorySequence(leftBackToStack);
                break;
            case 2:
                drive.followTrajectorySequence(midBackToStack);
                break;
            case 3:
                drive.followTrajectorySequence(rightBackToStack);
        }
        die(1000);
        intake.setState(Intake.PositionState.RAISED);
        drive.followTrajectorySequence(stackToBack1);
        drive.followTrajectorySequence(backToStack1);
        die(1000);
        intake.setState(Intake.PositionState.RAISED);
        die(250);
        drive.followTrajectorySequence(stackToBack2);
    }

    public void die (int milliseconds) {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        while (timer.milliseconds() < milliseconds&&opModeIsActive()) {
            Context.tel.addData("in wait", timer.milliseconds());
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
        deposit.setState(Deposit.ClawState.CLOSED1);

        Context.statusError="Init Passed";
    }
}

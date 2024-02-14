package org.firstinspires.ftc.teamcode.opmodesOld.archive.farAuto;


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
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.EnhancedOpMode;

@Autonomous
public class farred2plus0 extends EnhancedOpMode {
    int dice =0;
    Pose2d redFarStart = new Pose2d(-35 ,-61,Math.toRadians(-270));
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
        drive.initLoop();
    }
    public void onStart()
    {
        drive.closeCameras();
    }
    public void linearOpMode() {
        offset=false;
        TrajectorySequence leftPurple = drive.trajectorySequenceBuilder(redFarStart)
                .addTemporalMarker(0.25, () -> intake.setState(Intake.PositionState.AUTO))
                .splineToConstantHeading(new Vector2d(-48, -36),Math.toRadians(-270))
                .addTemporalMarker(1,()->
                        intake.setState(Intake.SweeperState.ONE_SWEEP))
                .build();

        TrajectorySequence leftPurpleToBack = drive.trajectorySequenceBuilder(leftPurple.end())
                .setReversed(true)
                .lineTo(new Vector2d(-48, -37))
                .splineToSplineHeading(new Pose2d(-34,-57, Math.toRadians(180)), Math.toRadians(0),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineToConstantHeading(new Vector2d(20,-57))
                .splineToConstantHeading(new Vector2d(51.5, -25.5), Math.toRadians(0),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .addSpatialMarker(new Vector2d(-10, -57), () ->  {
                    scheduler.scheduleTaskList(actions.OLD_autoRaiseSlides(Slides.SlideState.HALF));
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .addSpatialMarker(new Vector2d(47,-26),()->{
                    scheduler.scheduleTaskList(actions.OLD_scorePixelDelay());
                })
                .addTemporalMarker(1, () ->  {intake.setState(Intake.PositionState.RAISED);})
                .build();
        TrajectorySequence leftBackToStack = drive.trajectorySequenceBuilder(leftPurpleToBack.end())
                .setReversed(false)
                .splineToConstantHeading(new Vector2d(10, -58), Math.toRadians(180),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(60))
                .lineToConstantHeading(new Vector2d(-28,-58))
                .splineToConstantHeading(new Vector2d(-55, -29), Math.toRadians(180),
                        drive.getVelocityConstraint(35, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .lineToConstantHeading(new Vector2d(-59, -27.5  ),
                        drive.getVelocityConstraint(30, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .addSpatialMarker(new Vector2d(-30, -55), () -> {
                    intake.setState(Intake.PositionState.DOWN);
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    scheduler.scheduleTaskList(actions.OLD_runSweepersAuto(-56.5));
                })
                .build();


        TrajectorySequence midPurple1 = drive.trajectorySequenceBuilder(redFarStart)
                .addTemporalMarker(0.25, () -> intake.setState(Intake.PositionState.AUTO))
                .lineTo(new Vector2d(-36, -30))

                .build();
        TrajectorySequence midPurple2 = drive.trajectorySequenceBuilder(midPurple1.end())
                .lineTo(new Vector2d(-36, -34))
                .addTemporalMarker(0.5,()->
                        intake.setState(Intake.SweeperState.ONE_SWEEP))
                .build();
        TrajectorySequence midPurpleToBack = drive.trajectorySequenceBuilder(midPurple2.end())
                .lineTo(new Vector2d(-36, -35))
                .splineToSplineHeading(new Pose2d(-29, -57, Math.toRadians(180)), Math.toRadians(0),
                        drive.getVelocityConstraint(50, 2.4, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineToConstantHeading(new Vector2d(20,-57))
                .splineToConstantHeading(new Vector2d(50.5, -31), Math.toRadians(0),
                        drive.getVelocityConstraint(50, 2.4, 15.06),
                        drive.getAccelerationConstraint(40))
                .addSpatialMarker(new Vector2d(-10, -57), () ->  {
                    scheduler.scheduleTaskList(actions.OLD_autoRaiseSlides(Slides.SlideState.HALF));
                    intake.setState(Intake.PowerState.OFF);
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .addSpatialMarker(new Vector2d(47,-31),()->{
                    scheduler.scheduleTaskList(actions.OLD_scorePixelDelay());
                })
                .addTemporalMarker(1, () ->  {intake.setState(Intake.PositionState.RAISED);})
                .build();

        TrajectorySequence midBackToStack = drive.trajectorySequenceBuilder(midPurpleToBack.end())
                .setReversed(false)
                .splineToConstantHeading(new Vector2d(10, -58), Math.toRadians(180),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(60))
                .lineToConstantHeading(new Vector2d(-28,-58))
                .splineToConstantHeading(new Vector2d(-55, -33), Math.toRadians(180),
                        drive.getVelocityConstraint(35, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .lineToConstantHeading(new Vector2d(-60, -33),
                        drive.getVelocityConstraint(30, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .addSpatialMarker(new Vector2d(-30, -55), () -> {
                    intake.setState(Intake.PositionState.DOWN);
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    scheduler.scheduleTaskList(actions.OLD_runSweepersAuto(-56.5));
                })
                .build();

        TrajectorySequence rightPurple = drive.trajectorySequenceBuilder(redFarStart)
                .addTemporalMarker(1.2, () -> intake.setState(Intake.PositionState.AUTO))
                .addTemporalMarker(1.5,()->
                        intake.setState(Intake.SweeperState.ONE_SWEEP))
                .lineTo(new Vector2d(-36, -45),
                        drive.getVelocityConstraint(30, 1.5, 15.06),
                        drive.getAccelerationConstraint(30))
                .splineToSplineHeading(new Pose2d(-31.5, -35.5, Math.toRadians(45)), Math.toRadians(45),
                        drive.getVelocityConstraint(30, 0.8, 15.06),
                        drive.getAccelerationConstraint(35))
                .build();
        TrajectorySequence rightPurpleToBack = drive.trajectorySequenceBuilder(rightPurple.end())
                .addTemporalMarker(1, () ->  {intake.setState(Intake.PositionState.RAISED);})
                .lineTo(new Vector2d(-32, -37),
                        drive.getVelocityConstraint(40, 2.4, 15.06),
                        drive.getAccelerationConstraint(35))
                .splineToSplineHeading(new Pose2d(-32, -59, Math.toRadians(180)), Math.toRadians(0),
                        drive.getVelocityConstraint(40, 2.4, 15.06),
                        drive.getAccelerationConstraint(35))
                .lineToConstantHeading(new Vector2d(20,-59),
                        drive.getVelocityConstraint(40, 2.4, 15.06),
                        drive.getAccelerationConstraint(35))
                .splineToConstantHeading(new Vector2d(50.5, -39), Math.toRadians(0),
                        drive.getVelocityConstraint(40, 2.4, 15.06),
                        drive.getAccelerationConstraint(35))
                .addSpatialMarker(new Vector2d(-10, -57), () ->  {
                    scheduler.scheduleTaskList(actions.OLD_autoRaiseSlides(Slides.SlideState.HALF));
                    intake.setState(Intake.PowerState.OFF);
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .addSpatialMarker(new Vector2d(47,-26),()->{
                    scheduler.scheduleTaskList(actions.OLD_scorePixelDelay());
                })
                .build();
        TrajectorySequence rightBackToStack = drive.trajectorySequenceBuilder(rightPurpleToBack.end())
                .setReversed(false)
                .splineToConstantHeading(new Vector2d(10, -58), Math.toRadians(180),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(60))
                .lineToConstantHeading(new Vector2d(-28,-58))
                .splineToConstantHeading(new Vector2d(-55, -34), Math.toRadians(180),
                        drive.getVelocityConstraint(35, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .lineToConstantHeading(new Vector2d(-60, -34),
                        drive.getVelocityConstraint(30, 2, 15.06),
                        drive.getAccelerationConstraint(35))
                .addSpatialMarker(new Vector2d(-30, -55), () -> {
                    intake.setState(Intake.PositionState.DOWN);
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    scheduler.scheduleTaskList(actions.OLD_runSweepersAuto(-56.5));
                })
                .build();
        TrajectorySequence stackToBack1 = drive.trajectorySequenceBuilder(leftBackToStack.end())
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(-36,-57),Math.toRadians(0),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineToConstantHeading(new Vector2d(20,-57),
                        drive.getVelocityConstraint(30, 2, 15.06),
                        drive.getAccelerationConstraint(30))
                .splineToConstantHeading(new Vector2d(48, -33
                        ), Math.toRadians(0),
                        drive.getVelocityConstraint(30, 2, 15.06),
                        drive.getAccelerationConstraint(30))
                .lineTo(new Vector2d(51, -33),
                        drive.getVelocityConstraint(25, 2, 15.06),
                        drive.getAccelerationConstraint(30))
                .addSpatialMarker(new Vector2d(-30, -57), () -> {
                    scheduler.scheduleTaskList(actions.slidesOnly(Slides.SlideState.AUTO_LOW));
                    intake.setState(Intake.PositionState.RAISED);
                })

                .addSpatialMarker(new Vector2d(49,-41),()->{
                    scheduler.scheduleTaskList(actions.OLD_scorePixelDelay());
                })
                .addSpatialMarker(new Vector2d(20, -57), () -> {
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .build();
        TrajectorySequence backToStack1 = drive.trajectorySequenceBuilder(stackToBack1.end())

                .setReversed(false)
                .splineToConstantHeading(new Vector2d(25, -57), Math.toRadians(180),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(45))
                .lineToConstantHeading(new Vector2d(-28,-57))
                .splineToConstantHeading(new Vector2d(-55, -30), Math.toRadians(180),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .lineToConstantHeading(new Vector2d(-60, -30),
                        drive.getVelocityConstraint(40, 2, 15.06),
                        drive.getAccelerationConstraint(40))
                .addSpatialMarker(new Vector2d(-30, -55), () -> {
                    intake.setState(Intake.PositionState.DOWN);
                    intake.setState(Intake.PowerState.INTAKE_AUTO);
                    intake.setState(Intake.ConveyorState.INTAKE);
                    scheduler.scheduleTaskList(actions.OLD_runSweepersAuto(-56,true));
                })
                .build();
        TrajectorySequence  stackToBack2 = drive.trajectorySequenceBuilder(leftBackToStack.end())
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(-36,-57),Math.toRadians(0),
                        drive.getVelocityConstraint(45, 2, 15.06),
                        drive.getAccelerationConstraint(45))
                .lineToConstantHeading(new Vector2d(20,-57))
                .splineToConstantHeading(new Vector2d(49, -36), Math.toRadians(0))
                .lineTo(new Vector2d(50.5, -36))
                .addSpatialMarker(new Vector2d(-30, -57), () -> {
                    scheduler.scheduleTaskList(actions.slidesOnly(Slides.SlideState.AUTO_TWO));
                    intake.setState(Intake.PositionState.RAISED);
                })

                .addSpatialMarker(new Vector2d(49,-41),()->{
                    scheduler.scheduleTaskList(actions.OLD_scorePixelDelay());
                })
                .addSpatialMarker(new Vector2d(20, -57), () -> {
                    intake.setState(Intake.ConveyorState.OFF);
                })
                .build();
        drive.setPoseEstimate(redFarStart);

        waitForStart();

        waitMS(10000);
        switch(dice){
            case 1:
                drive.followTrajectorySequence(leftPurple);
                break;
            case 2:
                drive.followTrajectorySequence(midPurple1);
                drive.followTrajectorySequence(midPurple2);
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
    }

    public void waitMS(int milliseconds) {
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

        Context.isTeamRed=true;
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
    }
}
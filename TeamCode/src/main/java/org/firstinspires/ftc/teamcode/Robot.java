package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants.MAX_ACCEL;
import static org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants.MAX_ANG_ACCEL;
import static org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants.MAX_ANG_VEL;
import static org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants.MAX_VEL;
import static org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants.MOTOR_VELO_PID;
import static org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants.RUN_USING_ENCODER;
import static org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants.TRACK_WIDTH;
import static org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants.encoderTicksToInches;
import static org.firstinspires.ftc.teamcode.util.WhipTrajectory.map;

import android.util.Log;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.drive.DriveSignal;
import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.followers.HolonomicPIDVAFollower;
import com.acmerobotics.roadrunner.followers.TrajectoryFollower;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.LynxModuleImuType;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.DroneLauncher;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.NavxWrapper;
import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.roadrunner.drive.TwoWheelTrackingLocalizer;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceRunner;
import org.firstinspires.ftc.teamcode.roadrunner.util.LynxModuleUtil;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.AutoSelector;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.NamedTrajectory;
import org.firstinspires.ftc.teamcode.util.ReadTimer;
import org.firstinspires.ftc.teamcode.util.Tel;
import org.firstinspires.ftc.teamcode.util.WhipTrajectory;
import org.firstinspires.ftc.teamcode.util.enums.Paths;
import org.firstinspires.ftc.teamcode.vision.TeamElementDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Config
public class Robot extends MecanumDrive
{

    //public static PIDCoefficients TRANSLATIONAL_PID = new PIDCoefficients(9, 0, 1);
    //public static PIDCoefficients HEADING_PID = new PIDCoefficients(8, 0, 1);
    public static PIDCoefficients TRANSLATIONAL_PID = new PIDCoefficients(6,0,0);//9, 0, 1);
   public static PIDCoefficients HEADING_PID = new PIDCoefficients(6,0,0);//8, 0, 1);


    public static double LATERAL_MULTIPLIER = 2.3;

    public static double VX_WEIGHT = 1;
    public static double VY_WEIGHT = 1;
    public static double OMEGA_WEIGHT = 1;

    private final byte NAVX_DEVICE_UPDATE_RATE_HZ = 100;

    private TrajectorySequenceRunner trajectorySequenceRunner;

    public static final TrajectoryVelocityConstraint VEL_CONSTRAINT = getVelocityConstraint(MAX_VEL, MAX_ANG_VEL, TRACK_WIDTH);
    public static final TrajectoryAccelerationConstraint ACCEL_CONSTRAINT = getAccelerationConstraint(MAX_ACCEL);

    private TrajectoryFollower follower;
    private IMU imu;
    private DcMotorEx leftFront, leftRear, rightRear, rightFront;

    private DcMotor hang;
    private List<DcMotorEx> motors;
    private VoltageSensor batteryVoltageSensor;
    private double hangPower = 0;

    private List<Integer> lastEncPositions = new ArrayList<>();
    private List<Integer> lastEncVels = new ArrayList<>();

    private List<Double> headingTrack = new ArrayList<>();
    private List<WhipTrajectory> trajectories;

    List<LynxModule> modules;

    LinearOpMode l;
    HardwareMap hardwareMap;
    public Paths k = Paths.Score_Third;
    Tel tel;
    public Slides slides;
    public Deposit deposit;
    public Intake intake;

    public DroneLauncher droneLauncher;
    OpenCvWebcam camera;

    public TeamElementDetection teamElementDetector;
    Pose2d localDrivePowers;
    ElapsedTime timer;
    public boolean waitingForCS=false;
    public boolean tapeDetected=false;
    ColorRangeSensor cs3, cs2, cs1;
    static Robot robot;
    public TaskScheduler scheduler;
    public static boolean dashTeleEnabled=true;
    private LynxModule controlHub;
    private LynxModule expansionHub;
    NavxWrapper navxWrapper;
    private AHRS navx;
    ReadTimer chub, ehub;
    public TwoWheelTrackingLocalizer localizer;
    Thread colorSensors;



    public Robot(LinearOpMode l)
    {

        super(DriveConstants.kV, DriveConstants.kA, DriveConstants.kStatic, TRACK_WIDTH, TRACK_WIDTH, LATERAL_MULTIPLIER);
        this.l=l;

        if(Context.opmode!=null)
        {
            Context.updateValues();
        }

        tel = Tel.instance();

        hardwareMap=l.hardwareMap;
        timer=new ElapsedTime();

        modules=new ArrayList<>();

        try {
            for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
                modules.add(module);
            }

            for(LynxModule module: modules)
            {
                if(module.getImuType()== LynxModuleImuType.BHI260)
                    controlHub=module;
                else if(module.getImuType() == LynxModuleImuType.BNO055)
                    expansionHub=module;
            }

            //controlHub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
            //expansionHub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        } catch (RuntimeException e) {
            throw new RuntimeException("One or more of the REV hubs could not be found. More info: " + e);
        }

        if(!Context.isTele)
        {
            cameraInit();
        }
        //teamElementDetector=new TeamElementDetection(l.telemetry);
        dtInit();

        slides=new Slides(hardwareMap);
        deposit=new Deposit(hardwareMap);
        intake=new Intake(hardwareMap);
        droneLauncher = new DroneLauncher(hardwareMap);
        hang = hardwareMap.get(DcMotor.class, "hang");


        if(!Context.noHwInit)
        {
            intake.init();
            deposit.init();
            droneLauncher.init();
        }

        scheduler=new TaskScheduler();

        if(!Context.isTele)
        {
            colorSensorsStart();
        }


        //navx=new ReadTimer(navxWrapper::update, 20);
        //chub=new ReadTimer(this::cHubUpdate);
        //ehub=new ReadTimer(this::eHubUpdate, 50);
    }

    public static Robot getInstance()
    {
        if(robot==null)
        {
            robot=new Robot(Context.opmode);
        }
        return robot;
    }

    public static void destroyInstance()
    {
        if(robot!=null)
        {
            robot.deposit.onDeath();
            if(!Context.isTele)
            {
                robot.colorSensors.interrupt();
            }
        }

        robot=null;
    }

    public void colorSensorsStart()
    {
        colorSensors = new Thread()
        {
            public void run()
            {
                while(!isInterrupted() && Context.opmode.opModeIsActive())
                {
                    if(Context.colorSensorsEnabled)
                    {
                        intake.updatePixelsPresent();
                    }
                }
            }
        };
        colorSensors.start();
    }


    public void cameraInit()
    {
        int monitorID=hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "frontCamera"), monitorID);
        teamElementDetector=new TeamElementDetection(l.telemetry);
        camera.setPipeline(teamElementDetector);
        //FtcDashboard.getInstance().startCameraStream(camera, 0);
        camera.setMillisecondsPermissionTimeout(5000);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened()
            {
                camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });
    }

    public void setLocalDrivePowers(Pose2d localDrivePowers)
    {
        this.localDrivePowers=localDrivePowers;
    }

    public void updateDrivePowers()
    {
        setWeightedDrivePower(localDrivePowers);
    }

    public void closeCameras()
    {
        camera.stopStreaming();
    }

    public void dtInit()
    {

        follower = new HolonomicPIDVAFollower(TRANSLATIONAL_PID, TRANSLATIONAL_PID, HEADING_PID,
                new Pose2d(3, 3, Math.toRadians(10)), 0);

        LynxModuleUtil.ensureMinimumFirmwareVersion(hardwareMap);

        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();

        for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }


        // TODO: adjust the names of the following hardware devices to match your configuration
       navx =  AHRS.getInstance(hardwareMap.get(NavxMicroNavigationSensor.class, "navx"),
                AHRS.DeviceDataType.kProcessedData,
                NAVX_DEVICE_UPDATE_RATE_HZ);

        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                new Orientation(
                        AxesReference.INTRINSIC,
                        AxesOrder.XYX,
                            AngleUnit.DEGREES,
                        55+180,
                        0,
                        0,
                        0

                )
        ));
        imu.initialize(parameters);

        leftFront = hardwareMap.get(DcMotorEx.class, "fl");
        leftRear = hardwareMap.get(DcMotorEx.class, "bl");
        rightRear = hardwareMap.get(DcMotorEx.class, "br");
        rightFront = hardwareMap.get(DcMotorEx.class, "fr");
//        cs3 = hardwareMap.get(ColorRangeSensor .class, "cs3");
//        cs2 = hardwareMap.get(ColorRangeSensor.class, "cs2");
//        cs1 = hardwareMap.get(ColorRangeSensor.class, "cs1");
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);
        motors = Arrays.asList(leftFront, leftRear, rightRear, rightFront);

        for (DcMotorEx motor : motors) {
            MotorConfigurationType motorConfigurationType = motor.getMotorType().clone();
            motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
            motor.setMotorType(motorConfigurationType);
        }

        if (RUN_USING_ENCODER) {
            setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        if (RUN_USING_ENCODER && MOTOR_VELO_PID != null) {
            setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, MOTOR_VELO_PID);
        }

        // TODO: reverse any motors using DcMotor.setDirection()

        List<Integer> lastTrackingEncPositions = new ArrayList<>();
        List<Integer> lastTrackingEncVels = new ArrayList<>();

        localizer = new TwoWheelTrackingLocalizer(hardwareMap, this);
        // TODO: if desired, use setLocalizer() to change the localization method
        setLocalizer(localizer);

        trajectorySequenceRunner = new TrajectorySequenceRunner(
                follower, HEADING_PID, batteryVoltageSensor,
                lastEncPositions, lastEncVels, lastTrackingEncPositions, lastTrackingEncVels
        );

    }

    public void initLoop()
    {
        read();
        write();
        //modulesUpdate();
    /**/    //navxWrapper.update();
        if(!Context.isTele)
        {
            Tel.instance().addData("Vision Zone", Context.dice, 0);
            AutoSelector.getInstance().loop();
        }
        tel.update();
        //telemetryUpdate();
        //loop whatever else u want
    }
    public void setYaw()
    {
    }
    public void setHangPower(double power){
        hangPower = power;
    }
    public void primaryLoop()
    {
        //modulesUpdate();

        read();
        write();


//        navxWrapper.update();
        if(isBusy()||!Context.isTele)
        {
            update();
            Tel.instance().addData("DT Vel", getPoseVelocity(), 1);
        }
        else {
            updateDrivePowers();
            if(hang!=null) {
                hang.setPower(hangPower);
            }
        }

        if(timer.milliseconds()>500)
        {
            Log.d(null, "Status: " + Context.statusError);
            timer.reset();
        }
        Tel.instance().addData("debug", Context.debug);
        //telemetryUpdate();
        tel.update();
    }

    private void modulesUpdate()
    {
        chub.update();
        ehub.update();
        if(!Context.isTele){}
    }

    private void cHubUpdate()
    {
        controlHub.getBulkData();

        if(l.isStarted())
        {
            if((isBusy()||!Context.isTele))
                update();
            else {
                updateDrivePowers();
            }
        }

        deposit.updateLoop();
        deposit.writeLoop();
    }

    private void eHubUpdate()
    {
        expansionHub.getBulkData();

        if(l.isStarted() && hang!=null && Context.isTele) {
            hang.setPower(hangPower);
        }

        slides.updateLoop();
        droneLauncher.updateLoop();
        intake.updateLoop();
        slides.writeLoop();
        droneLauncher.writeLoop();
        intake.writeLoop();
    }

    private void telemetryUpdate()
    {
        slides.telemetryUpdateExternal();
        deposit.telemetryUpdateExternal();
        intake.telemetryUpdateExternal();
        droneLauncher.telemetryUpdateExternal();
        tel.update();
    }

    public void read()
    {
        slides.updateLoop();
        deposit.updateLoop();
        intake.updateLoop();
        droneLauncher.updateLoop();
    }

    public void write()
    {
        slides.writeLoop();
        deposit.writeLoop();
        intake.writeLoop();
        droneLauncher.writeLoop();
    }



    public void onEnd()
    {
        Context.resetValues();
    }

    public double getBatteryVoltage()
    {
        double result = Double.POSITIVE_INFINITY;
        for (VoltageSensor sensor : hardwareMap.voltageSensor)
        {
            double voltage = sensor.getVoltage();
            if (voltage > 0)
            {
                result = Math.min(result, voltage);
            }
        }
        return result;
    }

    public double getCurrent()
    {
        double current=0;
        for(LynxModule l: modules)
        {
            current+=l.getCurrent(CurrentUnit.AMPS);
        }
        return current;
    }

    public TrajectoryBuilder trajectoryBuilder(Pose2d startPose) {
        return new TrajectoryBuilder(startPose, VEL_CONSTRAINT, ACCEL_CONSTRAINT);
    }

    public TrajectoryBuilder trajectoryBuilder(Pose2d startPose, boolean reversed) {
        return new TrajectoryBuilder(startPose, reversed, VEL_CONSTRAINT, ACCEL_CONSTRAINT);
    }

    public TrajectoryBuilder trajectoryBuilder(Pose2d startPose, double startHeading) {
        return new TrajectoryBuilder(startPose, startHeading, VEL_CONSTRAINT, ACCEL_CONSTRAINT);
    }

    public static TrajectorySequenceBuilder trajectorySequenceBuilder(Pose2d startPose) {
        return new TrajectorySequenceBuilder(
                startPose,
                VEL_CONSTRAINT, ACCEL_CONSTRAINT,
                MAX_ANG_VEL, MAX_ANG_ACCEL
        );
    }

    public void turnAsync(double angle) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(
                trajectorySequenceBuilder(getPoseEstimate())
                        .turn(angle)
                        .build()
        );
    }

    public void turn(double angle) {
        turnAsync(angle);
        waitForIdle();
    }

    public void followTrajectoryAsync(Trajectory trajectory) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(
                trajectorySequenceBuilder(trajectory.start())
                        .addTrajectory(trajectory)
                        .build()
        );
    }

    public void followTrajectory(Trajectory trajectory) {
        followTrajectoryAsync(trajectory);
        waitForIdle();
    }
    public  WhipTrajectory get (int index){
        return this.trajectories.get(index);
    }
    public void set(NamedTrajectory[][] trajectories,List<Task>[] actions){
        switch (Context.dice){
            default:
            this.trajectories = map(trajectories[0], actions);
            break;
            case MIDDLE:
                this.trajectories = map(trajectories[1], actions);
            break;
            case RIGHT:
                this.trajectories = map(trajectories[2], actions);
            break;
        }
    }
    public void run(Paths trajectory)
    {
        for(WhipTrajectory item : trajectories){
            if(item.getPath().equals(trajectory)){
                if(Context.opmode.opModeIsActive())
                {
                    k = trajectory;
                    followTrajectorySequenceAsync(item.getTrajectory());
                    scheduler.scheduleTaskList(item.getTasks());
                    waitForIdle();
                }
                else
                    break;
            }
        }
        //RobotLog.e("WE DIDNT FIND THE TRAJECTORY BRO");

    }

    @SafeVarargs
    public static List<Task>[] getTaskList(List<Task>... tasks) {
        return (tasks);
    }
    public void followTrajectorySequenceAsync(TrajectorySequence trajectorySequence) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(trajectorySequence);
    }

    public void followTrajectorySequence(TrajectorySequence trajectorySequence)
    {
        if(Context.opmode.opModeIsActive())
        {
            followTrajectorySequenceAsync(trajectorySequence);
            waitForIdle();
        }
    }

    public void followTrajectorySequence(TrajectorySequence trajectorySequence, List<Task> taskList)
    {
        if(Context.opmode.opModeIsActive())
        {
            followTrajectorySequenceAsync(trajectorySequence);
            //maybe make it not blocking? or terminate after
            scheduler.scheduleTaskList(taskList);
            waitForIdle();
        }
    }

    public Pose2d getLastError() {
        return trajectorySequenceRunner.getLastPoseError();
    }

    public void update() {
        updatePoseEstimate();
        DriveSignal signal = trajectorySequenceRunner.update(getPoseEstimate(), getPoseVelocity());
        if (signal != null) setDriveSignal(signal);
        //Context.debug++;
    }

    public void waitForIdle() {
        while (!Thread.currentThread().isInterrupted() && isBusy() && Context.opmode.opModeIsActive())
        {
            Method x =null;
            try{
                x= Context.opmode.getClass().getMethod("primaryLoop",(Class<?>[]) null);
            }
            catch(NoSuchMethodException ignored){

        }
            if(x==null){
                //RobotLog.e("yep");
                update();
                break;
            }else {
                //RobotLog.e("nah");
            }
        }
    }

    public boolean isBusy() {
        return trajectorySequenceRunner.isBusy();
    }

    public void setMode(DcMotor.RunMode runMode) {
        for (DcMotorEx motor : motors) {
            motor.setMode(runMode);
        }
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        for (DcMotorEx motor : motors) {
            motor.setZeroPowerBehavior(zeroPowerBehavior);
        }
    }

    public void setPIDFCoefficients(DcMotor.RunMode runMode, PIDFCoefficients coefficients) {
        PIDFCoefficients compensatedCoefficients = new PIDFCoefficients(
                coefficients.p, coefficients.i, coefficients.d,
                coefficients.f * 12 / batteryVoltageSensor.getVoltage()
        );

        for (DcMotorEx motor : motors) {
            motor.setPIDFCoefficients(runMode, compensatedCoefficients);
        }
    }

    public void setWeightedDrivePower(Pose2d drivePower) {
        Pose2d vel = drivePower;
        if (Math.abs(drivePower.getX()) + Math.abs(drivePower.getY())
                + Math.abs(drivePower.getHeading()) > 1) {
            // re-normalize the powers according to the weights
            double denom = VX_WEIGHT * Math.abs(drivePower.getX())
                    + VY_WEIGHT * Math.abs(drivePower.getY())
                    + OMEGA_WEIGHT * Math.abs(drivePower.getHeading());

            vel = new Pose2d(
                    VX_WEIGHT * drivePower.getX(),
                    VY_WEIGHT * drivePower.getY(),
                    OMEGA_WEIGHT * drivePower.getHeading()
            ).div(denom);
        }

        setDrivePower(vel);
    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        lastEncPositions.clear();

        List<Double> wheelPositions = new ArrayList<>();
        for (DcMotorEx motor : motors) {
            int position = motor.getCurrentPosition();
            lastEncPositions.add(position);
            wheelPositions.add(encoderTicksToInches(position));
        }
        return wheelPositions;
    }

    @Override
    public List<Double> getWheelVelocities() {
        lastEncVels.clear();

        List<Double> wheelVelocities = new ArrayList<>();
        for (DcMotorEx motor : motors) {
            int vel = (int) motor.getVelocity();
            lastEncVels.add(vel);
            wheelVelocities.add(encoderTicksToInches(vel));
        }
        return wheelVelocities;
    }

    @Override
    public void setMotorPowers(double v, double v1, double v2, double v3) {
        leftFront.setPower(v);
        leftRear.setPower(v1);
        rightRear.setPower(v2);
        rightFront.setPower(v3);
    }

    @Override
    public double getRawExternalHeading() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
     //   return Math.toRadians(-navx.getYaw());
    }

    @Override
    public Double getExternalHeadingVelocity()
    {
        return (Double)0.0;
    }

    public static TrajectoryVelocityConstraint getVelocityConstraint(double maxVel, double maxAngularVel, double trackWidth) {
        return new MinVelocityConstraint(Arrays.asList(
                new AngularVelocityConstraint(maxAngularVel),
                new MecanumVelocityConstraint(maxVel, trackWidth)
        ));
    }

    public static TrajectoryAccelerationConstraint getAccelerationConstraint(double maxAccel) {
        return new ProfileAccelerationConstraint(maxAccel);
    }
    public boolean isPixelCs1() {
        int distance = 70;
        if (cs1.getDistance(DistanceUnit.MM) < distance) {
            return true;
        }
        return false;
    }
    public boolean isPixelCs2() {
        int distance = 70;
        if (cs2.getDistance(DistanceUnit.MM) < distance) {
            return true;
        }
        return false;
    }
}

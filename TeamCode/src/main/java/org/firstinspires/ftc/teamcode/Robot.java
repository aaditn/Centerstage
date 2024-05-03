package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.util.WhipTrajectory.map;

import android.util.Log;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.LynxModuleImuType;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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
import org.firstinspires.ftc.teamcode.modules.Slides;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskScheduler;
import org.firstinspires.ftc.teamcode.util.AutoSelector;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.NamedTrajectory;
import org.firstinspires.ftc.teamcode.util.ReadTimer;
import org.firstinspires.ftc.teamcode.util.Tel;
import org.firstinspires.ftc.teamcode.util.WhipTrajectory;
import org.firstinspires.ftc.teamcode.util.enums.Paths;
import org.firstinspires.ftc.teamcode.vision.AprilTagPipeline;
import org.firstinspires.ftc.teamcode.vision.TeamElementDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Config
public class Robot extends MecanumDrive
{
    public static double LATERAL_MULTIPLIER = 2.3;

    public static double VX_WEIGHT = 1;
    public static double VY_WEIGHT = 1;
    public static double OMEGA_WEIGHT = 1;
    private IMU imu;

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
    public Paths k = Paths.Back3;
    Tel tel;
    public Slides slides;
    public Deposit deposit;
    public Intake intake;
    public Servo purplePlacer;

    public DroneLauncher droneLauncher;
    OpenCvWebcam camera;

    public TeamElementDetection teamElementDetector;
    Pose2d localDrivePowers = new Pose2d(new Vector2d(0,0), new Rotation2d(0,0));

    public List<PoseVelocity2d> localVeloPowers = new ArrayList<>();
    ElapsedTime timer;
    public boolean waitingForCS=false;
    public boolean tapeDetected=false;
    ColorRangeSensor cs3, cs2, cs1;
    static Robot robot;
    public TaskScheduler scheduler;
    public static boolean dashTeleEnabled=true;
    private LynxModule controlHub;
    private LynxModule expansionHub;
    ReadTimer chub, ehub;
    Thread colorSensors;


    public Robot(LinearOpMode l, Pose2d... startPose)
    {

        super(l.hardwareMap, startPose[0]);
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

        slides=new Slides(hardwareMap);
        deposit=new Deposit(hardwareMap);
        intake=new Intake(hardwareMap);
        droneLauncher = new DroneLauncher(hardwareMap);
        hang = hardwareMap.get(DcMotor.class, "hang");
        purplePlacer = hardwareMap.get(Servo.class, "purplePlacer");


        if(!Context.noHwInit)
        {
            intake.init();
            deposit.init();
            droneLauncher.init();
        }

        scheduler=new TaskScheduler();

        if(!Context.isTele)
        {
            RobotLog.e("Attempted to start");
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
            assert Context.opmode != null;
            robot=new Robot(Context.opmode, new Pose2d(0,0,0));
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
        RobotLog.e("Start Method");
        colorSensors = new Thread()
        {
            @Override
            public void run()
            {
                RobotLog.e("Thread Started");
                RobotLog.e("" + !isInterrupted() + " "+Context.opmode.opModeIsActive());
                while(!isInterrupted() && !Context.opmode.isStopRequested())
                {
                    if(Context.colorSensorsEnabled)
                    {
                        RobotLog.e("Color Sensors Enabled");
                        intake.updatePixelsPresent();
                    }
                    //RobotLog.e("Thread Running");
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
        if (Context.drive != null) {
            Context.drive.driveWithCorrection(localVeloPowers.get(0), localVeloPowers.get(1));
        } else {
            PoseVelocity2d poseVelocity2d = new PoseVelocity2d(localDrivePowers.position, localDrivePowers.heading.toDouble());
            setDrivePowers(poseVelocity2d);
        }
    }

    public void closeCameras()
    {
        camera.stopStreaming();
    }

    public void dtInit()
    {
        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();

        for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

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


        for (DcMotorEx motor : motors) {
            MotorConfigurationType motorConfigurationType = motor.getMotorType().clone();
            motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
            motor.setMotorType(motorConfigurationType);
        }
        // TODO: reverse any motors using DcMotor.setDirection()


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
        if(!Context.isTele)
        {
           // update();

        }
        else {
            updateDrivePowers();
            if(hang!=null) {
                hang.setPower(hangPower);
            }
        }
        Tel.instance().addData("DT Pose", pose.position);

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
            if((isBusy ||!Context.isTele))
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

    public  WhipTrajectory get (int index){
        return this.trajectories.get(index);
    }
    public void set(NamedTrajectory[][] trajectories, List<Task>[] actions){
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

    public NamedTrajectory get(Paths trajectory) {
        for (WhipTrajectory item : trajectories) {
            if (item.getPath().equals(trajectory)) {
                if (Context.opmode.opModeIsActive())
                    return item.getTrajectory();
                {
                }
            }
        }
        return trajectories.get(1).getTrajectory();
    }
    public void run(Paths trajectory)
    {
        for(WhipTrajectory item : trajectories){
            if(item.getPath().equals(trajectory)){
                if(Context.opmode.opModeIsActive())
                {
                    RobotLog.e("isBusyBefore " + isBusy);
                    k = trajectory;
                    scheduler.scheduleTaskList(item.getTasks());
                   // followTrajectorySequence(item.getTrajectory().getAction());
                    isBusy = true;
                    RobotLog.e(item.getTrajectory().getName() + ": " + trajectoryDuration);
                    RobotLog.e("isBusyMid " + isBusy);
                    waitForIdle(item);
                    RobotLog.e("isBusyAfter " + isBusy);
                }
                else
                    break;
            }
        }

    }

    public void AprilTagRun(AprilTagPipeline vision, Telemetry telemetry, List<Pose2d> previous) {
        double aprilTagConfidence = 0.5;
        if (k.equals(Paths.Back1) || k.equals(Paths.Back2) || k.equals(Paths.Back3)) {
            if (robot.pose.position.x > 20 && robot.pose.position.x < 50) {
                Pose2d current = robot.pose;
                vision.setEstHeading(current.heading.toDouble());
                vision.telemetryAprilTag(telemetry);
                List<Pose2d> detectionPositions = vision.getPos();
                int i = 0;
                for(Pose2d exist : detectionPositions) {

                    Tel.instance().addData("exist", exist.position.x);
                    Tel.instance().addData("prev",  exist.position.x);
                    Tel.instance().addData("rawExternalHeading", robot.getRawExternalHeading());
                    if(exist.position.x != previous.get(i).position.x) {
                        robot.pose = new Pose2d(
                                (current.position.x + exist.position.x * aprilTagConfidence) / (1 + aprilTagConfidence),
                                (current.position.y + exist.position.y * aprilTagConfidence) / (1 + aprilTagConfidence),
                                (robot.getRawExternalHeading())
                        );

                    }
                    previous.set(i,exist);
                    i++;
                }
                Pose2d updatePos = robot.pose;
                // if it doesn't pass through apriltag you still want to update imu
                robot.pose = (new Pose2d(updatePos.position.x, updatePos.position.y, robot.getRawExternalHeading()));
            }
        }
    }

    @SafeVarargs
    public static List<Task>[] getTaskList(List<Task>... tasks) {
        return (tasks);
    }

    public void update() {
        updatePoseEstimate();
        TelemetryPacket p = new TelemetryPacket();
        //updateTrajectory(p);
      //  item.getTrajectory().getAction().run(new TelemetryPacket());
    }

    public void waitForIdle(WhipTrajectory item) {
        while (!Thread.currentThread().isInterrupted() && isBusy && Context.opmode.opModeIsActive())
        {

            item.getTrajectory().getAction().run(new TelemetryPacket());
            Method x =null;
            try{
                x= Context.opmode.getClass().getMethod("primaryLoop",(Class<?>[]) null);
            }
            catch(NoSuchMethodException ignored){
                RobotLog.e("waitForIdle");
        }
            if(x==null){
                //RobotLog.e("yep");
                //update();
                break;
            }else {
                //RobotLog.e("nah");
            }
        }
    }
    
    public double piTagOrUs(double x, double y) {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }


   /* public void setMode(DcMotor.RunMode runMode) {
        for (DcMotorEx motor : motors) {
            motor.setMode(runMode);
        }
    }

    */

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
    public double getRawExternalHeading() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
     //   return Math.toRadians(-navx.getYaw());
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

    public double getTotalCurrent() {
        return controlHub.getCurrent(CurrentUnit.AMPS) + expansionHub.getCurrent(CurrentUnit.AMPS);

    }

    public double getCurrentMotor(int index) {
        if (index == 0) {
            return leftFront.getCurrent(CurrentUnit.AMPS);
        }
        if (index == 1) {
            return rightFront.getCurrent(CurrentUnit.AMPS);
        }
        if (index == 2) {
            return leftBack.getCurrent(CurrentUnit.AMPS);
        }
        if (index == 3) {
            return rightBack.getCurrent(CurrentUnit.AMPS);
        }
        return 0;
    }
}

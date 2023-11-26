package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.util.Context;

@Config
@Autonomous
public class auto extends LinearOpMode {

    DcMotor slideLeft,slideRight;
    int slidesPos;
    Servo wrist, pusher
            ;    public static double pusherIn=0.04;
    public static double pusherPushed=0.18;//what is this for

    public static double pusherPrep=0.12;
    public static double pusherOne=0.265;
    public static double pusherTwo=0.4;
    public static double initwrist =.5;
    public static double depositwrist=0.3;

    Intake intake;
    Deposit deposit;
    SampleMecanumDrive m;
    Pose2d startPos = new Pose2d(16,56,Math.toRadians(270));


    @Override
    public void runOpMode() throws InterruptedException {

        Context.resetValues();
        Context.tel=new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);
        m= new SampleMecanumDrive(hardwareMap);
        m.setPoseEstimate(startPos);
        wrist =hardwareMap.get(Servo.class,"wrist");
        Trajectory placePixel2 = m.trajectoryBuilder(startPos)
                .lineToConstantHeading(new Vector2d(16,29))
                .build();
        Trajectory dropPixel2 = m.trajectoryBuilder(placePixel2.end())
                .lineToLinearHeading(new Pose2d(57,31,Math.toRadians(180)),
                        m.getVelocityConstraint(47.5, 1.65, 15.06),
                        m.getAccelerationConstraint(45))
                .build();
        pusher=hardwareMap.get(Servo.class, "pusher");
        deposit=new Deposit(hardwareMap);
        intake=new Intake(hardwareMap);

        slideLeft=hardwareMap.get(DcMotor.class, "slide1");
        slideRight=hardwareMap.get(DcMotor.class, "slide2");
        slideRight.setDirection(DcMotorSimple.Direction.REVERSE);
        slideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideLeft.setTargetPosition(slidesPos);
        slideRight.setTargetPosition(slidesPos);
        slideLeft.setPower(1);
        slideRight.setPower(1);
        slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intake.init();
        deposit.init();
        waitForStart();
        wrist.setPosition(initwrist);
        pusher.setPosition(pusherPrep);
        waitForStart();
        m.followTrajectory(placePixel2);
        intake.setState(Intake.PowerState.LOW);

        intake.writeLoop();
        intake.updateLoop();
        waitT(2500);
        intake.setState(Intake.PowerState.OFF);
        intake.writeLoop();
        intake.updateLoop();
        intake.setOperationState(Module.OperationState.MANUAL);
        m.followTrajectory(dropPixel2);

        slidesPos=50;
        slideLeft.setTargetPosition(slidesPos);
        slideRight.setTargetPosition(slidesPos);
        slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        deposit.setState(Deposit.RotationState.DEPOSIT_HIGH);
        deposit.updateLoop();
        deposit.writeLoop();
        wrist.setPosition(depositwrist);
        while(slideLeft.getCurrentPosition()-slideLeft.getTargetPosition() >20){

            slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        waitT(2500);
        pusher.setPosition(pusherTwo);

        deposit.updateLoop();
        deposit.writeLoop();
        waitT(2500);
        slidesPos = 0;
        slideLeft.setTargetPosition(slidesPos);
        slideRight.setTargetPosition(slidesPos);

        slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while(Math.abs(slideLeft.getCurrentPosition()-slideLeft.getTargetPosition() )>20){
            if(Math.abs(slideLeft.getCurrentPosition()-slideLeft.getTargetPosition() ) >200) {
                deposit.setState(Deposit.RotationState.TRANSFER);
                wrist.setPosition(initwrist);
                pusher.setPosition(pusherIn);

                deposit.updateLoop();
                deposit.writeLoop();
            }

            slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }



    }
    public void waitT(int ticks){
        ElapsedTime  x= new ElapsedTime();
        while(x.milliseconds()<ticks){
            m.update();

            slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }
    public void slidesRaise(){
    }
}
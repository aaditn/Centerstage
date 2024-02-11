package org.firstinspires.ftc.teamcode.non_module_testing;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.util.Tel;
import org.firstinspires.ftc.teamcode.vision.AprilTagPipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a simple teleop routine for testing localization. Drive the robot around like a normal
 * teleop routine and make sure the robot's estimated pose matches the robot's actual pose (slight
 * errors are not out of the ordinary, especially with sudden drive motions). The goal of this
 * exercise is to ascertain whether the localizer has been configured properly (note: the pure
 * encoder localizer heading may be significantly off if the track width has not been tuned).
 */
@TeleOp(group = "drive")
public class SuitedLocalizationTest extends LinearOpMode {
    double aprilTagConfidence =.1;
    private AprilTagPipeline vision = new AprilTagPipeline();


    List<Pose2d> previous = new ArrayList<>();
    @Override

    public void runOpMode() throws InterruptedException {
        Robot drive = new Robot(this, true);

        vision.initAprilTag(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Pose2d startPos = new Pose2d(20,56,Math.toRadians(270));

        int count = 0;

        vision.setEstHeading(drive.getPoseEstimate().getHeading());
        previous.add(new Pose2d(0,0,0));
        previous.add(new Pose2d(0,0,0));
        previous.add(new Pose2d(0,0,0));
        previous.add(new Pose2d(0,0,0));
        previous.add(new Pose2d(0,0,0));
        previous.add(new Pose2d(0,0,0));
        waitForStart();
        drive.setYaw();
        drive.setPoseEstimate(startPos);
        while (!isStopRequested()) {
            drive.setLocalDrivePowers(
                    new Pose2d(
                            gamepad1.left_stick_y,
                            gamepad1.left_stick_x,
                            -gamepad1.right_stick_x
                    )
            );
            drive.primaryLoop();
            drive.update();
            Pose2d current = drive.getPoseEstimate();
            vision.setEstHeading(current.getHeading());
            vision.telemetryAprilTag(telemetry);
            List<Pose2d> detectionPositions = vision.getPos();
            int i = 0;
            for(Pose2d exist : detectionPositions) {

                Tel.instance().addData("exist", exist.getX());
                Tel.instance().addData("prev", previous.get(i).getX());
                Tel.instance().addData("count", count);
                Tel.instance().addData("rawExternalHeading", drive.getRawExternalHeading());
                count++;
                if(exist.getX() != previous.get(i).getX()) {
                    drive.setPoseEstimate(new Pose2d(
                            (current.getX() + exist.getX() * aprilTagConfidence) / (1 + aprilTagConfidence),
                            (current.getY() + exist.getY() * aprilTagConfidence) / (1 + aprilTagConfidence),
                            (drive.getRawExternalHeading())
                    ));

                }
                previous.set(i,exist);
                i++;
            }
            Pose2d updatePos = drive.getPoseEstimate();
            // if it doesn't pass through apriltag you still want to update imu
            drive.setPoseEstimate(new Pose2d(updatePos.getX(), updatePos.getY(), drive.getRawExternalHeading()));

            Tel.instance().addData("x", current.getX());
            Tel.instance().addData("y",current.getY());
            Tel.instance().addData("heading",Math.toDegrees(current.getHeading()));


        }
    }
}

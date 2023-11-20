package org.firstinspires.ftc.teamcode.roadrunner.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

public class Trajectories {
    HardwareMap hardwareMap;
    public Trajectories(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }
    SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
    public static Pose2d blueFarStart = new Pose2d(-36,61,Math.toRadians(270));
    public static Pose2d blueCloseStart = new Pose2d(12,61,Math.toRadians(270));
    public static Pose2d redFarStart = new Pose2d(-36,-61,Math.toRadians(270));
    public static Pose2d redCloseStart = new Pose2d(12,-61,Math.toRadians(270));

    TrajectorySequence blueFarRightPlacePurple = drive.trajectorySequenceBuilder(blueFarStart)
            .splineTo(new Vector2d(-35, 35), Math.toRadians(-90))
            .splineTo(new Vector2d(-38, 23), Math.toRadians(135))
            .build();
    TrajectorySequence blueFarRightFirstCycle = drive.trajectorySequenceBuilder(blueFarRightPlacePurple.end())
            .lineToConstantHeading(new Vector2d(-41, 20))
            .splineToSplineHeading(new Pose2d(-58, 12,Math.toRadians(180)), Math.toRadians(180))
            .build();
    TrajectorySequence blueFarRightPlaceYellow = drive.trajectorySequenceBuilder(blueFarRightFirstCycle.end())
            .lineTo(new Vector2d(8, 12))
            .splineToConstantHeading(new Vector2d(50, 28), Math.toRadians(0))
            .build();
    TrajectorySequence blueFarRightPickAllianceYellow = drive.trajectorySequenceBuilder(blueFarRightPlaceYellow.end())
            .splineToConstantHeading(new Vector2d(30, 61), Math.toRadians(180))
            .build();
    TrajectorySequence blueFarRightPlaceAllianceYellow = drive.trajectorySequenceBuilder(blueFarRightPickAllianceYellow.end())
            .lineToConstantHeading(new Vector2d(31, 61))
            .splineToConstantHeading(new Vector2d(50, 28), Math.toRadians(0))
            .build();

    TrajectorySequence[] blueFarRight = {
            blueFarRightPlacePurple,
            blueFarRightFirstCycle,
            blueFarRightPlaceYellow,
            blueFarRightPickAllianceYellow,
            blueFarRightPlaceAllianceYellow
    };
    TrajectorySequence[] blueCloseRight = {

    };
    TrajectorySequence[] redFarRight = {

    };
    TrajectorySequence[] redCloseRight = {

    };
    TrajectorySequence[] blueFarMid = {

    };
    TrajectorySequence[] blueCloseMid = {

    };
    TrajectorySequence[] redFarMid = {

    };
    TrajectorySequence[] redCloseMid = {

    };
    TrajectorySequence[] blueFarLeft = {

    };
    TrajectorySequence[] blueCloseLeft = {

    };
    TrajectorySequence[] redFarLeft = {

    };
    TrajectorySequence[] redCloseLeft = {

    };
    public TrajectorySequence[] getSequence(String color, String position, String spike) {
        if (color.equals("red")) {
            if (position.equals("close")) {
                if (spike.equals("left")) {
                    return blueCloseLeft;
                } else if (spike.equals("mid")) {
                    return blueCloseMid;
                } else {
                    return blueCloseRight;
                }

            } else { // far
                if (spike.equals("left")) {
                    return blueFarLeft;
                } else if (spike.equals("mid")) {
                    return blueFarMid;
                } else {
                    return blueFarRight;
                }
            }
        } else { // blue
            if (position.equals("close")) {
                if (spike.equals("left")) {
                    return redCloseLeft;
                } else if (spike.equals("mid")) {
                    return redCloseMid;
                } else {
                    return redCloseRight;
                }

            } else { // far
                if (spike.equals("left")) {
                    return redFarLeft;
                } else if (spike.equals("mid")) {
                    return redFarMid;
                } else {
                    return redFarRight;
                }
            }
        }
    }
}


package org.firstinspires.ftc.teamcode.vision;

import android.os.SystemClock;
import android.util.Log;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.MovingStatistics;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.matrices.GeneralMatrixF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.apriltag.AprilTagCanvasAnnotator;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;
import org.firstinspires.ftc.vision.apriltag.AprilTagMetadata;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseRaw;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessorImpl;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.imgproc.Imgproc;
import org.openftc.apriltag.AprilTagDetectorJNI;
import org.openftc.apriltag.ApriltagDetectionJNI;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
@Config
public class AprilTagTestPipeline extends OpenCvPipeline
{
    double startTime, loopCount;
    public double FPS, loopTime;
    double fx, fy, cx, cy, width, height;

    private long nativeApriltagPtr;
    private Mat grey = new Mat();
    private ArrayList<AprilTagDetection> detections = new ArrayList<>();

    private Mat cameraMatrix;

    private final AprilTagLibrary tagLibrary;

    private final DistanceUnit outputUnitsLength = DistanceUnit.INCH;
    private final AngleUnit outputUnitsAngle = AngleUnit.RADIANS;

    private volatile PoseSolver poseSolver = PoseSolver.OPENCV_ITERATIVE;
    public static int threadCount=3;
    public static boolean detectionEnabled=true;
    public boolean running;

    public AprilTagTestPipeline()
    {
        tagLibrary = AprilTagGameDatabase.getCurrentGameTagLibrary();

        // Allocate a native context object. See the corresponding deletion in the finalizer
        nativeApriltagPtr = AprilTagDetectorJNI.createApriltagDetector(TagFamily.TAG_36h11.ATLibTF.string, 3, threadCount);

        width=CameraConstants.width;
        height=CameraConstants.height;
        fx=CameraConstants.fx;
        fy=CameraConstants.fy;
        cx=CameraConstants.cx;
        cy=CameraConstants.cy;

        constructMatrix();
    }
    public void init(Mat input)
    {
        startTime = SystemClock.elapsedRealtime();
    }
    @Override
    public Mat processFrame(Mat input)
    {
        if(detectionEnabled)
        {
            running=true;
            Imgproc.cvtColor(input, grey, Imgproc.COLOR_RGBA2GRAY);

            // Run AprilTag
            detections = runAprilTagDetectorForMultipleTagSizes(SystemClock.elapsedRealtimeNanos());
        }
        else
        {
            running=false;
        }

        return input;
    }

    protected void finalize()
    {
        // Might be null if createApriltagDetector() threw an exception
        if(nativeApriltagPtr != 0)
        {
            // Delete the native context we created in the constructor
            AprilTagDetectorJNI.releaseApriltagDetector(nativeApriltagPtr);
            nativeApriltagPtr = 0;
        }
        else
        {
            System.out.println("AprilTagDetectionPipeline.finalize(): nativeApriltagPtr was NULL");
        }
    }

    private void resetLoopTracker(double startTime)
    {
        this.startTime=startTime;
        loopCount=0;
    }
    private MovingStatistics solveTime = new MovingStatistics(50);
    ArrayList<AprilTagDetection> runAprilTagDetectorForMultipleTagSizes(long captureTimeNanos)
    {
        long ptrDetectionArray = AprilTagDetectorJNI.runApriltagDetector(nativeApriltagPtr, grey.dataAddr(), grey.width(), grey.height());
        if (ptrDetectionArray != 0)
        {
            long[] detectionPointers = ApriltagDetectionJNI.getDetectionPointers(ptrDetectionArray);
            ArrayList<AprilTagDetection> detections = new ArrayList<>(detectionPointers.length);

            for (long ptrDetection : detectionPointers)
            {
                AprilTagMetadata metadata = tagLibrary.lookupTag(ApriltagDetectionJNI.getId(ptrDetection));

                double[][] corners = ApriltagDetectionJNI.getCorners(ptrDetection);

                Point[] cornerPts = new Point[4];
                for (int p = 0; p < 4; p++)
                {
                    cornerPts[p] = new Point(corners[p][0], corners[p][1]);
                }

                AprilTagPoseRaw rawPose;
                AprilTagPoseFtc ftcPose;

                if (metadata != null)
                {

                    long startSolveTime = System.currentTimeMillis();

                    if (poseSolver == PoseSolver.APRILTAG_BUILTIN)
                    {
                        double[] pose = ApriltagDetectionJNI.getPoseEstimate(
                                ptrDetection,
                                outputUnitsLength.fromUnit(metadata.distanceUnit, metadata.tagsize),
                                fx, fy, cx, cy);

                        // Build rotation matrix
                        float[] rotMtxVals = new float[3 * 3];
                        for (int i = 0; i < 9; i++)
                        {
                            rotMtxVals[i] = (float) pose[3 + i];
                        }

                        rawPose = new AprilTagPoseRaw(
                                pose[0], pose[1], pose[2], // x y z
                                new GeneralMatrixF(3, 3, rotMtxVals)); // R
                    }
                    else
                    {
                        AprilTagTestPipeline.Pose opencvPose = poseFromTrapezoid(
                                cornerPts,
                                cameraMatrix,
                                outputUnitsLength.fromUnit(metadata.distanceUnit, metadata.tagsize), poseSolver.code);

                        // Build rotation matrix
                        Mat R = new Mat(3, 3, CvType.CV_32F);
                        Calib3d.Rodrigues(opencvPose.rvec, R);
                        float[] tmp2 = new float[9];
                        R.get(0,0, tmp2);

                        rawPose = new AprilTagPoseRaw(
                                opencvPose.tvec.get(0,0)[0], // x
                                opencvPose.tvec.get(1,0)[0], // y
                                opencvPose.tvec.get(2,0)[0], // z
                                new GeneralMatrixF(3,3, tmp2)); // R
                    }

                    long endSolveTime = System.currentTimeMillis();
                    solveTime.add(endSolveTime-startSolveTime);
                }
                else
                {
                    // We don't know anything about the tag size so we can't solve the pose
                    rawPose = null;
                }

                if (rawPose != null)
                {
                    Orientation rot = Orientation.getOrientation(rawPose.R, AxesReference.INTRINSIC, AxesOrder.YXZ, outputUnitsAngle);

                    ftcPose = new AprilTagPoseFtc(
                            rawPose.x,  // x   NB: These are *intentionally* not matched directly;
                            rawPose.z,  // y       this is the mapping between the AprilTag coordinate
                            -rawPose.y, // z       system and the FTC coordinate system
                            -rot.firstAngle, // yaw
                            rot.secondAngle, // pitch
                            rot.thirdAngle,  // roll
                            Math.hypot(rawPose.x, rawPose.z), // range
                            outputUnitsAngle.fromUnit(AngleUnit.RADIANS, Math.atan2(-rawPose.x, rawPose.z)), // bearing
                            outputUnitsAngle.fromUnit(AngleUnit.RADIANS, Math.atan2(-rawPose.y, rawPose.z))); // elevation
                }
                else
                {
                    ftcPose = null;
                }

                double[] center = ApriltagDetectionJNI.getCenterpoint(ptrDetection);

                detections.add(new AprilTagDetection(
                        ApriltagDetectionJNI.getId(ptrDetection),
                        ApriltagDetectionJNI.getHamming(ptrDetection),
                        ApriltagDetectionJNI.getDecisionMargin(ptrDetection),
                        new Point(center[0], center[1]), cornerPts, metadata, ftcPose, rawPose, captureTimeNanos));
            }

            ApriltagDetectionJNI.freeDetectionList(ptrDetectionArray);
            return detections;
        }

        return new ArrayList<>();
    }

    public int getPerTagAvgPoseSolveTime()
    {
        return (int) Math.round(solveTime.getMean());
    }

    public ArrayList<AprilTagDetection> getDetections()
    {
        return detections;
    }


    void constructMatrix()
    {
        //     Construct the camera matrix.
        //
        //      --         --
        //     | fx   0   cx |
        //     | 0    fy  cy |
        //     | 0    0   1  |
        //      --         --
        //

        cameraMatrix = new Mat(3,3, CvType.CV_32FC1);

        cameraMatrix.put(0,0, fx);
        cameraMatrix.put(0,1,0);
        cameraMatrix.put(0,2, cx);

        cameraMatrix.put(1,0,0);
        cameraMatrix.put(1,1,fy);
        cameraMatrix.put(1,2,cy);

        cameraMatrix.put(2, 0, 0);
        cameraMatrix.put(2,1,0);
        cameraMatrix.put(2,2,1);
    }

    /**
     * Converts an AprilTag pose to an OpenCV pose
     * @param aprilTagPose pose to convert
     * @return OpenCV output pose
     */
    static AprilTagTestPipeline.Pose aprilTagPoseToOpenCvPose(AprilTagPoseRaw aprilTagPose)
    {
        AprilTagTestPipeline.Pose pose = new AprilTagTestPipeline.Pose();
        pose.tvec.put(0,0, aprilTagPose.x);
        pose.tvec.put(1,0, aprilTagPose.y);
        pose.tvec.put(2,0, aprilTagPose.z);

        Mat R = new Mat(3, 3, CvType.CV_32F);

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                R.put(i,j, aprilTagPose.R.get(i,j));
            }
        }

        Calib3d.Rodrigues(R, pose.rvec);

        return pose;
    }

    /**
     * Extracts 6DOF pose from a trapezoid, using a camera intrinsics matrix and the
     * original size of the tag.
     *
     * @param points the points which form the trapezoid
     * @param cameraMatrix the camera intrinsics matrix
     * @param tagsize the original length of the tag
     * @return the 6DOF pose of the camera relative to the tag
     */
    static AprilTagTestPipeline.Pose poseFromTrapezoid(Point[] points, Mat cameraMatrix, double tagsize, int solveMethod)
    {
        // The actual 2d points of the tag detected in the image
        MatOfPoint2f points2d = new MatOfPoint2f(points);

        // The 3d points of the tag in an 'ideal projection'
        Point3[] arrayPoints3d = new Point3[4];
        arrayPoints3d[0] = new Point3(-tagsize/2, tagsize/2, 0);
        arrayPoints3d[1] = new Point3(tagsize/2, tagsize/2, 0);
        arrayPoints3d[2] = new Point3(tagsize/2, -tagsize/2, 0);
        arrayPoints3d[3] = new Point3(-tagsize/2, -tagsize/2, 0);
        MatOfPoint3f points3d = new MatOfPoint3f(arrayPoints3d);

        // Using this information, actually solve for pose
        AprilTagTestPipeline.Pose pose = new AprilTagTestPipeline.Pose();
        Calib3d.solvePnP(points3d, points2d, cameraMatrix, new MatOfDouble(), pose.rvec, pose.tvec, false, solveMethod);

        return pose;
    }

    static class Pose
    {
        Mat rvec;
        Mat tvec;

        public Pose()
        {
            rvec = new Mat(3, 1, CvType.CV_32F);
            tvec = new Mat(3, 1, CvType.CV_32F);
        }

        public Pose(Mat rvec, Mat tvec)
        {
            this.rvec = rvec;
            this.tvec = tvec;
        }
    }

    public enum PoseSolver
    {
        APRILTAG_BUILTIN(-1),
        OPENCV_ITERATIVE(Calib3d.SOLVEPNP_ITERATIVE),
        OPENCV_SOLVEPNP_EPNP(Calib3d.SOLVEPNP_EPNP),
        OPENCV_IPPE(Calib3d.SOLVEPNP_IPPE),
        OPENCV_IPPE_SQUARE(Calib3d.SOLVEPNP_IPPE_SQUARE),
        OPENCV_SQPNP(Calib3d.SOLVEPNP_SQPNP);

        final int code;

        PoseSolver(int code)
        {
            this.code = code;
        }
    }

    public enum TagFamily
    {
        TAG_36h11(AprilTagDetectorJNI.TagFamily.TAG_36h11),
        TAG_25h9(AprilTagDetectorJNI.TagFamily.TAG_25h9),
        TAG_16h5(AprilTagDetectorJNI.TagFamily.TAG_16h5),
        TAG_standard41h12(AprilTagDetectorJNI.TagFamily.TAG_standard41h12);

        final AprilTagDetectorJNI.TagFamily ATLibTF;

        TagFamily(AprilTagDetectorJNI.TagFamily ATLibTF)
        {
            this.ATLibTF = ATLibTF;
        }
    }
}

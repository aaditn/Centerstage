package org.firstinspires.ftc.teamcode.vision;

import static org.opencv.imgproc.Imgproc.MORPH_CLOSE;
import static org.opencv.imgproc.Imgproc.MORPH_OPEN;
//import static org.opencv.imgproc.Imgproc.rectangle;

//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.enums.Dice;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class TeamElementDetection extends OpenCvPipeline{

    Telemetry tel;
    Scalar rLowHSV;
    Scalar rHighHSV;
    Scalar rLowHSV2;
    Scalar rHighHSV2;

    Scalar bLowHSV;
    Scalar bHighHSV;
    public static int HLow = 0;
    public static int SLow = 0;
    public static int VLow = 0;
    public static int HHigh = 10;
    public static int SHigh = 255;
    public static int VHigh = 255;


    public static int HLow2 = 170;
    public static int SLow2 = 0;
    public static int VLow2 = 0;
    public static int HHigh2 = 182;//H ranges 0-179
    public static int SHigh2 = 255;
    public static int VHigh2 = 255;

    public static int bHLow = 103;
    public static int bSLow = 0;
    public static int bVLow = 0;
    public static int bHHigh = 130;
    public static int bSHigh = 255;
    public static int bVHigh = 255;

    public int threshold = 1000;//change if needed
    public double centerY = -1;
    public double largestArea = -1;
    List<MatOfPoint> Contours=new ArrayList<MatOfPoint>();
    Mat HSV = new Mat();
    Mat kernel = new Mat();
    Mat kernel2 = new Mat();
    Mat inRange = new Mat();
    Mat inRange2 = new Mat();
    Mat combined = new Mat();
    Mat morphed = new Mat();
    Mat morphed2 = new Mat();
    Mat hierarchy = new Mat();
    Mat mat = new Mat();
    Mat maskTemplate = new Mat();
    Moments m;

    public TeamElementDetection(Telemetry tel){
        this.tel=tel;
    }


    public void init(Mat input){
        rLowHSV = new Scalar(HLow, SLow, VLow);
        rHighHSV = new Scalar(HHigh, SHigh, VHigh);
        rLowHSV2 = new Scalar(HLow2, SLow2, VLow2);
        rHighHSV2 = new Scalar(HHigh2, SHigh2, VHigh2);

        bLowHSV = new Scalar(bHLow, bSLow, bVLow);
        bHighHSV = new Scalar(bHHigh, bSHigh, bVHigh);

        maskTemplate=new Mat(input.rows()-1, input.cols()-1, CvType.CV_8U, new Scalar(255, 255, 255));
        kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(15, 15));//13, 17
        kernel2 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(15, 15));
    }
    public void release(){
        //HSV.release();
        //kernel.release();
        //kernel2.release();
        inRange.release();
        inRange2.release();
        combined.release();
        morphed.release();
        morphed2.release();
        hierarchy.release();
        mat.release();
        //maskTemplate.release();
        Contours.clear();
    }
    public Mat processFrame(Mat input){
        Contours.clear();
        hierarchy.release();
        //if(input.rows()>0&&input.get(0, 0)[0]>0)
        //{
            Imgproc.cvtColor(input, HSV, Imgproc.COLOR_RGB2HSV);

            if(Context.isTeamRed){
                Core.inRange(HSV, rLowHSV, rHighHSV, inRange);
                Core.inRange(HSV, rLowHSV2, rHighHSV2, inRange2);
                Core.bitwise_or(inRange, inRange2, combined);
            } else {
                Core.inRange(HSV, bLowHSV, bHighHSV, combined);
            }

            Imgproc.morphologyEx(combined, morphed, MORPH_OPEN, kernel);
            Imgproc.morphologyEx(morphed, morphed2, MORPH_CLOSE, kernel2);
            Imgproc.findContours(morphed2, Contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            //tel.addData("Contour count", Contours.size());
            //tel.update();

            int contourIndex=-1;
            double contourArea=-1;

            for (int i = 0; i < Contours.size(); i++){

                double area = Imgproc.contourArea(Contours.get(i));

                if(area > contourArea && area > threshold){
                    contourArea = area;
                    contourIndex = i;
                }

            }
            if (Contours.size() > 0&& contourIndex>=0){
                mat = input.clone();//empty- all black
                Imgproc.drawContours(mat, Contours, contourIndex, new Scalar(255, 255, 255), -1);
                m = Imgproc.moments(Contours.get(contourIndex));
                if(m.m00!=0)
                {
                    centerY =m.m01/m.m00;
                }
                else
                {
                    centerY =-1;
                }
                largestArea=contourArea;
            }
            else{
                mat=input;
                centerY =-1;
            }

            //TODO change what the if/elses are
            if(centerY<0)
                Context.dice=Dice.LEFT;
            else if(centerY<107)
                Context.dice=Dice.MIDDLE;
            else
                Context.dice=Dice.RIGHT;

            return mat;
        //}
        //Imgproc.rectangle(maskTemplate, new Rect(0, 100, 50, 100), rLowHSV);
        //return input;
    }

    public double getLargestArea()
    {
        return largestArea;
    }


}

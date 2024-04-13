package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.Slides;

public class PresetPackage {
    public Deposit.RotateState rotateState;
    public Slides.SlideState slideState;

    public Deposit.WristState wristState;
    public String color1;
    public String color2;

    public PresetPackage(int index, String color1, String color2, Deposit.RotateState rotateState, Slides.SlideState slideState) {
        this.rotateState = rotateState;
        this.slideState = slideState;
        this.color1 = color1;
        this.color2 = color2;
    }

    public PresetPackage(Slides.SlideState slideState, Deposit.RotateState rotateState, Deposit.WristState wristState) {
        this.rotateState = rotateState;
        this.slideState = slideState;
        this.wristState = wristState;

    }
    public void returnLEDs(Gamepad gamepad) {
        if (color1.equals("purple")) {
            gamepad.setLedColor(0.54, 0.17, 0.89, 100000);
        } else if (color1.equals("yellow")) {
            gamepad.setLedColor(1, 1, 0, 100000);
        } else if (color1.equals("green")) {
            gamepad.setLedColor(0, 1, 0, 100000);
        } else if (color1.equals("white")) {
            gamepad.setLedColor(1, 1, 1, 100000);
        } else {
            gamepad.setLedColor(0, 0, 0, 100000);
        }
    }


}

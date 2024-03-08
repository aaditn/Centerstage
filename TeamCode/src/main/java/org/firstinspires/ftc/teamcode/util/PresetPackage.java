package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.teamcode.modules.Deposit;
import org.firstinspires.ftc.teamcode.modules.Slides;

public class PresetPackage {
    public Deposit.RotateState rotateState;
    public Slides.SlideState slideState;

    public PresetPackage(int index, String id, Deposit.RotateState rotateState, Slides.SlideState slideState) {
        this.rotateState = rotateState;
        this.slideState = slideState;
    }

}

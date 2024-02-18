package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.Robot
import org.firstinspires.ftc.teamcode.util.AutoSelector.CyclePixelCount
import org.firstinspires.ftc.teamcode.util.enums.Dice

object Context
{
    @JvmField var tel: MultipleTelemetry? = null
    @JvmField var isTeamRed: Boolean = true
    @JvmField var debug: Int = 0
    @JvmField var opmode: LinearOpMode? = null;
    @JvmField var isTele: Boolean = true
    @JvmField var statusError: String ="Functioning Normally"
    @JvmField var autoWaitTime: Int = 0
    @JvmField var autoState: CyclePixelCount = CyclePixelCount.ZERO
    @JvmField var colorSensorsEnabled: Boolean = false
    @JvmField var trajStatus: String = "Not Loaded"
    @JvmField var dice: Dice = Dice.UNINITIALIZED
    @JvmField var dashTeleEnabled: Boolean = true
    @JvmField var noHwInit: Boolean=false

    @JvmStatic fun updateValues()
    {
        if (opmode!!.javaClass.isAnnotationPresent(Autonomous::class.java))
        {
            isTele=false
        }
        dashTeleEnabled= Robot.dashTeleEnabled
    }

    @JvmStatic fun resetValues()
    {
        tel=null;
        isTeamRed=true;
        isTele=true;
        debug=0;
        statusError="Functioning Normally"
        autoWaitTime=0
        autoState= CyclePixelCount.ZERO
        colorSensorsEnabled=false
        trajStatus="Not Loaded"
        dice= Dice.UNINITIALIZED
        dashTeleEnabled=true
        noHwInit=false
    }
    @JvmStatic fun clearValues()
    {
        //opmode=null;
        tel=null;
        isTeamRed=true;
        isTele=true;
        debug=0;
        statusError="Functioning Normally"
        autoWaitTime=0
        autoState= CyclePixelCount.ZERO
        colorSensorsEnabled=false
        trajStatus="Not Loaded"
        dice= Dice.UNINITIALIZED
        dashTeleEnabled=true
        noHwInit=false
    }
}
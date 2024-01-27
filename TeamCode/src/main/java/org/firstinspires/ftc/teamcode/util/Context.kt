package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.util.AutoSelector.Auto_State

object Context
{
    @JvmField var tel: MultipleTelemetry? = null
    @JvmField var isTeamRed: Boolean = true
    @JvmField var debug: Int = 0
    @JvmField var opmode: LinearOpMode? = null;
    @JvmField var isTele: Boolean = true
    @JvmField var statusError: String ="Functioning Normally"
    @JvmField var autoWaitTime: Int = 0
    @JvmField var autoState: Auto_State = Auto_State.TEMP
    @JvmField var colorSensorsEnabled: Boolean = false
    @JvmField var trajStatus: String = "Not Loaded"

    @JvmStatic fun updateValues()
    {
        if (opmode!!.javaClass.isAnnotationPresent(Autonomous::class.java))
        {
            isTele=false
        }
    }

    @JvmStatic fun resetValues()
    {
        tel=null;
        isTeamRed=true;
        isTele=true;
        debug=0;
        statusError="Functioning Normally"
        autoWaitTime=0
        autoState=Auto_State.TEMP
        colorSensorsEnabled=false
        trajStatus="Not Loaded"
    }
    @JvmStatic fun clearValues()
    {
        opmode=null;
        tel=null;
        isTeamRed=true;
        isTele=true;
        debug=0;
        statusError="Functioning Normally"
        autoWaitTime=0
        autoState=Auto_State.TEMP
        colorSensorsEnabled=false
        trajStatus="Not Loaded"
    }
}
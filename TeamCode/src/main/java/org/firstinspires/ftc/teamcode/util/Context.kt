package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

object Context
{
    @JvmField var tel: MultipleTelemetry? = null
    @JvmField var isTeamRed: Boolean = true
    @JvmField var debug: Int = 0
    @JvmField var opmode: LinearOpMode? = null;
    @JvmField var isTele: Boolean = true

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
    }
}
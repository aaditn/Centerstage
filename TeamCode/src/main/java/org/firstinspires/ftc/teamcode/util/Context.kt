package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry

object Context
{
    @JvmField var tel: MultipleTelemetry? = null
    @JvmField var isTeamRed: Boolean = true
    @JvmField var debug: Int = 0

    @JvmStatic fun updateValues()
    {

    }

    @JvmStatic fun resetValues()
    {
        tel=null;
        isTeamRed=true;
        debug=0;
    }
}
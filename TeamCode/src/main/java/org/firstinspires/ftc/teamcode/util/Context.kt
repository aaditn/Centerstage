package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry

object Context
{
    @JvmField var tel: MultipleTelemetry? = null

    @JvmStatic fun updateValues()
    {

    }

    @JvmStatic fun resetValues()
    {
        tel=null;
    }
}
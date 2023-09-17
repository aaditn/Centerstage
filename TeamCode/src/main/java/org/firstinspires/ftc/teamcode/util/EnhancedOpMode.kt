package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class EnhancedOpMode: LinearOpMode ()
{

    var loopTimes: Long=0;
    private var error: String="None";
    var currentCRContext: CoroutineContext? = null
    var linearElapsedTime: ElapsedTime? = null
    private val dash = FtcDashboard.getInstance()

    override fun runOpMode()
    {
        linearElapsedTime = ElapsedTime()
        initialize()
        startCoroutine()
        linearOpMode()
        while(!isStopRequested)
        {

        }
    }

    protected open fun runBlocking(a: Action) {
        val c = Canvas()
        a.preview(c)
        var b = true
        while (b && !isStopRequested) {
            val p = TelemetryPacket()
            p.fieldOverlay().operations.addAll(c.operations)
            b = a.run(p)
            dash.sendTelemetryPacket(p)
        }
    }

    private fun printError()
    {
        telemetry.addData("Coroutine Error", error)
    }
    abstract fun linearOpMode()
    abstract fun initialize()
    open fun initLoop(){}
    open fun onStart(){}
    abstract fun primaryLoop()

    fun delayLinear(delay: Long)
    {
        linearElapsedTime?.reset()
        while(linearElapsedTime?.milliseconds()!! < delay)
        {

        }
    }

    open fun onEnd(){}

    private fun startCoroutine()
    {

        GlobalScope.launch (Dispatchers.Main)
        {
            while(!isStarted&&!isStopRequested)
            {
                try
                {
                    initLoop()
                    printError()
                    delay(loopTimes)
                }
                catch(e: Exception)
                {
                    error=e.toString();
                }
            }


            try
            {
                onStart()
                printError()
            }
            catch(e: Exception)
            {
                error=e.toString();
            }


            while(!isStopRequested)
            {
                try
                {
                    primaryLoop()
                    printError()
                    delay(loopTimes)
                }
                catch(e: Exception)
                {
                    error=e.toString();
                }
            }


            try
            {
                onEnd()
                printError()
            }
            catch(e: Exception)
            {
                error=e.toString();
            }
        }
    }
}
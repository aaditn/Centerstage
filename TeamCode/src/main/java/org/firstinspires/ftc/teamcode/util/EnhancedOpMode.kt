package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class EnhancedOpMode: LinearOpMode ()
{

    var loopTimes: Long=0;
    private var error: String="None";

    override fun runOpMode()
    {
        initialize()
        startCoroutine()
        linearOpMode()
        while(!isStopRequested)
        {

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
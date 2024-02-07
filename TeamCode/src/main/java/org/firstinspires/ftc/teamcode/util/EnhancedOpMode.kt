package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.FtcDashboard
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.firstinspires.ftc.teamcode.Robot
import org.firstinspires.ftc.teamcode.modules.RobotActions
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleStateConverter
import org.firstinspires.ftc.teamcode.util.Context.clearValues
import org.firstinspires.ftc.teamcode.util.Context.opmode
import kotlin.coroutines.CoroutineContext

abstract class EnhancedOpMode(): LinearOpMode ()
{

    var loopTimes: Long=0;
    private var error: String="None";
    var currentCRContext: CoroutineContext? = null
    var linearElapsedTime: ElapsedTime? = null
    private val dash = FtcDashboard.getInstance()

    override fun runOpMode()
    {
        linearElapsedTime = ElapsedTime()

        resetStaticVars()
        Context.opmode=this

        initialize()
        startCoroutine()
        linearOpMode()
        while(!isStopRequested)
        {

        }
        resetStaticVars()
    }

    private fun printError()
    {
        telemetry.addData("Coroutine Error", error)
    }
    private fun resetStaticVars()
    {
        clearValues()
        Robot.destroyInstance()
        RobotActions.deleteInstance()
        ModuleStateConverter.deleteInstance()
        AutoSelector.destroyInstance()
        Tel.destroyInstance()
    }
    abstract fun linearOpMode()
    abstract fun initialize()
    open fun initLoop(){}
    open fun onStart(){}
    abstract fun primaryLoop()

    fun delayLinear(delay: Long)
    {
        linearElapsedTime?.reset()
        while(linearElapsedTime?.milliseconds()!! < delay && opmode!!.opModeIsActive())
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
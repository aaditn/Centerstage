package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.opmodes.tele.TeleOpRewrite;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.TaskListBuilder;
import org.firstinspires.ftc.teamcode.util.Context;

import java.util.List;
@Config
public class RobotActions
{
    static RobotActions robotActions;
    Robot robot;
    Slides slides;
    Deposit deposit;
    Intake intake;
    TaskListBuilder builder;
    Intake.SweeperState[] sweepStates = {Intake.SweeperState.INIT,Intake.SweeperState.ONE_SWEEP, Intake.SweeperState.TWO_SWEEP,Intake.SweeperState.THREE_SWEEP,Intake.SweeperState.FOUR_SWEEP};

    public static double slidesDelay=2000;
    public static RobotActions getInstance()
    {
        if (robotActions == null) {
            robotActions = new RobotActions(Robot.getInstance());
        }
        return robotActions;
    }
    public List<Task> raiseIntake(double xPos)
    {
        return builder.createNew()
                .await(()->robot.getPoseEstimate().getX()<xPos)
                .moduleAction(intake, Intake.PositionState.MID)
                .moduleAction(intake, Intake.SweeperState.ZERO)
                .moduleAction(intake, Intake.PowerState.OFF)
                .moduleAction(intake, Intake.ConveyorState.OFF)
                .build();
    }

    public List<Task> lowerIntake(double drop_x , double sweep_x,int cycle)
    {
        switch(cycle) {
            case 0:
                return builder.createNew()
                        .await(() -> robot.getPoseEstimate().getX() < drop_x)
                        .moduleAction(intake, Intake.PositionState.DOWN)
                        .moduleAction(intake, Intake.PowerState.INTAKE)
                        .moduleAction(intake, Intake.ConveyorState.INTAKE)
                        .await(() -> robot.getPoseEstimate().getX() < sweep_x)
                        .moduleAction(intake, Intake.SweeperState.TWO_SWEEP)
                        .delay(750)
                        .moduleAction(intake, Intake.SweeperState.THREE_SWEEP)
                        .build();

            case 1:
                return builder.createNew()
                        .await(() -> robot.getPoseEstimate().getX() < drop_x)
                        .moduleAction(intake, Intake.PositionState.DOWN)
                        .moduleAction(intake, Intake.PowerState.INTAKE)
                        .moduleAction(intake, Intake.ConveyorState.INTAKE)
                        .await(() -> robot.getPoseEstimate().getX() < sweep_x)
                        .moduleAction(intake, Intake.SweeperState.FOUR_SWEEP)
                        .delay(750)
                        .moduleAction(intake, Intake.SweeperState.FIVE_SWEEP)
                        .build();

            case 2:
                return builder.createNew()
                        .await(() -> robot.getPoseEstimate().getX() < drop_x)
                        .moduleAction(intake, Intake.PositionState.DOWN)
                        .moduleAction(intake, Intake.PowerState.INTAKE)
                        .moduleAction(intake, Intake.ConveyorState.INTAKE)
                        .await(() -> robot.getPoseEstimate().getX() < sweep_x)
                        .moduleAction(intake, Intake.SweeperState.SIX_SWEEP)
                        .delay(750)
                        .moduleAction(intake, Intake.SweeperState.SEVEN_SWEEP)
                        .build();
        }
        return builder.createNew().build();
    }
    public static void deleteInstance()
    {
        robotActions=null;
    }

    public RobotActions(Robot robot)
    {
        this.robot=robot;
        builder=new TaskListBuilder(Context.opmode, robot);

        deposit=robot.deposit;
        slides=robot.slides;
        intake=robot.intake;
    }
    public List<Task> autoRaiseSlides(Slides.SlideState row)
    {
        if(slides.getState()==Slides.SlideState.GROUND)
        {
            return builder.createNew()
                    .executeCode(()->slides.macroRunning=true)
                    .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                    .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                    .delay(50)
                    .moduleAction(slides, row)
                    .delay(200)
                    .moduleAction(deposit, Deposit.WristState.HOVER)
                    .await(slides::isIdle)
                    .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                    .executeCode(()->slides.macroRunning=false)
                    .build();
        }

        return builder.createNew()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(slides, row)
                .awaitPreviousModuleActionCompletion()
                .executeCode(()->slides.macroRunning=false)
                .build();
    }

    public List<Task> runSweepersAuto(double xPos,boolean x)
    {
        return builder.createNew()
                .await(()->robot.getPoseEstimate().getX()<xPos)
                .moduleAction(intake, Intake.SweeperState.FOUR_SWEEP)
                .delay(750)
                .moduleAction(intake, Intake.SweeperState.FIVE_SWEEP)
                .build();
    }
    public List<Task> deployPurple(double... yPos){
        switch(Context.dice) {
            default:
            return builder.createNew()
                    .delay(500)
                    .moduleAction(intake, Intake.PositionState.DOWN)
                    .await(() -> Math.abs(robot.getPoseEstimate().getY()) < yPos[0])
                    .moduleAction(intake, Intake.SweeperState.ONE_SWEEP)
                    .delay(500)
                    .moduleAction(intake, Intake.PositionState.MID)
                    .build();
            case MIDDLE:
                return builder.createNew()
                        .delay(500)
                        .moduleAction(intake, Intake.PositionState.DOWN)
                        .await(() -> Math.abs(robot.getPoseEstimate().getY()) < yPos[1])
                        .moduleAction(intake, Intake.SweeperState.ONE_SWEEP)
                        .delay(500)
                        .moduleAction(intake, Intake.PositionState.MID)
                        .build();

            case RIGHT:
                return builder.createNew()
                        .delay(500)
                        .moduleAction(intake, Intake.PositionState.DOWN)
                        .await(() -> Math.abs(robot.getPoseEstimate().getY()) < yPos[2])
                        .moduleAction(intake, Intake.SweeperState.ONE_SWEEP)
                        .delay(500)
                        .moduleAction(intake, Intake.PositionState.MID)
                        .build();
        }
    }
    public List<Task> yellowDrop(double xPos){
        return builder.createNew()
                .await(()->robot.getPoseEstimate().getX()>15)
                .executeCode(()-> RobotLog.e("s"))
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                .delay(1000)
                .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                .delay(150)
                .moduleAction(deposit, Deposit.FlipState.DOWN)
                .delay(300)
                .moduleAction(slides, Slides.SlideState.AUTO_LOW)
                .moduleAction(deposit, Deposit.WristState.HOVER)
                .delay(400)
                //.await(()->slides.getStatus()==Module.Status.IDLE)
                .delay(200)
                .executeCode(()->slides.macroRunning=false)
                .await(()->robot.getPoseEstimate().getX()>xPos)

                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .delay(300)
                .moduleAction(deposit, Deposit.ExtensionState.TRANSFER_PARTIAL)
                .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                .delay(500)
                .moduleAction(deposit, Deposit.RotateState.ZERO)
                .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                .moduleAction(deposit, Deposit.WristState.PARTIAL2)
                .delay(800)
                .executeCode(()->slides.setOperationState(Module.OperationState.PRESET))
                .delay(200)
                .moduleAction(slides, Slides.SlideState.GROUND)
                .delay(100)
                .await(()->slides.currentPosition()<150)
                //.moduleAction(deposit, Deposit.WristState.PARTIAL2)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .await(slides::isIdle)
                .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                .executeCode(()->slides.macroRunning=false)
                .build();
    }
    public List<Task> scorePixels(double xPos, TeleOpRewrite.DepositState state){
        switch(state) {
            case LEFT:
                return builder.createNew()
                        .await(() -> robot.getPoseEstimate().getX() > 15)
                        .executeCode(() -> RobotLog.e("s"))

                        .executeCode(() -> slides.macroRunning = true)
                        .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                        .delay(1000)
                        .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                        .delay(150)
                        .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                        .delay(300)
                        .moduleAction(slides, Slides.SlideState.AUTO_TWO)
                        .moduleAction(deposit, Deposit.WristState.HOVER)
                        .delay(400)
                        //.await(()->slides.getStatus()==Module.Status.IDLE)
                        .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                        .moduleAction(deposit, Deposit.RotateState.PLUS_NINETY)
                        .delay(200)
                        .moduleAction(deposit, Deposit.ExtensionState.DEPOSIT)
                        .executeCode(() -> slides.macroRunning = false)
                        .await(() -> robot.getPoseEstimate().getX() > xPos)

                        .executeCode(() -> slides.macroRunning = true)
                        .moduleAction(deposit, Deposit.ClawState.OPEN)
                        .delay(300)
                        .moduleAction(deposit, Deposit.ExtensionState.TRANSFER_PARTIAL)
                        .delay(500)
                        .moduleAction(deposit, Deposit.RotateState.ZERO)
                        .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                        .moduleAction(deposit, Deposit.WristState.PARTIAL2)
                        .delay(800)
                        .executeCode(() -> slides.setOperationState(Module.OperationState.PRESET))
                        .delay(200)
                        .moduleAction(slides, Slides.SlideState.GROUND)
                        .delay(100)
                        .await(() -> slides.currentPosition() < 150)
                        //.moduleAction(deposit, Deposit.WristState.PARTIAL2)
                        .moduleAction(deposit, Deposit.ClawState.OPEN)
                        .await(slides::isIdle)
                        .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                        .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                        .executeCode(() -> slides.macroRunning = false)
                        .build();
            case RIGHT:
                return builder.createNew()
                        .await(()->robot.getPoseEstimate().getX()>15)
                        .executeCode(()-> RobotLog.e("s"))

                        .executeCode(()->slides.macroRunning=true)
                        .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                        .delay(1000)
                        .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                        .delay(150)
                        .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                        .delay(300)
                        .moduleAction(slides, Slides.SlideState.AUTO_TWO)
                        .moduleAction(deposit, Deposit.WristState.HOVER)
                        .delay(400)
                        //.await(()->slides.getStatus()==Module.Status.IDLE)
                        .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                        .moduleAction(deposit, Deposit.RotateState.PLUS_NINETY)
                        .moduleAction(deposit,Deposit.FlipState.RIGHT)
                        .delay(200)
                        .moduleAction(deposit,Deposit.ExtensionState.DEPOSIT)
                        .executeCode(()->slides.macroRunning=false)
                        .await(()->robot.getPoseEstimate().getX()>xPos)

                        .executeCode(()->slides.macroRunning=true)
                        .moduleAction(deposit, Deposit.ClawState.OPEN)
                        .delay(300)
                        .moduleAction(deposit, Deposit.ExtensionState.TRANSFER_PARTIAL)
                        .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                        .delay(500)
                        .moduleAction(deposit, Deposit.RotateState.ZERO)
                        .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                        .moduleAction(deposit, Deposit.WristState.PARTIAL2)
                        .delay(800)
                        .executeCode(()->slides.setOperationState(Module.OperationState.PRESET))
                        .delay(200)
                        .moduleAction(slides, Slides.SlideState.GROUND)
                        .delay(100)
                        .await(()->slides.currentPosition()<150)
                        //.moduleAction(deposit, Deposit.WristState.PARTIAL2)
                        .moduleAction(deposit, Deposit.ClawState.OPEN)
                        .await(slides::isIdle)
                        .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                        .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                        .executeCode(()->slides.macroRunning=false)
                        .build();
            default:
                return builder.createNew().build();
        }
    }
    public List<Task> runSweepersAuto(double xPos,boolean x, int sweeperIndex)
    {
        return builder.createNew()
                .await(()->robot.getPoseEstimate().getX()<xPos)
                .moduleAction(intake, sweepStates[sweeperIndex])
                .delay(750)
                .moduleAction(intake, sweepStates[sweeperIndex+1])
                .build();
    }
    public List<Task> runSweepersAuto(double xPos,boolean x,boolean y)
    {
        return builder.createNew()
                .await(()->robot.getPoseEstimate().getX()<xPos)
                .moduleAction(intake, Intake.SweeperState.SEVEN_SWEEP)
                .build();
    }

    public List<Task> runSweepersAuto(double xPos)
    {
        return builder.createNew()
                .await(()->robot.getPoseEstimate().getX()<xPos)
                .moduleAction(intake,Intake.SweeperState.TWO_SWEEP)
                .delay(750)
                .moduleAction(intake,Intake.SweeperState.THREE_SWEEP)
                .build();
    }
    public List<Task> transitionDeposit(TeleOpRewrite.DepositState init, TeleOpRewrite.DepositState end)
    {
            switch(init) {
                case EXTENDED:
                    switch(end) {
                        case EXTENDED:

                            return builder.createNew()
                                    .build();
                        case NORMAL:
                            return builder.createNew()
                                    .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                                    .build();
                        case LEFT:
                            return builder.createNew()
                                    .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                                    .moduleAction(deposit,Deposit.FlipState.DEPOSIT)
                                    .delay(500)
                                    .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                                    .moduleAction(deposit, Deposit.RotateState.PLUS_NINETY)
                                    .moduleAction(deposit,Deposit.FlipState.LEFT)
                                    .delay(200)
                                    .moduleAction(deposit,Deposit.ExtensionState.DEPOSIT)
                                    .build();

                        case RIGHT:
                            return builder.createNew()
                                    .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                                    .moduleAction(deposit,Deposit.FlipState.DEPOSIT)
                                    .delay(500)
                                    .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                                    .moduleAction(deposit, Deposit.RotateState.PLUS_NINETY)
                                    .moduleAction(deposit,Deposit.FlipState.RIGHT)
                                    .delay(200)
                                    .moduleAction(deposit,Deposit.ExtensionState.DEPOSIT)
                                    .build();
                    }
                case RIGHT:
                    switch(end) {
                        case EXTENDED:
                            return builder.createNew()
                                    .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                                    .moduleAction(deposit,Deposit.FlipState.DEPOSIT)
                                    .delay(1000)
                                    .moduleAction(deposit, Deposit.WristState.HOVER)
                                    .moduleAction(deposit,Deposit.FlipState.DOWN)
                                    .moduleAction(deposit,Deposit.ExtensionState.DEPOSIT)
                                    .build();
                        case NORMAL:
                            return builder.createNew()
                                    .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                                    .moduleAction(deposit,Deposit.FlipState.DEPOSIT)
                                    .delay(1000)
                                    .moduleAction(deposit, Deposit.WristState.HOVER)
                                    .moduleAction(deposit,Deposit.FlipState.DOWN)
                                    .build();
                        case LEFT:
                            return builder.createNew()
                                    .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                                    .moduleAction(deposit,Deposit.FlipState.DEPOSIT)
                                    .delay(500)
                                    .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                                    .moduleAction(deposit, Deposit.RotateState.PLUS_NINETY)
                                    .moduleAction(deposit,Deposit.FlipState.LEFT)
                                    .delay(200)
                                    .moduleAction(deposit,Deposit.ExtensionState.DEPOSIT)
                                    .build();

                        case RIGHT:

                            return builder.createNew()
                                    .build();
                    }
                case LEFT:
                    switch(end) {
                        case EXTENDED:
                            return builder.createNew()
                                    .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                                    .moduleAction(deposit,Deposit.FlipState.DEPOSIT)
                                    .delay(1000)
                                    .moduleAction(deposit, Deposit.WristState.HOVER)
                                    .moduleAction(deposit,Deposit.FlipState.DOWN)
                                    .moduleAction(deposit,Deposit.ExtensionState.DEPOSIT)
                                    .build();
                        case NORMAL:
                            return builder.createNew()
                                    .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                                    .moduleAction(deposit,Deposit.FlipState.DEPOSIT)
                                    .delay(1000)
                                    .moduleAction(deposit, Deposit.WristState.HOVER)
                                    .moduleAction(deposit,Deposit.FlipState.DOWN)
                                    .build();
                        case LEFT:

                            return builder.createNew()
                                    .build();
                        case RIGHT:

                            return builder.createNew()
                                    .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                                    .moduleAction(deposit,Deposit.FlipState.DEPOSIT)
                                    .delay(500)
                                    .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                                    .moduleAction(deposit, Deposit.RotateState.PLUS_NINETY)
                                    .moduleAction(deposit,Deposit.FlipState.RIGHT)
                                    .delay(200)
                                    .moduleAction(deposit,Deposit.ExtensionState.DEPOSIT)
                                    .build();
                    }
                case NORMAL:
                    switch(end) {
                    case EXTENDED:

                        return builder.createNew()

                                .moduleAction(deposit, Deposit.ExtensionState.DEPOSIT)
                                .build();
                        case NORMAL:
                        return builder.createNew()
                                .build();
                    case LEFT:
                        return builder.createNew()
                                .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                                .moduleAction(deposit,Deposit.FlipState.DEPOSIT)
                                .delay(500)
                                .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                                .moduleAction(deposit, Deposit.RotateState.PLUS_NINETY)
                                .moduleAction(deposit,Deposit.FlipState.LEFT)
                                .delay(200)
                                .moduleAction(deposit,Deposit.ExtensionState.DEPOSIT)
                                .build();

                    case RIGHT:
                        return builder.createNew()
                                .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                                .moduleAction(deposit,Deposit.FlipState.DEPOSIT)
                                .delay(500)
                                .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                                .moduleAction(deposit, Deposit.RotateState.PLUS_NINETY)
                                .moduleAction(deposit,Deposit.FlipState.RIGHT)
                                .delay(200)
                                .moduleAction(deposit,Deposit.ExtensionState.DEPOSIT)
                                .build();

                }
            }
        return builder.createNew()
                .build();
    }
    public List<Task> slidesOnly(Slides.SlideState row, TeleOpRewrite.DepositState state)
    {
        if(slides.getState()==Slides.SlideState.GROUND)
        {
                switch(state) {
                    case EXTENDED:
                        return builder.createNew()
                                .executeCode(()->slides.macroRunning=true)
                                .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                                .delay(1000)
                                .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                                .delay(150)
                                .moduleAction(deposit, Deposit.FlipState.DOWN)
                                .delay(300)
                                .moduleAction(slides, row)
                                .moduleAction(deposit, Deposit.WristState.HOVER)
                                .delay(400)
                                //.await(()->slides.getStatus()==Module.Status.IDLE)
                                .moduleAction(deposit, Deposit.RotateState.PLUS_NINETY)
                                .delay(200)
                                .moduleAction(deposit,Deposit.ExtensionState.DEPOSIT)
                                .executeCode(()->slides.macroRunning=false)
                                .build();
                    case RIGHT:
                        return builder.createNew()
                                .executeCode(()->slides.macroRunning=true)
                                .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                                .delay(1000)
                                .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                                .delay(150)
                                .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                                .delay(300)
                                .moduleAction(slides, row)
                                .moduleAction(deposit, Deposit.WristState.HOVER)
                                .delay(400)
                                //.await(()->slides.getStatus()==Module.Status.IDLE)
                                .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                                .moduleAction(deposit, Deposit.RotateState.PLUS_NINETY)
                                .moduleAction(deposit,Deposit.FlipState.RIGHT)
                                .delay(200)
                                .moduleAction(deposit,Deposit.ExtensionState.DEPOSIT)
                                .executeCode(()->slides.macroRunning=false)
                                .build();
                    case LEFT:
                        return builder.createNew()
                                .executeCode(()->slides.macroRunning=true)
                                .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                                .delay(1000)
                                .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                                .delay(150)
                                .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                                .delay(300)
                                .moduleAction(slides, row)
                                .moduleAction(deposit, Deposit.WristState.HOVER)
                                .delay(400)
                                //.await(()->slides.getStatus()==Module.Status.IDLE)
                                .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                                .moduleAction(deposit, Deposit.RotateState.PLUS_NINETY)
                                .moduleAction(deposit,Deposit.FlipState.LEFT)
                                .delay(200)
                                .moduleAction(deposit,Deposit.ExtensionState.DEPOSIT)
                                .executeCode(()->slides.macroRunning=false)
                                .build();
                    case NORMAL:
                        return builder.createNew()
                                .executeCode(()->slides.macroRunning=true)
                                .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                                .delay(1000)
                                .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                                .delay(150)
                                .moduleAction(deposit, Deposit.FlipState.DOWN)
                                .delay(300)
                                .moduleAction(slides, row)
                                .moduleAction(deposit, Deposit.WristState.HOVER)
                                .delay(400)
                                //.await(()->slides.getStatus()==Module.Status.IDLE)
                                .delay(200)
                                .executeCode(()->slides.macroRunning=false)
                                .build();
                }
            }

        return builder.createNew()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(slides, row)
                .awaitPreviousModuleActionCompletion()
                .executeCode(()->slides.macroRunning=false)
                .build();
    }

    public List<Task> slidesOnly(Slides.SlideState row)
    {
        if(slides.getState()==Slides.SlideState.GROUND)
        {
            return builder.createNew()
                    .executeCode(()->slides.macroRunning=true)
                    .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                    .delay(1000)
                    .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                    .delay(150)
                    .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                    .delay(300)
                    .moduleAction(slides, row)
                    .moduleAction(deposit, Deposit.WristState.HOVER)
                    .delay(400)
                    //.await(()->slides.getStatus()==Module.Status.IDLE)
                    .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                    .moduleAction(deposit, Deposit.RotateState.PLUS_NINETY)
                    .moduleAction(deposit,Deposit.FlipState.LEFT)
                    .delay(200)
                    .moduleAction(deposit,Deposit.ExtensionState.DEPOSIT)
                    .executeCode(()->slides.macroRunning=false)
                    .build();

        }

        return builder.createNew()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(slides, row)
                .awaitPreviousModuleActionCompletion()
                .executeCode(()->slides.macroRunning=false)
                .build();
    }

    public List<Task> slidesOnly(Slides.SlideState row, boolean x)
    {
        if(slides.getState()==Slides.SlideState.GROUND)
        {
            return builder.createNew()
                    .executeCode(()->slides.macroRunning=true)
                    .moduleAction(deposit, Deposit.ClawState.CLOSED_EDGE)
                    .delay(1000)
                    .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                    .delay(150)
                    .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                    .delay(250)
                    .moduleAction(slides, row)
                    //.delay(50)
                    .delay(500)
                    .moduleAction(deposit, Deposit.WristState.HOVER)
                    .delay(200)
                    //.await(()->slides.getStatus()==Module.Status.IDLE)
                    .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                    .moduleAction(deposit,Deposit.FlipState.LEFT)
                    .moduleAction(deposit, Deposit.RotateState.PLUS_NINETY)
                    .delay(200)
                    .moduleAction(deposit,Deposit.ExtensionState.DEPOSIT)
                    .executeCode(()->slides.macroRunning=false)
                    .build();

        }

        return builder.createNew()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(slides, row)
                .awaitPreviousModuleActionCompletion()
                .executeCode(()->slides.macroRunning=false)
                .build();
    }
    public List<Task> updatedSlidesMacro(Slides.SlideState row)
    {
        if(slides.getState()==Slides.SlideState.GROUND)
        {
            return builder.createNew()
                    .executeCode(()->slides.macroRunning=true)
                    .moduleAction(slides, Slides.SlideState.HALF)
                    .await(slides::isIdle)
                    .delay(400)
                    .moduleAction(deposit, Deposit.ClawState.PRIMED)
                    .moduleAction(deposit, Deposit.WristState.TRANSFER)
                    .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                    .delay(1000)
                    .moduleAction(slides, Slides.SlideState.GROUND)
                    .await(slides::isIdle)
                    .delay(100)
                    .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                    .delay(1000)
                    .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                    .delay(150)
                    .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                    .delay(50)
                    .moduleAction(slides, row)
                    //.delay(50)
                    .delay(300)
                    .moduleAction(deposit, Deposit.WristState.HOVER)
                    .delay(200)
                    //.await(()->slides.getStatus()==Module.Status.IDLE)
                    .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                    .executeCode(()->slides.macroRunning=false)
                    .build();
        }

        return builder.createNew()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(slides, row)
                .awaitPreviousModuleActionCompletion()
                .executeCode(()->slides.macroRunning=false)
                .build();
    }
    public List<Task> raiseSlides(Slides.SlideState row)
    {
        if(!Context.isTele&&slides.getState()==Slides.SlideState.GROUND){
            return builder.createNew()
                    .moduleAction(deposit, Deposit.WristState.TRANSFER)
                    .delay(50)
                    .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                    .delay(500)
                    .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                    .delay(200)
                    .await(()->robot.getPoseEstimate().getX()>-10)
                    .executeCode(()->slides.macroRunning=true)
                    .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                    .delay(150)
                    .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                    .delay(50)
                    .moduleAction(slides, row)
                    //.delay(50)
                    .delay(300)
                    .moduleAction(deposit, Deposit.WristState.HOVER)
                    .delay(200)
                    //.await(()->slides.getStatus()==Module.Status.IDLE)
                    .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                    .executeCode(()->slides.macroRunning=false)
                    .build();
        }
        else if(slides.getState()==Slides.SlideState.GROUND)
        {
            return builder.createNew()
                    .moduleAction(deposit, Deposit.WristState.TRANSFER)
                    .delay(50)
                    .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                    .delay(500)
                    .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                    .delay(2000)
                    .executeCode(()->slides.macroRunning=true)
                    .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                    .delay(150)
                    .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                    .delay(50)
                    .moduleAction(slides, row)
                    //.delay(50)
                    .delay(300)
                    .moduleAction(deposit, Deposit.WristState.HOVER)
                    .delay(200)
                    //.await(()->slides.getStatus()==Module.Status.IDLE)
                    .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                    .executeCode(()->slides.macroRunning=false)
                    .build();
        }

        return builder.createNew()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(slides, row)
                .awaitPreviousModuleActionCompletion()
                .executeCode(()->slides.macroRunning=false)
                .build();
    }

    public List<Task> lowerSlides()
    {

        return builder.createNew()
                //.executeCode(()->claw.setPosition(clawOpen))
                //.delay(500)
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.WristState.HOVER)
                .moduleAction(deposit, Deposit.RotateState.ZERO)
                .delay(300)
                .moduleAction(slides, Slides.SlideState.GROUND)
                .delay(100)
                .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                .await(()->slides.currentPosition()<100)
                .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                .moduleAction(deposit, Deposit.ClawState.PRIMED)
                .await(()->slides.getStatus()==Module.Status.IDLE)
                .executeCode(()->slides.macroRunning=false)
                .build();
    }

    public List<Task> scorePixels()
    {
        return builder.createNew()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .delay(300)
                .moduleAction(deposit, Deposit.ExtensionState.TRANSFER_PARTIAL)
                .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                .delay(500)
                .moduleAction(deposit, Deposit.RotateState.ZERO)
                .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                .moduleAction(deposit, Deposit.WristState.PARTIAL2)
                .delay(800)
                .executeCode(()->slides.setOperationState(Module.OperationState.PRESET))
                .delay(200)
                .moduleAction(slides, Slides.SlideState.GROUND)
                .delay(100)
                .await(()->slides.currentPosition()<150)
                //.moduleAction(deposit, Deposit.WristState.PARTIAL2)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .await(slides::isIdle)
                .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                .executeCode(()->slides.macroRunning=false)
                .build();
    }

    public List<Task> scorePixelDelay()
    {
        /*
        return builder.createNew()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .delay(300)
                .addTaskList(lowerSlides())
                .build();*/
        return builder.createNew()
                //.executeCode(()->claw.setPosition(clawOpen))
                //.delay(500)
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .delay(200)
                .moduleAction(deposit, Deposit.RotateState.ZERO)
                .delay(500)
                .moduleAction(deposit, Deposit.WristState.HOVER)
                .delay(300)
                .moduleAction(slides, Slides.SlideState.GROUND)
                .delay(100)
                .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                .await(()->slides.currentPosition()<100)
                .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .await(slides::isIdle)
                .executeCode(()->slides.macroRunning=false)
                .build();
    }
    public List<Task> raiseIntake()
    {
        return builder.createNew()
                .moduleAction(intake, Intake.PositionState.MID)
                .moduleAction(intake, Intake.SweeperState.ZERO)
                .moduleAction(intake, Intake.PowerState.OFF)
                .moduleAction(intake, Intake.ConveyorState.OFF)
                .build();
    }

    public List<Task> lowerIntake()
    {
        return builder.createNew()
                .moduleAction(intake, Intake.PositionState.DOWN)
                .moduleAction(intake, Intake.PowerState.INTAKE)
                .moduleAction(intake, Intake.ConveyorState.INTAKE)
                .build();
    }

    public List<Task> grabAndHold()
    {
        return builder.createNew()
                .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                .delay(300)
                .moduleAction(deposit, Deposit.WristState.HOVER)
                .build();
    }

    public List<Task> autoRaiseSlides(double xThreshold, Slides.SlideState level, Deposit.RotateState rotation)
    {
        return builder.createNew()
                .await(()->robot.getPoseEstimate().getX()>xThreshold)
                .addTaskList(raiseSlides(level))
                .moduleAction(deposit, rotation)
                .build();
    }

}

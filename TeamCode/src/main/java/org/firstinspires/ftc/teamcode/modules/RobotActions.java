package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.opmodes.tele.TeleOpRewrite;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.Builder;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.enums.Paths;

import java.util.List;
@Config
public class RobotActions
{
    static RobotActions robotActions;
    Robot robot;
    Slides slides;
    Deposit deposit;
    Intake intake;

    public static RobotActions getInstance()
    {
        if (robotActions == null) {
            robotActions = new RobotActions(Robot.getInstance());
        }
        return robotActions;
    }
    public static void deleteInstance()
    {
        robotActions=null;
    }

    public RobotActions(Robot robot)
    {
        this.robot=robot;

        deposit=robot.deposit;
        slides=robot.slides;
        intake=robot.intake;
    }

    public List<Task> lowerIntake(double drop_x , double sweep_x,int cycle)
    {
        switch(cycle) {
            case 0:
                return Builder.create()
                        .addTaskList(sweepySweep(drop_x, sweep_x, Intake.SweeperState.TWO_SWEEP, Intake.SweeperState.THREE_SWEEP))
                        .build();
            case 1:
                return Builder.create()
                        .addTaskList(sweepySweep(drop_x, sweep_x, Intake.SweeperState.FOUR_SWEEP, Intake.SweeperState.FIVE_SWEEP))
                        .build();

            case 2:
                return Builder.create()
                        .addTaskList(sweepySweep(drop_x, sweep_x, Intake.SweeperState.SIX_SWEEP, Intake.SweeperState.SEVEN_SWEEP))
                        .build();
        }
        return Builder.create().build();
    }
    private List<Task> sweepySweep(double drop_x, double sweep_x, Intake.SweeperState sweep1, Intake.SweeperState sweep2)
    {
        return Builder.create()
                .await(() -> robot.getPoseEstimate().getX() < drop_x)
                .moduleAction(intake, Intake.PositionState.DOWN)
                .moduleAction(intake, Intake.PowerState.INTAKE)
                .moduleAction(intake, Intake.ConveyorState.INTAKE)
                .await(() -> robot.getPoseEstimate().getX() < sweep_x)
                .moduleAction(intake, sweep1)
                .delay(750)
                .moduleAction(intake, sweep2)
                .build();
    }

    public List<Task> deployPurple(double yLeft, double yMid, double yRight){
        switch(Context.dice) {

            case MIDDLE:
                return Builder.create()
                        .addTaskList(deployPurple(yMid))
                        .build();
            case RIGHT:
                return Builder.create()
                        .addTaskList(deployPurple(yRight))
                        .build();
            default:
                return Builder.create()
                        .addTaskList(deployPurple(yLeft))
                        .build();
        }
    }
    public List<Task> deployPurple(double y)
    {
        return Builder.create()
                .delay(500)
                .moduleAction(intake, Intake.PositionState.DOWN)
                .await(() -> Math.abs(robot.getPoseEstimate().getY()) < y)
                .moduleAction(intake, Intake.SweeperState.ONE_SWEEP)
                .delay(500)
                .moduleAction(intake, Intake.PositionState.MID)
                .build();
    }

    public List<Task> yellowDrop(double xPos){
        return Builder.create()
                .addTaskList(slidesOnly(Slides.SlideState.AUTO_LOW))
                .executeCode(()->slides.macroRunning=false)
                .await(()->robot.getPoseEstimate().getX()>xPos||(robot.k != Paths.Score_Spike||robot.k != Paths.Score_First&&robot.k != Paths.Score_Second&&robot.k != Paths.Score_Third))
                .delay(400)
                .addTaskList(scorePixels())
                .build();
    }

    public List<Task> yellowDrop(double xPos, double yPos){
        return Builder.create()
                .addTaskList(slidesOnly(Slides.SlideState.AUTO_LOW))
                .await(()->robot.getPoseEstimate().getX()>xPos||(robot.k != Paths.Score_Spike||robot.k != Paths.Score_First&&robot.k != Paths.Score_Second&&robot.k != Paths.Score_Third))
                .awaitDtYWithin(yPos, 5)
                .delay(400)
                .addTaskList(scorePixels())
                .build();
    }
    public List<Task> scorePixels(double xPos, TeleOpRewrite.DepositState state){
        switch(state) {
            case LEFT:
                return Builder.create()
                        .await(() -> robot.getPoseEstimate().getX() > 15)
                        .executeCode(() -> RobotLog.e("s"))
                        .addTaskList(slidesSide(Slides.SlideState.AUTO_TWO, Deposit.FlipState.LEFT))
                        .await(() -> robot.getPoseEstimate().getX() > xPos)
                        .addTaskList(scorePixels())
                        .build();
            case RIGHT:
                return Builder.create()
                        .await(()->robot.getPoseEstimate().getX()>15)
                        .executeCode(()-> RobotLog.e("s"))
                        .addTaskList(slidesSide(Slides.SlideState.AUTO_TWO, Deposit.FlipState.RIGHT))
                        .await(()->robot.getPoseEstimate().getX()>xPos)
                        .addTaskList(scorePixels())
                        .build();
            default:
                return Builder.create().build();
        }
    }
    public List<Task> transitionDeposit(TeleOpRewrite.DepositState init, TeleOpRewrite.DepositState end)
    {
            switch(init) {
                case EXTENDED:
                    switch(end) {
                        case EXTENDED:
                            return Builder.create()
                                    .build();
                        case NORMAL:
                            return Builder.create()
                                    .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                                    .build();
                        case LEFT:
                            return Builder.create()
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
                            return Builder.create()
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
                            return Builder.create()
                                    .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                                    .moduleAction(deposit,Deposit.FlipState.DEPOSIT)
                                    .delay(1000)
                                    .moduleAction(deposit, Deposit.WristState.HOVER)
                                    .moduleAction(deposit,Deposit.FlipState.DOWN)
                                    .moduleAction(deposit,Deposit.ExtensionState.DEPOSIT)
                                    .build();
                        case NORMAL:
                            return Builder.create()
                                    .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                                    .moduleAction(deposit,Deposit.FlipState.DEPOSIT)
                                    .delay(1000)
                                    .moduleAction(deposit, Deposit.WristState.HOVER)
                                    .moduleAction(deposit,Deposit.FlipState.DOWN)
                                    .build();
                        case LEFT:
                            return Builder.create()
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

                            return Builder.create()
                                    .build();
                    }
                case LEFT:
                    switch(end) {
                        case EXTENDED:
                            return Builder.create()
                                    .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                                    .moduleAction(deposit,Deposit.FlipState.DEPOSIT)
                                    .delay(1000)
                                    .moduleAction(deposit, Deposit.WristState.HOVER)
                                    .moduleAction(deposit,Deposit.FlipState.DOWN)
                                    .moduleAction(deposit,Deposit.ExtensionState.DEPOSIT)
                                    .build();
                        case NORMAL:
                            return Builder.create()
                                    .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                                    .moduleAction(deposit,Deposit.FlipState.DEPOSIT)
                                    .delay(1000)
                                    .moduleAction(deposit, Deposit.WristState.HOVER)
                                    .moduleAction(deposit,Deposit.FlipState.DOWN)
                                    .build();
                        case LEFT:

                            return Builder.create()
                                    .build();
                        case RIGHT:

                            return Builder.create()
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

                        return Builder.create()

                                .moduleAction(deposit, Deposit.ExtensionState.DEPOSIT)
                                .build();
                        case NORMAL:
                        return Builder.create()
                                .build();
                    case LEFT:
                        return Builder.create()
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
                        return Builder.create()
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
        return Builder.create()
                .build();
    }
    public List<Task> slidesOnly(Slides.SlideState row, TeleOpRewrite.DepositState state)
    {
        if(slides.getState()==Slides.SlideState.GROUND || slides.getState()==Slides.SlideState.GROUND_UNTIL_LIMIT)
        {
                switch(state) {
                    case EXTENDED:
                        return Builder.create()
                                .executeCode(()->slides.macroRunning=true)
                                .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                                .delay(500)
                                .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                                .delay(150)
                                .moduleAction(deposit, Deposit.FlipState.DOWN)
                                .delay(100)
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
                        return Builder.create().
                                addTaskList(slidesSide(row, Deposit.FlipState.RIGHT))
                                .build();
                    case LEFT:
                        return Builder.create().
                                addTaskList(slidesSide(row, Deposit.FlipState.LEFT))
                                .build();
                    case NORMAL:
                        return slidesOnly(row);
                }
            }

        return Builder.create()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(slides, row)
                .awaitPreviousModuleActionCompletion()
                .executeCode(()->slides.macroRunning=false)
                .build();
    }

    private List<Task> slidesSide(Slides.SlideState row, Deposit.FlipState flip)
    {
        return Builder.create()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                .delay(1000)
                .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                .delay(150)
                .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                .delay(100)
                .moduleAction(slides, row)
                .moduleAction(deposit, Deposit.WristState.HOVER)
                .delay(400)
                //.await(()->slides.getStatus()==Module.Status.IDLE)
                .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                .moduleAction(deposit, flip)
                .moduleAction(deposit, Deposit.RotateState.PLUS_NINETY)
                .delay(200)
                .moduleAction(deposit,Deposit.ExtensionState.DEPOSIT)
                .executeCode(()->slides.macroRunning=false)
                .build();
    }

    public List<Task> slidesOnly(Slides.SlideState row)
    {
        if(slides.getState()==Slides.SlideState.GROUND|| slides.getState()==Slides.SlideState.GROUND_UNTIL_LIMIT)
        {
            return Builder.create()
                    .executeCode(()->slides.macroRunning=true)
                    .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                    .delay(500)
                    .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                    .delay(150)
                    .moduleAction(deposit, Deposit.FlipState.DOWN)
                    .delay(100)
                    .moduleAction(slides, row)
                    .moduleAction(deposit, Deposit.WristState.HOVER)
                    .moduleAction(deposit, Deposit.RotateState.PLUS_NINETY)
                    .delay(400)
                    //.await(()->slides.getStatus()==Module.Status.IDLE)
                    .delay(200)
                    .executeCode(()->slides.macroRunning=false)
                    .build();
        }

        return Builder.create()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(slides, row)
                .awaitPreviousModuleActionCompletion()
                .executeCode(()->slides.macroRunning=false)
                .build();
    }

    public List<Task> scorePixels()
    {
        return Builder.create()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .delay(300)
                .moduleAction(deposit, Deposit.ExtensionState.TRANSFER_PARTIAL)
                .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                .delay(500)
                .moduleAction(deposit, Deposit.RotateState.ZERO)
                .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                .moduleAction(deposit, Deposit.WristState.PARTIAL)
                .delay(300)
                .executeCode(()->slides.setOperationState(Module.OperationState.PRESET))
//                .delay(200)
                .moduleAction(slides, Slides.SlideState.GROUND)
                .delay(100)
                .await(()->slides.currentPosition()<180)
                //.moduleAction(deposit, Deposit.WristState.PARTIAL2)
                .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                .await(slides::isIdle)
                .moduleAction(slides, Slides.SlideState.GROUND_UNTIL_LIMIT)
                .await(slides::isIdle)
                .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                .executeCode(()->slides.macroRunning=false)
                .build();
    }


    public List<Task> raiseIntake()
    {
        return Builder.create()
                .moduleAction(intake, Intake.PositionState.MID)
                .moduleAction(intake, Intake.SweeperState.ZERO)
                .moduleAction(intake, Intake.PowerState.OFF)
                .moduleAction(intake, Intake.ConveyorState.OFF)
                .build();
    }

    public List<Task> lowerIntake()
    {
        return Builder.create()
                .moduleAction(intake, Intake.PositionState.DOWN)
                .moduleAction(intake, Intake.PowerState.INTAKE)
                .moduleAction(intake, Intake.ConveyorState.INTAKE)
                .build();
    }



    //OLD ONES (FOR OLD AUTOS)
    public List<Task> OLD_autoRaiseSlides(Slides.SlideState row)
    {
        if(slides.getState()==Slides.SlideState.GROUND)
        {
            return Builder.create()
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

        return Builder.create()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(slides, row)
                .awaitPreviousModuleActionCompletion()
                .executeCode(()->slides.macroRunning=false)
                .build();
    }

    public List<Task> OLD_runSweepersAuto(double xPos, boolean x)
    {
        return Builder.create()
                .await(()->robot.getPoseEstimate().getX()<xPos)
                .moduleAction(intake, Intake.SweeperState.FOUR_SWEEP)
                .delay(750)
                .moduleAction(intake, Intake.SweeperState.FIVE_SWEEP)
                .build();
    }

    public List<Task> OLD_runSweepersAuto(double xPos, boolean x, boolean y)
    {
        return Builder.create()
                .await(()->robot.getPoseEstimate().getX()<xPos)
                .moduleAction(intake, Intake.SweeperState.SEVEN_SWEEP)
                .build();
    }

    public List<Task> OLD_runSweepersAuto(double xPos)
    {
        return Builder.create()
                .await(()->robot.getPoseEstimate().getX()<xPos)
                .moduleAction(intake,Intake.SweeperState.TWO_SWEEP)
                .delay(750)
                .moduleAction(intake,Intake.SweeperState.THREE_SWEEP)
                .build();
    }

    public List<Task> OLD_raiseSlides(Slides.SlideState row)
    {
        if(!Context.isTele&&slides.getState()==Slides.SlideState.GROUND){
            return Builder.create()
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
            return Builder.create()
                    .moduleAction(deposit, Deposit.WristState.TRANSFER)
                    .delay(50)
                    .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                    .delay(500)
                    .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                    .delay(2000)
                    .executeCode(()->slides.macroRunning=true)
                    .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                    .delay(150)
                    .moduleAction(slides, row)
                    .delay(100)
                    .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                    //.delay(50)
                    .delay(300)
                    .moduleAction(deposit, Deposit.WristState.HOVER)
                    .delay(200)
                    //.await(()->slides.getStatus()==Module.Status.IDLE)
                    .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                    .executeCode(()->slides.macroRunning=false)
                    .build();
        }

        return Builder.create()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(slides, row)
                .awaitPreviousModuleActionCompletion()
                .executeCode(()->slides.macroRunning=false)
                .build();
    }

    public List<Task> OLD_scorePixelDelay()
    {
        /*
        return Builder.create()()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .delay(300)
                .addTaskList(lowerSlides())
                .build();*/
        return Builder.create()
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
}

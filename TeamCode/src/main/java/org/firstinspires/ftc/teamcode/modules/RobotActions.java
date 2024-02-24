package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.ModuleState;
import org.firstinspires.ftc.teamcode.opmodes.tele.TeleOpRewrite;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.task_scheduler.Builder;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.enums.Dice;
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
        Intake.SweeperState sweep1 = cycle==0?Intake.SweeperState.ONE_SWEEP: cycle==1?Intake.SweeperState.THREE_SWEEP: Intake.SweeperState.FIVE_SWEEP;
        Intake.SweeperState sweep2 = cycle==0?Intake.SweeperState.TWO_SWEEP: cycle==1?Intake.SweeperState.FOUR_SWEEP: Intake.SweeperState.SIX_SWEEP;

        return Builder.create()
            .await(() -> robot.getPoseEstimate().getX() < drop_x)
            .moduleAction(intake, Intake.PositionState.DOWN)
            .moduleAction(intake, Intake.PowerState.INTAKE)
            .moduleAction(intake, Intake.ConveyorState.INTAKE)
            .await(() -> robot.getPoseEstimate().getX() < sweep_x || (robot.k!=Paths.Return_to_Stack && robot.k!=Paths.Go_To_Stack))
//            .awaitDtXWithin(-56.5, 4)
            .moduleAction(intake, sweep1)
            .moduleAction(intake, sweep2)
            .build();
    }
    public List<Task> lowerIntake(double drop_x , double sweep_x,int cycle, boolean x)
    {
        Intake.SweeperState sweep1 = cycle==0?Intake.SweeperState.ONE_SWEEP: cycle==1?Intake.SweeperState.THREE_SWEEP: Intake.SweeperState.FIVE_SWEEP;
        Intake.SweeperState sweep2 = cycle==0?Intake.SweeperState.TWO_SWEEP: cycle==1?Intake.SweeperState.FOUR_SWEEP: Intake.SweeperState.SIX_SWEEP;

        return Builder.create()
                .await(() -> robot.getPoseEstimate().getX() < drop_x)
                .addTaskList(quickReset())
                .moduleAction(intake, Intake.PositionState.DOWN)
                .moduleAction(intake, Intake.PowerState.INTAKE)
                .moduleAction(intake, Intake.ConveyorState.INTAKE)
                .await(() -> robot.getPoseEstimate().getX() < sweep_x || (robot.k!=Paths.Return_to_Stack && robot.k!=Paths.Go_To_Stack))
//            .awaitDtXWithin(-56.5, 4)
                .moduleAction(intake, sweep1)
                .delay(750)
                .moduleAction(intake, sweep2)
                .build();
    }

    public List<Task> deployPurple(double yLeft, double yMid, double yRight)
    {
        double y = Context.dice == Dice.MIDDLE?yMid: Context.dice == Dice.RIGHT?yRight: yLeft;

        return Builder.create()
                .delay(50)
                .moduleAction(intake, Intake.PositionState.AUTO)
                .delay(50)
                .await(() -> ((Math.abs(robot.getPoseEstimate().getHeading() - robot.get(0).getTrajectory().end().getHeading())<Math.toRadians(8))||robot.k!=Paths.Purple) &&
                        (Math.abs(robot.getPoseEstimate().getY() - robot.get(0).getTrajectory().end().getY())<2))
                .moduleAction(intake, Intake.PositionState.DOWN)
                .delay(300)
                .moduleAction(intake, Intake.SweeperState.ZERO)
                .delay(200)
                .moduleAction(intake, Intake.PositionState.MID)
                .build();
    }

    public List<Task> deployPurple(double yLeft, double yMid, double yRight, long waitTime)
    {
        double y = Context.dice == Dice.MIDDLE?yMid: Context.dice == Dice.RIGHT?yRight: yLeft;

        return Builder.create()
                .delay(waitTime)
                .moduleAction(intake, Intake.PositionState.DOWN)
                .delay(500)
                .moduleAction(intake, Intake.SweeperState.ONE_SWEEP)
                .delay(500)
                .moduleAction(intake, Intake.PositionState.MID)
                .build();
    }

    public List<Task> yellowDrop(double xPos, double startSlides){
        return Builder.create()
                .awaitDtXWithin(startSlides,4)
                .addTaskList(slidesOnlyAutonomousProgrammingVersionForAutomatedControl(Slides.SlideState.AUTO_TWO))
                .executeCode(()->slides.macroRunning=false)
                .await(()->robot.getPoseEstimate().getX()>xPos||(robot.k != Paths.Score_First&&robot.k != Paths.Score_Second&&robot.k != Paths.Score_Third))
                .delay(1200)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .delay(600)
                .addTaskList(resetOnly())
                .build();
    }

    public List<Task> yellowDrop(double xPos, double startSlides, Slides.SlideState height){
        return Builder.create()
                .awaitDtXWithin(startSlides,4)
                .addTaskList(slidesOnlyAutonomousProgrammingVersionForAutomatedControl(height))
                .executeCode(()->slides.macroRunning=false)
                .await(()->robot.getPoseEstimate().getX()>xPos||(robot.k != Paths.Score_First&&robot.k != Paths.Score_Second&&robot.k != Paths.Score_Third))
                .delay(1400)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .delay(300)
                .moduleAction(deposit, Deposit.RotateState.ZERO)
                .addTaskList(resetOnly())
                .build();
    }
    public List<Task> yellowDropTruss(double xPos, double startSlides, Slides.SlideState height){
        return Builder.create()
                .awaitDtXWithin(startSlides,4)
                .addTaskList(slidesOnlyAutonomousProgrammingVersionForAutomatedControl(height,true))
                .executeCode(()->slides.macroRunning=false)
                .await(()->robot.getPoseEstimate().getX()>xPos||(robot.k != Paths.Score_First&&robot.k != Paths.Score_Second&&robot.k != Paths.Score_Third))
                .delay(1400)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .delay(300)
                .moduleAction(deposit, Deposit.RotateState.ZERO)
                .addTaskList(resetOnly())
                .build();
    }

    public List<Task> yellowDrop(double xPos, double startSlides, boolean first){
        return Builder.create()
                .awaitDtXWithin(startSlides,4)
                .addTaskList(slidesOnlyAutonomousProgrammingVersionForAutomatedControl(Slides.SlideState.AUTO_LOW))
                .executeCode(()->slides.macroRunning=false)
                .await(()->robot.getPoseEstimate().getX()>xPos||(robot.k != Paths.Score_First&&robot.k != Paths.Score_Second&&robot.k != Paths.Score_Third))
                .delay(700)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .delay(600)
                .addTaskList(resetOnly())
                .build();
    }

    public List<Task> scorePixels(double xPos, TeleOpRewrite.DepositState state){
        switch(state) {
            case LEFT:
                return Builder.create()
                        .await(() -> robot.getPoseEstimate().getX() > -30)
                        .executeCode(() -> RobotLog.e("s"))
                        .addTaskList(slidesSide(Slides.SlideState.AUTO_TWO, Deposit.FlipState.LEFT))
                        .await(() -> robot.getPoseEstimate().getX() > xPos||(robot.k != Paths.Score_First&&robot.k != Paths.Score_Second&&robot.k != Paths.Score_Third))
                        .delay(300)
                        .addTaskList(scorePixels(state))
                        .build();
            case RIGHT:
                return Builder.create()
                        .await(()->robot.getPoseEstimate().getX()>-40)
                        .executeCode(()-> RobotLog.e("s"))
                        .addTaskList(slidesSide(Slides.SlideState.AUTO_TWO, Deposit.FlipState.RIGHT))
                        .await(()->robot.getPoseEstimate().getX() > xPos||(robot.k != Paths.Score_First&&robot.k != Paths.Score_Second&&robot.k != Paths.Score_Third))
                        .delay(300)
                        .addTaskList(scorePixels(state))
                        .build();
            default:
                return Builder.create().build();
        }
    }
    public List<Task> scorePixels(double xPos, TeleOpRewrite.DepositState state,double xPos2){
        switch(state) {
            case LEFT:
                return Builder.create()
                        .await(() -> robot.getPoseEstimate().getX() > xPos2)
                        .executeCode(() -> RobotLog.e("s"))
                        .addTaskList(slidesSide(Slides.SlideState.AUTO_TWO, Deposit.FlipState.LEFT))
                        .await(() -> robot.getPoseEstimate().getX() > xPos||(robot.k != Paths.Score_First&&robot.k != Paths.Score_Second&&robot.k != Paths.Score_Third))
                        .delay(300)
                        .addTaskList(scorePixels(state))
                        .build();
            case RIGHT:
                return Builder.create()
                        .await(()->robot.getPoseEstimate().getX()>xPos2)
                        .executeCode(()-> RobotLog.e("s"))
                        .addTaskList(slidesSide(Slides.SlideState.AUTO_TWO, Deposit.FlipState.RIGHT))
                        .await(()->robot.getPoseEstimate().getX() > xPos||(robot.k != Paths.Score_First&&robot.k != Paths.Score_Second&&robot.k != Paths.Score_Third))
                        .delay(300)
                        .addTaskList(scorePixels(state))
                        .build();
            default:
                return Builder.create().build();
        }
    }


    public List<Task> scorePixelsFailed(double xPos, TeleOpRewrite.DepositState state){
        switch(state) {
            case LEFT:
                return Builder.create()
                        .delay(2000)
                        .await(() -> robot.getPoseEstimate().getX() > -19)
                        .executeCode(() -> RobotLog.e("s"))
                        .addTaskList(slidesSide(Slides.SlideState.AUTO_TWO, Deposit.FlipState.LEFT))
                        .delay(1500)
                        .addTaskList(scorePixels(state))
                        .delay(1000)
                        .build();
            case RIGHT:
                return Builder.create()
                        .delay(3000)
                        .await(()->robot.getPoseEstimate().getX()>-40)
                        .executeCode(()-> RobotLog.e("s"))
                        .addTaskList(slidesSide(Slides.SlideState.AUTO_TWO, Deposit.FlipState.RIGHT))
                        .delay(2500)
                        .addTaskList(scorePixels(state))
                        .delay(1000)
                        .build();
            default:
                return Builder.create().build();
        }
    }

    public List<Task> scorePixels(TeleOpRewrite.DepositState state)
    {
        boolean depositIsLeft = state==TeleOpRewrite.DepositState.LEFT;
        boolean depositIsRight = state==TeleOpRewrite.DepositState.RIGHT;
        boolean depositIsExtended = state==TeleOpRewrite.DepositState.EXTENDED;
        boolean depositIsNormal = state==TeleOpRewrite.DepositState.NORMAL;

        long delay = deposit.getState(Deposit.FlipState.class) == Deposit.FlipState.LEFT? 1300:
                deposit.getState(Deposit.FlipState.class) == Deposit.FlipState.RIGHT? 800: 50;
        delay += slides.getState(Slides.SlideState.class) == Slides.SlideState.ROW3? 0:
                slides.getState(Slides.SlideState.class) == Slides.SlideState.ROW2? 300: 550;

        TeleOpRewrite.DepositState init = depositIsLeft? TeleOpRewrite.DepositState.LEFT: depositIsRight? TeleOpRewrite.DepositState.RIGHT:
                depositIsExtended? TeleOpRewrite.DepositState.EXTENDED: TeleOpRewrite.DepositState.NORMAL;

        return Builder.create()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .delay(600)
                .addTaskList(transitionDepositModified(init, TeleOpRewrite.DepositState.NORMAL))
                //.delay(depositIsLeft? 1000: 0)
                .moduleAction(deposit, Deposit.ExtensionState.TRANSFER_PARTIAL)
                .moduleAction(deposit, Deposit.RotateState.ZERO)
                .delay(200)
                .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                .moduleAction(deposit, Deposit.WristState.PARTIAL)
                .delay(300)
                .executeCode(()->slides.setOperationState(Module.OperationState.PRESET))
                //.moduleAction(deposit, Deposit.WristState.TELESCOPE)
                .delay(200)
                .moduleAction(slides, Slides.SlideState.GROUND)
                .await(()->slides.currentPosition()<600)
                .moduleAction(deposit, Deposit.WristState.TRANSFER)
                //.moduleAction(deposit, Deposit.WristState.PARTIAL2
                //.await(slides::isIdle)
                //.moduleAction(slides, Slides.SlideState.GROUND_UNTIL_LIMIT)
                //.await(slides::isIdle)
                .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                .executeCode(()->slides.macroRunning=false)
                .build();
    }
    public List<Task> scorePixels(double xPos, TeleOpRewrite.DepositState state, boolean x){
        switch(state) {
            case LEFT:
                return Builder.create()
                        .await(() -> robot.getPoseEstimate().getX() > -19)
                        .executeCode(() -> RobotLog.e("s"))
                        .addTaskList(slidesSide(Slides.SlideState.ROW1, Deposit.FlipState.LEFT))
                        .await(() -> robot.getPoseEstimate().getX() > xPos||(robot.k != Paths.Score_First&&robot.k != Paths.Score_Second&&robot.k != Paths.Score_Third))
                        .delay(300)
                        .addTaskList(scorePixels(state))
                        .build();
            case RIGHT:
                return Builder.create()
                        .await(()->robot.getPoseEstimate().getX()>-40)
                        .executeCode(()-> RobotLog.e("s"))
                        .addTaskList(slidesSide(Slides.SlideState.ROW1, Deposit.FlipState.RIGHT))
                        .await(()->robot.getPoseEstimate().getX() > xPos||(robot.k != Paths.Score_First&&robot.k != Paths.Score_Second&&robot.k != Paths.Score_Third))
                        .delay(300)
                        .addTaskList(scorePixels(state))
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
                                    .delay(800)
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
                                    .delay(800)
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
                                    .delay(800)
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
                                    .delay(800)
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
                                .delay(800)
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
                                .delay(800)
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

    public List<Task> transitionDepositModified(TeleOpRewrite.DepositState init, TeleOpRewrite.DepositState end)
    {
        switch(init) {
            case EXTENDED:
                switch(end) {
                    case NORMAL:
                        return Builder.create()
                                .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                                .build();

                }
            case RIGHT:
                switch(end) {
                    case NORMAL:
                        return Builder.create()
                                .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                                .moduleAction(deposit,Deposit.FlipState.DEPOSIT)
                                .delay(650)
                                .moduleAction(deposit, Deposit.WristState.HOVER)
                                //.moduleAction(deposit,Deposit.FlipState.DOWN)
                                .build();
                }
            case LEFT:
                switch(end) {
                    case NORMAL:
                        return Builder.create()
                                .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                                .moduleAction(deposit,Deposit.FlipState.DEPOSIT)
                                .delay(500)
                                .moduleAction(deposit, Deposit.WristState.HOVER)
                                //.moduleAction(deposit,Deposit.FlipState.DOWN)
                                .build();
                }
            case NORMAL:
                switch(end) {
                    case NORMAL:
                        return Builder.create()
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
                                .delay(800)
                                //.await(()->slides.getStatus()==Module.Status.IDLE)
                                .moduleAction(deposit, Deposit.RotateState.PLUS_NINETY)
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
                .delay(500)
                .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                .delay(150)
                .moduleAction(deposit, Deposit.FlipState.DEPOSIT)
                .delay(100)
                .moduleAction(slides, row)
                .moduleAction(deposit, Deposit.WristState.HOVER)
                .delay(600)
                //.await(()->slides.getStatus()==Module.Status.IDLE)
                .moduleAction(deposit, Deposit.WristState.DEPOSIT)
                .moduleAction(deposit, flip)
                .moduleAction(deposit, Deposit.RotateState.PLUS_NINETY)
                .delay(200)
                .moduleAction(deposit,Deposit.ExtensionState.DEPOSIT)
                .executeCode(()->slides.macroRunning=false)
                .build();
    }
public List<Task> quickReset(){
        return Builder.create()
                .executeCode(()->slides.macroRunning=true)
                .delay(150)
//                .moduleAction(deposit, Deposit.FlipState.PARTIAL)
                .moduleAction(slides, Slides.SlideState.HALF)
                .delay(600)
                .moduleAction(slides, Slides.SlideState.GROUND)
//                .moduleAction(deposit, Deposit.FlipState.TRANSFER)
            .delay(150)
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
                    .moduleAction(deposit, Deposit.FlipState.PARTIAL)
                    .delay(250)
                    .moduleAction(slides, row)
                    .delay(200)
                    .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                    .delay(200)
                    .moduleAction(deposit, Deposit.FlipState.DOWN)
                    .moduleAction(deposit, Deposit.WristState.HOVER)
                    .delay(300)
                    .moduleAction(deposit, Deposit.RotateState.PLUS_NINETY)
                    .await(slides::isIdle)
                    //.await(()->slides.getStatus()==Module.Status.IDLE)
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
    public List<Task> slidesOnlyAutonomousProgrammingVersionForAutomatedControl(Slides.SlideState row)
    {
        if(slides.getState()==Slides.SlideState.GROUND|| slides.getState()==Slides.SlideState.GROUND_UNTIL_LIMIT)
        {
            return Builder.create()
                    .executeCode(()->slides.macroRunning=true)
                    .moduleAction(deposit, Deposit.ClawState.CLOSED1)
                    .delay(500)
                    .moduleAction(deposit, Deposit.WristState.TRANSFER)
                    .moduleAction(slides, row)
                    .delay(300)
                    .moduleAction(deposit, Deposit.FlipState.DOWN)
                    .delay(300)
                    //.moduleAction(deposit, Deposit.WristState.PARTIAL)
                    .moduleAction(deposit, Deposit.WristState.YELLOW_HOVER)
                    .moduleAction(deposit, Deposit.RotateState.PLUS_NINETY)
                    //.moduleAction(deposit, Deposit.WristState.YELLOW_HOVER)
                    .await(slides::isIdle)
                    .executeCode(()->slides.macroRunning=false)
                    .build();
        }

        return Builder.create()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(slides, row)
                .awaitPreviousModuleActionCompletion()
                .executeCode(()->slides.macroRunning=false)
                .build();
    } public List<Task> slidesOnlyAutonomousProgrammingVersionForAutomatedControl(Slides.SlideState row,boolean x)
{
    if(slides.getState()==Slides.SlideState.GROUND|| slides.getState()==Slides.SlideState.GROUND_UNTIL_LIMIT)
    {
        return Builder.create()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.ClawState.CLOSED1)
                .delay(500)
                .moduleAction(deposit, Deposit.WristState.TRANSFER)
                .moduleAction(slides, row)
                .delay(300)
                .moduleAction(deposit, Deposit.FlipState.DOWN)
                .delay(300)
                //.moduleAction(deposit, Deposit.WristState.PARTIAL)
                .moduleAction(deposit, Deposit.WristState.YELLOW_HOVER)
                .moduleAction(deposit,Deposit.ExtensionState.DEPOSIT)
                .moduleAction(deposit, Deposit.RotateState.PLUS_NINETY)
                //.moduleAction(deposit, Deposit.WristState.YELLOW_HOVER)
                .await(slides::isIdle)
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
        ModuleState state = deposit.getState(Deposit.FlipState.class);
        ModuleState extensionState = deposit.getState(Deposit.ExtensionState.class);
        boolean depositIsLeft = state==Deposit.FlipState.LEFT;
        boolean depositIsRight = state==Deposit.FlipState.RIGHT;
        boolean depositIsExtended = state==Deposit.FlipState.DOWN && extensionState ==Deposit.ExtensionState.DEPOSIT;
        boolean depositIsNormal = state==Deposit.FlipState.DOWN && extensionState != Deposit.ExtensionState.DEPOSIT;

        long delay = slides.getState(Slides.SlideState.class) == Slides.SlideState.ROW3? 0:
                slides.getState(Slides.SlideState.class) == Slides.SlideState.ROW2? 500: 700;

        TeleOpRewrite.DepositState init = depositIsLeft? TeleOpRewrite.DepositState.LEFT: depositIsRight? TeleOpRewrite.DepositState.RIGHT:
                depositIsExtended? TeleOpRewrite.DepositState.EXTENDED: TeleOpRewrite.DepositState.NORMAL;


        return Builder.create()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.ClawState.OPEN)
                .delay(400)
                .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                .delay(depositIsExtended? 300: 0)
                .addTaskList(transitionDepositModified(init, TeleOpRewrite.DepositState.NORMAL))
                .moduleAction(deposit, Deposit.ExtensionState.TRANSFER_PARTIAL)
                .moduleAction(deposit, Deposit.RotateState.ZERO)
                .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                .moduleAction(deposit, Deposit.WristState.PARTIAL)
//                .delay(300)
                .executeCode(()->slides.setOperationState(Module.OperationState.PRESET))
                //.moduleAction(deposit, Deposit.WristState.TELESCOPE)
                .delay(delay)
                .moduleAction(slides, Slides.SlideState.GROUND)
                .await(()->slides.currentPosition()<600)
                .moduleAction(deposit, Deposit.WristState.TRANSFER)
                //.moduleAction(deposit, Deposit.WristState.PARTIAL2
                //.await(slides::isIdle)
                //.moduleAction(slides, Slides.SlideState.GROUND_UNTIL_LIMIT)
                //.await(slides::isIdle)
                .moduleAction(deposit, Deposit.ExtensionState.TRANSFER)
                .executeCode(()->slides.macroRunning=false)
                .build();
    }
    public List<Task> resetOnly()
    {

        return Builder.create()
                //.executeCode(()->slides.macroRunning=true)

                .moduleAction(deposit, Deposit.RotateState.ZERO)
                //.moduleAction(deposit, Deposit.ClawState.OPEN)
                //.delay(600)
                .addTaskList(transitionDepositModified(TeleOpRewrite.DepositState.NORMAL, TeleOpRewrite.DepositState.NORMAL))
                //.delay(depositIsLeft? 300: 0)
                .moduleAction(deposit, Deposit.ExtensionState.TRANSFER_PARTIAL)
                .delay(500)
                .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                .moduleAction(deposit, Deposit.WristState.PARTIAL)
                .delay(400)
                .executeCode(()->slides.setOperationState(Module.OperationState.PRESET))
                //.moduleAction(deposit, Deposit.WristState.TELESCOPE)
                .moduleAction(slides, Slides.SlideState.GROUND)
                .await(()->slides.currentPosition()<600)
                .moduleAction(deposit, Deposit.WristState.TRANSFER)
                //.moduleAction(deposit, Deposit.WristState.PARTIAL2
                //.await(slides::isIdle)
                //.moduleAction(slides, Slides.SlideState.GROUND_UNTIL_LIMIT)
                //.await(slides::isIdle)
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

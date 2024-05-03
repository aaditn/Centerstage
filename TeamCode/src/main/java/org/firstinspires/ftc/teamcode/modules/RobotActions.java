package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.moduleUtil.Module;
import org.firstinspires.ftc.teamcode.opmodes.tele.EthanOpp;
import org.firstinspires.ftc.teamcode.task_scheduler.Builder;
import org.firstinspires.ftc.teamcode.task_scheduler.Task;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.PresetPackage;
import org.firstinspires.ftc.teamcode.util.enums.Dice;

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

    public List<Task> lowerIntake(double drop_x , double sweep_x,int cycle, boolean lmao)
    {
        Intake.SweeperState sweep1 = cycle==0?Intake.SweeperState.ONE_SWEEP: cycle==1?Intake.SweeperState.THREE_SWEEP: cycle==2? Intake.SweeperState.FIVE_SWEEP: Intake.SweeperState.SEVEN_SWEEP;
        Intake.SweeperState sweep2 = cycle==0?Intake.SweeperState.ZERO: cycle==1?Intake.SweeperState.TWO_SWEEP: cycle==2?Intake.SweeperState.FOUR_SWEEP: Intake.SweeperState.SIX_SWEEP;

        if (cycle == 2) {
            return Builder.create()
                    .await(() -> robot.pose.position.x < drop_x)
                    .executeCode(()->Context.colorSensorsEnabled=true)
                    .moduleAction(intake, Intake.PositionState.MID)
                    .moduleAction(intake, Intake.PowerState.INTAKE)
                    .moduleAction(intake, Intake.ConveyorState.INTAKE)
                    .build();
        }
        return Builder.create()
                .await(() -> robot.pose.position.x < drop_x)
                .executeCode(()->Context.colorSensorsEnabled=true)
                .moduleAction(intake, Intake.PositionState.DOWN)
                .moduleAction(intake, Intake.PowerState.INTAKE)
                .moduleAction(intake, Intake.ConveyorState.INTAKE)
                /* .await(() -> robot.pose.position.x < sweep_x || (robot.k!=Paths.Return_to_Stack && robot.k!=Paths.Go_To_Stack))
                     .executeCode(()->Context.colorSensorsEnabled=false)
                     .conditionalModuleAction(intake, sweep1, ()->!(intake.pixel1Present|| intake.pixel2Present))
                     .conditionalModuleAction(intake, sweep2, ()->!(intake.pixel1Present|| intake.pixel2Present))

                 */
                .await(() -> robot.pose.position.x < sweep_x)
                .delay(700)
                .moduleAction(intake, sweep2)
                .delay(700)
                .moduleAction(intake, sweep1)

                .build();


    }
    public List<Task> deployPurple(double yLeft, double yMid, double yRight)
    {
        double y = Context.dice == Dice.MIDDLE?yMid: Context.dice == Dice.RIGHT?yRight: yLeft;
        double closed = 1;
        double open = 0.5;

        RobotLog.e("y is " + y);
        return Builder.create()
                .executeCode(() -> deposit.setState(Deposit.ClawState.CLOSED1))
                .delay((long) y)
                .executeCode(() -> intake.setState(Intake.PowerState.SLOW))
                .delay(500)
                .executeCode(() -> intake.setState(Intake.PowerState.OFF))

                .build();
    }

    public List<Task> autoDrop(double xPos, double startSlides, Slides.SlideState height, Deposit.RotateState rotateState){
        return Builder.create()
                .await(() -> robot.pose.position.x > startSlides)
                .addTaskList(slidesOnly(height, rotateState))
                .executeCode(()->slides.macroRunning=false)
                .await(()->robot.pose.position.x>xPos)
                .delay(250)
                .addTaskList(scorePixels())
                //.executeCode(()->Context.colorSensorsEnabled=false)
                .build();
    }
    public List<Task> slidesOnly(Slides.SlideState slideState, Deposit.RotateState rotateState) {

        if (!slides.getState().equals(Slides.SlideState.GROUND)) {
            return Builder.create()
                    .moduleAction(slides, slideState)
                    .moduleAction(deposit, rotateState)
                    .moduleAction(deposit, Deposit.WristState.FLICK)
                    .build();
        }
        return Builder.create()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                .delay(400)
                .moduleAction(deposit, Deposit.FlipState.PARTIAL)
                .moduleAction(slides, slideState)
                // .delay(100)
                // .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                .delay(200)
                .moduleAction(deposit, rotateState)
                .delay(100)
                .moduleAction(deposit, Deposit.FlipState.DOWN)
                .moduleAction(deposit, Deposit.WristState.FLICK)
                .delay(300)
                .await(slides::isIdle)
                //.await(()->slides.getStatus()==Module.Status.IDLE)
                .executeCode(()->slides.macroRunning=false)
                .executeCode(()->slides.instantAdd=false)
                .build();

    }
    public List<Task> slidesOnly(Slides.SlideState slideState) {

        if (!slides.getState().equals(Slides.SlideState.GROUND)) {
            return Builder.create()
                    .moduleAction(slides, slideState)
                    .moduleAction(deposit, Deposit.WristState.FLICK)
                    .build();
        }
        return Builder.create()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                .delay(400)
                .moduleAction(deposit, Deposit.FlipState.PARTIAL)
                .moduleAction(slides, slideState)
                // .delay(100)
                // .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                .delay(200)
                .moduleAction(deposit, Deposit.FlipState.DOWN)
                .moduleAction(deposit, Deposit.WristState.FLICK)
                .delay(300)
                .await(slides::isIdle)
                //.await(()->slides.getStatus()==Module.Status.IDLE)
                .executeCode(()->slides.macroRunning=false)
                .executeCode(()->slides.instantAdd=false)
                .build();

    }

    public List<Task> slidesOnly(Slides.SlideState slideState, Deposit.WristState state) {

        if (!slides.getState().equals(Slides.SlideState.GROUND)) {
            return Builder.create()
                    .moduleAction(slides, slideState)
                    .moduleAction(deposit, state)
                    .build();
        }
        return Builder.create()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                .delay(400)
                .moduleAction(deposit, Deposit.FlipState.PARTIAL)
                .moduleAction(slides, slideState)
                // .delay(100)
                // .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                .delay(200)
                .moduleAction(deposit, Deposit.FlipState.DOWN)
                .moduleAction(deposit, Deposit.WristState.FLICK)
                .delay(300)
                .await(slides::isIdle)
                //.await(()->slides.getStatus()==Module.Status.IDLE)
                .executeCode(()->slides.macroRunning=false)
                .executeCode(()->slides.instantAdd=false)
                .build();

    }

    public List<Task> scorePixels()
    {
        if (deposit.getState(Deposit.RotateState.class).equals(Deposit.RotateState.ZERO) || deposit.getState(Deposit.RotateState.class).equals(Deposit.RotateState.MINUS_ONE_EIGHTY)) {
            return Builder.create()
                    .executeCode(()->slides.macroRunning=true)
                    .moduleAction(deposit, Deposit.WristState.FLICK)
                    //   .delay(100)
                    .moduleAction(deposit, Deposit.ClawState.OPEN)
                    .delay(250)
                    .moduleAction(deposit, Deposit.ExtensionState.TRANSFER_PARTIAL)
                    .moduleAction(deposit, Deposit.RotateState.ZERO)
                    .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                    .moduleAction(deposit, Deposit.WristState.PARTIAL)
                    .executeCode(()-> EthanOpp.driveType = EthanOpp.DriveType.EXIT_BACKSTAGE)
                    .executeCode(()->slides.setOperationState(Module.OperationState.PRESET))
                    .delay(0)
                    .moduleAction(slides, Slides.SlideState.GROUND)
                    .await(()->slides.currentPosition()<600)
                    .moduleAction(deposit, Deposit.WristState.TRANSFER)
                    .executeCode(()->slides.macroRunning=false)

                    .build();
        } else {
            return Builder.create()
                    .executeCode(()->slides.macroRunning=true)
                    .moduleAction(deposit, Deposit.ClawState.OPEN)
                    .delay(300)
                    .executeCode(()-> EthanOpp.driveType = EthanOpp.DriveType.EXIT_BACKSTAGE)
                    .delay(150)
                    .moduleAction(deposit, Deposit.ExtensionState.TRANSFER_PARTIAL)
                    .moduleAction(deposit, Deposit.RotateState.ZERO)
                    .moduleAction(deposit, Deposit.FlipState.TRANSFER)
                    .moduleAction(deposit, Deposit.WristState.PARTIAL)
                    .executeCode(()->slides.setOperationState(Module.OperationState.PRESET))
                    .moduleAction(slides, Slides.SlideState.GROUND)
                    .delay(50)
                    .await(()->slides.currentPosition()<600)
                    .moduleAction(deposit, Deposit.WristState.TRANSFER)
                    .executeCode(()->slides.macroRunning=false)

                    .build();
        }

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
    public List<Task> slidesOnly(Slides.SlideState slideState, Deposit.RotateState rotateState, Deposit.WristState state) {

        if (!slides.getState().equals(Slides.SlideState.GROUND)) {
            return Builder.create()
                    .moduleAction(slides, slideState)
                    .moduleAction(deposit, rotateState)
                    .moduleAction(deposit, state)
                    .build();
        }
        return Builder.create()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                .delay(400)
                .moduleAction(deposit, Deposit.FlipState.PARTIAL)
                .moduleAction(slides, slideState)
                // .delay(100)
                // .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                .delay(200)
                .moduleAction(deposit, rotateState)
                .delay(100)
                .moduleAction(deposit, Deposit.FlipState.DOWN)
                .moduleAction(deposit, state)
                .delay(300)
                .await(slides::isIdle)
                //.await(()->slides.getStatus()==Module.Status.IDLE)
                .executeCode(()->slides.macroRunning=false)
                .executeCode(()->slides.instantAdd=false)
                .build();

    }


    public List<Task> slidesOnly(PresetPackage p) {
        Slides.SlideState slideState = p.slideState;
        Deposit.RotateState rotateState = p.rotateState;
        Deposit.WristState state = p.wristState;

        if (!slides.getState().equals(Slides.SlideState.GROUND)) {
            return Builder.create()
                    .moduleAction(slides, slideState)
                    .moduleAction(deposit, rotateState)
                    .moduleAction(deposit, state)
                    .build();
        }
        return Builder.create()
                .executeCode(()->slides.macroRunning=true)
                .moduleAction(deposit, Deposit.ClawState.CLOSED2)
                .delay(400)
                .moduleAction(deposit, Deposit.FlipState.PARTIAL)
                .moduleAction(slides, slideState)
                // .delay(100)
                // .moduleAction(deposit, Deposit.WristState.TELESCOPE)
                .delay(200)
                .moduleAction(deposit, rotateState)
                .delay(100)
                .moduleAction(deposit, Deposit.FlipState.DOWN)
                .moduleAction(deposit, state)
                .delay(300)
                .await(slides::isIdle)
                //.await(()->slides.getStatus()==Module.Status.IDLE)
                .executeCode(()->slides.macroRunning=false)
                .executeCode(()->slides.instantAdd=false)
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

    public List<Task> addTasks(List<List<Task>> tasks) {
        List<Task> added = Builder.create().build();
        for (int i = 0; i < tasks.size(); i++) {
            added.addAll(tasks.get(i));
        }
        return added;
    }
}

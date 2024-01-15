package org.firstinspires.ftc.teamcode.modules;

import org.firstinspires.ftc.teamcode.Robot;

public class RobotActions
{
    static RobotActions robotActions;
    Robot robot;
    public static RobotActions getInstance()
    {
        if (robotActions == null) {
            robotActions = new RobotActions(Robot.getInstance());
        }
        return robotActions;
    }

    public RobotActions(Robot robot)
    {
        this.robot=robot;
    }

}

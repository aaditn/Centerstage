package org.firstinspires.ftc.teamcode.localizer;

import android.os.SystemClock;

import org.firstinspires.ftc.teamcode.roadrunner.util.Encoder;

public class DriveEncoder
{
    Encoder encoder;
    double initialPosition, currentVel, currentPos, pastPos, lastTimeUpdated, lastTimeRetrieved;

    public DriveEncoder (Encoder encoder)
    {
        this.encoder=encoder;
        initialPosition=this.encoder.getCurrentPosition();
        pastPos=initialPosition;
    }

    public DriveEncoder (Encoder encoder, Encoder.Direction dir)
    {
        this.encoder=encoder;
        this.encoder.setDirection(dir);
        initialPosition=this.encoder.getCurrentPosition();
    }
    public void update()
    {
        currentVel=encoder.getCorrectedVelocity();
        currentPos=encoder.getCurrentPosition();
        lastTimeUpdated=SystemClock.elapsedRealtime();
    }
    public double getDeltaPos()
    {
        double returnPos=currentPos-pastPos;
        pastPos=currentPos;
        lastTimeRetrieved=SystemClock.elapsedRealtime();
        return returnPos;
    }
    public double getVelocity()
    {
        return currentVel;
    }
}

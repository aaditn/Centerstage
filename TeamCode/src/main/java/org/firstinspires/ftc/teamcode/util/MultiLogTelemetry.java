package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MultiLogTelemetry implements Telemetry
{
    MultipleTelemetry telemetry;
    String loggerString;
    boolean enableLogging;
    public MultiLogTelemetry(MultipleTelemetry telemetry)
    {
        this.telemetry=telemetry;
    }

    public void toggleLogging()
    {
        enableLogging=!enableLogging;
    }

    public void logToRobot()
    {
        //write the string to a file with a timestamp
    }

    @Override
    public Item addData(String caption, String format, Object... args)
    {
        return null;
    }



    @Override
    public Item addData(String caption, Object value) {
        telemetry.addData(caption, value);
        loggerString=caption.toString()+": "+value.toString();
        return null;
    }

    @Override
    public <T> Item addData(String caption, Func<T> valueProducer) {

        return null;
    }

    @Override
    public <T> Item addData(String caption, String format, Func<T> valueProducer) {
        return null;
    }

    @Override
    public boolean removeItem(Item item) {
        return false;
    }

    @Override
    public void clear() {
        telemetry.clear();
    }

    @Override
    public void clearAll() {
        telemetry.clearAll();
    }

    @Override
    public Object addAction(Runnable action) {
        return null;
    }

    @Override
    public boolean removeAction(Object token) {
        return false;
    }

    @Override
    public void speak(String text) {

    }

    @Override
    public void speak(String text, String languageCode, String countryCode) {

    }

    @Override
    public boolean update()
    {
        telemetry.update();
        return false;
    }

    @Override
    public Line addLine() {
        return null;
    }

    @Override
    public Line addLine(String lineCaption) {
        return null;
    }

    @Override
    public boolean removeLine(Line line) {
        return false;
    }

    @Override
    public boolean isAutoClear() {
        return false;
    }

    @Override
    public void setAutoClear(boolean autoClear) {

    }

    @Override
    public int getMsTransmissionInterval() {
        return 0;
    }

    @Override
    public void setMsTransmissionInterval(int msTransmissionInterval) {
        telemetry.setMsTransmissionInterval(msTransmissionInterval);
    }

    @Override
    public String getItemSeparator() {
        return null;
    }

    @Override
    public void setItemSeparator(String itemSeparator) {

    }

    @Override
    public String getCaptionValueSeparator() {
        return null;
    }

    @Override
    public void setCaptionValueSeparator(String captionValueSeparator) {

    }

    @Override
    public void setDisplayFormat(DisplayFormat displayFormat) {

    }

    @Override
    public Log log()
    {
        return null;
    }
}

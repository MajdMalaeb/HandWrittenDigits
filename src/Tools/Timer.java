/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

/**
 *
 * @author Majd Malaeb
 */
public class Timer {

    private long startTime, endTime;

    public Timer() {
    }

    public void start() {
        if (startTime == 0) {
            startTime = System.nanoTime();
        }
    }

    public void stop() {
        if (endTime == 0) {
            endTime = System.nanoTime();
        }
    }

    public long getTime() {
        if (startTime > endTime) {
            return System.nanoTime() - startTime;
        }
        return endTime - startTime;
    }

    @Override
    public String toString() {
        return toString(getTime());
    }

    /**
     * Convert nanoSecs to String
     *
     * @param nanoSecs
     * @return
     */
    public static String toString(long nanoSecs) {
        int minutes = (int) (nanoSecs / 60000000000.0);
        int seconds = (int) (nanoSecs / 1000000000.0) - (minutes * 60);
        int millisecs = (int) (((nanoSecs / 1000000000.0) - (seconds + minutes * 60)) * 1000);
        return (minutes != 0 ? (minutes + "min") : "") + (seconds != 0 ? (seconds + "s") : "") + (millisecs != 0 ? (millisecs + "ms") : "");
    }

}

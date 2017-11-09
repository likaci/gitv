package org.cybergarage.util;

public final class TimerUtil {
    public static final void wait(int waitTime) {
        try {
            Thread.sleep((long) waitTime);
        } catch (Exception e) {
        }
    }

    public static final void waitRandom(int time) {
        try {
            Thread.sleep((long) ((int) (Math.random() * ((double) time))));
        } catch (Exception e) {
        }
    }
}

package com.gala.multiscreen.dmr;

import com.gala.multiscreen.dmr.util.MSLog;
import com.gala.multiscreen.dmr.util.MSLog.LogType;

public class SeekCounter {
    private static final SeekCounter mSeekCounter = new SeekCounter();
    private static int numOfSeek = 0;

    private SeekCounter() {
    }

    public static SeekCounter getSeekCounter() {
        return mSeekCounter;
    }

    public synchronized boolean isSeeking() {
        showLog();
        return numOfSeek > 0;
    }

    public synchronized void doSeek() {
        numOfSeek++;
        showLog();
    }

    public synchronized void finishSeek() {
        numOfSeek = 0;
        showLog();
    }

    private void showLog() {
        MSLog.log("numOfSeek = " + numOfSeek, LogType.SEEK);
    }
}

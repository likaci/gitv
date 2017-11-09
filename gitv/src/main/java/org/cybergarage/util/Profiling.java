package org.cybergarage.util;

import android.util.Log;

public class Profiling {
    private static boolean DEBUG = true;
    private static String TAG = "gala_profiling";
    private long mTime = 0;

    public static int i(String msg) {
        return Log.i(TAG, msg);
    }

    public void start() {
        if (DEBUG) {
            this.mTime = System.currentTimeMillis();
        }
    }

    public void end(String msg) {
        if (DEBUG) {
            i(new StringBuilder(String.valueOf(msg)).append(" time: ").append(System.currentTimeMillis() - this.mTime).toString());
        }
    }
}

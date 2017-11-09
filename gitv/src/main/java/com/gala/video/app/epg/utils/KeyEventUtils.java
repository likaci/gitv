package com.gala.video.app.epg.utils;

import android.app.Instrumentation;
import android.util.Log;
import com.gala.video.lib.framework.core.utils.ThreadUtils;

public class KeyEventUtils {
    private static final Instrumentation mInstrumentation = new Instrumentation();

    public static void simulateKeyEvent(final int... keyCodes) {
        ThreadUtils.execute(new Runnable() {
            public void run() {
                try {
                    for (int code : keyCodes) {
                        KeyEventUtils.mInstrumentation.sendKeyDownUpSync(code);
                    }
                } catch (Exception e) {
                    Log.w("KeyEventUtils", "KeyEventUtils ---  simulateKeyEvent  error : " + e.getMessage());
                }
            }
        });
    }
}

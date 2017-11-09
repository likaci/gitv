package com.gala.video.lib.share.ifimpl.logrecord.utils;

import android.app.ActivityManager;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

public class MonkeyUtils {
    private static final String TAG = "MonkeyUtils";
    public static final int VERSION_CUPCAKE = 3;
    public static final int VERSION_DONUT = 4;
    public static final int VERSION_FROYO = 8;
    public static final int VERSION_JELLYBEAN = 16;
    private static boolean fetchedSdkIntField = false;
    private static Field sdkIntField = null;

    public static boolean isMonkeyRunning() {
        if (getVersionSdkIntCompat() >= 8) {
            boolean isUserMonkey = ActivityManager.isUserAMonkey();
            if (!isUserMonkey) {
                return isUserMonkey;
            }
        }
        return checkIsMonkeyPSRunning();
    }

    private static boolean checkIsMonkeyPSRunning() {
        try {
            String monkeyProcessInfo = isMonkeyState();
            if (monkeyProcessInfo == null) {
                return false;
            }
            Log.v(TAG, "monkeyProcessInfo = " + monkeyProcessInfo);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String isMonkeyState() throws IOException {
        String line;
        Log.i(TAG, "Here go into Monkey check");
        Log.i(TAG, "ps | grep -c \"com.android.commands.monkey\"");
        Process sMonkeyProcess = Runtime.getRuntime().exec("ps");
        String result = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(sMonkeyProcess.getInputStream()), 8192);
        Log.i(TAG, "reader = " + reader.toString());
        do {
            try {
                line = reader.readLine();
                if (line != null) {
                }
                break;
            } catch (IOException e) {
                Log.v(TAG, "monkey unexpected exception " + e.getMessage());
                try {
                    sMonkeyProcess.destroy();
                    sMonkeyProcess.exitValue();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Log.v(TAG, "destroyed monkey process");
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e2) {
                        Log.v(TAG, "unexpected exception", e2);
                    }
                }
            } catch (Throwable th) {
                try {
                    sMonkeyProcess.destroy();
                    sMonkeyProcess.exitValue();
                } catch (Exception ex2) {
                    ex2.printStackTrace();
                }
                Log.v(TAG, "destroyed monkey process");
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e22) {
                        Log.v(TAG, "unexpected exception", e22);
                    }
                }
            }
        } while (!line.contains("com.android.commands.monkey"));
        result = line;
        try {
            sMonkeyProcess.destroy();
            sMonkeyProcess.exitValue();
        } catch (Exception ex22) {
            ex22.printStackTrace();
        }
        Log.v(TAG, "destroyed monkey process");
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e222) {
                Log.v(TAG, "unexpected exception", e222);
            }
        }
        return result;
    }

    private static int getVersionSdkIntCompat() {
        try {
            Field field = getSdkIntField();
            if (field != null) {
                return ((Integer) field.get(null)).intValue();
            }
        } catch (IllegalAccessException e) {
        }
        return 3;
    }

    private static Field getSdkIntField() {
        if (!fetchedSdkIntField) {
            try {
                sdkIntField = VERSION.class.getField("SDK_INT");
            } catch (NoSuchFieldException e) {
            }
            fetchedSdkIntField = true;
        }
        return sdkIntField;
    }
}

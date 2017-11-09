package com.gala.video.app.stub;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Process;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ProcessHelper {
    private static final String TAG = "ProcessHelper";
    private static String mProcessName = "";

    public static String getCurrentProcessName() {
        return getCurrentProcessName("");
    }

    public static String getCurrentProcessName(String defaultValue) {
        String processName = defaultValue;
        if (!"".equals(mProcessName)) {
            processName = mProcessName;
        }
        Log.i(TAG, "currentProcesssName=" + processName);
        return processName;
    }

    public static void fetchCurrentProcessName(Context context) {
        Object activityThread = getActivityThread(context);
        if (activityThread != null) {
            try {
                Method m = Class.forName("android.app.ActivityThread").getMethod("getProcessName", new Class[0]);
                m.setAccessible(true);
                Object o = m.invoke(activityThread, new Object[0]);
                if (o != null) {
                    mProcessName = o.toString();
                }
            } catch (Exception e) {
            }
        }
        if ("".equals(mProcessName)) {
            mProcessName = getRunningProcessName(context);
        }
    }

    private static Object getActivityThread(Context context) {
        Object thread = null;
        try {
            Method m = Class.forName("android.app.ActivityThread").getMethod("currentActivityThread", new Class[0]);
            m.setAccessible(true);
            thread = m.invoke(null, new Object[0]);
            if (thread == null) {
                Field mLoadedApk = context.getClass().getField("mLoadedApk");
                mLoadedApk.setAccessible(true);
                Object apk = mLoadedApk.get(context);
                Field mActivityThreadField = apk.getClass().getDeclaredField("mActivityThread");
                mActivityThreadField.setAccessible(true);
                thread = mActivityThreadField.get(apk);
            }
        } catch (Throwable ignore) {
            Log.i(TAG, "getCurrentProcessName", ignore);
        }
        return thread;
    }

    private static String getRunningProcessName(Context context) {
        String processName = "";
        int pid = Process.myPid();
        for (RunningAppProcessInfo appProcess : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                processName = appProcess.processName;
            }
        }
        return processName;
    }
}

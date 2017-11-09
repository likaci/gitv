package com.gala.video.lib.framework.core.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.os.Process;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;

public class ProcessHelper {
    private static ProcessHelper mInstance = null;
    private ActivityManager mActivityManager = ((ActivityManager) AppRuntimeEnv.get().getApplicationContext().getSystemService("activity"));

    private ProcessHelper() {
    }

    public static synchronized ProcessHelper getInstance() {
        ProcessHelper processHelper;
        synchronized (ProcessHelper.class) {
            if (mInstance == null) {
                mInstance = new ProcessHelper();
            }
            processHelper = mInstance;
        }
        return processHelper;
    }

    public void killProcess(int pid) {
        Process.killProcess(pid);
    }

    public void killProcess(String processName) {
        int pid = getProcessPid(processName);
        if (pid > 0) {
            killProcess(pid);
        }
    }

    public int getProcessPid(String processName) {
        for (RunningAppProcessInfo appProcess : this.mActivityManager.getRunningAppProcesses()) {
            if (appProcess.processName.equals(processName)) {
                return appProcess.pid;
            }
        }
        return -1;
    }

    public String getProcessName(int pid) {
        for (RunningAppProcessInfo appProcess : this.mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }
}

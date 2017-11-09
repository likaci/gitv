package com.gala.video.app.player.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.os.Debug;
import android.os.Debug.MemoryInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.project.Project;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MemInfoHelper {
    private static final int DELAY_TIME = 10000;
    private static final int PRINT_MEMINF_MESSAGE = 1;
    private static final String TAG = "MemInfoHelper";
    private static MemInfoHelper mInstance = null;
    private static final String mThreadName = "get-meminfo-thread";
    private ProcessInfo mMPlayerProcessInfo;
    private ProcessInfo mMainProcessInfo;
    private WorkHandler mWorkHander;
    private HandlerThread mWorkThread;

    private class ProcessInfo {
        int pid;
        String processName;

        private ProcessInfo() {
        }
    }

    private class WorkHandler extends Handler {
        public WorkHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MemInfoHelper.this.mWorkHander.removeCallbacksAndMessages(null);
                    MemInfoHelper.this.printMemInfo(MemInfoHelper.this.mMainProcessInfo);
                    MemInfoHelper.this.printMemInfo(MemInfoHelper.this.mMPlayerProcessInfo);
                    MemInfoHelper.this.startPrintMemInfo();
                    return;
                default:
                    return;
            }
        }
    }

    private MemInfoHelper() {
    }

    private void initProcessInfo() {
        boolean checkMainOver = false;
        boolean checkPlayerOver = false;
        String processName = Project.getInstance().getBuild().getPackageName();
        for (RunningAppProcessInfo app : ((ActivityManager) AppRuntimeEnv.get().getApplicationContext().getSystemService("activity")).getRunningAppProcesses()) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "initProcessInfo() " + app.pid + "-" + app.processName + ",myPid=" + Process.myPid());
            }
            if (processName.equals(app.processName)) {
                checkMainOver = true;
                this.mMainProcessInfo = new ProcessInfo();
                this.mMainProcessInfo.pid = app.pid;
                this.mMainProcessInfo.processName = app.processName;
            } else if ((processName + ":player").equals(app.processName)) {
                checkPlayerOver = true;
                this.mMPlayerProcessInfo = new ProcessInfo();
                this.mMPlayerProcessInfo.pid = app.pid;
                this.mMPlayerProcessInfo.processName = app.processName;
            }
            if (checkMainOver && checkPlayerOver) {
                break;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "mMainProcessInfo = " + this.mMainProcessInfo + ",mMPlayerProcessInfo=" + this.mMPlayerProcessInfo);
        }
    }

    public static synchronized MemInfoHelper getInstance() {
        MemInfoHelper memInfoHelper;
        synchronized (MemInfoHelper.class) {
            if (mInstance == null) {
                mInstance = new MemInfoHelper();
            }
            memInfoHelper = mInstance;
        }
        return memInfoHelper;
    }

    public synchronized void initialize() {
        this.mWorkThread = new HandlerThread(mThreadName);
        this.mWorkThread.start();
        this.mWorkHander = new WorkHandler(this.mWorkThread.getLooper());
        initProcessInfo();
    }

    public synchronized void startPrintMemInfo() {
        if ((!Project.getInstance().getBuild().supportPlayerMultiProcess() && GetInterfaceTools.getIInit().isMainProcess()) || (Project.getInstance().getBuild().supportPlayerMultiProcess() && GetInterfaceTools.getIInit().isPlayerProcess())) {
            Message msg = this.mWorkHander.obtainMessage();
            msg.what = 1;
            this.mWorkHander.sendMessageDelayed(msg, 10000);
        }
    }

    public synchronized void stopPrintMemInfo() {
        this.mWorkHander.removeCallbacksAndMessages(null);
        this.mWorkThread.quit();
    }

    private MemoryInfo getProcessMemInfo(int pid) {
        return ((ActivityManager) AppRuntimeEnv.get().getApplicationContext().getSystemService("activity")).getProcessMemoryInfo(new int[]{pid})[0];
    }

    private void printMemInfo(ProcessInfo processInfo) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "mMainProcessInfo = " + this.mMainProcessInfo + ",mMPlayerProcessInfo=" + this.mMPlayerProcessInfo);
        }
        if (processInfo != null) {
            MemoryInfo meminfo = getProcessMemInfo(processInfo.pid);
            StringBuilder memInfoString = new StringBuilder();
            memInfoString.append("MemInfo:\n").append("ProcessID               : " + processInfo.pid).append("\n").append("ProcessName             : " + processInfo.processName).append("\n").append("totalPss                : " + meminfo.getTotalPss() + "\n").append("dalvikPss               : " + meminfo.dalvikPss + "\n").append("nativePss               : " + meminfo.nativePss + "\n").append("otherPss                : " + meminfo.otherPss + "\n").append("nativePrivateDirty      : " + meminfo.nativePrivateDirty + "\n").append("nativeSharedDirty       : " + meminfo.nativeSharedDirty + "\n").append("dalvikPrivateDirty      : " + meminfo.dalvikPrivateDirty + "\n").append("dalvikSharedDirty       : " + meminfo.dalvikSharedDirty + "\n").append("NativeHeapAllocatedSize : " + Debug.getNativeHeapAllocatedSize() + "B\n").append("NativeHeapFreeSize      : " + Debug.getNativeHeapFreeSize() + "B\n").append("NativeHeapSize          : " + Debug.getNativeHeapSize() + "B\n");
            System.out.print(memInfoString.toString());
        }
    }

    private void printCpuInfo() {
        try {
            System.out.println("CPUInfo");
            Process process = Runtime.getRuntime().exec("/system/bin/top -n 1 -m 5 -t");
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()), 8192);
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    System.out.println(line);
                } else {
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

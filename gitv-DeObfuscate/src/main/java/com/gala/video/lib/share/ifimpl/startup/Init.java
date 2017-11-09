package com.gala.video.lib.share.ifimpl.startup;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Process;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.startup.IInit;
import com.gala.video.lib.share.ifmanager.bussnessIF.startup.IInit.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.startup.InitTaskInput;
import com.gala.video.lib.share.project.Project;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class Init extends Wrapper implements IInit {
    private static final String TAG = "startup/Init";
    private int mCurrentProcess = -1;
    private ScheduledThreadPoolExecutor mExecutor = new ScheduledThreadPoolExecutor(1);

    Init() {
        checkCurrentProcess(AppRuntimeEnv.get().getApplicationContext());
        LogUtils.m1568d(TAG, "Init");
    }

    public void execute(InitTaskInput input) {
        if (input == null || input.getTask() == null) {
            LogUtils.m1571e(TAG, "init input is invalid!!!");
        } else if (!input.getInitProcess().contains(Integer.valueOf(this.mCurrentProcess))) {
        } else {
            if (input.getInitPattern() == 100) {
                input.getTask().run();
                return;
            }
            long delay = input.getDelay();
            if (delay != 0) {
                this.mExecutor.schedule(input.getTask(), delay, TimeUnit.MILLISECONDS);
            } else {
                this.mExecutor.execute(input.getTask());
            }
        }
    }

    public boolean isPlayerProcess() {
        return this.mCurrentProcess == 1;
    }

    public boolean isMainProcess() {
        return this.mCurrentProcess == 0;
    }

    private void checkCurrentProcess(Context context) {
        String processName = Project.getInstance().getBuild().getPackageName();
        for (RunningAppProcessInfo app : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "checkProcessRunning() " + app.pid + "-" + app.processName + ",myPid=" + Process.myPid());
            }
            if (app.pid != Process.myPid() || !processName.equals(app.processName)) {
                if (app.pid != Process.myPid() || !(processName + ":player").equals(app.processName)) {
                    if (app.pid == Process.myPid() && IMsgUtils.PUSH_SERVICE_NAME.equals(app.processName)) {
                        this.mCurrentProcess = 2;
                        break;
                    }
                } else {
                    this.mCurrentProcess = 1;
                    break;
                }
            }
            this.mCurrentProcess = 0;
            break;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "checkProcessRunning() package name =" + processName);
        }
    }
}

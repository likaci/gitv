package com.gala.video.app.epg.home.data.hdata.task;

import android.os.Process;
import com.gala.video.app.epg.home.data.hdata.DataRequestRouter;
import com.gala.video.app.epg.home.data.hdata.DataRequestTaskStrategy.onExecuteListener;
import com.gala.video.lib.framework.core.utils.LogUtils;

public abstract class BaseRequestTask implements Runnable {
    private static final String TAG = "home/BaseRequestTask";
    private onExecuteListener mListener;
    private int mResult = 1;
    protected int mTaskId = -1;

    public abstract void invoke();

    public abstract void onOneTaskFinished();

    public void setTaskCallBack(onExecuteListener callback) {
        this.mListener = callback;
    }

    public int getRequestResult() {
        return this.mResult;
    }

    public String identifier() {
        return String.valueOf(getClass().getSimpleName());
    }

    public void run() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "invoke task = " + getClass().getName() + " thread id = " + Thread.currentThread().getId());
        }
        Process.setThreadPriority(10);
        invoke();
        onOneTaskFinished();
        if (this.mListener != null) {
            this.mListener.onFinished();
        }
        next();
    }

    protected void next() {
        DataRequestRouter.sInstance.next(this.mTaskId);
    }
}

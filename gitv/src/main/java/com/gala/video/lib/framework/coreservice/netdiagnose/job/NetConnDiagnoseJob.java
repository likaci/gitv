package com.gala.video.lib.framework.coreservice.netdiagnose.job;

import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.job.JobControllerHolder;
import com.gala.video.lib.framework.core.job.JobError;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.StateCallback;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;

public class NetConnDiagnoseJob extends NetDiagnoseJob {
    private static final String TAG = "NetConnDiagnoseJob";

    private class Callback extends JobControllerHolder implements StateCallback {
        public Callback(JobController controller) {
            super(controller);
        }

        public void getStateResult(int state) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(NetConnDiagnoseJob.TAG, "getStateResult: " + state);
            }
            ((NetDiagnoseInfo) NetConnDiagnoseJob.this.getData()).setNetConnDiagnoseResult(state);
            switch (state) {
                case 1:
                case 2:
                    NetConnDiagnoseJob.this.mIsJobComplete = true;
                    NetConnDiagnoseJob.this.notifyJobSuccess(getController());
                    return;
                default:
                    NetConnDiagnoseJob.this.mIsJobComplete = true;
                    NetConnDiagnoseJob.this.notifyJobFail(getController(), new JobError(""));
                    return;
            }
        }
    }

    public NetConnDiagnoseJob(NetDiagnoseInfo data) {
        super(data);
    }

    public NetConnDiagnoseJob(NetDiagnoseInfo data, NetDiagnoseJobListener listener) {
        super(data, listener);
    }

    public void onRun(JobController controller) {
        super.onRun(controller);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> onRun");
        }
        NetWorkManager.getInstance().checkNetWork(new Callback(controller));
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "<< onRun");
        }
    }
}

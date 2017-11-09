package com.gala.video.lib.framework.coreservice.netdiagnose.job;

import com.gala.video.lib.framework.core.job.Job;
import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.job.JobError;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import java.util.Timer;
import java.util.TimerTask;

public class NetDiagnoseJob extends Job<NetDiagnoseInfo> {
    protected static String TAG = "NetDiagnoseJob";
    private static final long sJobTime = 300000;
    protected boolean mIsJobComplete = false;

    public NetDiagnoseJob(NetDiagnoseInfo data) {
        super(TAG, data);
    }

    public NetDiagnoseJob(NetDiagnoseInfo data, NetDiagnoseJobListener listener) {
        super(TAG, data, listener);
    }

    public void onRun(final JobController controller) {
        LogUtils.m1568d(TAG, ">>onRun");
        this.mIsJobComplete = false;
        new Timer().schedule(new TimerTask() {
            public void run() {
                LogUtils.m1568d(NetDiagnoseJob.TAG, "300s later, is job complete:" + NetDiagnoseJob.this.mIsJobComplete);
                if (NetDiagnoseJob.this.mIsJobComplete) {
                    LogUtils.m1568d(NetDiagnoseJob.TAG, "job complete normal");
                } else {
                    NetDiagnoseJob.this.notifyJobFail(controller, new JobError("time limit"));
                }
            }
        }, 300000);
        LogUtils.m1568d(TAG, "<<onRun");
    }
}

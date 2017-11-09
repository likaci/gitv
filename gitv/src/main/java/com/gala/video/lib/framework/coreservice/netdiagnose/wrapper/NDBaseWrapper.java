package com.gala.video.lib.framework.coreservice.netdiagnose.wrapper;

import android.os.Handler;
import android.os.Looper;
import com.gala.video.lib.framework.core.job.Job;
import com.gala.video.lib.framework.coreservice.netdiagnose.INDDoneListener;
import com.gala.video.lib.framework.coreservice.netdiagnose.INDWrapperOperate;
import com.gala.video.lib.framework.coreservice.netdiagnose.INetDiagnoseController;
import com.gala.video.lib.framework.coreservice.netdiagnose.job.NetDiagnoseJob;
import com.gala.video.lib.framework.coreservice.netdiagnose.job.NetDiagnoseJobListener;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import java.util.HashMap;
import java.util.Map;

public class NDBaseWrapper implements INDWrapperOperate {
    public static final String KEY_JOB_DATA = "data";
    public static final String KEY_JOB_SUCCESS = "success";
    protected INDDoneListener mDoneListener = null;
    protected NetDiagnoseJob mJob = null;
    protected NetDiagnoseJobListener mJobListener = new NetDiagnoseJobListener() {
        public void onJobDone(final Job<NetDiagnoseInfo> job) {
            NDBaseWrapper.this.mMainHandler.post(new Runnable() {
                public void run() {
                    Map<String, Object> result = new HashMap();
                    result.put("data", job.getData());
                    NDBaseWrapper.this.notifyResult(job, result);
                }
            });
        }
    };
    protected Handler mMainHandler = new Handler(Looper.getMainLooper());
    protected String mResult;
    protected boolean mRunNextWhenFail = false;

    public NDBaseWrapper(boolean run) {
        setRunNextWhenFail(run);
    }

    public void setJob(NetDiagnoseJob job) {
        this.mJob = job;
        this.mJob.setListener(this.mJobListener);
    }

    public NetDiagnoseJob getJobEntity(INetDiagnoseController ndCtlr) {
        return this.mJob;
    }

    public void setRunNextWhenFail(boolean run) {
        this.mRunNextWhenFail = run;
    }

    public void setDoneListener(INDDoneListener listener) {
        this.mDoneListener = listener;
    }

    public String getResult() {
        return this.mResult;
    }

    protected void notifyResult(Job<NetDiagnoseInfo> job, Map<String, Object> result) {
        if (this.mDoneListener != null) {
            result.put("success", Boolean.valueOf(job.getState() == 2));
            this.mDoneListener.onFinish(result);
        }
    }
}

package com.gala.video.lib.share.ifimpl.netdiagnose.job;

import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.job.JobControllerHolder;
import com.gala.video.lib.framework.core.job.JobError;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.netdiagnose.job.NetDiagnoseJob;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import com.gala.video.lib.share.ifimpl.netdiagnose.collection.CollectionTask;
import com.gala.video.lib.share.ifimpl.netdiagnose.collection.ICheckInterfaceCallBack;

public class CollectInfoJob extends NetDiagnoseJob {
    private final String TAG;
    private CollectionTask mCollectionTask;

    private class Callback extends JobControllerHolder implements ICheckInterfaceCallBack {
        public Callback(JobController controller) {
            super(controller);
        }

        public void checkInterfaceSuccess(String response) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(CollectInfoJob.this.TAG, " checkInterfaceSuccess");
            }
            ((NetDiagnoseInfo) CollectInfoJob.this.getData()).setCollectionResult(response);
            CollectInfoJob.this.mIsJobComplete = true;
            CollectInfoJob.this.notifyJobSuccess(getController());
        }

        public void checkInterfaceFail(String response) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(CollectInfoJob.this.TAG, "checkInterfaceFail");
            }
            ((NetDiagnoseInfo) CollectInfoJob.this.getData()).setCollectionResult(response);
            CollectInfoJob.this.mIsJobComplete = true;
            CollectInfoJob.this.notifyJobFail(getController(), new JobError(null));
        }
    }

    public CollectInfoJob(NetDiagnoseInfo data) {
        super(data);
        this.TAG = "NetDiagnoseJob/CollectInfoJob@" + hashCode();
        this.mCollectionTask = new CollectionTask();
    }

    public CollectInfoJob(NetDiagnoseInfo data, String pingUrls) {
        super(data);
        this.TAG = "NetDiagnoseJob/CollectInfoJob@" + hashCode();
        this.mCollectionTask = new CollectionTask(pingUrls);
    }

    public void onRun(JobController controller) {
        super.onRun(controller);
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> onRun");
        }
        this.mCollectionTask.collection(new Callback(controller));
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "<< onRun");
        }
    }
}

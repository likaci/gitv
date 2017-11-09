package com.gala.video.lib.framework.coreservice.netdiagnose.job;

import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import com.gala.video.lib.framework.coreservice.netdiagnose.utils.NetDiagnoseDNSUtils;

public class DNSJob extends NetDiagnoseJob {
    private final String TAG = ("NetDiagnoseJob/DNSJob@" + hashCode());

    public DNSJob(NetDiagnoseInfo data) {
        super(data);
    }

    public DNSJob(NetDiagnoseInfo data, NetDiagnoseJobListener listener) {
        super(data, listener);
    }

    public void onRun(JobController controller) {
        super.onRun(controller);
        LogUtils.m1568d(this.TAG, ">> onRun");
        ((NetDiagnoseInfo) getData()).setDnsResult(NetDiagnoseDNSUtils.executeDNS2());
        this.mIsJobComplete = true;
        notifyJobSuccess(controller);
        LogUtils.m1568d(this.TAG, "<< onRun");
    }
}

package com.gala.video.lib.framework.coreservice.netdiagnose;

import android.content.Context;
import com.gala.speedrunner.netdoctor.TVNetDoctor;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.netdiagnose.job.NetDiagnoseJob;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import java.util.List;

public class NetDiagnoseBaseRunner extends NetDiagnoseRunner implements INetDiagnoseController {
    private static final String TAG = "NetDiag/NetDiagnoseController";
    private Context mContext;
    private TVNetDoctor mTvNetDoctor = new TVNetDoctor();

    public NetDiagnoseBaseRunner(Context context, NetDiagnoseInfo info, String deviceId, String domainName) {
        super(context, info);
        this.mContext = context;
        this.mTvNetDoctor.initNetDoctor(deviceId, domainName);
    }

    public NetDiagnoseInfo getNDInfo() {
        return getInfo();
    }

    public TVNetDoctor getNetDoctor() {
        return this.mTvNetDoctor;
    }

    public void startCheckEx(List<INDWrapperOperate> wrapperList) {
        NetDiagnoseJob header = null;
        NetDiagnoseJob preJob = null;
        for (INDWrapperOperate wrapper : wrapperList) {
            NetDiagnoseJob job = wrapper.getJobEntity(this);
            if (preJob == null) {
                preJob = job;
                header = job;
            } else {
                preJob.link(job);
                preJob = job;
            }
        }
        submit(header);
    }

    public void stopCheck() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "stopCheck, mTvNetDoctor is null ?" + (this.mTvNetDoctor == null));
        }
        if (this.mTvNetDoctor != null && this.mTvNetDoctor.isStart()) {
            this.mTvNetDoctor.stopPlay();
            this.mTvNetDoctor = null;
        }
        cancel();
    }
}

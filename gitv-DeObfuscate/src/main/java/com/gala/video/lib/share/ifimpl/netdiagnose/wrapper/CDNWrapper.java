package com.gala.video.lib.share.ifimpl.netdiagnose.wrapper;

import com.gala.video.lib.framework.coreservice.netdiagnose.INDUploadCallback;
import com.gala.video.lib.framework.coreservice.netdiagnose.INetDiagnoseController;
import com.gala.video.lib.framework.coreservice.netdiagnose.job.NetDiagnoseJob;
import com.gala.video.lib.framework.coreservice.netdiagnose.wrapper.NDBaseWrapper;
import com.gala.video.lib.share.ifimpl.netdiagnose.job.CdnDiagnoseJob;
import com.netdoc.FileType;

public class CDNWrapper extends NDBaseWrapper {
    private int mPlayerType;
    private INDUploadCallback mUploadcallback = null;

    public CDNWrapper(int playerType, boolean run) {
        super(run);
        this.mPlayerType = playerType;
    }

    public NetDiagnoseJob getJobEntity(INetDiagnoseController ndCtlr) {
        FileType fileType = FileType.TYPE_F4V;
        if (this.mPlayerType == 1) {
            fileType = FileType.TYPE_HLS;
        }
        NetDiagnoseJob job = new CdnDiagnoseJob(ndCtlr.getNDInfo(), this.mJobListener, ndCtlr.getNetDoctor(), this.mUploadcallback, fileType);
        job.setRunNextWhenFail(this.mRunNextWhenFail);
        return job;
    }

    public void setNDUploadCallback(INDUploadCallback callback) {
        this.mUploadcallback = callback;
    }
}

package com.gala.video.lib.framework.coreservice.netdiagnose;

import com.gala.video.lib.framework.coreservice.netdiagnose.job.NetDiagnoseJob;

public interface INDWrapperOperate {
    NetDiagnoseJob getJobEntity(INetDiagnoseController iNetDiagnoseController);

    String getResult();

    void setDoneListener(INDDoneListener iNDDoneListener);

    void setRunNextWhenFail(boolean z);
}

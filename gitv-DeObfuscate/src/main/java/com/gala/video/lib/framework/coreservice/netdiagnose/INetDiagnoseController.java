package com.gala.video.lib.framework.coreservice.netdiagnose;

import com.gala.speedrunner.netdoctor.TVNetDoctor;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import java.util.List;

public interface INetDiagnoseController {
    NetDiagnoseInfo getNDInfo();

    TVNetDoctor getNetDoctor();

    void startCheckEx(List<INDWrapperOperate> list);

    void stopCheck();
}

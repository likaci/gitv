package com.gala.video.lib.share.ifimpl.netdiagnose;

import android.content.Context;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.video.lib.framework.coreservice.netdiagnose.NetDiagnoseBaseRunner;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import com.gala.video.lib.share.project.Project;

public class NetDiagnoseController extends NetDiagnoseBaseRunner {
    public NetDiagnoseController(Context context, NetDiagnoseInfo info) {
        super(context, info, TVApiBase.getTVApiProperty().getPassportDeviceId(), Project.getInstance().getBuild().getDomainName());
    }
}

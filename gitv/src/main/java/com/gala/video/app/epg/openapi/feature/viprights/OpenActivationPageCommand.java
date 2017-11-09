package com.gala.video.app.epg.openapi.feature.viprights;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.qiyi.tv.client.impl.Params.OperationType;
import com.qiyi.tv.client.impl.Params.TargetType;

public class OpenActivationPageCommand extends ServerCommand<Intent> {
    public OpenActivationPageCommand(Context context) {
        super(context, TargetType.TARGET_VIP_RIGHTS, OperationType.OP_OPEN_ACTIVATION_PAGE, 30000);
        setNeedNetwork(false);
    }

    protected Bundle onProcess(Bundle inParams) {
        GetInterfaceTools.getLoginProvider().startActivateActivity(getContext(), "from_openapi", 14);
        increaseAccessCount();
        Bundle result = OpenApiResultCreater.createResultBundle(0);
        ServerParamsHelper.setCommandContinue(result, false);
        return result;
    }
}

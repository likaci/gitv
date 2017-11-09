package com.gala.video.app.epg.openapi.feature.open;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.qiyi.tv.client.impl.Params.TargetType;

public class OpenActivatePageCommand extends ServerCommand<Intent> {
    private static final String TAG = "OpenActivatePageCommand";

    public OpenActivatePageCommand(Context context) {
        super(context, TargetType.TARGET_ACTIVATE_PAGE, 20001, 30000);
        setNeedNetwork(false);
    }

    public Bundle onProcess(Bundle params) {
        String activateCode = ServerParamsHelper.parseActivateCode(params);
        if (activateCode == null || activateCode.equals("")) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "process() OpenActivatePageCommand activateCode is null!!!");
            }
            return OpenApiResultCreater.createResultBundle(6);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "process() OpenActivatePageCommand activateCode is :" + activateCode);
        }
        GetInterfaceTools.getLoginProvider().startActivateActivityOpenApi(getContext(), activateCode, ServerParamsHelper.parseIntentFlag(params));
        Bundle result = OpenApiResultCreater.createResultBundle(0);
        ServerParamsHelper.setCommandContinue(result, false);
        increaseAccessCount();
        return result;
    }
}

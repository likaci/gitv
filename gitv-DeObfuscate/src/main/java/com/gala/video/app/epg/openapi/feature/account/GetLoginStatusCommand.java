package com.gala.video.app.epg.openapi.feature.account;

import android.content.Context;
import android.os.Bundle;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.qiyi.tv.client.impl.Params.DataType;

public class GetLoginStatusCommand extends ServerCommand<Void> {
    private static final String TAG = "GetLoginStatusCommand";

    public GetLoginStatusCommand(Context context) {
        super(context, 10002, 20003, DataType.DATA_ACCOUNT_STATUS);
    }

    public Bundle onProcess(Bundle params) {
        Bundle bundle = OpenApiResultCreater.createResultBundle(0);
        boolean isLogin = GetInterfaceTools.getIGalaAccountManager().isLogin(getContext());
        if (isLogin) {
            ServerParamsHelper.setLoginStatus(bundle, 1);
        } else {
            ServerParamsHelper.setLoginStatus(bundle, 0);
        }
        increaseAccessCount();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "process() isLogin=" + isLogin);
        }
        return bundle;
    }
}

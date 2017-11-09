package com.gala.video.app.player.openapi.feature.appsetting;

import android.content.Context;
import android.os.Bundle;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.qiyi.tv.client.impl.Params.TargetType;

public class GetStreamTypeCommand extends ServerCommand<Void> {
    private static final String TAG = "GetStreamTypeCommand";

    public GetStreamTypeCommand(Context context) {
        super(context, TargetType.TARGET_STREAM_TYPE, 20003, 30000);
        setNeedNetwork(false);
    }

    protected Bundle onProcess(Bundle params) {
        int sdkType = covertToSdkStreamType(PlayerAppConfig.getDefaultStreamType());
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onProcess() streamType=" + sdkType);
        }
        Bundle result = OpenApiResultCreater.createResultBundle(0);
        ServerParamsHelper.setStreamType(result, sdkType);
        increaseAccessCount();
        return result;
    }

    private int covertToSdkStreamType(int localStreamType) {
        switch (localStreamType) {
            case 2:
                return 0;
            case 3:
                return 1;
            case 4:
                return 2;
            case 5:
                return 3;
            case 6:
                return 4;
            default:
                return -1;
        }
    }
}

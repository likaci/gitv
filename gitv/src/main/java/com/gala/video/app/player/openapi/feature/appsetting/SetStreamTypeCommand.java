package com.gala.video.app.player.openapi.feature.appsetting;

import android.content.Context;
import android.os.Bundle;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.qiyi.tv.client.impl.Params.TargetType;

public class SetStreamTypeCommand extends ServerCommand<Void> {
    private static final String TAG = "SetStreamTypeCommand";

    public SetStreamTypeCommand(Context context) {
        super(context, TargetType.TARGET_STREAM_TYPE, 20002, 30000);
        setNeedNetwork(false);
    }

    protected Bundle onProcess(Bundle params) {
        int streamType = ServerParamsHelper.parseStreamType(params);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onProcess() streamType=" + streamType);
        }
        PlayerAppConfig.setDefaultStreamType(covertToLocalStreamType(streamType));
        increaseAccessCount();
        return OpenApiResultCreater.createResultBundle(0);
    }

    private int covertToLocalStreamType(int sdkStreamType) {
        switch (sdkStreamType) {
            case 0:
                return 2;
            case 1:
                return 3;
            case 2:
                return 4;
            case 3:
                return 5;
            case 4:
                return 6;
            default:
                return -1;
        }
    }
}

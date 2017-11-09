package com.gala.video.app.player.openapi.feature.appsetting;

import android.content.Context;
import android.os.Bundle;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.qiyi.tv.client.impl.Params.TargetType;

public class GetScreenScaleCommand extends ServerCommand<Void> {
    private static final String TAG = "GetScreenScaleCommand";

    public GetScreenScaleCommand(Context context) {
        super(context, TargetType.TARGET_SCREENSCALE, 20003, 30000);
        setNeedNetwork(false);
    }

    protected Bundle onProcess(Bundle params) {
        boolean isFullScreen = PlayerAppConfig.getStretchPlaybackToFullScreen();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onProcess() isFullScreen=" + isFullScreen);
        }
        Bundle result = OpenApiResultCreater.createResultBundle(0);
        ServerParamsHelper.setIsFullScreen(result, isFullScreen);
        increaseAccessCount();
        return result;
    }
}

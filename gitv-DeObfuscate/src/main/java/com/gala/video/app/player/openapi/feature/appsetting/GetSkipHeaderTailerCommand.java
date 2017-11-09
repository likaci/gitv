package com.gala.video.app.player.openapi.feature.appsetting;

import android.content.Context;
import android.os.Bundle;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.qiyi.tv.client.impl.Params.TargetType;

public class GetSkipHeaderTailerCommand extends ServerCommand<Void> {
    private static final String TAG = "GetSkipHeaderTailerCommand";

    public GetSkipHeaderTailerCommand(Context context) {
        super(context, TargetType.TARGET_SKIP_HEADER_TAILER, 20003, 30000);
        setNeedNetwork(false);
    }

    protected Bundle onProcess(Bundle params) {
        boolean isSkip = PlayerAppConfig.shouldSkipVideoHeaderAndTail();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onProcess() isSkip=" + isSkip);
        }
        Bundle result = OpenApiResultCreater.createResultBundle(0);
        ServerParamsHelper.setIsSkipHeaderTailer(result, isSkip);
        increaseAccessCount();
        return result;
    }
}

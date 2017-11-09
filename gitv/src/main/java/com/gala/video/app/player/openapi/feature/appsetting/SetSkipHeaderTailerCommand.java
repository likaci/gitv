package com.gala.video.app.player.openapi.feature.appsetting;

import android.content.Context;
import android.os.Bundle;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.qiyi.tv.client.impl.Params.TargetType;

public class SetSkipHeaderTailerCommand extends ServerCommand<Void> {
    private static final String TAG = "SetSkipHeaderTailerCommand";

    public SetSkipHeaderTailerCommand(Context context) {
        super(context, TargetType.TARGET_SKIP_HEADER_TAILER, 20002, 30000);
        setNeedNetwork(false);
    }

    protected Bundle onProcess(Bundle params) {
        boolean isSkip = ServerParamsHelper.parseIsSkipHeaderTailer(params);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onProcess() isSkip=" + isSkip);
        }
        PlayerAppConfig.setSkipVideoHeaderAndTail(isSkip);
        increaseAccessCount();
        return OpenApiResultCreater.createResultBundle(0);
    }
}

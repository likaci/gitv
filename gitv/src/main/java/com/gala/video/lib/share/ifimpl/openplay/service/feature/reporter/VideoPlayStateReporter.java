package com.gala.video.lib.share.ifimpl.openplay.service.feature.reporter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiManager;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.impl.Params.Extras;

public class VideoPlayStateReporter implements IVideoPlayStateReporter {
    private static final String TAG = "VideoPlayStateReporter";
    private Context mContext;

    public VideoPlayStateReporter(Context context) {
        this.mContext = context;
    }

    public void reportVideoState(int state, Media media) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "reportVideoState(" + state + ", " + media + ")");
        }
        if (OpenApiManager.instance().isAuthSuccess()) {
            Bundle bundle = new Bundle();
            ServerParamsHelper.setPlayState(bundle, state);
            ServerParamsHelper.setMedia(bundle, media);
            Intent intent = new Intent(Extras.ACTION_PLAY_STATE);
            intent.putExtras(bundle);
            this.mContext.sendBroadcast(intent);
        }
    }
}

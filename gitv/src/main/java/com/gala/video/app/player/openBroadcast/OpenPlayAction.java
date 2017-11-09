package com.gala.video.app.player.openBroadcast;

import android.content.Context;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class OpenPlayAction extends BasePlayAndDetailAction {
    private final String TAG = "openplay/broadcast/OpenPlayAction";

    String playAndDetail(Context context, Album album) {
        LogUtils.i("openplay/broadcast/OpenPlayAction", "===playAndDetail===");
        startPlay(context.getApplicationContext(), album);
        return "";
    }
}

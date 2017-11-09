package com.gala.video.lib.share.ifimpl.openplay.service.feature.reporter;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.qiyi.tv.client.data.Media;

public class NullVideoPlayStateReporter implements IVideoPlayStateReporter {
    private static final String TAG = "NullVideoPlayStateReporter";

    public void reportVideoState(int state, Media media) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "NullVideoPlayStateReporter() , state = " + state + " , media = " + media);
        }
    }
}

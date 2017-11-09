package com.gala.video.lib.share.ifimpl.openplay.service.feature.reporter;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.qiyi.tv.client.data.Media;

public class NullHistoryChangedReporter implements IHistoryChangedReporter {
    private static final String TAG = "NullHistoryChangedReporter";

    public void reportHistoryChanged(int action, Media media) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "reportHistoryChanged() , action = " + action + " , media = " + media);
        }
    }
}

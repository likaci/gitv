package com.gala.video.lib.share.ifimpl.openplay.service.feature.reporter;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.qiyi.tv.client.data.Media;

public class NullFavoriteChangedReporter implements IFavoriteChangedReporter {
    private static final String TAG = "NullFavoriteChangedReporter";

    public void reportFavoriteChanged(int action, Media media) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "reportFavoriteChanged() , action = " + action + " , media = " + media);
        }
    }
}

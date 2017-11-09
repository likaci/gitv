package com.gala.video.app.epg.web.subject.play;

import com.gala.video.app.epg.web.model.WebInfo;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class PlayControlFactory {
    private static final String TAG = "EPG/Web/PlayControlFactory";

    public static PlayBaseControl create(WebInfo webInfo) {
        if (webInfo.getType() == 1) {
            LogUtils.m1568d(TAG, "mPageType == WebConstants.TAG_ZHIBO");
            return new LivePlayControl(webInfo);
        }
        LogUtils.m1568d(TAG, "mPageType == WebConstants.TAG_DEF");
        return new DianBoPlayControl(webInfo);
    }
}

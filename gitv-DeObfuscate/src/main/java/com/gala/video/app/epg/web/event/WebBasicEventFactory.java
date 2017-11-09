package com.gala.video.app.epg.web.event;

import com.gala.video.lib.share.ifimpl.web.provider.WebPluginProvider;
import com.gala.video.lib.share.project.Project;
import com.gala.video.webview.event.WebBaseEvent;

public class WebBasicEventFactory {
    public static WebBaseEvent create(boolean isAccelerateExclude) {
        if (Project.getInstance().getControl().isOpenCrossWalk() && WebPluginProvider.getInstance().isAlready()) {
            return new XWalkViewEvent();
        }
        return new IGaLaWebViewEvent(isAccelerateExclude);
    }

    public static WebBaseEvent createIGaLaWebViewEvent() {
        return new IGaLaWebViewEvent();
    }
}

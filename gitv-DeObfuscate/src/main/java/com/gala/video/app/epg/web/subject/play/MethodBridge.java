package com.gala.video.app.epg.web.subject.play;

import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.webview.event.WebBaseEvent;

public class MethodBridge implements IMethodBridge {
    private WebBaseEvent mWebBasicEvent;

    public MethodBridge(WebBaseEvent webBasicEvent) {
        this.mWebBasicEvent = webBasicEvent;
    }

    public void onVideoSwitched(String info) {
        loadJsMethod(String.format(WebConstants.JS_onVideoPluginSwitched, new Object[]{info}));
    }

    public void onPlaybackFinished() {
        loadJsMethod(WebConstants.JS_onVideoPluginPlayFinished);
    }

    public void onScreenModeSwitched(int mode) {
        loadJsMethod(String.format(WebConstants.JS_onScreenModeSwitched, new Object[]{Integer.valueOf(mode)}));
    }

    public void refreshVipBuyButton(int temp) {
        loadJsMethod(String.format(WebConstants.JS_onBuyIsVisible, new Object[]{Integer.valueOf(temp)}));
    }

    public void onBackSubject() {
        loadJsMethod(WebConstants.JS_onBackSubject);
    }

    private void loadJsMethod(String url) {
        if (this.mWebBasicEvent != null) {
            this.mWebBasicEvent.loadJsMethod(url);
        }
    }
}

package com.gala.video.app.epg.web;

import android.view.KeyEvent;
import com.gala.video.app.epg.web.utils.WebDataUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebViewDataImpl;

public class WebCommonActivity extends WebBaseActivity {
    private static final String TAG = "EPG/web/WebCommonActivity";

    protected String getWebUrl() {
        CharSequence pageUrl = this.mBaseWebInfo.getPageUrl();
        return !StringUtils.isEmpty(pageUrl) ? pageUrl : WebDataUtils.getWebUrl(this.mBaseWebInfo.getCurrentPageType());
    }

    protected WebViewDataImpl generateJsonParams() {
        return WebDataUtils.generateJsonParams(super.generateJsonParams(), this.mBaseWebInfo, this.mBaseWebInfo.getCurrentPageType());
    }

    public boolean handleKeyEvent(KeyEvent event) {
        return super.handleJsKeyEvent(event);
    }
}

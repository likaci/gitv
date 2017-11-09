package com.gala.video.app.epg.web.model;

import android.content.Context;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebViewDataImpl;

public class WebBaseTypeParams {
    private Context mContext;
    private String mJsonString;
    private WebViewDataImpl mWebViewDataImpl;

    public String getJsonString() {
        return this.mJsonString;
    }

    public void setJsonString(String mJsonString) {
        this.mJsonString = mJsonString;
    }

    public void setWebViewData(WebViewDataImpl webViewDataImpl) {
        this.mWebViewDataImpl = webViewDataImpl;
    }

    public WebViewDataImpl getWebViewDataImpl() {
        return this.mWebViewDataImpl;
    }

    public Context getContext() {
        return this.mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }
}

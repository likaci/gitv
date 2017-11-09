package com.gala.video.lib.share.ifimpl.web.provider;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.IWebJsonParmsProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.IWebJsonParmsProvider.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebViewDataImpl;

public class WebJsonParmsProvider extends Wrapper implements IWebJsonParmsProvider {
    private static final String TAG = "EPG/web/WebJsonParmsProvider";
    private WebViewDataImpl mWebViewDataImpl;

    private void initDefault() {
        if (this.mWebViewDataImpl == null) {
            LogUtils.m1571e(TAG, "initDefault() ->  mWebViewDataImpl == null");
            this.mWebViewDataImpl = new WebViewDataImpl();
            this.mWebViewDataImpl.init();
        }
        this.mWebViewDataImpl.resetTVApi();
    }

    public WebViewDataImpl getDefaultDataImpl() {
        initDefault();
        this.mWebViewDataImpl.clearData();
        this.mWebViewDataImpl.initUserJsonData();
        this.mWebViewDataImpl.initDynamicJsonData();
        return this.mWebViewDataImpl;
    }

    public String getJson() {
        return getDefaultDataImpl().getJson();
    }
}

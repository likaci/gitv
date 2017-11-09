package com.gala.video.app.epg.web.core;

import android.app.Activity;
import android.content.Context;
import com.gala.video.app.epg.web.function.WebFunContract.IFunBase;
import com.gala.video.app.epg.web.utils.WebDataUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebViewDataImpl;

public class FunctionBase implements IFunBase {
    private static final String TAG = "EPG/web/FunctionBase";
    private Context mContext;
    private WebViewDataImpl mWebViewDataImpl;

    public FunctionBase(WebViewDataImpl webViewDataImpl) {
        this.mWebViewDataImpl = webViewDataImpl;
    }

    public FunctionBase(Context context, WebViewDataImpl webViewDataImpl) {
        this(webViewDataImpl);
        this.mContext = context;
    }

    public String getParams() {
        LogUtils.m1568d(TAG, "H5 getParams");
        return this.mWebViewDataImpl.getJson();
    }

    public String getUserInfoParams(String info) {
        LogUtils.m1568d(TAG, "H5 getUserInfoParams info:" + info);
        return WebDataUtils.getInfoJson();
    }

    public String getSupportMethodList(String paramJson) {
        return null;
    }

    public void finish() {
        LogUtils.m1568d(TAG, "finish");
        if (this.mContext instanceof Activity) {
            LogUtils.m1568d(TAG, "finish start");
            ((Activity) this.mContext).finish();
            LogUtils.m1568d(TAG, "finish end");
        }
    }
}

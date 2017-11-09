package com.gala.video.app.epg.web.core;

import android.util.Log;
import android.webkit.JavascriptInterface;
import com.gala.video.app.epg.web.function.WebFunContract.IFunLoad;
import com.gala.video.webview.widget.AbsWebView.IWebViewLoad;

public class FunctionLoad implements IFunLoad {
    protected final String TAG = TAG();
    private IWebViewLoad mLoadCallback;

    public FunctionLoad(IWebViewLoad uiCallback) {
        this.mLoadCallback = uiCallback;
    }

    protected String TAG() {
        return "EPG/web/FunctionLoad";
    }

    @JavascriptInterface
    public void onLoadCompleted() {
        Log.d(this.TAG, "H5 onLoadCompleted");
        if (this.mLoadCallback != null) {
            this.mLoadCallback.onWebViewLoadCompleted();
        }
    }

    @JavascriptInterface
    public void onLoadFailed(String errorInfo) {
        Log.e(this.TAG, "H5 onLoadFailed errorInfo:" + errorInfo);
        if (this.mLoadCallback != null) {
            this.mLoadCallback.onWebViewLoadFailed(errorInfo);
        }
    }
}

package com.gala.video.webview.core;

import android.util.Log;
import android.webkit.JavascriptInterface;
import com.gala.video.webview.core.WebSDKFunContract.IFunLoad;
import com.gala.video.webview.widget.AbsWebView.IWebViewLoad;

public class FunctionSDKLoad implements IFunLoad {
    protected final String TAG = TAG();
    private IWebViewLoad mLoadCallback;

    public FunctionSDKLoad(IWebViewLoad uiCallback) {
        this.mLoadCallback = uiCallback;
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

    protected String TAG() {
        return "EPG/sdk/web/FunctionLoad";
    }
}

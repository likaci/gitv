package com.gala.video.webview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import com.gala.video.webview.utils.Utils;

public class SafeWebView extends WebView {
    private static final String TAG = "EPG/SafeWebView";
    protected boolean mIsDestroy = false;
    private boolean mIsSdk = true;

    public SafeWebView(Context context) {
        super(context);
        removeSearchBoxImpl();
    }

    public SafeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        removeSearchBoxImpl();
    }

    public SafeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        removeSearchBoxImpl();
    }

    private void removeSearchBoxImpl() {
        if (Utils.hasHoneycomb() && !Utils.hasJellyBeanMR1()) {
            super.removeJavascriptInterface("searchBoxJavaBridge_");
            super.removeJavascriptInterface("accessibility");
            super.removeJavascriptInterface("accessibilityTraversal");
        }
    }

    public void destroy() {
        this.mIsDestroy = true;
        super.destroy();
    }

    public void loadUrl(String url) {
        Log.i(TAG, "loadUrl mIsSdk:" + this.mIsSdk);
        Log.i(TAG, "loadUrl url:" + url);
        if (this.mIsSdk) {
            super.loadUrl(url);
        } else if (this.mIsDestroy) {
            Log.i(TAG, "loadUrl mIsDestroy return!");
        } else {
            super.loadUrl(url);
        }
    }

    public void setSdk(boolean sdk) {
        this.mIsSdk = sdk;
    }
}

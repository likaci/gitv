package com.gala.video.webview.event;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.gala.video.lib.framework.core.network.download.core.IGalaDownloadParameter;
import com.gala.video.webview.utils.Utils;
import com.gala.video.webview.utils.WebSDKConstants;
import com.gala.video.webview.utils.WebSDKDataUtils;
import com.gala.video.webview.widget.BaseWebView;
import com.gala.video.webview.widget.WebChromeClientBw;
import com.gala.video.webview.widget.WebViewClientBw;
import java.io.IOException;

public class WebViewEvent extends WebBaseEvent {
    private static final String TAG = "EPG/web/WebViewEvent";
    private boolean mIsDisableAccelerate;
    private boolean mReceivedError;
    private boolean mShouldOverrideUrlLoading;
    private BaseWebView mWebView;
    private SafeWebViewClient mWebViewClient;

    public class SafeWebViewClient extends WebViewClientBw {
        public SafeWebViewClient(BaseWebView webViewEx) {
            super(webViewEx);
        }
    }

    public class SafeWebChromeClient extends WebChromeClientBw {
        public SafeWebChromeClient(BaseWebView webView) {
            super(webView);
        }

        @Deprecated
        public void onConsoleMessage(String message, int lineNumber, String sourceID) {
            Log.d("Subject_HTML_Client", message + " sourceID " + sourceID);
        }
    }

    public WebViewEvent() {
        this.mShouldOverrideUrlLoading = false;
        this.mReceivedError = false;
        this.mWebViewClient = new SafeWebViewClient(this.mWebView) {
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return WebViewEvent.this.handleResourceRequest(super.shouldInterceptRequest(view, url), url);
            }

            @SuppressLint({"NewApi"})
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                WebResourceResponse response = super.shouldInterceptRequest(view, request);
                if (VERSION.SDK_INT < 21 || request == null || request.getUrl() == null) {
                    return response;
                }
                return WebViewEvent.this.getWebResponse(response, request.getUrl().toString());
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(WebViewEvent.TAG, "shouldOverrideUrlLoading url:" + url);
                if (url.startsWith("androiduri://")) {
                    WebViewEvent.this.onClickWebURI(url);
                } else {
                    WebViewEvent.this.mShouldOverrideUrlLoading = true;
                    view.loadUrl(url);
                }
                return true;
            }

            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Log.d(WebViewEvent.TAG, "onLoadResource url:" + url);
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.e(WebViewEvent.TAG, "onReceivedSslError:" + error);
                if (WebSDKDataUtils.isSSLProceed(error)) {
                    Log.d(WebViewEvent.TAG, "onReceivedSslError -> need Proceed");
                    handler.proceed();
                    return;
                }
                super.onReceivedSslError(view, handler, error);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(WebViewEvent.TAG, "onPageStarted url:" + url);
                WebViewEvent.this.mShouldOverrideUrlLoading = false;
                if (!WebViewEvent.this.mReceivedError && WebViewEvent.this.mOnLoadListener != null) {
                    WebViewEvent.this.mOnLoadListener.onLoading();
                }
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(WebViewEvent.TAG, "onPageFinished url:" + url);
                Log.d(WebViewEvent.TAG, "onPageFinished mShouldOverrideUrlLoading:" + WebViewEvent.this.mShouldOverrideUrlLoading);
                if (!WebViewEvent.this.mShouldOverrideUrlLoading && !WebViewEvent.this.mReceivedError) {
                    if (WebViewEvent.this.mOnLoadListener != null) {
                        WebViewEvent.this.mOnLoadListener.onSuccess();
                    }
                    if (WebViewEvent.this.mWebView != null && WebViewEvent.this.mWebView.getSettings() != null) {
                        WebSettings settings = WebViewEvent.this.mWebView.getSettings();
                        if (!settings.getLoadsImagesAutomatically()) {
                            settings.setLoadsImagesAutomatically(true);
                        }
                    }
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (-6 == errorCode || -1 == errorCode || -2 == errorCode || -8 == errorCode) {
                    WebViewEvent.this.mReceivedError = true;
                }
                Log.e(WebViewEvent.TAG, "onReceivedError, errorCode:" + errorCode + ", description:" + description);
                if (WebViewEvent.this.mOnLoadListener != null) {
                    Log.d(WebViewEvent.TAG, "onReceivedError mOnLoadListener != null");
                    WebViewEvent.this.mOnLoadListener.onError();
                }
            }
        };
        Log.d(TAG, "WebEvent Type: WebViewEvent");
    }

    public WebViewEvent(boolean isAccelerateExclude) {
        this();
        this.mIsDisableAccelerate = isAccelerateExclude;
    }

    public int getEventType() {
        return 0;
    }

    public boolean isAlready() {
        return this.mWebView != null;
    }

    public void initView() {
        if (isAlready()) {
            this.mWebView.setBackgroundColor(0);
            this.mWebView.setWebViewClient(this.mWebViewClient);
            this.mWebView.setWebChromeClient(new SafeWebChromeClient(this.mWebView));
            Utils.disableAccessibility(this.mContext.getApplicationContext());
            WebSettings webSettings = this.mWebView.getSettings();
            setupWebSettings(webSettings);
            safeSetting(webSettings);
            initLayerType();
        }
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    protected void setupWebSettings(WebSettings webSettings) {
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(Utils.hasKitkat());
        webSettings.setUserAgentString("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36");
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setCacheMode(-1);
        webSettings.setAppCacheMaxSize(IGalaDownloadParameter.FILE_SIZE_LIMIT);
        if (!Utils.hasKitkat()) {
            webSettings.setDatabasePath(this.mContext.getApplicationContext().getDatabasePath("h5_database").getPath());
        }
    }

    private void safeSetting(WebSettings webSettings) {
        webSettings.setSavePassword(false);
        webSettings.setAllowFileAccess(false);
        if (Utils.hasJellyBean()) {
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
    }

    private void initLayerType() {
        if (isOpenHardWardEnable()) {
            Log.d(TAG, "initLayerType() -> isOpenHardWardEnable() mIsDisableAccelerate:" + this.mIsDisableAccelerate);
            if (this.mIsDisableAccelerate) {
                setSoftwareLayertype();
                return;
            }
            return;
        }
        Log.d(TAG, "initLayerType() -> js close hardware layer type!");
        setSoftwareLayertype();
    }

    public void setSoftwareLayertype() {
        if (isAlready()) {
            this.mWebView.setLayerType(1, null);
        }
    }

    protected boolean isOpenHardWardEnable() {
        return true;
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        if (this.mRootLayout != null) {
            this.mRootLayout.removeView(this.mWebView);
        }
        if (isAlready()) {
            this.mWebView.clearMatches();
            this.mWebView.clearHistory();
            this.mWebView.clearSslPreferences();
            this.mWebView.loadUrl("about:blank");
            this.mWebView.removeAllViews();
            if (VERSION.SDK_INT < 18) {
                this.mWebView.removeJavascriptInterface(getJSInterfaceName());
            }
            this.mWebView.destroy();
        }
        this.mWebView = null;
        if (this.mWebViewClient != null) {
            this.mWebViewClient = null;
        }
        super.onDestroy();
    }

    public void onHide() {
        if (isAlready()) {
            this.mWebView.setVisibility(8);
        }
    }

    public void loadJsMethod(String jsUrl) {
        if (isAlready()) {
            this.mWebView.loadUrl(jsUrl);
        }
    }

    @SuppressLint({"JavascriptInterface"})
    public void addJavascriptInterface(Object object) {
        if (object != null && this.mWebView != null) {
            this.mWebView.addJavascriptInterface(object, getJSInterfaceName());
        }
    }

    public void showErrorView() {
        if (isAlready()) {
            this.mWebView.setFocusable(false);
            this.mWebView.setVisibility(8);
        }
    }

    public void onResume() {
        if (isAlready()) {
            this.mWebView.onResume();
        }
    }

    public void onPause() {
        if (isAlready()) {
            this.mWebView.onPause();
        }
    }

    private WebResourceResponse getWebResponse(WebResourceResponse response, String url) {
        if (!Utils.hasLollipop()) {
            return response;
        }
        if (isShouldChangeFont(url)) {
            Log.d(TAG, "Webview shouldInterceptLoadRequest requestURI:" + url);
            try {
                response = new WebResourceResponse("application/x-font-ttf", "UTF-8", this.mContext.getApplicationContext().getAssets().open(url.substring(url.indexOf(WebSDKConstants.INJECTTION_TTF) + WebSDKConstants.INJECTTION_TTF.length(), url.length())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    private WebResourceResponse handleResourceRequest(WebResourceResponse response, String url) {
        if (!Utils.isIcoResource(url)) {
            return getWebResponse(response, url);
        }
        try {
            return new WebResourceResponse("image/ico", "UTF-8", null);
        } catch (Exception e) {
            e.printStackTrace();
            return response;
        }
    }

    public boolean canGoBack() {
        return isAlready() ? this.mWebView.canGoBack() : false;
    }

    public void goBack() {
        if (isAlready()) {
            this.mWebView.goBack();
        }
    }

    public View getView() {
        if (this.mWebView == null) {
            this.mWebView = new BaseWebView(this.mContext.getApplicationContext());
        }
        return this.mWebView;
    }

    public void showLoading() {
        this.mWebView.setVisibility(8);
    }

    public void showResult() {
        this.mWebView.setVisibility(0);
        if (this.mWebUrlType == 1) {
            this.mWebView.setFocusable(true);
            this.mWebView.requestFocus();
            return;
        }
        this.mWebView.setFocusable(false);
    }

    public void showResult(boolean focusable) {
        Log.i(TAG, "showResult focusable: " + focusable);
        if (focusable) {
            showResult();
            return;
        }
        Log.i(TAG, "showResult: 不需要获取焦点");
        this.mWebView.setVisibility(0);
    }

    public void requestWebFocus() {
        if (this.mWebView != null) {
            this.mWebView.setFocusable(true);
            this.mWebView.requestFocus();
        }
    }

    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
        if (this.mWebView != null) {
            this.mWebView.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
        }
    }

    public void setHorizontalScrollBar(boolean horizontalScrollBarEnabled) {
        if (this.mWebView != null) {
            this.mWebView.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
        }
    }

    public void setSdk(boolean sdk) {
        if (this.mWebView != null) {
            this.mWebView.setSdk(sdk);
        }
    }

    public void setHVScrollBar(boolean vHScrollBarEnabled) {
        setVerticalScrollBarEnabled(vHScrollBarEnabled);
        setHorizontalScrollBar(vHScrollBarEnabled);
    }

    protected void onClickWebURI(String url) {
    }
}

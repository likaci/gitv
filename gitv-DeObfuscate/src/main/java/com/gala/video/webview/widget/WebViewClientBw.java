package com.gala.video.webview.widget;

import android.graphics.Bitmap;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewClientBw extends WebViewClient {
    private BaseWebView mBaseWebView;

    public WebViewClientBw(BaseWebView webView) {
        this.mBaseWebView = webView;
    }

    public void onLoadResource(WebView view, String url) {
        if (hasWebViewEx()) {
            this.mBaseWebView.injectJavascriptInterfaces(view);
        }
        super.onLoadResource(view, url);
    }

    @Deprecated
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        if (hasWebViewEx()) {
            this.mBaseWebView.injectJavascriptInterfaces(view);
        }
        return super.shouldInterceptRequest(view, url);
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (hasWebViewEx()) {
            this.mBaseWebView.injectJavascriptInterfaces(view);
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (hasWebViewEx()) {
            this.mBaseWebView.injectJavascriptInterfaces(view);
        }
        super.onPageStarted(view, url, favicon);
    }

    public void onPageFinished(WebView view, String url) {
        if (hasWebViewEx()) {
            this.mBaseWebView.injectJavascriptInterfaces(view);
        }
        super.onPageFinished(view, url);
    }

    private boolean hasWebViewEx() {
        return this.mBaseWebView != null;
    }
}

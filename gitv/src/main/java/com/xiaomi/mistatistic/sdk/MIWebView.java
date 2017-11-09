package com.xiaomi.mistatistic.sdk;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MIWebView extends WebView {
    public void setWebViewClient(WebViewClient webViewClient) {
        super.setWebViewClient(new b(this, webViewClient));
    }
}

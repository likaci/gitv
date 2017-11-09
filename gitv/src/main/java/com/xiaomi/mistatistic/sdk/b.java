package com.xiaomi.mistatistic.sdk;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.xiaomi.mistatistic.sdk.controller.j;
import com.xiaomi.mistatistic.sdk.data.HttpEvent;
import java.util.HashMap;
import java.util.Map;

class b extends WebViewClient {
    final /* synthetic */ MIWebView a;
    private WebViewClient b;
    private Map c = new HashMap();

    public b(MIWebView mIWebView, WebViewClient webViewClient) {
        this.a = mIWebView;
        this.b = webViewClient;
    }

    public void doUpdateVisitedHistory(WebView webView, String str, boolean z) {
        if (this.b != null) {
            this.b.doUpdateVisitedHistory(webView, str, z);
        } else {
            super.doUpdateVisitedHistory(webView, str, z);
        }
    }

    public void onFormResubmission(WebView webView, Message message, Message message2) {
        if (this.b != null) {
            this.b.onFormResubmission(webView, message, message2);
        } else {
            super.onFormResubmission(webView, message, message2);
        }
    }

    public void onLoadResource(WebView webView, String str) {
        if (this.b != null) {
            this.b.onLoadResource(webView, str);
        } else {
            super.onLoadResource(webView, str);
        }
    }

    public void onPageFinished(WebView webView, String str) {
        if (this.b != null) {
            this.b.onPageFinished(webView, str);
        } else {
            super.onPageFinished(webView, str);
        }
        Long l = (Long) this.c.get(str);
        if (l != null) {
            j.a().a(new HttpEvent(str, System.currentTimeMillis() - l.longValue()));
        }
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        if (this.b != null) {
            this.b.onPageStarted(webView, str, bitmap);
        } else {
            super.onPageStarted(webView, str, bitmap);
        }
        this.c.put(str, Long.valueOf(System.currentTimeMillis()));
    }

    public void onReceivedError(WebView webView, int i, String str, String str2) {
        if (this.b != null) {
            this.b.onReceivedError(webView, i, str, str2);
        } else {
            super.onReceivedError(webView, i, str, str2);
        }
        Long l = (Long) this.c.get(str2);
        if (l != null) {
            j.a().a(new HttpEvent(str2, System.currentTimeMillis() - l.longValue(), (long) i));
        }
    }

    public void onReceivedHttpAuthRequest(WebView webView, HttpAuthHandler httpAuthHandler, String str, String str2) {
        if (this.b != null) {
            this.b.onReceivedHttpAuthRequest(webView, httpAuthHandler, str, str2);
        } else {
            super.onReceivedHttpAuthRequest(webView, httpAuthHandler, str, str2);
        }
    }

    public void onReceivedLoginRequest(WebView webView, String str, String str2, String str3) {
        if (this.b != null) {
            this.b.onReceivedLoginRequest(webView, str, str2, str3);
        } else {
            super.onReceivedLoginRequest(webView, str, str2, str3);
        }
    }

    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        if (this.b != null) {
            this.b.onReceivedSslError(webView, sslErrorHandler, sslError);
        } else {
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
        }
    }

    public void onScaleChanged(WebView webView, float f, float f2) {
        if (this.b != null) {
            this.b.onScaleChanged(webView, f, f2);
        } else {
            super.onScaleChanged(webView, f, f2);
        }
    }

    public void onTooManyRedirects(WebView webView, Message message, Message message2) {
        if (this.b != null) {
            this.b.onTooManyRedirects(webView, message, message2);
        } else {
            super.onTooManyRedirects(webView, message, message2);
        }
    }

    public void onUnhandledKeyEvent(WebView webView, KeyEvent keyEvent) {
        if (this.b != null) {
            this.b.onUnhandledKeyEvent(webView, keyEvent);
        } else {
            super.onUnhandledKeyEvent(webView, keyEvent);
        }
    }

    public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
        return this.b != null ? this.b.shouldInterceptRequest(webView, str) : super.shouldInterceptRequest(webView, str);
    }

    public boolean shouldOverrideKeyEvent(WebView webView, KeyEvent keyEvent) {
        return this.b != null ? this.b.shouldOverrideKeyEvent(webView, keyEvent) : super.shouldOverrideKeyEvent(webView, keyEvent);
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        return this.b != null ? this.b.shouldOverrideUrlLoading(webView, str) : super.shouldOverrideUrlLoading(webView, str);
    }
}

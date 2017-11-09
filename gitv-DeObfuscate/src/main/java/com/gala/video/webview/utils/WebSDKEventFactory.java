package com.gala.video.webview.utils;

import com.gala.video.webview.event.WebBaseEvent;
import com.gala.video.webview.event.WebViewEvent;
import com.gala.video.webview.event.WebViewJSEvent;

public class WebSDKEventFactory {
    public static WebBaseEvent createWebViewEvent() {
        return new WebViewEvent();
    }

    public static WebBaseEvent createWebViewJSEvent() {
        return new WebViewJSEvent();
    }
}

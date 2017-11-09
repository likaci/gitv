package com.gala.video.webview.widget;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import java.util.Map;
import org.json.JSONObject;

public class WebChromeClientBw extends WebChromeClient {
    private static final String TAG = "WebChromeClientBw";
    private BaseWebView mBaseWebView;
    private Map<String, JsInterfacehelper> mJsInterfacehelperMap = this.mBaseWebView.getJsCallJava();

    public WebChromeClientBw(BaseWebView webView) {
        this.mBaseWebView = webView;
        if (this.mBaseWebView != null) {
            this.mBaseWebView.injectJavascriptInterfaces(this.mBaseWebView);
        }
    }

    public final void onProgressChanged(WebView view, int newProgress) {
        if (this.mBaseWebView != null) {
            this.mBaseWebView.injectJavascriptInterfaces(view);
        }
        super.onProgressChanged(view, newProgress);
    }

    public final boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        if (this.mJsInterfacehelperMap == null || !JsInterfacehelper.isSafeWebViewCallMsg(message)) {
            Log.e(TAG, "Js prompt mJsInterfacehelperMap is null or is no SafeWebViewCallMsg");
            return super.onJsPrompt(view, url, message, defaultValue, result);
        } else if (!(view instanceof BaseWebView)) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        } else {
            JSONObject jsPromptObject = JsInterfacehelper.getMessageJSONObject(message);
            String interfaceName = JsInterfacehelper.getInterfacedName(jsPromptObject);
            if (!TextUtils.isEmpty(interfaceName)) {
                JsInterfacehelper jsCallJava = (JsInterfacehelper) this.mJsInterfacehelperMap.get(interfaceName);
                if (jsCallJava != null) {
                    result.confirm(jsCallJava.call(view, jsPromptObject));
                }
            }
            return true;
        }
    }
}

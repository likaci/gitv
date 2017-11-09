package com.gala.video.webview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import com.gala.video.webview.utils.Utils;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BaseWebView extends SafeWebView {
    private static final String TAG = "BaseWebView";
    private Map<String, JsInterfacehelper> mJsInterfacehelperMap;
    private String mJsStringCache = null;

    public BaseWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseWebView(Context context) {
        super(context);
    }

    public void systemAddJavascriptInterface(Object obj, String interfaceName) {
        super.addJavascriptInterface(obj, interfaceName);
    }

    public void addJavascriptInterface(Object obj, String interfaceName) {
        if (Utils.hasJellyBeanMR1()) {
            Log.e(TAG, "addJavascriptInterface 4.2 up");
            systemAddJavascriptInterface(obj, interfaceName);
        } else if (Utils.hasClassEx()) {
            Log.e(TAG, "addJavascriptInterface4.2 below");
            if (this.mJsInterfacehelperMap == null) {
                this.mJsInterfacehelperMap = new HashMap();
            }
            this.mJsInterfacehelperMap.put(interfaceName, new JsInterfacehelper(interfaceName, obj));
        } else {
            Log.e(TAG, "addJavascriptInterface !hasClassEx");
            systemAddJavascriptInterface(obj, interfaceName);
        }
    }

    public Map<String, JsInterfacehelper> getJsCallJava() {
        return this.mJsInterfacehelperMap;
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public void onResume() {
        super.onResume();
        if (getSettings() != null) {
            getSettings().setJavaScriptEnabled(true);
        }
    }

    public void onPause() {
        super.onPause();
        if (getSettings() != null) {
            getSettings().setJavaScriptEnabled(false);
        }
    }

    public void removeJavascriptInterface(String interfaceName) {
        if (Utils.hasJellyBeanMR1()) {
            super.removeJavascriptInterface(interfaceName);
        } else if (this.mJsInterfacehelperMap != null) {
            this.mJsInterfacehelperMap.remove(interfaceName);
            for (Entry<String, JsInterfacehelper> entry : this.mJsInterfacehelperMap.entrySet()) {
                if (entry.getValue() != null) {
                    loadUrl(((JsInterfacehelper) entry.getValue()).getInterfaceJS());
                }
            }
        }
    }

    void injectJavascriptInterfaces(WebView webView) {
        if (webView instanceof BaseWebView) {
            injectJavascriptInterfaces();
        }
    }

    private void injectJavascriptInterfaces() {
        if (!TextUtils.isEmpty(this.mJsStringCache)) {
            Log.e(TAG, "mJsStringCache is not null");
            loadUrl(this.mJsStringCache);
        } else if (this.mJsInterfacehelperMap != null) {
            for (Entry<String, JsInterfacehelper> entry : this.mJsInterfacehelperMap.entrySet()) {
                this.mJsStringCache = ((JsInterfacehelper) entry.getValue()).getInterfaceJS();
                Log.e(TAG, "injectJavascriptInterfaces mJsStringCache");
                loadUrl(this.mJsStringCache);
            }
        }
    }

    public void destroy() {
        super.destroy();
        if (this.mJsInterfacehelperMap != null) {
            this.mJsInterfacehelperMap.clear();
        }
    }
}

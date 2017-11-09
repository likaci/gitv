package com.gala.video.webview.event;

import android.util.Log;
import android.view.KeyEvent;
import com.gala.video.webview.utils.WebSDKConstants;

public class WebViewJSEvent extends WebViewEvent {
    private static final String TAG = "EPG/web/WebViewJSEvent";

    public boolean handleJsKeyEvent(KeyEvent event) {
        Log.i(TAG, "handleJsKeyEvent WebViewJSEvent");
        switch (event.getKeyCode()) {
            case 19:
                loadMethod(WebSDKConstants.JS_onKeyUp);
                return true;
            case 20:
                loadMethod(WebSDKConstants.JS_onKeyDown);
                return true;
            case 21:
                loadMethod(WebSDKConstants.JS_onKeyLeft);
                return true;
            case 22:
                loadMethod(WebSDKConstants.JS_onKeyRight);
                return true;
            case 23:
            case 66:
                loadMethod(WebSDKConstants.JS_onKeyEnter);
                return true;
            default:
                return super.handleJsKeyEvent(event);
        }
    }
}

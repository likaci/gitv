package com.gala.video.app.epg.web.event;

import android.view.KeyEvent;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.utils.MemoryLevelInfo;
import com.gala.video.webview.utils.WebSDKConstants;

public class IGaLaWebViewEvent extends WebViewEventBrage {
    private static final String TAG = "EPG/web/IGaLaWebViewEvent";

    public IGaLaWebViewEvent(boolean isAccelerateExclude) {
        super(isAccelerateExclude);
    }

    protected boolean isOpenHardWardEnable() {
        return GetInterfaceTools.getIJSConfigDataProvider().getJSConfigResult().isOpenHardWardEnable();
    }

    protected void onClickWebURI(String url) {
        GetInterfaceTools.getWebEntry().onClickWebURI(AppRuntimeEnv.get().getApplicationContext(), url);
    }

    protected boolean isHighConfigDevice() {
        boolean isHigh = MemoryLevelInfo.isHighConfigDevice();
        LogUtils.m1568d(TAG, "isHighConfigDevice:" + isHigh);
        return isHigh;
    }

    public boolean handleJsKeyEvent(KeyEvent event) {
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
            case 82:
                loadMethod(WebSDKConstants.JS_onKeyMenu);
                return true;
            default:
                return super.handleJsKeyEvent(event);
        }
    }
}

package com.gala.video.app.epg.web.event;

import android.net.http.SslError;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebResourceResponse;
import com.gala.video.crosswalkinterface.IXWalkPlugin;
import com.gala.video.crosswalkinterface.IXWalkResourceClient;
import com.gala.video.crosswalkinterface.IXWalkUIClient;
import com.gala.video.crosswalkinterface.IXWalkView;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.ifimpl.web.provider.WebPluginProvider;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.utils.MemoryLevelInfo;
import com.gala.video.webview.event.WebBaseEvent;
import com.gala.video.webview.utils.WebSDKConstants;
import com.gala.video.webview.utils.WebSDKDataUtils;
import java.io.IOException;
import org.cybergarage.soap.SOAP;

public class XWalkViewEvent extends WebBaseEvent {
    private static final String TAG = "EPG/web/XWalkViewEvent";
    private IXWalkPlugin mXWalkPlugin = WebPluginProvider.getInstance().getIXWalkPlugin();
    private IXWalkResourceClient mXWalkResourceClient = new IXWalkResourceClient() {
        public void onLoadStarted(IXWalkView view, String url) {
            LogUtils.d(XWalkViewEvent.TAG, "onLoadStarted() -> url:" + url);
        }

        public void onLoadFinished(IXWalkView view, String url) {
            LogUtils.d(XWalkViewEvent.TAG, "onLoadFinished() -> url:" + url);
        }

        public void onDocumentLoadedInFrame(IXWalkView view, long frameId) {
        }

        public void onProgressChanged(IXWalkView view, int progressInPercent) {
        }

        public void onReceivedLoadError(IXWalkView view, int errorCode, String description, String failingUrl) {
            LogUtils.d(XWalkViewEvent.TAG, "onReceivedLoadError() -> failingUrl:" + failingUrl + ",errorCode:" + errorCode);
        }

        public void onReceivedSslError(IXWalkView view, ValueCallback<Boolean> callback, SslError error) {
            LogUtils.e(XWalkViewEvent.TAG, "onReceivedSslError:" + error);
            if (WebSDKDataUtils.isSSLProceed(error)) {
                LogUtils.d(XWalkViewEvent.TAG, "onReceivedSslError -> need Proceed");
                callback.onReceiveValue(Boolean.valueOf(true));
                return;
            }
            callback.onReceiveValue(Boolean.valueOf(false));
        }

        public WebResourceResponse shouldInterceptLoadRequest(IXWalkView view, String url) {
            LogUtils.d(XWalkViewEvent.TAG, "XwalkView shouldInterceptLoadRequest url:" + url);
            if (!XWalkViewEvent.this.isShouldChangeFont(url)) {
                return null;
            }
            try {
                return new WebResourceResponse("application/x-font-ttf", "UTF-8", XWalkViewEvent.this.mContext.getAssets().open(url.substring(url.indexOf(WebSDKConstants.INJECTTION_TTF) + WebSDKConstants.INJECTTION_TTF.length(), url.length())));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public boolean shouldOverrideUrlLoading(IXWalkView view, String url) {
            LogUtils.d(XWalkViewEvent.TAG, "shouldOverrideUrlLoading url:" + url);
            if (!url.startsWith("androiduri://")) {
                return false;
            }
            GetInterfaceTools.getWebEntry().onClickWebURI(AppRuntimeEnv.get().getApplicationContext(), url);
            return true;
        }
    };
    private IXWalkUIClient mXWalkUIClient = new IXWalkUIClient() {
        public boolean shouldOverrideKeyEvent(IXWalkView view, KeyEvent event) {
            LogUtils.d(XWalkViewEvent.TAG, "shouldOverrideKeyEvent");
            return false;
        }

        public void onFullscreenToggled(IXWalkView view, boolean enterFullscreen) {
            LogUtils.d(XWalkViewEvent.TAG, "onFullscreenToggled");
        }

        public boolean onConsoleMessage(IXWalkView view, String message, int lineNumber, String sourceId) {
            LogUtils.d("Subject_HTML_Client", "line " + lineNumber + " of " + sourceId + SOAP.DELIM + message);
            return false;
        }

        public void onPageLoadStarted(IXWalkView view, String url) {
            LogUtils.d(XWalkViewEvent.TAG, "onPageLoadStarted url:" + url);
            if (XWalkViewEvent.this.mOnLoadListener != null) {
                XWalkViewEvent.this.mOnLoadListener.onLoading();
            }
        }

        public void onPageLoadStopped(IXWalkView view, String url, String status) {
            LogUtils.d(XWalkViewEvent.TAG, "onPageLoadStopped url:" + url + ",status:" + status);
            if (XWalkViewEvent.this.mOnLoadListener != null) {
                if (StringUtils.isEmpty((CharSequence) status) || WebConstants.STATUS_FAILED.equals(status)) {
                    XWalkViewEvent.this.mOnLoadListener.onError();
                } else if (WebConstants.STATUS_FINISHED.equals(status)) {
                    LogUtils.d(XWalkViewEvent.TAG, "STATUS_FINISHED.equals(status) status:" + status);
                    XWalkViewEvent.this.mOnLoadListener.onSuccess();
                } else {
                    XWalkViewEvent.this.mOnLoadListener.onSuccess();
                }
            }
        }
    };
    private IXWalkView mXWalkView;

    public XWalkViewEvent() {
        LogUtils.d(TAG, "WebEvent Type: XwalkViewEvent");
    }

    public void initView() {
        if (this.mXWalkView != null) {
            this.mXWalkView.setZOrderOnTop(false);
            this.mXWalkView.setFocusable(false);
            this.mXWalkView.setBackgroundColor(0);
            this.mXWalkView.setResourceClient(this.mXWalkResourceClient);
            this.mXWalkView.setUIClient(this.mXWalkUIClient);
        }
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

    public View getView() {
        this.mXWalkView = this.mXWalkPlugin.create(this.mContext);
        return this.mXWalkView.getXWalkView();
    }

    public boolean isAlready() {
        return (this.mXWalkPlugin == null || this.mXWalkView == null) ? false : true;
    }

    public void loadJsMethod(String jsUrl) {
        LogUtils.d(TAG, "loadJsMethod:" + jsUrl);
        if (isAlready()) {
            this.mXWalkView.load(jsUrl, null);
        }
    }

    public void onResume() {
        if (this.mXWalkView != null) {
            this.mXWalkView.resumeTimers();
        }
    }

    public void onDestroy() {
        if (this.mXWalkView != null) {
            this.mXWalkView.removeAllViews();
            this.mXWalkView.onDestroy();
            this.mXWalkView = null;
        }
        this.mXWalkResourceClient = null;
        this.mXWalkUIClient = null;
        this.mXWalkView = null;
        this.mXWalkPlugin = null;
    }

    public void onPause() {
        if (this.mXWalkView != null) {
            this.mXWalkView.pauseTimers();
        }
    }

    public void showErrorView() {
        if (isAlready()) {
            this.mXWalkView.setFocusable(false);
            this.mXWalkView.setVisibility(8);
        }
    }

    public int getEventType() {
        return 1;
    }

    public boolean canGoBack() {
        return isAlready() ? this.mXWalkView.canGoBack() : false;
    }

    public void goBack() {
        if (isAlready()) {
            this.mXWalkView.goBack();
        }
    }

    public void showLoading() {
        if (this.mXWalkView != null) {
            this.mXWalkView.setVisibility(8);
        }
    }

    public void showResult() {
        if (this.mXWalkView != null) {
            this.mXWalkView.setVisibility(0);
        }
    }

    public void addJavascriptInterface(Object object) {
        if (object != null && this.mXWalkView != null) {
            this.mXWalkView.addJavascriptInterface(object, getJSInterfaceName());
        }
    }

    protected boolean isHighConfigDevice() {
        return MemoryLevelInfo.isHighConfigDevice();
    }
}

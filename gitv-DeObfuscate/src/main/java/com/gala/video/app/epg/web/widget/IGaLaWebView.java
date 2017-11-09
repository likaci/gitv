package com.gala.video.app.epg.web.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import com.gala.sdk.player.ScreenMode;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.web.core.FunctionBase;
import com.gala.video.app.epg.web.core.FunctionLoad;
import com.gala.video.app.epg.web.core.FunctionPlayer;
import com.gala.video.app.epg.web.core.FunctionSkip;
import com.gala.video.app.epg.web.core.FunctionUser;
import com.gala.video.app.epg.web.core.FuntionDialog;
import com.gala.video.app.epg.web.core.WebFunManager;
import com.gala.video.app.epg.web.function.IPlayerListener;
import com.gala.video.app.epg.web.function.IScreenCallback;
import com.gala.video.app.epg.web.function.ISkipCallback;
import com.gala.video.app.epg.web.function.IWebDialog;
import com.gala.video.app.epg.web.pingback.IWebLoadPingback.ILoadStateListener;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.NetworkUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebViewDataImpl;
import com.gala.video.webview.widget.AbsWebView.ILoadingState;
import com.gala.video.webview.widget.AbsWebView.IWebViewLoad;

public class IGaLaWebView extends WebView implements IWebViewLoad, IWebDialog, ISkipCallback {
    private ScreenMode mCurScreenMode = ScreenMode.WINDOWED;
    private String mDialogState = "0";
    private ILoadStateListener mILoadStateListener;
    private IPlayerListener mPlayerCallback;
    private RelativeLayout mPlayerContainer;
    private IScreenCallback mScreenCallback = new C12663();
    private Runnable mToastRunnable = new C12652();
    private WebViewDataImpl mWebViewDataImpl;

    class C12641 implements ILoadingState {
        C12641() {
        }

        public void messageState(int state) {
            LogUtils.m1574i(IGaLaWebView.this.TAG, "ILoadingState state:" + state);
            if (state == 0) {
                IGaLaWebView.this.mHandler.postDelayed(IGaLaWebView.this.mToastRunnable, 10000);
                return;
            }
            LogUtils.m1574i(IGaLaWebView.this.TAG, "ILoadingState net not loading  remove toast!");
            IGaLaWebView.this.mHandler.removeCallbacks(IGaLaWebView.this.mToastRunnable);
        }
    }

    class C12652 implements Runnable {
        C12652() {
        }

        public void run() {
            QToast.makeTextAndShow(AppRuntimeEnv.get().getApplicationContext(), C0508R.string.web_timeout_error, (int) QToast.LENGTH_4000);
        }
    }

    class C12663 implements IScreenCallback {
        C12663() {
        }

        public void switchScreenMode(ScreenMode mode) {
            IGaLaWebView.this.setScreenMode(mode);
        }
    }

    class C12674 implements Runnable {
        C12674() {
        }

        public void run() {
            IGaLaWebView.this.goBack();
        }
    }

    public void setLoadStateListener(ILoadStateListener listenre) {
        this.mILoadStateListener = listenre;
    }

    public int getEventType() {
        return getBasicEvent().getEventType();
    }

    public IGaLaWebView(Context context) {
        super(context);
    }

    public IGaLaWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IGaLaWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(WebViewDataImpl data, String url) {
        this.mWebViewDataImpl = data;
        init(url);
        setILoadingState(new C12641());
        this.mWebViewDataImpl.putEngine(getEngine());
        setSdk(false);
    }

    public RelativeLayout getPlayerContainer() {
        if (this.mPlayerContainer == null) {
            this.mPlayerContainer = (RelativeLayout) ((ViewStub) findViewById(C0508R.id.epg_webview_extra_container_layout_viewstub)).inflate();
        }
        return this.mPlayerContainer;
    }

    protected Object getJSInterfaceObject() {
        WebFunManager mWebWuleManager = new WebFunManager();
        mWebWuleManager.setIFunLoad(new FunctionLoad(this));
        mWebWuleManager.setIFunBase(new FunctionBase(this.mContext, this.mWebViewDataImpl));
        mWebWuleManager.setFunUser(new FunctionUser(this.mContext));
        mWebWuleManager.setFunSkip(new FunctionSkip(this.mContext, this.mWebViewDataImpl, this));
        mWebWuleManager.setIFunDialog(new FuntionDialog(this));
        if (this.mPlayerCallback != null) {
            mWebWuleManager.setIFunPlayer(new FunctionPlayer(this.mPlayerCallback));
        }
        return mWebWuleManager;
    }

    public void setIFunPlayer(IPlayerListener callback) {
        this.mPlayerCallback = callback;
    }

    public void onDestroy() {
        super.onDestroy();
        this.mPlayerCallback = null;
    }

    public void show(String url) {
        if (!NetworkUtils.isNetworkAvaliable() || StringUtils.isEmpty((CharSequence) url)) {
            showError();
        } else {
            super.show(url);
        }
    }

    protected void setSdk(boolean sdk) {
        super.setSdk(sdk);
    }

    public void onWebViewLoadCompleted() {
        if (this.mILoadStateListener != null) {
            this.mILoadStateListener.onPingbackCompleted();
        }
        showResult();
    }

    public void onWebViewLoadFailed(String error) {
        onLoadFailedPost(error);
    }

    public boolean handleJsKeyEvent(KeyEvent event) {
        if (getType() == 0 && isWindowedOrDefault()) {
            if (event.getKeyCode() == 4) {
                LogUtils.m1574i(this.TAG, "handleJsKeyEvent mDialogState: " + this.mDialogState);
                if (!"1".equals(this.mDialogState)) {
                    return super.handleJsKeyEvent(event);
                }
                this.mDialogState = "0";
                Log.i(this.TAG, "handleJsKeyEvent show: ");
                this.mWebBaseEvent.loadJsMethod(String.format(WebConstants.JS_onDismissDialog, new Object[]{""}));
                return true;
            }
            LogUtils.m1568d(this.TAG, "isWindowedOrDefault && inside!!" + this.mDialogState);
            if (this.mWebBaseEvent.handleJsKeyEvent(event)) {
                return true;
            }
        }
        return super.handleJsKeyEvent(event);
    }

    private void setScreenMode(ScreenMode mode) {
        this.mCurScreenMode = mode;
    }

    private boolean isWindowedOrDefault() {
        return ScreenMode.WINDOWED == this.mCurScreenMode;
    }

    public IScreenCallback getScreenCallback() {
        return this.mScreenCallback;
    }

    public String getEngine() {
        return getEventType() == 0 ? "webview" : "crosswalk";
    }

    public void setDialogState(String state) {
        Log.i(this.TAG, "setDialogState state: " + state);
        this.mDialogState = state;
    }

    public void goBackEvent() {
        Log.i(this.TAG, "goBackEvent");
        if (this.mHandler == null) {
            Log.i(this.TAG, "goBackEvent mHandler == null");
        } else {
            this.mHandler.post(new C12674());
        }
    }
}

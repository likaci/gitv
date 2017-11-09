package com.gala.video.webview.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.gala.video.webview.data.WebParams;
import com.gala.video.webview.event.WebBaseEvent;
import com.gala.video.webview.utils.WebSDKConstants;
import com.gala.video.webview.utils.WebSDKEventFactory;

public abstract class AbsWebView extends FrameLayout {
    private static final int DELAY_MILLIS = 1500;
    public static final String ERROR_DEFAULT = "";
    protected static final int LOAD_ERROR_MSG = 2;
    protected static final int LOAD_SUCCESS_MSG = 1;
    protected static final int PROGRESSBAR_MSG = 0;
    protected String TAG;
    protected Context mContext;
    protected Handler mHandler;
    private ILoadingState mILoadingState;
    private boolean mIsShowLoading;
    protected View mLoadingView;
    private LoadListener mOnLoadListener;
    protected LinearLayout mProgressBar;
    protected WebBaseEvent mWebBaseEvent;
    protected String mWebUrl;
    private int mWebUrlType;
    private boolean misNeedFocus;

    public interface ILoadingState {
        void messageState(int i);
    }

    public interface IWebViewLoad {
        void onWebViewLoadCompleted();

        void onWebViewLoadFailed(String str);
    }

    public interface OnLoadListener {
        void onError();

        void onLoading();

        void onSuccess();
    }

    class LoadListener implements OnLoadListener {
        LoadListener() {
        }

        public void onSuccess() {
            Log.d(AbsWebView.this.TAG, "mOnLoadListener onSuccess mWebUrlType:" + AbsWebView.this.getType());
            if (AbsWebView.this.getType() == 1) {
                AbsWebView.this.sendEmptyMessage(1);
            }
        }

        public void onError() {
            Log.d(AbsWebView.this.TAG, "mOnLoadListener onError mWebUrlType:" + AbsWebView.this.getType());
            AbsWebView.this.onLoadFailedPost("");
        }

        public void onLoading() {
            Log.d(AbsWebView.this.TAG, "mOnLoadListener onLoading mWebUrlType:" + AbsWebView.this.getType());
            AbsWebView.this.showLoadingDelayed();
        }
    }

    protected abstract View getLoadingView();

    protected abstract LinearLayout getProgressBarItem();

    public abstract String getTag();

    protected abstract void initView();

    protected abstract void showErrorView(String str);

    public AbsWebView(Context context) {
        this(context, null);
    }

    public AbsWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.TAG = getTag();
        this.mWebUrl = "";
        this.mWebUrlType = -1;
        this.mIsShowLoading = false;
        this.mLoadingView = null;
        this.mProgressBar = null;
        this.mOnLoadListener = null;
        this.mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        if (AbsWebView.this.mILoadingState != null) {
                            AbsWebView.this.mILoadingState.messageState(0);
                        }
                        AbsWebView.this.showLoading();
                        return;
                    case 1:
                        if (AbsWebView.this.mILoadingState != null) {
                            AbsWebView.this.mILoadingState.messageState(1);
                        }
                        AbsWebView.this.showResultView();
                        return;
                    case 2:
                        if (AbsWebView.this.mILoadingState != null) {
                            AbsWebView.this.mILoadingState.messageState(2);
                        }
                        AbsWebView.this.showError((String) msg.obj);
                        return;
                    default:
                        return;
                }
            }
        };
        this.misNeedFocus = true;
        this.mContext = context;
        setFocusable(true);
        initView();
    }

    public void startLoading() {
        if (getLoadingViewLayout() != null) {
            getLoadingViewLayout().setVisibility(isDisplayLoading() ? 0 : 8);
        }
        if (isDisplayLoading()) {
            showLoadingDelayed();
        }
    }

    public void init() {
        init("");
    }

    public void init(WebParams params) {
        init("");
        apply(params);
    }

    public void init(String url) {
        this.mWebUrl = url;
        this.mWebUrlType = getWebUrlType(url);
        initEvent();
    }

    private void initEvent() {
        this.mWebBaseEvent = getBaseEvent();
        this.mWebBaseEvent.init(this, this.mContext, getJSInterfaceObject());
        if (this.mOnLoadListener == null) {
            this.mOnLoadListener = new LoadListener();
        }
        this.mWebBaseEvent.setOnLoadListener(this.mOnLoadListener);
        this.mWebBaseEvent.setUrlType(this.mWebUrlType);
        this.mWebBaseEvent.initView();
    }

    protected WebBaseEvent getBaseEvent() {
        return WebSDKEventFactory.createWebViewJSEvent();
    }

    public WebBaseEvent getBasicEvent() {
        return this.mWebBaseEvent;
    }

    public void setILoadingState(ILoadingState loadingState) {
        this.mILoadingState = loadingState;
    }

    private void sendEmptyMessage(int msg) {
        if (this.mHandler != null) {
            this.mHandler.removeMessages(msg);
            this.mHandler.sendEmptyMessage(msg);
        }
    }

    private void sendEmptyMessageDelayed(int msg, int time) {
        if (this.mHandler != null) {
            this.mHandler.removeMessages(msg);
            this.mHandler.sendEmptyMessageDelayed(msg, (long) time);
        }
    }

    protected void showLoadingDelayed() {
        Log.d(this.TAG, "showLoadingDelayed :" + this.mIsShowLoading);
        if (!this.mIsShowLoading) {
            clearProgressHandler();
            if (isLoadDelayed()) {
                sendEmptyMessageDelayed(0, getDelayMillis());
            } else {
                sendEmptyMessage(0);
            }
        }
        this.mIsShowLoading = true;
    }

    protected void showResult() {
        sendEmptyMessageDelayed(1, 300);
    }

    private void clearProgressHandler() {
        if (this.mHandler != null) {
            this.mHandler.removeMessages(0);
        }
    }

    private void showLoading() {
        int i = 0;
        if (!(this.mLoadingView == null || this.mLoadingView.getVisibility() == 0)) {
            int i2;
            View view = this.mLoadingView;
            if (isDisplayLoading()) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            view.setVisibility(i2);
        }
        if (!isDisplayLoading()) {
            i = 8;
        }
        setProgressBar(i);
    }

    private void showResultView() {
        Log.d(this.TAG, "showResultView");
        this.mIsShowLoading = false;
        clearProgressHandler();
        setLoadingView(8);
        if (this.mProgressBar != null) {
            setProgressBar(8);
        }
        if (this.mWebBaseEvent != null) {
            this.mWebBaseEvent.showResult(this.misNeedFocus);
        }
        showSuccessView();
    }

    private void setProgressBar(int visibility) {
        if (getProgressBarLayout() != null) {
            getProgressBarLayout().setVisibility(visibility);
        }
    }

    private void setLoadingView(int visibility) {
        if (this.mLoadingView != null) {
            this.mLoadingView.setVisibility(visibility);
        }
    }

    private void showError(String error) {
        Log.d(this.TAG, "showError error:" + error);
        clearProgressHandler();
        setLoadingView(isDisplayLoading() ? 0 : 8);
        if (this.mWebBaseEvent != null) {
            this.mWebBaseEvent.showErrorView();
        }
        if (this.mProgressBar != null) {
            setProgressBar(8);
        }
        showErrorView(error);
    }

    protected void showError() {
        showError("");
    }

    public void onLoadFailedPost(String error) {
        sendFailedMessage(error);
    }

    private void sendFailedMessage(String error) {
        if (this.mHandler != null) {
            Message message = this.mHandler.obtainMessage(2);
            message.obj = error;
            this.mHandler.sendMessage(message);
        }
    }

    public void setType(int type) {
        this.mWebUrlType = type;
    }

    public int getType() {
        return this.mWebUrlType;
    }

    protected int getWebUrlType(String url) {
        return isInsideUrl(url) ? 0 : 1;
    }

    private static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    private boolean isInsideUrl(String url) {
        if (isEmpty(url) || url.contains(WebSDKConstants.DOMAIN_CMS) || url.contains(WebSDKConstants.DOMAIN_GALA)) {
            return true;
        }
        return false;
    }

    public void show(String url) {
        Log.i(this.TAG, "show url:" + url);
        Log.i(this.TAG, "show mWebUrl:" + this.mWebUrl);
        if (isEmpty(this.mWebUrl)) {
            this.mWebUrlType = getWebUrlType(url);
            this.mWebBaseEvent.setUrlType(this.mWebUrlType);
        }
        this.mWebBaseEvent.loadJsMethod(url);
    }

    public void show(String url, boolean focus) {
        this.misNeedFocus = focus;
        Log.i(this.TAG, "show misNeedFocus: " + this.misNeedFocus);
        show(url);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 1) {
            return super.dispatchKeyEvent(event);
        }
        if (this.mWebBaseEvent == null) {
            return super.dispatchKeyEvent(event);
        }
        if (event.getKeyCode() != 4 || !this.mWebBaseEvent.canGoBack()) {
            return super.dispatchKeyEvent(event);
        }
        this.mWebBaseEvent.goBack();
        return true;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void onPost(Runnable r) {
        if (this.mHandler != null) {
            this.mHandler.post(r);
        }
    }

    public void onPause() {
        if (this.mWebBaseEvent != null) {
            this.mWebBaseEvent.onPause();
        }
    }

    public void onResume() {
        if (this.mWebBaseEvent != null) {
            this.mWebBaseEvent.onResume();
        }
    }

    public void onDestroy() {
        Log.d(this.TAG, "onDestroy");
        this.mOnLoadListener = null;
        this.mProgressBar = null;
        if (this.mWebBaseEvent != null) {
            this.mWebBaseEvent.onDestroy();
        }
        this.mWebBaseEvent = null;
    }

    public void onHide() {
        if (this.mWebBaseEvent != null) {
            this.mWebBaseEvent.onHide();
        }
    }

    private LinearLayout getProgressBarLayout() {
        if (this.mProgressBar == null) {
            this.mProgressBar = getProgressBarItem();
        }
        return this.mProgressBar;
    }

    private View getLoadingViewLayout() {
        if (this.mLoadingView == null) {
            this.mLoadingView = getLoadingView();
        }
        return this.mLoadingView;
    }

    protected Object getJSInterfaceObject() {
        return null;
    }

    protected boolean isLoadDelayed() {
        return true;
    }

    protected boolean isDisplayLoading() {
        return true;
    }

    protected int getDelayMillis() {
        return 1500;
    }

    protected void showSuccessView() {
    }

    protected void requestWebFocus() {
        if (this.mWebBaseEvent != null) {
            this.mWebBaseEvent.requestWebFocus();
        }
    }

    protected void setVerticalScrollBar(boolean verticalScrollBarEnabled) {
        if (this.mWebBaseEvent != null) {
            this.mWebBaseEvent.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
        }
    }

    protected void setHorizontalScrollBar(boolean horizontalScrollBarEnabled) {
        if (this.mWebBaseEvent != null) {
            this.mWebBaseEvent.setHorizontalScrollBar(horizontalScrollBarEnabled);
        }
    }

    protected void setHVScrollBar(boolean vHScrollBarEnabled) {
        if (this.mWebBaseEvent != null) {
            this.mWebBaseEvent.setHVScrollBar(vHScrollBarEnabled);
        }
    }

    public void goBack() {
        if (this.mWebBaseEvent != null) {
            this.mWebBaseEvent.goBack();
        }
    }

    protected void setSdk(boolean sdk) {
        if (this.mWebBaseEvent != null) {
            this.mWebBaseEvent.setSdk(sdk);
        }
    }

    public void apply(WebParams params) {
        if (this.mWebBaseEvent == null || params == null) {
            Log.e(this.TAG, "mWebBaseEvent or apply params is null");
            return;
        }
        Log.e(this.TAG, "apply params:" + params);
        this.mWebBaseEvent.setHorizontalScrollBar(params.horizontalScrollBarEnabled);
        this.mWebBaseEvent.setVerticalScrollBarEnabled(params.verticalScrollBarEnabled);
        this.mWebBaseEvent.setSdk(params.mIsSdk);
    }

    protected boolean handleJsKeyEvent(KeyEvent event) {
        if (this.mWebBaseEvent == null || !this.mWebBaseEvent.handleJsKeyEvent(event)) {
            return false;
        }
        return true;
    }
}

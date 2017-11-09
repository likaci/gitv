package com.gala.video.webview.event;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.gala.video.webview.utils.WebSDKConstants;
import com.gala.video.webview.widget.AbsWebView.OnLoadListener;

public abstract class WebBaseEvent implements IWebBaseEvent {
    protected Context mContext;
    public OnLoadListener mOnLoadListener;
    protected FrameLayout mRootLayout = null;
    protected int mWebUrlType = -1;

    public abstract View getView();

    public void init(View root, Context context, Object object) {
        init(root, context);
        addJavascriptInterface(object);
    }

    public void init(View root, Context context) {
        this.mContext = context;
        init(root);
    }

    private void init(View root) {
        try {
            this.mRootLayout = (FrameLayout) root;
            this.mRootLayout.addView(getView(), 0, new LayoutParams(-1, -1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean handleJsKeyEvent(KeyEvent event) {
        return false;
    }

    public void loadMethod(String jsUrl) {
        if (isAlready()) {
            loadJsMethod(jsUrl);
        }
    }

    public void onDestroy() {
        if (this.mRootLayout != null) {
            this.mRootLayout = null;
        }
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.mOnLoadListener = onLoadListener;
    }

    public void setUrlType(int type) {
        this.mWebUrlType = type;
    }

    protected String getJSInterfaceName() {
        return "Android";
    }

    protected boolean isShouldChangeFont(String url) {
        return url != null && url.contains(WebSDKConstants.INJECTTION_TTF) && isHighConfigDevice();
    }

    protected boolean isHighConfigDevice() {
        return false;
    }

    public void onHide() {
    }

    public void requestWebFocus() {
    }

    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
    }

    public void setHorizontalScrollBar(boolean horizontalScrollBarEnabled) {
    }

    public void setHVScrollBar(boolean vHScrollBarEnabled) {
    }

    public void setSdk(boolean sdk) {
    }

    public void showResult(boolean focusable) {
    }
}

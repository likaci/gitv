package com.gala.video.app.player.perftracker;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class FloatingWindow {
    private final String TAG = ("Player/Perf/FloatingWindow@" + Integer.toHexString(super.hashCode()));
    private View mContent;
    private Context mContext;
    private WindowManager mWMS;

    public FloatingWindow(Context context, View content) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "<init>: context=" + context + ", content=" + content);
        }
        if (context == null || content == null) {
            throw new IllegalArgumentException("Please provide valid context & content");
        }
        this.mContext = context;
        this.mContent = content;
        this.mWMS = (WindowManager) this.mContext.getApplicationContext().getSystemService("window");
    }

    public void showWindow() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "showWindow");
        }
        LayoutParams wmParams = new LayoutParams();
        wmParams.type = 2003;
        wmParams.format = 1;
        wmParams.flags = 8;
        wmParams.gravity = 53;
        wmParams.x = 0;
        wmParams.y = 0;
        wmParams.width = -2;
        wmParams.height = -2;
        this.mWMS.addView(this.mContent, wmParams);
    }

    public void hideWindow() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "hideWindow");
        }
        try {
            this.mWMS.removeView(this.mContent);
        } catch (Exception e) {
            if (LogUtils.mIsDebug) {
                LogUtils.e(this.TAG, "hideWindow: exception happened", e);
            }
        }
    }

    public boolean isShown() {
        return this.mContent != null && this.mContent.isShown();
    }

    public View getContentView() {
        return this.mContent;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FloatingWindow").append("@").append(super.hashCode());
        builder.append("{");
        builder.append("content=").append(this.mContent);
        builder.append(", context=").append(this.mContext);
        builder.append("}");
        return builder.toString();
    }
}

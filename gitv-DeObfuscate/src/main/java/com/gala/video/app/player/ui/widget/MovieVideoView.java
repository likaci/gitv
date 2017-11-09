package com.gala.video.app.player.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import com.gala.sdk.player.IPlayerProfile;
import com.gala.sdk.player.VideoSurfaceView;
import com.gala.sdk.utils.MyLogUtils;
import com.gala.video.app.player.utils.MyPlayerProfile;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class MovieVideoView extends VideoSurfaceView {
    private static final String TAG = "Player/Player/MovieVideoView";
    private boolean mIgnoreWindowChange;

    public MovieVideoView(Context context) {
        super(context);
        init();
    }

    public MovieVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MovieVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        IPlayerProfile profile = new MyPlayerProfile();
        int format = profile != null ? profile.getSurfaceFormat() : 0;
        if (format != 0) {
            getHolder().setFormat(format);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "init() format=" + format);
        }
    }

    public void setIgnoreWindowChange(boolean ignoreWindowChange) {
        this.mIgnoreWindowChange = ignoreWindowChange;
    }

    public boolean getIgnoreWindowChange() {
        return this.mIgnoreWindowChange;
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        MyLogUtils.m462d(TAG, "onVisibilityChanged(" + changedView + ", " + visibility + ")");
        super.onVisibilityChanged(changedView, visibility);
    }

    public void setVisibility(int visibility) {
        MyLogUtils.m462d(TAG, "setVisibility(" + visibility + ")");
        super.setVisibility(visibility);
    }

    protected void onWindowVisibilityChanged(int visibility) {
        MyLogUtils.m462d(TAG, "onWindowVisibilityChanged(" + visibility + ") " + this.mIgnoreWindowChange);
        if (!this.mIgnoreWindowChange) {
            super.onWindowVisibilityChanged(visibility);
        }
    }

    protected void onDetachedFromWindow() {
        MyLogUtils.m462d(TAG, "onDetachedFromWindow() " + this.mIgnoreWindowChange);
        if (!this.mIgnoreWindowChange) {
            super.onDetachedFromWindow();
        }
    }

    protected void onAttachedToWindow() {
        MyLogUtils.m462d(TAG, "onAttachedToWindow() ");
        if (VERSION.SDK_INT >= 19) {
            MyLogUtils.m462d(TAG, "isAttachedToWindow() " + isAttachedToWindow());
        }
        super.onAttachedToWindow();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        MyLogUtils.m462d(TAG, "onMeasure() ");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void draw(Canvas canvas) {
        MyLogUtils.m462d(TAG, "draw() ");
        super.draw(canvas);
    }

    public Display getDisplay() {
        MyLogUtils.m462d(TAG, "getDisplay() ");
        if (VERSION.SDK_INT >= 19) {
            MyLogUtils.m462d(TAG, "isAttachedToWindow() " + isAttachedToWindow());
        }
        return super.getDisplay();
    }

    public void requestLayout() {
        MyLogUtils.m462d(TAG, "requestLayout() ");
        super.requestLayout();
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        MyLogUtils.m462d(TAG, "onLayout() ");
        super.onLayout(changed, left, top, right, bottom);
    }
}

package com.gala.video.app.player.ui.widget.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import com.gala.video.app.player.R;
import com.gala.video.app.player.ui.widget.ThreeDimensionalParams;
import com.gala.video.app.player.ui.widget.views.ICountDownView.OnVisibilityChangeListener;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class CountDownView extends TextView implements ICountDownView {
    private static final String TAG = "Player/Ui/CountDownView";
    private float mAdTimeFullSize;
    private float mAdTimeWindowSize;
    private float mAdWidth;
    private float mAdWidthWindow;
    private int mCountDownTime;

    public CountDownView(Context context) {
        super(context);
        init(context);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/DS-DIGI.TTF"), 1);
        setTextColor(-1);
        setBackgroundResource(R.drawable.player_ad_timming_bg);
        this.mAdTimeFullSize = (float) context.getResources().getDimensionPixelSize(R.dimen.dimen_40dp);
        this.mAdWidth = (float) context.getResources().getDimensionPixelSize(R.dimen.dimen_63dp);
    }

    public void show(int countDownTime) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "show(" + countDownTime + ")");
        }
        this.mCountDownTime = countDownTime;
        if (this.mCountDownTime >= 0) {
            setBackgroundResource(R.drawable.player_ad_timming_bg);
            setText(String.valueOf(this.mCountDownTime));
            setVisibility(0);
        }
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "hide()");
        }
        setVisibility(8);
        setText(null);
        this.mCountDownTime = 0;
    }

    private void updateWindowSize(float zoomRatio) {
        if (0.0f == this.mAdTimeWindowSize) {
            this.mAdTimeWindowSize = this.mAdTimeFullSize * zoomRatio;
            this.mAdWidthWindow = this.mAdWidth * zoomRatio;
        }
    }

    public void switchScreen(boolean isFullScreen, float zoomRatio) {
        updateWindowSize(zoomRatio);
        if (isFullScreen) {
            setTextSize(0, this.mAdTimeFullSize);
            setWidth((int) this.mAdWidth);
            setHeight((int) this.mAdWidth);
            return;
        }
        setTextSize(0, this.mAdTimeWindowSize);
        setWidth((int) this.mAdWidthWindow);
        setHeight((int) this.mAdWidthWindow);
    }

    public void setThreeDimensional(boolean is3D) {
        if (is3D) {
            setTextScaleX(ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
        }
    }

    public void setOnVisibilityChangeListener(OnVisibilityChangeListener listener) {
    }
}

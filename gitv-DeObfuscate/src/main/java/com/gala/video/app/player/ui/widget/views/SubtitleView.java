package com.gala.video.app.player.ui.widget.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;
import com.gala.sdk.player.IThreeDimensional;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.ui.widget.ThreeDimensionalParams;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class SubtitleView extends TextView implements IThreeDimensional, ThreeDimensionalParams {
    private static final String TAG = "Player/Ui/SubtitleView";

    public SubtitleView(Context context) {
        super(context);
        init();
    }

    public SubtitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SubtitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setTextColor(Color.parseColor("#f1f1f1"));
        setTextSize((float) ((int) getResources().getDimension(C1291R.dimen.dimen_32dp)));
        setGravity(17);
        setMaxWidth((int) getResources().getDimension(C1291R.dimen.dimen_1120dp));
        setShadowLayer(10.0f, 10.0f, 5.0f, Color.parseColor("#000000"));
        setVisibility(8);
    }

    public void setSubtitle(String subtitle) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setSubtitle(" + subtitle + ")");
        }
        setVisibility(4);
        setText(subtitle);
        setVisibility(0);
    }

    public void setThreeDimensional(boolean enable) {
        setTextScaleX(enable ? ThreeDimensionalParams.TEXT_SCALE_FOR_3D : 1.0f);
    }
}

package com.gala.sdk.player;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class WindowZoomRatio {
    public static final float WINDOW_ZOOM_RATIO_16_BY_9_SMALL = 0.54f;
    public static final float WINDOW_ZOOM_RATIO_4_BY_3_BIG = 0.72f;
    public static final float WINDOW_ZOOM_RATIO_MIN = 0.4f;
    private float f691a;
    private boolean f692a;

    public WindowZoomRatio(boolean useCustomSetting, float customRatio) {
        this.f692a = useCustomSetting;
        this.f691a = customRatio;
    }

    public float getResultRatio(Context context, LayoutParams params) {
        if (this.f692a && this.f691a > 0.0f && this.f691a <= 1.0f) {
            return this.f691a;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d("Player/WindowZoomRatio", "calculateWindowZoomRatio: params=" + params);
        }
        if (params == null) {
            return 1.0f;
        }
        float f = (float) params.width;
        float f2 = (float) params.height;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float f3 = (float) displayMetrics.widthPixels;
        float f4 = (float) displayMetrics.heightPixels;
        float f5 = f / f3;
        float f6 = f2 / f4;
        if (f5 >= f6) {
            f5 = f6;
        }
        if (!LogUtils.mIsDebug) {
            return f5;
        }
        LogUtils.m1568d("Player/WindowZoomRatio", "<< calculateWindowZoomRatio: , window w/h=" + f + "/" + f2 + ", screen w/h=" + f3 + "/" + f4 + ", return=" + f5);
        return f5;
    }

    public String toString() {
        return "WindowZoomRatio{useCustomSetting=" + this.f692a + ", customRatio=" + this.f691a;
    }
}

package com.gala.video.widget.util;

import android.content.Context;
import com.gala.video.app.player.ui.widget.ThreeDimensionalParams;

public class DisplayUtils {
    private static float fontScale;
    private static float height;
    private static DisplayUtils mDisplayUtils = null;
    private static float ratio;
    private static float scale;
    private static float width;

    private DisplayUtils() {
    }

    private DisplayUtils(Context context) {
        scale = context.getResources().getDisplayMetrics().density;
        fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        width = (float) context.getResources().getDisplayMetrics().widthPixels;
        height = (float) context.getResources().getDisplayMetrics().heightPixels;
        if (width == 1920.0f || height == 1080.0f) {
            ratio = 1.5f;
        } else if (width == 1280.0f || height == 720.0f) {
            ratio = 1.0f;
        }
    }

    public static DisplayUtils getInstance(Context context) {
        if (mDisplayUtils == null) {
            mDisplayUtils = new DisplayUtils(context);
        }
        return mDisplayUtils;
    }

    public int px2dip(float pxValue) {
        return (int) ((pxValue / scale) + ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
    }

    public int dip2px(int dipValue) {
        return (int) (((((float) dipValue) * ratio) / scale) + ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
    }

    public int px2sp(int pxValue) {
        return (int) ((((float) pxValue) / fontScale) + ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
    }

    public int sp2px(int spValue) {
        return (int) (((((float) spValue) * ratio) / fontScale) + ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
    }
}

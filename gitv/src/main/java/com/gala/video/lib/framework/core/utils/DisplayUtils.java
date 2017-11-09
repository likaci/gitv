package com.gala.video.lib.framework.core.utils;

import com.gala.video.app.player.ui.widget.ThreeDimensionalParams;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;

public class DisplayUtils {
    public static String getDisplayMetrics() {
        int disWidth = getScreenWidth();
        int disHeight = getScreenHeight();
        return String.format("%dx%d", new Object[]{Integer.valueOf(disWidth), Integer.valueOf(disHeight)});
    }

    public static int getScreenWidth() {
        return AppRuntimeEnv.get().getApplicationContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return AppRuntimeEnv.get().getApplicationContext().getResources().getDisplayMetrics().heightPixels;
    }

    public static float getScreenDensity() {
        return AppRuntimeEnv.get().getApplicationContext().getResources().getDisplayMetrics().density;
    }

    public static int getScreenDensityDpi() {
        return AppRuntimeEnv.get().getApplicationContext().getResources().getDisplayMetrics().densityDpi;
    }

    public static int px2dip(float pxValue) {
        return (int) ((pxValue / getScreenDensity()) + ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
    }

    public static int dip2px(int dipValue) {
        return (int) ((((float) dipValue) * getScreenDensity()) + ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
    }

    public static int px2sp(int pxValue) {
        return (int) ((((float) pxValue) / AppRuntimeEnv.get().getApplicationContext().getResources().getDisplayMetrics().scaledDensity) + ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
    }

    public static int sp2px(int spValue) {
        return (int) ((((float) spValue) * AppRuntimeEnv.get().getApplicationContext().getResources().getDisplayMetrics().scaledDensity) + ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
    }
}

package com.tvos.apps.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;

public class ActivityUtil {
    private static final String TAG = "ActivityUtil";

    public static DisplayMetrics getDisplayMetrics(Activity act) {
        DisplayMetrics metrics = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.i(TAG, "Display metrics is " + metrics);
        return metrics;
    }
}

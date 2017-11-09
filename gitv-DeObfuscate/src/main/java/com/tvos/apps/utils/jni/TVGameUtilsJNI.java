package com.tvos.apps.utils.jni;

import android.util.Log;
import com.gala.video.lib.share.ifmanager.InterfaceKey;

public class TVGameUtilsJNI {
    private static final String TAG = TVGameUtilsJNI.class.getSimpleName();

    private static native void destoryJNI();

    private static native void initJNI();

    static {
        System.loadLibrary("TVGameUtilsJNI");
    }

    public static void init() {
        Log.d(TAG, InterfaceKey.SHARE_IT);
        initJNI();
    }

    public static void destory() {
        Log.d(TAG, "destory");
        destoryJNI();
    }
}

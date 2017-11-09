package com.qidun.tvapi;

import android.content.Context;

public class NativeTmcPlayer {
    public native String qdsc(Context context, String str, String str2);

    public native String tmc(Context context, String str, String str2, String str3);

    public native String vfc(Context context, String str, String str2);

    static {
        System.loadLibrary("qidunkey");
    }
}

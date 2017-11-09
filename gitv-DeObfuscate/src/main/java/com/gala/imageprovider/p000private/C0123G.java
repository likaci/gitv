package com.gala.imageprovider.p000private;

import android.graphics.Bitmap;
import android.util.Log;
import com.gala.video.lib.share.common.configs.WebConstants;

public final class C0123G {
    public static boolean f541a = C0122F.m277a();

    public static int m279a(String str, String str2) {
        return Log.d(str, str2);
    }

    public static int m282b(String str, String str2) {
        return Log.e(str, str2);
    }

    public static int m280a(String str, String str2, Throwable th) {
        return Log.e(str, str2, th);
    }

    public static String m281a(Bitmap bitmap) {
        StringBuilder stringBuilder = new StringBuilder();
        if (bitmap == null) {
            stringBuilder.append("{NULL}");
        } else {
            stringBuilder.append("{Bitmap@").append(bitmap.hashCode()).append("[").append(bitmap.getWidth()).append(WebConstants.PARAM_KEY_X).append(bitmap.getHeight()).append("], config=" + bitmap.getConfig() + "}");
        }
        return stringBuilder.toString();
    }
}

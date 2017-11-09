package com.gala.cloudui.utils;

import android.util.Log;
import com.gala.cloudui.block.Cute;
import java.util.HashMap;

public class CuteUtils {
    private static IInitCallback f491a;
    private static final HashMap<String, Cute[]> f492a = new HashMap();
    private static boolean f493a;

    public interface IInitCallback {
        void onFail();
    }

    public static boolean isInitOk() {
        return f493a;
    }

    public static void setInitOk(boolean ok) {
        Log.e("CuteUtils", "setInitOk,ok:" + ok);
        f493a = ok;
    }

    public static void setInitCallback(IInitCallback callback) {
        f491a = callback;
    }

    public static Cute[] getCutes(String style) {
        if (!f493a) {
            Log.e("CuteUtils", "setInitOk,ok=false,begin callback.onFail,mIInitCallback=" + f491a);
            if (f491a != null) {
                f491a.onFail();
            }
        }
        Cute[] cuteArr = (Cute[]) f492a.get(style);
        if (CloudUtilsGala.isArrayEmpty(cuteArr)) {
            Log.e("CuteUtils", "getStyleByName,fatal error, no this item style, style name:" + style + ",sCuteHashMap.size()=" + f492a.size());
        }
        return cuteArr;
    }

    public static void putItemStyle(String style, Cute[] cutes) {
        m264a(cutes);
        sortCuteMap(cutes);
        m264a(cutes);
        f492a.put(style, cutes);
    }

    public static void sortCuteMap(Cute[] views) {
        if (!CloudUtilsGala.isArrayEmpty(views)) {
            int length = views.length;
            for (int i = 0; i < length - 1; i++) {
                for (int i2 = i + 1; i2 < length; i2++) {
                    if (views[i].getZOrder() > views[i2].getZOrder()) {
                        Cute cute = views[i];
                        views[i] = views[i2];
                        views[i2] = cute;
                    }
                }
            }
        }
    }

    private static void m264a(Cute[] cuteArr) {
    }
}

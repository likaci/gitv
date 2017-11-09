package com.gala.cloudui.utils;

import android.util.Log;
import com.gala.cloudui.block.Cute;
import java.util.HashMap;

public class CuteUtils {
    private static IInitCallback a;
    private static final HashMap<String, Cute[]> f268a = new HashMap();
    private static boolean f269a;

    public interface IInitCallback {
        void onFail();
    }

    public static boolean isInitOk() {
        return f269a;
    }

    public static void setInitOk(boolean ok) {
        Log.e("CuteUtils", "setInitOk,ok:" + ok);
        f269a = ok;
    }

    public static void setInitCallback(IInitCallback callback) {
        a = callback;
    }

    public static Cute[] getCutes(String style) {
        if (!f269a) {
            Log.e("CuteUtils", "setInitOk,ok=false,begin callback.onFail,mIInitCallback=" + a);
            if (a != null) {
                a.onFail();
            }
        }
        Cute[] cuteArr = (Cute[]) f268a.get(style);
        if (CloudUtilsGala.isArrayEmpty(cuteArr)) {
            Log.e("CuteUtils", "getStyleByName,fatal error, no this item style, style name:" + style + ",sCuteHashMap.size()=" + f268a.size());
        }
        return cuteArr;
    }

    public static void putItemStyle(String style, Cute[] cutes) {
        a(cutes);
        sortCuteMap(cutes);
        a(cutes);
        f268a.put(style, cutes);
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

    private static void a(Cute[] cuteArr) {
    }
}

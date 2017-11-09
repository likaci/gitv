package com.gala.video.webview.utils;

import android.content.Context;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityManager;
import android.webkit.MimeTypeMap;
import java.lang.reflect.Method;

public class Utils {
    public static boolean hasJellyBean() {
        return VERSION.SDK_INT >= 16;
    }

    public static boolean hasHoneycomb() {
        return VERSION.SDK_INT >= 11;
    }

    public static boolean hasJellyBeanMR1() {
        return VERSION.SDK_INT >= 17;
    }

    public static boolean hasKitkat() {
        return VERSION.SDK_INT >= 19;
    }

    public static boolean hasLollipop() {
        return VERSION.SDK_INT >= 21;
    }

    public static boolean hasClassEx() {
        try {
            if (Class.forName("android.webkit.JavascriptInterface") != null) {
                return true;
            }
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isIcoResource(String requestUrl) {
        if (TextUtils.isEmpty(requestUrl)) {
            return false;
        }
        return TextUtils.equals(MimeTypeMap.getFileExtensionFromUrl(requestUrl), "ico");
    }

    public static void disableAccessibility(Context context) {
        if (VERSION.SDK_INT == 17 && context != null) {
            try {
                AccessibilityManager am = (AccessibilityManager) context.getSystemService("accessibility");
                if (am.isEnabled()) {
                    Method setState = am.getClass().getDeclaredMethod("setState", new Class[]{Integer.TYPE});
                    setState.setAccessible(true);
                    setState.invoke(am, new Object[]{Integer.valueOf(0)});
                }
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
    }
}

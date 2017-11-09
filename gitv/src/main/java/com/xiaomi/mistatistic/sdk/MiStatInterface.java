package com.xiaomi.mistatistic.sdk;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.mistatistic.sdk.controller.a;
import com.xiaomi.mistatistic.sdk.controller.ab;
import com.xiaomi.mistatistic.sdk.controller.g;
import com.xiaomi.mistatistic.sdk.controller.i;
import com.xiaomi.mistatistic.sdk.controller.m;
import com.xiaomi.mistatistic.sdk.controller.o;
import com.xiaomi.mistatistic.sdk.controller.s;
import com.xiaomi.mistatistic.sdk.controller.t;
import com.xiaomi.mistatistic.sdk.controller.x;
import com.xiaomi.mistatistic.sdk.data.b;
import com.xiaomi.mistatistic.sdk.data.c;
import com.xiaomi.mistatistic.sdk.data.d;
import com.xiaomi.mistatistic.sdk.data.e;
import java.util.Map;

public abstract class MiStatInterface {
    public static final long MAX_UPLOAD_INTERVAL = 86400000;
    public static final long MIN_UPLOAD_INTERVAL = 300000;
    public static final int UPLOAD_POLICY_BATCH = 2;
    public static final int UPLOAD_POLICY_DEVELOPMENT = 5;
    public static final int UPLOAD_POLICY_INTERVAL = 4;
    public static final int UPLOAD_POLICY_REALTIME = 0;
    public static final int UPLOAD_POLICY_WHILE_INITIALIZE = 3;
    public static final int UPLOAD_POLICY_WIFI_ONLY = 1;
    private static boolean sInitialized = false;

    private static void checkCategoryAndKey(String str, String str2) {
        if (!TextUtils.isEmpty(str) && str.startsWith("mistat_")) {
            throw new IllegalArgumentException("category cannot start with mistat_");
        } else if (!TextUtils.isEmpty(str2) && str2.startsWith("mistat_")) {
            throw new IllegalArgumentException("key cannot start with mistat_");
        }
    }

    private static void checkInitialized() {
        if (!sInitialized) {
            throw new IllegalStateException("not initialized, do you forget to call initialize when application started?");
        }
    }

    public static void enableExceptionCatcher(boolean z) {
        a.a(false);
        a.a(z ? 2 : 3);
    }

    public static final void enableLog() {
        o.a();
    }

    public static final String getDeviceID(Context context) {
        return g.a(context);
    }

    public static final void initialize(Context context, String str, String str2, String str3) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            throw new IllegalArgumentException("appID or appKey is empty.");
        }
        Context applicationContext = context.getApplicationContext();
        if (str3 == null) {
            str3 = "";
        }
        a.a(applicationContext, str, str2, str3);
        i.a();
        new g().a();
        ab.a().b();
        sInitialized = true;
    }

    public static boolean isExceptionCatcherEnabled() {
        return a.d() != 1;
    }

    public static final void recordCalculateEvent(String str, String str2, long j) {
        recordCalculateEvent(str, str2, j, null);
    }

    public static final void recordCalculateEvent(String str, String str2, long j, Map map) {
        checkInitialized();
        checkCategoryAndKey(str, str2);
        if (s.a(a.a(), "basic_info_reported")) {
            m.a(new b(TextUtils.isEmpty(str) ? "mistat_default" : str, str2, j, map));
        }
    }

    public static final void recordCountEvent(String str, String str2) {
        recordCountEvent(str, str2, null);
    }

    public static final void recordCountEvent(String str, String str2, Map map) {
        checkInitialized();
        checkCategoryAndKey(str, str2);
        if (s.a(a.a(), "basic_info_reported")) {
            if (TextUtils.isEmpty(str)) {
                str = "mistat_default";
            }
            m.a(new c(str, str2, map));
        }
    }

    public static final void recordNumericPropertyEvent(String str, String str2, long j) {
        checkInitialized();
        checkCategoryAndKey(str, str2);
        if (s.a(a.a(), "basic_info_reported")) {
            if (TextUtils.isEmpty(str)) {
                str = "mistat_default";
            }
            m.a(new d(str, str2, j));
        }
    }

    public static final void recordPageEnd() {
        checkInitialized();
        x.a().b();
    }

    public static final void recordPageStart(Activity activity, String str) {
        checkInitialized();
        x.a().a(activity, str);
        com.xiaomi.mistatistic.sdk.controller.b.a().a(new com.xiaomi.mistatistic.sdk.controller.a.a());
    }

    public static final void recordStringPropertyEvent(String str, String str2, String str3) {
        checkInitialized();
        checkCategoryAndKey(str, str2);
        if (s.a(a.a(), "basic_info_reported")) {
            if (TextUtils.isEmpty(str)) {
                str = "mistat_default";
            }
            m.a(new e(str, str2, str3));
        }
    }

    public static final void setUploadPolicy(int i, long j) {
        checkInitialized();
        if (i != 4 || (j >= 300000 && j <= 86400000)) {
            ab.a().a(i, j);
            return;
        }
        throw new IllegalArgumentException("interval should be set between 5 minutes and 1 day");
    }

    public static boolean shouldExceptionUploadImmediately() {
        return a.d() == 2;
    }

    public static final void triggerUploadManually() {
        checkInitialized();
        new t().a();
    }
}

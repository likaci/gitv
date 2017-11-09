package com.xiaomi.mistatistic.sdk;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.mistatistic.sdk.controller.C2111a;
import com.xiaomi.mistatistic.sdk.controller.C2112b;
import com.xiaomi.mistatistic.sdk.controller.C2116g;
import com.xiaomi.mistatistic.sdk.controller.C2118i;
import com.xiaomi.mistatistic.sdk.controller.C2122m;
import com.xiaomi.mistatistic.sdk.controller.C2124o;
import com.xiaomi.mistatistic.sdk.controller.C2128s;
import com.xiaomi.mistatistic.sdk.controller.C2129t;
import com.xiaomi.mistatistic.sdk.controller.C2133x;
import com.xiaomi.mistatistic.sdk.controller.ab;
import com.xiaomi.mistatistic.sdk.controller.p037a.C2106a;
import com.xiaomi.mistatistic.sdk.data.C2138d;
import com.xiaomi.mistatistic.sdk.data.C2139b;
import com.xiaomi.mistatistic.sdk.data.C2140c;
import com.xiaomi.mistatistic.sdk.data.C2141e;
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
        C2102a.m1762a(false);
        C2102a.m1760a(z ? 2 : 3);
    }

    public static final void enableLog() {
        C2124o.m1839a();
    }

    public static final String getDeviceID(Context context) {
        return C2116g.m1807a(context);
    }

    public static final void initialize(Context context, String str, String str2, String str3) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            throw new IllegalArgumentException("appID or appKey is empty.");
        }
        Context applicationContext = context.getApplicationContext();
        if (str3 == null) {
            str3 = "";
        }
        C2111a.m1780a(applicationContext, str, str2, str3);
        C2118i.m1816a();
        new C2116g().m1813a();
        ab.m1789a().m1792b();
        sInitialized = true;
    }

    public static boolean isExceptionCatcherEnabled() {
        return C2102a.m1766d() != 1;
    }

    public static final void recordCalculateEvent(String str, String str2, long j) {
        recordCalculateEvent(str, str2, j, null);
    }

    public static final void recordCalculateEvent(String str, String str2, long j, Map map) {
        checkInitialized();
        checkCategoryAndKey(str, str2);
        if (C2128s.m1853a(C2111a.m1779a(), "basic_info_reported")) {
            C2122m.m1837a(new C2139b(TextUtils.isEmpty(str) ? "mistat_default" : str, str2, j, map));
        }
    }

    public static final void recordCountEvent(String str, String str2) {
        recordCountEvent(str, str2, null);
    }

    public static final void recordCountEvent(String str, String str2, Map map) {
        checkInitialized();
        checkCategoryAndKey(str, str2);
        if (C2128s.m1853a(C2111a.m1779a(), "basic_info_reported")) {
            if (TextUtils.isEmpty(str)) {
                str = "mistat_default";
            }
            C2122m.m1837a(new C2140c(str, str2, map));
        }
    }

    public static final void recordNumericPropertyEvent(String str, String str2, long j) {
        checkInitialized();
        checkCategoryAndKey(str, str2);
        if (C2128s.m1853a(C2111a.m1779a(), "basic_info_reported")) {
            if (TextUtils.isEmpty(str)) {
                str = "mistat_default";
            }
            C2122m.m1837a(new C2138d(str, str2, j));
        }
    }

    public static final void recordPageEnd() {
        checkInitialized();
        C2133x.m1870a().m1879b();
    }

    public static final void recordPageStart(Activity activity, String str) {
        checkInitialized();
        C2133x.m1870a().m1878a(activity, str);
        C2112b.m1802a().m1805a(new C2106a());
    }

    public static final void recordStringPropertyEvent(String str, String str2, String str3) {
        checkInitialized();
        checkCategoryAndKey(str, str2);
        if (C2128s.m1853a(C2111a.m1779a(), "basic_info_reported")) {
            if (TextUtils.isEmpty(str)) {
                str = "mistat_default";
            }
            C2122m.m1837a(new C2141e(str, str2, str3));
        }
    }

    public static final void setUploadPolicy(int i, long j) {
        checkInitialized();
        if (i != 4 || (j >= 300000 && j <= 86400000)) {
            ab.m1789a().m1791a(i, j);
            return;
        }
        throw new IllegalArgumentException("interval should be set between 5 minutes and 1 day");
    }

    public static boolean shouldExceptionUploadImmediately() {
        return C2102a.m1766d() == 2;
    }

    public static final void triggerUploadManually() {
        checkInitialized();
        new C2129t().m1866a();
    }
}

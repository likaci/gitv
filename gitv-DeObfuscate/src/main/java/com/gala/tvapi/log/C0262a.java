package com.gala.tvapi.log;

import android.util.Log;
import com.gala.video.api.log.ApiEngineLog;
import java.util.List;

public final class C0262a {
    private static ThreadLocal<StringBuilder> f923a = new C02611();
    private static boolean f924a = false;
    private static final boolean f925b = Log.isLoggable("TVAPIDebug", 3);

    static class C02611 extends ThreadLocal<StringBuilder> {
        C02611() {
        }

        protected final /* synthetic */ Object initialValue() {
            return m627a();
        }

        private synchronized StringBuilder m627a() {
            return new StringBuilder(20);
        }
    }

    public static void m632a(boolean z) {
        f924a = z;
        ApiEngineLog.setDebugEnabled(z);
    }

    public static boolean m633a() {
        return ApiEngineLog.getDebugEnabled();
    }

    public static void m629a(String str, Object obj) {
        if (f924a) {
            Log.d("<TVAPI> DEBUG " + str + ") ", obj.toString());
        }
    }

    public static void m634b(String str, Object obj) {
        if (f924a) {
            Log.w("<TVAPI> ERROR " + str + ") ", obj.toString());
        }
    }

    public static void m630a(String str, String str2, int i) {
        StringBuilder stringBuilder = (StringBuilder) f923a.get();
        stringBuilder.delete(0, stringBuilder.length());
        C0262a.m629a(stringBuilder.append("id=").append(i).append(", ").append(str).toString(), "url = " + str2);
    }

    public static void m631a(String str, List<String> list, int i) {
        StringBuilder stringBuilder = (StringBuilder) f923a.get();
        stringBuilder.delete(0, stringBuilder.length());
        String stringBuilder2 = stringBuilder.append("id=").append(i).append(", ").append(str).toString();
        if (list != null && list.size() > 0) {
            for (String str2 : list) {
                C0262a.m629a(stringBuilder2, "header = " + str2);
            }
        }
    }

    public static void m628a(String str, long j, String str2, int i, boolean z, boolean z2) {
        StringBuilder stringBuilder = (StringBuilder) f923a.get();
        stringBuilder.delete(0, stringBuilder.length());
        String stringBuilder2 = stringBuilder.append("id=").append(i).append(", ").append(str).append(", response_time = ").append(j).append("ms").toString();
        if (!z) {
            C0262a.m634b(stringBuilder2, "call error. data = " + str2);
        } else if (z2) {
            C0262a.m629a(stringBuilder2, "data = " + str2);
        } else if (f925b) {
            C0262a.m629a(stringBuilder2, "data = " + str2);
        } else {
            C0262a.m629a(stringBuilder2, "get result");
        }
    }
}

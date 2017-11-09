package com.gala.tvapi.log;

import android.util.Log;
import com.gala.video.api.log.ApiEngineLog;
import java.util.List;

public final class a {
    private static ThreadLocal<StringBuilder> a = new ThreadLocal<StringBuilder>() {
        protected final /* synthetic */ Object initialValue() {
            return a();
        }

        private synchronized StringBuilder a() {
            return new StringBuilder(20);
        }
    };
    private static boolean f196a = false;
    private static final boolean b = Log.isLoggable("TVAPIDebug", 3);

    public static void a(boolean z) {
        f196a = z;
        ApiEngineLog.setDebugEnabled(z);
    }

    public static boolean a() {
        return ApiEngineLog.getDebugEnabled();
    }

    public static void a(String str, Object obj) {
        if (f196a) {
            Log.d("<TVAPI> DEBUG " + str + ") ", obj.toString());
        }
    }

    public static void b(String str, Object obj) {
        if (f196a) {
            Log.w("<TVAPI> ERROR " + str + ") ", obj.toString());
        }
    }

    public static void a(String str, String str2, int i) {
        StringBuilder stringBuilder = (StringBuilder) a.get();
        stringBuilder.delete(0, stringBuilder.length());
        a(stringBuilder.append("id=").append(i).append(", ").append(str).toString(), "url = " + str2);
    }

    public static void a(String str, List<String> list, int i) {
        StringBuilder stringBuilder = (StringBuilder) a.get();
        stringBuilder.delete(0, stringBuilder.length());
        String stringBuilder2 = stringBuilder.append("id=").append(i).append(", ").append(str).toString();
        if (list != null && list.size() > 0) {
            for (String str2 : list) {
                a(stringBuilder2, "header = " + str2);
            }
        }
    }

    public static void a(String str, long j, String str2, int i, boolean z, boolean z2) {
        StringBuilder stringBuilder = (StringBuilder) a.get();
        stringBuilder.delete(0, stringBuilder.length());
        String stringBuilder2 = stringBuilder.append("id=").append(i).append(", ").append(str).append(", response_time = ").append(j).append("ms").toString();
        if (!z) {
            b(stringBuilder2, "call error. data = " + str2);
        } else if (z2) {
            a(stringBuilder2, "data = " + str2);
        } else if (b) {
            a(stringBuilder2, "data = " + str2);
        } else {
            a(stringBuilder2, "get result");
        }
    }
}

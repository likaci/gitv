package com.xcrash.crashreporter.generic;

import org.json.JSONObject;

public interface ICrashCallback {
    public static final int CRASH_ANR = 2;
    public static final int CRASH_ERROR = 4;
    public static final int CRASH_JAVA = 3;
    public static final int CRASH_NATIVE = 1;

    boolean disableUploadCrash();

    JSONObject getAppData(String str, boolean z, int i);

    void onCrash(JSONObject jSONObject, int i, String str);
}

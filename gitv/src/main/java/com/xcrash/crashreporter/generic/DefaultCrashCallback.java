package com.xcrash.crashreporter.generic;

import com.xcrash.crashreporter.utils.DebugLog;
import org.json.JSONObject;

public class DefaultCrashCallback implements ICrashCallback {
    private static final String TAG = "Xcrash.callback";

    public JSONObject getAppData(String process, boolean isOnCrash, int type) {
        return null;
    }

    public void onCrash(JSONObject json, int type, String message) {
        DebugLog.log(TAG, "crash happened:", Integer.valueOf(type), message);
    }

    public boolean disableUploadCrash() {
        return false;
    }
}

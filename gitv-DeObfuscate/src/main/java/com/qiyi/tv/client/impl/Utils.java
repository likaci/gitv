package com.qiyi.tv.client.impl;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Looper;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.qiyi.tv.client.NotInitializedException;
import com.qiyi.tv.client.OperationInMainThreadException;

public class Utils {
    public static final int INTENT_FLAG_DEFAULT = 335577088;
    public static final int INTENT_FLAG_INVALID = -1;

    private Utils() {
    }

    public static int parseErrorCode(Exception exception) {
        Log.m1621d("Utils", "parseErrorCode(" + exception + ")", exception);
        if (exception instanceof DeadObjectException) {
            return 8;
        }
        if (exception instanceof NotInitializedException) {
            return 3;
        }
        return 1;
    }

    public static boolean isServerDied(Exception exception) {
        boolean z = false;
        if (exception instanceof DeadObjectException) {
            z = true;
        }
        Log.m1620d("Utils", "isServerDied(" + exception + ") return " + z);
        return z;
    }

    public static void startActivity(Context context, Intent intent) throws ActivityNotFoundException {
        Log.m1620d("Utils", "startActivity(" + intent + ")");
        dumpIntent("startActivity()", intent);
        int flags = intent.getFlags();
        if (flags >= 0) {
            intent.setFlags(flags);
        } else {
            intent.setFlags(INTENT_FLAG_DEFAULT);
        }
        context.startActivity(intent);
    }

    public static void copyBundle(Bundle target, Bundle source) {
        dumpBundle("copyBundle() target=", target);
        dumpBundle("copyBundle() source=", source);
        if (!(target == null || source == null)) {
            target.putAll(source);
        }
        dumpBundle("copyBundle() final=", target);
    }

    public static void dumpBundle(String tag, Bundle bundle) {
        if (bundle != null) {
            Log.m1620d("Utils", "dumpBundle() " + tag + ": bundle size=" + bundle.size());
            for (String str : bundle.keySet()) {
                Log.m1620d("Utils", "dumpBundle() " + tag + ": key[" + str + "]=" + bundle.get(str));
            }
            return;
        }
        Log.m1620d("Utils", "dumpBundle() " + tag + ": Null Bundle");
    }

    public static void dumpIntent(String tag, Intent intent) {
        if (intent != null) {
            Log.m1620d("Utils", "dumpIntent() " + tag + ": action=" + intent.getAction());
            Log.m1620d("Utils", "dumpIntent() " + tag + ": categories=" + intent.getCategories());
            Log.m1620d("Utils", "dumpIntent() " + tag + ": flag=" + intent.getFlags() + "[" + Integer.toBinaryString(intent.getFlags()) + AlbumEnterFactory.SIGN_STR);
            dumpBundle(tag, intent.getExtras());
            return;
        }
        Log.m1620d("Utils", "dumpIntent() " + tag + ": Null Intent");
    }

    public static Bundle createResultBundle(int code) {
        Bundle bundle = new Bundle();
        ParamsHelper.setResultCode(bundle, code);
        return bundle;
    }

    public static void assertTrue(boolean value, String message) {
        if (!value) {
            Log.m1624w("Utils", "assertTrue() fail! message is: " + message);
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertNotInMainThread() {
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            throw new OperationInMainThreadException("This function cannot be called in main thread!", null);
        }
    }
}

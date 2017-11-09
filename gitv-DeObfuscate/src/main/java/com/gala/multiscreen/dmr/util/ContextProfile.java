package com.gala.multiscreen.dmr.util;

import android.content.Context;

public class ContextProfile {
    private static Context mContext = null;

    public static void setContext(Context context) {
        mContext = context;
    }

    public static Context getContext() {
        return mContext;
    }
}

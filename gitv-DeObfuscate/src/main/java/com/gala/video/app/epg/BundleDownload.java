package com.gala.video.app.epg;

import android.util.Log;

public class BundleDownload {
    private static final String TAG = "BundleDownload";

    private static class SingletonHelper {
        private static final BundleDownload instance = new BundleDownload();

        private SingletonHelper() {
        }
    }

    private BundleDownload() {
    }

    public static BundleDownload getInstance() {
        return SingletonHelper.instance;
    }

    public void check() {
        Log.d(TAG, "do nothing");
    }
}

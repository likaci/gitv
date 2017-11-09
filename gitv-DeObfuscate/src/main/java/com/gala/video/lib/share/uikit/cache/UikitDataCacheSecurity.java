package com.gala.video.lib.share.uikit.cache;

import android.content.SharedPreferences.Editor;
import android.util.Log;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import java.io.File;

public class UikitDataCacheSecurity {
    private static final String TAG = "UikitDataCacheSecurity";
    private static final UikitDataCacheSecurity sInstance = new UikitDataCacheSecurity();
    private boolean isSecurity = false;

    private UikitDataCacheSecurity() {
    }

    public static UikitDataCacheSecurity getInstance() {
        return sInstance;
    }

    public boolean isCacheSecurity() {
        return AppRuntimeEnv.get().getApplicationContext().getSharedPreferences("UIKIT_BUILD", 0).getBoolean("FLAG", false);
    }

    public void clearCache() {
        File dir = new File(UikitDataCacheConstants.PATH);
        if (dir != null && dir.isDirectory()) {
            File[] fileList = dir.listFiles();
            if (fileList != null && fileList.length > 0) {
                for (int i = 0; i < fileList.length; i++) {
                    if (fileList[i].getName().contains("uikit_")) {
                        fileList[i].delete();
                    }
                }
            }
        }
    }

    public void setSecurity(boolean flag) {
        Editor localEditor = AppRuntimeEnv.get().getApplicationContext().getSharedPreferences("UIKIT_BUILD", 0).edit();
        localEditor.putBoolean("FLAG", flag);
        localEditor.commit();
        Log.d(TAG, "set flag = " + flag);
    }
}

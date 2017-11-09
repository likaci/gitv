package com.gala.video;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.multidex.MultiDex;
import android.util.Log;
import com.gala.video.app.epg.GalaApplication;
import com.gala.video.app.player.PlayerApplication;
import com.gala.video.app.stub.HostBuild;
import com.gala.video.app.stub.ProcessHelper;

public class HostApp extends GalaApplication {
    private static final String TAG = "HostApp";

    public void onCreate() {
        Log.d(TAG, "onCreate");
        ProcessHelper.fetchCurrentProcessName(this);
        HostBuild.load(this);
        super.onCreate();
        new PlayerApplication().init();
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public Context getBaseContext() {
        Log.d(TAG, "getBaseContext");
        Context baseContext = super.getBaseContext();
        if (baseContext instanceof ContextWrapper) {
            return ((ContextWrapper) baseContext).getBaseContext();
        }
        return baseContext;
    }
}

package com.gala.video.app.epg.ui.applist.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.gala.appmanager.GalaAppManager;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class AppLauncherActivity extends Activity {
    private static final String TAG = "EPG/home/AppLauncherActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GalaAppManager appManager = GalaAppManager.createAppManager(this, null);
        try {
            Intent intent = getIntent();
            if (intent != null) {
                String from = intent.getStringExtra("start_app_form");
                if ("start_app_package_name".equals(from)) {
                    String pkgName = intent.getStringExtra("package_name");
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(TAG, "onCreate() -> app package name:" + pkgName);
                    }
                    appManager.startApp(pkgName);
                } else if ("start_app_use_position".equals(from)) {
                    int position = intent.getIntExtra("third_app_position", -1);
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(TAG, "onCreate() -> app position:" + position);
                    }
                    appManager.startApp(position);
                }
                finish();
            }
            if (LogUtils.mIsDebug) {
                Log.e(TAG, "onCreate() -> intent = null");
            }
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

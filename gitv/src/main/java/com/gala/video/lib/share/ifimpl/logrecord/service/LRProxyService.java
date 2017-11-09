package com.gala.video.lib.share.ifimpl.logrecord.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.gala.report.core.log.ILogCore;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class LRProxyService extends Service {
    private static final String TAG = "LRProxyService";
    private LRBinder mBinder = new LRBinder();
    protected ILogCore mPluginService;

    public class LRBinder extends Binder {
        LRProxyService getService() {
            return LRProxyService.this;
        }
    }

    public void onCreate() {
        Log.v(TAG, "onCreate");
        try {
            if (this.mPluginService == null) {
                this.mPluginService = GetInterfaceTools.getILogRecordProvider().getLogCore();
            }
            if (this.mPluginService != null) {
                this.mPluginService.onCreate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        int ret = super.onStartCommand(intent, flags, startId);
        try {
            if (this.mPluginService == null) {
                this.mPluginService = GetInterfaceTools.getILogRecordProvider().getLogCore();
            }
            if (this.mPluginService != null) {
                this.mPluginService.onStartCommand();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 2;
    }

    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        try {
            if (this.mPluginService == null) {
                this.mPluginService = GetInterfaceTools.getILogRecordProvider().getLogCore();
            }
            if (this.mPluginService != null) {
                this.mPluginService.onDestroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        Log.e(TAG, "start IBinder~~~");
        return this.mBinder;
    }
}

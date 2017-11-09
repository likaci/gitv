package com.gala.video.lib.share.ifimpl.logrecord.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.gala.report.core.multiprocess.IMultiProcess;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class MessengerServiceProxy extends Service {
    public static final String TAG = "LogRecord/MessengerServiceProxy";
    protected IMultiProcess mPluginService;

    public void onCreate() {
        Log.v(TAG, "onCreate");
        try {
            if (this.mPluginService == null) {
                this.mPluginService = GetInterfaceTools.getILogRecordProvider().getMultiProcess();
            }
            this.mPluginService.onCreateMultiProcess(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onCreate();
    }

    public IBinder onBind(Intent intent) {
        Log.v(TAG, "onBind");
        if (this.mPluginService != null) {
            return this.mPluginService.onBindMultiProcess(intent);
        }
        Log.v(TAG, "mPluginService is null");
        return null;
    }
}

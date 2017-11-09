package com.gala.sdk.plugin.server.storage;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.gala.sdk.plugin.AppInfo;
import com.gala.sdk.plugin.HostPluginInfo;
import com.gala.sdk.plugin.Log;
import com.gala.sdk.plugin.server.download.DownloadInfo;
import com.gala.sdk.plugin.server.storage.IStorageServer.Stub;
import com.gala.sdk.plugin.server.utils.DataUtils;
import com.gala.sdk.plugin.server.utils.PluginDebugUtils;
import com.gala.sdk.plugin.server.utils.PluginDebugUtils.DEBUG_PROPERTY;
import java.util.List;

public class StorageServer extends Service {
    private static final String TAG = "StorageServer";
    private StorageBinder mBinder;

    public static class StorageBinder extends Stub {
        private static final String TAG = "StorageBinder";
        private StorageStub mStorageStub;

        public StorageBinder(Context context, AppInfo info) {
            if (Log.VERBOSE) {
                Log.v(TAG, "StorageBinder<<(context=" + context + ", info=" + info + ")");
            }
            this.mStorageStub = new StorageStub(context, info);
            if (Log.VERBOSE) {
                Log.v(TAG, "StorageBinder>>()");
            }
        }

        public List<PluginInfo> loadPluginInfos(HostPluginInfo hPluginInfo, boolean needRemoveDumy) throws RemoteException {
            return this.mStorageStub.loadPluginInfos(hPluginInfo, needRemoveDumy);
        }

        public void savePluginInfos(HostPluginInfo hPluginInfo, List<PluginInfo> list) throws RemoteException {
            this.mStorageStub.savePluginInfos(hPluginInfo, list);
        }

        public boolean download(DownloadInfo info, StorageException se) throws RemoteException {
            return this.mStorageStub.download(info, se);
        }

        public boolean copySoLibToHost(PluginInfo info, StorageException se) throws RemoteException {
            if (Log.VERBOSE) {
                Log.v(TAG, "copySoLibToHost<<(info=" + info + ")");
            }
            boolean success = this.mStorageStub.copySoLibToHost(info, se);
            if (PluginDebugUtils.needThrowable(DEBUG_PROPERTY.COPY_SO_FAILED)) {
                throw createRemoteException(new Exception("(apkFilePath=" + info.getPath() + ", targetSoLib=" + info.getLibFolder() + ") copy so failed!!(for debug!)"));
            }
            if (Log.VERBOSE) {
                Log.v(TAG, "copySoLibToHost>>() return " + success);
            }
            return success;
        }

        public boolean copyPluginFromAssets(String assetsPath, String targetPath, StorageException se) throws RemoteException {
            return this.mStorageStub.copyPluginFromAssets(assetsPath, targetPath, se);
        }

        public void removePluginFiles(HostPluginInfo hPluginInfo, PluginInfo info) throws RemoteException {
            this.mStorageStub.removePluginFiles(hPluginInfo, info);
        }

        private RemoteException createRemoteException(Throwable throwable) {
            RemoteException exception = new RemoteException();
            exception.setStackTrace(throwable.getStackTrace());
            return exception;
        }
    }

    public void onCreate() {
        if (Log.VERBOSE) {
            Log.v(TAG, "onCreate()");
        }
        super.onCreate();
    }

    public IBinder onBind(Intent intent) {
        if (Log.VERBOSE) {
            Log.v(TAG, "onBind<<(intent=" + intent + ")");
        }
        if (this.mBinder == null) {
            this.mBinder = new StorageBinder(this, DataUtils.getAppInfo(intent.getExtras()));
        }
        if (Log.VERBOSE) {
            Log.v(TAG, "onBind>>() return binder=" + this.mBinder);
        }
        return this.mBinder;
    }

    public boolean onUnbind(Intent intent) {
        if (Log.VERBOSE) {
            Log.v(TAG, "onUnbind(intent=" + intent + ")");
        }
        this.mBinder = null;
        return super.onUnbind(intent);
    }

    public void onDestroy() {
        if (Log.VERBOSE) {
            Log.v(TAG, "onDestroy()");
        }
        super.onDestroy();
    }
}

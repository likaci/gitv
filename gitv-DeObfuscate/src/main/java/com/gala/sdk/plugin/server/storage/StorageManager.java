package com.gala.sdk.plugin.server.storage;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import com.gala.sdk.plugin.AppInfo;
import com.gala.sdk.plugin.HostPluginInfo;
import com.gala.sdk.plugin.Log;
import com.gala.sdk.plugin.PluginType;
import com.gala.sdk.plugin.server.download.DownloadInfo;
import com.gala.sdk.plugin.server.storage.IStorageServer.Stub;
import com.gala.sdk.plugin.server.utils.DataUtils;
import com.gala.sdk.plugin.server.utils.FileUtils;
import com.gala.sdk.plugin.server.utils.ListUtils;
import com.gala.sdk.plugin.server.utils.PluginDebugUtils;
import com.gala.sdk.plugin.server.utils.PluginDebugUtils.DEBUG_PROPERTY;
import com.gala.sdk.plugin.server.utils.Util;
import com.gala.video.lib.share.ifimpl.interaction.ActionSet;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class StorageManager {
    private static final String PLUGIN_ASSESTS_FOLDER = "plugin";
    private static final String PLUGIN_CLASS_NAME = "com.gala.video.plugin.PluginProvider";
    private static final String PLUGIN_FOLDER = "plugin";
    private static final String SPLIT = "-";
    private static final String TAG = "StorageManager";
    private static StorageManager sInstance = null;
    private final AppInfo mAppInfo;
    private final AtomicBoolean mConnected = new AtomicBoolean(false);
    private IStorageServer mEmptyServer;
    private final String mFileRootPath;
    private final String mFileRootPathSd;
    private final Context mHostApplicationContext;
    private boolean mIsMultiProcess;
    private IStorageServer mStorageServer;
    private StorageStub mStorageStub;

    private static class EmptyStorageServer implements IStorageServer {
        private static final String TAG = "StorageManager-EmptyStorageServer";

        private EmptyStorageServer() {
        }

        public IBinder asBinder() {
            if (Log.VERBOSE) {
                Log.m434v(TAG, "asBinder<<()");
            }
            if (Log.VERBOSE) {
                Log.m434v(TAG, "asBinder>>() return null");
            }
            return null;
        }

        public List<PluginInfo> loadPluginInfos(HostPluginInfo hPluginInfo, boolean needRemoveDumy) throws RemoteException {
            if (Log.VERBOSE) {
                Log.m434v(TAG, "loadPluginInfos<<(hPluginInfo=" + hPluginInfo + ", needRemoveDumy=" + needRemoveDumy + ")");
            }
            List<PluginInfo> list = new ArrayList();
            if (Log.VERBOSE) {
                Log.m434v(TAG, "loadPluginInfos>>() return " + list);
            }
            return list;
        }

        public void savePluginInfos(HostPluginInfo hPluginInfo, List<PluginInfo> infos) throws RemoteException {
            if (Log.VERBOSE) {
                Log.m434v(TAG, "savePluginInfos<<(hPluginInfo=" + hPluginInfo + ", infos=" + infos + ")");
            }
            if (Log.VERBOSE) {
                Log.m434v(TAG, "savePluginInfos>>()");
            }
        }

        public boolean download(DownloadInfo info, StorageException e) throws RemoteException {
            if (Log.VERBOSE) {
                Log.m434v(TAG, "downloadAsync<<(info=" + info + ")");
            }
            if (Log.VERBOSE) {
                Log.m434v(TAG, "downloadAsync>>() return false");
            }
            return false;
        }

        public boolean copySoLibToHost(PluginInfo info, StorageException e) throws RemoteException {
            if (Log.VERBOSE) {
                Log.m434v(TAG, "copySoLibToHost<<(info=" + info + ")");
            }
            if (Log.VERBOSE) {
                Log.m434v(TAG, "copySoLibToHost>>() return false");
            }
            return false;
        }

        public boolean copyPluginFromAssets(String assetsPath, String targetPath, StorageException e) throws RemoteException {
            if (Log.VERBOSE) {
                Log.m434v(TAG, "copyPluginFromAssets<<(assetsPath=" + assetsPath + ", targetPath=" + targetPath + ")");
            }
            if (Log.VERBOSE) {
                Log.m434v(TAG, "copyPluginFromAssets>>() return false");
            }
            return false;
        }

        public void removePluginFiles(HostPluginInfo hPluginInfo, PluginInfo info) throws RemoteException {
            if (Log.VERBOSE) {
                Log.m434v(TAG, "removePluginFiles<<(info=" + info + ")");
            }
            if (Log.VERBOSE) {
                Log.m434v(TAG, "removePluginFiles>>()");
            }
        }
    }

    public static synchronized void initizlie(Context context, AppInfo info, boolean isMultiProcess) {
        synchronized (StorageManager.class) {
            if (sInstance == null) {
                sInstance = new StorageManager(context, info, isMultiProcess);
            }
        }
    }

    public static StorageManager instance() {
        return sInstance;
    }

    private StorageManager(Context context, AppInfo info, boolean isMultiProcess) {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "StorageManager<<(context=" + context + ", info=" + info + ", isMultiProcess" + isMultiProcess + ")");
        }
        this.mHostApplicationContext = context;
        this.mAppInfo = info;
        this.mIsMultiProcess = isMultiProcess;
        this.mFileRootPath = this.mHostApplicationContext.getFilesDir().getAbsolutePath();
        if ("mounted".equals(Environment.getExternalStorageState())) {
            File filePathSD = this.mHostApplicationContext.getExternalFilesDir("plugin");
            if (filePathSD != null) {
                this.mFileRootPathSd = filePathSD.getAbsolutePath();
            } else {
                this.mFileRootPathSd = "";
            }
        } else {
            this.mFileRootPathSd = "";
        }
        if (this.mIsMultiProcess) {
            this.mEmptyServer = new EmptyStorageServer();
        }
        this.mStorageStub = new StorageStub(context, info);
        if (Log.VERBOSE) {
            Log.m434v(TAG, "StorageManager>>()mFileRootPath->" + this.mFileRootPath);
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "StorageManager>>()mFileRootPathSd->" + this.mFileRootPathSd);
        }
    }

    public String getPluginFileRootPath() {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "StorageManager>> mPluginFileRootPath=" + this.mFileRootPath);
        }
        return this.mFileRootPath;
    }

    public String getPluginFileRootPathSd() {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "StorageManager>> mPluginFileRootPathSd=" + this.mFileRootPathSd);
        }
        return this.mFileRootPathSd;
    }

    private synchronized IStorageServer getStorageServer() {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "getStorageServer<<()");
        }
        if (!this.mConnected.get()) {
            connect();
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "getStorageServer>>() return " + this.mStorageServer);
        }
        return this.mStorageServer;
    }

    private boolean isMultiProcessPlugin(String pluginId) {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "isMultiProcessPlugin<<()  " + pluginId);
        }
        boolean b = this.mIsMultiProcess && !PluginType.DEFAULT_SINGLE_PROCESS_TYPE.equals((String) this.mAppInfo.getPluginTypes().get(pluginId));
        if (Log.VERBOSE) {
            Log.m434v(TAG, "isMultiProcessPlugin>>() return " + b);
        }
        return b;
    }

    private void connect() {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "connect<<()");
        }
        Intent intent = new Intent(ActionSet.SER_STORAGE);
        intent.setPackage(this.mHostApplicationContext.getPackageName());
        Bundle extras = new Bundle();
        DataUtils.putAppInfo(extras, this.mAppInfo);
        intent.putExtras(extras);
        if (Log.VERBOSE) {
            Log.m434v(TAG, "connect...() intent=" + intent.toString() + ", context=" + this.mHostApplicationContext);
        }
        final ConditionVariable lock = new ConditionVariable();
        boolean success = this.mHostApplicationContext.bindService(intent, new ServiceConnection() {
            public void onServiceDisconnected(ComponentName name) {
                if (Log.VERBOSE) {
                    Log.m434v(StorageManager.TAG, "connect...() bindService onServiceDisconnected!!");
                }
                StorageManager.this.mConnected.set(false);
                StorageManager.this.mStorageServer = StorageManager.this.mEmptyServer;
                lock.open();
            }

            public void onServiceConnected(ComponentName name, IBinder service) {
                if (Log.VERBOSE) {
                    Log.m434v(StorageManager.TAG, "connect...() bindService onServiceConnected!! service=1" + service);
                }
                StorageManager.this.mStorageServer = Stub.asInterface(service);
                if (Log.VERBOSE) {
                    Log.m434v(StorageManager.TAG, "connect...() bindService onServiceConnected!! service=2" + StorageManager.this.mStorageServer);
                }
                StorageManager.this.mConnected.set(true);
                lock.open();
            }
        }, 1);
        if (Log.VERBOSE) {
            Log.m434v(TAG, "connect...() bindService return " + success);
        }
        if (success) {
            lock.block();
        } else {
            this.mConnected.set(false);
            this.mStorageServer = this.mEmptyServer;
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "connect>>()");
        }
    }

    public List<PluginInfo> loadPluginInfos(HostPluginInfo hPluginInfo, boolean needRemoveDumy) {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "loadPluginInfos<<(hPluginInfo=" + hPluginInfo + ", needRemoveDumy=" + needRemoveDumy + ")");
        }
        List<PluginInfo> list = new ArrayList();
        if (isMultiProcessPlugin(hPluginInfo.getPluginId())) {
            try {
                list.addAll(getStorageServer().loadPluginInfos(hPluginInfo, needRemoveDumy));
            } catch (RemoteException e) {
                list.clear();
                e.printStackTrace();
            }
        } else {
            list.addAll(this.mStorageStub.loadPluginInfos(hPluginInfo, needRemoveDumy));
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "loadPluginInfos>>() return list=" + list);
        }
        return list;
    }

    public void savePluginInfos(HostPluginInfo hPluginInfo, List<PluginInfo> list) {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "savePluginInfos<<(hPluginInfo=" + hPluginInfo + ", list=" + list + ")");
        }
        if (isMultiProcessPlugin(hPluginInfo.getPluginId())) {
            try {
                getStorageServer().savePluginInfos(hPluginInfo, list);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            this.mStorageStub.savePluginInfos(hPluginInfo, list);
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "savePluginInfos>>()");
        }
    }

    private PluginInfo findPluginInfo(List<PluginInfo> list, String version) {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "findPluginInfo<<(list=" + list + ", version=" + version + ")");
        }
        PluginInfo find = null;
        for (PluginInfo info : list) {
            if (info != null && Util.equals(info.getVersionName(), version)) {
                find = info;
                break;
            }
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "findPluginInfo>>() return " + find);
        }
        return find;
    }

    public PluginInfo loadPluginInfo(HostPluginInfo hPluginInfo, String versionName) {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "loadPluginInfo<<(hPluginInfo=" + hPluginInfo + ", versionName=" + versionName + ")");
        }
        PluginInfo info = null;
        List list = null;
        if (isMultiProcessPlugin(hPluginInfo.getPluginId())) {
            try {
                list = getStorageServer().loadPluginInfos(hPluginInfo, false);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            list = this.mStorageStub.loadPluginInfos(hPluginInfo, false);
        }
        if (!ListUtils.isEmpty(list)) {
            info = findPluginInfo(list, versionName);
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "loadPluginInfo>>() return " + info);
        }
        return info;
    }

    public void savePluginInfo(PluginInfo info, HostPluginInfo hInfo) {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "savePluginInfo<<(info=" + info + "HostPluginInfo" + hInfo + ")");
        }
        List<PluginInfo> list;
        PluginInfo find;
        if (isMultiProcessPlugin(info.getId())) {
            try {
                list = getStorageServer().loadPluginInfos(hInfo, false);
                find = findPluginInfo(list, info.getVersionName());
                if (find != null) {
                    list.remove(find);
                }
                list.add(info);
                getStorageServer().savePluginInfos(hInfo, list);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            list = this.mStorageStub.loadPluginInfos(hInfo, false);
            find = findPluginInfo(list, info.getVersionName());
            if (find != null) {
                list.remove(find);
            }
            list.add(info);
            this.mStorageStub.savePluginInfos(hInfo, list);
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "savePluginInfo>>()");
        }
    }

    public void removePluginFiles(HostPluginInfo hPluginInfo, PluginInfo info) {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "removePluginFiles<<(info=" + info + ")");
        }
        if (isMultiProcessPlugin(info.getId())) {
            try {
                getStorageServer().removePluginFiles(hPluginInfo, info);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            this.mStorageStub.removePluginFiles(hPluginInfo, info);
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "removePluginFiles>>()");
        }
    }

    public PluginInfo parsePluginInfo(HostPluginInfo hPluginInfo, String version, String fileName) {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "parsePluginInfo<<(hPluginInfo=" + hPluginInfo + ", version=" + version + ", fileName=" + fileName + ")");
        }
        String savePath = FileUtils.toFilePath(this.mFileRootPath, hPluginInfo.getPluginId(), hPluginInfo.getHostVersion(), version, fileName);
        String savePathSd = FileUtils.toFilePath(this.mFileRootPathSd, hPluginInfo.getPluginId(), hPluginInfo.getHostVersion(), version, fileName);
        PluginInfo info = new PluginInfo(hPluginInfo.getPluginId(), version, PLUGIN_CLASS_NAME, savePath);
        info.setPluginPathSd(savePathSd);
        if (Log.VERBOSE) {
            Log.m434v(TAG, "parsePluginInfo>>() return " + info);
        }
        return info;
    }

    public boolean startDownload(DownloadInfo info, String pluginId) throws Throwable {
        boolean success;
        if (Log.VERBOSE) {
            Log.m434v(TAG, "startDownload<<(info=" + info + ")");
        }
        StorageException exception = new StorageException();
        if (isMultiProcessPlugin(pluginId)) {
            success = getStorageServer().download(info, exception);
        } else {
            success = this.mStorageStub.download(info, exception);
        }
        if (exception.getThrowable() != null) {
            throw exception.getThrowable();
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "startDownload>>()" + success);
        }
        return success;
    }

    public boolean copySoLibToHost(PluginInfo info) throws Throwable {
        boolean success;
        if (Log.VERBOSE) {
            Log.m434v(TAG, "copySoLibToHost<<(info=" + info + ")");
        }
        StorageException exception = new StorageException();
        if (isMultiProcessPlugin(info.getId())) {
            success = getStorageServer().copySoLibToHost(info, exception);
        } else {
            success = this.mStorageStub.copySoLibToHost(info, exception);
        }
        if (exception.getThrowable() != null) {
            throw exception.getThrowable();
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "copySoLibToHost>>()");
        }
        return success;
    }

    public PluginInfo loadPluginInfoFromAssets(HostPluginInfo hPluginInfo, boolean useSd) throws Throwable {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "loadPluginInfoFromAssets<<(hPluginInfo=" + hPluginInfo + ")");
        }
        PluginInfo find = null;
        String assetsPath = null;
        String[] fileNames = this.mHostApplicationContext.getAssets().list("plugin");
        if (fileNames != null && fileNames.length > 0) {
            for (String fileName : fileNames) {
                PluginInfo assets = parsePluginInfoFromAssets(hPluginInfo, fileName);
                if (assets != null) {
                    find = assets;
                    assetsPath = FileUtils.toFilePath("plugin", fileName);
                    break;
                }
                if (Log.VERBOSE) {
                    Log.m434v(TAG, "fileNames= nomatch)");
                }
            }
        }
        if (find != null) {
            boolean success;
            StorageException exception = new StorageException();
            String path = "";
            if (useSd) {
                find.setTrySd(true);
            } else {
                find.setTrySd(false);
            }
            path = find.getPath();
            if (Log.VERBOSE) {
                Log.m434v(TAG, "path path=" + path + ")");
            }
            if (isMultiProcessPlugin(hPluginInfo.getPluginId())) {
                success = getStorageServer().copyPluginFromAssets(assetsPath, path, exception);
            } else {
                success = this.mStorageStub.copyPluginFromAssets(assetsPath, path, exception);
            }
            if (DEBUG_PROPERTY.PLAYER_PLUGIN_COPY_FROM_ASSETS_FAILED.endsWith(hPluginInfo.getPluginId()) && PluginDebugUtils.needThrowable(DEBUG_PROPERTY.PLAYER_PLUGIN_COPY_FROM_ASSETS_FAILED)) {
                success = false;
                exception.setThrowable(new Exception("(assets) copy from Assets failed!!(for pluginplayer debug!)"));
            }
            if (success) {
                boolean soSuccess;
                if (isMultiProcessPlugin(hPluginInfo.getPluginId())) {
                    soSuccess = getStorageServer().copySoLibToHost(find, exception);
                } else {
                    soSuccess = this.mStorageStub.copySoLibToHost(find, exception);
                }
                if (soSuccess) {
                    PluginInfo info = find;
                    if (Log.VERBOSE) {
                        Log.m434v(TAG, "loadPluginInfoFromAssets>>() return++ " + info);
                    }
                    return info;
                } else if (exception.getThrowable() != null) {
                    throw exception.getThrowable();
                } else {
                    throw new Exception("copySoLibToHost fail and no throwable");
                }
            } else if (exception.getThrowable() != null) {
                throw exception.getThrowable();
            } else {
                throw new Exception("copyPluginFromAssets fail and no throwable");
            }
        } else if (fileNames == null || fileNames.length <= 0) {
            throw new Exception("find is null!" + fileNames);
        } else {
            StringBuilder sb = new StringBuilder(hPluginInfo.getPluginId());
            for (String fileName2 : fileNames) {
                sb.append(fileName2).append("/");
            }
            throw new Exception("fileNames is" + fileNames.length + sb.toString());
        }
    }

    private PluginInfo parsePluginInfoFromAssets(HostPluginInfo hPluginInfo, String assetsFileName) throws Exception {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "parsePluginInfoFromAssets<<(assetsFileName=" + assetsFileName + ")" + "hPluginInfo " + hPluginInfo);
        }
        PluginInfo info = null;
        if (Util.isEmpty(assetsFileName)) {
            throw new Exception("assetsFileName is empty" + assetsFileName);
        }
        String fileNameNoEx = FileUtils.getFileNameNoEx(assetsFileName);
        if (Util.isEmpty(fileNameNoEx)) {
            throw new Exception("fileNameNoEx is empty" + fileNameNoEx);
        }
        String[] strings = fileNameNoEx.split(SPLIT);
        if (strings.length >= 3) {
            if (Util.equals(strings[0], hPluginInfo.getPluginId())) {
                info = parsePluginInfo(hPluginInfo, strings[2], assetsFileName);
            } else if (Log.VERBOSE) {
                Log.m434v(TAG, " no match");
            }
            if (Log.VERBOSE) {
                Log.m434v(TAG, "parsePluginInfoFromAssets>>() return " + info);
            }
            return info;
        }
        throw new Exception("strings.length < 3" + strings.length);
    }
}

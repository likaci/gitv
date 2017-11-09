package com.gala.sdk.plugin.server.storage;

import android.content.Context;
import android.os.RemoteException;
import com.gala.sdk.plugin.AppInfo;
import com.gala.sdk.plugin.HostPluginInfo;
import com.gala.sdk.plugin.Log;
import com.gala.sdk.plugin.server.download.DownloadInfo;
import com.gala.sdk.plugin.server.download.DownloadManager;
import com.gala.sdk.plugin.server.utils.FileUtils;
import com.gala.sdk.plugin.server.utils.PluginDebugUtils;
import com.gala.sdk.plugin.server.utils.PluginDebugUtils.DEBUG_PROPERTY;
import java.util.Collections;
import java.util.List;

public class StorageStub {
    private static final int DOWNLOAD_THREAD_COUNT = 1;
    private static final String KEY_PLUGININFO_PREFIX = "plugin_info_";
    private static final int MAX_LOCAL_PLUGIN_COUNT = 2;
    private static final String SHARED_NAME = "plugin_info_list_";
    private final String TAG = ("StorageStub@" + Integer.toHexString(hashCode()));
    private final Context mContext;
    private final DownloadManager mDownloadManager;

    public StorageStub(Context context, AppInfo info) {
        if (Log.VERBOSE) {
            Log.v(this.TAG, "StorageStub<<(context=" + context + ", info=" + info + ")");
        }
        this.mContext = context;
        this.mDownloadManager = new DownloadManager(1);
        if (Log.VERBOSE) {
            Log.v(this.TAG, "StorageStub>>()");
        }
    }

    public synchronized boolean download(DownloadInfo info, StorageException se) throws RemoteException {
        boolean success;
        if (Log.VERBOSE) {
            Log.v(this.TAG, "downloadApk<<(info=" + info + ")");
        }
        try {
            success = this.mDownloadManager.downloadPluginApk(info);
        } catch (Exception e) {
            success = false;
            se.setThrowable(e);
        }
        if (Log.VERBOSE) {
            Log.v(this.TAG, "downloadApk>>() return " + success);
        }
        return success;
    }

    public synchronized void removePluginFiles(HostPluginInfo hPluginInfo, PluginInfo info) {
        if (Log.VERBOSE) {
            Log.v(this.TAG, "removePluginFiles<<(info=" + info + ")");
        }
        FileUtils.deleteFile(info.getLibFolder());
        FileUtils.deleteFile(info.getPath());
        this.mContext.getSharedPreferences(SHARED_NAME + hPluginInfo.getPluginId() + hPluginInfo.getHostVersion(), 0).edit().remove(KEY_PLUGININFO_PREFIX + hPluginInfo.getPluginId());
        if (Log.VERBOSE) {
            Log.v(this.TAG, "removePluginFiles>>()" + hPluginInfo.getPluginId());
        }
    }

    public synchronized void savePluginInfos(HostPluginInfo hPluginInfo, List<PluginInfo> list) {
        if (Log.VERBOSE) {
            Log.v(this.TAG, "savePluginInfos<<(hPluginInfo=" + hPluginInfo + ", list=" + list + ")");
        }
        this.mContext.getSharedPreferences(SHARED_NAME + hPluginInfo.getPluginId() + hPluginInfo.getHostVersion(), 0).edit().putString(KEY_PLUGININFO_PREFIX + hPluginInfo.getPluginId(), PluginInfo.getJsonFromList(list)).apply();
        if (Log.VERBOSE) {
            Log.v(this.TAG, "savePluginInfos>>()");
        }
    }

    public synchronized boolean copyPluginFromAssets(String assetsPath, String targetPath, StorageException se) {
        boolean success;
        if (Log.VERBOSE) {
            Log.v(this.TAG, "copyPluginFromAssets<<(assetsPath=" + assetsPath + ", targetPath=" + targetPath + ")");
        }
        try {
            success = FileUtils.copyFromAssets(this.mContext, assetsPath, targetPath);
        } catch (Throwable e) {
            success = false;
            se.setThrowable(e);
        }
        if (PluginDebugUtils.needThrowable(DEBUG_PROPERTY.COPY_FROM_ASSETS_FAILED)) {
            success = false;
            se.setThrowable(new Exception("(assetsPath=" + assetsPath + ", targetPath=" + targetPath + ") copy from Assets failed!!(for debug!)"));
        }
        if (Log.VERBOSE) {
            Log.v(this.TAG, "copyPluginFromAssets>>() return " + success);
        }
        return success;
    }

    public synchronized List<PluginInfo> loadPluginInfos(HostPluginInfo hPluginInfo, boolean needRemoveDumy) {
        List<PluginInfo> historyList;
        if (Log.VERBOSE) {
            Log.v(this.TAG, "loadPluginInfos<<(hPluginInfo=" + hPluginInfo + ", needRemoveDumy=" + needRemoveDumy + ")");
        }
        String historyInfo = this.mContext.getSharedPreferences(SHARED_NAME + hPluginInfo.getPluginId() + hPluginInfo.getHostVersion(), 0).getString(KEY_PLUGININFO_PREFIX + hPluginInfo.getPluginId(), null);
        historyList = PluginInfo.getListFromJson(historyInfo);
        Collections.sort(historyList, PluginInfo.COMPARATOR);
        if (needRemoveDumy) {
            checkAndRemoveDummy(hPluginInfo, historyList);
        }
        if (Log.VERBOSE) {
            Log.v(this.TAG, "loadPluginInfos>>() return " + historyInfo);
        }
        return historyList;
    }

    private void checkAndRemoveDummy(HostPluginInfo hPluginInfo, List<PluginInfo> list) {
        if (Log.VERBOSE) {
            Log.v(this.TAG, "checkAndRemoveDummy<<(list=" + list + ")");
        }
        int lastSuccessIndex = 0;
        for (PluginInfo info : list) {
            if (lastSuccessIndex > 2 || !info.checkFileComplete()) {
                removePluginFiles(hPluginInfo, info);
            } else {
                lastSuccessIndex++;
            }
        }
        if (Log.VERBOSE) {
            Log.v(this.TAG, "checkAndRemoveDummy>>()");
        }
    }

    public synchronized boolean copySoLibToHost(PluginInfo info, StorageException se) {
        boolean success;
        if (Log.VERBOSE) {
            Log.v(this.TAG, "copySoLibToHost<<(info=" + info + ")");
        }
        try {
            success = SoLibHelper.copySoLibToHost(this.mContext, info);
        } catch (Throwable e) {
            success = false;
            se.setThrowable(e);
        }
        if (Log.VERBOSE) {
            Log.v(this.TAG, "copySoLibToHost>>() return " + success);
        }
        return success;
    }
}

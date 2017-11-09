package com.gala.video.lib.framework.core.network.downloadapp;

import android.content.Context;
import android.util.Log;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DownloadManager {
    public static final int APKFILE_EXIST = 1;
    public static final int APK_DOWNLOADING = 0;
    public static final int INIT_FAILURE = 2;
    private static final int MAX_TASK = 4;
    private static final String TAG = "DownloadManager";
    public static final int TASK_EXISTS = 3;
    public static final int TASK_FULL = 4;
    private static DownloadManager mInstance = new DownloadManager();
    private Context mContext;
    private String mSaveFileDir;
    private HashMap<String, DownloadThread> mTaskMap;

    public static DownloadManager getInstance() {
        return mInstance;
    }

    private DownloadManager() {
        this.mSaveFileDir = null;
        this.mTaskMap = null;
        this.mTaskMap = new LinkedHashMap();
    }

    public void init(Context context) {
        this.mContext = context;
        this.mSaveFileDir = context.getFilesDir() + "/";
    }

    private String getRootDir() {
        return this.mSaveFileDir;
    }

    public int start(String name, String url, DownloadListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("DownloadListener must be not null ! ");
        } else if (this.mTaskMap.size() >= 4) {
            return 4;
        } else {
            if (new File(this.mSaveFileDir, url.substring(url.lastIndexOf(47) + 1)).exists()) {
                return 1;
            }
            if (this.mContext == null) {
                Log.e(TAG, "DownloadManager ---mContext == null----");
                return 2;
            } else if (this.mTaskMap.containsKey(url)) {
                Log.e(TAG, "DownloadManager ---The task is already exist----");
                ((DownloadThread) this.mTaskMap.get(url)).setDownLoadListener(listener);
                return 3;
            } else {
                DownloadThread downloadThread = new DownloadThread(name, url, listener, this.mSaveFileDir);
                ThreadUtils.execute(downloadThread);
                this.mTaskMap.put(url, downloadThread);
                return 0;
            }
        }
    }

    public boolean stop(String url) {
        if (this.mContext == null) {
            Log.e(TAG, "DownloadManager ---stop() -> mContext == null");
            return false;
        } else if (!this.mTaskMap.containsKey(url)) {
            return false;
        } else {
            ((DownloadThread) this.mTaskMap.get(url)).setStop(true);
            this.mTaskMap.remove(url);
            return true;
        }
    }

    public boolean pause(String url) {
        if (this.mContext == null) {
            Log.e(TAG, "DownloadManager ---pause() ->mContext == null");
            return false;
        } else if (!this.mTaskMap.containsKey(url)) {
            return false;
        } else {
            ((DownloadThread) this.mTaskMap.get(url)).setPause(true);
            this.mTaskMap.remove(url);
            return true;
        }
    }

    public String getApkFilePath(String url) {
        if (!StringUtils.isEmpty((CharSequence) url)) {
            return getRootDir() + url.substring(url.lastIndexOf("/") + 1, url.length());
        }
        LogUtils.m1568d(TAG, "DownloadManager ---getApkFilePath() -> download apk url == null");
        return null;
    }
}

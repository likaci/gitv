package com.gitv.tvappstore.utils.download;

import android.content.Context;
import android.util.Log;
import com.gitv.tvappstore.utils.ThreadUtils;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class DownloadManager {
    public static final int APKFILE_EXIST = 1;
    public static final int APK_DOWNLOADING = 0;
    public static final int INIT_FAILURE = 2;
    private static final int MAX_TASK = 4;
    private static String TAG = "DownloadManager";
    public static final int TASK_EXISTS = 3;
    public static final int TASK_FULL = 4;
    private static DownloadManager instance = new DownloadManager();
    private Map<String, DownloadThread> taskMap;

    public static DownloadManager getInstance() {
        return instance;
    }

    private DownloadManager() {
        this.taskMap = null;
        this.taskMap = new LinkedHashMap();
    }

    public int start(String name, String url, DownloadListener listener, String dstDir) {
        if (this.taskMap.size() >= 4) {
            return 4;
        }
        if (new File(dstDir, url.substring(url.lastIndexOf(47) + 1)).exists()) {
            return 1;
        }
        if (this.taskMap.containsKey(url)) {
            Log.e(TAG, "----The task is already exist----");
            ((DownloadThread) this.taskMap.get(url)).setDownLoadListener(listener);
            return 3;
        }
        DownloadThread downloadThread = new DownloadThread(name, url, listener, dstDir);
        ThreadUtils.execute(downloadThread);
        this.taskMap.put(url, downloadThread);
        return 0;
    }

    public boolean queryProgress(String url, DownloadListener listener) {
        if (!this.taskMap.containsKey(url)) {
            return false;
        }
        Log.e(TAG, "----The task is already exist----");
        ((DownloadThread) this.taskMap.get(url)).setDownLoadListener(listener);
        return true;
    }

    public static boolean isdownload(Context context, String url) {
        if (url == null || url.isEmpty()) {
            Log.d(TAG, TAG + "file is not download");
        } else if (new File(context.getFilesDir() + "/", url.substring(url.lastIndexOf(47) + 1)).exists()) {
            return true;
        }
        return false;
    }

    public boolean stop(String url) {
        if (!this.taskMap.containsKey(url)) {
            return false;
        }
        ((DownloadThread) this.taskMap.get(url)).setStop(true);
        this.taskMap.remove(url);
        return true;
    }

    public boolean pause(String url) {
        if (!this.taskMap.containsKey(url)) {
            return false;
        }
        ((DownloadThread) this.taskMap.get(url)).setPause(true);
        this.taskMap.remove(url);
        return true;
    }
}

package com.tvos.downloadmanager;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;

public class DownloadManagerFactory {
    public static final String DEFAULT_DBNAME = "downloadmanager.db";
    private static Map<String, DownloadManager> mDownloadManagerMap = new HashMap();

    public static synchronized IDownloadManager createDownloadManager(Context context, String dbName) {
        IDownloadManager iDownloadManager;
        synchronized (DownloadManagerFactory.class) {
            if (dbName == null) {
                dbName = "downloadmanager.db";
            }
            if (!mDownloadManagerMap.containsKey(dbName)) {
                DownloadManager downloadManager = new DownloadManager(context, dbName);
                System.out.println("add " + dbName + " succeed");
                mDownloadManagerMap.put(dbName, downloadManager);
            }
            iDownloadManager = (IDownloadManager) mDownloadManagerMap.get(dbName);
        }
        return iDownloadManager;
    }

    public static synchronized IDownloadManager createDownloadManager(Context context, String dbName, int speedLimitDegree) {
        IDownloadManager iDownloadManager;
        synchronized (DownloadManagerFactory.class) {
            if (dbName == null) {
                dbName = "downloadmanager.db";
            }
            if (!mDownloadManagerMap.containsKey(dbName)) {
                DownloadManager downloadManager = new DownloadManager(context, dbName, speedLimitDegree);
                System.out.println("add " + dbName + " succeed");
                mDownloadManagerMap.put(dbName, downloadManager);
            }
            iDownloadManager = (IDownloadManager) mDownloadManagerMap.get(dbName);
        }
        return iDownloadManager;
    }

    public static IDownloadManager getDownloadManager(String dbName) {
        if (mDownloadManagerMap != null) {
            return (IDownloadManager) mDownloadManagerMap.get(dbName);
        }
        return null;
    }

    public static void release(String dbName) {
        if (mDownloadManagerMap.containsKey(dbName)) {
            ((DownloadManager) mDownloadManagerMap.get(dbName)).release();
            mDownloadManagerMap.remove(dbName);
        }
    }
}

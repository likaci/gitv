package com.tvos.downloadmanager.download;

import com.tvos.downloadmanager.data.DownloadParam;

public class DownloadTaskFactory {
    public static synchronized IDownloadTask createDownloadTask(DownloadParam downloadParam, DownloadThreadPool pool) {
        IDownloadTask task;
        synchronized (DownloadTaskFactory.class) {
            task = new MultiDownloadTask(downloadParam, pool);
            downloadParam.setP2pDownloadError(false);
            downloadParam.setP2PDownload(false);
        }
        return task;
    }
}

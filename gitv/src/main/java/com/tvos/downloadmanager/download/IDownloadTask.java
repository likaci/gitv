package com.tvos.downloadmanager.download;

import com.tvos.downloadmanager.data.DownloadParam;

public interface IDownloadTask {
    void close();

    DownloadParam getCurrentDownloadParam();

    long getDownloadSize();

    long getSpeed();

    void removeDownload();

    void setDownloadTaskListener(IDownloadTaskListener iDownloadTaskListener);

    void startDownload();

    void startRestDownload();

    void stopDownload();
}

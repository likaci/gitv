package com.tvos.downloadmanager.download;

import com.tvos.downloadmanager.data.DownloadParam;

public interface IDownload {
    long getDownloadSize(int i);

    long getSpeed(int i);

    int getStartedTaskSize();

    boolean isFull();

    boolean quit();

    boolean remove(int i);

    void setListener(IDownloadStatusListener iDownloadStatusListener);

    void setTaskSizeListener(IDownloadTaskSizeListener iDownloadTaskSizeListener);

    boolean start(DownloadParam downloadParam);

    void startDownloadRemainFilesInTasks();

    boolean stop(int i);
}

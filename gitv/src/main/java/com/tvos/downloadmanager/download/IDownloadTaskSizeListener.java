package com.tvos.downloadmanager.download;

public interface IDownloadTaskSizeListener {
    void onDownloadSizeIncreased(int i);

    void onDownloadSizeReduced(int i);
}

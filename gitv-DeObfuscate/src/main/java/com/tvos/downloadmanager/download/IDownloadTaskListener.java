package com.tvos.downloadmanager.download;

import com.tvos.downloadmanager.data.DownloadProgressRecord;

public interface IDownloadTaskListener {
    void onComplete(int i);

    void onError(int i, int i2, String str);

    void onProgress(int i, int i2);

    void onSaveProgress(DownloadProgressRecord downloadProgressRecord);

    void onSpeedUpdated(int i, long j);

    void onStarted(int i);

    void onStopped(int i);
}

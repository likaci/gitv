package com.tvos.downloadmanager.download;

import com.tvos.downloadmanager.data.DownloadInfo;

public interface IDownloadStatusListener {
    void onAdd(DownloadInfo downloadInfo);

    void onComplete(int i);

    void onError(int i, int i2, String str);

    void onPaused(int i);

    void onPauseing(int i);

    void onProgress(int i, int i2);

    void onRemove(int i);

    void onSpeedUpdated(int i, long j);

    void onStart(int i);

    void onStarting(int i);

    void onStop(int i);

    void onWait(int i);
}

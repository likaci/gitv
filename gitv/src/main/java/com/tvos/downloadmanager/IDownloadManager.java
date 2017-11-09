package com.tvos.downloadmanager;

import com.tvos.downloadmanager.data.DownloadInfo;
import com.tvos.downloadmanager.data.RequestInfo;
import com.tvos.downloadmanager.download.IDownloadStatusListener;
import java.util.List;

public interface IDownloadManager {
    boolean enqueue(RequestInfo requestInfo);

    DownloadInfo getDownloadInfo(int i);

    DownloadInfo getDownloadInfoByUrl(String str);

    List<DownloadInfo> getDownloadInfoList();

    long getDownloadSize(int i);

    int getDownloadStatus(int i);

    long getFileSize(int i);

    long getSpeed(int i);

    boolean pause(int i);

    boolean remove(int i);

    boolean resume(int i);

    boolean resume(int i, int i2);

    void setListener(IDownloadStatusListener iDownloadStatusListener);

    boolean start(int i);

    boolean start(int i, int i2);

    void startAll();

    void stopAll();
}

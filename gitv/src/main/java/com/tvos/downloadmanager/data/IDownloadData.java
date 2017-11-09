package com.tvos.downloadmanager.data;

import com.tvos.downloadmanager.download.IDownloadStatusListener;
import java.util.List;

public interface IDownloadData {
    DownloadInfo getDownloadInfo(int i);

    DownloadInfo getDownloadInfoByUrl(String str);

    List<DownloadInfo> getDownloadInfoList();

    DownloadParam getDownloadParamByUrl(String str);

    DownloadParam getDownloadParm(int i);

    int getDownloadStatus(int i);

    List<DownloadParam> getNextWaits(int i);

    int insert(RequestInfo requestInfo);

    boolean isExist(RequestInfo requestInfo);

    boolean isValid(int i);

    void release();

    boolean remove(int i);

    boolean removeDownloadFile(int i);

    void resetDownloadRecord(int i);

    void setListener(IDownloadStatusListener iDownloadStatusListener);

    void updateDownloadParm(DownloadParam downloadParam);

    void updateDownloadProgress(DownloadProgressRecord downloadProgressRecord);
}

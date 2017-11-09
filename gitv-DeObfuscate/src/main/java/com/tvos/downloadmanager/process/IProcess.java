package com.tvos.downloadmanager.process;

import com.tvos.downloadmanager.data.RequestInfo;
import com.tvos.downloadmanager.download.IDownloadStatusListener;

public interface IProcess {
    boolean process(int i);

    boolean process(int i, int i2);

    boolean process(RequestInfo requestInfo);

    void setDownloadStatusListener(IDownloadStatusListener iDownloadStatusListener);
}

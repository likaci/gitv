package com.tvos.downloadmanager.data;

import com.tvos.downloadmanager.download.DownloadThread;

public class DownloadThreadInfo {
    public boolean isError;
    public boolean isStarted;
    public boolean isStopped;
    public int pointId;
    public DownloadThread thread;
}

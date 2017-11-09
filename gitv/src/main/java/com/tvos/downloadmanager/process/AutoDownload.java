package com.tvos.downloadmanager.process;

import android.util.Log;
import com.tvos.downloadmanager.data.DownloadParam;
import com.tvos.downloadmanager.data.IDownloadData;
import com.tvos.downloadmanager.download.IDownload;
import java.util.List;

public class AutoDownload {
    private static final String TAG = "AutoDownload";
    private IDownload mDownload = null;
    private IDownloadData mDownloadData = null;

    public void init(IDownload download, IDownloadData downloadDataDB) {
        this.mDownload = download;
        this.mDownloadData = downloadDataDB;
    }

    public void startWaitedTasks(int remainSize) {
        Log.d(TAG, "startWaitedTasks remainSize " + remainSize);
        if (remainSize != 0 && this.mDownload != null && this.mDownloadData != null) {
            List<DownloadParam> paramList = this.mDownloadData.getNextWaits(remainSize);
            if (paramList != null) {
                for (DownloadParam param : paramList) {
                    this.mDownload.start(param);
                }
            }
        }
    }

    public void startDownloadRemainFilesInTasks() {
        Log.d(TAG, "startDownloadRemainFilesInTasks");
        if (this.mDownload != null) {
            this.mDownload.startDownloadRemainFilesInTasks();
        }
    }

    public void release() {
        if (this.mDownload != null) {
            this.mDownload.setTaskSizeListener(null);
        }
        this.mDownload = null;
    }
}

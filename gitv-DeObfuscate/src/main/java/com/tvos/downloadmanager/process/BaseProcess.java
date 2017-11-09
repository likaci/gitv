package com.tvos.downloadmanager.process;

import android.content.Context;
import com.tvos.downloadmanager.data.IDownloadData;
import com.tvos.downloadmanager.data.RequestInfo;
import com.tvos.downloadmanager.download.IDownload;
import com.tvos.downloadmanager.download.IDownloadStatusListener;

public class BaseProcess implements IProcess {
    private Context mContext;
    private IDownload mDownload;
    private IDownloadData mDownloadData;
    private IDownloadStatusListener mDownloadStatusListener;

    public BaseProcess(Context context) {
        setContext(this.mContext);
    }

    public boolean process(int id) {
        return true;
    }

    public boolean process(int id, int speedLimitDegree) {
        return true;
    }

    public boolean process(RequestInfo requestInfo) {
        return true;
    }

    public Context getContext() {
        return this.mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setDownloadStatusListener(IDownloadStatusListener listener) {
        this.mDownloadStatusListener = listener;
    }

    protected IDownloadStatusListener getDownloadStatusListener() {
        return this.mDownloadStatusListener;
    }

    public void release() {
        this.mContext = null;
        this.mDownloadStatusListener = null;
    }

    public IDownload getDownload() {
        return this.mDownload;
    }

    public void setDownload(IDownload mDownload) {
        this.mDownload = mDownload;
    }

    public IDownloadData getDownloadData() {
        return this.mDownloadData;
    }

    public void setDownloadData(IDownloadData mDownloadData) {
        this.mDownloadData = mDownloadData;
    }
}

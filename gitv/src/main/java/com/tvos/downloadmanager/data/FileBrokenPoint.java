package com.tvos.downloadmanager.data;

public class FileBrokenPoint {
    private long downloadSize;
    private int fbpid;
    private long filePosition;
    private boolean isError;
    private long reqSize;
    private int status;

    public int getFbpid() {
        return this.fbpid;
    }

    public void setFbpid(int fbpid) {
        this.fbpid = fbpid;
    }

    public long getDownloadSize() {
        return this.downloadSize;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public long getFilePosition() {
        return this.filePosition;
    }

    public void setFilePosition(long filePosition) {
        this.filePosition = filePosition;
    }

    public long getReqSize() {
        return this.reqSize;
    }

    public void setReqSize(long reqSize) {
        this.reqSize = reqSize;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isError() {
        return this.isError;
    }

    public void setError(boolean isError) {
        this.isError = isError;
    }
}

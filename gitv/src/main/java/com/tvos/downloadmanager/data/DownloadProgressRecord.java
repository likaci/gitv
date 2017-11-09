package com.tvos.downloadmanager.data;

import java.util.ArrayList;
import java.util.List;

public class DownloadProgressRecord {
    private long downloadSize;
    private long downloadTime;
    private long filesize;
    private int id;
    private boolean isMulthread;
    private boolean isResumeBroken;
    private List<FileBrokenPoint> multiInfos;

    public DownloadProgressRecord(DownloadParam param) {
        setId(param.getId());
        setDownloadSize(param.getDownloadSize());
        setFilesize(param.getFileSize());
        setResumeBroken(param.isResumeBroken());
        setDownloadTime(param.getDownloadTime());
        if (param.getMultiInfos() != null) {
            List<FileBrokenPoint> pmultiInfos = new ArrayList();
            for (int i = 0; i < param.getMultiInfos().size(); i++) {
                int fbpid = ((FileBrokenPoint) param.getMultiInfos().get(i)).getFbpid();
                long downloadSizeMul = ((FileBrokenPoint) param.getMultiInfos().get(i)).getDownloadSize();
                long filePosition = ((FileBrokenPoint) param.getMultiInfos().get(i)).getFilePosition();
                long reqSize = ((FileBrokenPoint) param.getMultiInfos().get(i)).getReqSize();
                FileBrokenPoint fbpdb = new FileBrokenPoint();
                fbpdb.setFbpid(fbpid);
                fbpdb.setDownloadSize(downloadSizeMul);
                fbpdb.setFilePosition(filePosition);
                fbpdb.setReqSize(reqSize);
                pmultiInfos.add(fbpdb);
            }
            setMultiInfos(pmultiInfos);
            return;
        }
        setMultiInfos(null);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDownloadSize() {
        return this.downloadSize;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public long getFilesize() {
        return this.filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public List<FileBrokenPoint> getMultiInfos() {
        return this.multiInfos;
    }

    public void setMultiInfos(List<FileBrokenPoint> multiInfos) {
        this.multiInfos = multiInfos;
    }

    public boolean isMulthread() {
        return this.isMulthread;
    }

    public void setMulthread(boolean isMulthread) {
        this.isMulthread = isMulthread;
    }

    public boolean isResumeBroken() {
        return this.isResumeBroken;
    }

    public void setResumeBroken(boolean isResumeBroken) {
        this.isResumeBroken = isResumeBroken;
    }

    public long getDownloadTime() {
        return this.downloadTime;
    }

    public void setDownloadTime(long downloadTime) {
        this.downloadTime = downloadTime;
    }
}

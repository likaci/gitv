package com.tvos.downloadmanager.data;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;

public class DownloadInfo {
    private String description;
    private int downloadId;
    private long downloadSize;
    private long downloadTime;
    private String filePath;
    private long fileSize;
    private String md5;
    private int speedLimitDegree;
    private int status;
    private String title;
    private String url;

    public long getDownloadTime() {
        return this.downloadTime;
    }

    public void setDownloadTime(long downloadTime) {
        this.downloadTime = downloadTime;
    }

    public int getSpeedLimitDegree() {
        return this.speedLimitDegree;
    }

    public void setSpeedLimitDegree(int speedLimitDegree) {
        this.speedLimitDegree = speedLimitDegree;
    }

    public int getDownloadId() {
        return this.downloadId;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDownloadSize() {
        return this.downloadSize;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String toString() {
        return "DownloadInfo [downloadId=" + this.downloadId + ", title=" + this.title + ", downloadSize=" + this.downloadSize + ", fileSize=" + this.fileSize + ", status=" + this.status + AlbumEnterFactory.SIGN_STR;
    }
}

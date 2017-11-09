package com.tvos.downloadmanager.data;

public class RequestInfo {
    private String description;
    private String destination;
    private long fileSize;
    private boolean isP2PDownload = false;
    private String md5;
    private String mimetype;
    private int speedLimitDegree = 0;
    private String title;
    private String uri;

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isP2PDownload() {
        return this.isP2PDownload;
    }

    public void setP2PDownload(boolean isP2PDownload) {
        this.isP2PDownload = isP2PDownload;
    }

    public int getSpeedLimitDegree() {
        return this.speedLimitDegree;
    }

    public void setSpeedLimitDegree(int speedLimitDegree) {
        this.speedLimitDegree = speedLimitDegree;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getMimetype() {
        return this.mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }
}

package com.tvos.downloadmanager.data;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.util.List;

public class DownloadParam {
    private String destination;
    private long downloadSize;
    private long downloadTime;
    private long filesize;
    private int id;
    private boolean isP2PDownload;
    private boolean isP2pDownloadError;
    private boolean isResumeBroken;
    private String md5;
    private String mimetype;
    private List<FileBrokenPoint> multiInfos;
    private boolean preAssignedThread;
    private int speedLimitDegree;
    private int status;
    private String title;
    private String uri;

    public boolean preAssignedThread() {
        return this.preAssignedThread;
    }

    public void setPreAssignedThread(boolean preAssignedThread) {
        this.preAssignedThread = preAssignedThread;
    }

    public boolean isP2PDownload() {
        return this.isP2PDownload;
    }

    public void setP2PDownload(boolean isP2PDownload) {
        this.isP2PDownload = isP2PDownload;
    }

    public boolean isP2pDownloadError() {
        return this.isP2pDownloadError;
    }

    public void setP2pDownloadError(boolean isP2pDownloadError) {
        this.isP2pDownloadError = isP2pDownloadError;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public long getDownloadSize() {
        return this.downloadSize;
    }

    public long getFileSize() {
        return this.filesize;
    }

    public void setFileSize(long size) {
        this.filesize = size;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public String getMimetype() {
        return this.mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public boolean isResumeBroken() {
        return this.isResumeBroken;
    }

    public void setResumeBroken(boolean isResumeBroken) {
        this.isResumeBroken = isResumeBroken;
    }

    public List<FileBrokenPoint> getMultiInfos() {
        return this.multiInfos;
    }

    public void setMultiInfos(List<FileBrokenPoint> multiInfos) {
        this.multiInfos = multiInfos;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String toString() {
        return "DownloadParam [id=" + this.id + ", title=" + this.title + ", downloadSize=" + this.downloadSize + ", filesize=" + this.filesize + ", uri=" + this.uri + ", md5=" + this.md5 + ", status=" + this.status + AlbumEnterFactory.SIGN_STR;
    }
}

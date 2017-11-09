package com.tvos.downloadmanager.data;

import java.util.ArrayList;
import java.util.List;

public class DownloadRecord {
    private String description;
    private String destination;
    private long downloadSize;
    private long downloadTime;
    private long fileSize;
    private int id;
    private boolean isP2PDownload;
    private boolean isP2pDownloadError;
    private boolean isResumeBroken;
    private String md5;
    private String mimetype;
    private List<FileBrokenPoint> multiInfos = new ArrayList();
    private int speedLimitDegree;
    private int status = -1;
    private String title;
    private String uri;

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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getDownloadSize() {
        return this.downloadSize;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
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

    public DownloadParam toDownloadParam() {
        DownloadParam downloadParam = new DownloadParam();
        downloadParam.setTitle(getTitle());
        downloadParam.setId(getId());
        downloadParam.setDestination(getDestination());
        downloadParam.setDownloadSize(getDownloadSize());
        downloadParam.setFileSize(getFileSize());
        downloadParam.setMd5(getMd5());
        downloadParam.setMimetype(getMimetype());
        downloadParam.setResumeBroken(isResumeBroken());
        downloadParam.setMultiInfos(getMultiInfos());
        downloadParam.setUri(getUri());
        downloadParam.setStatus(getStatus());
        downloadParam.setSpeedLimitDegree(getSpeedLimitDegree());
        downloadParam.setDownloadTime(getDownloadTime());
        downloadParam.setP2pDownloadError(isP2pDownloadError());
        downloadParam.setP2PDownload(this.isP2PDownload);
        return downloadParam;
    }

    public DownloadInfo toDownloadInfo() {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setDescription(getDescription());
        downloadInfo.setDownloadId(getId());
        downloadInfo.setDownloadSize(getDownloadSize());
        downloadInfo.setFileSize(getFileSize());
        downloadInfo.setFilePath(getDestination());
        downloadInfo.setStatus(getStatus());
        downloadInfo.setTitle(getTitle());
        downloadInfo.setUrl(getUri());
        downloadInfo.setMd5(getMd5());
        downloadInfo.setSpeedLimitDegree(getSpeedLimitDegree());
        downloadInfo.setDownloadTime(getDownloadTime());
        return downloadInfo;
    }

    public static DownloadRecord fromRequestInfo(RequestInfo info) {
        DownloadRecord record = new DownloadRecord();
        record.setTitle(info.getTitle());
        record.setUri(info.getUri());
        record.setDestination(info.getDestination());
        record.setMd5(info.getMd5());
        record.setMimetype(info.getMimetype());
        record.setStatus(-1);
        record.setDescription(info.getDescription());
        record.setSpeedLimitDegree(info.getSpeedLimitDegree());
        record.setP2PDownload(info.isP2PDownload());
        record.setFileSize(info.getFileSize());
        if (info.getTitle() == null || info.getTitle().equals("")) {
            record.setTitle(info.getUri().substring(info.getUri().lastIndexOf(47) + 1));
        } else {
            record.setTitle(info.getTitle());
        }
        return record;
    }

    public void fromDownloadParam(DownloadParam param) {
        setTitle(param.getTitle());
        setDownloadSize(param.getDownloadSize());
        setStatus(param.getStatus());
        setMultiInfos(param.getMultiInfos());
        setMimetype(param.getMimetype());
        setFileSize(param.getFileSize());
        setResumeBroken(param.isResumeBroken());
        setSpeedLimitDegree(getSpeedLimitDegree());
        setDownloadTime(param.getDownloadTime());
        setP2PDownload(param.isP2PDownload());
    }
}

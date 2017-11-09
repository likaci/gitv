package com.gala.video.lib.framework.core.network.download.core;

import java.util.HashMap;
import java.util.Map;

public class GalaDownloadParameter implements IGalaDownloadParameter {
    private long mConnectTimeOut = 15000;
    private long mDiskSizeLimit = IGalaDownloadParameter.DISK_SIZE_LIMIT;
    private String mDownloadUrl = null;
    private String mFileName = null;
    private Map<String, String> mHeaderMap = new HashMap(2);
    private int mLimitSpeed = 4096;
    private String mMd5Code = null;
    private long mRangeStartPoint = 0;
    private long mReadTimeOut = 15000;
    private int mReconnectTotal = 1;
    private String mSavePath = null;

    public void setDownloadUrl(String url) {
        this.mDownloadUrl = url;
    }

    public String getDownloadUrl() {
        return this.mDownloadUrl;
    }

    public void setFileName(String name) {
        this.mFileName = name;
    }

    public String getFileName() {
        return this.mFileName;
    }

    public void setSavePath(String savePath) {
        this.mSavePath = savePath;
    }

    public String getSavePath() {
        return this.mSavePath;
    }

    public void setDiskSizeLimit(long diskSizeLimit) {
        this.mDiskSizeLimit = diskSizeLimit;
    }

    public long getDiskSizeLimit() {
        return this.mDiskSizeLimit;
    }

    public void setRangeStartPoint(long point) {
        if (point != 0) {
            this.mRangeStartPoint = point;
            this.mHeaderMap.put("RANGE", "bytes=" + this.mRangeStartPoint + "-");
        }
    }

    public long getRangeStartPoint() {
        return this.mRangeStartPoint;
    }

    public void setConnectTimeOut(long timeOut) {
        this.mConnectTimeOut = timeOut;
    }

    public long getConnectTimeOut() {
        return this.mConnectTimeOut;
    }

    public void setReadTimeOut(long timeOut) {
        this.mReadTimeOut = timeOut;
    }

    public long getReadTimeOut() {
        return this.mReadTimeOut;
    }

    public void setLimitSpeed(int speed) {
        this.mLimitSpeed = speed;
    }

    public int getLimitSpeed() {
        return this.mLimitSpeed;
    }

    public void addHeader(String key, String value) {
        this.mHeaderMap.put(key, value);
    }

    public Map<String, String> getHeaderList() {
        return this.mHeaderMap;
    }

    public void setMD5Code(String md5) {
        this.mMd5Code = md5;
    }

    public String getMD5Code() {
        return this.mMd5Code;
    }

    public void setReconnectTotal(int total) {
        this.mReconnectTotal = total;
    }

    public int getReconnectTotal() {
        return this.mReconnectTotal;
    }
}

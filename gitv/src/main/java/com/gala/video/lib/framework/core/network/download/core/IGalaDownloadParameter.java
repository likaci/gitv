package com.gala.video.lib.framework.core.network.download.core;

import java.util.Map;

public interface IGalaDownloadParameter {
    public static final long CONNECT_READ_TIME_OUT = 15000;
    public static final long CONNECT_TIME_OUT = 15000;
    public static final long DISK_SIZE_LIMIT = 104857600;
    public static final long FILE_SIZE_LIMIT = 52428800;
    public static final int WRITE_SPEED_LIMIT = 4096;

    void addHeader(String str, String str2);

    long getConnectTimeOut();

    long getDiskSizeLimit();

    String getDownloadUrl();

    String getFileName();

    Map<String, String> getHeaderList();

    int getLimitSpeed();

    String getMD5Code();

    long getRangeStartPoint();

    long getReadTimeOut();

    int getReconnectTotal();

    String getSavePath();

    void setConnectTimeOut(long j);

    void setDiskSizeLimit(long j);

    void setDownloadUrl(String str);

    void setFileName(String str);

    void setLimitSpeed(int i);

    void setMD5Code(String str);

    void setRangeStartPoint(long j);

    void setReadTimeOut(long j);

    void setReconnectTotal(int i);

    void setSavePath(String str);
}

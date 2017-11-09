package com.tvos.downloadmanager.data;

public class DownloadRecordColumns {
    public static final String[] COLUMNS = new String[]{"id", "title", COLUMN_URI, COLUMN_DESTINATION, COLUMN_DESCRIPTION, COLUMN_MD5, COLUMN_MIMETYPE, "status", "downloadSize", COLUMN_FILESIZE, COLUMN_ISRESUMEBROKEN, COLUMN_SPEEDLIMITDEGREE, COLUMN_DOWNLOADTIME, COLUMN_ISP2PDOWNLOADERROR, COLUMN_ISP2PDOWNLOAD};
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DESTINATION = "destination";
    public static final String COLUMN_DOWNLOADSIZE = "downloadSize";
    public static final String COLUMN_DOWNLOADTIME = "downloadTime";
    public static final String COLUMN_FILESIZE = "fileSize";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ISP2PDOWNLOAD = "isP2PDownload";
    public static final String COLUMN_ISP2PDOWNLOADERROR = "isP2pDownloadError";
    public static final String COLUMN_ISRESUMEBROKEN = "isResumeBroken";
    public static final String COLUMN_MD5 = "md5";
    public static final String COLUMN_MIMETYPE = "mimetype";
    public static final String COLUMN_SPEEDLIMITDEGREE = "speedLimitDegree";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_URI = "uri";
    public static final String TABLE_NAME = "downloadrecord";
}

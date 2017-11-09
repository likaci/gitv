package com.gala.afinal.bitmap.download;

public interface Downloader {
    public static final int CONN_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 10000;

    byte[] download(String str);
}

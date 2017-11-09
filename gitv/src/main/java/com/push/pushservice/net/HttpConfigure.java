package com.push.pushservice.net;

public class HttpConfigure {
    private int mHttpConnectTimeOut = 5000;
    private int mHttpRetryTimes = 5;
    private int mHttpSoTimeOut = 5000;

    public void setHttpSoTimeOutTime(int milliSecond) {
        this.mHttpSoTimeOut = milliSecond;
    }

    public int getHttpSoTimeOutTime() {
        return this.mHttpSoTimeOut;
    }

    public void setHttpConnectTimeOutTime(int milliSecond) {
        this.mHttpConnectTimeOut = milliSecond;
    }

    public int getHttpConnectTimeOutTime() {
        return this.mHttpConnectTimeOut;
    }

    public void setHttpRetryTimes(int times) {
        this.mHttpRetryTimes = times;
    }

    public int getHttpRetryTimes() {
        return this.mHttpRetryTimes;
    }
}

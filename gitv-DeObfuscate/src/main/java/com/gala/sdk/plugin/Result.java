package com.gala.sdk.plugin;

import java.util.ArrayList;
import java.util.List;

public class Result<T> {
    private int mCode;
    private T mData;
    private long mDelayTime;
    private final List<LoadProviderException> mExceptions = new ArrayList();
    private int mPeriod;

    public Result(int code, int period, T data, List<LoadProviderException> es) {
        this.mCode = code;
        this.mPeriod = period;
        this.mData = data;
        this.mExceptions.addAll(es);
    }

    public void addException(LoadProviderException e) {
        this.mExceptions.add(e);
    }

    public int getCode() {
        return this.mCode;
    }

    public int getPeriod() {
        return this.mPeriod;
    }

    public T getData() {
        return this.mData;
    }

    public List<LoadProviderException> getExceptions() {
        return new ArrayList(this.mExceptions);
    }

    public void setDelayTime(long delaytime) {
        this.mDelayTime = delaytime;
    }

    public long getDelayTime() {
        return this.mDelayTime;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Result(").append("code=").append(this.mCode).append(", data=").append(this.mData).append(", exception=").append(this.mExceptions).append(", period=").append(this.mPeriod).append(")");
        return builder.toString();
    }
}

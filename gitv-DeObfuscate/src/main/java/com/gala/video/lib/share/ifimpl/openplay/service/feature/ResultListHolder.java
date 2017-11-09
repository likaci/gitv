package com.gala.video.lib.share.ifimpl.openplay.service.feature;

import android.os.Bundle;
import android.os.Parcelable;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import java.util.ArrayList;
import java.util.List;

public class ResultListHolder<T extends Parcelable> extends NetworkHolder {
    private static final String TAG = "ResultListHolder";
    private int mCode;
    private final ArrayList<T> mList;
    private int mMaxCount;

    public ResultListHolder() {
        this.mMaxCount = -1;
        this.mCode = 0;
        this.mList = new ArrayList();
        this.mMaxCount = -1;
    }

    public ResultListHolder(int maxCount) {
        this.mMaxCount = -1;
        this.mCode = 0;
        this.mList = new ArrayList();
        this.mMaxCount = maxCount;
    }

    public void add(T value) {
        this.mList.add(value);
    }

    public boolean isReachMax() {
        return this.mMaxCount > 0 && this.mList.size() >= this.mMaxCount;
    }

    public void setCode(int code) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setCode(" + code + ")");
        }
        this.mCode = code;
    }

    public Bundle getResult() {
        return OpenApiResultCreater.createResultBundle(this.mCode, this.mList);
    }

    public List<T> getList() {
        return new ArrayList(this.mList);
    }

    public String toString() {
        return "ResultListHolder(mCode=" + this.mCode + ", mMaxCount=" + this.mMaxCount + ", size=" + this.mList.size() + ")";
    }
}

package com.gala.video.lib.share.ifimpl.openplay.service.feature;

import android.os.Bundle;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import java.util.ArrayList;

public class ResultStringListHolder extends NetworkHolder {
    private static final String TAG = "ResultStringListHolder";
    private int mCode = 0;
    private ArrayList<String> mList = new ArrayList();

    public void add(String value) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "add(" + value + ")");
        }
        this.mList.add(value);
    }

    public void setCode(int code) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setCode(" + code + ")");
        }
        this.mCode = code;
    }

    public Bundle getResult() {
        return OpenApiResultCreater.createResultBundleOfString(this.mCode, this.mList);
    }

    public String toString() {
        return "ResultListHolder(code=" + this.mCode + ", size=" + this.mList.size() + ")";
    }
}

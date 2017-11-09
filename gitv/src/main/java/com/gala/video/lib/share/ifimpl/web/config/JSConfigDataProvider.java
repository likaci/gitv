package com.gala.video.lib.share.ifimpl.web.config;

import com.alibaba.fastjson.JSONObject;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.web.IJSConfigDataProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.web.IJSConfigDataProvider.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.web.IJSConfigResult;

class JSConfigDataProvider extends Wrapper implements IJSConfigDataProvider {
    private static final String TAG = "EPG/web/JSConfigDataProvider";
    private JSConfigResult mJSConfigResult;

    JSConfigDataProvider() {
        if (this.mJSConfigResult == null) {
            this.mJSConfigResult = new JSConfigResult();
        }
    }

    public void loadData() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "loadData");
        }
        new JSConfigDataLoad().loadSynData(new JSConfigDataLoadCallback() {
            public void onSuccess(JSONObject jsonObject) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d(JSConfigDataProvider.TAG, "onSuccess jsonObject");
                }
                JSConfigDataProvider.this.mJSConfigResult.init(jsonObject);
                LogUtils.i(JSConfigDataProvider.TAG, "onSuccess: " + JSConfigDataProvider.this.mJSConfigResult.toString());
            }

            public void onFail(String msg) {
                if (LogUtils.mIsDebug) {
                    LogUtils.e(JSConfigDataProvider.TAG, "onFail msg:" + msg);
                }
            }
        });
    }

    public IJSConfigResult getJSConfigResult() {
        return this.mJSConfigResult;
    }

    public int getMemoryLevel() {
        return this.mJSConfigResult != null ? this.mJSConfigResult.getMemoryLevel() : 2;
    }

    public void setMemoryLevel(int level) {
        if (this.mJSConfigResult != null) {
            this.mJSConfigResult.setMemoryLevel(level);
        }
    }
}

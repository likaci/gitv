package com.gala.video.lib.framework.core.utils;

import android.view.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class DebugHelper {
    private static final String LOG_TAG = "EPG/utils/DebugHelper";
    private boolean mFlag = true;
    private List<Integer> mKeyCodeLists = new ArrayList();
    private List<Integer> mKeyOrderList = new ArrayList();
    private OnDebugTriggerListener mOnDebugTriggerListener;
    private long mPreTime;
    private long mTotalTime;

    public interface OnDebugTriggerListener {
        void onDebugTrigger();
    }

    public void monitorKeyEvent(KeyEvent event) {
        if (this.mKeyCodeLists.isEmpty()) {
            this.mPreTime = System.currentTimeMillis();
        }
        LogUtils.m1576i(LOG_TAG, "keyCode  ---------- ", Integer.valueOf(event.getKeyCode()));
        this.mKeyCodeLists.add(Integer.valueOf(keyCode));
        if (System.currentTimeMillis() - this.mPreTime > this.mTotalTime) {
            LogUtils.m1571e(LOG_TAG, "------- TimeOut > 1000ms");
            resetData();
        } else if (this.mKeyCodeLists.size() == this.mKeyOrderList.size()) {
            int num = this.mKeyOrderList.size();
            for (int i = 0; i < num; i++) {
                if (this.mKeyOrderList.get(i) != this.mKeyCodeLists.get(i)) {
                    this.mFlag = false;
                    break;
                }
            }
            if (this.mFlag && this.mOnDebugTriggerListener != null) {
                LogUtils.m1576i(LOG_TAG, "------- mOnDebugTriggerListener.onDebugTrigger() --- ", Boolean.valueOf(this.mFlag));
                this.mOnDebugTriggerListener.onDebugTrigger();
            }
            resetData();
        }
    }

    private void resetData() {
        LogUtils.m1574i(LOG_TAG, "------- resetData()");
        this.mFlag = true;
        this.mKeyCodeLists.clear();
    }

    public OnDebugTriggerListener getOnDebugTriggerListener() {
        return this.mOnDebugTriggerListener;
    }

    public void setOnDebugTriggerListener(OnDebugTriggerListener onDebugTriggerListener) {
        this.mOnDebugTriggerListener = onDebugTriggerListener;
    }

    public void setKeyOrderList(int... keyCode) {
        for (int code : keyCode) {
            this.mKeyOrderList.add(Integer.valueOf(code));
        }
    }

    public void setTotalTime(long time) {
        this.mTotalTime = time;
    }
}

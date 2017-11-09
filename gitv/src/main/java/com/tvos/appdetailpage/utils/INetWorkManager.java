package com.tvos.appdetailpage.utils;

import android.content.Context;

public interface INetWorkManager {
    public static final int STATE_NONE = 0;
    public static final int STATE_WIFI_ERROR = 3;
    public static final int STATE_WIFI_NORMAL = 1;
    public static final int STATE_WIRED_ERROR = 4;
    public static final int STATE_WIRED_NORMAL = 2;

    public interface OnNetStateChangedListener {
        void onStateChanged(int i, int i2);
    }

    public interface StateCallback {
        void getStateResult(int i);
    }

    void checkNetWork();

    void checkNetWork(StateCallback stateCallback);

    int checkNetWorkSync();

    int getNetState();

    void initNetWorkManager(Context context);

    void registerStateChangedListener(OnNetStateChangedListener onNetStateChangedListener);

    void unRegisterStateChangedListener(OnNetStateChangedListener onNetStateChangedListener);
}

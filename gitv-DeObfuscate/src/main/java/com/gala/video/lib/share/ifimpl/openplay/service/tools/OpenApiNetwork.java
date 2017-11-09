package com.gala.video.lib.share.ifimpl.openplay.service.tools;

import com.gala.video.api.ApiException;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.StateCallback;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.NetworkUtils;

public class OpenApiNetwork {
    private static final String NETWORK_ERROR = "-50";
    private static final String TAG = "OpenApiNetwork";

    public static boolean isNetworkInvalid(ApiException exception) {
        if (exception != null && LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "ApiException fail:code=[" + exception.getCode() + "], http=[" + exception.getHttpCode() + "], " + exception.getMessage());
        }
        boolean invalid = exception == null ? false : "-50".equals(exception.getHttpCode());
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "isNetworkInvalid(" + exception + ") return " + invalid);
        }
        return invalid;
    }

    public static boolean isNetworkAvaliable() {
        boolean isNetworkAvaliable = NetworkUtils.isNetworkAvaliable();
        if (isNetworkAvaliable) {
            return isNetworkAvaliable;
        }
        return isNetworkAvaliableWithBlocking();
    }

    public static boolean isNetworkAvaliableWithBlocking() {
        final Object lock = new Object();
        new Thread8K(new Runnable() {

            class C17151 implements StateCallback {
                C17151() {
                }

                public void getStateResult(int state) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(OpenApiNetwork.TAG, "isNetworkAvaliableWithBlocking() getStateResult(" + state + ") notify!");
                    }
                    synchronized (lock) {
                        lock.notifyAll();
                    }
                }
            }

            public void run() {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(OpenApiNetwork.TAG, "isNetworkAvaliableWithBlocking() begin check!");
                }
                NetWorkManager.getInstance().checkNetWork(new C17151());
            }
        }, TAG).start();
        try {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "isNetworkAvaliableWithBlocking() wait begin.");
            }
            synchronized (lock) {
                lock.wait();
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "isNetworkAvaliableWithBlocking() wait end.");
            }
        } catch (InterruptedException e) {
            try {
                LogUtils.m1578w(TAG, "isNetworkAvaliableWithBlocking() fail!", e);
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, "isNetworkAvaliableWithBlocking() wait end.");
                }
            } catch (Throwable th) {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, "isNetworkAvaliableWithBlocking() wait end.");
                }
            }
        }
        return NetworkUtils.isNetworkAvaliable();
    }
}

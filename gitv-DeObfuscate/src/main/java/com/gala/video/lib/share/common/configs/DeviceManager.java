package com.gala.video.lib.share.common.configs;

import android.content.Context;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DeviceManager {
    public static final int STATE_AUTH_FAIL = 2;
    public static final int STATE_AUTH_SUCCESS = 1;
    public static final int STATE_UNKNOWN = 0;
    private static final String TAG = "DeviceManager";
    private static DeviceManager sInstance;
    private Context mContext;
    private final List<OnStateChangedListener> mListeners = new CopyOnWriteArrayList();
    private int mState;

    public interface OnStateChangedListener {
        void onStateChanged(int i);
    }

    public static synchronized void initialize(Context context) {
        synchronized (DeviceManager.class) {
            if (sInstance == null) {
                sInstance = new DeviceManager(context);
            }
        }
    }

    public static synchronized DeviceManager instance() {
        DeviceManager deviceManager;
        synchronized (DeviceManager.class) {
            if (sInstance == null) {
                sInstance = new DeviceManager(AppRuntimeEnv.get().getApplicationContext());
            }
            deviceManager = sInstance;
        }
        return deviceManager;
    }

    private DeviceManager(Context context) {
        this.mContext = context;
    }

    public boolean isAuthSuccess() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "isAuthSuccess() state=" + this.mState);
        }
        if (this.mState == 1) {
            return true;
        }
        return false;
    }

    public void setState(int state) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setState(" + state + ")");
        }
        this.mState = state;
        notifyStateChanged(state);
    }

    private void notifyStateChanged(int state) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "notifyStateChanged(" + state + ")");
        }
        for (OnStateChangedListener listener : this.mListeners) {
            listener.onStateChanged(state);
        }
    }

    public void addListener(OnStateChangedListener listener) {
        this.mListeners.add(listener);
    }

    public void removeListener(OnStateChangedListener listener) {
        this.mListeners.remove(listener);
    }
}

package com.gala.video.lib.share.ifimpl.ucenter.account.impl;

import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LoginCallbackRecorder {
    private static final String TAG = "AccountManager";
    private static LoginCallbackRecorder sInstance;
    private final List<LoginCallbackRecorderListener> mListeners = new CopyOnWriteArrayList();

    public interface LoginCallbackRecorderListener {
        void onLogin(String str);

        void onLogout(String str);
    }

    private LoginCallbackRecorder() {
    }

    public static synchronized LoginCallbackRecorder get() {
        LoginCallbackRecorder loginCallbackRecorder;
        synchronized (LoginCallbackRecorder.class) {
            if (sInstance == null) {
                sInstance = new LoginCallbackRecorder();
            }
            loginCallbackRecorder = sInstance;
        }
        return loginCallbackRecorder;
    }

    public void addListener(LoginCallbackRecorderListener listener) {
        if (listener != null && !this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        } else if (listener == null) {
            LogUtils.e("addListener fail!!! LoginCallbackRecorderListener param can't be null !!!");
        } else {
            LogUtils.e("addListener fail!!! LoginCallbackRecorderListener param already exist !!!");
        }
    }

    public void removeListener(LoginCallbackRecorderListener listener) {
        if (this.mListeners.contains(listener)) {
            this.mListeners.remove(listener);
            return;
        }
        LogUtils.e("removeListener fail!!! listener already be removed or listener is null !!!");
    }

    void notifyLogin(String uid) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "notifyLogin(" + uid + ") mIsLogin= true");
        }
        for (LoginCallbackRecorderListener listener : this.mListeners) {
            listener.onLogin(uid);
        }
    }

    void notifyLogout(String uid) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "notifyLogout(" + uid + ") mIsLogin=false");
        }
        for (LoginCallbackRecorderListener listener : this.mListeners) {
            listener.onLogout(uid);
        }
    }
}

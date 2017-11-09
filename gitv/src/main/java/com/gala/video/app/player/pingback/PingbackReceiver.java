package com.gala.video.app.player.pingback;

import com.gala.video.lib.framework.core.utils.LogUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PingbackReceiver {
    private static final String TAG = "PingbackReceiver";

    public void start() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "start()");
        }
        EventBus.getDefault().register(this);
    }

    public void stop() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "stop()");
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(Pingback pingback) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onEvent class=" + pingback);
        }
        if (pingback.check()) {
            pingback.send();
        } else {
            LogUtils.e(TAG, "onEvent(" + pingback + ")");
        }
    }
}

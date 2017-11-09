package com.gala.video.app.epg.home.data.pingback;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ClickPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ShowPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.IHomePingback;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HomePingbackFactory {
    private static final String TAG = "HomePingbackFactory";
    private static HomePingbackFactory sFactory = new HomePingbackFactory();
    private ThreadPoolExecutor mThreadPoolExecutor;

    private HomePingbackFactory() {
        HomePingbackDebug.DEBUG_LOG = AppClientUtils.isDebugMode();
        if (HomePingbackDebug.DEBUG_LOG) {
            this.mThreadPoolExecutor = new ThreadPoolExecutor(1, 1, 200, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(100));
        }
    }

    public static HomePingbackFactory instance() {
        return sFactory;
    }

    public IHomePingback createPingback(Object pingbackType) {
        HomePingback pingback = null;
        if (pingbackType instanceof ShowPingback) {
            pingback = new HomePageShowPingback((ShowPingback) pingbackType);
        } else if (pingbackType instanceof ClickPingback) {
            pingback = new HomePageClickPingback((ClickPingback) pingbackType);
        } else if (pingbackType instanceof CommonPingback) {
            pingback = new HomeCommonPingback((CommonPingback) pingbackType);
        } else {
            LogUtils.m1577w(TAG, "Do not exist home pingback type : " + null);
        }
        if (pingback != null) {
            pingback.setThreadPoolExecutor(this.mThreadPoolExecutor);
        }
        return pingback;
    }
}

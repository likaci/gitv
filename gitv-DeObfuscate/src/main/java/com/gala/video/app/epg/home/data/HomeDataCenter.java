package com.gala.video.app.epg.home.data;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.PlatformType;
import com.gala.video.app.epg.dependency.Dependencies;
import com.gala.video.app.epg.home.controller.HomeController;
import com.gala.video.app.epg.home.controller.UIEvent.UICallback;
import com.gala.video.app.epg.home.data.bus.HomeDataObservable;
import com.gala.video.app.epg.home.data.bus.IHomeDataObserver;
import com.gala.video.app.epg.home.data.hdata.HomeDataType;
import com.gala.video.app.epg.home.data.provider.RecommendQuitApkProvider;
import com.gala.video.app.epg.ui.imsg.IMsgSSStatusChangeListener;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.ucenter.account.impl.LoginCallbackRecorder;
import com.gala.video.lib.share.ifimpl.ucenter.account.impl.LoginCallbackRecorder.LoginCallbackRecorderListener;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.uikit.cache.UikitDataCacheSecurity;

public class HomeDataCenter {
    private static final String TAG = "HomeDataCenter";
    private static Context sContext = null;

    static class C06191 implements UICallback {
        C06191() {
        }

        public boolean onMessage(int event, Object arg) {
            if (event == 1) {
                UikitDataCacheSecurity.getInstance().setSecurity(true);
                LogUtils.m1568d("HomeDataCenter", "Build ui is finished");
                HomeDataCenter.initPushService();
            }
            return true;
        }
    }

    static class C06202 implements LoginCallbackRecorderListener {
        C06202() {
        }

        public void onLogin(String uid) {
            RecommendQuitApkProvider.getInstance().clearCache();
            HomeDataCenter.initPushService();
        }

        public void onLogout(String uid) {
            RecommendQuitApkProvider.getInstance().clearCache();
            HomeDataCenter.initPushService();
        }
    }

    static class C06213 implements Runnable {
        C06213() {
        }

        public void run() {
            if (Dependencies.checkSupportPushService() && Project.getInstance().getBuild().isOpenMessageCenter() && TVApiBase.getTVApiProperty().getPlatform() != PlatformType.TAIWAN) {
                LogUtils.m1568d("HomeDataCenter", "Init Push Service");
                GetInterfaceTools.getMsgCenter().init();
                GetInterfaceTools.getIScreenSaver().getStatusDispatcher().register(new IMsgSSStatusChangeListener());
            }
        }
    }

    public static void initialize(Context context) {
        sContext = context;
        registerBuildUICompleteCallback();
        registerAccountManagerListener();
    }

    private static void registerBuildUICompleteCallback() {
        HomeController.sUIEvent.register(new C06191());
    }

    public static void registerAccountManagerListener() {
        LoginCallbackRecorder.get().addListener(new C06202());
    }

    public static void initPushService() {
        new Thread8K(new C06213(), "HomeDataCenter").start();
    }

    public static void setCheckBuildUIFlag(int flag) {
        if (sContext != null) {
            Editor localEditor = sContext.getSharedPreferences("BUILD_UI", 0).edit();
            localEditor.putInt("FLAG", flag);
            localEditor.apply();
            LogUtils.m1568d("HomeDataCenter", "set flag = " + flag);
        }
    }

    public static int getCheckBuildUIFlag() {
        if (sContext != null) {
            return sContext.getSharedPreferences("BUILD_UI", 0).getInt("FLAG", -1);
        }
        return -1;
    }

    public static synchronized void registerObserver(HomeDataType type, IHomeDataObserver observer) {
        synchronized (HomeDataCenter.class) {
            HomeDataObservable.getInstance().addObserver(type.ordinal(), observer);
        }
    }

    public static synchronized void unregisterObserver(HomeDataType type, IHomeDataObserver observer) {
        synchronized (HomeDataCenter.class) {
            HomeDataObservable.getInstance().deleteObserver(type.ordinal(), observer);
        }
    }

    public static synchronized void clearObserver() {
        synchronized (HomeDataCenter.class) {
            HomeDataObservable.getInstance().clear();
        }
    }
}

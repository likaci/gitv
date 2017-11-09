package com.gala.video.app.epg.home.ads;

import com.gala.sdk.player.AbsAdCacheManager;
import com.gala.sdk.player.AdCacheManager;
import com.gala.sdk.player.AdCacheManager.AdCacheItem;
import com.gala.sdk.player.AdCacheManager.IAdCacheStrategy;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerFeatureProxy.OnStateChangedListener;
import com.gala.video.lib.share.system.preference.AppPreference;
import java.util.concurrent.atomic.AtomicBoolean;

public class AdCacheManagerProxy extends AbsAdCacheManager {
    private static AdCacheManagerProxy sInstance;
    public final String TAG = ("AdCacheManagerProxy" + Integer.toHexString(super.hashCode()));
    private AdCacheManager mClient;
    private AtomicBoolean mClientConnenting = new AtomicBoolean(false);
    OnStateChangedListener mListener = new C05561();

    class C05561 implements OnStateChangedListener {
        C05561() {
        }

        public void onSuccess() {
            AdCacheManagerProxy.this.mClient = GetInterfaceTools.getPlayerFeatureProxy().getPlayerFeatureOnlyInitJava().getAdCacheManager();
            AdCacheManagerProxy.this.flushAllCachedInvokes();
        }

        public void onLoading() {
        }

        public void onFailed() {
            AdCacheManagerProxy.this.mClientConnenting.set(false);
        }

        public void onCanceled() {
        }
    }

    private AdCacheManagerProxy() {
    }

    public static synchronized AdCacheManagerProxy getInstance() {
        AdCacheManagerProxy adCacheManagerProxy;
        synchronized (AdCacheManagerProxy.class) {
            if (sInstance == null) {
                sInstance = new AdCacheManagerProxy();
            }
            adCacheManagerProxy = sInstance;
        }
        return adCacheManagerProxy;
    }

    public void addTask(AdCacheItem item) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "addTask: item=" + item);
        }
        initClientAsync();
        if (this.mClient == null) {
            super.addTask(item);
        } else {
            this.mClient.addTask(item);
        }
    }

    public void setCurrentRunningState(int level) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setCurrentRunningState:" + level);
        }
        initClientAsync();
        AdCacheManager manager = this.mClient;
        if (manager == null) {
            super.setCurrentRunningState(level);
        } else {
            manager.setCurrentRunningState(level);
        }
    }

    public void addAdCacheStrategy(IAdCacheStrategy iAdCacheStrategy) {
    }

    public String getRootPath(int cacheAdType) {
        if (cacheAdType == 1) {
            return new AppPreference(AppRuntimeEnv.get().getApplicationContext(), AdCacheManager.SHARED_PREF_STARTUP_AD_CACHE_PATH).get(AdCacheManager.SHARED_PREF_STARTUP_AD_CACHE_PATH);
        }
        return "";
    }

    private void initClientAsync() {
        if (!this.mClientConnenting.get()) {
            this.mClientConnenting.set(true);
            GetInterfaceTools.getPlayerFeatureProxy().loadPlayerFeatureAsync(null, this.mListener, false);
        }
    }
}

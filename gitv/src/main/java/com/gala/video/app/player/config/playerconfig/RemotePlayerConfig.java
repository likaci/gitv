package com.gala.video.app.player.config.playerconfig;

import com.gala.sdk.utils.MyLogUtils;
import com.gala.video.app.player.config.configReader.ConfigReaderManager;
import com.gala.video.app.player.config.configReader.IConfigReader.OnConfigReadListener;
import com.gala.video.app.player.config.configWriter.ConfigWriterManager;
import com.gala.video.app.player.utils.AdCasterSwitchHelper;
import java.util.concurrent.atomic.AtomicBoolean;

public class RemotePlayerConfig extends AbsPlayerConfig {
    private static final int MAX_FAILURE_COUNT_REMOTE = 5;
    private int mCachedConfigFailureCount;
    private OnConfigReadListener mCachedConfigReadListener;
    private boolean mReady;
    private int mRemoteConfigFailureCount;
    private AtomicBoolean mRemoteConfigFetching;
    private OnConfigReadListener mRemoteConfigReadListener;

    private static class RemotePlayerConfigInstanceHolder {
        public static RemotePlayerConfig sRemoteConfig = new RemotePlayerConfig();

        private RemotePlayerConfigInstanceHolder() {
        }
    }

    private RemotePlayerConfig() {
        this.mReady = false;
        this.mCachedConfigReadListener = new OnConfigReadListener() {
            public void onSuccess(String jsonResult) {
                MyLogUtils.d(RemotePlayerConfig.this.TAG, "mCachedConfigReadListener.onSuccess(jsonResult: " + jsonResult);
                RemotePlayerConfig.this.parseJsResult(jsonResult);
                AdCasterSwitchHelper.updateSwitchValue(RemotePlayerConfig.this.mDisableAdCaster);
                RemotePlayerConfig.this.mReady = true;
            }

            public void onFailed(Throwable e) {
                MyLogUtils.e(RemotePlayerConfig.this.TAG, "mCachedConfigReadListener.onFailed(" + e + ")");
                RemotePlayerConfig.this.mCachedConfigFailureCount = RemotePlayerConfig.this.mCachedConfigFailureCount + 1;
            }
        };
        this.mRemoteConfigReadListener = new OnConfigReadListener() {
            public void onSuccess(String jsonResult) {
                MyLogUtils.d(RemotePlayerConfig.this.TAG, "mRemoteConfigReadListener.onSuccess(jsonResult: " + jsonResult);
                RemotePlayerConfig.this.parseJsResult(jsonResult);
                RemotePlayerConfig.this.mReady = true;
                RemotePlayerConfig.this.mRemoteConfigFetching.set(false);
                ConfigWriterManager.instance().getConfigWriter().writeConfigAsync(jsonResult, null);
                AdCasterSwitchHelper.updateSwitchValue(RemotePlayerConfig.this.mDisableAdCaster);
            }

            public void onFailed(Throwable e) {
                MyLogUtils.e(RemotePlayerConfig.this.TAG, "mRemoteConfigReadListener.onFailed(" + e + ")");
                RemotePlayerConfig.this.mRemoteConfigFailureCount = RemotePlayerConfig.this.mRemoteConfigFailureCount + 1;
                RemotePlayerConfig.this.mRemoteConfigFetching.set(false);
            }
        };
        this.mRemoteConfigFetching = new AtomicBoolean(false);
        this.TAG = "PlayerConfig/RemotePlayerCofig@" + Integer.toHexString(hashCode());
    }

    public static RemotePlayerConfig instance() {
        return RemotePlayerConfigInstanceHolder.sRemoteConfig;
    }

    public void load() {
        MyLogUtils.d(this.TAG, "load(), mReady= " + this.mReady + ", mRemoteConfigFailureCount=" + this.mRemoteConfigFailureCount + ", mCachedConfigFailureCount=" + this.mCachedConfigFailureCount);
        if (this.mRemoteConfigFailureCount >= 5) {
            MyLogUtils.d(this.TAG, "load(), excessive max failed load attempts, return.");
        } else if (!this.mRemoteConfigFetching.get()) {
            this.mRemoteConfigFetching.set(true);
            ConfigReaderManager.instance().getConfigReader(2).readConfigAsync(this.mRemoteConfigReadListener);
        }
    }

    public boolean ready() {
        return this.mReady;
    }
}

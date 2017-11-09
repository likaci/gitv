package com.gala.video.app.player.config.playerconfig;

import com.gala.sdk.utils.MyLogUtils;
import com.gala.video.app.player.config.configReader.ConfigReaderManager;
import com.gala.video.app.player.config.configReader.IConfigReader.OnConfigReadListener;
import com.gala.video.app.player.utils.AdCasterSwitchHelper;
import java.util.concurrent.atomic.AtomicBoolean;

public class CachedPlayerConfig extends AbsPlayerConfig {
    private static final int MAX_FAILURE_COUNT_CACHE = 1;
    private int mCachedConfigFailureCount;
    private AtomicBoolean mCachedConfigFetching;
    private OnConfigReadListener mCachedConfigReadListener;
    private boolean mReady;

    private static class CachedPlayerConfigInstanceHolder {
        public static CachedPlayerConfig sCachedConfig = new CachedPlayerConfig();

        private CachedPlayerConfigInstanceHolder() {
        }
    }

    private CachedPlayerConfig() {
        this.mReady = false;
        this.mCachedConfigFailureCount = 0;
        this.mCachedConfigReadListener = new OnConfigReadListener() {
            public void onSuccess(String jsonResult) {
                MyLogUtils.d(CachedPlayerConfig.this.TAG, "mCachedConfigReadListener.onSuccess(jsonResult: " + jsonResult);
                CachedPlayerConfig.this.parseJsResult(jsonResult);
                AdCasterSwitchHelper.updateSwitchValue(CachedPlayerConfig.this.mDisableAdCaster);
                CachedPlayerConfig.this.mReady = true;
                CachedPlayerConfig.this.mCachedConfigFetching.set(false);
            }

            public void onFailed(Throwable e) {
                MyLogUtils.e(CachedPlayerConfig.this.TAG, "mCachedConfigReadListener.onFailed(" + e + ")");
                CachedPlayerConfig.this.mCachedConfigFailureCount = CachedPlayerConfig.this.mCachedConfigFailureCount + 1;
                CachedPlayerConfig.this.mCachedConfigFetching.set(false);
            }
        };
        this.mCachedConfigFetching = new AtomicBoolean(false);
        this.TAG = "PlayerConfig/CachedPlayerConfig@" + Integer.toHexString(hashCode());
    }

    public static CachedPlayerConfig instance() {
        return CachedPlayerConfigInstanceHolder.sCachedConfig;
    }

    public void load() {
        MyLogUtils.d(this.TAG, "load(), mReady= " + this.mReady + ",  mCachedConfigFailureCount=" + this.mCachedConfigFailureCount);
        if (this.mCachedConfigFetching.get()) {
            MyLogUtils.d(this.TAG, "load(), mReady= " + this.mReady);
            return;
        }
        this.mCachedConfigFetching.set(true);
        if (this.mCachedConfigFailureCount < 1) {
            MyLogUtils.d(this.TAG, "load(), mReady= " + this.mReady);
            ConfigReaderManager.instance().getConfigReader(1).readConfigAsync(this.mCachedConfigReadListener);
            return;
        }
        MyLogUtils.d(this.TAG, "load(), excessive max failed load attempts, return.");
    }

    public boolean ready() {
        return this.mReady;
    }
}

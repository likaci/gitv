package com.gala.video.app.player.config.playerconfig;

import com.gala.sdk.utils.MyLogUtils;
import com.gala.video.app.player.config.DeviceInfoParams;
import com.gala.video.app.player.config.PlayerConfigJsFunc2Java;
import com.gala.video.app.player.utils.AdCasterSwitchHelper;

public class LocalPlayerConfig extends AbsPlayerConfig {
    private boolean mReady;

    private static class LocalPlayerConfigInstanceHolder {
        public static LocalPlayerConfig sLocalConfig = new LocalPlayerConfig();

        private LocalPlayerConfigInstanceHolder() {
        }
    }

    private LocalPlayerConfig() {
        this.mReady = false;
        this.TAG = "PlayerConfig/LocalPlayerConfig@" + Integer.toHexString(hashCode());
        load();
    }

    public void load() {
        MyLogUtils.m462d(this.TAG, "load(), mReady=" + this.mReady);
        if (!this.mReady) {
            DeviceInfoParams params = DeviceInfoParams.instance();
            String jsonResult = PlayerConfigJsFunc2Java.getPlayerConfig(params.getCpuInfo(), params.getProductName(), params.getModelName(), params.getTotalMemory(), params.getAndroidVersion(), params.getApkVersion(), params.getUuid());
            MyLogUtils.m462d(this.TAG, "load(), jsonResult = " + jsonResult);
            parseJsResult(jsonResult);
            AdCasterSwitchHelper.updateSwitchValue(this.mDisableAdCaster);
            this.mReady = true;
        }
    }

    public static LocalPlayerConfig instance() {
        return LocalPlayerConfigInstanceHolder.sLocalConfig;
    }

    public boolean ready() {
        return this.mReady;
    }
}

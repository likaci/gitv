package com.gala.sdk.player;

import android.util.SparseArray;
import com.gala.sdk.player.Parameter.Keys;

public class InitParameter {
    public static final int HINT_TYPE_HIDE_PAUSE_AD = 3001;
    public static final int HINT_TYPE_SHOW_CLICK_THROUGH_AD = 2001;
    public static final int HINT_TYPE_SKIP_AD = 1001;
    private SparseArray<String> mAdsHintNameMap = new SparseArray();
    private String mAppVersion;
    private String mCdnDispatchParam;
    private long mDelayedMilliSec;
    private String mDeviceInfo;
    private boolean mShowAdCountDown;

    public InitParameter setInitDelay(long delay) {
        this.mDelayedMilliSec = delay;
        return this;
    }

    public InitParameter setShowAdCountDown(boolean isShow) {
        this.mShowAdCountDown = isShow;
        return this;
    }

    public InitParameter setAppVersion(String version) {
        this.mAppVersion = version;
        return this;
    }

    public InitParameter setDeviceInfo(String deviceInfo) {
        this.mDeviceInfo = deviceInfo;
        return this;
    }

    public InitParameter setCdnDispatchParam(String cdnParam) {
        this.mCdnDispatchParam = cdnParam;
        return this;
    }

    public InitParameter addAdsHint(int hintType, String hintName) {
        this.mAdsHintNameMap.put(hintType, hintName);
        return this;
    }

    private void addAdsHintToMap(int hintType, Parameter param) {
        String str = (String) this.mAdsHintNameMap.get(1001);
        if (str != null && !str.isEmpty()) {
            switch (hintType) {
                case 1001:
                    param.setString(Keys.S_AD_HINT_SKIP_AD, str);
                    return;
                case HINT_TYPE_SHOW_CLICK_THROUGH_AD /*2001*/:
                    param.setString(Keys.S_AD_HINT_SHOW_CLICK_THROUGH, str);
                    return;
                case HINT_TYPE_HIDE_PAUSE_AD /*3001*/:
                    param.setString(Keys.S_AD_HINT_HIDE_PAUSE, str);
                    return;
                default:
                    return;
            }
        }
    }

    public Parameter toParameter() {
        Parameter parameter = new Parameter();
        parameter.setInt64(Keys.I_DELAY_INIT_MS, this.mDelayedMilliSec);
        parameter.setBoolean(Keys.B_AD_SHOW_COUNTDOWN, this.mShowAdCountDown);
        parameter.setString(Keys.S_APP_VERSION, this.mAppVersion);
        parameter.setString(Keys.S_DEVICE_INFO, this.mDeviceInfo);
        parameter.setString(Keys.S_CDN_DISPATCH_PARAM, this.mCdnDispatchParam);
        addAdsHintToMap(1001, parameter);
        addAdsHintToMap(HINT_TYPE_SHOW_CLICK_THROUGH_AD, parameter);
        addAdsHintToMap(HINT_TYPE_HIDE_PAUSE_AD, parameter);
        return parameter;
    }
}

package com.gala.video.app.player.ui.overlay;

import com.gala.video.app.player.R;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LoadingInfo {
    private static final String TAG = "Player/Ui/LoadingInfo";
    private boolean mIs3d;
    private boolean mIsExclusive;
    private boolean mIsVip;
    private long mLiveTime;
    private String mTitle;

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String title) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setTitle(" + title + ")");
        }
        this.mTitle = title;
    }

    public boolean isExclusive() {
        return this.mIsExclusive;
    }

    public void setIsExclusive(boolean exclusive) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setIsExclusive(" + exclusive + ")");
        }
        this.mIsExclusive = exclusive;
    }

    public boolean isVip() {
        return this.mIsVip;
    }

    public void setIsVip(boolean vip) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setIsVip(" + vip + ")");
        }
        this.mIsVip = vip;
    }

    public boolean is3d() {
        return this.mIs3d;
    }

    public void setIs3d(boolean is3d) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setIs3d(" + is3d + ")");
        }
        this.mIs3d = is3d;
    }

    public void setLiveTime(long time) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setLiveTime(" + time + ")");
        }
        this.mLiveTime = time;
        if (checkLiveTime() && !StringUtils.isEmpty(this.mTitle)) {
            String liveTime = new SimpleDateFormat("HH:mm", Locale.CHINA).format(new Date(this.mLiveTime));
            this.mTitle = AppRuntimeEnv.get().getApplicationContext().getResources().getString(R.string.live_loding_name, new Object[]{liveTime, this.mTitle});
        }
    }

    public LoadingInfo(String name) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "LoadingInfo(" + name);
        }
        if (!StringUtils.isEmpty((CharSequence) name)) {
            this.mTitle = name;
        }
    }

    public static void updateExistingInfo(LoadingInfo destInfo, LoadingInfo srcInfo) {
        destInfo.mTitle = srcInfo.mTitle;
    }

    private boolean checkLiveTime() {
        return DeviceUtils.getServerTimeMillis() - this.mLiveTime <= 0;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LoadingInfo{");
        builder.append("title=" + this.mTitle);
        builder.append(", isExclusive=" + this.mIsExclusive);
        builder.append(", isVip=" + this.mIsVip);
        builder.append(", is3d=" + this.mIs3d);
        builder.append("}");
        return builder.toString();
    }
}

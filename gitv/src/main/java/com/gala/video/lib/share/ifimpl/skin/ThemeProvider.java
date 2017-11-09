package com.gala.video.lib.share.ifimpl.skin;

import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.SerializableUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.skin.IThemeProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.skin.IThemeProvider.ThemeModel;
import com.gala.video.lib.share.utils.Precondition;
import java.io.File;
import java.io.IOException;

class ThemeProvider implements IThemeProvider {
    public static final String HOME_DATA_CACHE = "home/home_cache/";
    public static final String HOME_DAY_THEME_CACHE = "home/home_cache/day_theme_channel_icons.dem";
    private static final String TAG = "ThemeProvider";
    private static final ThemeProvider mInstance = new ThemeProvider();
    private ThemeModel mDayData;
    private int mStatus = 0;

    ThemeProvider() {
    }

    public void saveDayThemeJson(ThemeModel model) {
        try {
            SerializableUtils.write(model, "home/home_cache/day_theme_channel_icons.dem");
        } catch (IOException e) {
            LogUtils.e(TAG, "write day theme channel icons failed");
        }
    }

    public String getDayChannelIconUrls() {
        return getChannelIconUrls("home/home_cache/day_theme_channel_icons.dem");
    }

    private String getChannelIconUrls(String cacheName) {
        ThemeModel model = getThemeModel(cacheName);
        if (model != null) {
            return model.mChannelIconUrls;
        }
        return "";
    }

    public String getDayThemeSourcePath() {
        return getThemeSourcePath("home/home_cache/day_theme_channel_icons.dem");
    }

    private String getThemeSourcePath(String cacheName) {
        ThemeModel model = getThemeModel(cacheName);
        if (model != null) {
            return model.mThemeSourcePath;
        }
        return "";
    }

    public String getDayThemeSourceName() {
        return getThemeSourceName("home/home_cache/day_theme_channel_icons.dem");
    }

    private String getThemeSourceName(String cacheName) {
        ThemeModel model = getThemeModel(cacheName);
        if (model != null) {
            return model.mThemeSourceName;
        }
        return "";
    }

    public ThemeModel getThemeModel(String cacheName) {
        try {
            if (cacheName.equals("home/home_cache/day_theme_channel_icons.dem")) {
                if (this.mDayData == null) {
                    this.mDayData = (ThemeModel) SerializableUtils.read(cacheName);
                }
                return this.mDayData;
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "read day theme channel icons failed");
        }
        return null;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    public int getStatus() {
        if (this.mStatus == 0) {
            ThemeModel model = getThemeModel("home/home_cache/day_theme_channel_icons.dem");
            if (!(model == null || Precondition.isEmpty(model.mThemeSourcePath))) {
                this.mStatus = 1;
            }
        }
        return this.mStatus;
    }

    public void resetDayTheme() {
        String path = getThemeSourcePath("home/home_cache/day_theme_channel_icons.dem");
        if (!Precondition.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
        File cacheFile = new File(AppRuntimeEnv.get().getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "home/home_cache/day_theme_channel_icons.dem");
        if (cacheFile.exists()) {
            cacheFile.delete();
        }
        this.mDayData = null;
        this.mStatus = 0;
    }
}

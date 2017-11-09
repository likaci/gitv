package com.gala.video.lib.share.ifimpl.background;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.R;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.background.IBackgroundManager.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.skin.IThemeZipHelper.BackgroundType;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import com.gala.video.lib.share.utils.MemoryLevelInfo;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.io.File;
import java.io.IOException;

class BackgroundManager extends Wrapper {
    private static final String TAG = "EPG/home/BackgroundManager";
    private Context mAppContext;
    private Drawable mCurBGDrawable;

    public BackgroundManager() {
        this.mAppContext = null;
        this.mCurBGDrawable = null;
        this.mAppContext = AppRuntimeEnv.get().getApplicationContext();
    }

    public Drawable getBackgroundDrawable() {
        if (MemoryLevelInfo.isLowMemoryDevice()) {
            return new ColorDrawable(this.mAppContext.getResources().getColor(R.color.app_background));
        }
        return getBackgroundDrawable(SystemConfigPreference.getNightModeBackground(this.mAppContext));
    }

    private Drawable getBackgroundDrawable(String drawableName) {
        LogUtils.d(TAG, "getBackgroundDrawable drawableName  = " + drawableName);
        try {
            if (drawableName.equals(SystemConfigPreference.SETTING_BACKGROUND_NIGHT_DEFAULT)) {
                this.mCurBGDrawable = getDefaultBackgroundDrawable();
            } else {
                String FilePath = getSettingBgDrawablePath(drawableName);
                File drawableFile = new File(FilePath);
                LogUtils.d(TAG, "getBackgroundDrawable FilePath  = " + FilePath);
                if (drawableFile.exists() && drawableFile.isFile()) {
                    Bitmap bit = BitmapFactory.decodeFile(FilePath);
                    if (bit == null) {
                        this.mCurBGDrawable = getDefaultBackgroundDrawable();
                    } else {
                        this.mCurBGDrawable = new BitmapDrawable(bit);
                    }
                } else {
                    LogUtils.e(TAG, "getBackgroundDrawable FilePath is not exist");
                    this.mCurBGDrawable = getDefaultBackgroundDrawable();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "getBackgroundDrawable Exception  e = " + e.getMessage());
            this.mCurBGDrawable = getDefaultBackgroundDrawable();
        }
        return this.mCurBGDrawable;
    }

    private Drawable getDefaultBackgroundDrawable() {
        return ResourceUtil.getDrawable(R.drawable.share_default_background);
    }

    public void setBackgroundDrawable(String drawableName) {
        LogUtils.d(TAG, "setBackgroundDrawable path  = " + drawableName);
        SystemConfigPreference.setNightModeBackground(this.mAppContext, drawableName);
        if (Project.getInstance().getBuild().isHomeVersion()) {
            WallpaperManager wpm = WallpaperManager.getInstance(this.mAppContext);
            try {
                if (drawableName.equals(SystemConfigPreference.SETTING_BACKGROUND_NIGHT_DEFAULT)) {
                    Bitmap bmp = Bitmap.createBitmap(16, 9, Config.ARGB_4444);
                    new Canvas(bmp).drawColor(this.mAppContext.getResources().getColor(R.color.setting_night_bg));
                    wpm.setBitmap(bmp);
                    return;
                }
                Bitmap bit = BitmapFactory.decodeFile(getSettingBgDrawablePath(drawableName));
                if (bit != null) {
                    wpm.setBitmap(bit);
                }
            } catch (IOException e) {
                LogUtils.e(TAG, "setBackgroundDrawable set launcher backgroud fail---e" + e);
            }
        }
    }

    private String getSettingBgDrawablePath(String fileName) {
        String folderPath = "";
        if (fileName.startsWith(SystemConfigPreference.SETTING_BACKGROUND_DAY)) {
            folderPath = GetInterfaceTools.getIThemeZipHelper().getBackground(BackgroundType.DAY_BACKGROUND);
        } else if (fileName.startsWith(SystemConfigPreference.SETTING_BACKGROUND_NIGHT)) {
            folderPath = GetInterfaceTools.getIThemeZipHelper().getBackground(BackgroundType.NIGHT_BACKGROUND);
        }
        LogUtils.d(TAG, "getSettingBgDrawablePath fileName  = " + fileName + " ;folderPath = " + folderPath);
        return folderPath + fileName;
    }
}

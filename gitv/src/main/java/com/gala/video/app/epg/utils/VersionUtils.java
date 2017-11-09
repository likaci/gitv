package com.gala.video.app.epg.utils;

import android.content.Context;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;

public class VersionUtils {
    public static final int MODE_AGAIN = 3;
    public static final int MODE_NEW_INSTALL = 1;
    public static final int MODE_UPDATE = 2;
    private static VersionUtils instance;
    private int launchMode = 3;
    private boolean mIsOpenMarked = false;

    public static VersionUtils get() {
        if (instance == null) {
            instance = new VersionUtils();
        }
        return instance;
    }

    public void markOpenApp(Context ctx) {
        if (!this.mIsOpenMarked) {
            this.mIsOpenMarked = true;
            CharSequence lastVersion = SystemConfigPreference.getLastInstallVersion(ctx);
            String currentVersion = Project.getInstance().getBuild().getVersionString();
            if (StringUtils.isEmpty(lastVersion)) {
                this.launchMode = 1;
                SystemConfigPreference.setLastInstallVersion(ctx, currentVersion);
            } else if (lastVersion.equals(currentVersion)) {
                this.launchMode = 3;
            } else {
                this.launchMode = 2;
                SystemConfigPreference.setLastInstallVersion(ctx, currentVersion);
            }
            LogUtils.i("VersionUtils", "get LaunchMode:" + this.launchMode + ", lastVersion:" + lastVersion + ", currentVersion:" + currentVersion);
        }
    }

    public int getLaunchMode() {
        return this.launchMode;
    }

    public boolean isFirstOpen() {
        return this.launchMode != 3;
    }

    public boolean isAfterInstall() {
        return this.launchMode == 1;
    }

    public boolean isAfterUpdate() {
        return this.launchMode == 2;
    }
}

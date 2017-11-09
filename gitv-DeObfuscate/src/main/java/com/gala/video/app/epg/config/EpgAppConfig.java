package com.gala.video.app.epg.config;

import android.content.Context;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.widget.GlobalQRFeedBackDialog;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.project.Project;

public class EpgAppConfig {
    public static boolean isAddFavourite() {
        return true;
    }

    public static int getDefaultSkewCover() {
        return C0508R.drawable.epg_default_skew_image;
    }

    public static GlobalDialog getGlobalQRDialog(Context context) {
        return new GlobalQRFeedBackDialog(context);
    }

    public static int getDigitKeyboardLayoutResId() {
        return C0508R.layout.epg_net_diagnose_digit_keyboard;
    }

    public static boolean isUseAlbumListCache() {
        return Project.getInstance().getBuild().isUseAlbumListCache();
    }

    public static boolean shouldAuthMac() {
        return Project.getInstance().getBuild().shouldAuthMac();
    }

    public static int getWelcomeLaoutId() {
        if (Project.getInstance().getBuild().isLitchi()) {
            return C0508R.layout.epg_litchi_activity_welcome;
        }
        if (Project.getInstance().getBuild().isGitvUI()) {
            return C0508R.layout.epg_gitv_activity_welcome;
        }
        return C0508R.layout.epg_gitv_activity_welcome;
    }

    public static boolean isShowScreenAd() {
        return true;
    }
}

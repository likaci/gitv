package com.gala.video.app.player.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView.ScaleType;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.video.app.player.R;
import com.gala.video.app.player.ui.config.LoadingViewAnimManager;
import com.gala.video.app.player.ui.overlay.panels.PlayerErrorPanel.PlayerErrorPanelInfo;
import com.gala.video.app.player.ui.overlay.panels.PlayerErrorPanel.PlayerErrorPanelUIConfig;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;

public class UiUtils {
    private static final String TAG = "Player/Ui/UiUtils";
    private static Drawable sPlayerErrorBGForVIP;

    private UiUtils() {
    }

    public static Rect getBgDrawablePaddings(Context context, int itemBgResId) {
        Rect bgDrawablePaddings = new Rect();
        Drawable drawable = context.getResources().getDrawable(itemBgResId);
        if (drawable != null) {
            drawable.getPadding(bgDrawablePaddings);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getBgDrawablePaddings: " + bgDrawablePaddings);
        }
        return bgDrawablePaddings;
    }

    public static int getDimensionInPx(Context context, int resId) {
        return context.getResources().getDimensionPixelSize(resId);
    }

    public static PlayerErrorPanelUIConfig getPlayerErrorPanelUIConfigForVIP() {
        PlayerErrorPanelUIConfig config = new PlayerErrorPanelUIConfig();
        config.setBackgroundScaleType(ScaleType.FIT_XY).setMainTextColor(ResourceUtil.getColor(R.color.player_ui_vip_error_tip_main_start), ResourceUtil.getColor(R.color.player_ui_vip_error_tip_main_end)).setMainTextSize(ResourceUtil.getDimensionPixelSize(R.dimen.dimen_24dp)).setMainTextGravity(1).setMainTextTopMargin(ResourceUtil.getDimen(R.dimen.player_error_main_text_topmargin_for_vip));
        return config;
    }

    public static PlayerErrorPanelUIConfig getPlayerErrorPanelUIConfigForCommon() {
        PlayerErrorPanelUIConfig config = new PlayerErrorPanelUIConfig();
        config.setBackgroundScaleType(ScaleType.FIT_XY).setMainTextColor(ResourceUtil.getColor(R.color.player_ui_common_error_tip_main), ResourceUtil.getColor(R.color.player_ui_common_error_tip_main)).setMainTextSize(ResourceUtil.getDimensionPixelSize(R.dimen.dimen_20dp)).setCornerTextColor(ResourceUtil.getColor(R.color.player_ui_common_error_tip_corner), ResourceUtil.getColor(R.color.player_ui_common_error_tip_corner)).setCornerTextSize(ResourceUtil.getDimensionPixelSize(R.dimen.dimen_17dp));
        return config;
    }

    public static PlayerErrorPanelUIConfig getPlayerErrorPanelUIConfigForCarousel() {
        PlayerErrorPanelUIConfig config = new PlayerErrorPanelUIConfig();
        config.setBackgroundScaleType(ScaleType.FIT_XY);
        return config;
    }

    public static PlayerErrorPanelInfo getPlayerErrorPanelInfoForVIP() {
        PlayerErrorPanelInfo info = new PlayerErrorPanelInfo();
        info.setBackgroundDrawable(getPlayerErrorBGForVIP()).setMainText(ResourceUtil.getStr(R.string.player_vip_error_main_text));
        return info;
    }

    public static PlayerErrorPanelInfo getPlayerErrorPanelInfoForCommon(String errorMessage) {
        PlayerErrorPanelInfo info = new PlayerErrorPanelInfo();
        info.setBackgroundDrawable(LoadingViewAnimManager.getInstance(ResourceUtil.getContext()).getBackground()).setMainText(errorMessage).setCornerText(ResourceUtil.getStr(R.string.player_common_error_tip_text));
        return info;
    }

    public static PlayerErrorPanelInfo getPlayerErrorPanelInfoForCarousel() {
        PlayerErrorPanelInfo info = new PlayerErrorPanelInfo();
        info.setBackgroundDrawable(ResourceUtil.getDrawable(R.drawable.player_bg_fullscreen_live_nosignal));
        return info;
    }

    private static Drawable getPlayerErrorBGForVIP() {
        if (sPlayerErrorBGForVIP != null) {
            Drawable drawable = sPlayerErrorBGForVIP;
            LogRecordUtils.logd(TAG, "getPlayerErrorBGForVIP, use cached bg sPlayerErrorBGForVIP=" + sPlayerErrorBGForVIP);
            return drawable;
        }
        drawable = LoadingViewAnimManager.getInstance(ResourceUtil.getContext()).getBackground();
        ThreadUtils.execute(new Runnable() {
            public void run() {
                CharSequence path = UiUtils.getPlayerErrorBGPath();
                if (!StringUtils.isEmpty(path)) {
                    Bitmap bitmap = UiUtils.createTargetBitmap(path, ResourceUtil.getDimen(R.dimen.dimen_600dp), ResourceUtil.getDimen(R.dimen.dimen_337dp), ImageRequest.ScaleType.CENTER_INSIDE, Config.ARGB_8888);
                    LogRecordUtils.logd(UiUtils.TAG, "getPlayerErrorBGForVIP, fetched online bitmap, path=" + path + ", bitmap=" + bitmap);
                    if (bitmap != null) {
                        UiUtils.sPlayerErrorBGForVIP = new BitmapDrawable(ResourceUtil.getResource(), bitmap);
                        LogRecordUtils.logd(UiUtils.TAG, "getPlayerErrorBGForVIP, fetched sPlayerErrorBGForVIP=" + UiUtils.sPlayerErrorBGForVIP);
                    }
                }
            }
        });
        return drawable;
    }

    private static String getPlayerErrorBGPath() {
        String path = null;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (!(model == null || ListUtils.isEmpty(model.getBugVipTipPicPath()))) {
            List loadinglist = model.getBugVipTipPicPath();
            if (!ListUtils.isEmpty(loadinglist)) {
                path = (String) loadinglist.get(0);
            }
        }
        LogRecordUtils.logd(TAG, "getPlayerErrorBGPath, path=" + path);
        return path;
    }

    private static Bitmap createTargetBitmap(String path, int targetWidth, int targetHeight, ImageRequest.ScaleType type, Config config) {
        int i = 1;
        try {
            Options dbo = new Options();
            dbo.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, dbo);
            int nativeWidth = dbo.outWidth;
            int nativeHeight = dbo.outHeight;
            Options options = createBasicOptions(config);
            if (nativeWidth > targetWidth || nativeHeight > targetHeight) {
                float dx = ((float) nativeWidth) / ((float) targetWidth);
                float dy = ((float) nativeHeight) / ((float) targetHeight);
                float scale = type == ImageRequest.ScaleType.CENTER_INSIDE ? Math.max(dx, dy) : Math.min(dx, dy);
                if (scale > 1.0f) {
                    i = Math.round(scale);
                }
                options.inSampleSize = i;
            } else {
                options.inSampleSize = 1;
            }
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(path, options);
        } catch (OutOfMemoryError ex) {
            LogUtils.e(TAG, "createTargetBitmap: OOM", ex);
            return null;
        }
    }

    private static Options createBasicOptions(Config config) {
        Options options = new Options();
        if (config != null) {
            options.inPreferredConfig = config;
            if (config != Config.ARGB_8888) {
                options.inDither = true;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "createBasicOptions() return " + options);
        }
        return options;
    }
}

package com.gala.video.app.epg.home.utils;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.utils.EpgImageCache;
import com.gala.video.lib.share.utils.ImageCacheUtil;
import com.gala.video.lib.share.utils.ResourceUtil;

public class ItemUiFactory {
    public static int getRectBgResId(boolean isVipTab) {
        if (isVipTab) {
            return C0508R.drawable.share_item_vip_selector;
        }
        return C0508R.drawable.share_item_rect_selector;
    }

    public static int getRectBtnResId(boolean isVipTab) {
        if (isVipTab) {
            return C0508R.drawable.share_item_rect_vip_btn_selector;
        }
        return C0508R.drawable.share_item_rect_btn_selector;
    }

    public static Drawable getRectBgDrawable(boolean isVipTab) {
        if (isVipTab) {
            return ImageCacheUtil.RECT_BG_VIP_DRAWABLE;
        }
        return ImageCacheUtil.RECT_BG_DRAWABLE;
    }

    public static Drawable getRectBtnDrawable(boolean isVipTab) {
        if (isVipTab) {
            return ImageCacheUtil.RECT_BTN_VIP_DRAWABLE;
        }
        return ImageCacheUtil.RECT_BTN_DRAWABLE;
    }

    public static Drawable getSkewBgDrawable(boolean isVipTab) {
        if (isVipTab) {
            return ResourceUtil.getDrawable(C0508R.drawable.epg_item_skew_bg_vip);
        }
        return ResourceUtil.getDrawable(C0508R.drawable.epg_item_skew_bg);
    }

    public static Drawable getCircleBgFocusDrawable(boolean isVipTab) {
        if (isVipTab) {
            return ImageCacheUtil.CIRCLE_BG_VIP_FOCUS_DRAWABLE;
        }
        return ImageCacheUtil.CIRCLE_BG_FOCUS_DRAWABLE;
    }

    public static Drawable getFocusColorDrawable(boolean isVipTab) {
        if (isVipTab) {
            return ImageCacheUtil.TITLE_FOCUS_VIP_DRAWABLE;
        }
        return ImageCacheUtil.TITLE_FOCUS_DRAWABLE;
    }

    public static int getCircleTextSpecialFocusColor(boolean isVipTab) {
        if (isVipTab) {
            return ResourceUtil.getColor(C0508R.color.item_vip_focus_color);
        }
        return ResourceUtil.getColor(C0508R.color.item_normal_focus_color);
    }

    public static Drawable getSearchHistoryViewBGDrawable(Boolean isVipTab) {
        if (isVipTab.booleanValue()) {
            return EpgImageCache.SEARCH_HISTORY_RECT_VIP_BTN_DRAWABLE;
        }
        return EpgImageCache.SEARCH_HISTORY_RECT_BTN_DRAWABLE;
    }

    public static Drawable getSearchHistoryViewNoShadowBGDrawable(Boolean isVipTab) {
        if (isVipTab.booleanValue()) {
            return EpgImageCache.SEARCH_HISTORY_RECT_VIP_BTN_NO_SHADOW_DRAWABLE;
        }
        return EpgImageCache.SEARCH_HISTORY_RECT_BTN_NO_SHADOW_DRAWABLE;
    }

    public static String getCarsoulChannelId(String id) {
        if (TextUtils.isEmpty(id)) {
            return null;
        }
        if (id.length() == 1) {
            return "0" + id;
        }
        return id;
    }
}

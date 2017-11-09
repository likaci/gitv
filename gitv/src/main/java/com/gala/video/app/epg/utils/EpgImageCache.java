package com.gala.video.app.epg.utils;

import android.graphics.drawable.Drawable;
import com.gala.video.app.epg.R;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.ResourceUtil;

public class EpgImageCache {
    public static Drawable CARD_COVER_COLOR_UNFOCUS_DRAWABLE = ResourceUtil.getDrawable(R.drawable.epg_card_item_title_cover_unfocus_bg);
    public static Drawable CARD_ITEM_DESC_BG = ResourceUtil.getDrawable(R.drawable.epg_card_item_desc_bg);
    public static Drawable CARD_POPUP_GREEN_BG = ResourceUtil.getDrawable(R.drawable.epg_card_popup_green_bg);
    public static Drawable COVER_COLOR_UNFOCUS_DRAWABLE = ResourceUtil.getDrawable(R.drawable.epg_item_title_cover_unfocus_bg);
    public static Drawable COVER_COLOR_UNFOCUS_DRAWABLE_FOR_RECOMMENDVIEW = ResourceUtil.getDrawable(R.drawable.epg_item_title_cover_unfocus_bg_for_recommendview);
    public static Drawable DAILY_NEWS_COLOR_DRAWABLE = CARD_COVER_COLOR_UNFOCUS_DRAWABLE;
    public static Drawable DEFAULT_DAILY_NEWS_DRAWABLE = ResourceUtil.getDrawable(R.drawable.epg_tab_home_rec_news);
    public static Drawable DEFAULT_NO_BG_DRAWABLE = ResourceUtil.getDrawable(Project.getInstance().getResProvider().getDefaultNoBGCover());
    public static Drawable HOME_HISTORY_ICON = ResourceUtil.getDrawable(R.drawable.epg_home_history_icon);
    public static Drawable HOME_SEARCH_ICON = ResourceUtil.getDrawable(R.drawable.epg_home_search_icon);
    public static Drawable SEARCH_HISTORY_RECT_BTN_DRAWABLE = ResourceUtil.getDrawable(R.drawable.epg_search_history_item_rect_btn_selector);
    public static Drawable SEARCH_HISTORY_RECT_BTN_NO_SHADOW_DRAWABLE = ResourceUtil.getDrawable(R.drawable.epg_search_history_item_rect_btn_no_shadow_selector);
    public static Drawable SEARCH_HISTORY_RECT_VIP_BTN_DRAWABLE = ResourceUtil.getDrawable(R.drawable.epg_search_history_item_rect_vip_btn_selector);
    public static Drawable SEARCH_HISTORY_RECT_VIP_BTN_NO_SHADOW_DRAWABLE = ResourceUtil.getDrawable(R.drawable.epg_search_history_item_rect_vip_btn_no_shadow_selector);
    public static Drawable UNCOVER_COLOR_UNFOCUS_DRAWABLE_NO_SKIN = ResourceUtil.getDrawable(R.drawable.epg_item_title_uncover_unfocus_bg_no_skin);
    public static Drawable UNFOCUS_CHANNELID_ALL_COLOR_DRAWABLE = ResourceUtil.getDrawable(R.drawable.epg_item_channel_all_id_unfocus_bg);
    public static Drawable UNFOCUS_CHANNELID_COLOR_DRAWABLE = ResourceUtil.getDrawable(R.drawable.epg_item_channel_id_unfocus_bg);

    public static void resetCacheAfterSkinChange() {
        DEFAULT_NO_BG_DRAWABLE = ResourceUtil.getDrawable(Project.getInstance().getResProvider().getDefaultNoBGCover());
        DEFAULT_DAILY_NEWS_DRAWABLE = ResourceUtil.getDrawable(R.drawable.epg_tab_home_rec_news);
        UNFOCUS_CHANNELID_ALL_COLOR_DRAWABLE = ResourceUtil.getDrawable(R.drawable.epg_item_channel_all_id_unfocus_bg);
        HOME_SEARCH_ICON = ResourceUtil.getDrawable(R.drawable.epg_home_search_icon);
        HOME_HISTORY_ICON = ResourceUtil.getDrawable(R.drawable.epg_home_history_icon);
    }
}

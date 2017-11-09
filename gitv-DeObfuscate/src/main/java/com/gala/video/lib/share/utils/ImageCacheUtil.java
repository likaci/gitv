package com.gala.video.lib.share.utils;

import android.graphics.drawable.Drawable;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.project.Project;

public class ImageCacheUtil {
    public static Drawable ALBUM_DESC_BG = ResourceUtil.getDrawable(C1632R.drawable.share_album_desc_bg);
    public static Drawable CIRCLE_BG_FOCUS_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_item_circle_bg_focus);
    public static Drawable CIRCLE_BG_UNFOCUS_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_item_circle_bg_unfocus);
    public static Drawable CIRCLE_BG_VIP_FOCUS_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_item_circle_bg_focus_vip);
    public static Drawable CORNER_3D_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_corner_3d);
    public static Drawable CORNER_BG_LEFT = ResourceUtil.getDrawable(C1632R.drawable.share_corner_bg_left);
    public static Drawable CORNER_BG_RIGHT = ResourceUtil.getDrawable(C1632R.drawable.share_corner_bg_right);
    public static Drawable CORNER_DIANBOQUAN_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_corner_yongquan);
    public static Drawable CORNER_DOLBY_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_corner_dolby);
    public static Drawable CORNER_DUBO_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_corner_dubo);
    public static Drawable CORNER_END_LIVING_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_corner_end_living);
    public static Drawable CORNER_FOCUS_IMG_AD = ResourceUtil.getDrawable(C1632R.drawable.share_corner_focus_image_ad);
    public static Drawable CORNER_FUFEIDIANBO_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_corner_fufeidianbo);
    public static Drawable CORNER_LIVING_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_corner_living);
    public static Drawable CORNER_NOTICE_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_corner_notice);
    public static Drawable CORNER_VIP_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_corner_vip);
    public static Drawable DEFAULT_CIRCLE_DRAWABLE = ResourceUtil.getDrawable(Project.getInstance().getResProvider().getDefaultCircleCover());
    public static Drawable DEFAULT_DRAWABLE = ResourceUtil.getDrawable(Project.getInstance().getResProvider().getDefaultCover());
    public static Drawable DEFAULT_DRAWABLE_NO_SKIN = ResourceUtil.getDrawable(C1632R.drawable.share_default_image_no_skin);
    public static Drawable RECT_BG_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_item_rect_selector);
    public static Drawable RECT_BG_VIP_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_item_vip_selector);
    public static Drawable RECT_BTN_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_item_rect_btn_selector);
    public static Drawable RECT_BTN_VIP_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_item_rect_vip_btn_selector);
    public static Drawable RECYCLE_COVER = ResourceUtil.getDrawable(C1632R.drawable.share_album_recycle_cover);
    public static Drawable TITLE_FOCUS_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_item_title_focus_bg);
    public static Drawable TITLE_FOCUS_VIP_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_item_title_focus_vip_bg);
    public static Drawable UNCOVER_COLOR_UNFOCUS_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_item_title_uncover_unfocus_bg);
    public static Drawable UNCOVER_COLOR_UNFOCUS_DRAWABLE_FOR_PLAYER = ResourceUtil.getDrawable(C1632R.drawable.share_item_title_uncover_unfocus_bg_for_player);

    public static void resetCacheAfterSkinChange() {
        DEFAULT_DRAWABLE = ResourceUtil.getDrawable(Project.getInstance().getResProvider().getDefaultCover());
        DEFAULT_CIRCLE_DRAWABLE = ResourceUtil.getDrawable(Project.getInstance().getResProvider().getDefaultCircleCover());
        RECT_BTN_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_item_rect_btn_selector);
        UNCOVER_COLOR_UNFOCUS_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_item_title_uncover_unfocus_bg);
        CIRCLE_BG_UNFOCUS_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_item_circle_bg_unfocus);
        CIRCLE_BG_VIP_FOCUS_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_item_circle_bg_focus_vip);
        CIRCLE_BG_FOCUS_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_item_circle_bg_focus);
        RECT_BTN_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_item_rect_btn_selector);
        RECT_BTN_VIP_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_item_rect_vip_btn_selector);
        RECT_BG_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_item_rect_selector);
        RECT_BG_VIP_DRAWABLE = ResourceUtil.getDrawable(C1632R.drawable.share_item_vip_selector);
    }
}

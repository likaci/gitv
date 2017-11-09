package com.gala.video.app.player.ui.config.style.common;

import com.gala.video.app.player.R;
import com.gala.video.app.player.ui.config.style.IEpisodeListUIStyle;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.gala.video.widget.ItemPopupWindow.VerticalPosition;
import com.gala.video.widget.episode.ParentLayoutMode;
import java.util.Arrays;

public class EpisodeListUIStyle implements IEpisodeListUIStyle {
    public int getTipsTextColor() {
        return ResourceUtil.getColor(R.color.black);
    }

    public int getTipsBgResId() {
        return R.drawable.player_bg_txt_dropup_tips;
    }

    public int getTipsTextSizeResId() {
        return R.dimen.dimen_16dp;
    }

    public VerticalPosition getTipsShowLocation() {
        return VerticalPosition.DROPUP;
    }

    public int getItemBgResId() {
        return R.drawable.player_episode_item_bg2;
    }

    public int getItemWidthPx() {
        return ResourceUtil.getDimen(R.dimen.dimen_114dp);
    }

    public int getItemHeigthPx() {
        return ResourceUtil.getDimen(R.dimen.dimen_53dp);
    }

    public int getItemSpacingPx() {
        return 0;
    }

    public int getItemTextSizeId() {
        return R.dimen.dimen_26dp;
    }

    public int getItemTextColorNormal() {
        return ResourceUtil.getColor(R.color.detail_text_color_default);
    }

    public int getItemTextColorSelected() {
        return ResourceUtil.getColor(R.color.detail_text_color_selected);
    }

    public int getItemTextColorFocused() {
        return ResourceUtil.getColor(R.color.detail_text_color_focused);
    }

    public int getItemTextDisableNormal() {
        return ResourceUtil.getColor(R.color.detail_text_color_disable_normal);
    }

    public int getItemTextDisableFocused() {
        return ResourceUtil.getColor(R.color.detail_text_color_disable_focused);
    }

    public int getParentItemTextColorNormal() {
        return ResourceUtil.getColor(R.color.detail_text_color_default);
    }

    public int getParentItemHeightPx() {
        return ResourceUtil.getDimen(R.dimen.dimen_33dp);
    }

    public int getParentItemTextSizeId() {
        return R.dimen.dimen_24dp;
    }

    public ParentLayoutMode getParentLayoutMode() {
        return ParentLayoutMode.DOUBLE_CHILD_WIDTH;
    }

    public int getCornerIconResId() {
        return R.drawable.player_trailer_item_icon;
    }

    public int[] getCornerImgMargins() {
        int[] cornerImgMargins = new int[]{0, 0, 0, 0};
        if (cornerImgMargins == null || cornerImgMargins.length != 4) {
            throw new IllegalArgumentException("Please provide exactly 4 parameters for cornerImgMargins");
        }
        int[] iArr = new int[4];
        return Arrays.copyOf(cornerImgMargins, 4);
    }

    public int getArrowMarginLeft() {
        return ResourceUtil.getDimensionPixelSize(R.dimen.dimen_32dp);
    }

    public int getArrowMarginLeftTop() {
        return ResourceUtil.getDimensionPixelSize(R.dimen.dimen_98dp);
    }

    public int getArrowMarginRightTop() {
        return ResourceUtil.getDimensionPixelSize(R.dimen.dimen_98dp);
    }

    public int getArrowMarginRight() {
        return ResourceUtil.getDimensionPixelSize(R.dimen.dimen_32dp);
    }

    public int getEpisodeMarginLeft() {
        return ResourceUtil.getDimensionPixelSize(R.dimen.dimen_55dp);
    }

    public int getEpisodeMarginTop() {
        return ResourceUtil.getDimensionPixelSize(R.dimen.dimen_58dp);
    }

    public int getEpisodeMarginRight() {
        return 0;
    }

    public boolean isDetail() {
        return true;
    }
}

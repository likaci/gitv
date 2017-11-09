package com.gala.video.app.player.ui.config.style;

import com.gala.video.widget.ItemPopupWindow.VerticalPosition;
import com.gala.video.widget.episode.ParentLayoutMode;

public interface IEpisodeListUIStyle {
    int getArrowMarginLeft();

    int getArrowMarginLeftTop();

    int getArrowMarginRight();

    int getArrowMarginRightTop();

    int getCornerIconResId();

    int[] getCornerImgMargins();

    int getEpisodeMarginLeft();

    int getEpisodeMarginRight();

    int getEpisodeMarginTop();

    int getItemBgResId();

    int getItemHeigthPx();

    int getItemSpacingPx();

    int getItemTextColorFocused();

    int getItemTextColorNormal();

    int getItemTextColorSelected();

    int getItemTextDisableFocused();

    int getItemTextDisableNormal();

    int getItemTextSizeId();

    int getItemWidthPx();

    int getParentItemHeightPx();

    int getParentItemTextColorNormal();

    int getParentItemTextSizeId();

    ParentLayoutMode getParentLayoutMode();

    int getTipsBgResId();

    VerticalPosition getTipsShowLocation();

    int getTipsTextColor();

    int getTipsTextSizeResId();

    boolean isDetail();
}

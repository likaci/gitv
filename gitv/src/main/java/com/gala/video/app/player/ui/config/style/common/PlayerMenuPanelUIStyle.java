package com.gala.video.app.player.ui.config.style.common;

import android.widget.FrameLayout.LayoutParams;
import com.gala.video.app.player.R;
import com.gala.video.app.player.ui.config.style.IPlayerMenuPanelUIStyle;
import com.gala.video.lib.share.utils.ResourceUtil;

public class PlayerMenuPanelUIStyle implements IPlayerMenuPanelUIStyle {
    public LayoutParams getDefCornerImgLayoutParams() {
        return null;
    }

    public int getTabContentMarginPx() {
        return ResourceUtil.getDimen(R.dimen.dimen_67dp);
    }

    public int getMenuPanelLayoutParaW() {
        return -1;
    }

    public int getMenuPanelLayoutParaH() {
        return -2;
    }

    public int getMenuPanelBgResId() {
        return R.drawable.player_menupanel_newback;
    }

    public int getDefCornerIconResId() {
        return R.drawable.share_corner_vip;
    }

    public int getTabWidgetMarginPx() {
        return ResourceUtil.getDimen(R.dimen.dimen_67dp);
    }

    public int getTabContentBgResId() {
        return R.color.transparent;
    }

    public int getTabWidgetHeightPx() {
        return ResourceUtil.getDimen(R.dimen.player_tab_widget_height);
    }

    public int getTabWidgetWidthPx() {
        return ResourceUtil.getDimen(R.dimen.dimen_201dp);
    }

    public int getTabContentHeightPx() {
        return ResourceUtil.getDimen(R.dimen.player_tab_content_height);
    }

    public int getTabContentWidthPx() {
        return -1;
    }

    public int getMenuPanelLayoutResId() {
        return R.layout.player_menupanel2;
    }
}

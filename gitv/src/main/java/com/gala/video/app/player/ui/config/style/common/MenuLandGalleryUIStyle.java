package com.gala.video.app.player.ui.config.style.common;

import com.gala.video.app.player.R;
import com.gala.video.lib.share.utils.ResourceUtil;

public class MenuLandGalleryUIStyle implements IGalleryUIStyle {
    public boolean isDetailGallery() {
        return false;
    }

    public boolean isPort() {
        return false;
    }

    public boolean isExitDialog() {
        return false;
    }

    public int getGalleryMarginLeft() {
        return ResourceUtil.getDimensionPixelSize(R.dimen.dimen_40dp);
    }

    public int getGalleryMarginTop() {
        return ResourceUtil.getDimensionPixelSize(R.dimen.dimen_38dp);
    }

    public int getGalleryMarginRight() {
        return ResourceUtil.getDimensionPixelSize(R.dimen.dimen_0dp);
    }

    public int getGalleryMarginBottom() {
        return 0;
    }
}

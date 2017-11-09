package com.gala.video.app.player.ui.config.style.common;

import com.gala.video.app.player.R;
import com.gala.video.lib.share.utils.ResourceUtil;

public class ExitDialogPortGalleryUIStyle implements IGalleryUIStyle {
    public int getGalleryMarginLeft() {
        return ResourceUtil.getDimensionPixelSize(R.dimen.dimen_16dp);
    }

    public int getGalleryMarginTop() {
        return ResourceUtil.getDimensionPixelSize(R.dimen.dimen_15dp);
    }

    public int getGalleryMarginRight() {
        return 0;
    }

    public int getGalleryMarginBottom() {
        return 0;
    }

    public boolean isDetailGallery() {
        return false;
    }

    public boolean isPort() {
        return true;
    }

    public boolean isExitDialog() {
        return true;
    }
}

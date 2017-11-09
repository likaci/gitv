package com.gala.video.lib.share.project.res;

import com.gala.video.lib.share.R;

public class ResourceProvider implements IResourceInterface {
    public int getDefaultCover() {
        return R.drawable.share_default_image;
    }

    public int getDefaultNoBGCover() {
        return R.drawable.share_default_no_bg_image;
    }

    public int getDefaultCircleCover() {
        return R.drawable.share_default_circle_image;
    }

    public int getCannotConnInternet() {
        return R.string.cannot_conn_internet;
    }
}

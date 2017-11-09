package com.gala.video.lib.share.project.res;

import com.gala.video.lib.share.C1632R;

public class ResourceProvider implements IResourceInterface {
    public int getDefaultCover() {
        return C1632R.drawable.share_default_image;
    }

    public int getDefaultNoBGCover() {
        return C1632R.drawable.share_default_no_bg_image;
    }

    public int getDefaultCircleCover() {
        return C1632R.drawable.share_default_circle_image;
    }

    public int getCannotConnInternet() {
        return C1632R.string.cannot_conn_internet;
    }
}

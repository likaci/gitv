package com.gala.video.lib.share.uikit.item;

import android.graphics.drawable.Drawable;
import com.gala.video.lib.share.uikit.contract.AppItemContract.Presenter;

public class AppItem extends Item implements Presenter {
    public static final int APP_TYPE_ALL = 3;
    public static final int APP_TYPE_LOCAL = 1;
    public static final int APP_TYPE_SERVER = 2;
    public static final int APP_TYPE_STORE = 4;
    Drawable mDrawable = null;

    public void setIconDrawable(Drawable drawable) {
        this.mDrawable = drawable;
    }

    public Drawable getIconDrawable() {
        return this.mDrawable;
    }
}

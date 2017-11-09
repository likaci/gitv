package com.gala.video.lib.share.uikit.contract;

import android.graphics.drawable.Drawable;

public class AppItemContract {

    public interface Presenter extends com.gala.video.lib.share.uikit.contract.ItemContract.Presenter {
        Drawable getIconDrawable();

        void setIconDrawable(Drawable drawable);
    }
}

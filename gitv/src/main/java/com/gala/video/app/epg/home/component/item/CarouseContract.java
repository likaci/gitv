package com.gala.video.app.epg.home.component.item;

import android.animation.Animator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View.OnClickListener;

public interface CarouseContract {

    public interface Presenter extends com.gala.video.lib.share.uikit.contract.ItemContract.Presenter {
        void addObserver();

        void onDetachedFromWindow();

        void onShow();

        void onUnBind();

        void removeObserver();

        void requestFocus();

        void setView(Context context, View view);
    }

    public interface View {
        Animator alphaAnimationPlayCover();

        void coverRequestFocus();

        int getCoverHeight();

        int getCoverWidth();

        void getLocation(int[] iArr);

        android.view.View getPlayCoverView();

        boolean isCoverAttached();

        void setCoverAlpha(float f);

        void setCoverClickListener(OnClickListener onClickListener);

        void setCoverImage(Drawable drawable);
    }
}

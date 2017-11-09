package com.gala.video.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

public abstract class AbsGalleryPagerItem extends RelativeLayout {
    protected static final int SCALE_ANIM_DURATION = 200;
    protected static float sScaleAnimRatio = 1.05f;
    protected Context mContext;
    protected int mCornerIconResId = 0;
    protected Drawable mFocusDrawable;
    protected boolean mFocused;
    protected Drawable mNormalDrawable;

    public abstract Drawable getFocusBackground();

    public abstract Drawable getNormalBackground();

    public abstract void setCornerIconResId(int i);

    public abstract void setImageBitmap(Bitmap bitmap);

    public abstract void setImageBitmap(Bitmap bitmap, Animation animation);

    public abstract void setIsPlaying(boolean z);

    public abstract void setText(String str);

    public AbsGalleryPagerItem(Context context) {
        super(context);
        this.mContext = context;
    }

    public AbsGalleryPagerItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public AbsGalleryPagerItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public void setScore(String score) {
    }

    public void updateTime(Object itemInfo) {
    }

    public void setOnlineTime(Object onlineTime) {
    }
}

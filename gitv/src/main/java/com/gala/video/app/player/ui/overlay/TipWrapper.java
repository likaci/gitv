package com.gala.video.app.player.ui.overlay;

import android.graphics.drawable.Drawable;
import android.widget.RelativeLayout;
import com.gala.sdk.player.ITip;
import com.gala.sdk.player.TipExtra;
import com.gala.sdk.player.TipType;

public class TipWrapper implements ITip {
    private RelativeLayout mAdView;
    private CharSequence mContent;
    private Drawable mDrawable;
    private int mDuration;
    private TipExtra mExtra;
    private int mHeight;
    private int mID = 0;
    private Runnable mRunnable;
    private TipType mType;
    private int mWidth;

    TipWrapper(ITip tip) {
        this.mRunnable = tip.getRunnable();
        this.mDuration = tip.getShowDuration();
        this.mExtra = tip.getTipExtra();
        this.mType = tip.getTipType();
    }

    public TipWrapper setContent(CharSequence content) {
        this.mContent = content;
        return this;
    }

    public TipWrapper setDrawable(Drawable drawable) {
        this.mDrawable = drawable;
        return this;
    }

    public TipWrapper setWidth(int width) {
        this.mWidth = width;
        return this;
    }

    public TipWrapper setHeight(int height) {
        this.mHeight = height;
        return this;
    }

    public CharSequence getContent() {
        return this.mContent;
    }

    public Drawable getDrawable() {
        return this.mDrawable;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public TipWrapper setAdView(RelativeLayout rl) {
        this.mAdView = rl;
        return this;
    }

    public RelativeLayout getAdView() {
        return this.mAdView;
    }

    public TipWrapper setAdID(int id) {
        this.mID = id;
        return this;
    }

    public int getAdID() {
        return this.mID;
    }

    public Runnable getRunnable() {
        return this.mRunnable;
    }

    public int getShowDuration() {
        return this.mDuration;
    }

    public TipExtra getTipExtra() {
        return this.mExtra;
    }

    public TipType getTipType() {
        return this.mType;
    }
}

package com.gala.afinal.bitmap.core;

import android.graphics.Bitmap;
import android.view.animation.Animation;

public class BitmapDisplayConfig {
    private int f12a;
    private Bitmap f13a;
    private Animation f14a;
    private int f15b;
    private Bitmap f16b;
    private int f17c;

    public class AnimationType {
        public static final int fadeIn = 1;
        public static final int userDefined = 0;

        public AnimationType(BitmapDisplayConfig bitmapDisplayConfig) {
        }
    }

    public int getBitmapWidth() {
        return this.f12a;
    }

    public void setBitmapWidth(int bitmapWidth) {
        this.f12a = bitmapWidth;
    }

    public int getBitmapHeight() {
        return this.f15b;
    }

    public void setBitmapHeight(int bitmapHeight) {
        this.f15b = bitmapHeight;
    }

    public Animation getAnimation() {
        return this.f14a;
    }

    public void setAnimation(Animation animation) {
        this.f14a = animation;
    }

    public int getAnimationType() {
        return this.f17c;
    }

    public void setAnimationType(int animationType) {
        this.f17c = animationType;
    }

    public Bitmap getLoadingBitmap() {
        return this.f13a;
    }

    public void setLoadingBitmap(Bitmap loadingBitmap) {
        this.f13a = loadingBitmap;
    }

    public Bitmap getLoadfailBitmap() {
        return this.f16b;
    }

    public void setLoadfailBitmap(Bitmap loadfailBitmap) {
        this.f16b = loadfailBitmap;
    }
}

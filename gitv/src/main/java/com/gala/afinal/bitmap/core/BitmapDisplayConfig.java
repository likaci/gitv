package com.gala.afinal.bitmap.core;

import android.graphics.Bitmap;
import android.view.animation.Animation;

public class BitmapDisplayConfig {
    private int a;
    private Bitmap f1a;
    private Animation f2a;
    private int b;
    private Bitmap f3b;
    private int c;

    public class AnimationType {
        public static final int fadeIn = 1;
        public static final int userDefined = 0;

        public AnimationType(BitmapDisplayConfig bitmapDisplayConfig) {
        }
    }

    public int getBitmapWidth() {
        return this.a;
    }

    public void setBitmapWidth(int bitmapWidth) {
        this.a = bitmapWidth;
    }

    public int getBitmapHeight() {
        return this.b;
    }

    public void setBitmapHeight(int bitmapHeight) {
        this.b = bitmapHeight;
    }

    public Animation getAnimation() {
        return this.f2a;
    }

    public void setAnimation(Animation animation) {
        this.f2a = animation;
    }

    public int getAnimationType() {
        return this.c;
    }

    public void setAnimationType(int animationType) {
        this.c = animationType;
    }

    public Bitmap getLoadingBitmap() {
        return this.f1a;
    }

    public void setLoadingBitmap(Bitmap loadingBitmap) {
        this.f1a = loadingBitmap;
    }

    public Bitmap getLoadfailBitmap() {
        return this.f3b;
    }

    public void setLoadfailBitmap(Bitmap loadfailBitmap) {
        this.f3b = loadfailBitmap;
    }
}

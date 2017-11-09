package com.gala.cloudui.block;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import com.gala.cloudui.imp.ICuteBg;
import com.gala.cloudui.utils.CloudUtilsGala;

public class CuteBg extends Cute implements ICuteBg {
    private int a;
    private Drawable f265a;
    private int b;
    private Drawable f266b;
    private int c;
    private Drawable f267c;
    private int d;

    public int getPaddingLeft() {
        return this.a;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.a = paddingLeft;
    }

    public int getPaddingTop() {
        return this.b;
    }

    public void setPaddingTop(int paddingTop) {
        this.b = paddingTop;
    }

    public int getPaddingRight() {
        return this.c;
    }

    public void setPaddingRight(int paddingRight) {
        this.c = paddingRight;
    }

    public int getPaddingBottom() {
        return this.d;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.d = paddingBottom;
    }

    public Drawable getBackgroundDrawable() {
        if (this.f265a == null) {
            this.f265a = getCloudView().getBackground();
        }
        this.f266b = CloudUtilsGala.getCurStateDrawable(this.f265a, new int[]{16842908});
        this.f267c = CloudUtilsGala.getCurStateDrawable(this.f265a, new int[]{16842910});
        return this.f265a;
    }

    public void setBackgroundDrawable(Drawable mBackgroundDrawable) {
        this.f265a = mBackgroundDrawable;
    }

    public void draw(Canvas canvas) {
        int paddingLeft = getCloudView().getPaddingLeft();
        int paddingTop = getCloudView().getPaddingTop();
        int itemWidth = getCloudView().getItemWidth() - getCloudView().getPaddingRight();
        int itemHeight = getCloudView().getItemHeight() - getCloudView().getPaddingBottom();
        if (getCloudView().isFocused()) {
            a(canvas, paddingLeft, paddingTop, itemWidth, itemHeight, this.f266b);
            return;
        }
        a(canvas, paddingLeft, paddingTop, itemWidth, itemHeight, this.f267c);
    }

    private void a(Canvas canvas, int i, int i2, int i3, int i4, Drawable drawable) {
        if (drawable == null) {
            drawable = CloudUtilsGala.getCurStateDrawable(getBackgroundDrawable(), getCloudView().getDrawableState());
        }
        if (drawable != null) {
            drawable.setBounds(i, i2, i3, i4);
            drawable.draw(canvas);
        }
    }

    public void suck(Cute cute) {
        super.suck(cute);
        CuteBg cuteBg = (CuteBg) cute;
        this.a = cuteBg.a;
        this.b = cuteBg.b;
        this.c = cuteBg.c;
        this.d = cuteBg.d;
        clearDrawable(this.f265a);
        this.f265a = cuteBg.f265a;
        clearDrawable(this.f266b);
        this.f266b = cuteBg.f266b;
        clearDrawable(this.f267c);
        this.f267c = cuteBg.f267c;
    }
}

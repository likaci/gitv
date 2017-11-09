package com.gala.cloudui.block;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import com.gala.cloudui.imp.ICuteBg;
import com.gala.cloudui.utils.CloudUtilsGala;

public class CuteBg extends Cute implements ICuteBg {
    private int f382a;
    private Drawable f383a;
    private int f384b;
    private Drawable f385b;
    private int f386c;
    private Drawable f387c;
    private int f388d;

    public int getPaddingLeft() {
        return this.f382a;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.f382a = paddingLeft;
    }

    public int getPaddingTop() {
        return this.f384b;
    }

    public void setPaddingTop(int paddingTop) {
        this.f384b = paddingTop;
    }

    public int getPaddingRight() {
        return this.f386c;
    }

    public void setPaddingRight(int paddingRight) {
        this.f386c = paddingRight;
    }

    public int getPaddingBottom() {
        return this.f388d;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.f388d = paddingBottom;
    }

    public Drawable getBackgroundDrawable() {
        if (this.f383a == null) {
            this.f383a = getCloudView().getBackground();
        }
        this.f385b = CloudUtilsGala.getCurStateDrawable(this.f383a, new int[]{16842908});
        this.f387c = CloudUtilsGala.getCurStateDrawable(this.f383a, new int[]{16842910});
        return this.f383a;
    }

    public void setBackgroundDrawable(Drawable mBackgroundDrawable) {
        this.f383a = mBackgroundDrawable;
    }

    public void draw(Canvas canvas) {
        int paddingLeft = getCloudView().getPaddingLeft();
        int paddingTop = getCloudView().getPaddingTop();
        int itemWidth = getCloudView().getItemWidth() - getCloudView().getPaddingRight();
        int itemHeight = getCloudView().getItemHeight() - getCloudView().getPaddingBottom();
        if (getCloudView().isFocused()) {
            m248a(canvas, paddingLeft, paddingTop, itemWidth, itemHeight, this.f385b);
            return;
        }
        m248a(canvas, paddingLeft, paddingTop, itemWidth, itemHeight, this.f387c);
    }

    private void m248a(Canvas canvas, int i, int i2, int i3, int i4, Drawable drawable) {
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
        this.f382a = cuteBg.f382a;
        this.f384b = cuteBg.f384b;
        this.f386c = cuteBg.f386c;
        this.f388d = cuteBg.f388d;
        clearDrawable(this.f383a);
        this.f383a = cuteBg.f383a;
        clearDrawable(this.f385b);
        this.f385b = cuteBg.f385b;
        clearDrawable(this.f387c);
        this.f387c = cuteBg.f387c;
    }
}

package com.gala.cloudui.block;

import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import com.gala.cloudui.CloudViewGala;
import java.io.Serializable;
import pl.droidsonroids.gif.GifDrawable;

public abstract class Cute implements Serializable {
    private int a;
    private CloudViewGala f249a;
    private String f250a;
    private String b;

    public abstract void draw(Canvas canvas);

    public void setId(String id) {
        this.f250a = id;
    }

    public String getId() {
        return this.f250a;
    }

    public String getType() {
        return this.b;
    }

    public void setType(String mType) {
        this.b = mType;
    }

    public int getZOrder() {
        return this.a;
    }

    public void setZOrder(int order) {
        this.a = order;
        if (this.a != order && this.f249a != null) {
            this.f249a.setZOrder(this, order);
        }
    }

    public CloudViewGala getCloudView() {
        return this.f249a;
    }

    public void setCloudView(CloudViewGala mCloudView) {
        this.f249a = mCloudView;
    }

    public boolean isUnbind() {
        return this.f249a == null;
    }

    public void invalidate() {
        if (this.f249a != null) {
            this.f249a.invalidate();
        }
    }

    protected void drawImageBylimit(Drawable drawable, Canvas canvas, int left, int top, int right, int bottom, int clipCanvas) {
        if (canvas != null && drawable != null) {
            if (!(drawable instanceof GifDrawable) || !((GifDrawable) drawable).isRecycled()) {
                if (clipCanvas == 1) {
                    int left2 = getCloudView().getNinePatchBorders().left + getCloudView().getPaddingLeft();
                    int top2 = getCloudView().getNinePatchBorders().top + getCloudView().getPaddingTop();
                    int right2 = (getCloudView().getItemWidth() - getCloudView().getNinePatchBorders().right) - getCloudView().getPaddingRight();
                    int bottom2 = (getCloudView().getItemHeight() - getCloudView().getNinePatchBorders().bottom) - getCloudView().getPaddingBottom();
                    if (left < left2) {
                        left = left2;
                    }
                    if (right > right2) {
                        right = right2;
                    }
                    if (top < top2) {
                        top = top2;
                    }
                    if (bottom > bottom2) {
                        bottom = bottom2;
                    }
                }
                drawable.setBounds(left, top, right, bottom);
                canvas.save();
                canvas.clipRect(left, top, right, bottom);
                drawable.draw(canvas);
                canvas.restore();
            }
        }
    }

    protected void clearDrawable(Drawable d) {
        if (d != null) {
            if (d instanceof GifDrawable) {
                ((GifDrawable) d).stop();
                ((GifDrawable) d).recycle();
            } else if (d instanceof AnimationDrawable) {
                ((AnimationDrawable) d).stop();
            }
            if (d.getCallback() != null) {
                d.setCallback(null);
            }
        }
    }

    public void suck(Cute cute) {
        this.f250a = cute.f250a;
        this.b = cute.b;
        this.a = cute.a;
        this.f249a = cute.f249a;
    }
}

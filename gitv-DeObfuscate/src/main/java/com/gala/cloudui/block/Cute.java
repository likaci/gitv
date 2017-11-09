package com.gala.cloudui.block;

import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import com.gala.cloudui.CloudViewGala;
import java.io.Serializable;
import pl.droidsonroids.gif.GifDrawable;

public abstract class Cute implements Serializable {
    private int f378a;
    private CloudViewGala f379a;
    private String f380a;
    private String f381b;

    public abstract void draw(Canvas canvas);

    public void setId(String id) {
        this.f380a = id;
    }

    public String getId() {
        return this.f380a;
    }

    public String getType() {
        return this.f381b;
    }

    public void setType(String mType) {
        this.f381b = mType;
    }

    public int getZOrder() {
        return this.f378a;
    }

    public void setZOrder(int order) {
        this.f378a = order;
        if (this.f378a != order && this.f379a != null) {
            this.f379a.setZOrder(this, order);
        }
    }

    public CloudViewGala getCloudView() {
        return this.f379a;
    }

    public void setCloudView(CloudViewGala mCloudView) {
        this.f379a = mCloudView;
    }

    public boolean isUnbind() {
        return this.f379a == null;
    }

    public void invalidate() {
        if (this.f379a != null) {
            this.f379a.invalidate();
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
        this.f380a = cute.f380a;
        this.f381b = cute.f381b;
        this.f378a = cute.f378a;
        this.f379a = cute.f379a;
    }
}

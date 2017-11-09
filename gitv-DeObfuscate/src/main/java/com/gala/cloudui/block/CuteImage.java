package com.gala.cloudui.block;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.gala.cloudui.imp.ICuteImage;
import com.gala.cloudui.utils.CloudUtilsGala;
import pl.droidsonroids.gif.GifDrawable;

public class CuteImage extends Cute implements ICuteImage {
    private int f389a;
    private Drawable f390a;
    private int f391b;
    private Drawable f392b;
    private int f393c;
    private Drawable f394c;
    private int f395d;
    private int f396e;
    private int f397f;
    private int f398g;
    private int f399h;
    private int f400i;
    private int f401j;
    private int f402k = 1;
    private int f403l = 1;
    private int f404m = 0;
    private int f405n = 0;
    private int f406o = 5;

    public Drawable getDefaultDrawable() {
        return this.f394c;
    }

    public void setDefaultDrawable(Drawable drawable) {
        this.f394c = drawable;
    }

    public Drawable getDrawable() {
        return this.f390a;
    }

    public void setDrawable(Drawable drawable) {
        if (this.f390a != drawable) {
            clearDrawable(this.f390a);
            this.f390a = drawable;
            m250a(this.f390a);
            invalidate();
        }
    }

    public void setBitmap(Bitmap bitmap) {
        clearDrawable(this.f390a);
        this.f390a = null;
        if (bitmap != null) {
            setDrawable(new BitmapDrawable(CloudUtilsGala.getResource(), bitmap));
        } else {
            invalidate();
        }
    }

    public void setResourceId(int resId) {
        clearDrawable(this.f390a);
        this.f390a = null;
        if (resId > 0) {
            setDrawable(m249a(resId));
        } else {
            invalidate();
        }
    }

    public Drawable getFocusDrawable() {
        return this.f392b;
    }

    public void setFocusDrawable(Drawable drawable) {
        if (this.f392b != drawable) {
            clearDrawable(this.f392b);
            this.f392b = drawable;
            m250a(this.f392b);
            invalidate();
        }
    }

    public void setFocusBitmap(Bitmap bitmap) {
        clearDrawable(this.f392b);
        this.f392b = null;
        if (bitmap != null) {
            setFocusDrawable(new BitmapDrawable(CloudUtilsGala.getResource(), bitmap));
        } else {
            invalidate();
        }
    }

    public void setFocusResourceId(int resId) {
        clearDrawable(this.f392b);
        this.f392b = null;
        if (resId > 0) {
            setFocusDrawable(m249a(resId));
        } else {
            invalidate();
        }
    }

    private void m250a(Drawable drawable) {
        if (drawable != null) {
            if (drawable instanceof GifDrawable) {
                if (drawable.getCallback() != getCloudView()) {
                    drawable.setCallback(getCloudView());
                }
            } else if ((drawable instanceof AnimationDrawable) && drawable.getCallback() != getCloudView()) {
                drawable.setCallback(getCloudView());
            }
        }
    }

    private Drawable m249a(int i) {
        Resources resource = CloudUtilsGala.getResource();
        try {
            return new GifDrawable(resource, i);
        } catch (Exception e) {
            try {
                return resource.getDrawable(i);
            } catch (Exception e2) {
                return null;
            }
        }
    }

    public int getVisible() {
        return this.f402k;
    }

    public void setVisible(int visible) {
        if (this.f402k != visible) {
            this.f402k = visible;
            invalidate();
        }
    }

    public int getWidth() {
        return this.f389a;
    }

    public void setWidth(int width) {
        if (this.f389a != width) {
            this.f389a = width;
            invalidate();
        }
    }

    public int getHeight() {
        return this.f391b;
    }

    public void setHeight(int height) {
        if (this.f391b != height) {
            this.f391b = height;
            invalidate();
        }
    }

    public int getMarginLeft() {
        return this.f398g;
    }

    public void setMarginLeft(int leftMargin) {
        if (this.f398g != leftMargin) {
            this.f398g = leftMargin;
            invalidate();
        }
    }

    public int getMarginTop() {
        return this.f399h;
    }

    public void setMarginTop(int topMargin) {
        if (this.f399h != topMargin) {
            this.f399h = topMargin;
            invalidate();
        }
    }

    public int getMarginRight() {
        return this.f400i;
    }

    public void setMarginRight(int rightMargin) {
        if (this.f400i != rightMargin) {
            this.f400i = rightMargin;
            invalidate();
        }
    }

    public int getMarginBottom() {
        return this.f401j;
    }

    public void setMarginBottom(int bottomMargin) {
        if (this.f401j != bottomMargin) {
            this.f401j = bottomMargin;
            invalidate();
        }
    }

    public int getPaddingLeft() {
        return this.f393c;
    }

    public void setPaddingLeft(int paddingLeft) {
        if (this.f393c != paddingLeft) {
            this.f393c = paddingLeft;
            invalidate();
        }
    }

    public int getPaddingTop() {
        return this.f395d;
    }

    public void setPaddingTop(int paddingTop) {
        if (this.f395d != paddingTop) {
            this.f395d = paddingTop;
            invalidate();
        }
    }

    public int getPaddingRight() {
        return this.f396e;
    }

    public void setPaddingRight(int paddingRight) {
        if (this.f396e != paddingRight) {
            this.f396e = paddingRight;
            invalidate();
        }
    }

    public int getPaddingBottom() {
        return this.f397f;
    }

    public void setPaddingBottom(int paddingBottom) {
        if (this.f397f != paddingBottom) {
            this.f397f = paddingBottom;
            invalidate();
        }
    }

    public void setClipPadding(int clipPadding) {
        if (this.f403l != clipPadding) {
            this.f403l = clipPadding;
            invalidate();
        }
    }

    public int getClipPadding() {
        return this.f403l;
    }

    public int getClipType() {
        return this.f404m;
    }

    public void setClipType(int clipType) {
        this.f404m = clipType;
    }

    public int getScaleType() {
        return this.f405n;
    }

    public void setScaleType(int scaleType) {
        if (this.f405n != scaleType) {
            this.f405n = scaleType;
            invalidate();
        }
    }

    public int getGravity() {
        return this.f406o;
    }

    public void setGravity(int gravity) {
        if (this.f406o != gravity) {
            this.f406o = gravity;
            invalidate();
        }
    }

    public void draw(Canvas canvas) {
        int i = 0;
        if (this.f402k == 1) {
            Drawable drawable = this.f392b;
            Drawable drawable2 = this.f390a;
            if (!getCloudView().isFocused() || drawable == null) {
                drawable = drawable2;
            }
            if (drawable != null) {
                int i2;
                int i3;
                int i4;
                int i5 = this.f389a;
                int i6 = this.f391b;
                if (this.f403l == 1) {
                    i2 = getCloudView().getNinePatchBorders().left;
                    i3 = getCloudView().getNinePatchBorders().top;
                    i4 = getCloudView().getNinePatchBorders().bottom;
                    i = getCloudView().getNinePatchBorders().right;
                } else {
                    i4 = 0;
                    i3 = 0;
                    i2 = 0;
                }
                if (this.f405n == 1) {
                    if (i5 == 0) {
                        i5 = (getCloudView().getItemWidth() - i2) - i;
                    }
                    if (i6 == 0) {
                        i6 = (getCloudView().getItemHeight() - i3) - i4;
                    }
                    i5 = (i5 - this.f393c) - this.f396e;
                    i6 = (i6 - this.f395d) - this.f397f;
                    if (this.f406o != 1) {
                        if (this.f406o == 4) {
                            i3 = (getCloudView().getItemHeight() / 2) - (i6 / 2);
                        } else if (this.f406o == 7) {
                            i3 = (getCloudView().getItemHeight() - i4) - i6;
                        } else if (this.f406o == 3) {
                            i2 = (getCloudView().getItemWidth() - i) - i5;
                        } else if (this.f406o == 6) {
                            i2 = (getCloudView().getItemWidth() - i) - i5;
                            i3 = (getCloudView().getItemHeight() / 2) - (i6 / 2);
                        } else if (this.f406o == 9) {
                            i2 = (getCloudView().getItemWidth() - i) - i5;
                            i3 = (getCloudView().getItemHeight() - i4) - i6;
                        } else if (this.f406o == 2) {
                            i2 = (getCloudView().getItemWidth() / 2) - (i5 / 2);
                        } else if (this.f406o == 8) {
                            i2 = (getCloudView().getItemWidth() / 2) - (i5 / 2);
                            i3 = (getCloudView().getItemHeight() - i4) - i6;
                        } else {
                            i2 = (getCloudView().getItemWidth() / 2) - (i5 / 2);
                            i3 = (getCloudView().getItemHeight() / 2) - (i6 / 2);
                        }
                    }
                    i = (this.f398g + i2) - this.f400i;
                    i2 = (this.f399h + i3) - this.f401j;
                    i4 = i5;
                    i3 = i;
                    i = i6;
                } else {
                    if (i5 == 0) {
                        i = (getCloudView().getItemWidth() - i2) - i;
                    } else {
                        i = i5;
                    }
                    if (i6 == 0) {
                        i6 = (getCloudView().getItemHeight() - i3) - i4;
                    }
                    i4 = (i - this.f393c) - this.f396e;
                    i = (i6 - this.f395d) - this.f397f;
                    i6 = this.f393c + i2;
                    i2 = this.f395d + i3;
                    i3 = i6;
                }
                drawImageBylimit(drawable, canvas, i3, i2, i3 + i4, i2 + i, this.f403l);
            }
        }
    }

    public void suck(Cute cute) {
        super.suck(cute);
        CuteImage cuteImage = (CuteImage) cute;
        this.f389a = cuteImage.f389a;
        this.f391b = cuteImage.f391b;
        this.f393c = cuteImage.f393c;
        this.f395d = cuteImage.f395d;
        this.f396e = cuteImage.f396e;
        this.f397f = cuteImage.f397f;
        this.f398g = cuteImage.f398g;
        this.f399h = cuteImage.f399h;
        this.f400i = cuteImage.f400i;
        this.f401j = cuteImage.f401j;
        this.f402k = cuteImage.f402k;
        this.f403l = cuteImage.f403l;
        this.f404m = cuteImage.f404m;
        this.f405n = cuteImage.f405n;
        this.f406o = cuteImage.f406o;
        clearDrawable(this.f390a);
        this.f390a = cuteImage.f390a;
        clearDrawable(this.f392b);
        this.f392b = cuteImage.f392b;
        clearDrawable(this.f394c);
        this.f394c = cuteImage.f394c;
    }
}

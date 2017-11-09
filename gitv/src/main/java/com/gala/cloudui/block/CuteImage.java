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
    private int a;
    private Drawable f273a;
    private int b;
    private Drawable f274b;
    private int c;
    private Drawable f275c;
    private int d;
    private int e;
    private int f;
    private int g;
    private int h;
    private int i;
    private int j;
    private int k = 1;
    private int l = 1;
    private int m = 0;
    private int n = 0;
    private int o = 5;

    public Drawable getDefaultDrawable() {
        return this.f275c;
    }

    public void setDefaultDrawable(Drawable drawable) {
        this.f275c = drawable;
    }

    public Drawable getDrawable() {
        return this.f273a;
    }

    public void setDrawable(Drawable drawable) {
        if (this.f273a != drawable) {
            clearDrawable(this.f273a);
            this.f273a = drawable;
            a(this.f273a);
            invalidate();
        }
    }

    public void setBitmap(Bitmap bitmap) {
        clearDrawable(this.f273a);
        this.f273a = null;
        if (bitmap != null) {
            setDrawable(new BitmapDrawable(CloudUtilsGala.getResource(), bitmap));
        } else {
            invalidate();
        }
    }

    public void setResourceId(int resId) {
        clearDrawable(this.f273a);
        this.f273a = null;
        if (resId > 0) {
            setDrawable(a(resId));
        } else {
            invalidate();
        }
    }

    public Drawable getFocusDrawable() {
        return this.f274b;
    }

    public void setFocusDrawable(Drawable drawable) {
        if (this.f274b != drawable) {
            clearDrawable(this.f274b);
            this.f274b = drawable;
            a(this.f274b);
            invalidate();
        }
    }

    public void setFocusBitmap(Bitmap bitmap) {
        clearDrawable(this.f274b);
        this.f274b = null;
        if (bitmap != null) {
            setFocusDrawable(new BitmapDrawable(CloudUtilsGala.getResource(), bitmap));
        } else {
            invalidate();
        }
    }

    public void setFocusResourceId(int resId) {
        clearDrawable(this.f274b);
        this.f274b = null;
        if (resId > 0) {
            setFocusDrawable(a(resId));
        } else {
            invalidate();
        }
    }

    private void a(Drawable drawable) {
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

    private Drawable a(int i) {
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
        return this.k;
    }

    public void setVisible(int visible) {
        if (this.k != visible) {
            this.k = visible;
            invalidate();
        }
    }

    public int getWidth() {
        return this.a;
    }

    public void setWidth(int width) {
        if (this.a != width) {
            this.a = width;
            invalidate();
        }
    }

    public int getHeight() {
        return this.b;
    }

    public void setHeight(int height) {
        if (this.b != height) {
            this.b = height;
            invalidate();
        }
    }

    public int getMarginLeft() {
        return this.g;
    }

    public void setMarginLeft(int leftMargin) {
        if (this.g != leftMargin) {
            this.g = leftMargin;
            invalidate();
        }
    }

    public int getMarginTop() {
        return this.h;
    }

    public void setMarginTop(int topMargin) {
        if (this.h != topMargin) {
            this.h = topMargin;
            invalidate();
        }
    }

    public int getMarginRight() {
        return this.i;
    }

    public void setMarginRight(int rightMargin) {
        if (this.i != rightMargin) {
            this.i = rightMargin;
            invalidate();
        }
    }

    public int getMarginBottom() {
        return this.j;
    }

    public void setMarginBottom(int bottomMargin) {
        if (this.j != bottomMargin) {
            this.j = bottomMargin;
            invalidate();
        }
    }

    public int getPaddingLeft() {
        return this.c;
    }

    public void setPaddingLeft(int paddingLeft) {
        if (this.c != paddingLeft) {
            this.c = paddingLeft;
            invalidate();
        }
    }

    public int getPaddingTop() {
        return this.d;
    }

    public void setPaddingTop(int paddingTop) {
        if (this.d != paddingTop) {
            this.d = paddingTop;
            invalidate();
        }
    }

    public int getPaddingRight() {
        return this.e;
    }

    public void setPaddingRight(int paddingRight) {
        if (this.e != paddingRight) {
            this.e = paddingRight;
            invalidate();
        }
    }

    public int getPaddingBottom() {
        return this.f;
    }

    public void setPaddingBottom(int paddingBottom) {
        if (this.f != paddingBottom) {
            this.f = paddingBottom;
            invalidate();
        }
    }

    public void setClipPadding(int clipPadding) {
        if (this.l != clipPadding) {
            this.l = clipPadding;
            invalidate();
        }
    }

    public int getClipPadding() {
        return this.l;
    }

    public int getClipType() {
        return this.m;
    }

    public void setClipType(int clipType) {
        this.m = clipType;
    }

    public int getScaleType() {
        return this.n;
    }

    public void setScaleType(int scaleType) {
        if (this.n != scaleType) {
            this.n = scaleType;
            invalidate();
        }
    }

    public int getGravity() {
        return this.o;
    }

    public void setGravity(int gravity) {
        if (this.o != gravity) {
            this.o = gravity;
            invalidate();
        }
    }

    public void draw(Canvas canvas) {
        int i = 0;
        if (this.k == 1) {
            Drawable drawable = this.f274b;
            Drawable drawable2 = this.f273a;
            if (!getCloudView().isFocused() || drawable == null) {
                drawable = drawable2;
            }
            if (drawable != null) {
                int i2;
                int i3;
                int i4;
                int i5 = this.a;
                int i6 = this.b;
                if (this.l == 1) {
                    i2 = getCloudView().getNinePatchBorders().left;
                    i3 = getCloudView().getNinePatchBorders().top;
                    i4 = getCloudView().getNinePatchBorders().bottom;
                    i = getCloudView().getNinePatchBorders().right;
                } else {
                    i4 = 0;
                    i3 = 0;
                    i2 = 0;
                }
                if (this.n == 1) {
                    if (i5 == 0) {
                        i5 = (getCloudView().getItemWidth() - i2) - i;
                    }
                    if (i6 == 0) {
                        i6 = (getCloudView().getItemHeight() - i3) - i4;
                    }
                    i5 = (i5 - this.c) - this.e;
                    i6 = (i6 - this.d) - this.f;
                    if (this.o != 1) {
                        if (this.o == 4) {
                            i3 = (getCloudView().getItemHeight() / 2) - (i6 / 2);
                        } else if (this.o == 7) {
                            i3 = (getCloudView().getItemHeight() - i4) - i6;
                        } else if (this.o == 3) {
                            i2 = (getCloudView().getItemWidth() - i) - i5;
                        } else if (this.o == 6) {
                            i2 = (getCloudView().getItemWidth() - i) - i5;
                            i3 = (getCloudView().getItemHeight() / 2) - (i6 / 2);
                        } else if (this.o == 9) {
                            i2 = (getCloudView().getItemWidth() - i) - i5;
                            i3 = (getCloudView().getItemHeight() - i4) - i6;
                        } else if (this.o == 2) {
                            i2 = (getCloudView().getItemWidth() / 2) - (i5 / 2);
                        } else if (this.o == 8) {
                            i2 = (getCloudView().getItemWidth() / 2) - (i5 / 2);
                            i3 = (getCloudView().getItemHeight() - i4) - i6;
                        } else {
                            i2 = (getCloudView().getItemWidth() / 2) - (i5 / 2);
                            i3 = (getCloudView().getItemHeight() / 2) - (i6 / 2);
                        }
                    }
                    i = (this.g + i2) - this.i;
                    i2 = (this.h + i3) - this.j;
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
                    i4 = (i - this.c) - this.e;
                    i = (i6 - this.d) - this.f;
                    i6 = this.c + i2;
                    i2 = this.d + i3;
                    i3 = i6;
                }
                drawImageBylimit(drawable, canvas, i3, i2, i3 + i4, i2 + i, this.l);
            }
        }
    }

    public void suck(Cute cute) {
        super.suck(cute);
        CuteImage cuteImage = (CuteImage) cute;
        this.a = cuteImage.a;
        this.b = cuteImage.b;
        this.c = cuteImage.c;
        this.d = cuteImage.d;
        this.e = cuteImage.e;
        this.f = cuteImage.f;
        this.g = cuteImage.g;
        this.h = cuteImage.h;
        this.i = cuteImage.i;
        this.j = cuteImage.j;
        this.k = cuteImage.k;
        this.l = cuteImage.l;
        this.m = cuteImage.m;
        this.n = cuteImage.n;
        this.o = cuteImage.o;
        clearDrawable(this.f273a);
        this.f273a = cuteImage.f273a;
        clearDrawable(this.f274b);
        this.f274b = cuteImage.f274b;
        clearDrawable(this.f275c);
        this.f275c = cuteImage.f275c;
    }
}

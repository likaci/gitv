package com.gala.video.albumlist.widget;

import android.graphics.Canvas;

public abstract class C0406a {
    private int f1509a;
    private int f1510b;
    private int f1511c;
    private int f1512d;
    private int f1513e;
    private int f1514f;

    public abstract void draw(Canvas canvas);

    public void setHeight(int height) {
        this.f1509a = height;
    }

    public int getHeight() {
        return this.f1509a;
    }

    public void setWidth(int width) {
        this.f1510b = width;
    }

    public int getWidth() {
        return this.f1510b;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        this.f1511c = left;
        this.f1512d = top;
        this.f1513e = right;
        this.f1514f = bottom;
    }

    public int getPaddingLeft() {
        return this.f1511c;
    }

    public int getPaddingTop() {
        return this.f1512d;
    }

    public int getPaddingRight() {
        return this.f1513e;
    }

    public int getPaddingBottom() {
        return this.f1514f;
    }
}

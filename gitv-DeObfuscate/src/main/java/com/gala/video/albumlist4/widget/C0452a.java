package com.gala.video.albumlist4.widget;

import android.graphics.Canvas;

public abstract class C0452a {
    private int f1735a;
    private int f1736b;
    private int f1737c;
    private int f1738d;
    private int f1739e;
    private int f1740f;

    public abstract void draw(Canvas canvas);

    public void setHeight(int height) {
        this.f1735a = height;
    }

    public int getHeight() {
        return this.f1735a;
    }

    public void setWidth(int width) {
        this.f1736b = width;
    }

    public int getWidth() {
        return this.f1736b;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        this.f1737c = left;
        this.f1738d = top;
        this.f1739e = right;
        this.f1740f = bottom;
    }

    public int getPaddingLeft() {
        return this.f1737c;
    }

    public int getPaddingTop() {
        return this.f1738d;
    }

    public int getPaddingRight() {
        return this.f1739e;
    }

    public int getPaddingBottom() {
        return this.f1740f;
    }
}

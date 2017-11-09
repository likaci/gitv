package com.gala.video.albumlist4.widget;

import android.graphics.Canvas;

public abstract class a {
    private int a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;

    public abstract void draw(Canvas canvas);

    public void setHeight(int height) {
        this.a = height;
    }

    public int getHeight() {
        return this.a;
    }

    public void setWidth(int width) {
        this.b = width;
    }

    public int getWidth() {
        return this.b;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        this.c = left;
        this.d = top;
        this.e = right;
        this.f = bottom;
    }

    public int getPaddingLeft() {
        return this.c;
    }

    public int getPaddingTop() {
        return this.d;
    }

    public int getPaddingRight() {
        return this.e;
    }

    public int getPaddingBottom() {
        return this.f;
    }
}

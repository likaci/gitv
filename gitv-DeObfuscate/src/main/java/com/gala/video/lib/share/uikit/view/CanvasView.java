package com.gala.video.lib.share.uikit.view;

import android.graphics.Canvas;

public abstract class CanvasView {
    private int mHeight;
    private int mMarginBottom;
    private int mMarginLeft;
    private int mMarginRight;
    private int mMarginTop;
    private int mPaddingBottom;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mWidth;

    public abstract void draw(Canvas canvas);

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        this.mPaddingLeft = left;
        this.mPaddingTop = top;
        this.mPaddingRight = right;
        this.mPaddingBottom = bottom;
    }

    public int getPaddingLeft() {
        return this.mPaddingLeft;
    }

    public int getPaddingTop() {
        return this.mPaddingTop;
    }

    public int getPaddingRight() {
        return this.mPaddingRight;
    }

    public int getPaddingBottom() {
        return this.mPaddingBottom;
    }

    public void setMargin(int left, int top, int right, int bottom) {
        this.mMarginLeft = left;
        this.mMarginTop = top;
        this.mMarginRight = right;
        this.mMarginBottom = bottom;
    }

    public int getMarginLeft() {
        return this.mMarginLeft;
    }

    public int getMarginTop() {
        return this.mMarginTop;
    }

    public int getMarginRight() {
        return this.mMarginRight;
    }

    public int getMarginBottom() {
        return this.mMarginBottom;
    }
}

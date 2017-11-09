package com.gala.cloudui.imp;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public interface ICuteImage {
    int getClipPadding();

    int getClipType();

    Drawable getDefaultDrawable();

    Drawable getDrawable();

    Drawable getFocusDrawable();

    int getGravity();

    int getHeight();

    int getMarginBottom();

    int getMarginLeft();

    int getMarginRight();

    int getMarginTop();

    int getPaddingBottom();

    int getPaddingLeft();

    int getPaddingRight();

    int getPaddingTop();

    int getScaleType();

    int getVisible();

    int getWidth();

    void setBitmap(Bitmap bitmap);

    void setClipPadding(int i);

    void setClipType(int i);

    void setDefaultDrawable(Drawable drawable);

    void setDrawable(Drawable drawable);

    void setFocusBitmap(Bitmap bitmap);

    void setFocusDrawable(Drawable drawable);

    void setFocusResourceId(int i);

    void setGravity(int i);

    void setHeight(int i);

    void setMarginBottom(int i);

    void setMarginLeft(int i);

    void setMarginRight(int i);

    void setMarginTop(int i);

    void setPaddingBottom(int i);

    void setPaddingLeft(int i);

    void setPaddingRight(int i);

    void setPaddingTop(int i);

    void setResourceId(int i);

    void setScaleType(int i);

    void setVisible(int i);

    void setWidth(int i);
}

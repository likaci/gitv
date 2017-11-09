package com.gala.video.cloudui.view.impl;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.gala.video.cloudui.Gravity4CuteImage;
import com.gala.video.cloudui.ScaleType4CuteImage;

public interface IImage {
    Drawable getDrawable();

    Gravity4CuteImage getGravity();

    int getHeight();

    int getMarginBottom();

    int getMarginLeft();

    int getMarginRight();

    int getMarginTop();

    ScaleType4CuteImage getScaleType();

    Object getTag();

    int getVisible();

    int getWidth();

    void setBitmap(Bitmap bitmap);

    void setDrawable(Drawable drawable);

    void setGravity(Gravity4CuteImage gravity4CuteImage);

    void setHeight(int i);

    void setMarginBottom(int i);

    void setMarginLeft(int i);

    void setMarginRight(int i);

    void setMarginTop(int i);

    void setResourceId(int i);

    void setScaleType(ScaleType4CuteImage scaleType4CuteImage);

    void setTag(Object obj);

    void setVisible(int i);

    void setWidth(int i);
}

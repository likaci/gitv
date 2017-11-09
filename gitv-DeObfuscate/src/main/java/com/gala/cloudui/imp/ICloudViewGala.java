package com.gala.cloudui.imp;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.gala.cloudui.block.Cute;
import com.gala.cloudui.block.CuteBg;
import com.gala.cloudui.block.CuteImage;
import com.gala.cloudui.block.CuteText;

public interface ICloudViewGala {
    Drawable getBackground();

    Cute getChildAt(String str);

    int getContentHeight();

    int getContentWidth();

    CuteBg getCuteBg();

    CuteImage getCuteImage(String str);

    CuteText getCuteText(String str);

    int getItemHeight();

    int getItemWidth();

    Rect getNinePatchBorders();

    void recycle();

    void setBackground(Drawable drawable);

    void setBgPaddingBottom(int i);

    void setBgPaddingLeft(int i);

    void setBgPaddingRight(int i);

    void setBgPaddingTop(int i);

    void setDrawable(String str, Drawable drawable);

    void setPadding(int i, int i2, int i3, int i4);

    void setStyleByName(String str);

    void setText(String str, String str2);
}

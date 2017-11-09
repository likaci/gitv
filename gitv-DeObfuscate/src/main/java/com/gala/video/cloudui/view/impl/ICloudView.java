package com.gala.video.cloudui.view.impl;

import android.graphics.drawable.Drawable;
import com.gala.video.cloudui.CuteImageView;
import com.gala.video.cloudui.CuteTextView;
import com.gala.video.cloudui.CuteView;

public interface ICloudView {
    int getBgPaddingBottom();

    int getBgPaddingLeft();

    int getBgPaddingRight();

    int getBgPaddingTop();

    CuteView getChildAt(String str);

    int getContentHeight();

    int getContentWidth();

    CuteImageView getImageView(String str);

    int getItemHeight();

    int getItemWidth();

    int getNinePatchBorder();

    String getStyle();

    String getStyleStream();

    CuteTextView getTextView(String str);

    void setBgPaddingBottom(int i);

    void setBgPaddingLeft(int i);

    void setBgPaddingRight(int i);

    void setBgPaddingTop(int i);

    void setItemBackground(Drawable drawable);

    void setOrder(int i);

    void setStyle(String str);

    void setStyleStream(String str);
}

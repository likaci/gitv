package com.gala.cloudui.imp;

import android.graphics.drawable.Drawable;

public interface ICuteBg {
    Drawable getBackgroundDrawable();

    int getPaddingBottom();

    int getPaddingLeft();

    int getPaddingRight();

    int getPaddingTop();

    void setBackgroundDrawable(Drawable drawable);

    void setPaddingBottom(int i);

    void setPaddingLeft(int i);

    void setPaddingRight(int i);

    void setPaddingTop(int i);
}

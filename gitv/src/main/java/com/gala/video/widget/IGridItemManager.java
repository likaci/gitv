package com.gala.video.widget;

import android.graphics.Bitmap;

public interface IGridItemManager {
    public static final int ALPHA_ANIMATION = 1;
    public static final int ANIMATION_DURATION = 1000;
    public static final int BRIGHT_LEVEL = 30;
    public static final int DIM_LEVEL = -15;
    public static final int IMAGE_HEIGHT = 294;
    public static final int IMAGE_WIDTH = 234;
    public static final int NONE_ANIMATION = 0;
    public static final int TEXT_HEIGHT = 60;
    public static final int TEXT_OFFSET = -25;
    public static final int TEXT_WIDTH = 160;

    void setBackgroundResource(int i);

    void setImageBitmap(Bitmap bitmap);

    void setImageResource(int i, int i2);

    void setText(CharSequence charSequence);

    void setTextColor(int i);

    void setTextSize(float f);
}

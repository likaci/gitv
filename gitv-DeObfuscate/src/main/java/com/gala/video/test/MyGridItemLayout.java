package com.gala.video.test;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.gala.video.widget.IGridItemManager;
import com.gala.video.widget.view.GridItemLayout;

public class MyGridItemLayout extends GridItemLayout {
    public MyGridItemLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyGridItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridItemLayout(Context context) {
        super(context);
    }

    public int getImageWidth() {
        return IGridItemManager.IMAGE_WIDTH;
    }

    public int getImageHeight() {
        return 294;
    }

    public int getTextWidth() {
        return 160;
    }

    public int getTextOffset() {
        return -25;
    }

    public float getDimLevel() {
        return -15.0f;
    }

    public float getBrightLevel() {
        return 30.0f;
    }

    public boolean isSetImageBright() {
        return true;
    }

    public int getTextHeight() {
        return 60;
    }

    public Drawable getFocusBg() {
        return null;
    }

    public Drawable getNormalBg() {
        return null;
    }
}

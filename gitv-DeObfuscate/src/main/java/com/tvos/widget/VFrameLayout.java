package com.tvos.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class VFrameLayout extends FrameLayout {
    public VFrameLayout(Context context) {
        super(context);
    }

    public VFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void dispatchDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        super.dispatchDraw(canvas);
    }
}

package com.tvos.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;

public class VGridLayout extends GridLayout {
    private int mIndex = 0;

    public VGridLayout(Context context) {
        super(context);
        setChildrenDrawingOrderEnabled(true);
    }

    public VGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
    }

    public VGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setChildrenDrawingOrderEnabled(true);
    }

    public void setOnTop(View v) {
        this.mIndex = indexOfChild(v);
    }

    protected int getChildDrawingOrder(int childCount, int i) {
        if (this.mIndex >= childCount) {
            return super.getChildDrawingOrder(childCount, i);
        }
        if (i == this.mIndex) {
            return childCount - 1;
        }
        if (i == childCount - 1) {
            return this.mIndex;
        }
        return i;
    }

    protected void dispatchDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        super.dispatchDraw(canvas);
    }
}

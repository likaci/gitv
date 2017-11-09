package com.gala.video.albumlist4.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomTextView extends TextView {
    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(ViewGroup.getChildMeasureSpec(MeasureSpec.makeMeasureSpec(0, 0), 0, -2), ViewGroup.getChildMeasureSpec(MeasureSpec.makeMeasureSpec(0, 0), 0, -2));
    }
}

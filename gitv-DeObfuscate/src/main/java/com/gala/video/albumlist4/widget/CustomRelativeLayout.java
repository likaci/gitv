package com.gala.video.albumlist4.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.gala.video.albumlist4.widget.C0465c.C0463a;

public class CustomRelativeLayout extends RelativeLayout {
    public CustomRelativeLayout(Context context) {
        super(context);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public View focusSearch(View focused, int direction) {
        return C0465c.m1401a().m1411a((ViewGroup) this, focused, direction, C0463a.LEFT);
    }
}

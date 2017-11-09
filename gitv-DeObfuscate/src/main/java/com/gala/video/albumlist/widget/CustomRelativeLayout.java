package com.gala.video.albumlist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.gala.video.albumlist.widget.C0420c.C0418a;

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
        return C0420c.m1080a().m1092a((ViewGroup) this, focused, direction, C0418a.LEFT);
    }
}

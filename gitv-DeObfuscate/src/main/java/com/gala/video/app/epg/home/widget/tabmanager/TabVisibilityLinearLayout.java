package com.gala.video.app.epg.home.widget.tabmanager;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.LinearLayout;

public class TabVisibilityLinearLayout extends LinearLayout {
    private boolean clipChildren = true;
    private boolean compatibleMode = false;
    private int mHeight;

    public TabVisibilityLinearLayout(Context context) {
        super(context);
        init();
    }

    public TabVisibilityLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setClipChildren(boolean clipChildren) {
        super.setClipChildren(clipChildren);
        this.clipChildren = clipChildren;
    }

    private void init() {
        this.compatibleMode = VERSION.SDK_INT < 19;
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mHeight = b - t;
    }

    public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
        if (this.compatibleMode && !this.clipChildren) {
            dirty.offset(location[0] - getScrollX(), location[1] - getScrollY());
            dirty.union(0, 0, getWidth(), this.mHeight);
            dirty.offset(getScrollX() - location[0], getScrollY() - location[1]);
        }
        return super.invalidateChildInParent(location, dirty);
    }
}

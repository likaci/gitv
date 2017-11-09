package com.gala.video.app.epg.home.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.View;
import android.widget.FrameLayout;

public class RootFrameLayout extends FrameLayout {
    private boolean isRoot = true;

    public RootFrameLayout(Context context) {
        super(context);
    }

    public RootFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RootFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isRoot() {
        return this.isRoot;
    }

    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public View focusSearch(View focused, int direction) {
        if (this.isRoot) {
            return FocusFinder.getInstance().findNextFocus(this, focused, direction);
        }
        return super.focusSearch(focused, direction);
    }
}

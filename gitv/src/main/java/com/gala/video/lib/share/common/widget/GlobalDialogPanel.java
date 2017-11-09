package com.gala.video.lib.share.common.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class GlobalDialogPanel extends LinearLayout {
    private static final String TAG = "GlobalDialogPanel";
    private View mForceRequestView;

    public GlobalDialogPanel(Context context) {
        super(context);
    }

    public GlobalDialogPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GlobalDialogPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public GlobalDialogPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setForceRequestView(View view) {
        LogUtils.d(TAG, "--- setForceRequestView");
        this.mForceRequestView = view;
    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        LogUtils.d(TAG, "--- onRequestFocusInDescendants");
        if (this.mForceRequestView == null || this.mForceRequestView.getVisibility() != 0) {
            return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
        }
        this.mForceRequestView.setFocusable(true);
        this.mForceRequestView.setFocusableInTouchMode(true);
        this.mForceRequestView.requestFocus();
        this.mForceRequestView = null;
        return true;
    }
}

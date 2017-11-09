package com.tvos.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

public class VMenuItem extends FrameLayout {
    private static final String TAG = "VMenuItem";
    private OnMenuItemKeyListener mListener;

    public interface OnMenuItemKeyListener {
        void onMenuItemKeyCenter(VMenuItem vMenuItem);

        void onMenuItemKeyDown(VMenuItem vMenuItem);

        void onMenuItemKeyLeft(VMenuItem vMenuItem);

        void onMenuItemKeyRight(VMenuItem vMenuItem);

        void onMenuItemKeyUp(VMenuItem vMenuItem);
    }

    public VMenuItem(Context context) {
        this(context, null);
    }

    public VMenuItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMenuItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mListener = null;
        setClipChildren(false);
        setFocusable(true);
    }

    public void setOnMenuItemKeyListener(OnMenuItemKeyListener l) {
        this.mListener = l;
    }

    public void setView(View v) {
        if (getChildCount() > 0) {
            removeAllViews();
        }
        addView(v);
    }

    public View getView() {
        if (getChildCount() > 0) {
            return getChildAt(0);
        }
        return null;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 1) {
            return super.dispatchKeyEvent(event);
        }
        if (this.mListener == null) {
            return super.dispatchKeyEvent(event);
        }
        int keyCode = event.getKeyCode();
        if (keyCode == 19) {
            this.mListener.onMenuItemKeyUp(this);
        } else if (keyCode == 20) {
            this.mListener.onMenuItemKeyDown(this);
        } else if (keyCode == 21) {
            this.mListener.onMenuItemKeyLeft(this);
        } else if (keyCode == 22) {
            this.mListener.onMenuItemKeyRight(this);
        } else if (keyCode == 66 || keyCode == 23) {
            this.mListener.onMenuItemKeyCenter(this);
        }
        return super.dispatchKeyEvent(event);
    }
}

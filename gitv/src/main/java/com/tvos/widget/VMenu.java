package com.tvos.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;

public class VMenu implements OnFocusChangeListener {
    private static final long DEFAULT_DURATION = 250;
    private static final float DEFAULT_SCALE_RATIO = 1.1f;
    private static final String TAG = "VMenu";
    private Context mContext = null;
    private long mDuration = DEFAULT_DURATION;
    private boolean mIsShow = false;
    private OnMenuItemFocusChangeListener mListener = null;
    private OrderLinearLayout mMenuContainer = null;
    private FrameLayout mRootView = null;
    private float mScaleRatio = 1.1f;
    private WindowManager mWm = null;

    private class AntiAliasFrameLayout extends VFrameLayout {
        public AntiAliasFrameLayout(Context context) {
            super(context);
        }

        public AntiAliasFrameLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public AntiAliasFrameLayout(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public boolean dispatchKeyEvent(KeyEvent event) {
            if (event.getAction() == 0 && event.getKeyCode() == 4 && event.getRepeatCount() == 0) {
                VMenu.this.dismiss();
            }
            return super.dispatchKeyEvent(event);
        }
    }

    public interface OnMenuItemFocusChangeListener {
        void onMenuItemFocusChange(View view, int i, boolean z);
    }

    private class OrderLinearLayout extends LinearLayout {
        private int mIndex = 0;

        public OrderLinearLayout(Context context) {
            super(context);
            setChildrenDrawingOrderEnabled(true);
        }

        public OrderLinearLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            setChildrenDrawingOrderEnabled(true);
        }

        public OrderLinearLayout(Context context, AttributeSet attrs, int defStyle) {
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
    }

    public VMenu(Context context) {
        this.mContext = context.getApplicationContext();
        this.mWm = (WindowManager) this.mContext.getSystemService("window");
        initWidget();
    }

    private void initWidget() {
        this.mRootView = new AntiAliasFrameLayout(this.mContext);
        this.mRootView.setClipChildren(false);
        LayoutParams lp = new LayoutParams(-2, -2);
        lp.gravity = 17;
        this.mMenuContainer = new OrderLinearLayout(this.mContext);
        this.mMenuContainer.setLayoutParams(lp);
        this.mMenuContainer.setClipChildren(false);
        this.mRootView.addView(this.mMenuContainer);
    }

    public void setBackground(Drawable background) {
        this.mRootView.setBackground(background);
    }

    public void setBackgroundResource(int resid) {
        this.mRootView.setBackgroundResource(resid);
    }

    public void setBackgroundColor(int color) {
        this.mRootView.setBackgroundColor(color);
    }

    public void setScaleRatio(float scaleRatio) {
        this.mScaleRatio = scaleRatio;
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    public void setOnMenuItemFocusChangeListener(OnMenuItemFocusChangeListener l) {
        this.mListener = l;
    }

    public void addMenuItem(View menuItem) {
        this.mMenuContainer.addView(menuItem);
        menuItem.setFocusable(true);
        menuItem.setOnFocusChangeListener(this);
    }

    public void removeMenuItem(View menuItem) {
        this.mMenuContainer.removeView(menuItem);
    }

    public boolean isShow() {
        return this.mIsShow;
    }

    public void show() {
        if (!this.mIsShow) {
            this.mWm.addView(this.mRootView, createLayoutParams());
            this.mIsShow = true;
        }
    }

    public void dismiss() {
        if (this.mIsShow) {
            this.mWm.removeViewImmediate(this.mRootView);
            this.mIsShow = false;
        }
    }

    private WindowManager.LayoutParams createLayoutParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = 2003;
        params.format = 1;
        return params;
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            this.mMenuContainer.setOnTop(v);
            scaleView(v, this.mScaleRatio, this.mDuration);
        } else {
            scaleView(v, 1.0f, this.mDuration);
        }
        if (this.mListener != null) {
            this.mListener.onMenuItemFocusChange(v, this.mMenuContainer.indexOfChild(v), hasFocus);
        }
    }

    private void scaleView(View v, float scaleRatio, long duration) {
        AnimatorSet animSet = new AnimatorSet();
        r1 = new Animator[2];
        r1[0] = ObjectAnimator.ofFloat(v, "scaleX", new float[]{v.getScaleX(), scaleRatio});
        r1[1] = ObjectAnimator.ofFloat(v, "scaleY", new float[]{v.getScaleY(), scaleRatio});
        animSet.playTogether(r1);
        animSet.setDuration(duration).start();
    }
}

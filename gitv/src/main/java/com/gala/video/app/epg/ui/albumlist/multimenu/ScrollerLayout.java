package com.gala.video.app.epg.ui.albumlist.multimenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.mcto.ads.internal.net.SendFlag;
import java.util.ArrayList;

public class ScrollerLayout extends LinearLayout {
    public static final int INTERPOLATOR_DECELERATE = 1;
    public static final int INTERPOLATOR_LINEAR = 2;
    public static final int INTERPOLATOR_NULL = 0;
    private final String LOG_TAG = "EPG/weight/ScrollerLayout";
    private Context mContext;
    private View mCurView;
    private int mCurViewPos = 0;
    private ScrollerDrawListener mDrawListener;
    private ScrollerEventListener mEventListener;
    private boolean mIsFirstItemVisible = true;
    private boolean mIsLastItemVisible = true;
    private int mLastDistance;
    private boolean mLayerDefaultFlag = true;
    private int mLayoutCenterX;
    private int mMaxScrollX;
    private Scroller mScroller;
    private int mSelectPos = 0;
    private View mSelectView;

    public interface ScrollerEventListener {
        void onFirstItemVisible(ScrollerLayout scrollerLayout);

        void onItemClickListener(ScrollerLayout scrollerLayout, int i);

        void onItemSelectListener(View view, int i, boolean z);

        void onLastItemVisible(ScrollerLayout scrollerLayout);

        void onSlidingToLeft(ScrollerLayout scrollerLayout);

        void onSlidingToRight(ScrollerLayout scrollerLayout);
    }

    public interface ScrollerDrawListener {
        void drawItemDividingLine(ScrollerLayout scrollerLayout, Canvas canvas);

        void drawShaderLayer(ScrollerLayout scrollerLayout, Canvas canvas);
    }

    public ScrollerLayout(Context context) {
        super(context);
        initLayout(context);
    }

    public ScrollerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    public ScrollerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
    }

    private void initLayout(Context context) {
        setClipToPadding(false);
        setClipChildren(false);
        this.mContext = context;
        setLayerType(2, null);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @SuppressLint({"DrawAllocation"})
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int[] location = new int[2];
        getLocationInWindow(location);
        getLocationOnScreen(location);
        this.mLayoutCenterX = location[0] + (getWidth() / 2);
        this.mMaxScrollX = getChildAt(getChildCount() - 1).getRight() - getWidth();
        this.mSelectView = getChildAt(this.mSelectPos);
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            final int pos = i;
            if (view != null) {
                view.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (ScrollerLayout.this.mEventListener != null) {
                            ScrollerLayout.this.mSelectView = v;
                            ScrollerLayout.this.mEventListener.onItemClickListener(ScrollerLayout.this, pos);
                        }
                    }
                });
                view.setOnFocusChangeListener(new OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (ScrollerLayout.this.mEventListener != null) {
                            ScrollerLayout.this.mEventListener.onItemSelectListener(v, pos, hasFocus);
                        }
                    }
                });
            }
        }
        if (getChildAt(getChildCount() - 1).getRight() > getWidth() && this.mLayerDefaultFlag) {
            setLastItemVisible(false);
            setFirstItemVisible(true);
            this.mLayerDefaultFlag = false;
        }
        if (this.mCurViewPos != 0) {
            initScroll();
        }
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            int disX = this.mScroller.getCurrX() - this.mLastDistance;
            this.mLastDistance = this.mScroller.getCurrX();
            scrollBy(disX, 0);
            postInvalidate();
        }
        super.computeScroll();
    }

    public void init(Context context) {
        init(context, 0);
    }

    public void init(Context context, int scrollerType) {
        switch (scrollerType) {
            case 1:
                this.mScroller = new Scroller(this.mContext, new DecelerateInterpolator(1.2f));
                break;
            case 2:
                this.mScroller = new Scroller(this.mContext, new LinearInterpolator());
                break;
            default:
                this.mScroller = new Scroller(this.mContext, null);
                break;
        }
        setLayerType(2, null);
        setFocusable(true);
        setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
        setWillNotDraw(false);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return super.dispatchKeyEvent(event);
        }
        switch (event.getKeyCode()) {
            case 21:
                if (this.mCurViewPos <= 0) {
                    LogUtils.d("EPG/weight/ScrollerLayout", ">>>>> left first");
                    return true;
                }
                computeLeftScroll();
                if (this.mCurView == null) {
                    return true;
                }
                this.mCurView.requestFocus();
                return true;
            case 22:
                if (this.mCurViewPos + 1 >= getChildCount() - 1) {
                    LogUtils.d("EPG/weight/ScrollerLayout", ">>>>> right last");
                    return true;
                }
                computeRightScroll();
                if (this.mCurView == null) {
                    return true;
                }
                this.mCurView.requestFocus();
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    public void initScroll() {
        this.mCurView = getChildAt(this.mCurViewPos);
        if (getScrollX() < this.mMaxScrollX && getLocationX(this.mCurView) > this.mLayoutCenterX) {
            if (this.mEventListener != null) {
                this.mEventListener.onSlidingToRight(this);
            }
            this.mLastDistance = 0;
            int distance = (getLocationX(this.mCurView) - this.mLayoutCenterX) + (this.mCurView.getWidth() / 2);
            if (getScrollX() + distance > this.mMaxScrollX) {
                distance = this.mMaxScrollX - getScrollX();
                if (this.mEventListener != null) {
                    this.mEventListener.onLastItemVisible(this);
                }
            }
            this.mScroller.startScroll(0, 0, distance, 0);
            invalidate();
        }
    }

    private void computeRightScroll() {
        this.mCurViewPos++;
        this.mCurView = getChildAt(this.mCurViewPos);
        if (getScrollX() < this.mMaxScrollX && getLocationX(this.mCurView) > this.mLayoutCenterX) {
            if (this.mEventListener != null) {
                this.mEventListener.onSlidingToRight(this);
            }
            this.mLastDistance = 0;
            int distance = (getLocationX(this.mCurView) - this.mLayoutCenterX) + (this.mCurView.getWidth() / 2);
            if (getScrollX() + distance > this.mMaxScrollX) {
                distance = this.mMaxScrollX - getScrollX();
                if (this.mEventListener != null) {
                    this.mEventListener.onLastItemVisible(this);
                }
            }
            this.mScroller.startScroll(0, 0, distance, 0);
            invalidate();
        }
    }

    private void computeLeftScroll() {
        this.mCurViewPos--;
        this.mCurView = getChildAt(this.mCurViewPos);
        if (getScrollX() > 0 && getLocationX(this.mCurView) + this.mCurView.getWidth() < this.mLayoutCenterX) {
            if (this.mEventListener != null) {
                this.mEventListener.onSlidingToLeft(this);
            }
            this.mLastDistance = 0;
            int distance = ((getLocationX(this.mCurView) + this.mCurView.getWidth()) - this.mLayoutCenterX) - (this.mCurView.getWidth() / 2);
            if (getScrollX() + distance <= 0) {
                distance = -getScrollX();
                if (this.mEventListener != null) {
                    this.mEventListener.onFirstItemVisible(this);
                }
            }
            LogUtils.d("EPG/weight/ScrollerLayout", ">>>>> dispatchKeyEvent Left--- distance: " + distance);
            this.mScroller.startScroll(0, 0, distance, 0);
            invalidate();
        }
    }

    private int getLocationX(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        view.getLocationOnScreen(location);
        return location[0];
    }

    public View focusSearch(View focused, int direction) {
        return super.focusSearch(focused, direction);
    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        View view = getChildAt(this.mCurViewPos);
        if (view != null) {
            return view.requestFocus(direction, previouslyFocusedRect);
        }
        return false;
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            View view = getChildAt(this.mCurViewPos);
            if (view != null) {
                view.requestFocus();
            }
        }
    }

    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        Log.d("EPG/weight/ScrollerLayout", getTag() + " requestFocus");
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (isFocused()) {
            super.addFocusables(views, direction, focusableMode);
        } else {
            views.add(this);
        }
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mDrawListener != null) {
            this.mDrawListener.drawShaderLayer(this, canvas);
            this.mDrawListener.drawItemDividingLine(this, canvas);
        }
    }

    public void setScrollerEventListener(ScrollerEventListener listener) {
        this.mEventListener = listener;
    }

    public void setScrollerDrawListener(ScrollerDrawListener listener) {
        this.mDrawListener = listener;
    }

    public int getSelectPos() {
        return this.mSelectPos;
    }

    public View getSelectedView() {
        return this.mSelectView == null ? getChildAt(this.mSelectPos) : this.mSelectView;
    }

    public void setSelectPos(int selectPos) {
        this.mSelectPos = selectPos;
    }

    public void setFirstItemVisible(boolean leftFlag) {
        this.mIsLastItemVisible = leftFlag;
    }

    public void setLastItemVisible(boolean rightFlag) {
        this.mIsFirstItemVisible = rightFlag;
    }

    public boolean isLastItemVisible() {
        return this.mIsLastItemVisible;
    }

    public boolean isFirstItemVisible() {
        return this.mIsFirstItemVisible;
    }

    public void setCurViewPos(int pos) {
        if (pos >= 0) {
            this.mCurViewPos = pos;
        }
    }
}

package com.gala.video.app.player.ui.widget.views;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import com.gala.video.app.player.R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig.ItemSize;
import com.gala.video.lib.share.common.widget.AnimatorEx;
import com.gala.video.lib.share.common.widget.ScrollHolder;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.List;
import org.xbill.DNS.WKSRecord.Service;

public class VerticalScrollLayout extends LinearLayout {
    private static final int DEFAULT_DURATION = 250;
    private static final int MSG_DISPATCH_VISIBILITY_CHANGE = 1;
    private static final String TAG = "VerticalScrollLayout";
    private static int sBottomScrollOffset = ResourceUtil.getDimen(R.dimen.dimen_18dp);
    private static int sTopScrollOffset = ResourceUtil.getDimen(R.dimen.dimen_4dp);
    private static int sTopTitleHeight = ResourceUtil.getDimen(R.dimen.detail_top_title_height);
    private MyListener animatorListener;
    private boolean campatibleMode;
    private boolean clipChildren;
    private int defaultScroll;
    private List<OnChildStateChangeListener> mChildStateChangedListeners;
    private List<Boolean> mChildrenVisibility;
    private int mCurFocusIndex;
    private int mCurScrollDirection;
    private int[] mFocusedViewLocation;
    private int[] mFocusedViewSize;
    private Handler mMainHandler;
    private List<OnScrollStateChangedListener> mScrollListeners;
    private AnimatorEx mScroller;

    private class MyListener implements AnimatorListener {
        private boolean canceled;

        private MyListener() {
        }

        public void onAnimationStart(Animator animation) {
            VerticalScrollLayout.this.dispatchScrollStarted();
        }

        public void onAnimationEnd(Animator animation) {
            if (this.canceled) {
                this.canceled = false;
                return;
            }
            VerticalScrollLayout.this.dispatchScrollEnded();
            VerticalScrollLayout.this.mMainHandler.removeMessages(1);
            VerticalScrollLayout.this.mMainHandler.sendMessageDelayed(VerticalScrollLayout.this.mMainHandler.obtainMessage(1), 500);
        }

        public void onAnimationCancel(Animator animation) {
            this.canceled = true;
        }

        public void onAnimationRepeat(Animator animation) {
        }
    }

    public interface OnChildStateChangeListener {
        void onChildCountChanged(int i);

        void onChildVisibilityChanged(int i, boolean z);

        void onChildrenVisibilityRefreshed(List<Boolean> list);
    }

    public interface OnScrollStateChangedListener {
        void onScrollAnimEnded(int i, int i2);

        void onScrollAnimStarted(int i, int i2);
    }

    public VerticalScrollLayout(Context context) {
        super(context);
        this.defaultScroll = ItemSize.ITEM_360;
        this.mScrollListeners = new ArrayList();
        this.mChildStateChangedListeners = new ArrayList();
        this.mChildrenVisibility = new ArrayList();
        this.clipChildren = true;
        this.campatibleMode = false;
        this.mFocusedViewLocation = new int[2];
        this.mFocusedViewSize = new int[2];
        this.mMainHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    VerticalScrollLayout.this.correctChildVisibilityAndDispatch();
                } else {
                    super.handleMessage(msg);
                }
            }
        };
        this.animatorListener = new MyListener();
        initView(context);
    }

    public VerticalScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView(context);
    }

    public VerticalScrollLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.defaultScroll = ItemSize.ITEM_360;
        this.mScrollListeners = new ArrayList();
        this.mChildStateChangedListeners = new ArrayList();
        this.mChildrenVisibility = new ArrayList();
        this.clipChildren = true;
        this.campatibleMode = false;
        this.mFocusedViewLocation = new int[2];
        this.mFocusedViewSize = new int[2];
        this.mMainHandler = /* anonymous class already generated */;
        this.animatorListener = new MyListener();
        initView(context);
    }

    private void initView(Context context) {
        this.campatibleMode = VERSION.SDK_INT < 19;
        this.defaultScroll = (int) (360.0f * (((float) context.getResources().getDisplayMetrics().widthPixels) / 1920.0f));
        this.mScroller = new AnimatorEx(0.0f, 1.0f);
        this.mScroller.setDuration(250);
        this.mScroller.setInterpolator(new LinearInterpolator());
        this.mScroller.addListener(this.animatorListener);
        setOrientation(1);
        setChildrenDrawingOrderEnabled(true);
    }

    public void setClipChildren(boolean clipChildren) {
        super.setClipChildren(clipChildren);
        this.clipChildren = clipChildren;
    }

    public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
        if (this.campatibleMode && !this.clipChildren) {
            dirty.offset(location[0] - getScrollX(), location[1] - getScrollY());
            dirty.union(0, 0, getWidth(), getHeight());
            dirty.offset(getScrollX() - location[0], getScrollY() - location[1]);
        }
        return super.invalidateChildInParent(location, dirty);
    }

    protected int getChildDrawingOrder(int childCount, int i) {
        if (hasFocus() && !isFocused()) {
            int top = indexOfChild(getFocusedChild());
            if (i == childCount - 1) {
                return top;
            }
            if (i >= top) {
                return i + 1;
            }
        }
        return i;
    }

    public void bringChildToFront(View child) {
        invalidate();
    }

    public void addView(View child, int index, LayoutParams params) {
        super.addView(child, index, params);
        this.mChildrenVisibility.add(Boolean.valueOf(false));
        dispatchChildCountChanged(getChildCount());
    }

    private void correctChildVisibilityAndDispatch() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            boolean visible;
            if (i == 0) {
                if (getScrollY() <= 0) {
                    visible = true;
                } else {
                    visible = false;
                }
                if (visible != ((Boolean) this.mChildrenVisibility.get(i)).booleanValue()) {
                    dispatchChildVisibilityChanged(i, visible);
                    this.mChildrenVisibility.set(i, Boolean.valueOf(visible));
                }
            } else {
                if ((child.getTop() - getScrollY()) + getTop() < sTopTitleHeight || (child.getBottom() - getScrollY()) + getTop() > getBottom()) {
                    visible = false;
                } else {
                    visible = true;
                }
                if (visible != ((Boolean) this.mChildrenVisibility.get(i)).booleanValue()) {
                    dispatchChildVisibilityChanged(i, visible);
                    this.mChildrenVisibility.set(i, Boolean.valueOf(visible));
                }
            }
        }
        dispatchChildrenVisibilityRefreshed();
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        correctChildVisibilityAndDispatch();
    }

    public boolean canScroll() {
        return getHeight() < getChildAt(getChildCount() + -1).getBottom() + getPaddingBottom();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || executeKeyEvent(event);
    }

    public boolean executeKeyEvent(KeyEvent event) {
        if (!canScroll()) {
            return false;
        }
        if (event.getAction() != 0) {
            return false;
        }
        switch (event.getKeyCode()) {
            case 19:
                return arrowScroll(33);
            case 20:
                return arrowScroll(Service.CISCO_FNA);
            default:
                if (!LogUtils.mIsDebug) {
                    return false;
                }
                LogUtils.d(TAG, "unhandled key event=" + event);
                return false;
        }
    }

    public boolean arrowScroll(int direction) {
        View currentFocused = findFocus();
        if (currentFocused != this) {
            View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
            if (nextFocused == null) {
                if (!LogUtils.mIsDebug) {
                    return false;
                }
                LogUtils.d(TAG, "arrowScroll, return false, next focusable view is null.");
                return false;
            } else if (nextFocused.requestFocus(direction)) {
                boolean up;
                if (direction == 33) {
                    up = true;
                } else {
                    up = false;
                }
                if (!up && getChildAt(getChildCount() - 1).getBottom() <= (getScrollY() + getHeight()) - getPaddingBottom()) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(TAG, "arrowScroll, return true, don't need scroll on down direction");
                    }
                    return true;
                } else if (!up || getChildAt(0).getTop() < getScrollY() + getPaddingTop()) {
                    View parent = nextFocused.getParent();
                    View child = nextFocused;
                    while (parent != null && (parent instanceof View) && parent != this) {
                        child = parent;
                        parent = parent.getParent();
                    }
                    if (child != this) {
                        doScrollY(computeScrollDelta(child, up));
                    }
                    return true;
                } else {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(TAG, "arrowScroll, return true, don't need scroll on up direction");
                    }
                    return true;
                }
            } else if (!LogUtils.mIsDebug) {
                return false;
            } else {
                LogUtils.d(TAG, "arrowScroll, return false, request next focus failed.");
                return false;
            }
        } else if (!LogUtils.mIsDebug) {
            return false;
        } else {
            LogUtils.d(TAG, "arrowScroll, return false, focus on this VerticalScrollView.");
            return false;
        }
    }

    protected int computeScrollDelta(View focused, boolean isUp) {
        int i = 0;
        if (!(getChildCount() == 0 || focused == null)) {
            int scrollY = getScrollY();
            int index = indexOfChild(focused);
            int c = getChildCount();
            focused.getLocationInWindow(this.mFocusedViewLocation);
            this.mFocusedViewSize[0] = focused.getMeasuredWidth();
            this.mFocusedViewSize[1] = focused.getMeasuredHeight();
            int scroll1;
            if (isUp) {
                this.mCurScrollDirection = 33;
                if (index <= 0) {
                    i = -scrollY;
                } else {
                    scroll1 = (focused.getTop() + getTop()) - sTopTitleHeight;
                    if (index == 1) {
                        i = scroll1 - scrollY;
                    } else {
                        i = Math.min(scroll1, (getChildAt(c - 1).getBottom() - getHeight()) + getPaddingBottom()) - scrollY;
                    }
                }
            } else {
                this.mCurScrollDirection = Service.CISCO_FNA;
                if (index <= 0) {
                    i = 0;
                } else {
                    scroll1 = (focused.getTop() + getTop()) - sTopTitleHeight;
                    if (index == 1) {
                        i = scroll1 - scrollY;
                    } else if (index == 2 && index == c - 1) {
                        i = ((getChildAt(index - 1).getTop() + getTop()) - sTopTitleHeight) - scrollY;
                    } else {
                        i = Math.min(scroll1, (getChildAt(c - 1).getBottom() - getHeight()) + getPaddingBottom()) - scrollY;
                    }
                }
            }
            this.mCurFocusIndex = index;
        }
        return i;
    }

    private void doScrollY(int delta) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> doScrollY, delta=" + delta);
        }
        if (delta != 0) {
            smoothScroll(getScrollY(), delta);
        }
    }

    public final void smoothScroll(int start, int dy) {
        if (getChildCount() != 0) {
            if (this.mScroller.isRunning()) {
                this.mScroller.cancel();
                if (dy > this.defaultScroll) {
                    start += dy - this.defaultScroll;
                    dy = this.defaultScroll;
                    scrollTo(0, start);
                } else if (dy < (-this.defaultScroll)) {
                    start += this.defaultScroll + dy;
                    dy = -this.defaultScroll;
                    scrollTo(0, start);
                }
            }
            this.mScroller.setDuration((long) ((1.0f + (((float) Math.abs(dy)) / ((float) this.defaultScroll))) * 100.0f));
            this.mScroller.addAnimator(new ScrollHolder(this, false, start, start + dy));
            this.mScroller.start();
        }
    }

    private void dispatchScrollStarted() {
        for (OnScrollStateChangedListener listener : this.mScrollListeners) {
            listener.onScrollAnimStarted(this.mCurScrollDirection, this.mCurFocusIndex);
        }
    }

    private void dispatchScrollEnded() {
        for (OnScrollStateChangedListener listener : this.mScrollListeners) {
            listener.onScrollAnimEnded(this.mCurScrollDirection, this.mCurFocusIndex);
        }
    }

    private void dispatchChildCountChanged(int count) {
        for (OnChildStateChangeListener listener : this.mChildStateChangedListeners) {
            listener.onChildCountChanged(count);
        }
    }

    private void dispatchChildVisibilityChanged(int index, boolean visible) {
        for (OnChildStateChangeListener listener : this.mChildStateChangedListeners) {
            listener.onChildVisibilityChanged(index, visible);
        }
    }

    private void dispatchChildrenVisibilityRefreshed() {
        for (OnChildStateChangeListener listener : this.mChildStateChangedListeners) {
            listener.onChildrenVisibilityRefreshed(this.mChildrenVisibility);
        }
    }

    public void addOnScrollListener(OnScrollStateChangedListener listener) {
        if (!this.mScrollListeners.contains(listener)) {
            this.mScrollListeners.add(listener);
        }
    }

    public void removeOnScrollListener(OnScrollStateChangedListener listener) {
        this.mScrollListeners.remove(listener);
    }

    public void addOnChildStateChangedListener(OnChildStateChangeListener listener) {
        if (listener != null && !this.mChildStateChangedListeners.contains(listener)) {
            this.mChildStateChangedListeners.add(listener);
        }
    }

    public void removeOnChildStateChangedListener(OnChildStateChangeListener listener) {
        this.mChildStateChangedListeners.remove(listener);
    }

    public void clearOnChildStateChangedListener() {
        this.mChildStateChangedListeners.clear();
    }
}

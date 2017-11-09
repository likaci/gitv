package com.tvos.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AnimationUtils;
import android.widget.EdgeEffect;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import com.mcto.ads.internal.net.SendFlag;
import java.util.List;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;

public class VScrollView extends FrameLayout {
    static final int ANIMATED_SCROLL_GAP = 250;
    static final float MAX_SCROLL_FACTOR = 0.5f;
    private View mChildToScrollTo;
    private EdgeEffect mEdgeGlowBottom;
    private EdgeEffect mEdgeGlowLeft;
    private EdgeEffect mEdgeGlowRight;
    private EdgeEffect mEdgeGlowTop;
    private boolean mFillViewport;
    private boolean mIsLayoutDirty;
    private long mLastScroll;
    private int mMarginBottom;
    private int mMarginLeft;
    private int mMarginRight;
    private int mMarginTop;
    private OnScrollListener mOnScrollListener;
    private int mOverflingDistance;
    private OverScroller mScroller;
    private boolean mSmoothScrollingEnabled;
    private final Rect mTempRect;
    private VelocityTracker mVelocityTracker;

    public interface OnScrollListener {
        void onScroll(int i, int i2);
    }

    public VScrollView(Context context) {
        this(context, null);
    }

    public VScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mTempRect = new Rect();
        this.mMarginTop = 0;
        this.mMarginBottom = 0;
        this.mMarginLeft = 0;
        this.mMarginRight = 0;
        this.mIsLayoutDirty = true;
        this.mChildToScrollTo = null;
        this.mSmoothScrollingEnabled = true;
        initScrollView();
    }

    public VScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mTempRect = new Rect();
        this.mMarginTop = 0;
        this.mMarginBottom = 0;
        this.mMarginLeft = 0;
        this.mMarginRight = 0;
        this.mIsLayoutDirty = true;
        this.mChildToScrollTo = null;
        this.mSmoothScrollingEnabled = true;
        initScrollView();
    }

    protected float getTopFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int length = getVerticalFadingEdgeLength();
        if (getScrollY() < length) {
            return ((float) getScrollY()) / ((float) length);
        }
        return 1.0f;
    }

    protected float getBottomFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int length = getVerticalFadingEdgeLength();
        int span = (getChildAt(0).getBottom() - getScrollY()) - (getHeight() - getPaddingBottom());
        if (span < length) {
            return ((float) span) / ((float) length);
        }
        return 1.0f;
    }

    protected float getLeftFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int length = getHorizontalFadingEdgeLength();
        if (getScrollX() < length) {
            return ((float) getScrollX()) / ((float) length);
        }
        return 1.0f;
    }

    protected float getRightFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int length = getHorizontalFadingEdgeLength();
        int span = (getChildAt(0).getRight() - getScrollX()) - (getWidth() - getPaddingRight());
        if (span < length) {
            return ((float) span) / ((float) length);
        }
        return 1.0f;
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.mOnScrollListener = listener;
    }

    public int getMaxScrollAmountX() {
        return (int) (0.5f * ((float) (getRight() - getLeft())));
    }

    public int getMaxScrollAmountY() {
        return (int) (0.5f * ((float) (getBottom() - getTop())));
    }

    private void initScrollView() {
        this.mScroller = new OverScroller(getContext());
        setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
        setWillNotDraw(false);
        this.mOverflingDistance = ViewConfiguration.get(getContext()).getScaledOverflingDistance();
    }

    public void addView(View child) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("VScrollView can host only one direct child");
        }
        super.addView(child);
    }

    public void addView(View child, int index) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("VScrollView can host only one direct child");
        }
        super.addView(child, index);
    }

    public void addView(View child, LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("VScrollView can host only one direct child");
        }
        super.addView(child, params);
    }

    public void addView(View child, int index, LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("VScrollView can host only one direct child");
        }
        super.addView(child, index, params);
    }

    private boolean canScroll() {
        View child = getChildAt(0);
        if (child == null) {
            return false;
        }
        int childWidth = child.getWidth();
        int childHeight = child.getHeight();
        if (getWidth() < (getPaddingLeft() + childWidth) + getPaddingRight() || getHeight() < (getPaddingTop() + childHeight) + getPaddingBottom()) {
            return true;
        }
        return false;
    }

    public boolean isFillViewport() {
        return this.mFillViewport;
    }

    public void setFillViewport(boolean fillViewport) {
        if (fillViewport != this.mFillViewport) {
            this.mFillViewport = fillViewport;
            requestLayout();
        }
    }

    public boolean isSmoothScrollingEnabled() {
        return this.mSmoothScrollingEnabled;
    }

    public void setSmoothScrollingEnabled(boolean smoothScrollingEnabled) {
        this.mSmoothScrollingEnabled = smoothScrollingEnabled;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 0) {
            getChildAt(0).measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 0), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 0));
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || executeKeyEvent(event);
    }

    public boolean executeKeyEvent(KeyEvent event) {
        int i = 33;
        this.mTempRect.setEmpty();
        if (!canScroll()) {
            return false;
        }
        if (event.getAction() != 0) {
            return false;
        }
        switch (event.getKeyCode()) {
            case 19:
                if (event.isAltPressed()) {
                    return fullScroll(33);
                }
                return arrowScroll(33);
            case 20:
                if (event.isAltPressed()) {
                    return fullScroll(Service.CISCO_FNA);
                }
                return arrowScroll(Service.CISCO_FNA);
            case 21:
                if (event.isAltPressed()) {
                    return fullScroll(17);
                }
                return arrowScroll(17);
            case 22:
                if (event.isAltPressed()) {
                    return fullScroll(66);
                }
                return arrowScroll(66);
            case Protocol.CFTP /*62*/:
                if (!event.isShiftPressed()) {
                    i = Service.CISCO_FNA;
                }
                pageScroll(i);
                return false;
            default:
                return false;
        }
    }

    private void recycleVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept) {
            recycleVelocityTracker();
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    public boolean shouldDelayChildPressedState() {
        return true;
    }

    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if (this.mScroller.isFinished()) {
            super.scrollTo(scrollX, scrollY);
        } else {
            setScrollX(scrollX);
            setScrollY(scrollY);
            if (clampedX || clampedY) {
                int scrollRangeY;
                OverScroller overScroller = this.mScroller;
                int scrollX2 = getScrollX();
                int scrollY2 = getScrollY();
                int scrollRangeX = clampedX ? getScrollRangeX() : 0;
                if (clampedY) {
                    scrollRangeY = getScrollRangeY();
                } else {
                    scrollRangeY = 0;
                }
                overScroller.springBack(scrollX2, scrollY2, 0, scrollRangeX, 0, scrollRangeY);
            }
        }
        awakenScrollBars();
    }

    public boolean performAccessibilityAction(int action, Bundle arguments) {
        if (super.performAccessibilityAction(action, arguments)) {
            return true;
        }
        if (!isEnabled()) {
            return false;
        }
        int targetScrollX;
        int targetScrollY;
        switch (action) {
            case 4096:
                targetScrollX = Math.min(getScrollX() + ((getWidth() - getPaddingLeft()) - getPaddingRight()), getScrollRangeX());
                targetScrollY = Math.min(getScrollY() + ((getHeight() - getPaddingBottom()) - getPaddingTop()), getScrollRangeY());
                if (targetScrollY == getScrollY() && targetScrollX == getScrollX()) {
                    return false;
                }
                smoothScrollTo(targetScrollX, targetScrollY);
                return true;
            case 8192:
                targetScrollY = Math.max(getScrollY() - ((getHeight() - getPaddingBottom()) - getPaddingTop()), 0);
                targetScrollX = Math.max(0, getScrollX() - ((getWidth() - getPaddingLeft()) - getPaddingRight()));
                if (targetScrollY == getScrollY() && targetScrollX == getScrollX()) {
                    return false;
                }
                smoothScrollTo(targetScrollX, targetScrollY);
                return true;
            default:
                return false;
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(VScrollView.class.getName());
        if (isEnabled()) {
            int scrollRangeY = getScrollRangeY();
            int scrollRangeX = getScrollRangeX();
            if (scrollRangeY > 0 || scrollRangeX > 0) {
                info.setScrollable(true);
            }
        }
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(VScrollView.class.getName());
        boolean z = getScrollRangeX() > 0 || getScrollRangeY() > 0;
        event.setScrollable(z);
        event.setScrollX(getScrollX());
        event.setScrollY(getScrollY());
        event.setMaxScrollX(getScrollRangeX());
        event.setMaxScrollY(getScrollRangeY());
    }

    private int getScrollRangeX() {
        if (getChildCount() > 0) {
            return Math.max(0, getChildAt(0).getWidth() - ((getWidth() - getPaddingLeft()) - getPaddingRight()));
        }
        return 0;
    }

    private int getScrollRangeY() {
        if (getChildCount() > 0) {
            return Math.max(0, getChildAt(0).getHeight() - ((getHeight() - getPaddingBottom()) - getPaddingTop()));
        }
        return 0;
    }

    private View findFocusableViewInBounds(int direction, int left, int right, int top, int bottom) {
        List<View> focusables = getFocusables(2);
        View focusCandidate = null;
        boolean foundFullyContainedFocusable = false;
        int count = focusables.size();
        for (int i = 0; i < count; i++) {
            View view = (View) focusables.get(i);
            int viewLeft = view.getLeft();
            int viewRight = view.getRight();
            int viewTop = view.getTop();
            int viewBottom = view.getBottom();
            if (left < viewRight && viewLeft < right && top < viewBottom && viewTop < bottom) {
                boolean viewIsFullyContained = left < viewLeft && viewRight < right && top < viewTop && viewBottom < bottom;
                if (focusCandidate == null) {
                    focusCandidate = view;
                    foundFullyContainedFocusable = viewIsFullyContained;
                } else {
                    boolean viewIsCloserToBoundary = (direction == 17 && viewLeft < focusCandidate.getLeft()) || ((direction == 66 && viewRight > focusCandidate.getRight()) || ((direction == 33 && viewTop < focusCandidate.getTop()) || (direction == Service.CISCO_FNA && viewBottom > focusCandidate.getBottom())));
                    if (foundFullyContainedFocusable) {
                        if (viewIsFullyContained && viewIsCloserToBoundary) {
                            focusCandidate = view;
                        }
                    } else if (viewIsFullyContained) {
                        focusCandidate = view;
                        foundFullyContainedFocusable = true;
                    } else if (viewIsCloserToBoundary) {
                        focusCandidate = view;
                    }
                }
            }
        }
        return focusCandidate;
    }

    public boolean pageScroll(int direction) {
        int width = getWidth();
        int height = getHeight();
        View view;
        if (direction == 66) {
            this.mTempRect.left = getScrollX() + width;
            if (getChildCount() > 0) {
                view = getChildAt(0);
                if (this.mTempRect.left + width > view.getRight()) {
                    this.mTempRect.left = view.getRight() - width;
                }
            }
            this.mTempRect.right = this.mTempRect.left + width;
        } else if (direction == 17) {
            this.mTempRect.left = getScrollX() - width;
            if (this.mTempRect.left < 0) {
                this.mTempRect.left = 0;
            }
            this.mTempRect.right = this.mTempRect.left + width;
        } else if (direction == Service.CISCO_FNA) {
            this.mTempRect.top = getScrollY() + height;
            int count = getChildCount();
            if (count > 0) {
                view = getChildAt(count - 1);
                if (this.mTempRect.top + height > view.getBottom()) {
                    this.mTempRect.top = view.getBottom() - height;
                }
            }
            this.mTempRect.bottom = this.mTempRect.top + height;
        } else if (direction == 33) {
            this.mTempRect.top = getScrollY() - height;
            if (this.mTempRect.top < 0) {
                this.mTempRect.top = 0;
            }
            this.mTempRect.bottom = this.mTempRect.top + height;
        }
        return scrollAndFocus(direction, this.mTempRect.left, this.mTempRect.right, this.mTempRect.top, this.mTempRect.bottom);
    }

    public boolean fullScroll(int direction) {
        int height = getHeight();
        int width = getWidth();
        View view;
        if (direction == 66) {
            this.mTempRect.left = 0;
            this.mTempRect.right = width;
            if (getChildCount() > 0) {
                view = getChildAt(0);
                this.mTempRect.right = view.getRight();
                this.mTempRect.left = this.mTempRect.right - width;
            }
        } else if (direction == 17) {
            this.mTempRect.left = 0;
            this.mTempRect.right = width;
        } else if (direction == Service.CISCO_FNA) {
            this.mTempRect.top = 0;
            this.mTempRect.bottom = height;
            int count = getChildCount();
            if (count > 0) {
                view = getChildAt(count - 1);
                this.mTempRect.bottom = view.getBottom() + getPaddingBottom();
                this.mTempRect.top = this.mTempRect.bottom - height;
            }
        } else if (direction == 33) {
            this.mTempRect.top = 0;
            this.mTempRect.bottom = height;
        }
        return scrollAndFocus(direction, this.mTempRect.left, this.mTempRect.right, this.mTempRect.top, this.mTempRect.bottom);
    }

    private boolean scrollAndFocus(int direction, int left, int right, int top, int bottom) {
        boolean handled = true;
        int width = getWidth();
        int height = getHeight();
        int containerLeft = getScrollX();
        int containerTop = getScrollY();
        int containerRight = containerLeft + width;
        int containerBottom = containerTop + height;
        View newFocused = findFocusableViewInBounds(direction, left, right, top, bottom);
        if (newFocused == null) {
            newFocused = this;
        }
        if (left >= containerLeft && right <= containerRight && top >= containerTop && bottom <= containerBottom) {
            handled = false;
        } else if (direction == 17) {
            doScroll(left - containerLeft, 0);
        } else if (direction == 66) {
            doScroll(right - containerRight, 0);
        } else if (direction == Service.CISCO_FNA) {
            doScroll(0, bottom - containerBottom);
        } else if (direction == 33) {
            doScroll(0, top - containerTop);
        }
        if (newFocused != findFocus()) {
            newFocused.requestFocus(direction);
        }
        return handled;
    }

    public void setScrollMarginTop(int top) {
        this.mMarginTop = top;
    }

    public void setScrollMarginBottom(int bottom) {
        this.mMarginBottom = bottom;
    }

    public void setScrollMarginLeft(int left) {
        this.mMarginLeft = left;
    }

    public void setScrollMarginRight(int right) {
        this.mMarginRight = right;
    }

    public boolean arrowScroll(int direction) {
        View currentFocused = findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        }
        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        int maxJumpX = getMaxScrollAmountX();
        int maxJumpY = getMaxScrollAmountY();
        int scrollDeltaX;
        int scrollDeltaY;
        if (nextFocused == null || !isWithinDeltaOfScreen(nextFocused, maxJumpX, maxJumpX)) {
            scrollDeltaX = maxJumpX;
            scrollDeltaY = maxJumpY;
            if (direction == 17 && getScrollX() < scrollDeltaX) {
                scrollDeltaX = -getScrollX();
                scrollDeltaY = 0;
            } else if (direction == 66 && getChildCount() > 0) {
                if (getChildCount() > 0 && getChildAt(0).getRight() - (getScrollX() + getWidth()) < maxJumpX) {
                    scrollDeltaX = 0;
                }
                scrollDeltaY = 0;
            } else if (direction == 33 && getScrollY() < scrollDeltaY) {
                scrollDeltaY = -getScrollY();
                scrollDeltaX = 0;
            } else if (direction == Service.CISCO_FNA) {
                if (getChildCount() > 0 && getChildAt(0).getBottom() - ((getScrollY() + getHeight()) - getPaddingBottom()) < maxJumpY) {
                    scrollDeltaY = 0;
                }
                scrollDeltaX = 0;
            }
            if (scrollDeltaX == 0 && scrollDeltaY == 0) {
                return false;
            }
            doScroll(scrollDeltaX, scrollDeltaY);
        } else {
            nextFocused.getDrawingRect(this.mTempRect);
            offsetDescendantRectToMyCoords(nextFocused, this.mTempRect);
            scrollDeltaX = computeScrollDeltaXToGetChildRectOnScreen(this.mTempRect);
            scrollDeltaY = computeScrollDeltaYToGetChildRectOnScreen(this.mTempRect);
            if (scrollDeltaX > 0) {
                scrollDeltaX += this.mMarginRight;
            } else if (scrollDeltaX < 0) {
                scrollDeltaX -= this.mMarginLeft;
            }
            if (scrollDeltaY > 0) {
                scrollDeltaY += this.mMarginBottom;
            } else if (scrollDeltaY < 0) {
                scrollDeltaY -= this.mMarginTop;
            }
            doScroll(scrollDeltaX, scrollDeltaY);
            nextFocused.requestFocus(direction);
        }
        if (currentFocused != null && currentFocused.isFocused() && isOffScreen(currentFocused)) {
            int descendantFocusability = getDescendantFocusability();
            setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_1Q);
            requestFocus();
            setDescendantFocusability(descendantFocusability);
        }
        return true;
    }

    private boolean isOffScreen(View descendant) {
        return !isWithinDeltaOfScreen(descendant, 0, 0);
    }

    private boolean isWithinDeltaOfScreen(View descendant, int deltaX, int deltaY) {
        descendant.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(descendant, this.mTempRect);
        return this.mTempRect.right + deltaX >= getScrollX() && this.mTempRect.left - deltaX <= getScrollX() + getWidth() && this.mTempRect.bottom + deltaY >= getScrollY() && this.mTempRect.top - deltaY <= getScrollY() + getHeight();
    }

    private void doScroll(int deltaX, int deltaY) {
        if (deltaX != 0 || deltaY != 0) {
            if (this.mSmoothScrollingEnabled) {
                smoothScrollBy(deltaX, deltaY);
            } else {
                scrollBy(deltaX, deltaY);
            }
        }
    }

    public final void smoothScrollBy(int dx, int dy) {
        if (getChildCount() != 0) {
            if (AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll > 250) {
                int width = (getWidth() - getPaddingRight()) - getPaddingLeft();
                int height = (getHeight() - getPaddingBottom()) - getPaddingTop();
                int bottom = getChildAt(0).getHeight();
                int maxX = Math.max(0, getChildAt(0).getWidth() - width);
                int maxY = Math.max(0, bottom - height);
                int scrollX = getScrollX();
                int scrollY = getScrollY();
                this.mScroller.startScroll(scrollX, scrollY, Math.max(0, Math.min(scrollX + dx, maxX)) - scrollX, Math.max(0, Math.min(scrollY + dy, maxY)) - scrollY);
                if (VERSION.SDK_INT >= 16) {
                    postInvalidateOnAnimation();
                } else {
                    invalidate();
                }
            } else {
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                scrollBy(dx, dy);
            }
            this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
        }
    }

    public final void smoothScrollTo(int x, int y) {
        smoothScrollBy(x - getScrollX(), y - getScrollY());
    }

    protected int computeVerticalScrollRange() {
        int contentHeight = (getHeight() - getPaddingBottom()) - getPaddingTop();
        if (getChildCount() == 0) {
            return contentHeight;
        }
        int scrollRange = getChildAt(0).getBottom();
        int scrollY = getScrollY();
        int overscrollBottom = Math.max(0, scrollRange - contentHeight);
        if (scrollY < 0) {
            scrollRange -= scrollY;
        } else if (scrollY > overscrollBottom) {
            scrollRange += scrollY - overscrollBottom;
        }
        return scrollRange;
    }

    protected int computeVerticalScrollOffset() {
        return Math.max(0, super.computeVerticalScrollOffset());
    }

    protected int computeHorizontalScrollRange() {
        int contentWidth = (getWidth() - getPaddingLeft()) - getPaddingRight();
        if (getChildCount() == 0) {
            return contentWidth;
        }
        int scrollRange = getChildAt(0).getRight();
        int scrollX = getScrollX();
        int overscrollRight = Math.max(0, scrollRange - contentWidth);
        if (scrollX < 0) {
            scrollRange -= scrollX;
        } else if (scrollX > overscrollRight) {
            scrollRange += scrollX - overscrollRight;
        }
        return scrollRange;
    }

    protected int computeHorizontalScrollOffset() {
        return Math.max(0, super.computeHorizontalScrollOffset());
    }

    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        LayoutParams lp = child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight(), lp.width);
        child.measure(MeasureSpec.makeMeasureSpec(0, 0), getChildMeasureSpec(parentHeightMeasureSpec, getPaddingTop() + getPaddingBottom(), lp.height));
    }

    public void computeScroll() {
        boolean canOverscroll = true;
        if (this.mScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (!(oldX == x && oldY == y)) {
                int rangeX = getScrollRangeX();
                int rangeY = getScrollRangeY();
                int overscrollMode = getOverScrollMode();
                if (overscrollMode != 0 && (overscrollMode != 1 || (rangeX <= 0 && rangeY <= 0))) {
                    canOverscroll = false;
                }
                overScrollBy(x - oldX, y - oldY, oldX, oldY, rangeX, rangeY, this.mOverflingDistance, this.mOverflingDistance, false);
                onScrollChanged(getScrollX(), getScrollY(), oldX, oldY);
                if (canOverscroll) {
                    if (x < 0 && oldX >= 0) {
                        this.mEdgeGlowLeft.onAbsorb((int) this.mScroller.getCurrVelocity());
                    } else if (x > rangeX && oldX <= rangeX) {
                        this.mEdgeGlowRight.onAbsorb((int) this.mScroller.getCurrVelocity());
                    }
                    if (y < 0 && oldY >= 0) {
                        this.mEdgeGlowTop.onAbsorb((int) this.mScroller.getCurrVelocity());
                    } else if (y > rangeY && oldY <= rangeY) {
                        this.mEdgeGlowBottom.onAbsorb((int) this.mScroller.getCurrVelocity());
                    }
                }
            }
            if (!awakenScrollBars()) {
                if (VERSION.SDK_INT >= 16) {
                    postInvalidateOnAnimation();
                } else {
                    invalidate();
                }
            }
        }
    }

    private void scrollToChild(View child) {
        child.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(child, this.mTempRect);
        int scrollDeltaX = computeScrollDeltaXToGetChildRectOnScreen(this.mTempRect);
        int scrollDeltaY = computeScrollDeltaYToGetChildRectOnScreen(this.mTempRect);
        if (scrollDeltaX != 0 || scrollDeltaY != 0) {
            scrollBy(scrollDeltaX, scrollDeltaY);
        }
    }

    private boolean scrollToChildRect(Rect rect, boolean immediate) {
        boolean scrollX;
        int deltaX = computeScrollDeltaXToGetChildRectOnScreen(rect);
        int deltaY = computeScrollDeltaYToGetChildRectOnScreen(rect);
        if (deltaX != 0) {
            scrollX = true;
        } else {
            scrollX = false;
        }
        boolean scrollY;
        if (deltaY != 0) {
            scrollY = true;
        } else {
            scrollY = false;
        }
        if (scrollX || scrollY) {
            if (immediate) {
                scrollBy(deltaX, deltaY);
            } else {
                smoothScrollBy(deltaX, deltaY);
            }
        }
        if (scrollX || scrollY) {
            return true;
        }
        return false;
    }

    protected int computeScrollDeltaXToGetChildRectOnScreen(Rect rect) {
        if (getChildCount() == 0) {
            return 0;
        }
        int width = getWidth();
        int screenLeft = getScrollX();
        int screenRight = screenLeft + width;
        int fadingEdge = getHorizontalFadingEdgeLength();
        if (rect.left > 0) {
            screenLeft += fadingEdge;
        }
        if (rect.right < getChildAt(0).getWidth()) {
            screenRight -= fadingEdge;
        }
        int scrollDeltaX;
        if (rect.right > screenRight && rect.left > screenLeft) {
            if (rect.width() > width) {
                scrollDeltaX = 0 + (rect.left - screenLeft);
            } else {
                scrollDeltaX = 0 + (rect.right - screenRight);
            }
            return Math.min(scrollDeltaX, getChildAt(0).getRight() - screenRight);
        } else if (rect.left >= screenLeft || rect.right >= screenRight) {
            return 0;
        } else {
            if (rect.width() > width) {
                scrollDeltaX = 0 - (screenRight - rect.right);
            } else {
                scrollDeltaX = 0 - (screenLeft - rect.left);
            }
            return Math.max(scrollDeltaX, -getScrollX());
        }
    }

    protected int computeScrollDeltaYToGetChildRectOnScreen(Rect rect) {
        if (getChildCount() == 0) {
            return 0;
        }
        int height = getHeight();
        int screenTop = getScrollY();
        int screenBottom = screenTop + height;
        int fadingEdge = getVerticalFadingEdgeLength();
        if (rect.top > 0) {
            screenTop += fadingEdge;
        }
        if (rect.bottom < getChildAt(0).getHeight()) {
            screenBottom -= fadingEdge;
        }
        int scrollDeltaY;
        if (rect.bottom > screenBottom && rect.top > screenTop) {
            if (rect.height() > height) {
                scrollDeltaY = 0 + (rect.top - screenTop);
            } else {
                scrollDeltaY = 0 + (rect.bottom - screenBottom);
            }
            return Math.min(scrollDeltaY, getChildAt(0).getBottom() - screenBottom);
        } else if (rect.top >= screenTop || rect.bottom >= screenBottom) {
            return 0;
        } else {
            if (rect.height() > height) {
                scrollDeltaY = 0 - (screenBottom - rect.bottom);
            } else {
                scrollDeltaY = 0 - (screenTop - rect.top);
            }
            return Math.max(scrollDeltaY, -getScrollY());
        }
    }

    public void requestChildFocus(View child, View focused) {
        if (this.mIsLayoutDirty) {
            this.mChildToScrollTo = focused;
        } else {
            scrollToChild(focused);
        }
        super.requestChildFocus(child, focused);
    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        View nextFocus;
        if (previouslyFocusedRect == null) {
            nextFocus = FocusFinder.getInstance().findNextFocus(this, null, direction);
        } else {
            nextFocus = FocusFinder.getInstance().findNextFocusFromRect(this, previouslyFocusedRect, direction);
        }
        if (nextFocus == null || isOffScreen(nextFocus)) {
            return false;
        }
        return nextFocus.requestFocus(direction, previouslyFocusedRect);
    }

    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        rectangle.offset(child.getLeft() - child.getScrollX(), child.getTop() - child.getScrollY());
        return scrollToChildRect(rectangle, immediate);
    }

    public void requestLayout() {
        this.mIsLayoutDirty = true;
        super.requestLayout();
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mIsLayoutDirty = false;
        if (this.mChildToScrollTo != null && isViewDescendantOf(this.mChildToScrollTo, this)) {
            scrollToChild(this.mChildToScrollTo);
        }
        this.mChildToScrollTo = null;
        scrollTo(getScrollX(), getScrollY());
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        View currentFocused = findFocus();
        if (currentFocused != null && this != currentFocused && isWithinDeltaOfScreen(currentFocused, getRight() - getLeft(), getBottom() - getTop())) {
            currentFocused.getDrawingRect(this.mTempRect);
            offsetDescendantRectToMyCoords(currentFocused, this.mTempRect);
            doScroll(computeScrollDeltaXToGetChildRectOnScreen(this.mTempRect), computeScrollDeltaYToGetChildRectOnScreen(this.mTempRect));
        }
    }

    private static boolean isViewDescendantOf(View child, View parent) {
        if (child == parent) {
            return true;
        }
        ViewParent theParent = child.getParent();
        if ((theParent instanceof ViewGroup) && isViewDescendantOf((View) theParent, parent)) {
            return true;
        }
        return false;
    }

    public void scrollTo(int x, int y) {
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            x = clamp(x, (getWidth() - getPaddingRight()) - getPaddingLeft(), child.getWidth());
            y = clamp(y, (getHeight() - getPaddingBottom()) - getPaddingTop(), child.getHeight());
            if (x != getScrollX() || y != getScrollY()) {
                super.scrollTo(x, y);
                if (this.mOnScrollListener != null) {
                    this.mOnScrollListener.onScroll(x, y);
                }
            }
        }
    }

    public void setOverScrollMode(int mode) {
        if (mode == 2) {
            this.mEdgeGlowLeft = null;
            this.mEdgeGlowRight = null;
            this.mEdgeGlowTop = null;
            this.mEdgeGlowBottom = null;
        } else if (this.mEdgeGlowTop == null) {
            Context context = getContext();
            this.mEdgeGlowLeft = new EdgeEffect(context);
            this.mEdgeGlowRight = new EdgeEffect(context);
            this.mEdgeGlowTop = new EdgeEffect(context);
            this.mEdgeGlowBottom = new EdgeEffect(context);
        }
        super.setOverScrollMode(mode);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mEdgeGlowLeft != null) {
            int restoreCount;
            int height;
            int width;
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            if (!this.mEdgeGlowLeft.isFinished()) {
                restoreCount = canvas.save();
                height = (getHeight() - getPaddingTop()) - getPaddingBottom();
                canvas.rotate(270.0f);
                canvas.translate((float) ((-height) + getPaddingTop()), (float) Math.min(0, scrollX));
                this.mEdgeGlowLeft.setSize(height, getWidth());
                if (this.mEdgeGlowLeft.draw(canvas)) {
                    if (VERSION.SDK_INT >= 16) {
                        postInvalidateOnAnimation();
                    } else {
                        invalidate();
                    }
                }
                canvas.restoreToCount(restoreCount);
            }
            if (!this.mEdgeGlowRight.isFinished()) {
                restoreCount = canvas.save();
                width = getWidth();
                height = (getHeight() - getPaddingTop()) - getPaddingBottom();
                canvas.rotate(90.0f);
                canvas.translate((float) (-getPaddingTop()), (float) (-(Math.max(getScrollRangeX(), scrollX) + width)));
                this.mEdgeGlowRight.setSize(height, width);
                if (this.mEdgeGlowRight.draw(canvas)) {
                    if (VERSION.SDK_INT >= 16) {
                        postInvalidateOnAnimation();
                    } else {
                        invalidate();
                    }
                }
                canvas.restoreToCount(restoreCount);
            }
            if (!this.mEdgeGlowTop.isFinished()) {
                restoreCount = canvas.save();
                width = (getWidth() - getPaddingLeft()) - getPaddingRight();
                canvas.translate((float) getPaddingLeft(), (float) Math.min(0, scrollY));
                this.mEdgeGlowTop.setSize(width, getHeight());
                if (this.mEdgeGlowTop.draw(canvas)) {
                    if (VERSION.SDK_INT >= 16) {
                        postInvalidateOnAnimation();
                    } else {
                        invalidate();
                    }
                }
                canvas.restoreToCount(restoreCount);
            }
            if (!this.mEdgeGlowBottom.isFinished()) {
                restoreCount = canvas.save();
                width = (getWidth() - getPaddingLeft()) - getPaddingRight();
                height = getHeight();
                canvas.translate((float) ((-width) + getPaddingLeft()), (float) (Math.max(getScrollRangeY(), scrollY) + height));
                canvas.rotate(180.0f, (float) width, 0.0f);
                this.mEdgeGlowBottom.setSize(width, height);
                if (this.mEdgeGlowBottom.draw(canvas)) {
                    if (VERSION.SDK_INT >= 16) {
                        postInvalidateOnAnimation();
                    } else {
                        invalidate();
                    }
                }
                canvas.restoreToCount(restoreCount);
            }
        }
    }

    private static int clamp(int n, int my, int child) {
        if (my >= child || n < 0) {
            return 0;
        }
        if (my + n > child) {
            return child - my;
        }
        return n;
    }
}

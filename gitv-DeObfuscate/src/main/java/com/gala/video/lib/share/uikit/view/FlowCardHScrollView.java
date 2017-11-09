package com.gala.video.lib.share.uikit.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.common.widget.AnimatorEx;
import com.gala.video.lib.share.common.widget.ScrollHolder;
import com.gala.video.lib.share.ifimpl.ucenter.history.impl.HistoryInfoHelper;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiCommandHolder;
import com.gala.video.lib.share.uikit.view.widget.coverflow.KeyHandler;
import com.gala.video.lib.share.uikit.view.widget.coverflow.OnScrollListener;
import com.gala.video.lib.share.uikit.view.widget.coverflow.ScaleAnimatorUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.mcto.ads.internal.net.SendFlag;
import java.util.ArrayList;
import java.util.List;

public abstract class FlowCardHScrollView extends ViewGroup implements OnHierarchyChangeListener, OnFocusChangeListener {
    public static final boolean DBG = false;
    private static final int DEFAULT_MSG_DELAY = 5000;
    private static final int INDEXER_THRESHOLD = 12;
    public static final int INVISIBLE = 0;
    public static final int PARTIAL = 1;
    private static final float SCALE_2 = 1.1f;
    private static final float SCALE_3 = 1.15f;
    private static final String TAG = "FlowCardHScrollView";
    public static final int VISIBLE = 2;
    private static final int mIndexerPaddingBottom1 = ResourceUtil.getPx(20);
    private static final int mOffsetX = ResourceUtil.getPx(20);
    private static final int mOffsetY = ResourceUtil.getPx(7);
    private static final int mTopEdgeOffset = ResourceUtil.getPx(18);
    private MyAnimatorListener animatorListener = new MyAnimatorListener();
    private ScaleAnimatorUtil mAnimator;
    private boolean mCanScroll = false;
    private int mCenterX;
    private SparseIntArray mChildrenVisiblity = new SparseIntArray(12);
    private ArrayList<Drawable> mDrawables;
    private int mFocusIndex;
    private View mFocusTracker;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            FlowCardHScrollView.this.nextChildFocus(true);
            FlowCardHScrollView.this.sendDelayedMessage();
        }
    };
    private boolean mHeadIntersectable = false;
    private int mIntersection;
    private boolean mIsLayoutDirty = true;
    private KeyHandler mKeyHandler = new KeyHandler(3);
    private View mLastFocus;
    private OnKeyListener mOnKeyListener;
    private final int mScaleOutPx36 = ResourceUtil.getPx(36);
    private List<OnScrollListener> mScrollListeners = new ArrayList(2);
    private AnimatorEx mScroller;
    private ArrayList<View> mShowingViews = new ArrayList(5);

    class C18342 implements Runnable {
        C18342() {
        }

        public void run() {
            if (FlowCardHScrollView.this.mLastFocus != null) {
                FlowCardHScrollView.this.mLastFocus.requestFocus();
            }
        }
    }

    private class MyAnimatorListener implements AnimatorListener {
        private boolean canceled;

        private MyAnimatorListener() {
        }

        public void onAnimationStart(Animator animation) {
            if (animation == FlowCardHScrollView.this.mScroller) {
                FlowCardHScrollView.this.dispatchScrollState(true);
            } else {
                FlowCardHScrollView.this.invalidate();
            }
        }

        public void onAnimationEnd(Animator animation) {
            if (animation != FlowCardHScrollView.this.mScroller) {
                FlowCardHScrollView.this.invalidate();
            } else if (this.canceled) {
                this.canceled = false;
            } else {
                FlowCardHScrollView.this.dispatchScrollState(false);
            }
        }

        public void onAnimationCancel(Animator animation) {
            if (animation == FlowCardHScrollView.this.mScroller) {
                this.canceled = true;
            }
        }

        public void onAnimationRepeat(Animator animation) {
        }
    }

    protected abstract void invalidateFocus(View view, boolean z);

    protected abstract void onHideView(View view);

    public FlowCardHScrollView(Context context) {
        super(context);
        initView(context);
    }

    public FlowCardHScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        this.mScroller = new AnimatorEx(0.0f, 1.0f);
        this.mScroller.setDuration(250);
        this.mScroller.setInterpolator(new LinearInterpolator());
        this.mScroller.addListener(this.animatorListener);
        setChildrenDrawingOrderEnabled(true);
        setClipChildren(false);
        setClipToPadding(false);
        enableHeadIntersect(true);
        setFocusable(false);
        setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
        this.mAnimator = new ScaleAnimatorUtil();
        this.mAnimator.setDuration(250);
        this.mAnimator.setInterpolator(new LinearInterpolator());
        this.mAnimator.addListener(this.animatorListener);
        setOnHierarchyChangeListener(this);
        this.mDrawables = new ArrayList();
    }

    public void sendDelayedMessage() {
        this.mHandler.removeMessages(0);
        this.mHandler.sendEmptyMessageDelayed(0, IOpenApiCommandHolder.OAA_CONNECT_INTERVAL);
    }

    public void removeDelayedMessage() {
        this.mHandler.removeMessages(0);
    }

    public void setChildIntersectionLeftRight(int pixel) {
        this.mIntersection = pixel;
    }

    public int getChildIntersectionLeftRight() {
        return this.mIntersection;
    }

    public void enableHeadIntersect(boolean enable) {
        this.mHeadIntersectable = enable;
    }

    protected int getVirtualChildCount() {
        return getChildCount();
    }

    protected int getChildDrawingOrder(int childCount, int i) {
        int top = 1;
        if (this.mLastFocus != null) {
            top = this.mFocusIndex;
        } else if (hasFocus() && !isFocused()) {
            top = indexOfChild(getFocusedChild());
        }
        if (i < childCount - 2) {
            return (((top + i) + 2) + childCount) % childCount;
        }
        if (i == childCount - 2) {
            return ((top + 1) + childCount) % childCount;
        }
        if (i != childCount - 1) {
            return i;
        }
        return top;
    }

    private int getVirtualIndex(int i) {
        if (i < 0) {
            return getChildCount() - 1;
        }
        if (i > getChildCount() - 1) {
            return 0;
        }
        return i;
    }

    public View getVirtualChildAt(int i) {
        return getChildAt(getVirtualIndex(i));
    }

    private int virtualIndexOfChild(View child) {
        if (child == null) {
            return 1;
        }
        return indexOfChild(child);
    }

    protected int offsetToVirtualIndex(int index, int offset, int count) {
        if (index < 0 || index >= count) {
            return -1;
        }
        return ((index - offset) + count) % count;
    }

    protected int getNextLocationOffset(View child) {
        return 0;
    }

    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        LayoutParams lp = child.getLayoutParams();
        if (lp.width >= 0) {
            super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
            return;
        }
        child.measure(MeasureSpec.makeMeasureSpec(0, 0), getChildMeasureSpec(parentHeightMeasureSpec, getPaddingTop() + getPaddingBottom(), lp.height));
    }

    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        if (lp.width >= 0) {
            super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
            return;
        }
        child.measure(MeasureSpec.makeMeasureSpec(lp.leftMargin + lp.rightMargin, 0), getChildMeasureSpec(parentHeightMeasureSpec, (((getPaddingTop() + getPaddingBottom()) + lp.topMargin) + lp.bottomMargin) + heightUsed, lp.height));
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mTotalLength = 0;
        int maxHeight = 0;
        int childState = 0;
        int alternativeMaxHeight = 0;
        boolean allFillParent = true;
        int count = getVirtualChildCount();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        boolean matchHeight = false;
        boolean isExactly = widthMode == 1073741824;
        for (int i = 0; i < count; i++) {
            View child = getVirtualChildAt(i);
            if (!(child == null || child.getVisibility() == 8)) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                measureChildWithMargins(child, widthMeasureSpec, mTotalLength, heightMeasureSpec, 0);
                int childWidth = child.getMeasuredWidth();
                if (isExactly) {
                    mTotalLength += ((lp.leftMargin + childWidth) + lp.rightMargin) + getNextLocationOffset(child);
                } else {
                    int totalLength = mTotalLength;
                    mTotalLength = Math.max(totalLength, (((totalLength + childWidth) + lp.leftMargin) + lp.rightMargin) + getNextLocationOffset(child));
                }
                boolean matchHeightLocally = false;
                if (heightMode != 1073741824 && lp.height == -1) {
                    matchHeight = true;
                    matchHeightLocally = true;
                }
                int margin = lp.topMargin + lp.bottomMargin;
                int childHeight = child.getMeasuredHeight() + margin;
                childState = combineMeasuredStates(childState, child.getMeasuredState());
                maxHeight = Math.max(maxHeight, childHeight);
                allFillParent = allFillParent && lp.height == -1;
                if (!matchHeightLocally) {
                    margin = childHeight;
                }
                alternativeMaxHeight = Math.max(alternativeMaxHeight, margin);
            }
        }
        int widthSizeAndState = resolveSizeAndState(Math.max(mTotalLength + (getPaddingLeft() + getPaddingRight()), getSuggestedMinimumWidth()), widthMeasureSpec, 0);
        if (!(allFillParent || heightMode == 1073741824)) {
            maxHeight = alternativeMaxHeight;
        }
        setMeasuredDimension((-16777216 & childState) | widthSizeAndState, resolveSizeAndState(Math.max(maxHeight + (getPaddingTop() + getPaddingBottom()), getSuggestedMinimumHeight()), heightMeasureSpec, childState << 16));
        if (matchHeight) {
            forceUniformHeight(count, widthMeasureSpec);
        }
    }

    private void forceUniformHeight(int count, int widthMeasureSpec) {
        int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
        for (int i = 0; i < count; i++) {
            View child = getVirtualChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = child.getLayoutParams();
                if (lp.height == -1) {
                    int oldWidth = lp.width;
                    lp.width = child.getMeasuredWidth();
                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0);
                    lp.width = oldWidth;
                }
            }
        }
    }

    private void myonLayout(boolean changed, int left, int top, int right, int bottom) {
        int paddingTop = getPaddingTop();
        int childLeft = getPaddingLeft();
        int height = bottom - top;
        int paddingBottom = getPaddingBottom();
        int childBottom = height - paddingBottom;
        int childSpace = (height - paddingTop) - paddingBottom;
        int count = getVirtualChildCount();
        for (int i = 0; i < count; i++) {
            View child = getVirtualChildAt(i);
            if (!(child == null || child.getVisibility() == 8)) {
                int childTop;
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                int gravity = lp.gravity;
                if (gravity < 0) {
                    gravity = 48;
                }
                switch (gravity & HistoryInfoHelper.MSG_MERGE) {
                    case 16:
                        childTop = ((((childSpace - childHeight) / 2) + paddingTop) + lp.topMargin) - lp.bottomMargin;
                        break;
                    case 48:
                        childTop = paddingTop + lp.topMargin;
                        break;
                    case 80:
                        childTop = (childBottom - childHeight) - lp.bottomMargin;
                        break;
                    default:
                        childTop = paddingTop;
                        break;
                }
                childLeft += lp.leftMargin;
                child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
                childLeft += (lp.rightMargin + childWidth) + getNextLocationOffset(child);
                if (i == count - 1) {
                    boolean z;
                    if (child.getRight() + lp.rightMargin > (right - left) - getPaddingRight()) {
                        z = true;
                    } else {
                        z = false;
                    }
                    this.mCanScroll = z;
                }
            }
        }
    }

    public final void setY(float y) {
    }

    public float getY() {
        return (float) (getTop() + mTopEdgeOffset);
    }

    protected boolean addViewInLayout(View child, int index, LayoutParams params) {
        if (child.getParent() != null) {
            throw new IllegalStateException("The specified child already has a parent. You must call removeView() on the child's parent first.");
        }
        if (params instanceof MarginLayoutParams) {
            MarginLayoutParams lp = (MarginLayoutParams) params;
            if (getChildCount() != 0 || this.mHeadIntersectable) {
                lp.leftMargin -= this.mIntersection;
                lp.rightMargin -= this.mIntersection;
            } else {
                lp.rightMargin -= this.mIntersection;
            }
        }
        return super.addViewInLayout(child, -1, params);
    }

    public void addView(View child, LayoutParams params) {
        if (params instanceof MarginLayoutParams) {
            MarginLayoutParams lp = (MarginLayoutParams) params;
            if (getChildCount() != 0 || this.mHeadIntersectable) {
                lp.leftMargin -= this.mIntersection;
                lp.rightMargin -= this.mIntersection;
            } else {
                lp.rightMargin -= this.mIntersection;
            }
        }
        super.addView(child, params);
    }

    public final boolean canScroll() {
        return this.mCanScroll;
    }

    public boolean isQuickScroll() {
        return this.mKeyHandler.isKeyLongPress();
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (!hasFocus()) {
            views.add(this);
        } else if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                if (view != null && view.isShown()) {
                    view.addFocusables(views, direction, focusableMode);
                }
            }
        }
    }

    public void bringChildToFront(View child) {
        invalidate();
    }

    private boolean executeKeyEvent(KeyEvent event) {
        if (this.mKeyHandler.executeKeyEvent(event)) {
            return true;
        }
        if (event.getAction() != 0) {
            return false;
        }
        switch (event.getKeyCode()) {
            case 21:
                return arrowScroll(17);
            case 22:
                return arrowScroll(66);
            default:
                return false;
        }
    }

    private boolean arrowScroll(int direction) {
        if (this.mIsLayoutDirty) {
            Log.d(TAG, "layout is dirty, skip scroll this time");
            return false;
        }
        View currentFocused = getFocusedChild();
        if (this.mLastFocus != null) {
            currentFocused = this.mLastFocus;
        }
        if (arrowScroll(direction, currentFocused, false) != null) {
            return true;
        }
        return false;
    }

    private View arrowScroll(int direction, View currentChild, boolean preventFocus) {
        View nextFocused = getVirtualChildAt(virtualIndexOfChild(currentChild) + (direction == 17 ? -1 : 1));
        if (nextFocused == null) {
            return null;
        }
        if (!preventFocus && !nextFocused.requestFocus(direction)) {
            return null;
        }
        calcChildLayout(nextFocused);
        doScrollX(computeScrollDelta(nextFocused));
        return nextFocused;
    }

    private int getCenterX() {
        return (((getWidth() - getPaddingLeft()) - getPaddingRight()) / 2) + getPaddingLeft();
    }

    private int getCenterXOfView(View childView) {
        return childView.getLeft() + (childView.getWidth() / 2);
    }

    private int computeScrollDelta(View focused) {
        if (getChildCount() == 0 || focused == null) {
            return 0;
        }
        cancelScroll();
        return (getCenterXOfView(focused) - this.mCenterX) - getScrollX();
    }

    private void cancelScroll() {
        if (this.mScroller != null && this.mScroller.isRunning()) {
            this.animatorListener.canceled = true;
            this.mScroller.end();
        }
    }

    protected void stopScroll() {
        if (this.mScroller != null && this.mScroller.isRunning()) {
            this.mScroller.end();
        }
    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (this.mLastFocus != null) {
            return this.mLastFocus.requestFocus(direction, previouslyFocusedRect);
        }
        if (getChildCount() > 0) {
            return getChildAt(0).requestFocus(direction, previouslyFocusedRect);
        }
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    private void doScrollX(int delta) {
        if (delta != 0) {
            smoothScrollBy(delta);
        }
    }

    private void smoothScrollBy(int dx) {
        if (getChildCount() != 0) {
            if (this.mScroller.isRunning()) {
                this.mScroller.cancel();
            }
            int scrollX = getScrollX();
            this.mScroller.addAnimator(new ScrollHolder(this, true, scrollX, scrollX + dx));
            this.mScroller.start();
        }
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LinearLayout.LayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LinearLayout.LayoutParams(-1, -2);
    }

    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new LinearLayout.LayoutParams(p);
    }

    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof LinearLayout.LayoutParams;
    }

    private void addDrawable(Drawable drawable) {
        if (this.mDrawables == null) {
            this.mDrawables = new ArrayList();
        }
        if (!this.mDrawables.contains(drawable)) {
            this.mDrawables.add(drawable);
            drawable.setCallback(this);
        }
    }

    private void removeDrawable(Drawable drawable) {
        if (this.mDrawables != null) {
            this.mDrawables.remove(drawable);
            invalidate(drawable.getBounds());
            drawable.setCallback(null);
        }
    }

    private void removeDrawable(int index) {
        if (index >= 0 && this.mDrawables != null && index < this.mDrawables.size()) {
            Drawable d = (Drawable) this.mDrawables.get(index);
            this.mDrawables.remove(index);
            invalidate(d.getBounds());
            d.setCallback(null);
        }
    }

    private void clearDrawable() {
        if (this.mDrawables != null) {
            for (int i = this.mDrawables.size() - 1; i >= 0; i--) {
                removeDrawable((Drawable) this.mDrawables.get(i));
            }
        }
    }

    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        int sx = getScrollX();
        int left = getPaddingLeft() + sx;
        int right = (getWidth() - getPaddingRight()) + sx;
        int top = -this.mScaleOutPx36;
        int bottom = getHeight() + this.mScaleOutPx36;
        int restoreCount = canvas.save();
        canvas.clipRect(left, top, right, bottom);
        boolean canSee = true;
        int curIndex = indexOfChild(child);
        int nextFocusIndex = this.mFocusIndex;
        int leftOne = nextFocusIndex - 1;
        int rightOne = nextFocusIndex + 1;
        if (nextFocusIndex == 0) {
            leftOne = getChildCount() - 1;
        } else if (nextFocusIndex == getChildCount() - 1) {
            rightOne = 0;
        }
        int leftTwo = leftOne - 1;
        int rightTwo = rightOne + 1;
        if (leftOne == 0) {
            leftTwo = getChildCount() - 1;
        } else if (rightOne == getChildCount() - 1) {
            rightTwo = 0;
        }
        if (!(curIndex == leftOne || curIndex == rightOne || curIndex == nextFocusIndex || curIndex == leftTwo || curIndex == rightTwo)) {
            canSee = false;
        }
        boolean needInvalidate = canSee && super.drawChild(canvas, child, drawingTime);
        canvas.restoreToCount(restoreCount);
        return needInvalidate;
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        View child = this.mLastFocus;
        if (child != null && !this.mIsLayoutDirty && getChildCount() <= 12) {
            Animation animation = this.mLastFocus.getAnimation();
            boolean isShakeRunning;
            if (!(animation instanceof TranslateAnimation) || animation.hasEnded()) {
                isShakeRunning = false;
            } else {
                isShakeRunning = true;
            }
            if (!this.mScroller.isRunning() && !this.mAnimator.isRunning() && !isShakeRunning) {
                int sx = child.getRight();
                int sy = child.getBottom() - mIndexerPaddingBottom1;
                if (child.hasFocus()) {
                    sx += mOffsetX;
                    sy += mOffsetY;
                }
                canvas.translate((float) sx, (float) sy);
                int numDrawables = this.mDrawables == null ? 0 : this.mDrawables.size();
                for (int i = 0; i < numDrawables; i++) {
                    ((Drawable) this.mDrawables.get(i)).draw(canvas);
                }
                canvas.translate((float) (-sx), (float) (-sy));
            }
        }
    }

    public void onChildViewAdded(View parent, View child) {
        this.mIsLayoutDirty = true;
        this.mChildrenVisiblity.put(child.hashCode(), 0);
        addDrawable(getResources().getDrawable(C1632R.drawable.share_coverflow_selector));
        child.setOnFocusChangeListener(this);
    }

    public void onChildViewRemoved(View parent, View child) {
        if (child == this.mLastFocus) {
            Log.d(TAG, "last focus removed");
            this.mLastFocus = null;
        }
        this.mIsLayoutDirty = true;
        this.mChildrenVisiblity.delete(child.hashCode());
        removeDrawable(this.mDrawables.size() - 1);
    }

    protected void layoutOnLoop() {
        myonLayout(false, getLeft(), getTop(), getRight(), getBottom());
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        myonLayout(changed, l, t, r, b);
        boolean oldlayoutdirty = this.mIsLayoutDirty;
        this.mIsLayoutDirty = false;
        if (changed) {
            this.mCenterX = getCenterX();
        }
        int c = getChildCount();
        if (c != 0) {
            View lastFocusView = this.mLastFocus;
            if (oldlayoutdirty || this.mLastFocus == null) {
                View v = this.mLastFocus;
                if (v == null) {
                    setScrollX(0);
                    v = getFocusedChild();
                }
                if (v == null) {
                    v = getVirtualChildAt(1);
                }
                this.mLastFocus = v;
                this.mFocusIndex = indexOfChild(this.mLastFocus);
            }
            int centerWidth = this.mLastFocus.getWidth();
            this.mLastFocus.setLeft(((getWidth() / 2) - (centerWidth / 2)) + getScrollX());
            this.mLastFocus.setRight(this.mLastFocus.getLeft() + centerWidth);
            invalidateFocus(this.mLastFocus, this.mLastFocus.isFocused());
            calcChildLayout(this.mLastFocus);
            setScrollX(getCenterXOfView(this.mLastFocus) - this.mCenterX);
            resetChildrenVisiblity();
            if (oldlayoutdirty && c <= 12) {
                int right = 0;
                for (int i = c - 1; i >= 0; i--) {
                    Drawable d = (Drawable) this.mDrawables.get(i);
                    d.setBounds(right - d.getIntrinsicWidth(), 0, right, d.getIntrinsicHeight());
                    right -= d.getIntrinsicWidth();
                }
                resetIndexer();
            }
            if (!hasFocus()) {
                if (!(lastFocusView == null || lastFocusView == this.mLastFocus)) {
                    lastFocusView.setScaleX(1.0f);
                    lastFocusView.setScaleY(1.0f);
                }
                this.mLastFocus.setScaleX(1.1f);
                this.mLastFocus.setScaleY(1.1f);
            }
        }
    }

    private void resetIndexer() {
        int index = offsetToVirtualIndex(this.mFocusIndex, 1, getChildCount());
        int i = this.mDrawables.size() - 1;
        while (i >= 0) {
            ((Drawable) this.mDrawables.get(i)).setState(i == index ? View.ENABLED_SELECTED_STATE_SET : View.ENABLED_STATE_SET);
            i--;
        }
    }

    private void resetChildrenVisiblity() {
        int temp = getChildCount();
        for (int i = 0; i < temp; i++) {
            this.mChildrenVisiblity.put(getChildAt(i).hashCode(), 0);
        }
        dispatchChildrenVisiblity(true);
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (v == this.mLastFocus) {
            if (hasFocus) {
                resetIndexer();
                if (this.mAnimator.isRunning()) {
                    this.mAnimator.end();
                }
                this.mAnimator.zoomAnimation(v, true, 1.1f, SCALE_3, true);
                this.mAnimator.start();
            } else {
                if (this.mAnimator.isRunning()) {
                    this.mAnimator.end();
                }
                this.mAnimator.zoomAnimation(v, false, v.getScaleX(), v != this.mFocusTracker ? 1.1f : 1.0f, true);
                this.mAnimator.start();
            }
            invalidateFocus(v, hasFocus);
        }
    }

    private boolean nextChildFocus(boolean right) {
        if (this.mIsLayoutDirty) {
            Log.d(TAG, "layout is dirty, skip auto scroll");
            return false;
        } else if (this.mScroller.isRunning() || this.mAnimator.isRunning() || this.mLastFocus == null) {
            return false;
        } else {
            View next = arrowScroll(right ? 66 : 17, this.mLastFocus, true);
            if (next == null) {
                return false;
            }
            if (hasFocus()) {
                next.requestFocus();
            } else {
                View v = this.mLastFocus;
                this.mLastFocus = next;
                this.mFocusIndex = indexOfChild(this.mLastFocus);
                resetIndexer();
                if (this.mAnimator.isRunning()) {
                    this.mAnimator.end();
                }
                this.mAnimator.zoomAnimation(v, false, v.getScaleX(), 1.0f, true);
                v = this.mLastFocus;
                this.mAnimator.zoomAnimation(v, false, v.getScaleX(), 1.1f, true);
                this.mAnimator.start();
            }
            return true;
        }
    }

    public void setOnKeyListener(OnKeyListener l) {
        this.mOnKeyListener = l;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            sendDelayedMessage();
        }
        if ((this.mOnKeyListener != null && this.mOnKeyListener.onKey(this, event.getKeyCode(), event)) || super.dispatchKeyEvent(event) || executeKeyEvent(event)) {
            return true;
        }
        return false;
    }

    private void calcChildLayout(View focused) {
        int nextFocusIndex = indexOfChild(focused);
        int leftone = nextFocusIndex - 1;
        int rightone = nextFocusIndex + 1;
        if (nextFocusIndex == 0) {
            leftone = getChildCount() - 1;
        } else if (nextFocusIndex == getChildCount() - 1) {
            rightone = 0;
        }
        int leftTwo = leftone - 1;
        int rightTwo = rightone + 1;
        if (leftone == 0) {
            leftTwo = getChildCount() - 1;
        } else if (rightone == getChildCount() - 1) {
            rightTwo = 0;
        }
        View left1View = getChildAt(leftone);
        View right1View = getChildAt(rightone);
        if (left1View != null) {
            int left1Width = left1View.getWidth();
            left1View.setLeft((getCenterXOfView(focused) - (getWidth() / 2)) + getPaddingLeft());
            left1View.setRight(left1View.getLeft() + left1Width);
        }
        if (right1View != null) {
            int right1Width = right1View.getWidth();
            right1View.setRight((getCenterXOfView(focused) + (getWidth() / 2)) - getPaddingRight());
            right1View.setLeft(right1View.getRight() - right1Width);
        }
        View left2View = getChildAt(leftTwo);
        View right2View = getChildAt(rightTwo);
        if (left2View != null) {
            int left2Width = left2View.getWidth();
            left2View.setRight(left1View.getLeft());
            left2View.setLeft(left2View.getRight() - left2Width);
        }
        if (right2View != null) {
            int right2Width = right2View.getWidth();
            right2View.setLeft(right1View.getRight());
            right2View.setRight(right2View.getLeft() + right2Width);
        }
        ArrayList<View> oldShowingViews = (ArrayList) this.mShowingViews.clone();
        this.mShowingViews.clear();
        this.mShowingViews.add(left1View);
        this.mShowingViews.add(focused);
        this.mShowingViews.add(right1View);
        this.mShowingViews.add(right2View);
        this.mShowingViews.add(left2View);
        for (int i = 0; i < oldShowingViews.size(); i++) {
            View view = (View) oldShowingViews.get(i);
            if (!this.mShowingViews.contains(view)) {
                onHideView(view);
            }
        }
    }

    protected ArrayList<View> getShowingViewList() {
        return this.mShowingViews;
    }

    public void requestChildFocus(View child, View focused) {
        boolean reset = false;
        if (!(getFocusedChild() != null || this.mLastFocus == null || child == this.mLastFocus)) {
            reset = true;
        }
        if (reset) {
            this.mLastFocus.post(new C18342());
            super.requestChildFocus(child, focused);
            return;
        }
        this.mFocusTracker = this.mLastFocus;
        super.requestChildFocus(child, focused);
        this.mFocusTracker = null;
        this.mLastFocus = child;
        this.mFocusIndex = indexOfChild(this.mLastFocus);
    }

    protected int getFocusIndex() {
        return this.mFocusIndex;
    }

    private void dispatchChildrenVisiblity(boolean isLayout) {
        int visiblityCount = this.mChildrenVisiblity.size();
        int left = getPaddingLeft() + getScrollX();
        int right = (getWidth() - getPaddingRight()) + getScrollX();
        for (int i = 0; i < visiblityCount; i++) {
            int visibility;
            int size;
            int j;
            View v = getChildAt(i);
            if (v.getRight() <= left || v.getLeft() >= right) {
                visibility = 0;
            } else if (v.getLeft() < left || v.getRight() > right) {
                visibility = 1;
            } else {
                visibility = 2;
            }
            if (isLayout) {
                size = this.mScrollListeners.size();
                for (j = 0; j < size; j++) {
                    ((OnScrollListener) this.mScrollListeners.get(j)).onLayoutChanged(v, i, visibility);
                }
            }
            int old = this.mChildrenVisiblity.get(v.hashCode());
            if (visibility != old) {
                this.mChildrenVisiblity.put(v.hashCode(), visibility);
                size = this.mScrollListeners.size();
                for (j = 0; j < size; j++) {
                    ((OnScrollListener) this.mScrollListeners.get(j)).onChildVisibilityChange(v, i, visibility, old);
                }
            }
        }
    }

    protected int getChildViewVisibility(View v) {
        if (v == null) {
            return 0;
        }
        int left = getPaddingLeft() + getScrollX();
        int right = (getWidth() - getPaddingRight()) + getScrollX();
        if (v.getRight() <= left || v.getLeft() >= right) {
            return 0;
        }
        if (v.getLeft() < left || v.getRight() > right) {
            return 1;
        }
        return 2;
    }

    private void dispatchScrollState(boolean start) {
        int size = this.mScrollListeners.size();
        if (size > 0) {
            if (!start) {
                dispatchChildrenVisiblity(false);
            }
            for (int i = 0; i < size; i++) {
                ((OnScrollListener) this.mScrollListeners.get(i)).onScrollStateChanged(start);
            }
        }
    }

    protected void addOnScrollListener(OnScrollListener listener) {
        if (!this.mScrollListeners.contains(listener)) {
            this.mScrollListeners.add(listener);
        }
    }

    protected void removeOnScrollListener(OnScrollListener listener) {
        this.mScrollListeners.remove(listener);
    }
}

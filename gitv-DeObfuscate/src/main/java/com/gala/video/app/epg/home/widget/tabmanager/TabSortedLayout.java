package com.gala.video.app.epg.home.widget.tabmanager;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.Scroller;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import com.gala.video.app.epg.C0508R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.ViewUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.cybergarage.soap.SOAP;

public class TabSortedLayout extends TableRow implements OnFocusChangeListener, OnClickListener {
    private static final int DURATION_SCROLL_TAB = 150;
    private static final long DURATION_SWAP_TAB = 200;
    private static final int STATUS_TAB_MOVING = 1;
    private static final int STATUS_TAB_MOVING_FINISHED = 2;
    private static final int STATUS_TAB_STATIC = 0;
    private static final int TAB_COUNT_ONE_LINE = 8;
    private static final String TAG = "widget/TabSortedLayout";
    private int isAnimationLeftFinished = 0;
    private int isAnimationRightFinished = 0;
    private TabSortedAdapter mAdapter;
    private List<TabSortedItemView> mChildViewList = new ArrayList();
    private Context mContext;
    private TabSortedItemView mCurrentFocusedView;
    private boolean mIsActivatedState = false;
    private boolean mIsAddItemTabFromAlternativeList = false;
    private boolean mIsFirstShow = true;
    private boolean mIsRemoveItemTabFromAlternativeList = false;
    private boolean mIsShowGuideIndicator = true;
    private TabSortedItemView mLastFocusView = null;
    private long mLastSpringBackAnimationTime = 0;
    private long mLastYSpringBackAnimationTime = 0;
    private List<TabSortedItemView> mOldChildViewList = new ArrayList();
    private Scroller mScroller;
    private TabSortedActivatedListener mTabSortedActivatedListener;
    private TabSortedMovingListener mTabSortedMovingListener;
    private int maxUnsortableChildIndex = -1;
    private int minSortableChildIndex = -1;

    public TabSortedLayout(Context context) {
        super(context);
        init(context);
    }

    public TabSortedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public View getVirtualChildAt(int i) {
        return (View) this.mChildViewList.get(i);
    }

    public int getVirtualChildCount() {
        return this.mAdapter != null ? this.mAdapter.getCount() : 0;
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (hasFocus()) {
            super.addFocusables(views, direction, focusableMode);
        } else {
            views.add(this);
        }
    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (this.mLastFocusView != null) {
            return this.mLastFocusView.requestFocus(direction, previouslyFocusedRect);
        }
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    public void setLastFocusView(TabSortedItemView itemView) {
        this.mLastFocusView = itemView;
    }

    private void init(Context context) {
        setOrientation(0);
        this.mContext = context;
        this.mScroller = new Scroller(context, new AccelerateDecelerateInterpolator());
        setChildrenDrawingOrderEnabled(true);
    }

    public void setMovingListener(TabSortedMovingListener tabSortedMovingListener) {
        this.mTabSortedMovingListener = tabSortedMovingListener;
    }

    public void setActivatedListener(TabSortedActivatedListener tabSortedActivatedListener) {
        this.mTabSortedActivatedListener = tabSortedActivatedListener;
    }

    public void setAdapter(TabSortedAdapter adapter) {
        this.mAdapter = adapter;
        int length = this.mAdapter.getCount();
        this.maxUnsortableChildIndex = adapter.getMaxUnsortableTabIndex();
        this.minSortableChildIndex = adapter.getMinSortableTabIndex();
        for (int i = 0; i < length; i++) {
            TabSortedItemView childView = this.mAdapter.getView(i, null, null);
            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
            if (childView.getLayoutParams() == null) {
                layoutParams = new LayoutParams(-2, -2);
            }
            layoutParams.leftMargin = this.mContext.getResources().getDimensionPixelSize(C0508R.dimen.dimen_022dp);
            layoutParams.rightMargin = this.mContext.getResources().getDimensionPixelSize(C0508R.dimen.dimen_022dp);
            layoutParams.width = this.mContext.getResources().getDimensionPixelSize(C0508R.dimen.dimen_196dp);
            layoutParams.height = this.mContext.getResources().getDimensionPixelSize(C0508R.dimen.dimen_121dp);
            childView.setId(ViewUtils.generateViewId());
            addView(childView, layoutParams);
            this.mChildViewList.add(childView);
            this.mOldChildViewList.add(childView);
            if (i <= this.maxUnsortableChildIndex) {
                childView.setLocked(true);
                childView.setFocusable(false);
                childView.setClickable(false);
            } else {
                childView.setOnFocusChangeListener(this);
                childView.setOnClickListener(this);
            }
        }
        updateChildrenFocusList();
    }

    public TabSortedAdapter getAdapter() {
        return this.mAdapter;
    }

    public void bringChildToFront(View child) {
        invalidate();
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (this.mIsAddItemTabFromAlternativeList) {
            smoothScrollToEnd();
            this.mIsAddItemTabFromAlternativeList = false;
        }
        if (this.mIsRemoveItemTabFromAlternativeList) {
            smoothScrollToEnd();
            this.mIsRemoveItemTabFromAlternativeList = false;
        }
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

    private void smoothScrollBy(int deltaX) {
        this.mScroller.startScroll(getScrollX(), this.mScroller.getFinalY(), deltaX, 0, 150);
        invalidate();
    }

    private void smoothScrollTo(int scrollX) {
        this.mScroller.startScroll(getScrollX(), this.mScroller.getFinalY(), scrollX - getScrollX(), 0, 150);
        invalidate();
    }

    private void scrollBy(int deltaX) {
        this.mScroller.startScroll(getScrollX(), this.mScroller.getFinalY(), deltaX, 0, 0);
        invalidate();
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            postInvalidate();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        dealWithSpringBackAnimation(this.mCurrentFocusedView, event);
        if (dealWithBackKeyEvent(this.mCurrentFocusedView, event)) {
            return true;
        }
        if (this.mIsActivatedState && event.getAction() == 0 && event.getKeyCode() == 22) {
            return swapTabsOnRightKeyDown();
        }
        if (this.mIsActivatedState && event.getAction() == 0 && event.getKeyCode() == 21) {
            return swapTabsOnLeftKeyDown();
        }
        return super.dispatchKeyEvent(event);
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (this.mIsFirstShow) {
                scrollBy(computeScrollDelta(v));
                this.mIsFirstShow = false;
            } else {
                smoothScrollBy(computeScrollDelta(v));
            }
            this.mCurrentFocusedView = (TabSortedItemView) v;
            this.mLastFocusView = this.mCurrentFocusedView;
            if (!this.mIsActivatedState) {
                ((TabSortedItemView) v).updateStatus(TabSortedState.FOCUS);
                return;
            }
            return;
        }
        ((TabSortedItemView) v).updateStatus(TabSortedState.NORMAL);
        if (this.mTabSortedActivatedListener != null && this.mIsActivatedState) {
            this.mTabSortedActivatedListener.cancelActivation((TabSortedItemView) v);
        }
        if (this.mIsActivatedState) {
            this.mIsActivatedState = false;
        }
    }

    public void onClick(View v) {
        if (v instanceof TabSortedItemView) {
            this.mIsActivatedState = !this.mIsActivatedState;
            TabSortedItemView tabSortedItemView = (TabSortedItemView) v;
            if (this.mIsActivatedState && this.mTabSortedActivatedListener != null) {
                this.mTabSortedActivatedListener.startActivation(tabSortedItemView);
            }
            if (!(this.mIsActivatedState || this.mTabSortedActivatedListener == null)) {
                this.mTabSortedActivatedListener.cancelActivation(tabSortedItemView);
            }
            this.mAdapter.onClick(tabSortedItemView, this.mChildViewList.indexOf(tabSortedItemView));
        }
    }

    private boolean swapTabsOnRightKeyDown() {
        if (this.isAnimationRightFinished == 1 || this.isAnimationLeftFinished == 1) {
            return true;
        }
        int curIndex = this.mChildViewList.indexOf(this.mCurrentFocusedView);
        if (curIndex < 0) {
            return true;
        }
        int nextIndex = curIndex + 1;
        if (nextIndex >= this.mChildViewList.size()) {
            return true;
        }
        final TabSortedItemView nextRightView = (TabSortedItemView) this.mChildViewList.get(nextIndex);
        final float offset = nextRightView.getX() - this.mCurrentFocusedView.getX();
        LogUtils.m1568d(TAG, "nextChildView.getX() - curFocusVieX.getX() : " + offset);
        ObjectAnimator animatorR = ObjectAnimator.ofFloat(this.mCurrentFocusedView, "TranslationX", new float[]{this.mCurrentFocusedView.getTranslationX() + offset});
        animatorR.setDuration(DURATION_SWAP_TAB);
        animatorR.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                TabSortedLayout.this.isAnimationRightFinished = 1;
            }

            public void onAnimationEnd(Animator animation) {
                TabSortedLayout.this.isAnimationRightFinished = 2;
                TabSortedLayout.this.smoothScrollBy(TabSortedLayout.this.computeScrollDelta(TabSortedLayout.this.mCurrentFocusedView));
                TabSortedLayout.this.updateChildActivatedState(TabSortedLayout.this.mCurrentFocusedView);
                if (TabSortedLayout.this.mTabSortedMovingListener != null) {
                    TabSortedLayout.this.mTabSortedMovingListener.moveForward(TabSortedLayout.this.mCurrentFocusedView.getData());
                }
                TabSortedLayout.this.mCurrentFocusedView.setX(TabSortedLayout.this.mCurrentFocusedView.getX() - offset);
                TabSortedLayout.this.requestLayout();
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        animatorR.start();
        ObjectAnimator animatorL = ObjectAnimator.ofFloat(nextRightView, "TranslationX", new float[]{nextRightView.getTranslationX() - offset});
        animatorL.setDuration(DURATION_SWAP_TAB);
        animatorL.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                TabSortedLayout.this.isAnimationLeftFinished = 1;
            }

            public void onAnimationEnd(Animator animation) {
                TabSortedLayout.this.isAnimationLeftFinished = 2;
                nextRightView.setX(nextRightView.getX() + offset);
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        animatorL.start();
        Collections.swap(this.mChildViewList, curIndex, nextIndex);
        updateChildrenFocusList();
        return true;
    }

    private boolean swapTabsOnLeftKeyDown() {
        if (this.isAnimationRightFinished == 1 || this.isAnimationLeftFinished == 1) {
            return true;
        }
        int curIndex = this.mChildViewList.indexOf(this.mCurrentFocusedView);
        if (curIndex < 0) {
            return true;
        }
        int preIndex = curIndex - 1;
        if (preIndex >= this.mChildViewList.size() || preIndex < 0) {
            return true;
        }
        final TabSortedItemView nextLeftView = (TabSortedItemView) this.mChildViewList.get(preIndex);
        if (nextLeftView.isLocked()) {
            return true;
        }
        final float offset = this.mCurrentFocusedView.getX() - nextLeftView.getX();
        LogUtils.m1568d(TAG, "nextChildView.getX() - curFocusVieX.getX() : " + offset);
        ObjectAnimator animatorL = ObjectAnimator.ofFloat(this.mCurrentFocusedView, "TranslationX", new float[]{this.mCurrentFocusedView.getTranslationX() - offset});
        animatorL.setDuration(DURATION_SWAP_TAB);
        animatorL.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                TabSortedLayout.this.isAnimationLeftFinished = 1;
            }

            public void onAnimationEnd(Animator animation) {
                TabSortedLayout.this.isAnimationLeftFinished = 2;
                TabSortedLayout.this.smoothScrollBy(TabSortedLayout.this.computeScrollDelta(TabSortedLayout.this.mCurrentFocusedView));
                TabSortedLayout.this.updateChildActivatedState(TabSortedLayout.this.mCurrentFocusedView);
                if (TabSortedLayout.this.mTabSortedMovingListener != null) {
                    TabSortedLayout.this.mTabSortedMovingListener.moveBackward(TabSortedLayout.this.mCurrentFocusedView.getData());
                }
                TabSortedLayout.this.mCurrentFocusedView.setX(TabSortedLayout.this.mCurrentFocusedView.getX() + offset);
                TabSortedLayout.this.requestLayout();
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        animatorL.start();
        ObjectAnimator animatorR = ObjectAnimator.ofFloat(nextLeftView, "TranslationX", new float[]{nextLeftView.getTranslationX() + offset});
        animatorR.setDuration(DURATION_SWAP_TAB);
        animatorR.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                TabSortedLayout.this.isAnimationRightFinished = 1;
            }

            public void onAnimationEnd(Animator animation) {
                TabSortedLayout.this.isAnimationRightFinished = 2;
                nextLeftView.setX(nextLeftView.getX() - offset);
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        animatorR.start();
        Collections.swap(this.mChildViewList, curIndex, preIndex);
        updateChildrenFocusList();
        return true;
    }

    private int getCenterXOfView(View v) {
        return (((int) v.getX()) + (v.getWidth() / 2)) - getScrollX();
    }

    private int computeScrollDelta(View focused) {
        if (getChildCount() <= 8 || focused == null) {
            return 0;
        }
        View end = (View) this.mChildViewList.get(getChildCount() - 1);
        return Math.max(Math.min(getCenterXOfView(focused) - (getWidth() / 2), ((((int) end.getX()) + end.getWidth()) - getScrollX()) - getWidth()), -getScrollX());
    }

    private void smoothScrollToEnd() {
        if (getChildCount() <= 8) {
            smoothScrollTo(0);
            return;
        }
        View end = getVirtualChildAt(getChildCount() - 1);
        int right = end.getRight();
        int parentWidth = getWidth();
        int scrollX = getScrollX();
        int margin = this.mContext.getResources().getDimensionPixelSize(C0508R.dimen.dimen_17dp);
        if (this.mIsRemoveItemTabFromAlternativeList) {
            if ((right + margin) - scrollX < parentWidth) {
                smoothScrollBy((end.getRight() - getWidth()) - getScrollX());
            }
        } else if (this.mIsAddItemTabFromAlternativeList) {
            smoothScrollBy((end.getRight() - getWidth()) - getScrollX());
        }
    }

    private void updateChildrenFocusList() {
        int length = this.mChildViewList.size();
        for (int i = 0; i < length; i++) {
            View curView = (View) this.mChildViewList.get(i);
            int preIndex = i - 1;
            int nextIndex = i + 1;
            if (preIndex < 0 || preIndex <= this.maxUnsortableChildIndex) {
                curView.setNextFocusLeftId(curView.getId());
            } else {
                curView.setNextFocusLeftId(((TabSortedItemView) this.mChildViewList.get(preIndex)).getId());
            }
            if (nextIndex > length - 1) {
                curView.setNextFocusRightId(curView.getId());
            } else {
                curView.setNextFocusRightId(((TabSortedItemView) this.mChildViewList.get(nextIndex)).getId());
            }
        }
    }

    private void updateChildActivatedState(View v) {
        if (this.mChildViewList.indexOf(this.mCurrentFocusedView) == this.minSortableChildIndex) {
            ((TabSortedItemView) v).updateStatus(TabSortedState.FOCUS_ACTIVATED_ARROW_RIGHT);
        } else if (this.mChildViewList.indexOf(this.mCurrentFocusedView) == this.mChildViewList.size() - 1) {
            ((TabSortedItemView) v).updateStatus(TabSortedState.FOCUS_ACTIVATED_ARROW_LEFT);
        } else {
            ((TabSortedItemView) v).updateStatus(TabSortedState.FOCUS_ACTIVATED_ARROW_LEFT_RIGHT);
        }
    }

    private void startSpringBackAnimation(View view) {
        if (AnimationUtils.currentAnimationTimeMillis() - this.mLastSpringBackAnimationTime > 500) {
            view.startAnimation(AnimationUtils.loadAnimation(this.mContext, C0508R.anim.epg_shake));
            this.mLastSpringBackAnimationTime = AnimationUtils.currentAnimationTimeMillis();
        }
    }

    private void startYSpringBackAnimation(View view) {
        if (AnimationUtils.currentAnimationTimeMillis() - this.mLastYSpringBackAnimationTime > 500) {
            view.startAnimation(AnimationUtils.loadAnimation(this.mContext, C0508R.anim.epg_shake_y));
            this.mLastYSpringBackAnimationTime = AnimationUtils.currentAnimationTimeMillis();
        }
    }

    private void dealWithSpringBackAnimation(View v, KeyEvent event) {
        if (event.getAction() == 0 && event.getKeyCode() == 22) {
            if (this.mChildViewList.indexOf(v) == this.mChildViewList.size() - 1) {
                startSpringBackAnimation(v);
            }
        } else if (event.getAction() == 0 && event.getKeyCode() == 21) {
            if (this.mChildViewList.indexOf(v) == this.minSortableChildIndex) {
                startSpringBackAnimation(v);
            }
        } else if (event.getAction() == 0 && event.getKeyCode() == 19) {
            startYSpringBackAnimation(v);
        }
    }

    private boolean dealWithBackKeyEvent(View v, KeyEvent event) {
        if (!this.mIsActivatedState || event.getKeyCode() != 4) {
            return false;
        }
        ((TabSortedItemView) v).updateStatus(TabSortedState.FOCUS);
        if (this.mTabSortedActivatedListener != null) {
            this.mTabSortedActivatedListener.cancelActivation(this.mCurrentFocusedView);
        }
        this.mIsActivatedState = false;
        return true;
    }

    public void addItemView(TabModel tabModel) {
        TabSortedItemView childView = new TabSortedItemView(this.mContext);
        childView.setText(tabModel.getTitle());
        childView.setData(tabModel);
        MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
        if (childView.getLayoutParams() == null) {
            layoutParams = new LayoutParams(-2, -2);
        }
        layoutParams.leftMargin = this.mContext.getResources().getDimensionPixelSize(C0508R.dimen.dimen_022dp);
        layoutParams.rightMargin = this.mContext.getResources().getDimensionPixelSize(C0508R.dimen.dimen_022dp);
        layoutParams.width = this.mContext.getResources().getDimensionPixelSize(C0508R.dimen.dimen_196dp);
        layoutParams.height = this.mContext.getResources().getDimensionPixelSize(C0508R.dimen.dimen_121dp);
        childView.setId(ViewUtils.generateViewId());
        childView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this.mContext, C0508R.anim.epg_tab_manager_add_tab_layout_animation));
        addView(childView, layoutParams);
        this.mIsAddItemTabFromAlternativeList = true;
        requestLayout();
        this.mChildViewList.add(childView);
        setLastFocusView(childView);
        updateChildrenFocusList();
        childView.setOnFocusChangeListener(this);
        childView.setOnClickListener(this);
        smoothScrollBy(computeScrollDelta(childView));
        this.mCurrentFocusedView = childView;
    }

    public void removeItemView(TabModel tabModel) {
        for (int i = 0; i < getChildCount(); i++) {
            TabSortedItemView itemView = (TabSortedItemView) getChildAt(i);
            if (itemView.getData().getId() == tabModel.getId()) {
                if (itemView.getData().getId() == this.mLastFocusView.getData().getId()) {
                    if (i == getChildCount() - 1) {
                        this.mLastFocusView = (TabSortedItemView) getChildAt(i - 1);
                    } else {
                        this.mLastFocusView = (TabSortedItemView) getChildAt(i + 1);
                    }
                }
                removeView(itemView);
                this.mChildViewList.remove(itemView);
                this.mIsRemoveItemTabFromAlternativeList = true;
                updateChildrenFocusList();
                return;
            }
        }
    }

    public String getChildSortedResult() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.mChildViewList.size(); i++) {
            if (i == this.mChildViewList.size() - 1) {
                result.append("tab_" + ((TabSortedItemView) this.mChildViewList.get(i)).getData().getTitle() + SOAP.DELIM + (i + 1));
            } else {
                result.append("tab_" + ((TabSortedItemView) this.mChildViewList.get(i)).getData().getTitle() + SOAP.DELIM + (i + 1) + ",");
            }
        }
        return result.toString();
    }

    public String getOldChildSortedResult() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.mOldChildViewList.size(); i++) {
            if (i == this.mOldChildViewList.size() - 1) {
                result.append("tab_" + ((TabSortedItemView) this.mOldChildViewList.get(i)).getData().getTitle() + SOAP.DELIM + (i + 1));
            } else {
                result.append("tab_" + ((TabSortedItemView) this.mOldChildViewList.get(i)).getData().getTitle() + SOAP.DELIM + (i + 1) + ",");
            }
        }
        return result.toString();
    }
}

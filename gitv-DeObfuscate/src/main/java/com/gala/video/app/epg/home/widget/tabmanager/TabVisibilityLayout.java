package com.gala.video.app.epg.home.widget.tabmanager;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.gala.video.app.epg.C0508R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.ViewUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import java.util.ArrayList;
import java.util.List;

public class TabVisibilityLayout extends LinearLayout implements OnFocusChangeListener, OnClickListener {
    private static final int COUNT_ONE_LINE = 8;
    private static final String TAG = "tabmanager/TabVisibilityLayout";
    private boolean clipChildren = true;
    private boolean compatibleMode = false;
    private TabVisibilityAdapter mAdapter;
    private List<TabVisibilityItemView> mChildViewList = new ArrayList();
    private Context mContext;
    private TabVisibilityItemView mCurrentFocusedView;
    private int mHeight;
    private TabVisibilityItemView mLastFocusView = null;
    private long mLastSpringBackAnimationTime = 0;
    private long mLastXSpringBackAnimationTime = 0;
    private TabVisibilityItemOnKeyListener mTabVisibilityItemOnKeyListener;
    private TabVisibilityListener mTabVisibilityListener;

    public TabVisibilityLayout(Context context) {
        super(context);
        init(context);
    }

    public TabVisibilityLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TabVisibilityLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        boolean z = true;
        setOrientation(1);
        this.mContext = context;
        if (VERSION.SDK_INT >= 19) {
            z = false;
        }
        this.compatibleMode = z;
        setClipChildren(false);
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

    public boolean dispatchKeyEvent(KeyEvent event) {
        dealWithSpringBackAnimation(this.mCurrentFocusedView, event);
        if (event.getAction() == 0 && event.getKeyCode() == 19 && this.mTabVisibilityItemOnKeyListener != null) {
            this.mTabVisibilityItemOnKeyListener.onDpadUpKeyEvent(this.mCurrentFocusedView, event);
        }
        return super.dispatchKeyEvent(event);
    }

    public void setClipChildren(boolean clipChildren) {
        super.setClipChildren(clipChildren);
        this.clipChildren = clipChildren;
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

    public void setAdapter(TabVisibilityAdapter adapter) {
        this.mAdapter = adapter;
        int length = this.mAdapter.getCount();
        int lineNum = length / 8;
        if (length % 8 != 0) {
            lineNum++;
        }
        for (int i = 0; i < lineNum; i++) {
            TabVisibilityLinearLayout linearLayout = new TabVisibilityLinearLayout(this.mContext);
            MarginLayoutParams layoutParams = (MarginLayoutParams) linearLayout.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new LayoutParams(-2, -2);
            }
            if (i == 0) {
                layoutParams.topMargin = this.mContext.getResources().getDimensionPixelSize(C0508R.dimen.dimen_3dp);
            }
            layoutParams.leftMargin = this.mContext.getResources().getDimensionPixelSize(C0508R.dimen.dimen_30dp);
            linearLayout.setId(ViewUtils.generateViewId());
            linearLayout.setOrientation(0);
            linearLayout.setClipChildren(false);
            addView(linearLayout, layoutParams);
            int j = i * 8;
            while (j < (i + 1) * 8 && j < length) {
                TabVisibilityItemView childView = this.mAdapter.getView(j, null, null);
                TabModel tabModel = (TabModel) this.mAdapter.getTabInfoList().get(j);
                MarginLayoutParams childLayoutParams = (MarginLayoutParams) childView.getLayoutParams();
                if (childView.getLayoutParams() == null) {
                    childLayoutParams = new LayoutParams(-2, -2);
                }
                childLayoutParams.width = this.mContext.getResources().getDimensionPixelSize(C0508R.dimen.dimen_136dp);
                childLayoutParams.height = this.mContext.getResources().getDimensionPixelSize(C0508R.dimen.dimen_53dp);
                childLayoutParams.rightMargin = this.mContext.getResources().getDimensionPixelSize(C0508R.dimen.dimen_17dp);
                childLayoutParams.bottomMargin = this.mContext.getResources().getDimensionPixelSize(C0508R.dimen.dimen_16dp);
                childView.setId(ViewUtils.generateViewId());
                childView.setOnClickListener(this);
                childView.setOnFocusChangeListener(this);
                if (tabModel.isShown()) {
                    childView.setStatus(TabVisibilityState.NORMAL_ADDED);
                } else {
                    childView.setStatus(TabVisibilityState.NORMAL_NOT_ADDED);
                }
                if (i == 0) {
                    childView.setIsAtFirstLine(true);
                }
                if (i == lineNum - 1) {
                    childView.setIsAtLastLine(true);
                }
                if (j == 0) {
                    childView.setIsFirstOne(true);
                }
                if (j == length - 1) {
                    childView.setIsLastOne(true);
                }
                linearLayout.addView(childView, childLayoutParams);
                this.mChildViewList.add(childView);
                j++;
            }
        }
        updateChildrenFocusList();
    }

    public void setTabVisibilityListener(TabVisibilityListener tabVisibilityListener) {
        this.mTabVisibilityListener = tabVisibilityListener;
    }

    public void setTabVisibilityItemOnKeyListener(TabVisibilityItemOnKeyListener mTabVisibilityItemOnKeyListener) {
        this.mTabVisibilityItemOnKeyListener = mTabVisibilityItemOnKeyListener;
    }

    private void updateChildrenFocusList() {
        int length = this.mChildViewList.size();
        for (int i = 0; i < length; i++) {
            View curView = (View) this.mChildViewList.get(i);
            int preIndex = i - 1;
            int nextIndex = i + 1;
            if (preIndex < 0) {
                curView.setNextFocusLeftId(curView.getId());
            } else {
                curView.setNextFocusLeftId(((TabVisibilityItemView) this.mChildViewList.get(preIndex)).getId());
            }
            if (nextIndex > length - 1) {
                curView.setNextFocusRightId(curView.getId());
            } else {
                curView.setNextFocusRightId(((TabVisibilityItemView) this.mChildViewList.get(nextIndex)).getId());
            }
        }
    }

    public void onClick(View v) {
        if (v instanceof TabVisibilityItemView) {
            TabVisibilityItemView itemView = (TabVisibilityItemView) v;
            TabVisibilityState state = itemView.getCurTabSortedState();
            if (state == TabVisibilityState.FOCUS_ADDIBLE) {
                if (this.mTabVisibilityListener != null) {
                    this.mTabVisibilityListener.addTab(itemView.getData(), itemView);
                }
            } else if (state != TabVisibilityState.FOCUS_REMOVABLE) {
                LogUtils.m1577w(TAG, "onClick, itemView status illegal: " + state);
            } else if (this.mTabVisibilityListener != null) {
                this.mTabVisibilityListener.removeTab(itemView.getData(), itemView);
            }
        }
    }

    public void onFocusChange(View v, boolean hasFocus) {
        this.mCurrentFocusedView = (TabVisibilityItemView) v;
        this.mLastFocusView = this.mCurrentFocusedView;
    }

    private void dealWithSpringBackAnimation(TabVisibilityItemView v, KeyEvent event) {
        if (event.getAction() == 0 && event.getKeyCode() == 20) {
            if (v.isAtLastLine()) {
                startSpringBackAnimation(v);
            }
        } else if (event.getAction() == 0 && event.getKeyCode() == 21) {
            if (v.isFirstOne()) {
                startXSpringBackAnimation(v);
            }
        } else if (event.getAction() == 0 && event.getKeyCode() == 22 && v.isLastOne()) {
            startXSpringBackAnimation(v);
        }
    }

    private void startSpringBackAnimation(View view) {
        if (AnimationUtils.currentAnimationTimeMillis() - this.mLastSpringBackAnimationTime > 500) {
            view.startAnimation(AnimationUtils.loadAnimation(this.mContext, C0508R.anim.epg_shake_y));
            this.mLastSpringBackAnimationTime = AnimationUtils.currentAnimationTimeMillis();
        }
    }

    private void startXSpringBackAnimation(View view) {
        if (AnimationUtils.currentAnimationTimeMillis() - this.mLastXSpringBackAnimationTime > 500) {
            view.startAnimation(AnimationUtils.loadAnimation(this.mContext, C0508R.anim.epg_shake));
            this.mLastXSpringBackAnimationTime = AnimationUtils.currentAnimationTimeMillis();
        }
    }
}

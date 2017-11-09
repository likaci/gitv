package com.gala.video.app.player.ui.widget.listview;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class DetailListView extends ListView {
    private static final int SCROLL_DURATION = 200;
    private final int FOCUS_CHANGE_GAP = 20;
    private final int NOT_NEED_SELECT_POS = -1;
    private String TAG;
    private BaseDetailListAdapter mAdapter;
    protected int mChangedFocusPos;
    private Context mContext;
    private int mDividerHeight;
    private boolean mDownScroll;
    private OnFocusChangeListener mFocusChangeListener = new C15383();
    private int mFocusedItemBgResId;
    private Handler mHandler = new Handler(Looper.myLooper());
    private int mItemHeight;
    private OnItemSelectedListener mItemSelectedListener = new C15351();
    private long mLastFocusChangeTime;
    protected int mLastFocusPos = -1;
    private int mLatestVisiblePosition;
    private int mMaxVisibleCount;
    private int mMiddleItemIndex;
    private boolean mNeedAjust;
    private OnUserScrollListener mOutListener;
    private int mPlayIndex = -1;
    private int mPreLast;
    private int mRealDivideHeight;
    private OnScrollListener mScrollListener = new C15372();
    private int mSelectionTopOffset;
    private int mTopOffset;
    private int mTotalCount;
    private boolean mUserChange = false;

    public interface OnUserScrollListener {
        void onLastItemVisibile();

        void onScrollStop(int i, int i2);
    }

    class C15351 implements OnItemSelectedListener {
        C15351() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            LogUtils.m1568d(DetailListView.this.TAG, ">>onItemSelected position ==" + position);
            if (DetailListView.this.hasFocus()) {
                LogUtils.m1568d(DetailListView.this.TAG, "onItemSelected position " + position);
                DetailListView.this.clearChildFocus();
                if (view != null && view.getOnFocusChangeListener() != null) {
                    view.getOnFocusChangeListener().onFocusChange(view, true);
                    DetailListView.this.adjustPositionOffset(position);
                    return;
                }
                return;
            }
            LogUtils.m1571e(DetailListView.this.TAG, "!not has focus, return");
            DetailListView.this.adjustPositionOffset(position);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            LogUtils.m1568d(DetailListView.this.TAG, ">>onNothingSelected");
        }
    }

    class C15372 implements OnScrollListener {

        class C15361 implements Runnable {
            C15361() {
            }

            public void run() {
                DetailListView.this.adjustPositionOffset(-1);
                DetailListView.this.mNeedAjust = false;
            }
        }

        C15372() {
        }

        public void onScrollStateChanged(AbsListView view, int scrollState) {
            LogUtils.m1568d(DetailListView.this.TAG, "onScrollStateChanged mNeedAjust " + DetailListView.this.mNeedAjust);
            if (scrollState == 0 && DetailListView.this.mNeedAjust) {
                DetailListView.this.mHandler.postDelayed(new C15361(), 50);
            }
            if (scrollState == 0 && DetailListView.this.mOutListener != null && DetailListView.this.mDownScroll) {
                DetailListView.this.mOutListener.onScrollStop(DetailListView.this.mLatestVisiblePosition, DetailListView.this.mTotalCount);
            }
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            int lastItem = firstVisibleItem + visibleItemCount;
            DetailListView.this.mLatestVisiblePosition = lastItem - 1;
            if (lastItem == totalItemCount && DetailListView.this.mPreLast != lastItem) {
                DetailListView.this.mPreLast = lastItem;
                if (DetailListView.this.mOutListener != null) {
                    DetailListView.this.mOutListener.onLastItemVisibile();
                }
            }
            if (!DetailListView.this.isVerticalScrollBarEnabled() && DetailListView.this.hasFocus()) {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(DetailListView.this.TAG, "onScroll enable scrollBar");
                }
                DetailListView.this.setVerticalScrollBarEnabled(true);
            }
        }
    }

    class C15383 implements OnFocusChangeListener {
        C15383() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(DetailListView.this.TAG, "onFocusChange hasFocus = {" + hasFocus + "},mLastFocusPos = {" + DetailListView.this.mLastFocusPos + "} mChangedFocusPos = {" + DetailListView.this.mChangedFocusPos + "}");
            }
            if (!hasFocus) {
                DetailListView.this.clearChildFocus();
                DetailListView.this.mLastFocusPos = DetailListView.this.getSelectedItemPosition();
            } else if (DetailListView.this.mChangedFocusPos != -1) {
                if (DetailListView.this.mChangedFocusPos == DetailListView.this.getSelectedItemPosition()) {
                    view = DetailListView.this.getChildView(DetailListView.this.mChangedFocusPos);
                    if (view != null) {
                        DetailListView.this.mItemSelectedListener.onItemSelected(DetailListView.this, view, DetailListView.this.mChangedFocusPos, (long) view.getId());
                    } else {
                        LogUtils.m1571e(DetailListView.this.TAG, "null == getchildview");
                    }
                }
                DetailListView.this.mChangedFocusPos = -1;
            } else if (DetailListView.this.mLastFocusPos != -1) {
                DetailListView.this.clearChildFocus();
                view = DetailListView.this.getChildView(DetailListView.this.mLastFocusPos);
                Log.e(DetailListView.this.TAG, "onFocusChange view = " + view + DetailListView.this.getChildCount());
                if (view != null && view.getOnFocusChangeListener() != null) {
                    DetailListView.this.mLastFocusChangeTime = SystemClock.elapsedRealtime();
                    view.getOnFocusChangeListener().onFocusChange(view, true);
                    DetailListView.this.adjustPositionOffset(DetailListView.this.mLastFocusPos);
                }
            } else {
                DetailListView.this.setSelection(0);
            }
        }
    }

    class C15394 implements Runnable {
        C15394() {
        }

        public void run() {
            DetailListView.this.requestFocus();
        }
    }

    public DetailListView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public DetailListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    public DetailListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        this.TAG = "DetailListView@" + Integer.toHexString(hashCode());
        setSelector(17170445);
        setOnItemSelectedListener(this.mItemSelectedListener);
        setOnScrollListener(this.mScrollListener);
        setVerticalScrollBarEnabled(false);
    }

    public void setUI(int[] dimens) {
        LogUtils.m1571e(this.TAG, "setUiDimens");
        if (dimens == null || dimens.length != 3) {
            LogUtils.m1571e(this.TAG, "invalid ui dimens");
            return;
        }
        this.mItemHeight = this.mContext.getResources().getDimensionPixelSize(dimens[0]);
        this.mDividerHeight = this.mContext.getResources().getDimensionPixelSize(dimens[1]);
        this.mFocusedItemBgResId = dimens[2];
        this.mTopOffset = -getDrawablePadding();
        this.mRealDivideHeight = this.mDividerHeight - (getDrawablePadding() * 2);
        setDividerHeight(this.mRealDivideHeight);
        this.mSelectionTopOffset = (this.mMiddleItemIndex * this.mItemHeight) + (this.mMiddleItemIndex * this.mDividerHeight);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setUiDimens mItemHeight = " + this.mItemHeight + ", mDividerHeight = " + this.mDividerHeight + "mTopOffset = " + this.mTopOffset + ", mSelectionTopOffset = " + this.mSelectionTopOffset);
        }
    }

    private int getDrawablePadding() {
        int dp = 0;
        if (this.mFocusedItemBgResId > 0) {
            Rect rect = new Rect();
            getContext().getResources().getDrawable(this.mFocusedItemBgResId).getPadding(rect);
            dp = rect.top;
        }
        if (LogUtils.mIsDebug) {
            Log.e(this.TAG, "(getDrawablePadding) = " + dp);
        }
        return dp;
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        boolean hasFocus = hasFocus();
        LogUtils.m1568d(this.TAG, ">>onFocusChanged hasFocus=" + hasFocus);
        this.mFocusChangeListener.onFocusChange(this, hasFocus);
        if (!hasFocus) {
            setVerticalScrollBarEnabled(false);
        }
    }

    private View getChildView(int index) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "getChildView index=" + index);
        }
        View view = null;
        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            if (index == getChildAt(i).getId()) {
                view = getChildAt(i);
            }
        }
        return view;
    }

    private void clearChildFocus() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (!(childView == null || childView.getOnFocusChangeListener() == null)) {
                childView.getOnFocusChangeListener().onFocusChange(childView, false);
            }
        }
    }

    public void setOnUserScrollListener(OnUserScrollListener outListener) {
        this.mOutListener = outListener;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtils.m1568d(this.TAG, ">>onKeyDown");
        this.mLastFocusChangeTime = SystemClock.elapsedRealtime();
        if (keyCode == 19 || keyCode == 20) {
            this.mUserChange = true;
        }
        if (this.mTotalCount <= this.mMaxVisibleCount) {
            return super.onKeyDown(keyCode, event);
        }
        int curItemPos = getSelectedItemPosition();
        if (getSelectedView() == null) {
            return super.onKeyDown(keyCode, event);
        }
        LogUtils.m1568d(this.TAG, "onKeyDown curItemPos= " + curItemPos + " keyCode " + keyCode);
        switch (keyCode) {
            case 19:
                LogUtils.m1568d(this.TAG, "curItemPos= " + curItemPos);
                this.mDownScroll = false;
                if (curItemPos != 0) {
                    this.mNeedAjust = true;
                    if (curItemPos > 1) {
                        if (curItemPos < (this.mTotalCount - this.mMiddleItemIndex) + 1 && curItemPos > this.mMiddleItemIndex - 1) {
                            smoothScrollToPositionFromTop((curItemPos - this.mMiddleItemIndex) - 1, this.mTopOffset, 200);
                            break;
                        }
                    }
                    scrollTo(0, 0);
                    break;
                }
                return false;
            case 20:
                this.mDownScroll = true;
                this.mNeedAjust = true;
                if (curItemPos > this.mMiddleItemIndex - 1 && curItemPos < this.mTotalCount - this.mMiddleItemIndex) {
                    smoothScrollToPositionFromTop((curItemPos - this.mMiddleItemIndex) + 1, this.mTopOffset, 200);
                    break;
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void adjustPositionOffset(int needSelectIndex) {
        int position = getSelectedItemPosition();
        LogUtils.m1568d(this.TAG, ">>adjustPositionOffset needSelectIndex =" + needSelectIndex + ", position = " + position);
        if (needSelectIndex != -1) {
            position = needSelectIndex;
        }
        if (position <= this.mMiddleItemIndex && position > 0) {
            smoothScrollToPositionFromTop(0, this.mTopOffset, 200);
        } else if (position > this.mMiddleItemIndex && position < this.mTotalCount - this.mMiddleItemIndex) {
            smoothScrollToPositionFromTop(position - this.mMiddleItemIndex, this.mTopOffset, 200);
        } else if (position >= this.mTotalCount - this.mMiddleItemIndex) {
            smoothScrollToPositionFromTop(this.mTotalCount - this.mMiddleItemIndex, this.mTopOffset, 200);
        }
    }

    public void setMaxVisibleCount(int maxVisibleCount) {
        this.mMaxVisibleCount = maxVisibleCount;
        this.mMiddleItemIndex = this.mMaxVisibleCount / 2;
    }

    public void setPlayingIndex(int index, boolean needFocus) {
        setPlayingIndex(index);
        if (needFocus) {
            post(new C15394());
        }
    }

    public void setPlayingIndex(final int index) {
        LogUtils.m1568d(this.TAG, ">>setPlayingIndex index = " + index);
        this.mPlayIndex = index;
        post(new Runnable() {
            public void run() {
                int currentIndex = DetailListView.this.getSelectedItemPosition();
                LogUtils.m1568d(DetailListView.this.TAG, "currentIndex != index currentIndex=" + currentIndex);
                if (DetailListView.this.hasFocus()) {
                    int time = (int) ((SystemClock.elapsedRealtime() - DetailListView.this.mLastFocusChangeTime) / 1000);
                    LogUtils.m1571e(DetailListView.this.TAG, "focus change time == " + time + ", mPlayIndex = " + DetailListView.this.mPlayIndex);
                    if (time > 20 || !DetailListView.this.mUserChange) {
                        DetailListView.this.setSelectionFromTop(index, DetailListView.this.mSelectionTopOffset);
                        if (currentIndex == index) {
                            View view = DetailListView.this.getChildView(index);
                            if (view != null) {
                                DetailListView.this.mItemSelectedListener.onItemSelected(DetailListView.this, view, index, (long) view.getId());
                            } else {
                                LogUtils.m1571e(DetailListView.this.TAG, "null == getchildview");
                            }
                        }
                    } else {
                        LogUtils.m1571e(DetailListView.this.TAG, "do not change listView, time is less than 20s");
                        DetailListView.this.setSelection(index);
                    }
                } else {
                    LogUtils.m1568d(DetailListView.this.TAG, "adjustPositionOffset index = " + index);
                    if (index >= 0) {
                        DetailListView.this.setSelectionFromTop(index, DetailListView.this.mSelectionTopOffset);
                        DetailListView.this.mNeedAjust = true;
                        DetailListView.this.mChangedFocusPos = index;
                    }
                }
                DetailListView.this.mPlayIndex = index;
                if (DetailListView.this.mAdapter != null) {
                    DetailListView.this.mAdapter.setPlayingInicatorIndex(DetailListView.this.mPlayIndex);
                }
            }
        });
    }

    public void setDetailListViewAdapter(BaseDetailListAdapter adapter) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setAdapter is null ? =" + (adapter == null));
        }
        if (adapter != null) {
            this.mAdapter = adapter;
            setAdapter(adapter);
            adapter.notifyDataSetChanged();
            onAdapterUpdate();
        }
    }

    public void onAdapterUpdate() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">>onAdapterUpdate mTotalCount=" + this.mTotalCount);
        }
        if (this.mAdapter != null) {
            this.mTotalCount = getAdapter().getCount();
        }
        adjustPositionOffset(-1);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "<<onAdapterUpdate mTotalCount=" + this.mTotalCount);
        }
    }

    public int getFocusIndex() {
        int selectIndex = getSelectedItemPosition();
        LogUtils.m1568d(this.TAG, "getFocusIndex return" + selectIndex);
        return selectIndex;
    }

    public void addFooterView(View v, Object data, boolean isSelectable) {
        LayoutParams layoutParams = new LayoutParams(-1, this.mItemHeight + (getDrawablePadding() * 2));
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "layoutParams.height=" + layoutParams.height + ",mRealDivideHeight=" + this.mRealDivideHeight);
        }
        v.setLayoutParams(layoutParams);
        super.addFooterView(v, data, isSelectable);
        onAdapterUpdate();
    }

    public boolean removeFooterView(View v) {
        onAdapterUpdate();
        return super.removeFooterView(v);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "onSizeChanged/getCount()=" + getCount() + "/getChildCount()" + getChildCount() + "/getAdapter().getCount()" + (getAdapter() != null ? getAdapter().getCount() : 0));
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "onMeasure/getCount()=" + getCount() + "/getChildCount()" + getChildCount() + "/getAdapter().getCount()" + (getAdapter() != null ? getAdapter().getCount() : 0));
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void layoutChildren() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "layoutChildren/getCount()=" + getCount() + "/getChildCount()" + getChildCount() + "/getAdapter().getCount()" + (getAdapter() != null ? getAdapter().getCount() : 0));
        }
        super.layoutChildren();
    }

    protected void onFinishInflate() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "onFinishInflate/getCount()=" + getCount() + "/getChildCount()" + getChildCount() + "/getAdapter().getCount()" + (getAdapter() != null ? getAdapter().getCount() : 0));
        }
        super.onFinishInflate();
    }
}

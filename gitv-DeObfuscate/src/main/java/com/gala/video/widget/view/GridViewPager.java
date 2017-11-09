package com.gala.video.widget.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Scroller;
import com.gala.video.widget.IPageViewListener;
import com.gala.video.widget.adapter.GridViewPagerAdapter;
import com.gala.video.widget.adapter.ViewAdapter;
import com.gala.video.widget.util.DebugOptions;
import com.gala.video.widget.util.LogUtils;
import com.gala.video.widget.view.DrawingOrderGridView.LayoutChildrenListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GridViewPager<T> extends ViewPager {
    private static final int LEAST_NUM = 1;
    private final String TAG;
    private int mCachePageSize = 1;
    private OnFocusChangeListener mChildFocusChangeListener = new C18901();
    private Rect mContentPadding;
    private Context mContext;
    private ArrayList<T> mDataList;
    private int mDeltaMarginLeft;
    private int mDeltaMarginTop;
    private boolean mDrawSelectorOnTop = false;
    private OnEdgeReachedListener mEdgeListener;
    private OnFocusableChangeListener mFocusableChangeListener;
    private Drawable mGridSelector = new ColorDrawable(0);
    private GridViewPagerAdapter<T> mGridViewPagerAdapter;
    private boolean mHasFocus = false;
    private int mHorizontalSpacing = -120;
    private int mLastPage = 0;
    private int mNextFocusDownID = -1;
    private int mNextFocusUpID = -1;
    private int mNumColumn = 6;
    private int mNumRow = 1;
    private int mNums;
    private OnFocusChangeListener mOnFocusChangeListener;
    private OnPageChangeListener mOnPageChangeListener = null;
    private int mPageSize = 0;
    private IPageViewListener mPageViewListener = null;
    private int mScrollDuration = 400;
    private int mTotal;
    private int mVerticalSpacing = 0;
    private int mViewPageScrollState;
    private ArrayList<DrawingOrderGridView> mViews = null;
    private boolean mZoomEnabled = true;
    private float mZoomRatio = 1.0f;
    OnItemClickListener onItemClickListener = new C18912();
    OnPageChangeListener onPageChangeListener = new C18923();

    class C18901 implements OnFocusChangeListener {
        C18901() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (GridViewPager.this.mHasFocus != GridViewPager.this.hasFocus() && GridViewPager.this.mOnFocusChangeListener != null) {
                GridViewPager.this.mHasFocus = !GridViewPager.this.mHasFocus;
                GridViewPager.this.mOnFocusChangeListener.onFocusChange(GridViewPager.this, GridViewPager.this.mHasFocus);
            }
        }
    }

    class C18912 implements OnItemClickListener {
        C18912() {
        }

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (GridViewPager.this.mPageViewListener != null) {
                GridViewPager.this.mPageViewListener.onItemClick(parent, view, position, id);
            }
        }
    }

    class C18923 implements OnPageChangeListener {
        C18923() {
        }

        public void onPageSelected(int position) {
            GridViewPager.this.setSelectedWhenTurnPage(position, "onPageSelected");
            if (GridViewPager.this.mOnPageChangeListener != null) {
                GridViewPager.this.mOnPageChangeListener.onPageSelected(position);
            }
            GridViewPager.this.notifyPageSelected(position);
        }

        public void onPageScrolled(int position, float posOffset, int posOffsetPixels) {
            if (GridViewPager.this.mOnPageChangeListener != null) {
                GridViewPager.this.mOnPageChangeListener.onPageScrolled(position, posOffset, posOffsetPixels);
            }
            GridViewPager.this.notifyPageScrolled(position, posOffset, posOffsetPixels);
        }

        public void onPageScrollStateChanged(int state) {
            GridViewPager.this.mViewPageScrollState = state;
            if (state == 2) {
                GridViewPager.this.onStartScroll();
            } else if (state == 0) {
                GridViewPager.this.onCompleteScroll();
            }
            if (GridViewPager.this.mOnPageChangeListener != null) {
                GridViewPager.this.mOnPageChangeListener.onPageScrollStateChanged(state);
            }
            GridViewPager.this.notifyPageScrollStateChanged(state);
        }
    }

    public enum Edge {
        LEFT,
        RIGHT
    }

    class FixedSpeedScroller extends Scroller {
        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, GridViewPager.this.mScrollDuration);
        }

        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, GridViewPager.this.mScrollDuration);
        }
    }

    public interface OnEdgeReachedListener {
        void onEdgeReached(Edge edge);
    }

    public interface OnFocusableChangeListener {
        void onFocusableChanged(boolean z);
    }

    public void setPageViewListener(IPageViewListener pageViewListener) {
        this.mPageViewListener = pageViewListener;
        if (this.mViews != null) {
            Iterator it = this.mViews.iterator();
            while (it.hasNext()) {
                ((DrawingOrderGridView) it.next()).setPageViewListener(pageViewListener);
            }
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    public GridViewPager(Context context) {
        super(context);
        init(context);
        this.TAG = "gridpageview/GridViewPager@" + Integer.toHexString(hashCode());
    }

    public GridViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        this.TAG = "gridpageview/GridViewPager@" + Integer.toHexString(hashCode());
    }

    private void init(Context context) {
        this.mContext = context;
        setScrollerAttrs(this.mContext);
        super.setOnPageChangeListener(this.onPageChangeListener);
        setOffscreenPageLimit(1);
        if (DebugOptions.isInDebugMode()) {
            setBackgroundColor(DebugOptions.DEBUG_BG_COLOR);
        }
    }

    public void clearFocus() {
        super.clearFocus();
        if (this.mViews != null) {
            getCurGridView().clearChildFocus();
        }
    }

    public void setOffscreenPageLimit(int offsetScreenPageLimit) {
        if (offsetScreenPageLimit <= 0) {
            offsetScreenPageLimit = 1;
        }
        this.mCachePageSize = (offsetScreenPageLimit * 2) + 1;
        super.setOffscreenPageLimit(offsetScreenPageLimit);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1598d(this.TAG, "dispatchKeyEvent: " + event);
        }
        if (this.mViewPageScrollState != 0) {
            return true;
        }
        if (this.mGridViewPagerAdapter != null) {
            this.mGridViewPagerAdapter.setLeft(event.getKeyCode() == 21);
        }
        int curPage = getCurrentItem();
        LogUtils.m1598d(this.TAG, "dispatchKeyEvent: current page=" + curPage);
        if (event.getAction() == 0) {
            int selectedItemPos;
            switch (event.getKeyCode()) {
                case 21:
                    if (curPage == 0) {
                        selectedItemPos = getCurGridView().getSelectedItemPosition();
                        LogUtils.m1601e(this.TAG, "selected item pos=" + selectedItemPos);
                        if (selectedItemPos % this.mNumColumn == 0 || selectedItemPos == 0) {
                            LogUtils.m1598d(this.TAG, "dispatch: LEFT edge reached");
                            if (this.mEdgeListener != null) {
                                this.mEdgeListener.onEdgeReached(Edge.LEFT);
                            }
                        }
                        if (selectedItemPos == 0) {
                            LogUtils.m1601e(this.TAG, "the 1st child has focus!!!");
                            return true;
                        }
                    }
                    break;
                case 22:
                    if (curPage == getPageCount() - 1) {
                        DrawingOrderGridView gridView = getCurGridView();
                        selectedItemPos = gridView.getSelectedItemPosition();
                        LogUtils.m1601e(this.TAG, "selected item pos=" + selectedItemPos);
                        if (selectedItemPos % this.mNumColumn == this.mNumColumn - 1 || selectedItemPos == gridView.getChildCount() - 1) {
                            LogUtils.m1598d(this.TAG, "dispatch: RIGHT edge reached");
                            if (this.mEdgeListener != null) {
                                this.mEdgeListener.onEdgeReached(Edge.RIGHT);
                            }
                        }
                        if (selectedItemPos == gridView.getChildCount() - 1) {
                            LogUtils.m1601e(this.TAG, "the last child has focus!!!");
                            return true;
                        }
                    }
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
        setSelectedWhenTurnPage(item, "setCurrentItem");
    }

    public void setGridAdapter(Class<?> adapter) {
        log("setGridAdapter: " + adapter + ", cachePageSize=" + this.mCachePageSize);
        reset();
        this.mNums = this.mNumColumn * this.mNumRow;
        this.mViews = new ArrayList();
        for (int i = 0; i < this.mCachePageSize; i++) {
            DrawingOrderGridView gridView = initGridView(adapter);
            addLayoutChildrenListener(gridView);
            this.mViews.add(gridView);
        }
        setNextFocusUpId(this.mNextFocusUpID);
        setNextFocusDownId(this.mNextFocusDownID);
    }

    public void addLayoutChildrenListenerOnce() {
        log("addLayoutChildrenListenerOnce()");
        if (this.mViews != null) {
            Iterator it = this.mViews.iterator();
            while (it.hasNext()) {
                addLayoutChildrenListener((DrawingOrderGridView) it.next());
            }
        }
    }

    private void addLayoutChildrenListener(final DrawingOrderGridView curGridView) {
        LogUtils.m1598d(this.TAG, "addLayoutChildrenListener: cur grid view=" + curGridView);
        if (curGridView != null) {
            curGridView.setLayoutChildrenListener(new LayoutChildrenListener() {

                class C18931 implements Runnable {
                    C18931() {
                    }

                    public void run() {
                        GridViewPager.this.setFocusable(true);
                        List<DrawingOrderGridView> childs = GridViewPager.this.getChildViews();
                        if (childs != null) {
                            for (DrawingOrderGridView gridView : childs) {
                                gridView.setFocusable(true);
                            }
                            if (GridViewPager.this.mFocusableChangeListener != null) {
                                GridViewPager.this.mFocusableChangeListener.onFocusableChanged(true);
                            }
                        }
                    }
                }

                public void onLayoutBegin() {
                    LogUtils.m1598d(GridViewPager.this.TAG, "addLayoutChildrenListener: onLayoutBegin");
                    GridViewPager.this.setFocusable(false);
                    List<DrawingOrderGridView> childs = GridViewPager.this.getChildViews();
                    if (childs != null) {
                        for (DrawingOrderGridView gridView : childs) {
                            gridView.setFocusable(false);
                        }
                        if (GridViewPager.this.mFocusableChangeListener != null) {
                            GridViewPager.this.mFocusableChangeListener.onFocusableChanged(false);
                        }
                    }
                }

                public void onLayoutEnd() {
                    LogUtils.m1598d(GridViewPager.this.TAG, "initAlbumPager: onLayoutEnd");
                    GridViewPager.this.post(new C18931());
                    curGridView.setLayoutChildrenListener(null);
                    Iterator it = GridViewPager.this.mViews.iterator();
                    while (it.hasNext()) {
                        ((DrawingOrderGridView) it.next()).setLayoutChildrenListener(null);
                    }
                }
            });
        }
    }

    public void setDataSource(ArrayList<T> datas) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1598d(this.TAG, "setDataSource: " + datas);
        }
        if (datas == null || (datas != null && datas.size() < 1)) {
            reset();
        } else if (isEmpty()) {
            throw new IllegalArgumentException("please invoke setGridAdapter first!");
        } else {
            log("setDataSource!!!");
            reset();
            this.mDataList = datas;
            this.mTotal = this.mDataList.size();
            this.mPageSize = getPageSize(this.mTotal, this.mNums);
            this.mGridViewPagerAdapter = new GridViewPagerAdapter();
            this.mGridViewPagerAdapter.setNums(this.mNums);
            this.mGridViewPagerAdapter.setViews(this.mViews);
            setAdapter(this.mGridViewPagerAdapter);
            this.mGridViewPagerAdapter.setDatas(this.mDataList);
        }
    }

    public void updateDataSource(ArrayList<T> datas) {
        LogUtils.m1598d(this.TAG, "updateDataSource");
        if (datas == null || (datas != null && datas.size() < 1)) {
            reset();
        } else if (isEmpty()) {
            throw new IllegalArgumentException("please invoke setGridAdapter first!");
        } else {
            log("setDataSource!!!");
            int curPage = getCurrentItem();
            setCurrentItem(curPage);
            this.mDataList = datas;
            this.mTotal = this.mDataList.size();
            this.mPageSize = getPageSize(this.mTotal, this.mNums);
            this.mGridViewPagerAdapter.setDatas(this.mDataList);
            setCurrentItem(curPage);
        }
    }

    private void reset() {
        removeAllViews();
        if (!isEmpty()) {
            this.mLastPage = 0;
            Iterator it = this.mViews.iterator();
            while (it.hasNext()) {
                ((DrawingOrderGridView) it.next()).reset();
            }
            setCurrentItem(0, false);
        }
    }

    public void setSelectedPage(int page) {
        if (page != getCurrentItem()) {
            setCurrentItem(page, false);
        }
    }

    public void setPagePosition(int itemPosition) {
        checkState();
        DrawingOrderGridView curGridView = getCurGridView();
        curGridView.setPageNo(this.mLastPage);
        curGridView.setCurGridItem(itemPosition);
    }

    public void appendDataSource(ArrayList<T> datas) {
        if (datas == null || (datas != null && datas.size() < 1)) {
            throw new IllegalArgumentException("datas is illegal");
        } else if (isEmpty() || this.mGridViewPagerAdapter == null) {
            throw new IllegalArgumentException("please invoke setGridAdapter first!");
        } else {
            this.mDataList.addAll(datas);
            this.mGridViewPagerAdapter.setDatas(this.mDataList);
        }
    }

    public DrawingOrderGridView initGridView(Class<?> adapter) {
        DrawingOrderGridView gridView = new DrawingOrderGridView(this.mContext);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this.onItemClickListener);
        gridView.setOnFocusChangeListener(this.mChildFocusChangeListener);
        gridView.setPageViewListener(this.mPageViewListener);
        gridView.setNumColumns(this.mNumColumn);
        gridView.setHorizontalSpacing(this.mHorizontalSpacing);
        gridView.setVerticalSpacing(this.mVerticalSpacing);
        gridView.setSelector(this.mGridSelector);
        gridView.setDrawSelectorOnTop(this.mDrawSelectorOnTop);
        return gridView;
    }

    public boolean hasFocus() {
        if (isEmpty()) {
            return false;
        }
        Iterator it = this.mViews.iterator();
        while (it.hasNext()) {
            if (((DrawingOrderGridView) it.next()).hasFocus()) {
                return true;
            }
        }
        return super.hasFocus();
    }

    public DrawingOrderGridView getCurGridView() {
        checkState();
        return (DrawingOrderGridView) this.mViews.get(getCurrentItem() % this.mCachePageSize);
    }

    public DrawingOrderGridView getLastGridView() {
        checkState();
        return (DrawingOrderGridView) this.mViews.get(this.mLastPage % this.mCachePageSize);
    }

    private void checkState() {
        if (this.mViews == null) {
            throw new IllegalStateException("mViews = null, must invoke setGridAdapter firstly !");
        }
    }

    public ViewAdapter<?> getCurAdapter() {
        return (ViewAdapter) getCurGridView().getAdapter();
    }

    public int getPagePosition() {
        return getCurGridView().getCurGridItem();
    }

    public int getPageSize(int total, int nums) {
        if (total % nums == 0) {
            return total / nums;
        }
        return (total / nums) + 1;
    }

    private void setSelectedWhenTurnPage(int page, String name) {
        if (page != this.mLastPage) {
            int itemPosition = this.mLastPage > page ? getLastPos() : getFirstPos();
            log(new StringBuilder(String.valueOf(name)).append("  ,  page = ").append(page).append(" , item  =  ").append(itemPosition).toString());
            setPagePosition(itemPosition);
            getLastGridView().onFocusChange(null, false);
            getCurGridView().onFocusChange(null, true);
            this.mLastPage = page;
            return;
        }
        log(new StringBuilder(String.valueOf(name)).append("  ,  page = ").append(page).append("  相同不执行 ！").toString());
    }

    private int getFirstPos() {
        int pos = 0;
        for (int i = getLastGridView().getCurGridItem() / this.mNumColumn; i >= 0; i--) {
            pos = i * this.mNumColumn;
            if (pos < getCurAdapter().getCount()) {
                break;
            }
        }
        return pos;
    }

    private int getLastPos() {
        return Math.min((this.mNumColumn * ((getLastGridView().getCurGridItem() / this.mNumColumn) + 1)) - 1, getCurAdapter().getCount() - 1);
    }

    private void notifyPageSelected(int page) {
        Iterator it = this.mViews.iterator();
        while (it.hasNext()) {
            ((ViewAdapter) ((GridView) it.next()).getAdapter()).onPageSelected(page);
        }
    }

    private void notifyPageScrollStateChanged(int state) {
        Iterator it = this.mViews.iterator();
        while (it.hasNext()) {
            ((ViewAdapter) ((GridView) it.next()).getAdapter()).onPageScrollStateChanged(state);
        }
    }

    private void notifyPageScrolled(int page, float posOffset, int posOffsetPixels) {
        Iterator it = this.mViews.iterator();
        while (it.hasNext()) {
            ((ViewAdapter) ((GridView) it.next()).getAdapter()).onPageScrolled(page, posOffset, posOffsetPixels);
        }
    }

    private void onCompleteScroll() {
        if (!(this.mPageViewListener == null || getCurGridView() == null || getCurGridView().getChildAt(0) == null)) {
            this.mPageViewListener.onItemSelected(getCurGridView(), getCurGridView().getChildAt(0), getPagePosition(), 0);
        }
        if (getCurGridView().getAdapter() != null && (getCurGridView().getAdapter() instanceof ViewAdapter)) {
            ((ViewAdapter) getCurGridView().getAdapter()).onCompleteScroll();
        }
    }

    private void onStartScroll() {
        int length = this.mViews.size();
        for (int i = 0; i < length; i++) {
            ((ViewAdapter) ((DrawingOrderGridView) this.mViews.get(i)).getAdapter()).onStartScroll();
        }
    }

    private void setScrollerAttrs(Context context) {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mField.set(this, new FixedSpeedScroller(context, new AccelerateDecelerateInterpolator()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void log(String msg) {
        LogUtils.m1598d(this.TAG, msg);
    }

    public ArrayList<DrawingOrderGridView> getChildViews() {
        return this.mViews;
    }

    public void setNumColumn(int numColumn) {
        this.mNumColumn = numColumn;
        if (!isEmpty()) {
            Iterator it = this.mViews.iterator();
            while (it.hasNext()) {
                ((DrawingOrderGridView) it.next()).setNumColumns(this.mNumColumn);
            }
        }
    }

    public void setNumRow(int numRow) {
        this.mNumRow = numRow;
    }

    public int getPageCount() {
        return this.mPageSize;
    }

    public int getScrollDuration() {
        return this.mScrollDuration;
    }

    public void setScrollDuration(int scrollDuration) {
        this.mScrollDuration = scrollDuration;
        setScrollerAttrs(this.mContext);
    }

    public List<T> getDataSourceList() {
        return this.mDataList;
    }

    public void setGridSelector(Drawable drawable) {
        this.mGridSelector = drawable;
    }

    public void setGridSelector(int drawable) {
        this.mGridSelector = getResources().getDrawable(drawable);
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        this.mHorizontalSpacing = horizontalSpacing;
        if (!isEmpty()) {
            Iterator it = this.mViews.iterator();
            while (it.hasNext()) {
                ((DrawingOrderGridView) it.next()).setHorizontalSpacing(this.mHorizontalSpacing);
            }
        }
    }

    public void setVerticalSpacing(int verticalSpacing) {
        this.mVerticalSpacing = verticalSpacing;
        if (!isEmpty()) {
            Iterator it = this.mViews.iterator();
            while (it.hasNext()) {
                ((DrawingOrderGridView) it.next()).setVerticalSpacing(this.mVerticalSpacing);
            }
        }
    }

    public void setDrawSelectorOnTop(boolean drawSelectorOnTop) {
        this.mDrawSelectorOnTop = drawSelectorOnTop;
    }

    public void setNextFocusUpId(int nextFocusUpId) {
        this.mNextFocusUpID = nextFocusUpId;
        if (!isEmpty()) {
            Iterator it = this.mViews.iterator();
            while (it.hasNext()) {
                DrawingOrderGridView view = (DrawingOrderGridView) it.next();
                if (this.mNextFocusUpID == getId()) {
                    view.setNextFocusUpId(view.getId());
                } else {
                    view.setNextFocusUpId(this.mNextFocusUpID);
                }
            }
        }
    }

    public void setNextFocusDownId(int nextFocusDownId) {
        this.mNextFocusDownID = nextFocusDownId;
        if (!isEmpty()) {
            Iterator it = this.mViews.iterator();
            while (it.hasNext()) {
                DrawingOrderGridView view = (DrawingOrderGridView) it.next();
                if (this.mNextFocusDownID == getId()) {
                    view.setNextFocusDownId(view.getId());
                } else {
                    view.setNextFocusDownId(this.mNextFocusDownID);
                }
            }
        }
    }

    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        this.mOnFocusChangeListener = l;
    }

    private boolean isEmpty() {
        if (this.mViews == null || this.mViews.isEmpty()) {
            return true;
        }
        return false;
    }

    public void setColumnWidth(int columnWidth) {
        if (this.mViews == null || this.mViews.isEmpty()) {
            throw new IllegalStateException("setItemStretchMode() should be called after setGridAdapter!");
        }
        Iterator it = this.mViews.iterator();
        while (it.hasNext()) {
            ((DrawingOrderGridView) it.next()).setColumnWidth(columnWidth);
        }
    }

    public void setItemStretchMode(int stretchMode) {
        if (this.mViews == null || this.mViews.isEmpty()) {
            throw new IllegalStateException("setItemStretchMode() should be called after setGridAdapter!");
        }
        Iterator it = this.mViews.iterator();
        while (it.hasNext()) {
            ((DrawingOrderGridView) it.next()).setStretchMode(stretchMode);
        }
    }

    private Rect getBgDrawablePaddings(int resId) {
        Rect bgDrawablePaddings = new Rect();
        Drawable d = this.mContext.getResources().getDrawable(resId);
        if (d != null) {
            d.getPadding(bgDrawablePaddings);
        }
        LogUtils.m1598d(this.TAG, "getBgDrawablePaddings: " + bgDrawablePaddings);
        return bgDrawablePaddings;
    }

    private float calculateZoomRatio(int itemContentWidth, int itemContentSpacing) {
        float ratio = ((float) ((itemContentSpacing * 2) + itemContentWidth)) / ((float) itemContentWidth);
        LogUtils.m1598d(this.TAG, "calculateZoomRatio: " + ratio);
        return ratio;
    }

    public void setItemDimens(int[] basicDimens, int itemBgResId) {
        setItemDimens(basicDimens, itemBgResId, 0.0f, false);
    }

    public void setItemDimens(int[] basicDimens, int itemBgResId, float arbitraryZoomRatio, boolean innerBackground) {
        resetItemDimensIfNeeded();
        int itemContentWidth = basicDimens[0];
        int itemContentHeight = basicDimens[1];
        int itemContentSpacing = basicDimens[2];
        LogUtils.m1598d(this.TAG, "setItemDimens: itemContent w/h=" + itemContentWidth + "/" + itemContentHeight + ", contentSpacing=" + itemContentSpacing + ", itemBgResId=" + itemBgResId);
        Rect itemBgPadding = getBgDrawablePaddings(itemBgResId);
        LogUtils.m1598d(this.TAG, "setItemDimens: itemBgPadding=" + itemBgPadding);
        if (arbitraryZoomRatio <= 0.0f) {
            arbitraryZoomRatio = calculateZoomRatio(itemContentWidth, itemContentSpacing);
        }
        this.mZoomRatio = arbitraryZoomRatio;
        float zoomRatio = this.mZoomRatio;
        int itemHPadding = itemBgPadding.left + itemBgPadding.right;
        int actualVPadding = itemBgPadding.top + (innerBackground ? 0 : itemBgPadding.bottom);
        LogUtils.m1598d(this.TAG, "setItemDimens: actualVPadding=" + actualVPadding);
        int itemPaddedWidth = itemContentWidth + itemHPadding;
        int itemPaddedHeight = itemContentHeight + actualVPadding;
        LogUtils.m1598d(this.TAG, "setItemDimens: item padded w/h=" + itemPaddedWidth + "/" + itemPaddedHeight);
        int deltaWidth = Math.round((zoomRatio - 1.0f) * ((float) itemPaddedWidth));
        int deltaHeight = Math.round((zoomRatio - 1.0f) * ((float) itemPaddedHeight));
        LogUtils.m1598d(this.TAG, "setItemDimens: zoomRatio=" + zoomRatio + ", delta w/h=" + deltaWidth + "/" + deltaHeight);
        setItemStretchMode(0);
        int columnWidth = (itemContentWidth + itemHPadding) + deltaWidth;
        LogUtils.m1598d(this.TAG, "setItemDimens: columnWidth=" + columnWidth);
        setColumnWidth(columnWidth);
        int horizontalSpacing = (itemContentSpacing - itemHPadding) - deltaWidth;
        setHorizontalSpacing(horizontalSpacing);
        LogUtils.m1598d(this.TAG, "setItemDimens: column width=" + columnWidth + ", H spacing=" + horizontalSpacing);
        LayoutParams params = (MarginLayoutParams) getLayoutParams();
        int totalItemWidth = ((this.mNumColumn * itemContentWidth) + ((this.mNumColumn - 1) * itemContentSpacing)) + itemHPadding;
        int totalItemHeight = (this.mNumRow * itemContentHeight) + actualVPadding;
        LogUtils.m1598d(this.TAG, "setItemDimens: pager total w/h (w/o extra anim space)=" + totalItemWidth + "/" + totalItemHeight);
        params.width = totalItemWidth + deltaWidth;
        params.height = totalItemHeight + deltaHeight;
        this.mContentPadding = new Rect(Math.round((((float) deltaWidth) / 2.0f) + ((float) itemBgPadding.left)), Math.round((((float) deltaHeight) / 2.0f) + ((float) itemBgPadding.top)), Math.round((((float) deltaWidth) / 2.0f) + ((float) itemBgPadding.right)), Math.round((((float) deltaHeight) / 2.0f) + ((float) itemBgPadding.bottom)));
        LogUtils.m1598d(this.TAG, "setItemDimens: content padding=" + this.mContentPadding);
        if (this.mZoomEnabled) {
            this.mDeltaMarginLeft = -this.mContentPadding.left;
            params.leftMargin += this.mDeltaMarginLeft;
            this.mDeltaMarginTop = -((this.mContentPadding.top - Math.round(((zoomRatio - 1.0f) * ((float) itemContentHeight)) / 2.0f)) - 5);
            params.topMargin += this.mDeltaMarginTop;
        }
        setLayoutParams(params);
        LogUtils.m1598d(this.TAG, "setItemDimens: pager total w/h (w/ extra anim space)=" + params.width + "/" + params.height + ", deltaMargin=" + this.mDeltaMarginLeft);
        Iterator it = this.mViews.iterator();
        while (it.hasNext()) {
            ((DrawingOrderGridView) it.next()).setGravity(1);
        }
    }

    public Rect getContentPadding() {
        if (this.mContentPadding != null) {
            return this.mContentPadding;
        }
        throw new IllegalStateException("Content padding can be fetched only after setItemDimens() is called!");
    }

    private void resetItemDimensIfNeeded() {
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        if (!(this.mDeltaMarginLeft == 0 || params == null)) {
            params.leftMargin -= this.mDeltaMarginLeft;
            LogUtils.m1598d(this.TAG, "resetItemDimensIfNeeded: left margin reseted to " + params.leftMargin);
        }
        if (this.mDeltaMarginTop != 0 && params != null) {
            params.topMargin -= this.mDeltaMarginTop;
            LogUtils.m1598d(this.TAG, "resetItemDimensIfNeeded: top margin reseted to " + params.topMargin);
        }
    }

    public void setParentClipping(boolean clipping) {
        for (ViewParent parent = getParent(); parent instanceof ViewGroup; parent = parent.getParent()) {
            ((ViewGroup) parent).setClipChildren(clipping);
        }
    }

    public void setOnEdgeReachedListener(OnEdgeReachedListener listener) {
        this.mEdgeListener = listener;
    }

    public void setOnFocusableChangeListener(OnFocusableChangeListener listener) {
        this.mFocusableChangeListener = listener;
    }

    public void setZoomEnabled(boolean enable) {
        this.mZoomEnabled = enable;
    }

    public float getZoomRatio() {
        return this.mZoomRatio;
    }
}

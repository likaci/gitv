package com.gala.video.app.epg.home.widget.pager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import com.gala.video.app.epg.HomeDebug;
import com.gala.video.app.epg.home.widget.PagerAdapter;
import com.gala.video.app.epg.home.widget.ViewPager;
import com.gala.video.app.epg.home.widget.tabhost.TabBarHost;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScrollViewPager extends ViewPager {
    private static final int ITEM_OFFSET = 10000;
    private static final String TAG = "home/ScrollViewPager";
    private ViewPageAdapter mAdapter;
    private Interpolator mInterpolator;
    private boolean mIsLooper = false;
    private int mPageCount = 0;
    public List<ViewGroup> mPageViewList = new ArrayList();
    private int mScrollDuration = 300;
    private TabBarHost mTabbar;

    class C07221 implements Runnable {
        C07221() {
        }

        public void run() {
            ScrollViewPager.this.mTabbar.requestChildFocus(ScrollViewPager.this.mTabbar.getFocusChildIndex());
        }
    }

    class FixedSpeedScroller extends Scroller {
        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, ScrollViewPager.this.mScrollDuration);
        }

        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, ScrollViewPager.this.mScrollDuration);
        }
    }

    public static class ViewPageAdapter extends PagerAdapter {
        private static final String TAG = "home/ViewPageAdapter";
        private Context mContext;
        private boolean mIsLoop;
        private int mSize;
        private List<ViewGroup> mViewList;

        private ViewPageAdapter(Context context, List<ViewGroup> list, boolean isLoop) {
            this.mViewList = new ArrayList(8);
            this.mViewList.clear();
            this.mViewList.addAll(list);
            this.mIsLoop = isLoop;
            this.mSize = list.size();
            this.mIsLoop = isLoop;
            this.mContext = context;
        }

        private Set<ViewGroup> calculateUpdatedPages(List<ViewGroup> newPages, List<ViewGroup> oldPages) {
            Set<ViewGroup> result = new HashSet();
            for (int index = 0; index < newPages.size(); index++) {
                if (index < oldPages.size()) {
                    if (HomeDebug.DEBUG_LOG) {
                        LogUtils.m1568d(TAG, "new page = " + newPages.get(index) + ", old page = " + oldPages.get(index));
                    }
                    if (!((ViewGroup) newPages.get(index)).equals(oldPages.get(index))) {
                        result.add(newPages.get(index));
                    }
                } else {
                    result.add(newPages.get(index));
                }
            }
            return result;
        }

        public void updateViewList(List<ViewGroup> list) {
            this.mViewList.clear();
            this.mViewList.addAll(list);
            this.mSize = list.size();
        }

        public int getCount() {
            return this.mIsLoop ? Integer.MAX_VALUE : this.mSize;
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public int getItemPosition(Object object) {
            super.getItemPosition(object);
            if (HomeDebug.DEBUG_LOG) {
                LogUtils.m1568d(TAG, "getItemPosition object = " + object + " ret = " + -2);
            }
            return -2;
        }

        public Object instantiateItem(ViewGroup container, int position) {
            View child = (View) this.mViewList.get(position % this.mSize);
            if (child != null) {
                ViewGroup parent = (ViewGroup) child.getParent();
                if (parent != null) {
                    parent.removeView(child);
                }
                LogUtils.m1568d(TAG, "instantiate page position = " + position + "child = " + child);
                container.addView(child);
            } else {
                LogUtils.m1571e(TAG, "instantiate page ,child is not builded,position = " + position);
            }
            return child;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            LogUtils.m1568d(TAG, "destroyItem position = " + position + ", object = " + object + ",container = " + container);
            if (object instanceof View) {
                container.removeView((View) object);
            }
        }
    }

    public ScrollViewPager(Context context) {
        super(context);
        init(context);
    }

    public void setTabBarHost(TabBarHost bar) {
        this.mTabbar = bar;
    }

    public void clearChildFocus(View child) {
        super.clearChildFocus(child);
        LogUtils.m1568d(TAG, "clearChildFocus child@" + child);
        if (this.mTabbar != null) {
            this.mTabbar.post(new C07221());
        }
    }

    public void setLooper(boolean isLoop) {
        this.mIsLooper = isLoop;
    }

    public void addPageViews(List<ViewGroup> pageViews, int count, Context context) {
        if (HomeDebug.DEBUG_LOG) {
            for (int index = 0; index < pageViews.size(); index++) {
                LogUtils.m1568d(TAG, " index = " + index + ", page view = " + pageViews.get(index));
            }
        }
        this.mPageViewList.clear();
        this.mPageViewList.addAll(pageViews);
        this.mPageCount = this.mPageViewList.size();
        if (this.mAdapter == null) {
            this.mAdapter = new ViewPageAdapter(getContext(), this.mPageViewList, this.mIsLooper);
            setAdapter(this.mAdapter);
            if (this.mIsLooper) {
                setOffscreenPageLimit(this.mPageCount >> 1);
                super.setCurrentItem(this.mPageCount * 10000);
                return;
            }
            LogUtils.m1568d(TAG, "setOffscreenPageLimit mPageCount:" + this.mPageCount);
            setOffscreenPageLimit(this.mPageCount);
            return;
        }
        this.mAdapter.updateViewList(this.mPageViewList);
        this.mAdapter.notifyDataSetChanged();
        if (this.mIsLooper) {
            setOffscreenPageLimit(this.mPageCount >> 1);
            return;
        }
        LogUtils.m1568d(TAG, "update setOffscreenPageLimit mPageCount:" + this.mPageCount);
        setOffscreenPageLimit(this.mPageCount);
    }

    public ScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setScrollerAttrs();
        setFocusable(false);
    }

    public int getScrollDuration() {
        return this.mScrollDuration;
    }

    public void setScrollDuration(int scrollDuration) {
        this.mScrollDuration = scrollDuration;
        setScrollerAttrs();
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
        setScrollerAttrs();
    }

    public Interpolator getInterpolator() {
        return this.mInterpolator;
    }

    protected void setScrollerAttrs() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            if (this.mInterpolator == null) {
                this.mInterpolator = new AccelerateDecelerateInterpolator();
            }
            mField.set(this, new FixedSpeedScroller(getContext(), this.mInterpolator));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

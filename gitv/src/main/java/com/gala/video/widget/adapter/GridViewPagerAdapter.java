package com.gala.video.widget.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.gala.video.widget.util.LogUtils;
import com.gala.video.widget.view.DrawingOrderGridView;
import com.gala.video.widget.view.GridViewPager;
import java.util.ArrayList;
import java.util.List;

public class GridViewPagerAdapter<T> extends PagerAdapter {
    private static final String TAG = "gridpageview/GridViewPagerAdapter";
    private int mCachePageSize = 0;
    private List<T> mDataList;
    private boolean mIsLeft = false;
    private int mNums = 0;
    private int mPageSize;
    private List<DrawingOrderGridView> mViews;

    public void setDatas(ArrayList<T> dataList) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setDatas: " + dataList);
        }
        this.mDataList = dataList;
        this.mPageSize = getPageSize(dataList.size(), this.mNums);
        notifyDataSetChanged();
    }

    public void setNums(int nums) {
        this.mNums = nums;
    }

    public Object instantiateItem(ViewGroup container, int page) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> instantiateItem, page=" + page);
        }
        DrawingOrderGridView gridView = (DrawingOrderGridView) this.mViews.get(page % this.mCachePageSize);
        gridView.setPageNo(page);
        int size = this.mDataList.size();
        List list = this.mDataList;
        int i = this.mNums * page;
        if ((page + 1) * this.mNums < size) {
            size = (page + 1) * this.mNums;
        }
        List<T> subList = list.subList(i, size);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "instantiateItem: subList=" + subList);
        }
        ((ViewAdapter) gridView.getAdapter()).notifyDataSetChanged(subList, page, container);
        if (gridView.getParent() == null) {
            ((GridViewPager) container).addView(gridView);
        }
        if (this.mIsLeft) {
            if (gridView.getChildCount() < 1) {
                gridView.setCurGridItem(0);
            } else {
                gridView.setCurGridItem(gridView.getChildCount() - 1);
            }
        }
        return gridView;
    }

    public int getCount() {
        return this.mPageSize > 0 ? this.mPageSize : 0;
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public void setViews(List<DrawingOrderGridView> views) {
        this.mViews = views;
        this.mCachePageSize = this.mViews.size();
    }

    public int getPageSize(int total, int nums) {
        if (total % nums == 0) {
            return total / nums;
        }
        return (total / nums) + 1;
    }

    public void setLeft(boolean isLeft) {
        this.mIsLeft = isLeft;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        int data_size = this.mDataList.size();
        int view_size = this.mViews.size();
        if (this.mViews != null && this.mDataList != null) {
            for (int i = 0; i < view_size; i++) {
                ViewAdapter<T> adapter = (ViewAdapter) ((DrawingOrderGridView) this.mViews.get(i)).getAdapter();
                if (adapter != null) {
                    int end;
                    int view_page_no = ((DrawingOrderGridView) this.mViews.get(i)).getPageNo();
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(TAG, "notify grid view data change, size=" + view_size + ", data_size=" + data_size + ", mNums=" + this.mNums);
                    }
                    if ((view_page_no + 1) * this.mNums >= data_size) {
                        end = data_size;
                    } else {
                        end = (view_page_no + 1) * this.mNums;
                    }
                    adapter.notifyDataSetChanged(this.mDataList.subList(this.mNums * view_page_no, end));
                }
            }
        }
    }
}

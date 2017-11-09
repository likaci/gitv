package com.gala.video.widget.adapter;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;

public abstract class ViewAdapter<T> extends BaseAdapter implements OnPageChangeListener {
    public void notifyDataSetChanged(List<T> datas, int dataPage, ViewGroup container) {
        notifyDataSetChanged(datas);
    }

    public void notifyDataSetChanged(List<T> list) {
    }

    public void onStartScroll() {
    }

    public void onCompleteScroll() {
    }

    public void onPageScrollStateChanged(int state) {
    }

    public void onPageScrolled(int page, float posOffset, int posOffsetPixels) {
    }

    public void onPageSelected(int selectedPage) {
    }
}

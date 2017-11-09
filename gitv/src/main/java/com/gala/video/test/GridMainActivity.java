package com.gala.video.test;

import android.app.Activity;
import android.os.Bundle;
import com.gala.video.widget.test.R;
import com.gala.video.widget.view.GridViewPager;
import java.util.ArrayList;

public class GridMainActivity extends Activity {
    private ArrayList<GalleryData> mDatas = new ArrayList();
    private ArrayList<GalleryData> mDatas2 = new ArrayList();
    private GridViewPager<GalleryData> mPager;
    private GridViewPager<GalleryData> mPager2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridmain);
        initGalleryPager();
        initGalleryPager2();
    }

    private void initGalleryPager() {
        this.mPager = (GridViewPager) findViewById(R.id.view_gridviewpager);
        this.mPager.setZoomEnabled(true);
        this.mPager.setOffscreenPageLimit(2);
        this.mPager.setNumColumn(7);
        this.mPager.setNumRow(1);
        this.mPager.setGridAdapter(PortraitVideoAdapter.class);
        int itemContentWidth = getResources().getDimensionPixelSize(R.dimen.dimen_133dp);
        int itemContentHeight = getResources().getDimensionPixelSize(R.dimen.dimen_251dp);
        int itemContentSpacing = getResources().getDimensionPixelSize(R.dimen.dimen_36dp);
        this.mPager.setItemDimens(new int[]{itemContentWidth, itemContentHeight, itemContentSpacing}, R.drawable.bg_unfocus, 1.05f, true);
        for (int i = 0; i < 10; i++) {
            this.mDatas.add(new GalleryData(i + 100, "DATA#" + (i + 1)));
        }
        this.mPager.setDataSource(this.mDatas);
    }

    private void initGalleryPager2() {
        this.mPager2 = (GridViewPager) findViewById(R.id.view_gridviewpager2);
        this.mPager2.setZoomEnabled(true);
        this.mPager2.setOffscreenPageLimit(2);
        this.mPager2.setNumColumn(7);
        this.mPager2.setNumRow(1);
        this.mPager2.setGridAdapter(PortraitVideoAdapter.class);
        int itemContentWidth = getResources().getDimensionPixelSize(R.dimen.dimen_133dp);
        int itemContentHeight = getResources().getDimensionPixelSize(R.dimen.dimen_251dp);
        int itemContentSpacing = getResources().getDimensionPixelSize(R.dimen.dimen_36dp);
        this.mPager2.setItemDimens(new int[]{itemContentWidth, itemContentHeight, itemContentSpacing}, R.drawable.bg_unfocus, 1.05f, true);
        for (int i = 0; i < 10; i++) {
            this.mDatas2.add(new GalleryData(i + 100, "DATA#" + (i + 1)));
        }
        this.mPager2.setDataSource(this.mDatas2);
    }
}

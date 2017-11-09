package com.gala.video.app.player.ui.widget.tabhost;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.gala.video.app.player.R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.widget.util.AnimationUtils;

public class VerticalTabHost extends MyTabHost {
    private static final String TAG = "Player/Ui/VerticalTabHost";
    private TabWidgetGravity mTabWidgetGravity = TabWidgetGravity.RIGHT;

    public enum TabWidgetGravity {
        LEFT,
        RIGHT
    }

    public VerticalTabHost(Context context) {
        super(context);
    }

    public VerticalTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void setTabWidgetGravity(TabWidgetGravity gravity) {
        this.mTabWidgetGravity = gravity;
    }

    protected void onFinishInflate() {
        int i = -2;
        LogUtils.d(TAG, "onFinishInflate: child count=" + getChildCount());
        int childCount = getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            this.mTabContentChildren.put(getChildAt(i2), getChildAt(i2).getLayoutParams());
        }
        removeAllViews();
        this.mOuterLayout = new LinearLayout(this.mContext);
        this.mOuterLayout.setOrientation(0);
        this.mOuterLayout.setGravity(1);
        MyTabWidget tab = new MyTabWidget(this.mContext);
        tab.setOrientation(1);
        tab.setId(16908307);
        tab.setTabFocusListener(this);
        tab.setOnMeasureListener(this);
        FrameLayout contentLayout = new FrameLayout(this.mContext);
        contentLayout.setId(16908305);
        for (View v : this.mTabContentChildren.keySet()) {
            contentLayout.addView(v, (LayoutParams) this.mTabContentChildren.get(v));
        }
        LinearLayout linearLayout;
        int i3;
        if (this.mTabWidgetGravity == TabWidgetGravity.LEFT) {
            linearLayout = this.mOuterLayout;
            i3 = this.mTabWidgetWidth > 0 ? this.mTabWidgetWidth : -2;
            if (this.mTabWidgetHeight > 0) {
                i = this.mTabWidgetHeight;
            }
            linearLayout.addView(tab, i3, i);
            this.mOuterLayout.addView(contentLayout, -1, -1);
        } else {
            linearLayout = this.mOuterLayout;
            if (this.mTabContentWidth > 0) {
                i3 = this.mTabContentWidth;
            } else {
                i3 = -1;
            }
            linearLayout.addView(contentLayout, i3, -1);
            linearLayout = this.mOuterLayout;
            if (this.mTabWidgetWidth > 0) {
                i = this.mTabWidgetWidth;
            }
            if (this.mTabWidgetHeight > 0) {
                i3 = this.mTabWidgetHeight;
            } else {
                i3 = -1;
            }
            linearLayout.addView(tab, i, i3);
        }
        addView(this.mOuterLayout, -1, -1);
        setup();
    }

    public void onTabWidgetMeasured(int childWidth, int childHeight) {
        LogUtils.d(TAG, "onTabWidgetMeasured: childHeight=" + childHeight);
        int heightW = (childHeight - ((int) (((float) childHeight) * (AnimationUtils.getDefaultZoomRatio() - 1.0f)))) + (this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_1dp) * 2);
        LogUtils.d(TAG, "heightW: childHeight=" + childHeight);
        MyTabWidget myTabWidget = (MyTabWidget) getTabWidget();
        int size = myTabWidget.getVisibleTabCount();
        for (int i = 0; i < size; i++) {
            View view = myTabWidget.getVisibleTabAt(i);
            RelativeLayout indicatorLayout = (RelativeLayout) view.findViewById(R.id.ll_indicator);
            LogUtils.d(TAG, "wigetW: indicatorLayout=" + indicatorLayout);
            if (indicatorLayout != null) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) indicatorLayout.getLayoutParams();
                LogUtils.d(TAG, "childHeight: lp =" + lp);
                if (lp != null) {
                    lp.height = heightW;
                    int width = lp.width;
                    lp.width = width - ((int) (((float) width) * (AnimationUtils.getDefaultZoomRatio() - 1.0f)));
                    LogUtils.d(TAG, "heightW: heightW=" + heightW + ", view.getPaddingLeft()=" + view.getPaddingLeft() + ",view.getPaddingRight()=" + view.getPaddingRight());
                    indicatorLayout.setLayoutParams(lp);
                }
            }
        }
        myTabWidget.removeOnMeasureListener();
    }

    public void updateTabWidetHeight(int tabCount) {
        if (tabCount <= 1) {
            MyTabWidget tabWidget = (MyTabWidget) getTabWidget();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabWidget.getLayoutParams();
            params.height /= 2;
            tabWidget.setLayoutParams(params);
        }
    }
}

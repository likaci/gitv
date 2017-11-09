package com.gala.video.app.player.ui.widget.tabhost;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabWidget;
import com.gala.video.app.player.R;
import com.gala.video.app.player.ui.widget.tabhost.MyTabWidget.TabFocusListener;
import com.gala.video.app.player.ui.widget.tabhost.MyTabWidget.TabWidgetMeasureListener;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.HashMap;

public class MyTabHost extends TabHost implements OnTabChangeListener, TabFocusListener, TabWidgetMeasureListener {
    private static final String TAG = "MyTabHost";
    protected int mContentHeight;
    private FrameLayout mContentLayout;
    protected Context mContext;
    protected LinearLayout mOuterLayout;
    private TabChangeListener mTabChangeListener;
    protected HashMap<View, LayoutParams> mTabContentChildren = new HashMap();
    protected int mTabContentWidth;
    private int mTabCount = 0;
    private TabFocusListener mTabFocusListener;
    private TabVisibilityChangeListener mTabVisibilityChangeListener;
    protected int mTabWidgetHeight;
    protected int mTabWidgetWidth;

    public interface TabChangeListener {
        void onTabChanged(String str);

        void onTabCountChanged(int i);
    }

    public interface TabVisibilityChangeListener {
        void onVisibilityChanged(int i);
    }

    public MyTabHost(Context context) {
        super(context);
        initViews(context);
    }

    public MyTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    private void initViews(Context context) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "initViews: child count=" + getChildCount());
        }
        this.mContext = context;
        setOnTabChangedListener(this);
        setClipChildren(false);
        setClipToPadding(false);
    }

    private MyTabWidget getMyTabWidget() {
        return (MyTabWidget) super.getTabWidget();
    }

    public void setMaxTabCount(int maxTabCount) {
        getMyTabWidget().setMaxTabCount(maxTabCount);
    }

    protected void onFinishInflate() {
        int i;
        super.onFinishInflate();
        LogUtils.d(TAG, "onFinishInflate: child count=" + getChildCount());
        int childCount = getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            this.mTabContentChildren.put(getChildAt(i2), getChildAt(i2).getLayoutParams());
        }
        removeAllViews();
        this.mOuterLayout = new LinearLayout(this.mContext);
        this.mOuterLayout.setOrientation(1);
        MyTabWidget tab = new MyTabWidget(this.mContext);
        tab.setId(16908307);
        this.mOuterLayout.addView(tab, -1, this.mTabWidgetHeight > 0 ? this.mTabWidgetHeight : -2);
        tab.setTabFocusListener(this);
        tab.setOnMeasureListener(this);
        this.mContentLayout = new FrameLayout(this.mContext);
        this.mContentLayout.setId(16908305);
        for (View v : this.mTabContentChildren.keySet()) {
            this.mContentLayout.addView(v, (LayoutParams) this.mTabContentChildren.get(v));
        }
        LinearLayout linearLayout = this.mOuterLayout;
        View view = this.mContentLayout;
        if (this.mContentHeight > 0) {
            i = this.mContentHeight;
        } else {
            i = -1;
        }
        linearLayout.addView(view, -1, i);
        addView(this.mOuterLayout, -1, -1);
        setup();
    }

    public void addViewToTabContent(View view) {
        if (view != null) {
            ViewParent parent = view.getParent();
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "addViewToTabContent: view=" + view + ", parent=" + parent + ", mContentLayout=" + this.mContentLayout);
            }
            if (parent == null || !parent.equals(this.mContentLayout)) {
                this.mContentLayout.addView(view);
            }
        }
    }

    public void onTabWidgetMeasured(int childWidth, int childHeight) {
        LogUtils.d(TAG, "wigetW: childWidth=" + childWidth + ",childHeight=" + childHeight);
        MyTabWidget myTabWidget = (MyTabWidget) getTabWidget();
        int size = myTabWidget.getVisibleTabCount();
        for (int i = 0; i < size; i++) {
            View view = myTabWidget.getVisibleTabAt(i);
            LinearLayout indicatorLayout = (LinearLayout) view.findViewById(R.id.ll_indicator);
            LogUtils.d(TAG, "wigetW: indicatorLayout=" + indicatorLayout);
            if (indicatorLayout != null) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) indicatorLayout.getLayoutParams();
                LogUtils.d(TAG, "wigetW: lp =" + lp);
                if (lp != null) {
                    lp.width = this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_210dp);
                    LogUtils.d(TAG, "view.getPaddingLeft()=" + view.getPaddingLeft() + ",view.getPaddingRight()=" + view.getPaddingRight());
                    indicatorLayout.setLayoutParams(lp);
                }
            }
        }
        myTabWidget.removeOnMeasureListener();
    }

    public void setTabWidgetWidth(int widthPx) {
        LogUtils.d(TAG, "setTabWidgetWidth:" + widthPx);
        if (this.mOuterLayout != null) {
            View tabWidget = this.mOuterLayout.findViewById(16908307);
            LayoutParams params = tabWidget.getLayoutParams();
            if (params != null) {
                params.width = widthPx;
                tabWidget.setLayoutParams(params);
                return;
            }
            return;
        }
        this.mTabWidgetWidth = widthPx;
    }

    public void setTabContentWidth(int widthPx) {
        if (this.mOuterLayout != null) {
            View contentLayout = this.mOuterLayout.findViewById(16908305);
            LayoutParams params = contentLayout.getLayoutParams();
            if (params != null) {
                params.width = widthPx;
                contentLayout.setLayoutParams(params);
                return;
            }
            return;
        }
        this.mTabContentWidth = widthPx;
    }

    public void setTabWidgetHeight(int heightPx) {
        LogUtils.d(TAG, "setTabWidgetHeight:" + heightPx);
        if (this.mOuterLayout != null) {
            View tabWidget = this.mOuterLayout.findViewById(16908307);
            LayoutParams params = tabWidget.getLayoutParams();
            if (params != null) {
                params.height = heightPx;
                tabWidget.setLayoutParams(params);
                return;
            }
            return;
        }
        this.mTabWidgetHeight = heightPx;
    }

    public void setTabContentHeight(int heightPx) {
        if (this.mOuterLayout != null) {
            View contentLayout = this.mOuterLayout.findViewById(16908305);
            LayoutParams params = contentLayout.getLayoutParams();
            if (params != null) {
                params.height = heightPx;
                contentLayout.setLayoutParams(params);
                return;
            }
            return;
        }
        this.mContentHeight = heightPx;
    }

    public void addNewTab(String tag, View indicatorView, int tabContentResId) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "addNewTab: tag=" + tag + ", indicator view={" + indicatorView + "}, content id=" + tabContentResId);
        }
        indicatorView.setTag(tag);
        addTab(newTabSpec(tag).setIndicator(indicatorView).setContent(tabContentResId));
        onTabChanged(getCurrentTabTag());
        if (this.mTabChangeListener != null) {
            this.mTabChangeListener.onTabCountChanged(getIndicatorCount());
        }
    }

    public void addNewTab(String tag, View indicatorView, final View tabContent) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "addNewTab: tag=" + tag + ", indicatorView={" + indicatorView + "}, tabContent=" + tabContent);
        }
        indicatorView.setTag(tag);
        addTab(newTabSpec(tag).setIndicator(indicatorView).setContent(new TabContentFactory() {
            public View createTabContent(String tag) {
                tabContent.setTag(tag);
                return tabContent;
            }
        }));
        onTabChanged(getCurrentTabTag());
        if (this.mTabChangeListener != null) {
            this.mTabChangeListener.onTabCountChanged(getIndicatorCount());
        }
    }

    public void hideTabByTag(String tag) {
        if (tag != null) {
            TabWidget tabWidget = getTabWidget();
            int tabCount = tabWidget.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                View tab = tabWidget.getChildTabViewAt(i);
                if (tag.equals(tab.getTag())) {
                    tab.setVisibility(8);
                    if (this.mTabChangeListener != null) {
                        this.mTabChangeListener.onTabCountChanged(getIndicatorCount());
                        return;
                    }
                    return;
                }
            }
        }
    }

    public void showTabByTag(String tag) {
        if (tag != null) {
            TabWidget tabWidget = getTabWidget();
            int tabCount = tabWidget.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                View tab = tabWidget.getChildTabViewAt(i);
                if (tag.equals(tab.getTag())) {
                    tab.setVisibility(0);
                    if (this.mTabChangeListener != null) {
                        this.mTabChangeListener.onTabCountChanged(getIndicatorCount());
                        return;
                    }
                    return;
                }
            }
        }
    }

    public void setTabChangeListener(TabChangeListener listener) {
        this.mTabChangeListener = listener;
    }

    public void onTabChanged(String tag) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onTabChanged: " + tag);
        }
        if (this.mTabChangeListener != null) {
            this.mTabChangeListener.onTabChanged(tag);
        }
    }

    public void setDividerDrawable(Drawable d) {
        getTabWidget().setDividerDrawable(d);
    }

    public View getIndicatorAt(int index) {
        return ((MyTabWidget) getTabWidget()).getVisibleTabAt(index);
    }

    public int getIndicatorCount() {
        return ((MyTabWidget) getTabWidget()).getVisibleTabCount();
    }

    public View getCurrentIndicator() {
        return getCurrentTabView();
    }

    public void clearAllTabs() {
        super.clearAllTabs();
        FrameLayout contentLayout = (FrameLayout) this.mOuterLayout.findViewById(16908305);
        for (View v : this.mTabContentChildren.keySet()) {
            contentLayout.addView(v, (LayoutParams) this.mTabContentChildren.get(v));
            if (v.getVisibility() != 8) {
                v.setVisibility(8);
            }
        }
    }

    public void onTabFocusChange(View tabView, boolean hasFocus) {
        if (this.mTabFocusListener != null) {
            this.mTabFocusListener.onTabFocusChange(tabView, hasFocus);
        }
    }

    public void setTabFocusListener(TabFocusListener listener) {
        this.mTabFocusListener = listener;
    }

    public void setTabVisibilityChangeListener(TabVisibilityChangeListener listener) {
        this.mTabVisibilityChangeListener = listener;
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        LogUtils.e(TAG, "onVisibilityChanged visibility=" + visibility);
        if (this.mTabVisibilityChangeListener != null) {
            this.mTabVisibilityChangeListener.onVisibilityChanged(visibility);
        }
    }

    public void updateTabWidetHeight(int tabCount) {
    }

    public void onTouchModeChanged(boolean isInTouchMode) {
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "dispatchKeyEvent (" + event + " )");
        }
        if (event.getKeyCode() == 25 || event.getKeyCode() == 164 || event.getKeyCode() == 24) {
            return false;
        }
        return super.dispatchKeyEvent(event);
    }
}

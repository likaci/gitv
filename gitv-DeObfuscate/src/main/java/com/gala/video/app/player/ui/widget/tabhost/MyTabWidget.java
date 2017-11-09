package com.gala.video.app.player.ui.widget.tabhost;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabWidget;
import com.gala.video.app.player.C1291R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyTabWidget extends TabWidget {
    private static final int TAB_COUNT_MAX = 5;
    private static final int TAB_COUNT_VERTICAL_MAX = 6;
    private static final String TAG = "Player/Ui/MyTabWidget";
    private boolean mAutoRequestFocus = true;
    private AtomicBoolean mInitReady = new AtomicBoolean(false);
    private int mTabCountMax = 5;
    private TabFocusListener mTabFocusListener;
    private TabWidgetMeasureListener mWidgetMeasureListener;

    public interface TabFocusListener {
        void onTabFocusChange(View view, boolean z);
    }

    public interface TabWidgetMeasureListener {
        void onTabWidgetMeasured(int i, int i2);
    }

    public MyTabWidget(Context context) {
        super(context);
        setClipChildren(false);
    }

    public MyTabWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClipChildren(false);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LogUtils.m1568d(TAG, "onMeasure widthMeasureSpec:" + widthMeasureSpec + ",heightMeasureSpec:" + heightMeasureSpec);
        if (getOrientation() == 1) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        LogUtils.m1568d(TAG, "onMeasure: old width=" + width + ", mode=" + widthMode);
        int newWidth = ((int) getContext().getResources().getDimension(C1291R.dimen.dimen_210dp)) * getVisibleTabCount();
        LogUtils.m1568d(TAG, "onMeasure: new width=" + newWidth);
        super.onMeasure(MeasureSpec.makeMeasureSpec(newWidth, widthMode), heightMeasureSpec);
    }

    public void setMaxTabCount(int maxTabCount) {
        this.mTabCountMax = maxTabCount;
        requestLayout();
    }

    public void setOnMeasureListener(TabWidgetMeasureListener listener) {
        this.mWidgetMeasureListener = listener;
    }

    public void removeOnMeasureListener() {
        this.mWidgetMeasureListener = null;
    }

    public int getVisibleTabCount() {
        int visibleTabCount = 0;
        int tabCount = getTabCount();
        for (int i = 0; i < tabCount; i++) {
            if (getChildTabViewAt(i).getVisibility() == 0) {
                visibleTabCount++;
            }
        }
        return visibleTabCount;
    }

    public View getVisibleTabAt(int index) {
        int visibleIndex = -1;
        int tabCount = getTabCount();
        int absPos = 0;
        while (absPos < tabCount) {
            if (getChildTabViewAt(absPos).getVisibility() == 0) {
                visibleIndex++;
            }
            if (visibleIndex == index) {
                break;
            }
            absPos++;
        }
        return getChildTabViewAt(absPos);
    }

    public void setTabFocusListener(TabFocusListener listener) {
        this.mTabFocusListener = listener;
    }

    public void onFocusChange(View v, boolean hasFocus) {
        super.onFocusChange(v, hasFocus);
        if (v != this && this.mTabFocusListener != null) {
            this.mTabFocusListener.onTabFocusChange(v, hasFocus);
        }
    }

    public void addView(View child) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "1111 child id=" + child.getId() + ", hasFocus=" + child.hasFocus() + ", isFocusable=" + child.isFocusable() + ", isFocused=" + child.isFocused());
        }
        super.addView(child);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "2222 child id=" + child.getId() + ", hasFocus=" + child.hasFocus() + ", isFocusable=" + child.isFocusable() + ", isFocused=" + child.isFocused());
        }
        if (getOrientation() == 1) {
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            layoutParams.width = -1;
            child.setLayoutParams(layoutParams);
        }
    }

    public synchronized void setAutoRequestFocus(boolean request) {
        this.mAutoRequestFocus = request;
    }

    public void focusCurrentTab(int index) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> focusCurrentTab(), index=" + index);
        }
        if (this.mInitReady.get() || this.mAutoRequestFocus) {
            super.focusCurrentTab(index);
        } else {
            setCurrentTab(index);
        }
        this.mInitReady.set(true);
    }
}

package com.gala.video.widget.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import com.gala.video.widget.IPageViewListener;
import com.gala.video.widget.adapter.ViewAdapter;
import com.gala.video.widget.util.LogUtils;
import com.gala.video.widget.util.ViewUtils;

public class DrawingOrderGridView extends GridView implements OnFocusChangeListener {
    private static final int NO_SELECTED = -1;
    private static final String TAG = "gridpageview/DrawingOrderGridView";
    private int mCurGridItem;
    private int mCurKeyCode;
    private int mLastPosition;
    private LayoutChildrenListener mLayoutChildrenListener;
    private OnItemSelectedListener mOnItemSelectedListener = new C18871();
    private OnFocusChangeListener mPageFocusChangeListener;
    private int mPageNo = 0;
    private IPageViewListener mPageViewListener = null;

    class C18871 implements OnItemSelectedListener {
        C18871() {
        }

        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            DrawingOrderGridView.this.log("onItemSelected-- page:" + DrawingOrderGridView.this.mPageNo + " , item:" + position + "   ,hasFocus() =" + DrawingOrderGridView.this.hasFocus());
            if (DrawingOrderGridView.this.hasFocus()) {
                if (DrawingOrderGridView.this.mCurGridItem > -1) {
                    DrawingOrderGridView.this.clearChildFocus();
                }
                DrawingOrderGridView.this.setCurGridItem(position);
                DrawingOrderGridView.this.setFocusChildStyle(view, true);
                if (DrawingOrderGridView.this.mPageViewListener != null) {
                    DrawingOrderGridView.this.mPageViewListener.onItemSelected(parent, view, position, id);
                }
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public interface LayoutChildrenListener {
        void onLayoutBegin();

        void onLayoutEnd();
    }

    public DrawingOrderGridView(Context context) {
        super(context);
        init();
    }

    public DrawingOrderGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DrawingOrderGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setLayoutChildrenListener(LayoutChildrenListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1598d(TAG, "setLayoutChildrenListener ( " + listener + ")");
        }
        this.mLayoutChildrenListener = listener;
    }

    protected void layoutChildren() {
        LogUtils.m1598d(TAG, "layoutChildren " + this.mLayoutChildrenListener);
        if (this.mLayoutChildrenListener != null) {
            this.mLayoutChildrenListener.onLayoutBegin();
        }
        try {
            super.layoutChildren();
        } catch (Exception e) {
            LogUtils.m1598d(TAG, "layoutChildren on framework exception!");
            e.printStackTrace();
        }
        if (this.mLayoutChildrenListener != null) {
            this.mLayoutChildrenListener.onLayoutEnd();
        }
    }

    private void init() {
        setId(ViewUtils.generateViewId());
        setCacheColorHint(0);
        setStretchMode(2);
        setVerticalScrollBarEnabled(false);
        setChildrenDrawingOrderEnabled(true);
        super.setOnFocusChangeListener(this);
        setFocusable(true);
    }

    public void setAdapter(Class<?> adapter) {
        log("setAdapter: " + adapter);
        try {
            setAdapter((ViewAdapter) adapter.getConstructor(new Class[]{Context.class}).newInstance(new Object[]{getContext()}));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setOnItemSelectedListener(this.mOnItemSelectedListener);
    }

    public void onFocusChange(View v, boolean hasFocus) {
        log("onFocusChange: page:" + this.mPageNo + ", item:" + this.mCurGridItem + ", hasFocus:" + hasFocus);
        setFocusChildStyle(getSelectedView(), hasFocus());
        if (this.mPageFocusChangeListener != null) {
            this.mPageFocusChangeListener.onFocusChange(v, hasFocus());
        }
    }

    protected int getChildDrawingOrder(int childCount, int i) {
        int selectedIndex = getSelectedItemPosition() - getFirstVisiblePosition();
        if (i == 0) {
            this.mLastPosition = 0;
        }
        if (i == childCount - 1) {
            this.mLastPosition = childCount - 1;
        }
        if (selectedIndex < 0) {
            return i;
        }
        int ret;
        if (i == childCount - 1) {
            ret = selectedIndex;
        } else if (i >= selectedIndex) {
            this.mLastPosition++;
            ret = childCount - this.mLastPosition;
        } else {
            ret = i;
        }
        if (ret < 0 || ret > childCount - 1) {
            ret = 0;
        }
        return ret;
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        log("onFocusChanged   :  page:" + this.mPageNo + "  ,item = " + this.mCurGridItem + "   , hasFocus:" + gainFocus);
        if (gainFocus) {
            setSelection(this.mCurGridItem);
            this.mOnItemSelectedListener.onItemSelected(this, getSelectedView(), this.mCurGridItem, 0);
        }
    }

    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        this.mPageFocusChangeListener = l;
    }

    private void log(String msg) {
        LogUtils.m1598d(TAG, msg);
    }

    public void setCurGridItem(int curGridItem) {
        this.mCurGridItem = curGridItem;
        setSelection(curGridItem);
    }

    public void setFocusChildStyle(View child, boolean hasFocus) {
        if (child != null) {
            if (child instanceof OnFocusChangeListener) {
                ((OnFocusChangeListener) child).onFocusChange(child, hasFocus);
            } else if (child.getOnFocusChangeListener() != null) {
                child.getOnFocusChangeListener().onFocusChange(child, hasFocus);
            } else {
                throw new IllegalStateException("adaptview child must implements OnFocusChangeListener or has OnFocusChangeListener !");
            }
        }
    }

    public void clearChildFocus() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            setFocusChildStyle(getChildAt(i), false);
        }
    }

    public int getPageNo() {
        return this.mPageNo;
    }

    public void setPageNo(int mPageNo) {
        this.mPageNo = mPageNo;
    }

    public void setPageViewListener(IPageViewListener pageViewListener) {
        this.mPageViewListener = pageViewListener;
    }

    public int getCurGridItem() {
        return this.mCurGridItem;
    }

    public int getCurKeyCode() {
        return this.mCurKeyCode;
    }

    public void setCurKeyCode(int mCurKeyCode) {
        this.mCurKeyCode = mCurKeyCode;
    }

    public void reset() {
        removeAllViewsInLayout();
        this.mCurGridItem = 0;
        this.mPageNo = 0;
        this.mLastPosition = 0;
        this.mCurKeyCode = 0;
        setSelection(0);
    }
}

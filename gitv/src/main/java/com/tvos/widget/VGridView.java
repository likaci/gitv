package com.tvos.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import com.tvos.widget.VScrollView.OnScrollListener;

public class VGridView extends FrameLayout {
    public static final int ARRAY_TYPE_HORIZONTAL = 0;
    public static final int ARRAY_TYPE_VERTICAL = 1;
    private int mArrayNum = 1;
    private int mArrayType = 0;
    private boolean mDataChanged = false;
    private AdapterDataSetObserver mDataSetObserver;
    private int mHorizontalSpace;
    private boolean[] mItemVisibilities;
    private int mLastKeyCode;
    private int mLastScrollX = 0;
    private int mLastScrollY = 0;
    private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                VGridView.this.mVGridLayout.setOnTop(v);
            }
            if (VGridView.this.mOnItemFocusChangeListener != null) {
                VGridView.this.mOnItemFocusChangeListener.onItemFocusChange(v, VGridView.this.mVGridLayout.indexOfChild(v), hasFocus);
            }
            VGridView.this.mVGridLayout.invalidate();
        }
    };
    private OnItemFocusChangeListener mOnItemFocusChangeListener;
    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        public void onScroll(int x, int y) {
            if ((VGridView.this.mLastKeyCode == 22 && x > VGridView.this.mLastScrollX) || ((VGridView.this.mLastKeyCode == 21 && x < VGridView.this.mLastScrollX) || ((VGridView.this.mLastKeyCode == 19 && y > VGridView.this.mLastScrollY) || (VGridView.this.mLastKeyCode == 20 && y > VGridView.this.mLastScrollX)))) {
                VGridView.this.mLastScrollX = x;
                VGridView.this.mLastScrollY = y;
                for (int i = 0; i < VGridView.this.mVAdapter.getCount(); i++) {
                    View view = VGridView.this.mVGridLayout.getChildAt(i);
                    boolean visibility = true;
                    if (view.getRight() < x || view.getLeft() > VGridView.this.mVScrollView.getRight() + x || view.getBottom() < y || view.getTop() > VGridView.this.mVScrollView.getBottom() + y) {
                        visibility = false;
                    }
                    if (visibility != VGridView.this.mItemVisibilities[i]) {
                        VGridView.this.mItemVisibilities[i] = visibility;
                        if (VGridView.this.mItemVisibilities[i]) {
                            VGridView.this.mVAdapter.onScrollItemIn(i, view);
                        } else {
                            VGridView.this.mVAdapter.onScrollItemOut(i, view);
                        }
                    }
                }
            }
        }
    };
    private VAdapter mVAdapter;
    private VGridLayout mVGridLayout;
    private VScrollView mVScrollView;
    private int mVerticalSpace;

    public interface OnItemFocusChangeListener {
        void onItemFocusChange(View view, int i, boolean z);
    }

    class AdapterDataSetObserver extends DataSetObserver {
        AdapterDataSetObserver() {
        }

        public void onChanged() {
            VGridView.this.requestLayout();
        }

        public void onInvalidated() {
            VGridView.this.requestLayout();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == 19 || event.getKeyCode() == 20 || event.getKeyCode() == 21 || event.getKeyCode() == 22) {
            this.mLastKeyCode = event.getKeyCode();
        }
        return super.dispatchKeyEvent(event);
    }

    public VGridView(Context context) {
        super(context);
        initViews(context);
    }

    public VGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public VGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }

    private void initViews(Context context) {
        setChildrenDrawingOrderEnabled(true);
        this.mVScrollView = new VScrollView(context);
        this.mVScrollView.setOnScrollListener(this.mOnScrollListener);
        this.mVGridLayout = new VGridLayout(context);
        this.mVScrollView.addView(this.mVGridLayout, new LayoutParams(-2, -2));
        addView(this.mVScrollView, new LayoutParams(-1, -1));
        setClipChildren(false);
        this.mVGridLayout.setClipChildren(false);
        this.mVScrollView.setClipChildren(false);
    }

    public void setOnItemFocusChangeListener(OnItemFocusChangeListener listener) {
        this.mOnItemFocusChangeListener = listener;
    }

    public void setScrollMarginTop(int top) {
        this.mVScrollView.setScrollMarginTop(top);
    }

    public void setScrollMarginBottom(int bottom) {
        this.mVScrollView.setScrollMarginBottom(bottom);
    }

    public void setScrollMarginLeft(int left) {
        this.mVScrollView.setScrollMarginLeft(left);
    }

    public void setScrollMarginRight(int right) {
        this.mVScrollView.setScrollMarginRight(right);
    }

    public void setArrayType(int arrayType, int arrayNum) {
        this.mArrayType = arrayType;
        this.mArrayNum = arrayNum;
    }

    public void setVerticalSpace(int space) {
        this.mVerticalSpace = space;
    }

    public void setHorizontalSpace(int space) {
        this.mHorizontalSpace = space;
    }

    public void setAdapter(VAdapter adapter) {
        if (!(this.mVAdapter == null || this.mDataSetObserver == null)) {
            this.mVAdapter.unregisterDataSetObserver(this.mDataSetObserver);
        }
        this.mVAdapter = adapter;
        if (this.mVAdapter != null) {
            this.mDataSetObserver = new AdapterDataSetObserver();
            this.mVAdapter.registerDataSetObserver(this.mDataSetObserver);
            this.mDataChanged = true;
        }
        requestLayout();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mVAdapter != null && this.mDataSetObserver == null) {
            this.mDataSetObserver = new AdapterDataSetObserver();
            this.mVAdapter.registerDataSetObserver(this.mDataSetObserver);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mVAdapter != null) {
            this.mVAdapter.unregisterDataSetObserver(this.mDataSetObserver);
            this.mDataSetObserver = null;
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (getChildCount() > 0) {
            this.mDataChanged = true;
        }
    }

    public void requestLayout() {
        if (!(this.mVAdapter == null || this.mVGridLayout == null || !this.mDataChanged)) {
            this.mDataChanged = false;
            this.mVGridLayout.removeAllViews();
            if (this.mArrayType == 0) {
                this.mVGridLayout.setOrientation(0);
                this.mVGridLayout.setColumnCount(this.mArrayNum);
            } else if (this.mArrayType == 1) {
                this.mVGridLayout.setOrientation(1);
                this.mVGridLayout.setRowCount(this.mArrayNum);
            }
            this.mItemVisibilities = new boolean[this.mVAdapter.getCount()];
            for (int i = 0; i < this.mVAdapter.getCount(); i++) {
                this.mItemVisibilities[i] = true;
                View view = this.mVAdapter.getView(i, this.mVGridLayout);
                view.setOnFocusChangeListener(this.mOnFocusChangeListener);
                GridLayout.LayoutParams params = this.mVAdapter.getLayoutParams(i);
                if (params == null) {
                    params = new GridLayout.LayoutParams(new FrameLayout.LayoutParams(-2, -2));
                }
                params.bottomMargin = this.mVerticalSpace;
                params.topMargin = this.mVerticalSpace;
                params.rightMargin = this.mHorizontalSpace;
                params.leftMargin = this.mHorizontalSpace;
                params.columnSpec = VGridLayout.spec(Integer.MIN_VALUE, this.mVAdapter.getColumnSpan(i));
                params.rowSpec = VGridLayout.spec(Integer.MIN_VALUE, this.mVAdapter.getRowSpan(i));
                this.mVGridLayout.addView(view, i, params);
                if (i == 0 && (hasFocus() || this.mVGridLayout.hasFocus() || this.mVScrollView.hasFocus())) {
                    view.requestFocus();
                    if (this.mOnItemFocusChangeListener != null) {
                        this.mOnItemFocusChangeListener.onItemFocusChange(view, this.mVGridLayout.indexOfChild(view), true);
                    }
                }
            }
        }
        super.requestLayout();
    }
}

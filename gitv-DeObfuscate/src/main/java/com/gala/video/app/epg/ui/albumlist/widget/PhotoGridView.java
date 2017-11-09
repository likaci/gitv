package com.gala.video.app.epg.ui.albumlist.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.gala.video.lib.framework.core.utils.ViewUtils;
import com.gala.video.lib.share.utils.NineDrawableUtils;
import com.gala.video.lib.share.utils.TagKeyUtil;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PhotoGridView extends RelativeLayout {
    private static final String TAG = "PhotoGridView";
    private static final int TAG_VIEW_POSITION = TagKeyUtil.generateTagKey();
    private BaseAdapter mAdapter;
    private int mAdapterCount;
    private List<View> mBottomViewArr;
    private int mClickPosition;
    private int mColumnNum;
    private int mContentHeight;
    private int mContentWidth;
    private Context mContext;
    private boolean mCurViewFocusable = true;
    private AdapterDataSetObserver mDataSetObserver;
    private Drawable mDrawable4Calc;
    private int mFocusPosition;
    private View mFocusView;
    private int mHorizontalSpace;
    private boolean mIsAttached;
    private boolean mIsInitCompleted;
    private boolean mIsOnchange;
    private int mItemHeight;
    private int mItemWidth;
    private int mLeftBaseMargin;
    private List<View> mLeftViewArr;
    private LoadStatusListener mLoadStatusListener;
    private boolean mNextDownFocusLeaveAvail = true;
    private boolean mNextLeftFocusLeaveAvail = true;
    private boolean mNextRightFocusLeaveAvail = true;
    private boolean mNextUpFocusLeaveAvail = true;
    private int mNinePatchShadow;
    private OnFocusChangeListener mOnFocusListener = new C08751();
    private OnTouchListener mOnTouchListener = new C08762();
    private PhotoGridParams mPhotoGridParams;
    private List<View> mRightViewArr;
    private int mRowNum;
    private float mScaleRate;
    private int mTopBaseMargin;
    private List<View> mTopViewArr;
    private int mVerticalSpace;
    private List<View> mViewArr;
    private WidgetStatusListener mWidgetStatusListener;

    class C08751 implements OnFocusChangeListener {
        C08751() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                PhotoGridView.this.mFocusView = v;
            }
            PhotoGridView.this.mFocusPosition = ((Integer) v.getTag(PhotoGridView.TAG_VIEW_POSITION)).intValue();
            if (PhotoGridView.this.mWidgetStatusListener != null) {
                PhotoGridView.this.mWidgetStatusListener.onItemSelectChange(v, PhotoGridView.this.mFocusPosition, hasFocus);
            }
        }
    }

    class C08762 implements OnTouchListener {
        C08762() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            PhotoGridView.this.mClickPosition = ((Integer) v.getTag(PhotoGridView.TAG_VIEW_POSITION)).intValue();
            if (PhotoGridView.this.mWidgetStatusListener != null) {
                PhotoGridView.this.mWidgetStatusListener.onItemTouch(v, event, PhotoGridView.this.mClickPosition);
            }
            Log.e("test", "test ---- touch ---- " + event.getAction());
            if (event.getAction() == 1 && PhotoGridView.this.mWidgetStatusListener != null) {
                PhotoGridView.this.mWidgetStatusListener.onItemClick(PhotoGridView.this, v, PhotoGridView.this.mClickPosition);
            }
            return true;
        }
    }

    private class AdapterDataSetObserver extends DataSetObserver {
        private AdapterDataSetObserver() {
        }

        public void onChanged() {
            super.onChanged();
            PhotoGridView.this.mIsOnchange = true;
            PhotoGridView.this.setContentChild();
        }

        public void onInvalidated() {
            super.onInvalidated();
            PhotoGridView.this.mIsOnchange = false;
            PhotoGridView.this.setContentChild();
        }
    }

    public PhotoGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public PhotoGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PhotoGridView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mIsInitCompleted = false;
    }

    public void setParams(PhotoGridParams photoGridParams) {
        this.mPhotoGridParams = photoGridParams;
    }

    public PhotoGridParams getParams() {
        return this.mPhotoGridParams;
    }

    private void onAttachedWindow() {
        try {
            if (this.mDataSetObserver != null) {
                this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
            }
        } catch (Exception e) {
        }
        this.mDataSetObserver = new AdapterDataSetObserver();
        this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
        this.mIsAttached = true;
        this.mIsOnchange = false;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!(!this.mIsAttached || this.mAdapter == null || this.mDataSetObserver == null)) {
            this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
            this.mDataSetObserver = null;
            this.mIsAttached = false;
        }
        removeAllViewsInLayout();
    }

    public void setAdapter(BaseAdapter adapter) {
        this.mIsInitCompleted = false;
        if (adapter != null) {
            this.mAdapter = adapter;
            onAttachedWindow();
            initParams(this.mPhotoGridParams);
            setContentChild();
        }
    }

    private void setContentChild() {
        if (this.mLoadStatusListener != null) {
            this.mLoadStatusListener.onStart();
        }
        resetStates();
        this.mAdapterCount = this.mAdapter.getCount();
        if (this.mAdapterCount > 0 && this.mColumnNum > 0) {
            int i;
            int i2 = this.mAdapterCount / this.mColumnNum;
            if (this.mAdapterCount % this.mColumnNum == 0) {
                i = 0;
            } else {
                i = 1;
            }
            this.mRowNum = i + i2;
            for (int row = 0; row < this.mRowNum; row++) {
                int column = 0;
                while (column < this.mColumnNum) {
                    if ((this.mColumnNum * row) + column < this.mAdapterCount) {
                        View view = this.mAdapter.getView((this.mColumnNum * row) + column, null, this);
                        if (view == null) {
                            Log.e(TAG, "PhotoGridView---setAdapter()---lack of view in getView()!!! ");
                            return;
                        }
                        if (row == 0 && column == 0) {
                            calcViewValues(view);
                        }
                        setItemViewFocused(view);
                        setItemViewId(view);
                        view.setTag(TAG_VIEW_POSITION, Integer.valueOf((this.mColumnNum * row) + column));
                        setItemViewListener(view);
                        LayoutParams viewParams = new LayoutParams(this.mItemWidth, this.mItemHeight);
                        viewParams.topMargin = this.mTopBaseMargin + ((this.mContentHeight + this.mVerticalSpace) * row);
                        viewParams.leftMargin = this.mLeftBaseMargin + ((this.mContentWidth + this.mHorizontalSpace) * column);
                        addView(view, viewParams);
                        storageView(view, row, column);
                    }
                    column++;
                }
            }
            ViewGroup.LayoutParams layoutParams2 = getLayoutParams();
            if (layoutParams2 != null) {
                int layoutW = ((this.mLeftBaseMargin * 2) + ((this.mColumnNum - 1) * (this.mContentWidth + this.mHorizontalSpace))) + this.mItemWidth;
                int layoutH = ((this.mTopBaseMargin * 2) + ((this.mRowNum - 1) * (this.mContentHeight + this.mVerticalSpace))) + this.mItemHeight;
                layoutParams2.height = layoutH;
                layoutParams2.width = layoutW;
                Log.d(TAG, "PhotoGridView---layoutW=" + layoutW + "---layoutH=" + layoutH);
            } else {
                Log.e(TAG, "PhotoGridView---setAdapter()---where is your LayoutParams!!! ");
            }
            Log.d(TAG, "PhotoGridView---mItemWidth=" + this.mItemWidth + "---mItemHeight=" + this.mItemHeight);
            configDefaultFocusRule();
            this.mIsInitCompleted = true;
            if (this.mLoadStatusListener != null) {
                this.mLoadStatusListener.onComplete();
            }
        }
    }

    protected void setItemViewListener(View view) {
        view.setOnFocusChangeListener(this.mOnFocusListener);
        view.setOnTouchListener(this.mOnTouchListener);
    }

    protected void setItemViewId(View view) {
        view.setId(ViewUtils.generateViewId());
    }

    public void setCurViewFocusable(boolean focusable) {
        this.mCurViewFocusable = focusable;
    }

    protected void setItemViewFocused(View view) {
        view.setFocusable(this.mCurViewFocusable);
        view.setFocusableInTouchMode(this.mCurViewFocusable);
        this.mCurViewFocusable = true;
    }

    private void resetStates() {
        removeAllViewsInLayout();
        initViewArray();
        this.mFocusPosition = 0;
        this.mClickPosition = 0;
    }

    private void initParams(PhotoGridParams photoGridParams) {
        if (photoGridParams == null) {
            Log.e(TAG, "PhotoGridView---setParams()---where is your PhotoGridParams!!! ");
            return;
        }
        this.mContentWidth = photoGridParams.contentWidth;
        this.mContentHeight = photoGridParams.contentHeight;
        this.mVerticalSpace = photoGridParams.verticalSpace;
        this.mHorizontalSpace = photoGridParams.horizontalSpace;
        this.mScaleRate = photoGridParams.scaleRate;
        this.mColumnNum = photoGridParams.columnNum;
        this.mDrawable4Calc = photoGridParams.drawable4CalcBorder;
    }

    private void calcViewValues(View view) {
        if (!this.mIsOnchange) {
            try {
                if (this.mDrawable4Calc == null) {
                    this.mDrawable4Calc = view.getBackground();
                }
                if (this.mDrawable4Calc == null) {
                    Log.e(TAG, "PhotoGridView---setAdapter()---where is your background!!! ");
                } else if (this.mDrawable4Calc instanceof StateListDrawable) {
                    this.mNinePatchShadow = NineDrawableUtils.calNinePatchBorder(this.mContext, reflectStateDrawable((StateListDrawable) this.mDrawable4Calc));
                } else if (this.mDrawable4Calc instanceof NinePatchDrawable) {
                    this.mNinePatchShadow = NineDrawableUtils.calNinePatchBorder(this.mContext, this.mDrawable4Calc);
                } else if (this.mDrawable4Calc instanceof Drawable) {
                    this.mNinePatchShadow = 0;
                }
            } catch (Exception e) {
                Log.e(TAG, "PhotoGridView---setAdapter()---background Illegal !!! ");
            }
        }
        Log.d(TAG, "PhotoGridView---ninePatchShadow= " + this.mNinePatchShadow);
        this.mItemWidth = this.mContentWidth + (this.mNinePatchShadow * 2);
        this.mItemHeight = this.mContentHeight + (this.mNinePatchShadow * 2);
        this.mTopBaseMargin = (int) (((float) ((this.mContentHeight / 2) + this.mNinePatchShadow)) * (this.mScaleRate - 1.0f));
        this.mLeftBaseMargin = (int) (((float) ((this.mContentWidth / 2) + this.mNinePatchShadow)) * (this.mScaleRate - 1.0f));
    }

    private void storageView(View view, int r, int c) {
        this.mViewArr.add(view);
        if (r == 0) {
            this.mTopViewArr.add(view);
        }
        if (r == this.mRowNum - 1) {
            this.mBottomViewArr.add(view);
        }
        if (c == 0) {
            this.mLeftViewArr.add(view);
        }
        if (c == this.mColumnNum - 1) {
            this.mRightViewArr.add(view);
        }
    }

    private void initViewArray() {
        if (this.mViewArr == null) {
            this.mViewArr = new ArrayList();
        } else {
            this.mViewArr.clear();
        }
        if (this.mRightViewArr == null) {
            this.mRightViewArr = new ArrayList();
        } else {
            this.mRightViewArr.clear();
        }
        if (this.mLeftViewArr == null) {
            this.mLeftViewArr = new ArrayList();
        } else {
            this.mLeftViewArr.clear();
        }
        if (this.mBottomViewArr == null) {
            this.mBottomViewArr = new ArrayList();
        } else {
            this.mBottomViewArr.clear();
        }
        if (this.mTopViewArr == null) {
            this.mTopViewArr = new ArrayList();
        } else {
            this.mTopViewArr.clear();
        }
    }

    public View getViewByPos(int position) {
        if (this.mViewArr == null || this.mViewArr.size() <= position || position < 0) {
            return null;
        }
        return (View) this.mViewArr.get(position);
    }

    public boolean isInitCompleted() {
        return this.mIsInitCompleted;
    }

    public void setNextUpFocusLeaveAvail(boolean nextUpFocusLeaveAvail) {
        this.mNextUpFocusLeaveAvail = nextUpFocusLeaveAvail;
    }

    public void setNextDownFocusLeaveAvail(boolean nextDownFocusLeaveAvail) {
        this.mNextDownFocusLeaveAvail = nextDownFocusLeaveAvail;
    }

    public void setNextLeftFocusLeaveAvail(boolean nextLeftFocusLeaveAvail) {
        this.mNextLeftFocusLeaveAvail = nextLeftFocusLeaveAvail;
    }

    public void setNextRightFocusLeaveAvail(boolean nextRightFocusLeaveAvail) {
        this.mNextRightFocusLeaveAvail = nextRightFocusLeaveAvail;
    }

    public List<View> getTopViewList() {
        return this.mTopViewArr;
    }

    public List<View> getBottomViewList() {
        return this.mBottomViewArr;
    }

    public List<View> getLeftViewList() {
        return this.mLeftViewArr;
    }

    public List<View> getRightViewList() {
        return this.mRightViewArr;
    }

    private void configDefaultFocusRule() {
        int r = 0;
        while (r < this.mRowNum) {
            int c = 0;
            while (c < this.mColumnNum) {
                if ((this.mColumnNum * r) + c < this.mAdapterCount) {
                    if (!this.mNextRightFocusLeaveAvail && r < this.mRowNum - 1 && c == this.mColumnNum - 1) {
                        getChildAt((this.mColumnNum * r) + c).setNextFocusRightId(getChildAt(this.mColumnNum * (r + 1)).getId());
                    }
                    if (!this.mNextLeftFocusLeaveAvail && r < this.mRowNum && r != 0 && c == 0) {
                        getChildAt((this.mColumnNum * r) + c).setNextFocusLeftId(getChildAt((this.mColumnNum * (r - 1)) + (this.mColumnNum - 1)).getId());
                    }
                    if (!this.mNextUpFocusLeaveAvail && r == 0) {
                        getChildAt((this.mColumnNum * r) + c).setNextFocusUpId(getChildAt((this.mColumnNum * r) + c).getId());
                    }
                    if (!this.mNextDownFocusLeaveAvail && r == this.mRowNum - 1) {
                        getChildAt((this.mColumnNum * r) + c).setNextFocusDownId(getChildAt((this.mColumnNum * r) + c).getId());
                    }
                }
                c++;
            }
            r++;
        }
    }

    private Drawable reflectStateDrawable(StateListDrawable drawable) {
        Drawable d = null;
        try {
            for (Method method : StateListDrawable.class.getMethods()) {
                if ("getStateDrawable".equals(method.getName())) {
                    d = (Drawable) method.invoke(drawable, new Object[]{Integer.valueOf(0)});
                }
            }
        } catch (Exception e) {
        }
        return d;
    }

    public void setListener(WidgetStatusListener widgetStatusListener) {
        this.mWidgetStatusListener = widgetStatusListener;
    }

    public void setOnLoadStatusListener(LoadStatusListener onLoadStatus) {
        this.mLoadStatusListener = onLoadStatus;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return super.dispatchKeyEvent(event);
        }
        int keyCode = event.getKeyCode();
        if ((keyCode != 23 && keyCode != 66) || this.mWidgetStatusListener == null || this.mFocusView == null) {
            return super.dispatchKeyEvent(event);
        }
        this.mClickPosition = ((Integer) this.mFocusView.getTag(TAG_VIEW_POSITION)).intValue();
        this.mWidgetStatusListener.onItemClick(this, this.mFocusView, this.mClickPosition);
        return true;
    }
}

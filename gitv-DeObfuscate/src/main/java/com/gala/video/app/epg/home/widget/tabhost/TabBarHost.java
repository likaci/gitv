package com.gala.video.app.epg.home.widget.tabhost;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Scroller;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.view.ViewDebug;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.common.widget.CardFocusHelper;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.ResourceUtil;
import org.xbill.DNS.WKSRecord.Service;

public class TabBarHost extends LinearLayout implements OnFocusChangeListener, OnClickListener {
    private static final int CLIP_HEIGHT = 94;
    private static final int CLIP_WIDTH = 45;
    private static final boolean DBG = ViewDebug.DBG;
    private static final int DEFAULT_COUNT = 9;
    private static int INTERVAL_PX = ResourceUtil.getDimensionPixelSize(C0508R.dimen.dimen_29dp);
    private static int MLENGTH_MAX_PX = ResourceUtil.getDimensionPixelSize(C0508R.dimen.dimen_1070dp);
    private static final int NO_INDEX = -1;
    private static int OFF_SET_PX = ResourceUtil.getDimensionPixelSize(C0508R.dimen.dimen_55dp);
    private static final String TAG = "/home/TabBarHost";
    private static final int TAG_KEY = Integer.MAX_VALUE;
    public static final int[] VIEW_IDS = new int[]{C0508R.id.tab_item_index_0, C0508R.id.tab_item_index_1, C0508R.id.tab_item_index_2, C0508R.id.tab_item_index_3, C0508R.id.tab_item_index_4, C0508R.id.tab_item_index_5, C0508R.id.tab_item_index_6, C0508R.id.tab_item_index_7, C0508R.id.tab_item_index_8, C0508R.id.tab_item_index_9, C0508R.id.tab_item_index_10, C0508R.id.tab_item_index_11, C0508R.id.tab_item_index_12, C0508R.id.tab_item_index_13, C0508R.id.tab_item_index_14, C0508R.id.tab_item_index_15, C0508R.id.tab_item_index_16, C0508R.id.tab_item_index_17, C0508R.id.tab_item_index_18, C0508R.id.tab_item_index_19};
    private boolean isOnLayout = false;
    private TabBarAdapter mAdapter;
    private int mCenterX;
    private int mCurSelectedChildIndex = -1;
    private int mDefaultFocusIndex = 1;
    private OnFocusChangeListener mFocusChangeListener;
    private long mLastAnimationX = 0;
    private BitmapDrawable mLbp = null;
    private View mLeftFadeView;
    private int mLength;
    private int mLength_px;
    private BitmapDrawable mRbp = null;
    private View mRightFadeView;
    private Scroller mScroller;
    private OnTurnPageListener mTurnPageListener;

    public interface OnTurnPageListener {
        void onTurnPage(int i);
    }

    class C07241 implements Runnable {
        C07241() {
        }

        public void run() {
            int count = TabBarHost.this.getChildCount();
            for (int i = 0; i < count; i++) {
                if (i > 0) {
                    TabBarHost.this.getChildAt(i).setNextFocusLeftId(TabBarHost.this.getChildAt(i - 1).getId());
                }
                if (i < count - 1) {
                    TabBarHost.this.getChildAt(i).setNextFocusRightId(TabBarHost.this.getChildAt(i + 1).getId());
                }
            }
            TabBarHost.this.getChildAt(0).setNextFocusLeftId(TabBarHost.this.getChildAt(0).getId());
        }
    }

    public TabBarHost(Context context) {
        super(context);
        init(context);
    }

    public TabBarHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(11)
    public TabBarHost(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mScroller = new Scroller(context);
        setGravity(51);
    }

    private int alpha(int color) {
        return (color >> 24) & 255;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateFadeView(Project.getInstance().getControl().getBackgroundDrawable());
    }

    public void updateFadeView(Drawable drawable) {
        Activity activity = (Activity) getContext();
        if (drawable != null) {
            int[] mLeftPixels;
            int[] mRightPixels;
            int i;
            int j;
            Bitmap leftBitmap;
            Bitmap rightBitmap;
            if (drawable instanceof BitmapDrawable) {
                Log.d(TAG, "drawable is BitmapDrawable");
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                Log.d(TAG, "width = " + width);
                Log.d(TAG, "height = " + height);
                int BitmapWidth = transformForWidth(45, width);
                int BitmapHeight = transformForHeight(CLIP_HEIGHT, height);
                mLeftPixels = new int[(BitmapWidth * BitmapHeight)];
                mRightPixels = new int[(BitmapWidth * BitmapHeight)];
                Log.d(TAG, "BitmapWidth = " + BitmapWidth);
                Log.d(TAG, "BitmapHeight = " + BitmapHeight);
                bitmap.getPixels(mLeftPixels, 0, BitmapWidth, 0, transformForHeight(111, height), BitmapWidth, BitmapHeight);
                bitmap.getPixels(mRightPixels, 0, BitmapWidth, transformForWidth(1609, width), transformForHeight(111, height), BitmapWidth, BitmapHeight);
                for (i = 0; i < BitmapHeight; i++) {
                    for (j = 0; j < BitmapWidth; j++) {
                        mLeftPixels[(i * BitmapWidth) + j] = (mLeftPixels[(i * BitmapWidth) + j] & 16777215) | (((int) (((double) (BitmapWidth - j)) * (255.0d / ((double) BitmapWidth)))) << 24);
                    }
                }
                for (i = 0; i < BitmapHeight; i++) {
                    for (j = 0; j < BitmapWidth; j++) {
                        mRightPixels[(i * BitmapWidth) + j] = (mRightPixels[(i * BitmapWidth) + j] & 16777215) | (((int) (((double) j) * (255.0d / ((double) BitmapWidth)))) << 24);
                    }
                }
                leftBitmap = Bitmap.createBitmap(mLeftPixels, BitmapWidth, BitmapHeight, Config.ARGB_8888);
                rightBitmap = Bitmap.createBitmap(mRightPixels, BitmapWidth, BitmapHeight, Config.ARGB_8888);
                this.mLbp = new BitmapDrawable(getResources(), leftBitmap);
                this.mRbp = new BitmapDrawable(getResources(), rightBitmap);
            } else if (drawable instanceof ColorDrawable) {
                Log.d(TAG, "drawable is ColorDrawable");
                int color = ((ColorDrawable) drawable).getColor();
                mLeftPixels = new int[4230];
                mRightPixels = new int[4230];
                for (i = 0; i < mLeftPixels.length; i++) {
                    mLeftPixels[i] = color;
                }
                for (i = 0; i < mRightPixels.length; i++) {
                    mRightPixels[i] = color;
                }
                for (i = 0; i < CLIP_HEIGHT; i++) {
                    for (j = 0; j < 45; j++) {
                        mLeftPixels[(i * 45) + j] = (mLeftPixels[(i * 45) + j] & 16777215) | (((int) (((double) (45 - j)) * 5.666666666666667d)) << 24);
                    }
                }
                for (i = 0; i < CLIP_HEIGHT; i++) {
                    for (j = 0; j < 45; j++) {
                        mRightPixels[(i * 45) + j] = (mRightPixels[(i * 45) + j] & 16777215) | (((int) (((double) j) * 5.666666666666667d)) << 24);
                    }
                }
                leftBitmap = Bitmap.createBitmap(mLeftPixels, 45, CLIP_HEIGHT, Config.ARGB_8888);
                rightBitmap = Bitmap.createBitmap(mRightPixels, 45, CLIP_HEIGHT, Config.ARGB_8888);
                this.mLbp = new BitmapDrawable(getResources(), leftBitmap);
                this.mRbp = new BitmapDrawable(getResources(), rightBitmap);
            } else if (drawable instanceof GradientDrawable) {
                mLeftPixels = new int[4230];
                mRightPixels = new int[4230];
                for (i = 0; i < mLeftPixels.length; i++) {
                    mLeftPixels[i] = -15461356;
                }
                for (i = 0; i < mRightPixels.length; i++) {
                    mRightPixels[i] = -15461356;
                }
                for (i = 0; i < CLIP_HEIGHT; i++) {
                    for (j = 0; j < 45; j++) {
                        mLeftPixels[(i * 45) + j] = (mLeftPixels[(i * 45) + j] & 16777215) | (((int) (((double) (45 - j)) * 5.666666666666667d)) << 24);
                    }
                }
                for (i = 0; i < CLIP_HEIGHT; i++) {
                    for (j = 0; j < 45; j++) {
                        mRightPixels[(i * 45) + j] = (mRightPixels[(i * 45) + j] & 16777215) | (((int) (((double) j) * 5.666666666666667d)) << 24);
                    }
                }
                leftBitmap = Bitmap.createBitmap(mLeftPixels, 45, CLIP_HEIGHT, Config.ARGB_8888);
                rightBitmap = Bitmap.createBitmap(mRightPixels, 45, CLIP_HEIGHT, Config.ARGB_8888);
                this.mLbp = new BitmapDrawable(getResources(), leftBitmap);
                this.mRbp = new BitmapDrawable(getResources(), rightBitmap);
            }
            if (this.mLeftFadeView == null) {
                this.mLeftFadeView = activity.findViewById(C0508R.id.epg_tab_host_leftimage);
            }
            if (this.mLbp != null) {
                AppClientUtils.setBackgroundDrawable(this.mLeftFadeView, this.mLbp);
            }
            if (this.mRightFadeView == null) {
                this.mRightFadeView = activity.findViewById(C0508R.id.epg_tab_host_rightimage);
            }
            if (this.mRbp != null) {
                AppClientUtils.setBackgroundDrawable(this.mRightFadeView, this.mRbp);
            }
            checkFadeView(0);
        }
    }

    private int transformForWidth(int value, int width) {
        return (int) ((((double) (width * value)) * 1.0d) / 1920.0d);
    }

    private int transformForHeight(int value, int height) {
        return (int) ((((double) (height * value)) * 1.0d) / 1080.0d);
    }

    public void setOnTurnPageListener(OnTurnPageListener listener) {
        this.mTurnPageListener = listener;
    }

    public void setAdapter(TabBarAdapter adapter, int defaultTabIndex) {
        if (this.mAdapter != adapter) {
            this.mAdapter = adapter;
        }
        this.mLength = this.mAdapter.getCount();
        if (defaultTabIndex >= this.mLength) {
            defaultTabIndex = 0;
        }
        if (this.mCurSelectedChildIndex == -1 || this.mCurSelectedChildIndex >= this.mLength) {
            this.mCurSelectedChildIndex = defaultTabIndex;
        }
        this.mDefaultFocusIndex = defaultTabIndex;
        removeAllViewsInLayout();
        this.mLength_px = 0;
        int i = 0;
        while (i < this.mLength) {
            View childView = this.mAdapter.getView(i, null, this);
            childView.setId(i < 20 ? VIEW_IDS[i] : -1);
            childView.setTag(TAG_KEY, Integer.valueOf(i));
            childView.setOnFocusChangeListener(this);
            childView.setOnClickListener(this);
            childView.setFocusable(true);
            childView.setFocusableInTouchMode(true);
            LayoutParams layoutParams = (LayoutParams) childView.getLayoutParams();
            if (childView.getLayoutParams() == null) {
                layoutParams = new LayoutParams(-2, -2);
            }
            childView.measure(0, 0);
            this.mLength_px += childView.getMeasuredWidth() + INTERVAL_PX;
            addViewInLayout(childView, -1, layoutParams);
            i++;
        }
        this.mLength_px -= INTERVAL_PX;
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) getLayoutParams();
        if (this.mLength_px <= MLENGTH_MAX_PX) {
            setGravity(1);
            try {
                int minOffSet = ResourceUtil.getDimensionPixelSize(C0508R.dimen.dimen_338dp);
                this.mLength_px -= minOffSet;
                if (this.mLength_px < 0) {
                    this.mLength_px = 0;
                }
                lp.setMargins(OFF_SET_PX - ((this.mLength_px * OFF_SET_PX) / (MLENGTH_MAX_PX - minOffSet)), 0, 0, 0);
                setLayoutParams(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            lp.setMargins(0, 0, 0, 0);
            setLayoutParams(lp);
            setGravity(51);
        }
        this.mLength_px = 0;
        requestLayout();
        post(new C07241());
    }

    public int getDefaultFocusIndex() {
        return this.mDefaultFocusIndex;
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.mCenterX = getCenterX();
        }
        this.isOnLayout = true;
        checkFadeView(0);
    }

    public void onFocusChange(View v, boolean hasFocus) {
        int newIndex = ((Integer) v.getTag(TAG_KEY)).intValue();
        if (newIndex == 0) {
            if (hasFocus) {
                ((ViewGroup) getParent()).setClipChildren(false);
                ((ViewGroup) getParent()).setClipToPadding(false);
                ((ViewGroup) getParent()).requestLayout();
            } else {
                ((ViewGroup) getParent()).setClipChildren(true);
                ((ViewGroup) getParent()).setClipToPadding(true);
                ((ViewGroup) getParent()).requestLayout();
            }
        }
        if (DBG) {
            Log.d(TAG, " onFocusChange newIndex = " + newIndex + ",has focus = " + hasFocus);
        }
        if (hasFocus) {
            if (this.mCurSelectedChildIndex != newIndex) {
                this.mAdapter.onChildSelectChanged(v, true);
                if (this.mCurSelectedChildIndex > -1) {
                    this.mAdapter.onChildSelectChanged(getChildAt(this.mCurSelectedChildIndex), false);
                    if (this.mTurnPageListener != null) {
                        this.mTurnPageListener.onTurnPage(newIndex);
                    }
                }
                this.mCurSelectedChildIndex = newIndex;
            }
            CardFocusHelper cardFocusHelper = CardFocusHelper.getMgr(v.getContext());
            if (cardFocusHelper != null) {
                cardFocusHelper.setScrollListenerEnable(false);
                cardFocusHelper.disableFocusVisible();
            }
        } else {
            this.mAdapter.onChildSelectChanged(v, true);
        }
        if (this.mFocusChangeListener != null) {
            this.mFocusChangeListener.onFocusChange(v, hasFocus);
        }
        this.mAdapter.onChildFocusChanged(v, hasFocus);
    }

    private int getCenterX() {
        return (((getWidth() - getPaddingLeft()) - getPaddingRight()) / 2) + getPaddingLeft();
    }

    private int getCenterXOfView(View childView) {
        return (childView.getLeft() + (childView.getWidth() / 2)) - getScrollX();
    }

    private int computeScrollDelta(View focused) {
        if (getChildCount() == 0 || focused == null) {
            return 0;
        }
        int scrollDelta = getCenterXOfView(focused) - this.mCenterX;
        View end = getChildAt(getChildCount() - 1);
        if (DBG) {
            Log.d(TAG, "scrollXDelta = " + scrollDelta);
            Log.d(TAG, "scrollX = " + getScrollX());
            Log.d(TAG, "width = " + getWidth());
            Log.d(TAG, "end = " + end.getRight());
        }
        return Math.max(Math.min(scrollDelta, (end.getRight() - getScrollX()) - getWidth()), -getScrollX());
    }

    public int[] getTabBarIds() {
        int[] ids = new int[this.mLength];
        for (int i = 0; i < this.mLength; i++) {
            ids[i] = getChildAt(i).getId();
        }
        return this.mLength > 0 ? ids : null;
    }

    public void setNextFocusUpId(int id) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).setNextFocusUpId(id);
        }
    }

    public void setNextFocusDownId(int id) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).setNextFocusDownId(id);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || executeKeyEvent(event);
    }

    private boolean executeKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return false;
        }
        switch (event.getKeyCode()) {
            case 21:
                if (DBG) {
                    Log.d(TAG, "execute key left down");
                }
                return arrowScroll(17);
            case 22:
                if (DBG) {
                    Log.d(TAG, "execute key right down");
                }
                return arrowScroll(66);
            default:
                return false;
        }
    }

    public void reset() {
        if (DBG) {
            Log.d(TAG, "tab bar reset");
        }
        scrollTo(0, 0);
        quickScrollTo(0, 0);
        checkFadeView(0);
    }

    private void quickScrollTo(int fx, int fy) {
        this.mScroller.startScroll(this.mScroller.getFinalX(), this.mScroller.getFinalY(), fx - this.mScroller.getFinalX(), fy - this.mScroller.getFinalY(), 1);
        invalidate();
    }

    private void doScrollX(int delta) {
        if (delta != 0) {
            smoothScrollBy(delta, 0);
        }
    }

    private boolean arrowScroll(int direction) {
        View currentFocused = findFocus();
        if (currentFocused == this) {
            return false;
        }
        if (direction == 33 || direction == Service.CISCO_FNA) {
            return false;
        }
        View nextFocused;
        int current = indexOfChild(currentFocused);
        if (current < 0) {
            nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        } else {
            nextFocused = getChildAt((direction == 17 ? -1 : 1) + current);
        }
        if (nextFocused != null) {
            if (DBG) {
                Log.d(TAG, "arrowScroll nextFocused requestFocus");
            }
            if (nextFocused != null && !nextFocused.requestFocus(direction)) {
                return false;
            }
            View parent = nextFocused.getParent();
            View child = nextFocused;
            while (parent != null && (parent instanceof View) && parent != this) {
                child = parent;
                parent = parent.getParent();
            }
            int scrollDelta = computeScrollDelta(child);
            doScrollX(scrollDelta);
            checkFadeView(scrollDelta);
            return true;
        } else if (direction == 66) {
            return false;
        } else {
            doBounceAnimation(currentFocused);
            return true;
        }
    }

    public void checkFadeView(int scrollDelta) {
        if (this.isOnLayout) {
            try {
                if (this.mRightFadeView != null && this.mLeftFadeView != null && getChildCount() > 0) {
                    if (getChildAt(0).getLeft() - scrollDelta >= getScrollX()) {
                        this.mLeftFadeView.setVisibility(4);
                    } else {
                        this.mLeftFadeView.setVisibility(0);
                    }
                    if (getChildAt(getChildCount() - 1).getRight() - scrollDelta <= getScrollX() + getWidth()) {
                        this.mRightFadeView.setVisibility(4);
                    } else {
                        this.mRightFadeView.setVisibility(0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public View getChildViewAt(int index) {
        return getChildAt(index);
    }

    public void onClick(View v) {
        this.mAdapter.onClick(v, ((Integer) v.getTag(TAG_KEY)).intValue());
    }

    public void requestChildFocus(int i) {
        requestFocus(getChildAt(i));
    }

    private void requestFocus(View childAt) {
        if (childAt != null) {
            childAt.requestFocus();
        }
    }

    private void smoothScrollBy(int dx, int dy) {
        this.mScroller.startScroll(getScrollX(), this.mScroller.getFinalY(), dx, dy);
        invalidate();
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }

    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        if (getOrientation() == 1 || lp.width >= 0) {
            super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
            return;
        }
        child.measure(MeasureSpec.makeMeasureSpec(lp.leftMargin + lp.rightMargin, 0), getChildMeasureSpec(parentHeightMeasureSpec, (((getPaddingTop() + getPaddingBottom()) + lp.topMargin) + lp.bottomMargin) + heightUsed, lp.height));
    }

    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        this.mFocusChangeListener = listener;
    }

    public int getCurrentChildIndex() {
        return this.mCurSelectedChildIndex;
    }

    public int getFocusChildIndex() {
        LogUtils.m1568d(TAG, "getFocusChildIndex = " + this.mCurSelectedChildIndex);
        if (this.mCurSelectedChildIndex >= getChildCount() || this.mCurSelectedChildIndex < 0) {
            return this.mDefaultFocusIndex;
        }
        return this.mCurSelectedChildIndex;
    }

    private void doBounceAnimation(View view) {
        if (AnimationUtils.currentAnimationTimeMillis() - this.mLastAnimationX > 500) {
            view.startAnimation(AnimationUtils.loadAnimation(getContext(), C0508R.anim.epg_shake));
            this.mLastAnimationX = AnimationUtils.currentAnimationTimeMillis();
        }
    }
}

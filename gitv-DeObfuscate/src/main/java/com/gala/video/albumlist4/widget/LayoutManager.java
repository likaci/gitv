package com.gala.video.albumlist4.widget;

import android.content.Context;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.View;
import com.gala.video.albumlist4.widget.C0468d.C0466a;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import org.xbill.DNS.WKSRecord.Service;

public abstract class LayoutManager {
    public static final int NEXT_ITEM = 4;
    public static final int NEXT_ROW = 16;
    public static final int PREV_ITEM = 2;
    public static final int PREV_ROW = 8;
    public static final int SCROLL_MODE_FOCUS = 0;
    public static final int SCROLL_MODE_NO_FOCUS = 1;
    protected int mContentHeight = Integer.MIN_VALUE;
    protected int mContentWidth = Integer.MIN_VALUE;
    protected int mFocusLeaveForbidden = 0;
    protected boolean mFocusLoop = false;
    protected boolean mFocusMemorable = true;
    protected FocusPlace mFocusPlace = FocusPlace.FOCUS_CENTER;
    protected int mGravity;
    protected Orientation mOrientation;
    protected RecyclerView mRecyclerView;
    protected int mScrollCenterHigh = 0;
    protected int mScrollCenterLow = 0;
    protected int mScrollMode = 0;
    protected View mScrollingView;
    protected int mShakeForbidden = 0;
    protected int mSpringbackDelta = 50;
    protected int mSpringbackForbidden = 0;

    public enum FocusPlace {
        FOCUS_CENTER,
        FOCUS_EDGE,
        FOCUS_CUSTOM
    }

    public enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    public abstract void changeForward(int i);

    public abstract boolean dispatchKeyEvent(KeyEvent keyEvent, int i);

    public abstract View focusSearch(View view, int i);

    public abstract int getFirstAttachedPosition();

    public abstract int getFocusPosition();

    public abstract View getFocusView();

    public abstract int getHorizontalMargin();

    public abstract int getLastAttachedPosition();

    public abstract int getMinScroll();

    public abstract int getMovement();

    public abstract int getNumRows();

    public abstract int getVerticalMargin();

    public abstract boolean gridOnRequestFocusInDescendants(int i, Rect rect);

    public abstract boolean hasScrollOffset();

    public abstract boolean isAtEdge(View view);

    public abstract boolean isNeedRequestFocus();

    public abstract void measureChild(View view);

    public abstract void onAdapterChanged(Adapter adapter);

    public abstract boolean onAddFocusables(ArrayList<View> arrayList, int i, int i2);

    public abstract void onFocusChanged(boolean z, int i, Rect rect);

    public abstract void onFocusLost(ViewHolder viewHolder);

    public abstract void onItemsAdded(int i, int i2);

    public abstract void onItemsRemoved(int i, int i2);

    public abstract void onLayoutChildren();

    public abstract void onRemoved(int i);

    public abstract boolean onRequestChildFocus(View view, View view2);

    public abstract void onUpdateChildren();

    public abstract boolean resumeChildFocus(View view);

    public abstract int scrollBy(int i, int i2);

    public abstract void setExtraPadding(int i);

    public abstract void setFocusPosition(int i);

    public abstract void setHorizontalMargin(int i);

    public abstract void setNumRows(int i);

    public abstract void setVerticalMargin(int i);

    public LayoutManager(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }

    public void setOrientation(Orientation orientation) {
        this.mOrientation = orientation;
    }

    public Orientation getOrientation() {
        return this.mOrientation;
    }

    public RecyclerView getRecyclerView() {
        return this.mRecyclerView;
    }

    public int getChildCount() {
        return this.mRecyclerView.getChildCount();
    }

    public void setContentHeight(int contentHeight) {
        this.mContentHeight = contentHeight;
    }

    public int getContentHeight() {
        return this.mContentHeight;
    }

    public void setContentWidth(int contentWidth) {
        this.mContentWidth = contentWidth;
    }

    public int getContentWidth() {
        return this.mContentWidth;
    }

    public View getChildAt(int index) {
        return this.mRecyclerView.getChildAt(index);
    }

    public void smoothScrollBy(int dx, int dy) {
        this.mRecyclerView.smoothScrollBy(dx, dy);
    }

    public int indexOfChild(View view) {
        return this.mRecyclerView.indexOfChild(view);
    }

    public int getCount() {
        return this.mRecyclerView.getCount();
    }

    public void removeView(View view) {
        this.mRecyclerView.removeView(view);
    }

    public void scrapView(View view, boolean remove) {
        this.mRecyclerView.m1280a(view, remove);
    }

    public void viewRecycled(View view) {
        this.mRecyclerView.m1272a(view);
    }

    public void setScrollMode(int mode) {
        this.mScrollMode = mode;
    }

    public int getScrollMode() {
        return this.mScrollMode;
    }

    public View getViewForLocation(C0466a location) {
        return this.mRecyclerView.m1274a(location);
    }

    public View getScrollingView() {
        return this.mScrollingView;
    }

    public boolean isFocusable(int position) {
        return this.mRecyclerView.isFocusable(position);
    }

    public int getViewPosition(View view) {
        return this.mRecyclerView.getViewPosition(view);
    }

    public void updateItem(ViewHolder vh, int position) {
        this.mRecyclerView.updateItem(vh, position);
    }

    public void addView(View child) {
        this.mRecyclerView.addView(child);
    }

    public void addView(View child, int index) {
        this.mRecyclerView.addView(child, index);
    }

    public int getLastPosition() {
        return this.mRecyclerView.getLastPosition();
    }

    public int getFirstIndex() {
        return 0;
    }

    public int getWidth() {
        return this.mRecyclerView.getWidth();
    }

    public void setSpringbackDelta(int delta) {
        this.mSpringbackDelta = delta;
    }

    public int getHeight() {
        return this.mRecyclerView.getHeight();
    }

    public Context getContext() {
        return this.mRecyclerView.getContext();
    }

    public void setGravity(int gravity) {
        this.mGravity = gravity;
    }

    public void setFocusPlace(FocusPlace focusPlace) {
        this.mFocusPlace = focusPlace;
    }

    public void setFocusPlace(int low, int high) {
        this.mFocusPlace = FocusPlace.FOCUS_CUSTOM;
        this.mScrollCenterLow = low;
        this.mScrollCenterHigh = high;
    }

    public void setFocusLeaveForbidden(int direction) {
        this.mFocusLeaveForbidden = direction & 240;
    }

    public void setSpringbackForbidden(int direction) {
        this.mSpringbackForbidden = direction & 240;
    }

    public void setShakeForbidden(int direction) {
        this.mShakeForbidden = direction & 240;
    }

    public int getPaddingLow() {
        return this.mOrientation == Orientation.HORIZONTAL ? this.mRecyclerView.getPaddingLeft() : this.mRecyclerView.getPaddingTop();
    }

    public int getPaddingHigh() {
        return this.mOrientation == Orientation.HORIZONTAL ? this.mRecyclerView.getPaddingRight() : this.mRecyclerView.getPaddingBottom();
    }

    public int getMovement(int direction) {
        if (this.mOrientation != Orientation.HORIZONTAL) {
            if (this.mOrientation == Orientation.VERTICAL) {
                switch (direction) {
                    case 17:
                    case 21:
                        return 2;
                    case 19:
                    case 33:
                        return 8;
                    case 20:
                    case Service.CISCO_FNA /*130*/:
                        return 16;
                    case 22:
                    case 66:
                        return 4;
                    default:
                        break;
                }
            }
        }
        switch (direction) {
            case 17:
                return 8;
            case 33:
                return 2;
            case 66:
                return 16;
            case Service.CISCO_FNA /*130*/:
                return 4;
        }
        return 0;
    }

    protected int getPaddingTop() {
        return this.mRecyclerView.getPaddingTop();
    }

    protected int getPaddingBottom() {
        return this.mRecyclerView.getPaddingBottom();
    }

    protected int getPaddingLeft() {
        return this.mRecyclerView.getPaddingLeft();
    }

    protected int getPaddingRight() {
        return this.mRecyclerView.getPaddingRight();
    }

    public View getViewByPosition(int position) {
        return this.mRecyclerView.getViewByPosition(position);
    }

    public void setFocusLoop(boolean focusLoop) {
        this.mFocusLoop = focusLoop;
    }

    public void setFocusMemorable(boolean memorable) {
        this.mFocusMemorable = memorable;
    }

    public void doOnDetachedFromWindow() {
    }
}

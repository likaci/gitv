package com.gala.video.albumlist.widget;

import android.content.Context;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.View;
import com.gala.video.albumlist.layout.BlockLayout;
import com.gala.video.albumlist.widget.BlocksView.Adapter;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.albumlist.widget.C0423d.C0421a;
import java.util.ArrayList;
import java.util.List;
import org.xbill.DNS.WKSRecord.Service;

public abstract class LayoutManager {
    public static final int NEXT_ITEM = 4;
    public static final int NEXT_ROW = 16;
    public static final int PREV_ITEM = 2;
    public static final int PREV_ROW = 8;
    public static final int SCROLL_MODE_FOCUS = 0;
    public static final int SCROLL_MODE_NO_FOCUS = 1;
    protected BlocksView mBlocksView;
    protected int mContentHeight = Integer.MIN_VALUE;
    protected int mContentWidth = Integer.MIN_VALUE;
    protected int mFocusLeaveForbidden = 0;
    protected boolean mFocusLoop = false;
    protected boolean mFocusMemorable = true;
    protected FocusPlace mFocusPlace = FocusPlace.FOCUS_CENTER;
    protected int mGravity;
    protected Orientation mOrientation = Orientation.VERTICAL;
    protected int mScrollCenterHigh = 0;
    protected int mScrollCenterLow = 0;
    protected int mScrollMode = 0;
    protected boolean mScrollOnly;
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

    public abstract void fastLayoutChildren();

    public abstract View findFocus();

    public abstract View focusSearch(View view, int i);

    public abstract BlockLayout getBlockLayout(int i);

    public abstract int getFirstAttachedPosition();

    public abstract int getFocusPosition();

    public abstract View getFocusView();

    public abstract int getHorizontalMargin();

    public abstract int getLastAttachedPosition();

    public abstract int getMinScroll();

    public abstract int getMovement();

    public abstract int getVerticalMargin();

    public abstract boolean hasScrollOffset();

    public abstract boolean isAtEdge(View view, int i);

    public abstract boolean isAtMax(View view);

    public abstract boolean isAtMin(View view);

    public abstract boolean isCanScroll();

    public abstract boolean isCanScroll(boolean z);

    public abstract boolean isNeedRequestFocus();

    public abstract boolean isOnTop();

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

    public abstract boolean onRequestFocusInDescendants(int i, Rect rect);

    public abstract void onScrollStop();

    public abstract void onUpdateChildren();

    public abstract void resetValues();

    public abstract boolean resumeChildFocus(View view);

    public abstract int scrollBy(int i, int i2);

    public abstract void setExtraPadding(int i);

    public abstract void setFocusPosition(int i);

    public abstract void setHorizontalMargin(int i);

    public abstract void setLayouts(List<BlockLayout> list);

    public abstract void setVerticalMargin(int i);

    public LayoutManager(BlocksView recyclerView) {
        this.mBlocksView = recyclerView;
    }

    public void setOrientation(Orientation orientation) {
        this.mOrientation = orientation;
    }

    public Orientation getOrientation() {
        return this.mOrientation;
    }

    public BlocksView getBlocksView() {
        return this.mBlocksView;
    }

    public int getChildCount() {
        return this.mBlocksView.getChildCount();
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
        return this.mBlocksView.getChildAt(index);
    }

    public void smoothScrollBy(int dx, int dy) {
        this.mBlocksView.smoothScrollBy(dx, dy);
    }

    public int indexOfChild(View view) {
        return this.mBlocksView.indexOfChild(view);
    }

    public int getCount() {
        return this.mBlocksView.getCount();
    }

    public void removeView(View view) {
        this.mBlocksView.removeView(view);
    }

    public void scrapView(View view, boolean remove) {
        this.mBlocksView.m1003a(view, remove);
    }

    public void viewRecycled(View view) {
        this.mBlocksView.m1010b(view);
    }

    public void setScrollMode(int mode) {
        this.mScrollMode = mode;
    }

    public int getScrollMode() {
        return this.mScrollMode;
    }

    public View getViewForLocation(C0421a location) {
        return this.mBlocksView.m996a(location);
    }

    public View getScrollingView() {
        return this.mScrollingView;
    }

    public boolean isFocusable(int position) {
        return this.mBlocksView.isFocusable(position);
    }

    public int getViewPosition(View view) {
        return this.mBlocksView.getViewPosition(view);
    }

    public void updateItem(ViewHolder vh, int position) {
        this.mBlocksView.updateItem(vh, position);
    }

    public void addView(View child) {
        this.mBlocksView.addView(child);
    }

    public void addView(View child, int index) {
        this.mBlocksView.addView(child, index);
    }

    public int getLastPosition() {
        return this.mBlocksView.getLastPosition();
    }

    public int getFirstIndex() {
        return 0;
    }

    public int getWidth() {
        return this.mBlocksView.getWidth();
    }

    public void setSpringbackDelta(int delta) {
        this.mSpringbackDelta = delta;
    }

    public int getHeight() {
        return this.mBlocksView.getHeight();
    }

    public Context getContext() {
        return this.mBlocksView.getContext();
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

    public void setScrollOnly(boolean scrollOnly) {
        this.mScrollOnly = scrollOnly;
    }

    public int getPaddingMin() {
        return this.mOrientation == Orientation.HORIZONTAL ? this.mBlocksView.getPaddingLeft() : this.mBlocksView.getPaddingTop();
    }

    public int getPaddingMax() {
        return this.mOrientation == Orientation.HORIZONTAL ? this.mBlocksView.getPaddingRight() : this.mBlocksView.getPaddingBottom();
    }

    public int getPaddingStart() {
        return this.mOrientation == Orientation.HORIZONTAL ? this.mBlocksView.getPaddingTop() : this.mBlocksView.getPaddingLeft();
    }

    public int getPaddingEnd() {
        return this.mOrientation == Orientation.HORIZONTAL ? this.mBlocksView.getPaddingBottom() : this.mBlocksView.getPaddingRight();
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
        return this.mBlocksView.getPaddingTop();
    }

    protected int getPaddingBottom() {
        return this.mBlocksView.getPaddingBottom();
    }

    protected int getPaddingLeft() {
        return this.mBlocksView.getPaddingLeft();
    }

    protected int getPaddingRight() {
        return this.mBlocksView.getPaddingRight();
    }

    public View getViewByPosition(int position) {
        return this.mBlocksView.getViewByPosition(position);
    }

    public void setFocusLoop(boolean focusLoop) {
        this.mFocusLoop = focusLoop;
    }

    public void setFocusMemorable(boolean memorable) {
        this.mFocusMemorable = memorable;
    }
}

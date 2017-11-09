package com.gala.video.albumlist4.widget;

import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.gala.video.albumlist4.utils.AnimationUtils;
import com.gala.video.albumlist4.utils.LOG;
import com.gala.video.albumlist4.widget.LayoutManager.FocusPlace;
import com.gala.video.albumlist4.widget.LayoutManager.Orientation;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.albumlist4.widget.c.a;
import com.gala.video.albumlist4.widget.d.b;
import com.gala.video.lib.share.ifimpl.ucenter.history.impl.HistoryInfoHelper;
import java.util.ArrayList;
import org.xbill.DNS.WKSRecord.Service;

class e extends LayoutManager implements b {
    private static int[] a = new int[2];
    private int f774a = -1;
    private d f775a = new f(this);
    private boolean f776a;
    private int b = 0;
    private boolean f777b;
    private int c = Integer.MAX_VALUE;
    private boolean f778c = false;
    private int d = 0;
    private boolean f779d = true;
    private int e = Integer.MIN_VALUE;
    private int f = Integer.MIN_VALUE;
    private int g = 1;
    private int h = 0;
    private int i = 0;
    private int j = 0;
    private int k = 16;
    private int l = 0;

    public e(RecyclerView recyclerView) {
        super(recyclerView);
    }

    public synchronized void onLayoutChildren() {
        this.f776a = true;
        boolean hasFocus = this.mRecyclerView.hasFocus();
        if (getCount() == 0) {
            a();
            this.mRecyclerView.removeUnattachedViews();
        } else {
            if (c()) {
                b();
            } else {
                if (this.f774a != -1) {
                    while (a()) {
                        if (getViewByPosition(this.f774a) != null) {
                            break;
                        }
                    }
                }
                while (true) {
                    f();
                    e();
                    int f = this.f775a.f();
                    int g = this.f775a.g();
                    View viewByPosition = getViewByPosition(this.f774a);
                    if (viewByPosition != null) {
                        a(viewByPosition, false);
                        if (hasFocus && !viewByPosition.hasFocus()) {
                            viewByPosition.requestFocus();
                        }
                    }
                    b(false);
                    a(false);
                    this.f775a.b(0);
                    this.f775a.d(a());
                    if (this.f775a.f() == f && this.f775a.g() == g) {
                        break;
                    }
                }
                this.mRecyclerView.f();
            }
            this.mRecyclerView.removeUnattachedViews();
            this.f776a = false;
        }
    }

    private void b() {
        f();
        e();
        a(false);
        b(false);
    }

    public boolean m201a() {
        return a(true);
    }

    private boolean m193c() {
        this.l = getOrientation() == Orientation.HORIZONTAL ? this.i : this.j;
        int count = getCount();
        if (this.f774a >= count) {
            this.f774a = count - 1;
        } else if (this.f774a == -1 && count > 0) {
            this.f774a = 0;
        }
        if (!this.mRecyclerView.f713a && this.f775a.f() >= 0) {
            return true;
        }
        c();
        a();
        return false;
    }

    private void c() {
        this.mRecyclerView.scrollBy(-this.mRecyclerView.getScrollX(), -this.mRecyclerView.getScrollY());
        this.b = 0;
        this.f775a.a();
        this.e = Integer.MIN_VALUE;
    }

    public void m199a() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            scrapView(getChildAt(i), false);
        }
    }

    public void changeForward(int newFocusPosition) {
        boolean z = true;
        if (this.k == 16 || this.k == 8) {
            if (this.k != 16) {
                z = false;
            }
            this.f779d = z;
        } else if (this.k == 4 || this.k == 2) {
            int f = f(this.f774a);
            int h = h(this.f774a);
            if (newFocusPosition < this.f774a - h) {
                this.f779d = false;
            } else if (newFocusPosition >= (this.f774a + f) - h) {
                this.f779d = true;
            }
        }
    }

    public boolean isAtEdge(View view) {
        return a(getViewPosition(view));
    }

    public boolean m202a(int i) {
        if (i == -1) {
            return false;
        }
        if (d(i) || e(i)) {
            return true;
        }
        return false;
    }

    public View focusSearch(View focused, int direction) {
        View a = a(focused, direction);
        if (a == null) {
            a = this.mRecyclerView.a(focused, direction);
            if (a == null || !((this.mFocusLeaveForbidden & direction) == 0 || a(this.mRecyclerView, a, direction))) {
                a(focused, direction);
                a = focused;
            }
        }
        if (!(a == focused || focused == null || focused.getAnimation() == null)) {
            focused.clearAnimation();
        }
        return a;
    }

    private boolean a(View view, View view2, int i) {
        int nextFocusForwardId;
        int id = view2.getId();
        switch (i) {
            case 2:
                nextFocusForwardId = view.getNextFocusForwardId();
                break;
            case 17:
                nextFocusForwardId = view.getNextFocusLeftId();
                break;
            case 33:
                nextFocusForwardId = view.getNextFocusUpId();
                break;
            case 66:
                nextFocusForwardId = view.getNextFocusRightId();
                break;
            case Service.CISCO_FNA /*130*/:
                nextFocusForwardId = view.getNextFocusDownId();
                break;
            default:
                nextFocusForwardId = -1;
                break;
        }
        if (nextFocusForwardId == -1 || id != nextFocusForwardId) {
            return false;
        }
        return true;
    }

    private void m192a(View view, int i) {
        if ((this.mShakeForbidden & i) == 0 && view != null && view.hasFocus()) {
            if (view instanceof ViewGroup) {
                View focusedChild = ((ViewGroup) view).getFocusedChild();
                if (focusedChild != null) {
                    view = focusedChild;
                }
            }
            AnimationUtils.shakeAnimation(getContext(), view, i);
        }
    }

    private View a(View view, int i) {
        int i2 = this.f774a;
        int movement = getMovement(i);
        View a = c.a().a(this.mRecyclerView, view, i, a.LEFT);
        i2 = getViewPosition(a);
        if (a != null && movement == 4 && (i2 > this.f774a + 1 || i2 < this.f774a)) {
            a = null;
        }
        if (a != null) {
            return a;
        }
        if (movement == 8 || movement == 16) {
            if ((movement == 8 ? b(this.f774a) : c(this.f774a)) || a(this.f774a)) {
                return null;
            }
            this.f778c = b();
            return view;
        } else if (!this.mFocusLoop || (this.mFocusLeaveForbidden & i) == 0) {
            return null;
        } else {
            i2 = this.f774a;
            do {
                if (movement == 4) {
                    i2++;
                } else {
                    i2--;
                }
                if (i2 < getFirstIndex() || i2 > getLastPosition()) {
                    return null;
                }
                a = getViewByPosition(i2);
                if (a == null) {
                    this.f778c = true;
                    a = view;
                }
            } while (!a.isFocusable());
            this.mRecyclerView.lineFeed();
            return a;
        }
    }

    private boolean b(int i) {
        View viewByPosition = getViewByPosition(i - 1);
        return (viewByPosition == null || viewByPosition.isFocusable()) ? false : true;
    }

    private boolean m194c(int i) {
        View viewByPosition = getViewByPosition(i + 1);
        return (viewByPosition == null || viewByPosition.isFocusable()) ? false : true;
    }

    public boolean onRequestChildFocus(View child, View focused) {
        if (child == null || focused == null) {
            return false;
        }
        if (!(this.f776a || this.f777b)) {
            a(child, true);
        }
        if (a[0] == 0 && a[1] == 0) {
            return false;
        }
        return true;
    }

    public int scrollBy(int da, int state) {
        boolean z = state == 3;
        if (!z) {
            if (da > 0) {
                if (!d() && this.b + da > this.d) {
                    da = this.d - this.b;
                }
            } else if (da < 0 && !e() && this.b + da < this.f) {
                da = this.f - this.b;
            }
        }
        c(da);
        this.b += da;
        if (!(this.f776a || z)) {
            if (da > 0) {
                if (this.mRecyclerView.c()) {
                    this.f775a.b(0);
                } else {
                    this.f775a.c(0);
                }
                a(false);
            } else {
                if (this.mRecyclerView.c()) {
                    this.f775a.d(a());
                } else {
                    this.f775a.e(a());
                }
                b(false);
            }
        }
        return da;
    }

    public boolean a(boolean z) {
        boolean a = this.f775a.a(a(), z);
        if (a) {
            d();
        }
        return a;
    }

    private boolean b(boolean z) {
        boolean a = this.f775a.a(z);
        if (a) {
            d();
        }
        return a;
    }

    private void d() {
        if (this.f778c) {
            this.mRecyclerView.c();
            this.f778c = false;
        }
    }

    public int a(int i) {
        View viewByPosition = getViewByPosition(i);
        if (viewByPosition != null) {
            return getOrientation() == Orientation.HORIZONTAL ? viewByPosition.getLeft() : viewByPosition.getTop();
        } else {
            LOG.e("GridLayoutManager", "index = " + i + " ChildCount = " + getChildCount() + " FirstAttachedIndex = " + getFirstAttachedPosition() + " LastAttachedIndex = " + getLastAttachedPosition());
            return 0;
        }
    }

    public int m203b(int i) {
        View viewByPosition = getViewByPosition(i);
        if (viewByPosition != null) {
            return getOrientation() == Orientation.HORIZONTAL ? viewByPosition.getRight() : viewByPosition.getBottom();
        } else {
            LOG.e("GridLayoutManager", "index = " + i + " ChildCount = " + getChildCount() + " FirstAttachedIndex = " + getFirstAttachedPosition() + " LastAttachedIndex = " + getLastAttachedPosition());
            return 0;
        }
    }

    public int m206c(int i) {
        View viewByPosition = getViewByPosition(i);
        if (viewByPosition != null) {
            return getOrientation() == Orientation.HORIZONTAL ? viewByPosition.getLeft() - this.mRecyclerView.getScrollX() : viewByPosition.getTop() - this.mRecyclerView.getScrollY();
        } else {
            LOG.e("GridLayoutManager", "index = " + i + " ChildCount = " + getChildCount() + " FirstAttachedIndex = " + getFirstAttachedPosition() + " LastAttachedIndex = " + getLastAttachedPosition());
            return 0;
        }
    }

    public int m207d(int i) {
        View viewByPosition = getViewByPosition(i);
        if (viewByPosition != null) {
            return getOrientation() == Orientation.HORIZONTAL ? viewByPosition.getRight() - this.mRecyclerView.getScrollX() : viewByPosition.getBottom() - this.mRecyclerView.getScrollY();
        } else {
            LOG.e("GridLayoutManager", "index = " + i + " ChildCount = " + getChildCount() + " FirstAttachedIndex = " + getFirstAttachedPosition() + " LastAttachedIndex = " + getLastAttachedPosition());
            return 0;
        }
    }

    public int a(d.a aVar, boolean z, Object[] objArr) {
        d(aVar.c);
        View viewForLocation = getViewForLocation(aVar);
        measureChild(viewForLocation);
        objArr[0] = viewForLocation;
        return getOrientation() == Orientation.HORIZONTAL ? viewForLocation.getMeasuredWidth() : viewForLocation.getMeasuredHeight();
    }

    public void measureChild(View child) {
        LayoutParams layoutParams = child.getLayoutParams();
        int i = this.mContentWidth != Integer.MIN_VALUE ? this.mContentWidth : layoutParams.width;
        int i2 = this.mContentHeight != Integer.MIN_VALUE ? this.mContentHeight : layoutParams.height;
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(this.h, 1073741824);
        int childMeasureSpec;
        if (getOrientation() == Orientation.HORIZONTAL) {
            childMeasureSpec = ViewGroup.getChildMeasureSpec(MeasureSpec.makeMeasureSpec(0, 0), 0, i);
            i = ViewGroup.getChildMeasureSpec(makeMeasureSpec, 0, i2);
            i2 = childMeasureSpec;
        } else {
            childMeasureSpec = ViewGroup.getChildMeasureSpec(makeMeasureSpec, 0, i);
            i = ViewGroup.getChildMeasureSpec(MeasureSpec.makeMeasureSpec(0, 0), 0, i2);
            i2 = childMeasureSpec;
        }
        child.measure(i2, i);
    }

    private int a(View view, int i, int i2) {
        int i3;
        int i4;
        int i5 = 0;
        LayoutParams layoutParams = view.getLayoutParams();
        if (getOrientation() == Orientation.HORIZONTAL) {
            i3 = this.j;
            if (this.mContentHeight > 0) {
                i4 = this.mContentHeight;
            } else if (layoutParams.height > 0) {
                i4 = layoutParams.height;
            } else {
                i4 = this.h;
            }
        } else {
            i3 = this.i;
            if (this.mContentWidth > 0) {
                i4 = this.mContentWidth;
            } else if (layoutParams.width > 0) {
                i4 = layoutParams.width;
            } else {
                i4 = this.h;
            }
        }
        int i6 = 0;
        while (i5 < i2) {
            i6 += i4 + i3;
            i5++;
        }
        return i6;
    }

    public void a(Object obj, int i, int i2, int i3, int i4) {
        View view = (View) obj;
        int i5 = i(i);
        int i6 = i4 + i2;
        int a = a(view, i, i3);
        int i7 = this.mGravity & HistoryInfoHelper.MSG_MERGE;
        int i8 = this.mGravity & 7;
        if ((this.mOrientation == Orientation.HORIZONTAL && i7 == 80) || (this.mOrientation == Orientation.VERTICAL && i8 == 5)) {
            a += this.h - i5;
        } else if ((this.mOrientation == Orientation.HORIZONTAL && i7 == 16) || (this.mOrientation == Orientation.VERTICAL && i8 == 1)) {
            a += (this.h - i5) / 2;
        }
        int i9;
        if (getOrientation() == Orientation.HORIZONTAL) {
            i7 = getPaddingTop() + a;
            a = i7 + i5;
            i9 = i7;
            i7 = i4;
            i4 = i9;
        } else {
            i7 = getPaddingLeft() + a;
            i9 = i6;
            i6 = i7 + i5;
            a = i9;
        }
        view.layout(i7, i4, i6, a);
        if (i == getLastPosition()) {
            e();
        }
        if (i == getFirstIndex()) {
            f();
        }
    }

    public void m204b(int i) {
        viewRecycled(getViewByPosition(i));
    }

    public void m200a(int i) {
        scrapView(getViewByPosition(i), false);
    }

    private void c(int i) {
        if (getOrientation() == Orientation.VERTICAL) {
            this.mRecyclerView.scrollBy(0, i);
        } else {
            this.mRecyclerView.scrollBy(i, 0);
        }
    }

    public int i(int i) {
        View viewByPosition = getViewByPosition(i);
        return getOrientation() == Orientation.HORIZONTAL ? viewByPosition.getMeasuredHeight() : viewByPosition.getMeasuredWidth();
    }

    private void e() {
        int lastAttachedPosition = getLastAttachedPosition();
        int lastPosition = getLastPosition();
        if (lastAttachedPosition < 0 || lastAttachedPosition != lastPosition) {
            this.c = Integer.MAX_VALUE;
            return;
        }
        this.c = (d(lastAttachedPosition) + this.b) + getPaddingHigh();
        lastAttachedPosition = a();
        if (this.e == Integer.MIN_VALUE || this.c - this.e > lastAttachedPosition) {
            this.d = this.c - lastAttachedPosition;
        } else {
            this.d = this.c;
        }
    }

    private void f() {
        int firstAttachedPosition = getFirstAttachedPosition();
        int firstIndex = getFirstIndex();
        if (this.e == Integer.MIN_VALUE && firstAttachedPosition >= 0 && firstAttachedPosition == firstIndex) {
            firstAttachedPosition = (c(firstAttachedPosition) + this.b) - getPaddingLow();
            this.f = firstAttachedPosition;
            this.e = firstAttachedPosition;
        }
    }

    private boolean m195d() {
        return this.c == Integer.MAX_VALUE;
    }

    private boolean m197e() {
        return this.e == Integer.MIN_VALUE;
    }

    private ViewHolder m191a(View view) {
        if (view == null) {
            return null;
        }
        return ((RecyclerView.LayoutParams) view.getLayoutParams()).a;
    }

    private void a(View view, boolean z) {
        this.mScrollingView = view;
        int viewPosition = getViewPosition(view);
        changeForward(viewPosition);
        boolean isFocusable = isFocusable(viewPosition);
        if (this.f774a != viewPosition && isFocusable) {
            this.f774a = viewPosition;
            this.mRecyclerView.a(viewPosition);
        }
        if (!view.hasFocus() && this.mRecyclerView.hasFocus() && isFocusable) {
            view.requestFocus();
        }
        if (view.getParent() != this.mRecyclerView) {
            view = getFocusView();
        }
        if (a(view, a)) {
            a(a[0], a[1], z);
        }
    }

    private void a(int i, int i2, boolean z) {
        if (i != 0 || i2 != 0) {
            if (this.f776a) {
                if (i == 0) {
                    i = i2;
                }
                scrollBy(i, 0);
                return;
            }
            smoothScrollBy(i, i2);
        }
    }

    private boolean m196d(int i) {
        return i < f(0);
    }

    private boolean e(int i) {
        int lastPosition = getLastPosition();
        return i >= lastPosition - h(lastPosition);
    }

    private boolean m198f() {
        return this.mFocusPlace == FocusPlace.FOCUS_CENTER || (this.mFocusPlace == FocusPlace.FOCUS_CUSTOM && this.mScrollCenterLow == this.mScrollCenterHigh);
    }

    private boolean a(View view, int[] iArr) {
        if (view == null) {
            return false;
        }
        int a = a(view);
        int b = b(view);
        if (!this.f776a && !f() && ((!this.f779d || b <= a) && (this.f779d || b >= a))) {
            return false;
        }
        a = getViewPosition(view);
        a = a(view, d(a), e(a)) - this.b;
        if (this.mOrientation == Orientation.HORIZONTAL) {
            iArr[0] = a;
            iArr[1] = 0;
        } else {
            iArr[0] = 0;
            iArr[1] = a;
        }
        return true;
    }

    private int a(View view, boolean z, boolean z2) {
        int a = a();
        int a2 = a(view);
        int i = a - a2;
        int b = b(view) + this.b;
        if (!e() && !d() && this.c - this.e <= a) {
            return this.e;
        }
        if (!e() && (z || b - this.e <= a2)) {
            return this.e;
        }
        if (d() || (!z2 && this.c - b > i)) {
            return b - a2;
        }
        return this.c - a;
    }

    private int a(View view) {
        if (this.mFocusPlace == FocusPlace.FOCUS_CENTER) {
            if (getOrientation() == Orientation.HORIZONTAL) {
                return getWidth() / 2;
            }
            return getHeight() / 2;
        } else if (this.mFocusPlace == FocusPlace.FOCUS_EDGE) {
            return getOrientation() == Orientation.HORIZONTAL ? this.f779d ? (getWidth() - (view.getWidth() / 2)) - getPaddingHigh() : (view.getWidth() / 2) + getPaddingLow() : this.f779d ? (getHeight() - (view.getHeight() / 2)) - getPaddingHigh() : (view.getHeight() / 2) + getPaddingLow();
        } else {
            if ((this.mScrollCenterLow <= 0 || this.mScrollCenterLow >= getHeight()) && (this.mScrollCenterHigh <= 0 || this.mScrollCenterHigh >= getHeight())) {
                return 0;
            }
            return this.f779d ? this.mScrollCenterHigh : this.mScrollCenterLow;
        }
    }

    private int b(View view) {
        return getOrientation() == Orientation.HORIZONTAL ? c(view) : d(view);
    }

    private int c(View view) {
        return (view.getLeft() - this.mRecyclerView.getScrollX()) + (view.getWidth() / 2);
    }

    private int d(View view) {
        return (view.getTop() - this.mRecyclerView.getScrollY()) + (view.getHeight() / 2);
    }

    private int a() {
        return getOrientation() == Orientation.HORIZONTAL ? getWidth() : getHeight();
    }

    public int getFocusPosition() {
        return this.f774a;
    }

    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        if (gainFocus) {
            int i = this.f774a;
            while (true) {
                View viewByPosition = getViewByPosition(i);
                if (viewByPosition != null) {
                    if (viewByPosition.getVisibility() == 0 && viewByPosition.hasFocusable()) {
                        viewByPosition.requestFocus();
                        return;
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public int getFirstAttachedPosition() {
        return this.f775a.f();
    }

    public int getLastAttachedPosition() {
        return this.f775a.g();
    }

    public void setNumRows(int numRows) {
        this.g = numRows;
        this.f775a.a(numRows);
    }

    private void d(int i) {
        int width = getWidth();
        int height = getHeight();
        int f = f(i);
        if (getOrientation() == Orientation.HORIZONTAL) {
            this.h = (((height - ((f - 1) * this.j)) - getPaddingTop()) - getPaddingBottom()) / f;
        } else {
            this.h = (((width - ((f - 1) * this.i)) - getPaddingLeft()) - getPaddingRight()) / f;
        }
    }

    public void setVerticalMargin(int margin) {
        this.j = margin;
    }

    public void setHorizontalMargin(int margin) {
        this.i = margin;
    }

    public void onUpdateChildren() {
        for (int firstAttachedPosition = getFirstAttachedPosition(); firstAttachedPosition <= getLastAttachedPosition(); firstAttachedPosition++) {
            ViewHolder a = a(getViewByPosition(firstAttachedPosition));
            if (a != null) {
                updateItem(a, a.getLayoutPosition());
            }
        }
    }

    public void setFocusPosition(int focusPosition) {
        this.f774a = focusPosition;
    }

    public boolean gridOnRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (!this.mFocusMemorable) {
            this.f774a = 0;
        }
        View viewByPosition = getViewByPosition(this.f774a);
        if (viewByPosition == null) {
            return false;
        }
        LOG.d("view isFocused = " + viewByPosition.isFocused() + " findFocus = " + viewByPosition.findFocus());
        if (!viewByPosition.isFocused() && viewByPosition.findFocus() == null) {
            return viewByPosition.requestFocus(direction, previouslyFocusedRect);
        }
        LOG.d("&&&&&&&&&&&");
        return true;
    }

    public boolean resumeChildFocus(View view) {
        this.f778c = false;
        if (!view.isFocused()) {
            view.requestFocus();
        }
        if (a[0] == 0 && a[1] == 0) {
            return false;
        }
        return true;
    }

    boolean m205b() {
        if (!e() && !d() && this.c - this.e <= a()) {
            return false;
        }
        if (this.f779d) {
            if (d() || this.b < this.d) {
                return true;
            }
            return false;
        } else if (e() || this.b > this.f) {
            return true;
        } else {
            return false;
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event, int direction) {
        this.k = getMovement(direction);
        if (!(event.getAction() == 0)) {
            this.mRecyclerView.b();
            return false;
        } else if (event.getRepeatCount() <= 0) {
            this.mRecyclerView.d();
            return false;
        } else if (event.getRepeatCount() == 1) {
            return true;
        } else {
            if (this.mScrollMode == 1) {
                boolean a = RecyclerView.a;
                if (a) {
                    return a;
                }
                return this.mRecyclerView.b();
            }
            this.mRecyclerView.e();
            return false;
        }
    }

    public int getVerticalMargin() {
        return this.j;
    }

    public int getHorizontalMargin() {
        return this.i;
    }

    public int getNumRows() {
        return this.g;
    }

    public int f(int i) {
        return this.mRecyclerView.getNumRows(i);
    }

    public boolean onAddFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if ((!this.mFocusMemorable || this.mRecyclerView.hasFocus() || this.mRecyclerView.isQuickSmooth()) && getChildCount() > 0) {
            int lastAttachedPosition = getLastAttachedPosition();
            for (int firstAttachedPosition = getFirstAttachedPosition(); firstAttachedPosition <= lastAttachedPosition; firstAttachedPosition++) {
                View viewByPosition = getViewByPosition(firstAttachedPosition);
                if (viewByPosition != null && viewByPosition.isShown()) {
                    viewByPosition.addFocusables(views, direction, focusableMode);
                }
            }
        } else if (this.mRecyclerView.isFocusable()) {
            views.add(this.mRecyclerView);
        }
        return true;
    }

    public void onAdapterChanged(Adapter oldAdapter) {
        if (oldAdapter != null) {
            this.f774a = -1;
        }
    }

    public int m208e(int i) {
        if (this.mRecyclerView.f696a == null) {
            return this.l;
        }
        return this.mRecyclerView.f696a.getItemOffsets(i, this.mRecyclerView);
    }

    public View getFocusView() {
        return getViewByPosition(this.f774a);
    }

    public void onRemoved(int position) {
        View viewByPosition = getViewByPosition(position);
        if (viewByPosition != null) {
            scrapView(viewByPosition, true);
            d dVar = this.f775a;
            dVar.b--;
        }
    }

    public boolean isNeedRequestFocus() {
        return this.f778c;
    }

    public void onItemsRemoved(int positionStart, int itemCount) {
        if (this.f774a != -1) {
            int i = this.f774a;
            if (positionStart <= i && positionStart + itemCount < i) {
                this.f774a -= itemCount;
            }
        }
    }

    public void onItemsAdded(int positionStart, int itemCount) {
        if (this.f774a != -1 && positionStart <= this.f774a) {
            this.f774a += itemCount;
        }
    }

    public void setExtraPadding(int extraPadding) {
        this.f775a.f(extraPadding);
    }

    public int getMovement() {
        return this.k;
    }

    public void onFocusLost(ViewHolder holder) {
        this.k = 16;
    }

    public int h(int i) {
        return this.mRecyclerView.getColumn(i);
    }

    public int g(int i) {
        return this.mRecyclerView.getRow(i);
    }

    public boolean hasScrollOffset() {
        return this.f == Integer.MIN_VALUE || this.e == Integer.MIN_VALUE;
    }

    public int getMinScroll() {
        return this.f;
    }
}

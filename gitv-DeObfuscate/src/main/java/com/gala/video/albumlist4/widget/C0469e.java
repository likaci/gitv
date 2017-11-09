package com.gala.video.albumlist4.widget;

import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.gala.video.albumlist4.utils.AnimationUtils;
import com.gala.video.albumlist4.utils.LOG;
import com.gala.video.albumlist4.widget.C0465c.C0463a;
import com.gala.video.albumlist4.widget.C0468d.C0466a;
import com.gala.video.albumlist4.widget.C0468d.C0467b;
import com.gala.video.albumlist4.widget.LayoutManager.FocusPlace;
import com.gala.video.albumlist4.widget.LayoutManager.Orientation;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.lib.share.ifimpl.ucenter.history.impl.HistoryInfoHelper;
import java.util.ArrayList;
import org.xbill.DNS.WKSRecord.Service;

class C0469e extends LayoutManager implements C0467b {
    private static int[] f1809a = new int[2];
    private int f1810a = -1;
    private C0468d f1811a = new C0470f(this);
    private boolean f1812a;
    private int f1813b = 0;
    private boolean f1814b;
    private int f1815c = Integer.MAX_VALUE;
    private boolean f1816c = false;
    private int f1817d = 0;
    private boolean f1818d = true;
    private int f1819e = Integer.MIN_VALUE;
    private int f1820f = Integer.MIN_VALUE;
    private int f1821g = 1;
    private int f1822h = 0;
    private int f1823i = 0;
    private int f1824j = 0;
    private int f1825k = 16;
    private int f1826l = 0;

    public C0469e(RecyclerView recyclerView) {
        super(recyclerView);
    }

    public synchronized void onLayoutChildren() {
        this.f1812a = true;
        boolean hasFocus = this.mRecyclerView.hasFocus();
        if (getCount() == 0) {
            m1454a();
            this.mRecyclerView.removeUnattachedViews();
        } else {
            if (m1470c()) {
                m1466b();
            } else {
                if (this.f1810a != -1) {
                    while (m1454a()) {
                        if (getViewByPosition(this.f1810a) != null) {
                            break;
                        }
                    }
                }
                while (true) {
                    m1482f();
                    m1479e();
                    int f = this.f1811a.m1451f();
                    int g = this.f1811a.m1453g();
                    View viewByPosition = getViewByPosition(this.f1810a);
                    if (viewByPosition != null) {
                        m1462a(viewByPosition, false);
                        if (hasFocus && !viewByPosition.hasFocus()) {
                            viewByPosition.requestFocus();
                        }
                    }
                    m1468b(false);
                    m1491a(false);
                    this.f1811a.m1433b(0);
                    this.f1811a.m1448d(m1454a());
                    if (this.f1811a.m1451f() == f && this.f1811a.m1453g() == g) {
                        break;
                    }
                }
                this.mRecyclerView.m1292f();
            }
            this.mRecyclerView.removeUnattachedViews();
            this.f1812a = false;
        }
    }

    private void m1466b() {
        m1482f();
        m1479e();
        m1491a(false);
        m1468b(false);
    }

    public boolean m1489a() {
        return m1491a(true);
    }

    private boolean m1472c() {
        this.f1826l = getOrientation() == Orientation.HORIZONTAL ? this.f1823i : this.f1824j;
        int count = getCount();
        if (this.f1810a >= count) {
            this.f1810a = count - 1;
        } else if (this.f1810a == -1 && count > 0) {
            this.f1810a = 0;
        }
        if (!this.mRecyclerView.f1642a && this.f1811a.m1451f() >= 0) {
            return true;
        }
        m1470c();
        m1454a();
        return false;
    }

    private void m1470c() {
        this.mRecyclerView.scrollBy(-this.mRecyclerView.getScrollX(), -this.mRecyclerView.getScrollY());
        this.f1813b = 0;
        this.f1811a.m1435a();
        this.f1819e = Integer.MIN_VALUE;
    }

    public void m1486a() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            scrapView(getChildAt(i), false);
        }
    }

    public void changeForward(int newFocusPosition) {
        boolean z = true;
        if (this.f1825k == 16 || this.f1825k == 8) {
            if (this.f1825k != 16) {
                z = false;
            }
            this.f1818d = z;
        } else if (this.f1825k == 4 || this.f1825k == 2) {
            int f = mo1005f(this.f1810a);
            int h = mo1018h(this.f1810a);
            if (newFocusPosition < this.f1810a - h) {
                this.f1818d = false;
            } else if (newFocusPosition >= (this.f1810a + f) - h) {
                this.f1818d = true;
            }
        }
    }

    public boolean isAtEdge(View view) {
        return mo996a(getViewPosition(view));
    }

    public boolean m1490a(int i) {
        if (i == -1) {
            return false;
        }
        if (mo1002d(i) || mo1004e(i)) {
            return true;
        }
        return false;
    }

    public View focusSearch(View focused, int direction) {
        View a = m1458a(focused, direction);
        if (a == null) {
            a = this.mRecyclerView.m1273a(focused, direction);
            if (a == null || !((this.mFocusLeaveForbidden & direction) == 0 || m1463a(this.mRecyclerView, a, direction))) {
                m1458a(focused, direction);
                a = focused;
            }
        }
        if (!(a == focused || focused == null || focused.getAnimation() == null)) {
            focused.clearAnimation();
        }
        return a;
    }

    private boolean m1463a(View view, View view2, int i) {
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

    private void m1461a(View view, int i) {
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

    private View m1458a(View view, int i) {
        int i2 = this.f1810a;
        int movement = getMovement(i);
        View a = C0465c.m1401a().m1411a(this.mRecyclerView, view, i, C0463a.LEFT);
        i2 = getViewPosition(a);
        if (a != null && movement == 4 && (i2 > this.f1810a + 1 || i2 < this.f1810a)) {
            a = null;
        }
        if (a != null) {
            return a;
        }
        if (movement == 8 || movement == 16) {
            if ((movement == 8 ? mo999b(this.f1810a) : mo1000c(this.f1810a)) || mo996a(this.f1810a)) {
                return null;
            }
            this.f1816c = m1466b();
            return view;
        } else if (!this.mFocusLoop || (this.mFocusLeaveForbidden & i) == 0) {
            return null;
        } else {
            i2 = this.f1810a;
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
                    this.f1816c = true;
                    a = view;
                }
            } while (!a.isFocusable());
            this.mRecyclerView.lineFeed();
            return a;
        }
    }

    private boolean mo999b(int i) {
        View viewByPosition = getViewByPosition(i - 1);
        return (viewByPosition == null || viewByPosition.isFocusable()) ? false : true;
    }

    private boolean m1473c(int i) {
        View viewByPosition = getViewByPosition(i + 1);
        return (viewByPosition == null || viewByPosition.isFocusable()) ? false : true;
    }

    public boolean onRequestChildFocus(View child, View focused) {
        if (child == null || focused == null) {
            return false;
        }
        if (!(this.f1812a || this.f1814b)) {
            m1462a(child, true);
        }
        if (f1809a[0] == 0 && f1809a[1] == 0) {
            return false;
        }
        return true;
    }

    public int scrollBy(int da, int state) {
        boolean z = state == 3;
        if (!z) {
            if (da > 0) {
                if (!m1475d() && this.f1813b + da > this.f1817d) {
                    da = this.f1817d - this.f1813b;
                }
            } else if (da < 0 && !m1479e() && this.f1813b + da < this.f1820f) {
                da = this.f1820f - this.f1813b;
            }
        }
        mo1000c(da);
        this.f1813b += da;
        if (!(this.f1812a || z)) {
            if (da > 0) {
                if (this.mRecyclerView.m1287c()) {
                    this.f1811a.m1433b(0);
                } else {
                    this.f1811a.m1434c(0);
                }
                m1491a(false);
            } else {
                if (this.mRecyclerView.m1287c()) {
                    this.f1811a.m1448d(m1454a());
                } else {
                    this.f1811a.m1450e(m1454a());
                }
                m1468b(false);
            }
        }
        return da;
    }

    public boolean m1491a(boolean z) {
        boolean a = this.f1811a.mo1041a(m1454a(), z);
        if (a) {
            m1475d();
        }
        return a;
    }

    private boolean m1468b(boolean z) {
        boolean a = this.f1811a.mo1042a(z);
        if (a) {
            m1475d();
        }
        return a;
    }

    private void m1475d() {
        if (this.f1816c) {
            this.mRecyclerView.m1287c();
            this.f1816c = false;
        }
    }

    public int mo996a(int i) {
        View viewByPosition = getViewByPosition(i);
        if (viewByPosition != null) {
            return getOrientation() == Orientation.HORIZONTAL ? viewByPosition.getLeft() : viewByPosition.getTop();
        } else {
            LOG.m1211e("GridLayoutManager", "index = " + i + " ChildCount = " + getChildCount() + " FirstAttachedIndex = " + getFirstAttachedPosition() + " LastAttachedIndex = " + getLastAttachedPosition());
            return 0;
        }
    }

    public int m1492b(int i) {
        View viewByPosition = getViewByPosition(i);
        if (viewByPosition != null) {
            return getOrientation() == Orientation.HORIZONTAL ? viewByPosition.getRight() : viewByPosition.getBottom();
        } else {
            LOG.m1211e("GridLayoutManager", "index = " + i + " ChildCount = " + getChildCount() + " FirstAttachedIndex = " + getFirstAttachedPosition() + " LastAttachedIndex = " + getLastAttachedPosition());
            return 0;
        }
    }

    public int m1495c(int i) {
        View viewByPosition = getViewByPosition(i);
        if (viewByPosition != null) {
            return getOrientation() == Orientation.HORIZONTAL ? viewByPosition.getLeft() - this.mRecyclerView.getScrollX() : viewByPosition.getTop() - this.mRecyclerView.getScrollY();
        } else {
            LOG.m1211e("GridLayoutManager", "index = " + i + " ChildCount = " + getChildCount() + " FirstAttachedIndex = " + getFirstAttachedPosition() + " LastAttachedIndex = " + getLastAttachedPosition());
            return 0;
        }
    }

    public int m1496d(int i) {
        View viewByPosition = getViewByPosition(i);
        if (viewByPosition != null) {
            return getOrientation() == Orientation.HORIZONTAL ? viewByPosition.getRight() - this.mRecyclerView.getScrollX() : viewByPosition.getBottom() - this.mRecyclerView.getScrollY();
        } else {
            LOG.m1211e("GridLayoutManager", "index = " + i + " ChildCount = " + getChildCount() + " FirstAttachedIndex = " + getFirstAttachedPosition() + " LastAttachedIndex = " + getLastAttachedPosition());
            return 0;
        }
    }

    public int mo997a(C0466a c0466a, boolean z, Object[] objArr) {
        mo1002d(c0466a.f1803c);
        View viewForLocation = getViewForLocation(c0466a);
        measureChild(viewForLocation);
        objArr[0] = viewForLocation;
        return getOrientation() == Orientation.HORIZONTAL ? viewForLocation.getMeasuredWidth() : viewForLocation.getMeasuredHeight();
    }

    public void measureChild(View child) {
        LayoutParams layoutParams = child.getLayoutParams();
        int i = this.mContentWidth != Integer.MIN_VALUE ? this.mContentWidth : layoutParams.width;
        int i2 = this.mContentHeight != Integer.MIN_VALUE ? this.mContentHeight : layoutParams.height;
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(this.f1822h, 1073741824);
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

    private int m1456a(View view, int i, int i2) {
        int i3;
        int i4;
        int i5 = 0;
        LayoutParams layoutParams = view.getLayoutParams();
        if (getOrientation() == Orientation.HORIZONTAL) {
            i3 = this.f1824j;
            if (this.mContentHeight > 0) {
                i4 = this.mContentHeight;
            } else if (layoutParams.height > 0) {
                i4 = layoutParams.height;
            } else {
                i4 = this.f1822h;
            }
        } else {
            i3 = this.f1823i;
            if (this.mContentWidth > 0) {
                i4 = this.mContentWidth;
            } else if (layoutParams.width > 0) {
                i4 = layoutParams.width;
            } else {
                i4 = this.f1822h;
            }
        }
        int i6 = 0;
        while (i5 < i2) {
            i6 += i4 + i3;
            i5++;
        }
        return i6;
    }

    public void mo998a(Object obj, int i, int i2, int i3, int i4) {
        View view = (View) obj;
        int i5 = m1501i(i);
        int i6 = i4 + i2;
        int a = m1456a(view, i, i3);
        int i7 = this.mGravity & HistoryInfoHelper.MSG_MERGE;
        int i8 = this.mGravity & 7;
        if ((this.mOrientation == Orientation.HORIZONTAL && i7 == 80) || (this.mOrientation == Orientation.VERTICAL && i8 == 5)) {
            a += this.f1822h - i5;
        } else if ((this.mOrientation == Orientation.HORIZONTAL && i7 == 16) || (this.mOrientation == Orientation.VERTICAL && i8 == 1)) {
            a += (this.f1822h - i5) / 2;
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
            m1479e();
        }
        if (i == getFirstIndex()) {
            m1482f();
        }
    }

    public void m1493b(int i) {
        viewRecycled(getViewByPosition(i));
    }

    public void m1487a(int i) {
        scrapView(getViewByPosition(i), false);
    }

    private void mo1000c(int i) {
        if (getOrientation() == Orientation.VERTICAL) {
            this.mRecyclerView.scrollBy(0, i);
        } else {
            this.mRecyclerView.scrollBy(i, 0);
        }
    }

    public int m1501i(int i) {
        View viewByPosition = getViewByPosition(i);
        return getOrientation() == Orientation.HORIZONTAL ? viewByPosition.getMeasuredHeight() : viewByPosition.getMeasuredWidth();
    }

    private void m1479e() {
        int lastAttachedPosition = getLastAttachedPosition();
        int lastPosition = getLastPosition();
        if (lastAttachedPosition < 0 || lastAttachedPosition != lastPosition) {
            this.f1815c = Integer.MAX_VALUE;
            return;
        }
        this.f1815c = (mo1002d(lastAttachedPosition) + this.f1813b) + getPaddingHigh();
        lastAttachedPosition = m1454a();
        if (this.f1819e == Integer.MIN_VALUE || this.f1815c - this.f1819e > lastAttachedPosition) {
            this.f1817d = this.f1815c - lastAttachedPosition;
        } else {
            this.f1817d = this.f1815c;
        }
    }

    private void m1482f() {
        int firstAttachedPosition = getFirstAttachedPosition();
        int firstIndex = getFirstIndex();
        if (this.f1819e == Integer.MIN_VALUE && firstAttachedPosition >= 0 && firstAttachedPosition == firstIndex) {
            firstAttachedPosition = (mo1000c(firstAttachedPosition) + this.f1813b) - getPaddingLow();
            this.f1820f = firstAttachedPosition;
            this.f1819e = firstAttachedPosition;
        }
    }

    private boolean m1477d() {
        return this.f1815c == Integer.MAX_VALUE;
    }

    private boolean m1480e() {
        return this.f1819e == Integer.MIN_VALUE;
    }

    private ViewHolder m1459a(View view) {
        if (view == null) {
            return null;
        }
        return ((RecyclerView.LayoutParams) view.getLayoutParams()).f1702a;
    }

    private void m1462a(View view, boolean z) {
        this.mScrollingView = view;
        int viewPosition = getViewPosition(view);
        changeForward(viewPosition);
        boolean isFocusable = isFocusable(viewPosition);
        if (this.f1810a != viewPosition && isFocusable) {
            this.f1810a = viewPosition;
            this.mRecyclerView.m1213a(viewPosition);
        }
        if (!view.hasFocus() && this.mRecyclerView.hasFocus() && isFocusable) {
            view.requestFocus();
        }
        if (view.getParent() != this.mRecyclerView) {
            view = getFocusView();
        }
        if (m1464a(view, f1809a)) {
            m1460a(f1809a[0], f1809a[1], z);
        }
    }

    private void m1460a(int i, int i2, boolean z) {
        if (i != 0 || i2 != 0) {
            if (this.f1812a) {
                if (i == 0) {
                    i = i2;
                }
                scrollBy(i, 0);
                return;
            }
            smoothScrollBy(i, i2);
        }
    }

    private boolean m1478d(int i) {
        return i < mo1005f(0);
    }

    private boolean mo1004e(int i) {
        int lastPosition = getLastPosition();
        return i >= lastPosition - mo1018h(lastPosition);
    }

    private boolean m1483f() {
        return this.mFocusPlace == FocusPlace.FOCUS_CENTER || (this.mFocusPlace == FocusPlace.FOCUS_CUSTOM && this.mScrollCenterLow == this.mScrollCenterHigh);
    }

    private boolean m1464a(View view, int[] iArr) {
        if (view == null) {
            return false;
        }
        int a = m1455a(view);
        int b = m1465b(view);
        if (!this.f1812a && !m1482f() && ((!this.f1818d || b <= a) && (this.f1818d || b >= a))) {
            return false;
        }
        a = getViewPosition(view);
        a = m1457a(view, mo1002d(a), mo1004e(a)) - this.f1813b;
        if (this.mOrientation == Orientation.HORIZONTAL) {
            iArr[0] = a;
            iArr[1] = 0;
        } else {
            iArr[0] = 0;
            iArr[1] = a;
        }
        return true;
    }

    private int m1457a(View view, boolean z, boolean z2) {
        int a = m1454a();
        int a2 = m1455a(view);
        int i = a - a2;
        int b = m1465b(view) + this.f1813b;
        if (!m1479e() && !m1475d() && this.f1815c - this.f1819e <= a) {
            return this.f1819e;
        }
        if (!m1479e() && (z || b - this.f1819e <= a2)) {
            return this.f1819e;
        }
        if (m1475d() || (!z2 && this.f1815c - b > i)) {
            return b - a2;
        }
        return this.f1815c - a;
    }

    private int m1455a(View view) {
        if (this.mFocusPlace == FocusPlace.FOCUS_CENTER) {
            if (getOrientation() == Orientation.HORIZONTAL) {
                return getWidth() / 2;
            }
            return getHeight() / 2;
        } else if (this.mFocusPlace == FocusPlace.FOCUS_EDGE) {
            return getOrientation() == Orientation.HORIZONTAL ? this.f1818d ? (getWidth() - (view.getWidth() / 2)) - getPaddingHigh() : (view.getWidth() / 2) + getPaddingLow() : this.f1818d ? (getHeight() - (view.getHeight() / 2)) - getPaddingHigh() : (view.getHeight() / 2) + getPaddingLow();
        } else {
            if ((this.mScrollCenterLow <= 0 || this.mScrollCenterLow >= getHeight()) && (this.mScrollCenterHigh <= 0 || this.mScrollCenterHigh >= getHeight())) {
                return 0;
            }
            return this.f1818d ? this.mScrollCenterHigh : this.mScrollCenterLow;
        }
    }

    private int m1465b(View view) {
        return getOrientation() == Orientation.HORIZONTAL ? m1469c(view) : m1474d(view);
    }

    private int m1469c(View view) {
        return (view.getLeft() - this.mRecyclerView.getScrollX()) + (view.getWidth() / 2);
    }

    private int m1474d(View view) {
        return (view.getTop() - this.mRecyclerView.getScrollY()) + (view.getHeight() / 2);
    }

    private int m1454a() {
        return getOrientation() == Orientation.HORIZONTAL ? getWidth() : getHeight();
    }

    public int getFocusPosition() {
        return this.f1810a;
    }

    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        if (gainFocus) {
            int i = this.f1810a;
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
        return this.f1811a.m1451f();
    }

    public int getLastAttachedPosition() {
        return this.f1811a.m1453g();
    }

    public void setNumRows(int numRows) {
        this.f1821g = numRows;
        this.f1811a.mo1040a(numRows);
    }

    private void mo1002d(int i) {
        int width = getWidth();
        int height = getHeight();
        int f = mo1005f(i);
        if (getOrientation() == Orientation.HORIZONTAL) {
            this.f1822h = (((height - ((f - 1) * this.f1824j)) - getPaddingTop()) - getPaddingBottom()) / f;
        } else {
            this.f1822h = (((width - ((f - 1) * this.f1823i)) - getPaddingLeft()) - getPaddingRight()) / f;
        }
    }

    public void setVerticalMargin(int margin) {
        this.f1824j = margin;
    }

    public void setHorizontalMargin(int margin) {
        this.f1823i = margin;
    }

    public void onUpdateChildren() {
        for (int firstAttachedPosition = getFirstAttachedPosition(); firstAttachedPosition <= getLastAttachedPosition(); firstAttachedPosition++) {
            ViewHolder a = m1455a(getViewByPosition(firstAttachedPosition));
            if (a != null) {
                updateItem(a, a.getLayoutPosition());
            }
        }
    }

    public void setFocusPosition(int focusPosition) {
        this.f1810a = focusPosition;
    }

    public boolean gridOnRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (!this.mFocusMemorable) {
            this.f1810a = 0;
        }
        View viewByPosition = getViewByPosition(this.f1810a);
        if (viewByPosition == null) {
            return false;
        }
        LOG.m1208d("view isFocused = " + viewByPosition.isFocused() + " findFocus = " + viewByPosition.findFocus());
        if (!viewByPosition.isFocused() && viewByPosition.findFocus() == null) {
            return viewByPosition.requestFocus(direction, previouslyFocusedRect);
        }
        LOG.m1208d("&&&&&&&&&&&");
        return true;
    }

    public boolean resumeChildFocus(View view) {
        this.f1816c = false;
        if (!view.isFocused()) {
            view.requestFocus();
        }
        if (f1809a[0] == 0 && f1809a[1] == 0) {
            return false;
        }
        return true;
    }

    boolean m1494b() {
        if (!m1479e() && !m1475d() && this.f1815c - this.f1819e <= m1454a()) {
            return false;
        }
        if (this.f1818d) {
            if (m1475d() || this.f1813b < this.f1817d) {
                return true;
            }
            return false;
        } else if (m1479e() || this.f1813b > this.f1820f) {
            return true;
        } else {
            return false;
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event, int direction) {
        this.f1825k = getMovement(direction);
        if (!(event.getAction() == 0)) {
            this.mRecyclerView.m1241b();
            return false;
        } else if (event.getRepeatCount() <= 0) {
            this.mRecyclerView.m1289d();
            return false;
        } else if (event.getRepeatCount() == 1) {
            return true;
        } else {
            if (this.mScrollMode == 1) {
                boolean a = RecyclerView.f1618a;
                if (a) {
                    return a;
                }
                return this.mRecyclerView.m1241b();
            }
            this.mRecyclerView.m1291e();
            return false;
        }
    }

    public int getVerticalMargin() {
        return this.f1824j;
    }

    public int getHorizontalMargin() {
        return this.f1823i;
    }

    public int getNumRows() {
        return this.f1821g;
    }

    public int mo1005f(int i) {
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
            this.f1810a = -1;
        }
    }

    public int m1497e(int i) {
        if (this.mRecyclerView.f1625a == null) {
            return this.f1826l;
        }
        return this.mRecyclerView.f1625a.getItemOffsets(i, this.mRecyclerView);
    }

    public View getFocusView() {
        return getViewByPosition(this.f1810a);
    }

    public void onRemoved(int position) {
        View viewByPosition = getViewByPosition(position);
        if (viewByPosition != null) {
            scrapView(viewByPosition, true);
            C0468d c0468d = this.f1811a;
            c0468d.f1806b--;
        }
    }

    public boolean isNeedRequestFocus() {
        return this.f1816c;
    }

    public void onItemsRemoved(int positionStart, int itemCount) {
        if (this.f1810a != -1) {
            int i = this.f1810a;
            if (positionStart <= i && positionStart + itemCount < i) {
                this.f1810a -= itemCount;
            }
        }
    }

    public void onItemsAdded(int positionStart, int itemCount) {
        if (this.f1810a != -1 && positionStart <= this.f1810a) {
            this.f1810a += itemCount;
        }
    }

    public void setExtraPadding(int extraPadding) {
        this.f1811a.m1452f(extraPadding);
    }

    public int getMovement() {
        return this.f1825k;
    }

    public void onFocusLost(ViewHolder holder) {
        this.f1825k = 16;
    }

    public int mo1018h(int i) {
        return this.mRecyclerView.getColumn(i);
    }

    public int mo1007g(int i) {
        return this.mRecyclerView.getRow(i);
    }

    public boolean hasScrollOffset() {
        return this.f1820f == Integer.MIN_VALUE || this.f1819e == Integer.MIN_VALUE;
    }

    public int getMinScroll() {
        return this.f1820f;
    }
}

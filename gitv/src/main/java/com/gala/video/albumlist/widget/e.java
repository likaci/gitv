package com.gala.video.albumlist.widget;

import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import com.gala.video.albumlist.layout.BlockLayout;
import com.gala.video.albumlist.utils.LOG;
import com.gala.video.albumlist.widget.BlocksView.Adapter;
import com.gala.video.albumlist.widget.BlocksView.LayoutParams;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.albumlist.widget.LayoutManager.FocusPlace;
import com.gala.video.albumlist.widget.LayoutManager.Orientation;
import com.gala.video.albumlist.widget.c.a;
import com.gala.video.albumlist.widget.d.b;
import com.gala.video.lib.share.ifimpl.ucenter.history.impl.HistoryInfoHelper;
import com.mcto.ads.internal.net.SendFlag;
import java.util.ArrayList;
import java.util.List;
import org.xbill.DNS.WKSRecord.Service;

class e extends LayoutManager implements b {
    private static int[] a = new int[2];
    protected int f676a = -1;
    private View f677a;
    private d f678a = new d(this);
    private boolean f679a;
    protected int b = -1;
    private boolean f680b = false;
    protected int c = 100;
    private boolean f681c = true;
    private int d = -1;
    private int e = 0;
    private int f = Integer.MAX_VALUE;
    private int g = 0;
    private int h = Integer.MIN_VALUE;
    private int i = Integer.MIN_VALUE;
    private int j = 0;
    private int k = 0;
    private int l = 0;
    private int m = 16;
    private int n = 0;

    public e(BlocksView blocksView) {
        super(blocksView);
    }

    public synchronized void onLayoutChildren() {
        if (this.mBlocksView.isAttached()) {
            this.f679a = true;
            boolean hasFocus = this.mBlocksView.hasFocus();
            if (getCount() == 0) {
                c();
                this.mBlocksView.removeUnattachedViews();
            } else {
                if (b()) {
                    a();
                    d();
                } else {
                    if (this.d != -1) {
                        while (a()) {
                            if (getViewByPosition(this.d) != null) {
                                break;
                            }
                        }
                    }
                    while (true) {
                        g();
                        f();
                        int i = this.f676a;
                        int i2 = this.b;
                        View viewByPosition = getViewByPosition(this.d);
                        if (viewByPosition != null) {
                            a(viewByPosition, false);
                        }
                        a(false);
                        b(false);
                        a(0);
                        c(d());
                        if (this.f676a == i && this.b == i2) {
                            break;
                        }
                    }
                    a(hasFocus);
                    this.mBlocksView.removeUnattachedViews();
                }
                this.f679a = false;
            }
        }
    }

    private void a(boolean z) {
        int i = this.d;
        while (i <= getLastAttachedPosition()) {
            View viewByPosition = getViewByPosition(i);
            if (viewByPosition != null && viewByPosition.isFocusable() && isFocusable(i)) {
                if (!viewByPosition.hasFocus() && z) {
                    viewByPosition.requestFocus();
                }
                this.d = i;
                return;
            }
            i++;
        }
        i = this.d;
        while (i >= getFirstAttachedPosition()) {
            viewByPosition = getViewByPosition(i);
            if (viewByPosition != null && viewByPosition.isFocusable() && isFocusable(i)) {
                if (!viewByPosition.hasFocus() && z) {
                    viewByPosition.requestFocus();
                }
                this.d = i;
                return;
            }
            i--;
        }
    }

    private void m139d() {
        a(false);
        b(false);
        g();
        f();
    }

    void m143a() {
        if (this.mBlocksView.isLayoutRequested()) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = getChildAt(i);
                if (childAt.getVisibility() != 8) {
                    childAt.measure(ViewGroup.getChildMeasureSpec(MeasureSpec.makeMeasureSpec(0, 0), 0, childAt.getWidth()), ViewGroup.getChildMeasureSpec(MeasureSpec.makeMeasureSpec(0, 0), 0, childAt.getHeight()));
                    childAt.layout(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
                }
            }
        }
    }

    public void fastLayoutChildren() {
        while (true) {
            g();
            f();
            int i = this.f676a;
            int i2 = this.b;
            a(false);
            b(false);
            a(0);
            c(d());
            if (this.f676a == i && this.b == i2) {
                return;
            }
        }
    }

    public boolean m145a() {
        return a(true);
    }

    private boolean b() {
        this.n = getOrientation() == Orientation.HORIZONTAL ? this.k : this.l;
        int count = getCount();
        if (this.d >= count) {
            this.d = count - 1;
        } else if (this.d == -1 && count > 0) {
            this.d = 0;
        }
        if (!this.mBlocksView.f609a && this.f676a >= 0) {
            return true;
        }
        this.mBlocksView.g();
        resetValues();
        c();
        return false;
    }

    public void resetValues() {
        this.f681c = true;
        this.mBlocksView.scrollBy(-this.mBlocksView.getScrollX(), -this.mBlocksView.getScrollY());
        this.e = 0;
        b();
        this.h = Integer.MIN_VALUE;
        this.f = Integer.MAX_VALUE;
    }

    public void m148b() {
        this.b = -1;
        this.f676a = -1;
    }

    public void m151c() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            scrapView(getChildAt(childCount), false);
        }
    }

    public void changeForward(int newFocusPosition) {
        boolean z = true;
        if (this.m == 16 || this.m == 8) {
            if (this.m != 16) {
                z = false;
            }
            this.f681c = z;
        } else if (this.m != 4 && this.m != 2) {
        } else {
            if (newFocusPosition < this.d) {
                this.f681c = false;
            } else {
                this.f681c = true;
            }
        }
    }

    public boolean isAtEdge(View view, int direction) {
        if (view == null) {
            return false;
        }
        boolean z = false;
        View view2 = view;
        while (this.mBlocksView.indexOfChild(view2) < 0) {
            view2 = (View) view2.getParent();
            z = true;
        }
        if (z) {
            View a = c.a().a((ViewGroup) view2, view, direction, a.LEFT);
            if (!(a == null || a == view)) {
                return false;
            }
        }
        if (isAtMin(view2) || isAtMax(view2)) {
            return true;
        }
        return false;
    }

    public boolean isAtMin(View view) {
        return a(view, this.mBlocksView.getDirection());
    }

    private boolean m137a(View view, int i) {
        boolean z;
        if (getFirstAttachedPosition() == 0 && (i == 17 || i == 33)) {
            z = true;
        } else {
            z = false;
        }
        int a = a(view);
        int viewPosition = getViewPosition(view) - 1;
        while (z && viewPosition >= 0) {
            View viewByPosition = getViewByPosition(viewPosition);
            if (i == 17) {
                if (viewByPosition != null && viewByPosition.isFocusable()) {
                    z = false;
                }
            } else if (viewByPosition != null && viewByPosition.isFocusable() && isFocusable(viewPosition) && a(viewByPosition) < a) {
                z = false;
            }
            viewPosition--;
        }
        return z;
    }

    public boolean isAtMax(View view) {
        return b(view, this.mBlocksView.getDirection());
    }

    private boolean m138b(View view, int i) {
        boolean z;
        if (getLastAttachedPosition() == getLastPosition() && (i == 66 || i == Service.CISCO_FNA)) {
            z = true;
        } else {
            z = false;
        }
        int b = b(view);
        int viewPosition = getViewPosition(view) + 1;
        while (z && viewPosition <= getLastPosition()) {
            View viewByPosition = getViewByPosition(viewPosition);
            if (i == 66) {
                if (viewByPosition != null && viewByPosition.isFocusable()) {
                    z = false;
                }
            } else if (viewByPosition != null && viewByPosition.isFocusable() && isFocusable(viewPosition) && b(viewByPosition) > b) {
                z = false;
            }
            viewPosition++;
        }
        return z;
    }

    public View focusSearch(View focused, int direction) {
        View a = a(focused, direction);
        if (BlocksView.containsView(this.mBlocksView, a)) {
            this.f677a = a;
        }
        return a;
    }

    private View a(View view, int i) {
        View view2;
        View b = b(view, i);
        if (b == null || isFocusable(getViewPosition(b))) {
            view2 = b;
            b = view;
        } else {
            onRequestChildFocus(b, b);
            view2 = null;
        }
        if (view2 == null) {
            this.mBlocksView.a(b);
            view2 = this.mBlocksView.a(b, i);
            if (view2 == null || !((this.mFocusLeaveForbidden & i) == 0 || a(this.mBlocksView, view2, i))) {
                a(view, i);
                view2 = view;
            }
        }
        if (!(view2 == view || view == null || view.getAnimation() == null)) {
            view.clearAnimation();
        }
        return view2;
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

    private void m136a(View view, int i) {
        if ((this.mShakeForbidden & i) == 0 && view != null && view.hasFocus()) {
            if (view instanceof ViewGroup) {
                View focusedChild = ((ViewGroup) view).getFocusedChild();
                if (focusedChild != null) {
                    view = focusedChild;
                }
            }
            this.mBlocksView.a(view, i);
        }
    }

    private View b(View view, int i) {
        int i2 = this.d;
        int movement = getMovement(i);
        View a = c.a().a(this.mBlocksView, view, i, a.CENTER);
        i2 = getViewPosition(a);
        if (a != null && movement == 4 && (i2 > this.d + 1 || i2 < this.d)) {
            a = null;
        }
        if (a != null) {
            return a;
        }
        if (movement == 8 || movement == 16) {
            if (isAtEdge(view, i)) {
                return null;
            }
            this.f680b = isCanScroll(this.f681c);
            return view;
        } else if (!this.mFocusLoop || (this.mFocusLeaveForbidden & i) == 0) {
            return null;
        } else {
            i2 = this.d;
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
                    this.f680b = true;
                    a = view;
                }
            } while (!a.isFocusable());
            this.mBlocksView.lineFeed();
            return a;
        }
    }

    public boolean onRequestChildFocus(View child, View focused) {
        if (child == null || focused == null) {
            return false;
        }
        this.f677a = focused;
        a(focused, true);
        if (a[0] == 0 && a[1] == 0) {
            return false;
        }
        return true;
    }

    public int scrollBy(int da, int state) {
        boolean z = state == 3;
        if (!z) {
            if (da > 0) {
                if (!c() && this.e + da > this.g) {
                    da = this.g - this.e;
                }
            } else if (da < 0 && !d() && this.e + da < this.i) {
                da = this.i - this.e;
            }
        }
        i(da);
        this.e += da;
        if (!(this.f679a || z || this.mScrollOnly)) {
            if (da > 0) {
                if (this.mBlocksView.c()) {
                    a(0);
                } else {
                    b(0);
                }
                a(false);
                f();
            } else {
                if (this.mBlocksView.c()) {
                    c(d());
                } else {
                    d(d());
                }
                b(false);
                g();
            }
        }
        return da;
    }

    protected void m144a(int i) {
        while (this.b >= this.f676a) {
            if ((e(this.f676a) <= i - a() ? 1 : null) != null) {
                h(this.f676a);
                this.f676a++;
            } else {
                return;
            }
        }
    }

    protected void m149b(int i) {
        for (int i2 = this.f676a; i2 <= this.b; i2++) {
            if ((e(i2) <= i ? 1 : null) != null) {
                e(i2);
            }
        }
    }

    protected void m152c(int i) {
        while (this.b >= this.f676a) {
            if ((d(this.b) >= a() + i ? 1 : null) != null) {
                h(this.b);
                this.b--;
            } else {
                return;
            }
        }
    }

    protected void m153d(int i) {
        for (int i2 = this.f676a; i2 <= this.b; i2++) {
            if ((d(i2) >= i ? 1 : null) != null) {
                e(i2);
            }
        }
    }

    public boolean m146a(boolean z) {
        boolean a = this.f678a.a(d(), z);
        if (a) {
            e();
        }
        return a;
    }

    private boolean b(boolean z) {
        boolean a = this.f678a.a(z);
        if (a) {
            e();
        }
        return a;
    }

    private void e() {
        if (this.f680b) {
            this.mBlocksView.c();
            this.f680b = false;
        }
    }

    public int a(int i) {
        return a(getViewByPosition(i));
    }

    public int m156g(int i) {
        LayoutParams layoutParams = (LayoutParams) getViewByPosition(i).getLayoutParams();
        return this.mOrientation == Orientation.HORIZONTAL ? layoutParams.leftMargin : layoutParams.topMargin;
    }

    public int m157h(int i) {
        LayoutParams layoutParams = (LayoutParams) getViewByPosition(i).getLayoutParams();
        return this.mOrientation == Orientation.HORIZONTAL ? layoutParams.rightMargin : layoutParams.bottomMargin;
    }

    public int m158i(int i) {
        LayoutParams layoutParams = (LayoutParams) getViewByPosition(i).getLayoutParams();
        return this.mOrientation == Orientation.HORIZONTAL ? layoutParams.topMargin : layoutParams.leftMargin;
    }

    public int m159j(int i) {
        LayoutParams layoutParams = (LayoutParams) getViewByPosition(i).getLayoutParams();
        return this.mOrientation == Orientation.HORIZONTAL ? layoutParams.bottomMargin : layoutParams.rightMargin;
    }

    public int m142a(View view) {
        if (view != null) {
            return getOrientation() == Orientation.HORIZONTAL ? view.getLeft() : view.getTop();
        } else {
            LOG.e("GridLayoutManager", "ChildCount = " + getChildCount() + " FirstAttachedIndex = " + getFirstAttachedPosition() + " LastAttachedIndex = " + getLastAttachedPosition());
            return 0;
        }
    }

    public int c(View view) {
        if (view != null) {
            return getOrientation() == Orientation.HORIZONTAL ? view.getTop() : view.getLeft();
        } else {
            LOG.e("GridLayoutManager", "ChildCount = " + getChildCount() + " FirstAttachedIndex = " + getFirstAttachedPosition() + " LastAttachedIndex = " + getLastAttachedPosition());
            return 0;
        }
    }

    public int c(int i) {
        return d(getViewByPosition(i));
    }

    public int d(View view) {
        if (view != null) {
            return getOrientation() == Orientation.HORIZONTAL ? view.getBottom() : view.getRight();
        } else {
            LOG.e("GridLayoutManager", "ChildCount = " + getChildCount() + " FirstAttachedIndex = " + getFirstAttachedPosition() + " LastAttachedIndex = " + getLastAttachedPosition());
            return 0;
        }
    }

    public int b(int i) {
        return b(getViewByPosition(i));
    }

    public int b(View view) {
        if (view != null) {
            return getOrientation() == Orientation.HORIZONTAL ? view.getRight() : view.getBottom();
        } else {
            LOG.e("GridLayoutManager", " ChildCount = " + getChildCount() + " FirstAttachedIndex = " + getFirstAttachedPosition() + " LastAttachedIndex = " + getLastAttachedPosition());
            return 0;
        }
    }

    public int d(int i) {
        View viewByPosition = getViewByPosition(i);
        if (viewByPosition != null) {
            return getOrientation() == Orientation.HORIZONTAL ? viewByPosition.getLeft() - this.mBlocksView.getScrollX() : viewByPosition.getTop() - this.mBlocksView.getScrollY();
        } else {
            LOG.e("GridLayoutManager", "index = " + i + " ChildCount = " + getChildCount() + " FirstAttachedIndex = " + getFirstAttachedPosition() + " LastAttachedIndex = " + getLastAttachedPosition());
            return 0;
        }
    }

    public int e(int i) {
        View viewByPosition = getViewByPosition(i);
        if (viewByPosition != null) {
            return getOrientation() == Orientation.HORIZONTAL ? viewByPosition.getRight() - this.mBlocksView.getScrollX() : viewByPosition.getBottom() - this.mBlocksView.getScrollY();
        } else {
            LOG.e("GridLayoutManager", "index = " + i + " ChildCount = " + getChildCount() + " FirstAttachedIndex = " + getFirstAttachedPosition() + " LastAttachedIndex = " + getLastAttachedPosition());
            return 0;
        }
    }

    public int a(d.a aVar, boolean z, Object[] objArr) {
        a(aVar.b, z);
        View viewForLocation = getViewForLocation(aVar);
        measureChild(viewForLocation);
        objArr[0] = viewForLocation;
        objArr[1] = Integer.valueOf(viewForLocation.getMeasuredWidth());
        objArr[2] = Integer.valueOf(viewForLocation.getMeasuredHeight());
        return getOrientation() == Orientation.HORIZONTAL ? viewForLocation.getMeasuredWidth() : viewForLocation.getMeasuredHeight();
    }

    public void measureChild(View child) {
        ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
        int i = this.mContentWidth != Integer.MIN_VALUE ? this.mContentWidth : layoutParams.width;
        int i2 = this.mContentHeight != Integer.MIN_VALUE ? this.mContentHeight : layoutParams.height;
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(this.j, 1073741824);
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
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (getOrientation() == Orientation.HORIZONTAL) {
            i3 = this.l;
            if (this.mContentHeight > 0) {
                i4 = this.mContentHeight;
            } else if (layoutParams.height > 0) {
                i4 = layoutParams.height;
            } else {
                i4 = this.j;
            }
        } else {
            i3 = this.k;
            if (this.mContentWidth > 0) {
                i4 = this.mContentWidth;
            } else if (layoutParams.width > 0) {
                i4 = layoutParams.width;
            } else {
                i4 = this.j;
            }
        }
        int i6 = 0;
        while (i5 < i2) {
            i6 += i4 + i3;
            i5++;
        }
        if (getOrientation() == Orientation.HORIZONTAL) {
            return getPaddingTop() + i6;
        }
        return getPaddingLeft() + i6;
    }

    public void a(Object obj, int i, int i2, int i3, int i4, boolean z) {
        View view = (View) obj;
        int l = l(i);
        int i5 = i4 + i2;
        if (getOrientation() == Orientation.HORIZONTAL) {
            l += i3;
        } else {
            int i6 = i5;
            i5 = l + i3;
            l = i6;
            int i7 = i4;
            i4 = i3;
            i3 = i7;
        }
        view.layout(i4, i3, i5, l);
        if (z) {
            f(i);
        } else {
            g(i);
        }
        this.mBlocksView.c(view);
    }

    public void b(Object obj, int i, int i2, int i3, int i4, boolean z) {
        View view = (View) obj;
        int l = l(i);
        int a = a(view, i, i3);
        int i5 = this.mGravity & HistoryInfoHelper.MSG_MERGE;
        int i6 = this.mGravity & 7;
        if ((this.mOrientation == Orientation.HORIZONTAL && i5 == 80) || (this.mOrientation == Orientation.VERTICAL && i6 == 5)) {
            a += this.j - l;
        } else if ((this.mOrientation == Orientation.HORIZONTAL && i5 == 16) || (this.mOrientation == Orientation.VERTICAL && i6 == 1)) {
            a += (this.j - l) / 2;
        }
        a(obj, i, i2, a, i4, z);
    }

    private void f(int i) {
        if (getFirstAttachedPosition() < 0 || getLastAttachedPosition() < 0) {
            this.b = i;
            this.f676a = i;
            return;
        }
        this.b = i;
    }

    private void g(int i) {
        if (getFirstAttachedPosition() < 0 || getLastAttachedPosition() < 0) {
            this.b = i;
            this.f676a = i;
        } else if (i < this.f676a) {
            this.f676a = i;
        }
    }

    public void m154e(int i) {
        viewRecycled(getViewByPosition(i));
    }

    private void h(int i) {
        scrapView(getViewByPosition(i), true);
    }

    private void i(int i) {
        if (getOrientation() == Orientation.VERTICAL) {
            this.mBlocksView.scrollBy(0, i);
        } else {
            this.mBlocksView.scrollBy(i, 0);
        }
    }

    public int l(int i) {
        View viewByPosition = getViewByPosition(i);
        return getOrientation() == Orientation.HORIZONTAL ? viewByPosition.getMeasuredHeight() : viewByPosition.getMeasuredWidth();
    }

    private void f() {
        int lastAttachedPosition = getLastAttachedPosition();
        int lastPosition = getLastPosition();
        if (lastAttachedPosition < 0 || lastAttachedPosition != lastPosition) {
            this.f = Integer.MAX_VALUE;
            return;
        }
        this.f = (e(c()) + this.e) + getPaddingMax();
        lastAttachedPosition = d();
        if (this.h == Integer.MIN_VALUE || this.f - this.h > lastAttachedPosition) {
            this.g = this.f - lastAttachedPosition;
        } else {
            this.g = this.f;
        }
    }

    private void g() {
        int firstAttachedPosition = getFirstAttachedPosition();
        int firstIndex = getFirstIndex();
        if (firstAttachedPosition < 0 || firstAttachedPosition != firstIndex) {
            this.h = Integer.MIN_VALUE;
            return;
        }
        firstAttachedPosition = (d(b()) + this.e) - getPaddingMin();
        this.i = firstAttachedPosition;
        this.h = firstAttachedPosition;
    }

    private boolean c() {
        return this.f == Integer.MAX_VALUE;
    }

    private boolean m140d() {
        return this.h == Integer.MIN_VALUE;
    }

    private ViewHolder a(View view) {
        if (view == null) {
            return null;
        }
        return ((LayoutParams) view.getLayoutParams()).a;
    }

    private void j(int i) {
        this.mBlocksView.a(this.d, false);
        this.mBlocksView.a(i, true);
    }

    private void a(View view, boolean z) {
        int viewPosition = getViewPosition(view);
        changeForward(viewPosition);
        boolean isFocusable = isFocusable(viewPosition);
        if (this.d != viewPosition && isFocusable) {
            j(viewPosition);
            this.d = viewPosition;
        }
        if (!view.hasFocus() && this.mBlocksView.hasFocus() && isFocusable) {
            view.requestFocus();
        }
        if (view.getParent() != this.mBlocksView) {
            view = getFocusView();
        }
        if (this.f679a || view != this.mScrollingView) {
            this.mScrollingView = view;
            this.mBlocksView.a(this.d);
            if (a(this.mScrollingView, a)) {
                a(a[0], a[1], z);
            }
        }
    }

    private void a(int i, int i2, boolean z) {
        if (i != 0 || i2 != 0) {
            if (this.f679a) {
                if (i == 0) {
                    i = i2;
                }
                scrollBy(i, 0);
                return;
            }
            smoothScrollBy(i, i2);
        }
    }

    private boolean m141e() {
        return this.mFocusPlace == FocusPlace.FOCUS_CENTER || (this.mFocusPlace == FocusPlace.FOCUS_CUSTOM && this.mScrollCenterLow == this.mScrollCenterHigh);
    }

    private boolean a(View view, int[] iArr) {
        if (view == null) {
            return false;
        }
        int e = e(view);
        int f = f(view);
        if (!this.f679a && !e() && ((!this.f681c || f <= e) && (this.f681c || f >= e))) {
            return false;
        }
        e = a(view, isAtMin(view), isAtMax(view)) - this.e;
        if (this.mOrientation == Orientation.HORIZONTAL) {
            iArr[0] = e;
            iArr[1] = 0;
        } else {
            iArr[0] = 0;
            iArr[1] = e;
        }
        return true;
    }

    private int a(View view, boolean z, boolean z2) {
        int d = d();
        int e = e(view);
        int i = d - e;
        int f = f(view) + this.e;
        if (!d() && !c() && this.f - this.h <= d) {
            return this.h;
        }
        if (!d() && (z || f - this.h <= e)) {
            return this.h;
        }
        if (c() || (!z2 && this.f - f > i)) {
            return f - e;
        }
        return this.f - d;
    }

    private int e(View view) {
        if (this.mFocusPlace == FocusPlace.FOCUS_CENTER) {
            if (getOrientation() == Orientation.HORIZONTAL) {
                return getWidth() / 2;
            }
            return getHeight() / 2;
        } else if (this.mFocusPlace == FocusPlace.FOCUS_EDGE) {
            return getOrientation() == Orientation.HORIZONTAL ? this.f681c ? (getWidth() - (view.getWidth() / 2)) - getPaddingMax() : (view.getWidth() / 2) + getPaddingMin() : this.f681c ? (getHeight() - (view.getHeight() / 2)) - getPaddingMax() : (view.getHeight() / 2) + getPaddingMin();
        } else {
            if ((this.mScrollCenterLow <= 0 || this.mScrollCenterLow >= getHeight()) && (this.mScrollCenterHigh <= 0 || this.mScrollCenterHigh >= getHeight())) {
                return 0;
            }
            return this.f681c ? this.mScrollCenterHigh : this.mScrollCenterLow;
        }
    }

    private int f(View view) {
        return getOrientation() == Orientation.HORIZONTAL ? g(view) : h(view);
    }

    private int g(View view) {
        return (view.getLeft() - this.mBlocksView.getScrollX()) + (view.getWidth() / 2);
    }

    private int h(View view) {
        return (view.getTop() - this.mBlocksView.getScrollY()) + (view.getHeight() / 2);
    }

    private int d() {
        return getOrientation() == Orientation.HORIZONTAL ? getWidth() : getHeight();
    }

    public int getFocusPosition() {
        return this.d;
    }

    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        if (gainFocus && this.mBlocksView.getDescendantFocusability() == SendFlag.FLAG_KEY_PINGBACK_MID) {
            int i = this.d;
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
        return this.f676a;
    }

    public int getLastAttachedPosition() {
        return this.b;
    }

    private void a(int i, boolean z) {
        int width = getWidth();
        int height = getHeight();
        int a = this.f678a.a(i, z);
        if (getOrientation() == Orientation.HORIZONTAL) {
            this.j = (((height - ((a - 1) * this.l)) - getPaddingTop()) - getPaddingBottom()) / a;
        } else {
            this.j = (((width - ((a - 1) * this.k)) - getPaddingLeft()) - getPaddingRight()) / a;
        }
    }

    public void setVerticalMargin(int margin) {
        this.l = margin;
    }

    public void setHorizontalMargin(int margin) {
        this.k = margin;
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
        this.d = focusPosition;
    }

    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        View viewByPosition = getViewByPosition(this.d);
        if (viewByPosition != null && viewByPosition.isFocusable() && isFocusable(this.d)) {
            return viewByPosition.requestFocus(direction, previouslyFocusedRect);
        }
        return true;
    }

    public boolean resumeChildFocus(View view) {
        this.f680b = false;
        if (!view.isFocused()) {
            view.requestFocus();
        }
        if (a[0] == 0 && a[1] == 0) {
            return false;
        }
        return true;
    }

    public boolean isCanScroll(boolean isForward) {
        if (this.f676a < 0 || this.b < 0) {
            return false;
        }
        if (!d() && !c() && this.f - this.h <= d()) {
            return false;
        }
        if (isForward) {
            if (c() || this.e < this.g) {
                return true;
            }
            return false;
        } else if (d() || this.e > this.i) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCanScroll() {
        if (this.f676a == -1 || this.b == -1) {
            return false;
        }
        if (d() || c() || this.f - this.h > d()) {
            return true;
        }
        return false;
    }

    public boolean dispatchKeyEvent(KeyEvent event, int direction) {
        this.m = getMovement(direction);
        if (!(event.getAction() == 0)) {
            this.mBlocksView.b();
            return false;
        } else if (event.getRepeatCount() <= 0) {
            this.mBlocksView.d();
            return false;
        } else if (event.getRepeatCount() == 1) {
            return true;
        } else {
            if (this.mScrollMode == 1) {
                return this.mBlocksView.a();
            }
            this.mBlocksView.e();
            return false;
        }
    }

    public int getVerticalMargin() {
        return this.l;
    }

    public int getHorizontalMargin() {
        return this.k;
    }

    public boolean onAddFocusables(ArrayList<View> views, int direction, int focusableMode) {
        boolean z = this.mBlocksView.hasFocus() || this.mBlocksView.isQuickSmooth();
        if ((!this.mFocusMemorable || z) && getChildCount() > 0) {
            int lastAttachedPosition = getLastAttachedPosition();
            for (int firstAttachedPosition = getFirstAttachedPosition(); firstAttachedPosition <= lastAttachedPosition; firstAttachedPosition++) {
                View viewByPosition = getViewByPosition(firstAttachedPosition);
                if (viewByPosition != null && viewByPosition.isShown() && (z || (!z && this.mBlocksView.isChildVisible(viewByPosition, true)))) {
                    viewByPosition.addFocusables(views, direction, focusableMode);
                }
            }
        } else if (this.mBlocksView.isFocusable()) {
            views.add(this.mBlocksView);
        }
        return true;
    }

    public void onAdapterChanged(Adapter oldAdapter) {
        if (oldAdapter != null) {
            this.d = -1;
        }
    }

    public int m155f(int i) {
        if (this.mBlocksView.f589a == null) {
            return this.n;
        }
        return this.mBlocksView.f589a.getItemOffsets(i, this.mBlocksView);
    }

    public View getFocusView() {
        return getViewByPosition(this.d);
    }

    public void onRemoved(int position) {
        View viewByPosition = getViewByPosition(position);
        if (viewByPosition != null) {
            scrapView(viewByPosition, true);
            this.b--;
        }
    }

    public boolean isNeedRequestFocus() {
        return this.f680b;
    }

    public void onItemsRemoved(int positionStart, int itemCount) {
        if (this.d != -1) {
            int i = this.d;
            if (positionStart <= i && positionStart + itemCount < i) {
                this.d -= itemCount;
            }
        }
    }

    public void onItemsAdded(int positionStart, int itemCount) {
        if (this.d != -1 && positionStart < this.d) {
            this.d += itemCount;
        }
    }

    public void setExtraPadding(int extraPadding) {
        this.c = extraPadding;
    }

    public int getMovement() {
        return this.m;
    }

    public void onFocusLost(ViewHolder holder) {
        this.m = 16;
    }

    public int k(int i) {
        return this.mBlocksView.getColumn(i);
    }

    public boolean hasScrollOffset() {
        return this.i == Integer.MIN_VALUE || this.h == Integer.MIN_VALUE;
    }

    public int getMinScroll() {
        return this.i;
    }

    public int a() {
        return this.c;
    }

    public int m147b() {
        int firstAttachedPosition = getFirstAttachedPosition();
        for (int i = firstAttachedPosition + 1; i < getLastAttachedPosition(); i++) {
            View viewByPosition = getViewByPosition(i);
            View viewByPosition2 = getViewByPosition(firstAttachedPosition);
            if (viewByPosition != null) {
                if (viewByPosition.getTop() < viewByPosition2.getTop()) {
                    firstAttachedPosition = i;
                } else if (viewByPosition.getTop() >= viewByPosition2.getBottom()) {
                    break;
                }
            }
        }
        return firstAttachedPosition;
    }

    public int m150c() {
        int lastAttachedPosition = getLastAttachedPosition();
        for (int i = lastAttachedPosition - 1; i > getFirstAttachedPosition(); i--) {
            View viewByPosition = getViewByPosition(i);
            View viewByPosition2 = getViewByPosition(lastAttachedPosition);
            if (viewByPosition != null) {
                if (viewByPosition.getBottom() > viewByPosition2.getBottom()) {
                    lastAttachedPosition = i;
                } else if (viewByPosition.getBottom() <= viewByPosition2.getTop()) {
                    break;
                }
            }
        }
        return lastAttachedPosition;
    }

    public BlockLayout getBlockLayout(int position) {
        return this.f678a.a(position);
    }

    public View findFocus() {
        return this.f677a;
    }

    public void setLayouts(List<BlockLayout> layouts) {
        this.f678a.a((List) layouts);
    }

    public void onScrollStop() {
        this.mScrollingView = null;
    }

    public boolean isOnTop() {
        return this.i != Integer.MIN_VALUE && this.i == this.e;
    }
}

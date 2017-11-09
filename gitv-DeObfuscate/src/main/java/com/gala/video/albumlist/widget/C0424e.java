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
import com.gala.video.albumlist.widget.C0420c.C0418a;
import com.gala.video.albumlist.widget.C0423d.C0421a;
import com.gala.video.albumlist.widget.C0423d.C0422b;
import com.gala.video.albumlist.widget.LayoutManager.FocusPlace;
import com.gala.video.albumlist.widget.LayoutManager.Orientation;
import com.gala.video.lib.share.ifimpl.ucenter.history.impl.HistoryInfoHelper;
import com.mcto.ads.internal.net.SendFlag;
import java.util.ArrayList;
import java.util.List;
import org.xbill.DNS.WKSRecord.Service;

class C0424e extends LayoutManager implements C0422b {
    private static int[] f1581a = new int[2];
    protected int f1582a = -1;
    private View f1583a;
    private C0423d f1584a = new C0423d(this);
    private boolean f1585a;
    protected int f1586b = -1;
    private boolean f1587b = false;
    protected int f1588c = 100;
    private boolean f1589c = true;
    private int f1590d = -1;
    private int f1591e = 0;
    private int f1592f = Integer.MAX_VALUE;
    private int f1593g = 0;
    private int f1594h = Integer.MIN_VALUE;
    private int f1595i = Integer.MIN_VALUE;
    private int f1596j = 0;
    private int f1597k = 0;
    private int f1598l = 0;
    private int f1599m = 16;
    private int f1600n = 0;

    public C0424e(BlocksView blocksView) {
        super(blocksView);
    }

    public synchronized void onLayoutChildren() {
        if (this.mBlocksView.isAttached()) {
            this.f1585a = true;
            boolean hasFocus = this.mBlocksView.hasFocus();
            if (getCount() == 0) {
                m1147c();
                this.mBlocksView.removeUnattachedViews();
            } else {
                if (m1144b()) {
                    mo908a();
                    m1148d();
                } else {
                    if (this.f1590d != -1) {
                        while (mo908a()) {
                            if (getViewByPosition(this.f1590d) != null) {
                                break;
                            }
                        }
                    }
                    while (true) {
                        m1158g();
                        m1155f();
                        int i = this.f1582a;
                        int i2 = this.f1586b;
                        View viewByPosition = getViewByPosition(this.f1590d);
                        if (viewByPosition != null) {
                            m1138a(viewByPosition, false);
                        }
                        m1139a(false);
                        m1146b(false);
                        mo909a(0);
                        mo916c(m1148d());
                        if (this.f1582a == i && this.f1586b == i2) {
                            break;
                        }
                    }
                    m1139a(hasFocus);
                    this.mBlocksView.removeUnattachedViews();
                }
                this.f1585a = false;
            }
        }
    }

    private void m1139a(boolean z) {
        int i = this.f1590d;
        while (i <= getLastAttachedPosition()) {
            View viewByPosition = getViewByPosition(i);
            if (viewByPosition != null && viewByPosition.isFocusable() && isFocusable(i)) {
                if (!viewByPosition.hasFocus() && z) {
                    viewByPosition.requestFocus();
                }
                this.f1590d = i;
                return;
            }
            i++;
        }
        i = this.f1590d;
        while (i >= getFirstAttachedPosition()) {
            viewByPosition = getViewByPosition(i);
            if (viewByPosition != null && viewByPosition.isFocusable() && isFocusable(i)) {
                if (!viewByPosition.hasFocus() && z) {
                    viewByPosition.requestFocus();
                }
                this.f1590d = i;
                return;
            }
            i--;
        }
    }

    private void m1149d() {
        m1139a(false);
        m1146b(false);
        m1158g();
        m1155f();
    }

    void m1168a() {
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
            m1158g();
            m1155f();
            int i = this.f1582a;
            int i2 = this.f1586b;
            m1139a(false);
            m1146b(false);
            mo909a(0);
            mo916c(m1148d());
            if (this.f1582a == i && this.f1586b == i2) {
                return;
            }
        }
    }

    public boolean m1171a() {
        return m1139a(true);
    }

    private boolean m1144b() {
        this.f1600n = getOrientation() == Orientation.HORIZONTAL ? this.f1597k : this.f1598l;
        int count = getCount();
        if (this.f1590d >= count) {
            this.f1590d = count - 1;
        } else if (this.f1590d == -1 && count > 0) {
            this.f1590d = 0;
        }
        if (!this.mBlocksView.f1458a && this.f1582a >= 0) {
            return true;
        }
        this.mBlocksView.m1020g();
        resetValues();
        m1147c();
        return false;
    }

    public void resetValues() {
        this.f1589c = true;
        this.mBlocksView.scrollBy(-this.mBlocksView.getScrollX(), -this.mBlocksView.getScrollY());
        this.f1591e = 0;
        m1144b();
        this.f1594h = Integer.MIN_VALUE;
        this.f1592f = Integer.MAX_VALUE;
    }

    public void m1176b() {
        this.f1586b = -1;
        this.f1582a = -1;
    }

    public void m1182c() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            scrapView(getChildAt(childCount), false);
        }
    }

    public void changeForward(int newFocusPosition) {
        boolean z = true;
        if (this.f1599m == 16 || this.f1599m == 8) {
            if (this.f1599m != 16) {
                z = false;
            }
            this.f1589c = z;
        } else if (this.f1599m != 4 && this.f1599m != 2) {
        } else {
            if (newFocusPosition < this.f1590d) {
                this.f1589c = false;
            } else {
                this.f1589c = true;
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
            View a = C0420c.m1080a().m1092a((ViewGroup) view2, view, direction, C0418a.LEFT);
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
        return m1133a(view, this.mBlocksView.getDirection());
    }

    private boolean m1140a(View view, int i) {
        boolean z;
        if (getFirstAttachedPosition() == 0 && (i == 17 || i == 33)) {
            z = true;
        } else {
            z = false;
        }
        int a = mo910a(view);
        int viewPosition = getViewPosition(view) - 1;
        while (z && viewPosition >= 0) {
            View viewByPosition = getViewByPosition(viewPosition);
            if (i == 17) {
                if (viewByPosition != null && viewByPosition.isFocusable()) {
                    z = false;
                }
            } else if (viewByPosition != null && viewByPosition.isFocusable() && isFocusable(viewPosition) && mo910a(viewByPosition) < a) {
                z = false;
            }
            viewPosition--;
        }
        return z;
    }

    public boolean isAtMax(View view) {
        return m1143b(view, this.mBlocksView.getDirection());
    }

    private boolean m1145b(View view, int i) {
        boolean z;
        if (getLastAttachedPosition() == getLastPosition() && (i == 66 || i == Service.CISCO_FNA)) {
            z = true;
        } else {
            z = false;
        }
        int b = mo914b(view);
        int viewPosition = getViewPosition(view) + 1;
        while (z && viewPosition <= getLastPosition()) {
            View viewByPosition = getViewByPosition(viewPosition);
            if (i == 66) {
                if (viewByPosition != null && viewByPosition.isFocusable()) {
                    z = false;
                }
            } else if (viewByPosition != null && viewByPosition.isFocusable() && isFocusable(viewPosition) && mo914b(viewByPosition) > b) {
                z = false;
            }
            viewPosition++;
        }
        return z;
    }

    public View focusSearch(View focused, int direction) {
        View a = m1133a(focused, direction);
        if (BlocksView.containsView(this.mBlocksView, a)) {
            this.f1583a = a;
        }
        return a;
    }

    private View m1133a(View view, int i) {
        View view2;
        View b = m1143b(view, i);
        if (b == null || isFocusable(getViewPosition(b))) {
            view2 = b;
            b = view;
        } else {
            onRequestChildFocus(b, b);
            view2 = null;
        }
        if (view2 == null) {
            this.mBlocksView.m994a(b);
            view2 = this.mBlocksView.m995a(b, i);
            if (view2 == null || !((this.mFocusLeaveForbidden & i) == 0 || m1141a(this.mBlocksView, view2, i))) {
                m1133a(view, i);
                view2 = view;
            }
        }
        if (!(view2 == view || view == null || view.getAnimation() == null)) {
            view.clearAnimation();
        }
        return view2;
    }

    private boolean m1141a(View view, View view2, int i) {
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

    private void m1137a(View view, int i) {
        if ((this.mShakeForbidden & i) == 0 && view != null && view.hasFocus()) {
            if (view instanceof ViewGroup) {
                View focusedChild = ((ViewGroup) view).getFocusedChild();
                if (focusedChild != null) {
                    view = focusedChild;
                }
            }
            this.mBlocksView.m995a(view, i);
        }
    }

    private View m1143b(View view, int i) {
        int i2 = this.f1590d;
        int movement = getMovement(i);
        View a = C0420c.m1080a().m1092a(this.mBlocksView, view, i, C0418a.CENTER);
        i2 = getViewPosition(a);
        if (a != null && movement == 4 && (i2 > this.f1590d + 1 || i2 < this.f1590d)) {
            a = null;
        }
        if (a != null) {
            return a;
        }
        if (movement == 8 || movement == 16) {
            if (isAtEdge(view, i)) {
                return null;
            }
            this.f1587b = isCanScroll(this.f1589c);
            return view;
        } else if (!this.mFocusLoop || (this.mFocusLeaveForbidden & i) == 0) {
            return null;
        } else {
            i2 = this.f1590d;
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
                    this.f1587b = true;
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
        this.f1583a = focused;
        m1138a(focused, true);
        if (f1581a[0] == 0 && f1581a[1] == 0) {
            return false;
        }
        return true;
    }

    public int scrollBy(int da, int state) {
        boolean z = state == 3;
        if (!z) {
            if (da > 0) {
                if (!m1147c() && this.f1591e + da > this.f1593g) {
                    da = this.f1593g - this.f1591e;
                }
            } else if (da < 0 && !m1148d() && this.f1591e + da < this.f1595i) {
                da = this.f1595i - this.f1591e;
            }
        }
        mo939i(da);
        this.f1591e += da;
        if (!(this.f1585a || z || this.mScrollOnly)) {
            if (da > 0) {
                if (this.mBlocksView.m1013c()) {
                    mo909a(0);
                } else {
                    mo913b(0);
                }
                m1139a(false);
                m1155f();
            } else {
                if (this.mBlocksView.m1013c()) {
                    mo916c(m1148d());
                } else {
                    mo919d(m1148d());
                }
                m1146b(false);
                m1158g();
            }
        }
        return da;
    }

    protected void m1169a(int i) {
        while (this.f1586b >= this.f1582a) {
            if ((mo922e(this.f1582a) <= i - mo908a() ? 1 : null) != null) {
                mo937h(this.f1582a);
                this.f1582a++;
            } else {
                return;
            }
        }
    }

    protected void m1177b(int i) {
        for (int i2 = this.f1582a; i2 <= this.f1586b; i2++) {
            if ((mo922e(i2) <= i ? 1 : null) != null) {
                mo922e(i2);
            }
        }
    }

    protected void m1183c(int i) {
        while (this.f1586b >= this.f1582a) {
            if ((mo919d(this.f1586b) >= mo908a() + i ? 1 : null) != null) {
                mo937h(this.f1586b);
                this.f1586b--;
            } else {
                return;
            }
        }
    }

    protected void m1186d(int i) {
        for (int i2 = this.f1582a; i2 <= this.f1586b; i2++) {
            if ((mo919d(i2) >= i ? 1 : null) != null) {
                mo922e(i2);
            }
        }
    }

    public boolean m1172a(boolean z) {
        boolean a = this.f1584a.m1124a(m1148d(), z);
        if (a) {
            m1152e();
        }
        return a;
    }

    private boolean m1146b(boolean z) {
        boolean a = this.f1584a.m1129a(z);
        if (a) {
            m1152e();
        }
        return a;
    }

    private void m1152e() {
        if (this.f1587b) {
            this.mBlocksView.m1013c();
            this.f1587b = false;
        }
    }

    public int mo909a(int i) {
        return mo910a(getViewByPosition(i));
    }

    public int m1190g(int i) {
        LayoutParams layoutParams = (LayoutParams) getViewByPosition(i).getLayoutParams();
        return this.mOrientation == Orientation.HORIZONTAL ? layoutParams.leftMargin : layoutParams.topMargin;
    }

    public int m1191h(int i) {
        LayoutParams layoutParams = (LayoutParams) getViewByPosition(i).getLayoutParams();
        return this.mOrientation == Orientation.HORIZONTAL ? layoutParams.rightMargin : layoutParams.bottomMargin;
    }

    public int m1192i(int i) {
        LayoutParams layoutParams = (LayoutParams) getViewByPosition(i).getLayoutParams();
        return this.mOrientation == Orientation.HORIZONTAL ? layoutParams.topMargin : layoutParams.leftMargin;
    }

    public int m1193j(int i) {
        LayoutParams layoutParams = (LayoutParams) getViewByPosition(i).getLayoutParams();
        return this.mOrientation == Orientation.HORIZONTAL ? layoutParams.bottomMargin : layoutParams.rightMargin;
    }

    public int m1166a(View view) {
        if (view != null) {
            return getOrientation() == Orientation.HORIZONTAL ? view.getLeft() : view.getTop();
        } else {
            LOG.m872e("GridLayoutManager", "ChildCount = " + getChildCount() + " FirstAttachedIndex = " + getFirstAttachedPosition() + " LastAttachedIndex = " + getLastAttachedPosition());
            return 0;
        }
    }

    public int mo917c(View view) {
        if (view != null) {
            return getOrientation() == Orientation.HORIZONTAL ? view.getTop() : view.getLeft();
        } else {
            LOG.m872e("GridLayoutManager", "ChildCount = " + getChildCount() + " FirstAttachedIndex = " + getFirstAttachedPosition() + " LastAttachedIndex = " + getLastAttachedPosition());
            return 0;
        }
    }

    public int mo916c(int i) {
        return mo920d(getViewByPosition(i));
    }

    public int mo920d(View view) {
        if (view != null) {
            return getOrientation() == Orientation.HORIZONTAL ? view.getBottom() : view.getRight();
        } else {
            LOG.m872e("GridLayoutManager", "ChildCount = " + getChildCount() + " FirstAttachedIndex = " + getFirstAttachedPosition() + " LastAttachedIndex = " + getLastAttachedPosition());
            return 0;
        }
    }

    public int mo913b(int i) {
        return mo914b(getViewByPosition(i));
    }

    public int mo914b(View view) {
        if (view != null) {
            return getOrientation() == Orientation.HORIZONTAL ? view.getRight() : view.getBottom();
        } else {
            LOG.m872e("GridLayoutManager", " ChildCount = " + getChildCount() + " FirstAttachedIndex = " + getFirstAttachedPosition() + " LastAttachedIndex = " + getLastAttachedPosition());
            return 0;
        }
    }

    public int mo919d(int i) {
        View viewByPosition = getViewByPosition(i);
        if (viewByPosition != null) {
            return getOrientation() == Orientation.HORIZONTAL ? viewByPosition.getLeft() - this.mBlocksView.getScrollX() : viewByPosition.getTop() - this.mBlocksView.getScrollY();
        } else {
            LOG.m872e("GridLayoutManager", "index = " + i + " ChildCount = " + getChildCount() + " FirstAttachedIndex = " + getFirstAttachedPosition() + " LastAttachedIndex = " + getLastAttachedPosition());
            return 0;
        }
    }

    public int mo922e(int i) {
        View viewByPosition = getViewByPosition(i);
        if (viewByPosition != null) {
            return getOrientation() == Orientation.HORIZONTAL ? viewByPosition.getRight() - this.mBlocksView.getScrollX() : viewByPosition.getBottom() - this.mBlocksView.getScrollY();
        } else {
            LOG.m872e("GridLayoutManager", "index = " + i + " ChildCount = " + getChildCount() + " FirstAttachedIndex = " + getFirstAttachedPosition() + " LastAttachedIndex = " + getLastAttachedPosition());
            return 0;
        }
    }

    public int mo911a(C0421a c0421a, boolean z, Object[] objArr) {
        m1136a(c0421a.f1577b, z);
        View viewForLocation = getViewForLocation(c0421a);
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
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(this.f1596j, 1073741824);
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

    private int m1131a(View view, int i, int i2) {
        int i3;
        int i4;
        int i5 = 0;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (getOrientation() == Orientation.HORIZONTAL) {
            i3 = this.f1598l;
            if (this.mContentHeight > 0) {
                i4 = this.mContentHeight;
            } else if (layoutParams.height > 0) {
                i4 = layoutParams.height;
            } else {
                i4 = this.f1596j;
            }
        } else {
            i3 = this.f1597k;
            if (this.mContentWidth > 0) {
                i4 = this.mContentWidth;
            } else if (layoutParams.width > 0) {
                i4 = layoutParams.width;
            } else {
                i4 = this.f1596j;
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

    public void mo912a(Object obj, int i, int i2, int i3, int i4, boolean z) {
        View view = (View) obj;
        int l = m1195l(i);
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
            mo923f(i);
        } else {
            mo927g(i);
        }
        this.mBlocksView.m1014c(view);
    }

    public void mo915b(Object obj, int i, int i2, int i3, int i4, boolean z) {
        View view = (View) obj;
        int l = m1195l(i);
        int a = m1131a(view, i, i3);
        int i5 = this.mGravity & HistoryInfoHelper.MSG_MERGE;
        int i6 = this.mGravity & 7;
        if ((this.mOrientation == Orientation.HORIZONTAL && i5 == 80) || (this.mOrientation == Orientation.VERTICAL && i6 == 5)) {
            a += this.f1596j - l;
        } else if ((this.mOrientation == Orientation.HORIZONTAL && i5 == 16) || (this.mOrientation == Orientation.VERTICAL && i6 == 1)) {
            a += (this.f1596j - l) / 2;
        }
        mo912a(obj, i, i2, a, i4, z);
    }

    private void mo923f(int i) {
        if (getFirstAttachedPosition() < 0 || getLastAttachedPosition() < 0) {
            this.f1586b = i;
            this.f1582a = i;
            return;
        }
        this.f1586b = i;
    }

    private void mo927g(int i) {
        if (getFirstAttachedPosition() < 0 || getLastAttachedPosition() < 0) {
            this.f1586b = i;
            this.f1582a = i;
        } else if (i < this.f1582a) {
            this.f1582a = i;
        }
    }

    public void m1188e(int i) {
        viewRecycled(getViewByPosition(i));
    }

    private void mo937h(int i) {
        scrapView(getViewByPosition(i), true);
    }

    private void mo939i(int i) {
        if (getOrientation() == Orientation.VERTICAL) {
            this.mBlocksView.scrollBy(0, i);
        } else {
            this.mBlocksView.scrollBy(i, 0);
        }
    }

    public int m1195l(int i) {
        View viewByPosition = getViewByPosition(i);
        return getOrientation() == Orientation.HORIZONTAL ? viewByPosition.getMeasuredHeight() : viewByPosition.getMeasuredWidth();
    }

    private void m1155f() {
        int lastAttachedPosition = getLastAttachedPosition();
        int lastPosition = getLastPosition();
        if (lastAttachedPosition < 0 || lastAttachedPosition != lastPosition) {
            this.f1592f = Integer.MAX_VALUE;
            return;
        }
        this.f1592f = (mo922e(m1147c()) + this.f1591e) + getPaddingMax();
        lastAttachedPosition = m1148d();
        if (this.f1594h == Integer.MIN_VALUE || this.f1592f - this.f1594h > lastAttachedPosition) {
            this.f1593g = this.f1592f - lastAttachedPosition;
        } else {
            this.f1593g = this.f1592f;
        }
    }

    private void m1158g() {
        int firstAttachedPosition = getFirstAttachedPosition();
        int firstIndex = getFirstIndex();
        if (firstAttachedPosition < 0 || firstAttachedPosition != firstIndex) {
            this.f1594h = Integer.MIN_VALUE;
            return;
        }
        firstAttachedPosition = (mo919d(m1144b()) + this.f1591e) - getPaddingMin();
        this.f1595i = firstAttachedPosition;
        this.f1594h = firstAttachedPosition;
    }

    private boolean m1147c() {
        return this.f1592f == Integer.MAX_VALUE;
    }

    private boolean m1150d() {
        return this.f1594h == Integer.MIN_VALUE;
    }

    private ViewHolder mo910a(View view) {
        if (view == null) {
            return null;
        }
        return ((LayoutParams) view.getLayoutParams()).f1396a;
    }

    private void mo947j(int i) {
        this.mBlocksView.m1000a(this.f1590d, false);
        this.mBlocksView.m1000a(i, true);
    }

    private void m1138a(View view, boolean z) {
        int viewPosition = getViewPosition(view);
        changeForward(viewPosition);
        boolean isFocusable = isFocusable(viewPosition);
        if (this.f1590d != viewPosition && isFocusable) {
            mo947j(viewPosition);
            this.f1590d = viewPosition;
        }
        if (!view.hasFocus() && this.mBlocksView.hasFocus() && isFocusable) {
            view.requestFocus();
        }
        if (view.getParent() != this.mBlocksView) {
            view = getFocusView();
        }
        if (this.f1585a || view != this.mScrollingView) {
            this.mScrollingView = view;
            this.mBlocksView.m931a(this.f1590d);
            if (m1142a(this.mScrollingView, f1581a)) {
                m1135a(f1581a[0], f1581a[1], z);
            }
        }
    }

    private void m1135a(int i, int i2, boolean z) {
        if (i != 0 || i2 != 0) {
            if (this.f1585a) {
                if (i == 0) {
                    i = i2;
                }
                scrollBy(i, 0);
                return;
            }
            smoothScrollBy(i, i2);
        }
    }

    private boolean m1153e() {
        return this.mFocusPlace == FocusPlace.FOCUS_CENTER || (this.mFocusPlace == FocusPlace.FOCUS_CUSTOM && this.mScrollCenterLow == this.mScrollCenterHigh);
    }

    private boolean m1142a(View view, int[] iArr) {
        if (view == null) {
            return false;
        }
        int e = m1151e(view);
        int f = m1154f(view);
        if (!this.f1585a && !m1152e() && ((!this.f1589c || f <= e) && (this.f1589c || f >= e))) {
            return false;
        }
        e = m1132a(view, isAtMin(view), isAtMax(view)) - this.f1591e;
        if (this.mOrientation == Orientation.HORIZONTAL) {
            iArr[0] = e;
            iArr[1] = 0;
        } else {
            iArr[0] = 0;
            iArr[1] = e;
        }
        return true;
    }

    private int m1132a(View view, boolean z, boolean z2) {
        int d = m1148d();
        int e = m1151e(view);
        int i = d - e;
        int f = m1154f(view) + this.f1591e;
        if (!m1148d() && !m1147c() && this.f1592f - this.f1594h <= d) {
            return this.f1594h;
        }
        if (!m1148d() && (z || f - this.f1594h <= e)) {
            return this.f1594h;
        }
        if (m1147c() || (!z2 && this.f1592f - f > i)) {
            return f - e;
        }
        return this.f1592f - d;
    }

    private int m1151e(View view) {
        if (this.mFocusPlace == FocusPlace.FOCUS_CENTER) {
            if (getOrientation() == Orientation.HORIZONTAL) {
                return getWidth() / 2;
            }
            return getHeight() / 2;
        } else if (this.mFocusPlace == FocusPlace.FOCUS_EDGE) {
            return getOrientation() == Orientation.HORIZONTAL ? this.f1589c ? (getWidth() - (view.getWidth() / 2)) - getPaddingMax() : (view.getWidth() / 2) + getPaddingMin() : this.f1589c ? (getHeight() - (view.getHeight() / 2)) - getPaddingMax() : (view.getHeight() / 2) + getPaddingMin();
        } else {
            if ((this.mScrollCenterLow <= 0 || this.mScrollCenterLow >= getHeight()) && (this.mScrollCenterHigh <= 0 || this.mScrollCenterHigh >= getHeight())) {
                return 0;
            }
            return this.f1589c ? this.mScrollCenterHigh : this.mScrollCenterLow;
        }
    }

    private int m1154f(View view) {
        return getOrientation() == Orientation.HORIZONTAL ? m1157g(view) : m1160h(view);
    }

    private int m1157g(View view) {
        return (view.getLeft() - this.mBlocksView.getScrollX()) + (view.getWidth() / 2);
    }

    private int m1160h(View view) {
        return (view.getTop() - this.mBlocksView.getScrollY()) + (view.getHeight() / 2);
    }

    private int m1148d() {
        return getOrientation() == Orientation.HORIZONTAL ? getWidth() : getHeight();
    }

    public int getFocusPosition() {
        return this.f1590d;
    }

    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        if (gainFocus && this.mBlocksView.getDescendantFocusability() == SendFlag.FLAG_KEY_PINGBACK_MID) {
            int i = this.f1590d;
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
        return this.f1582a;
    }

    public int getLastAttachedPosition() {
        return this.f1586b;
    }

    private void m1136a(int i, boolean z) {
        int width = getWidth();
        int height = getHeight();
        int a = this.f1584a.m1124a(i, z);
        if (getOrientation() == Orientation.HORIZONTAL) {
            this.f1596j = (((height - ((a - 1) * this.f1598l)) - getPaddingTop()) - getPaddingBottom()) / a;
        } else {
            this.f1596j = (((width - ((a - 1) * this.f1597k)) - getPaddingLeft()) - getPaddingRight()) / a;
        }
    }

    public void setVerticalMargin(int margin) {
        this.f1598l = margin;
    }

    public void setHorizontalMargin(int margin) {
        this.f1597k = margin;
    }

    public void onUpdateChildren() {
        for (int firstAttachedPosition = getFirstAttachedPosition(); firstAttachedPosition <= getLastAttachedPosition(); firstAttachedPosition++) {
            ViewHolder a = mo910a(getViewByPosition(firstAttachedPosition));
            if (a != null) {
                updateItem(a, a.getLayoutPosition());
            }
        }
    }

    public void setFocusPosition(int focusPosition) {
        this.f1590d = focusPosition;
    }

    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        View viewByPosition = getViewByPosition(this.f1590d);
        if (viewByPosition != null && viewByPosition.isFocusable() && isFocusable(this.f1590d)) {
            return viewByPosition.requestFocus(direction, previouslyFocusedRect);
        }
        return true;
    }

    public boolean resumeChildFocus(View view) {
        this.f1587b = false;
        if (!view.isFocused()) {
            view.requestFocus();
        }
        if (f1581a[0] == 0 && f1581a[1] == 0) {
            return false;
        }
        return true;
    }

    public boolean isCanScroll(boolean isForward) {
        if (this.f1582a < 0 || this.f1586b < 0) {
            return false;
        }
        if (!m1148d() && !m1147c() && this.f1592f - this.f1594h <= m1148d()) {
            return false;
        }
        if (isForward) {
            if (m1147c() || this.f1591e < this.f1593g) {
                return true;
            }
            return false;
        } else if (m1148d() || this.f1591e > this.f1595i) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCanScroll() {
        if (this.f1582a == -1 || this.f1586b == -1) {
            return false;
        }
        if (m1148d() || m1147c() || this.f1592f - this.f1594h > m1148d()) {
            return true;
        }
        return false;
    }

    public boolean dispatchKeyEvent(KeyEvent event, int direction) {
        this.f1599m = getMovement(direction);
        if (!(event.getAction() == 0)) {
            this.mBlocksView.m960b();
            return false;
        } else if (event.getRepeatCount() <= 0) {
            this.mBlocksView.m1016d();
            return false;
        } else if (event.getRepeatCount() == 1) {
            return true;
        } else {
            if (this.mScrollMode == 1) {
                return this.mBlocksView.m935a();
            }
            this.mBlocksView.m979e();
            return false;
        }
    }

    public int getVerticalMargin() {
        return this.f1598l;
    }

    public int getHorizontalMargin() {
        return this.f1597k;
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
            this.f1590d = -1;
        }
    }

    public int m1189f(int i) {
        if (this.mBlocksView.f1438a == null) {
            return this.f1600n;
        }
        return this.mBlocksView.f1438a.getItemOffsets(i, this.mBlocksView);
    }

    public View getFocusView() {
        return getViewByPosition(this.f1590d);
    }

    public void onRemoved(int position) {
        View viewByPosition = getViewByPosition(position);
        if (viewByPosition != null) {
            scrapView(viewByPosition, true);
            this.f1586b--;
        }
    }

    public boolean isNeedRequestFocus() {
        return this.f1587b;
    }

    public void onItemsRemoved(int positionStart, int itemCount) {
        if (this.f1590d != -1) {
            int i = this.f1590d;
            if (positionStart <= i && positionStart + itemCount < i) {
                this.f1590d -= itemCount;
            }
        }
    }

    public void onItemsAdded(int positionStart, int itemCount) {
        if (this.f1590d != -1 && positionStart < this.f1590d) {
            this.f1590d += itemCount;
        }
    }

    public void setExtraPadding(int extraPadding) {
        this.f1588c = extraPadding;
    }

    public int getMovement() {
        return this.f1599m;
    }

    public void onFocusLost(ViewHolder holder) {
        this.f1599m = 16;
    }

    public int mo948k(int i) {
        return this.mBlocksView.getColumn(i);
    }

    public boolean hasScrollOffset() {
        return this.f1595i == Integer.MIN_VALUE || this.f1594h == Integer.MIN_VALUE;
    }

    public int getMinScroll() {
        return this.f1595i;
    }

    public int mo908a() {
        return this.f1588c;
    }

    public int m1173b() {
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

    public int m1179c() {
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
        return this.f1584a.m1125a(position);
    }

    public View findFocus() {
        return this.f1583a;
    }

    public void setLayouts(List<BlockLayout> layouts) {
        this.f1584a.m1127a((List) layouts);
    }

    public void onScrollStop() {
        this.mScrollingView = null;
    }

    public boolean isOnTop() {
        return this.f1595i != Integer.MIN_VALUE && this.f1595i == this.f1591e;
    }
}

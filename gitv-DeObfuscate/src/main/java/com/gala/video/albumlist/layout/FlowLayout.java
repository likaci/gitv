package com.gala.video.albumlist.layout;

import android.view.View;
import com.gala.video.albumlist.widget.C0423d.C0421a;

public class FlowLayout extends BlockLayout {
    private Integer[] f1378a = new Integer[1];

    public boolean appendAttachedItems(int start, int toLimit, boolean oneColumnMode) {
        if (m851a(toLimit) || start < 0) {
            return false;
        }
        int count = this.a.getCount();
        C0421a a = m851a(start);
        boolean z = false;
        while (a.f1577b < count && !isOutRang(a.f1577b)) {
            int a2 = this.a.mo911a(a, true, this.a);
            int a3 = m849a(a, z, toLimit, this.f1378a);
            if (a3 == Integer.MIN_VALUE) {
                this.a.scrapView((View) this.a[0], true);
                return z;
            }
            Object obj = this.a[0];
            int i = a.f1577b;
            a.f1577b = i + 1;
            m847c(obj, i, a2, this.f1378a[0].intValue(), a3);
            z = true;
        }
        return z;
    }

    public boolean prependAttachedItems(int start, boolean oneColumnMode) {
        if (m854b(0) || start < 0) {
            return false;
        }
        C0421a a = m851a(start);
        boolean z = false;
        while (a.f1577b >= 0) {
            if (isOutRang(a.f1577b)) {
                m851a(a.f1577b + 1);
                return z;
            }
            int a2 = this.a.mo911a(a, true, this.a);
            int a3 = m850a(a, z, this.f1378a);
            if (a3 == Integer.MIN_VALUE) {
                this.a.scrapView((View) this.a[0], true);
                return z;
            }
            Object obj = this.a[0];
            int i = a.f1577b;
            a.f1577b = i - 1;
            m848d(obj, i, a2, this.f1378a[0].intValue(), a3 - a2);
            z = true;
        }
        m851a(0);
        return z;
    }

    protected boolean m853a(int i) {
        return this.a.mo922e(getMaxViewPosition()) >= this.a.mo908a() + i;
    }

    protected boolean m854b(int i) {
        return this.a.mo919d(getMinViewPosition()) <= i - this.a.mo908a();
    }

    private C0421a m851a(int i) {
        C0421a c0421a = this.a;
        c0421a.f1577b = i;
        c0421a.f1576a = 0;
        return c0421a;
    }

    private int m849a(C0421a c0421a, boolean z, int i, Integer[] numArr) {
        numArr[0] = Integer.valueOf(this.a.getPaddingStart());
        int paddingMin = this.a.getPaddingMin();
        if (this.a.getLastAttachedPosition() < 0) {
            return paddingMin;
        }
        if (getMinViewPosition() == -1) {
            return getViewMax(getMaxViewPosition()) + this.a.mo923f(getMaxViewPosition());
        }
        View viewByPosition = this.a.getViewByPosition(c0421a.f1577b - 1);
        if (viewByPosition == null) {
            return paddingMin;
        }
        if ((((Integer) this.a[1]).intValue() + this.a.mo920d(viewByPosition)) + getHorizontalMargin() <= this.a.getWidth() - this.a.getPaddingEnd()) {
            paddingMin = this.a.mo910a(viewByPosition);
            numArr[0] = Integer.valueOf(this.a.mo920d(viewByPosition) + getHorizontalMargin());
            return paddingMin;
        } else if (z && m851a(i)) {
            return Integer.MIN_VALUE;
        } else {
            return getViewMax(getMaxViewPosition()) + this.a.mo923f(getMaxViewPosition());
        }
    }

    private int m850a(C0421a c0421a, boolean z, Integer[] numArr) {
        this.f1378a[0] = Integer.valueOf(0);
        if (this.a.getFirstAttachedPosition() >= 0) {
            if (getMaxViewPosition() == -1) {
                return getViewMin(getMinViewPosition()) - this.a.mo923f(c0421a.f1577b);
            }
            View viewByPosition = this.a.getViewByPosition(c0421a.f1577b + 1);
            if (viewByPosition != null) {
                int b;
                if ((viewByPosition.getLeft() - ((Integer) this.a[1]).intValue()) - getHorizontalMargin() >= this.a.getPaddingStart()) {
                    b = this.a.mo914b(viewByPosition);
                    this.f1378a[0] = Integer.valueOf((this.a.mo917c(viewByPosition) - getHorizontalMargin()) - ((Integer) this.a[1]).intValue());
                    return b;
                } else if (z) {
                    m851a(c0421a.f1577b + 1);
                    if (m854b(0)) {
                        return Integer.MIN_VALUE;
                    }
                } else {
                    b = getViewMin(getMinViewPosition()) - this.a.mo923f(c0421a.f1577b);
                    this.f1378a[0] = Integer.valueOf(((this.a.getWidth() - getHorizontalMargin()) - ((Integer) this.a[1]).intValue()) - this.a.getPaddingEnd());
                    return b;
                }
            }
        }
        return 0;
    }

    private void m852a(int i) {
        int a = this.a.mo909a(getMinViewPosition());
        View viewByPosition = this.a.getViewByPosition(i);
        int b = this.a.mo914b(viewByPosition);
        int c = this.a.mo917c(viewByPosition) - this.a.getPaddingStart();
        while (i <= this.a.getLastAttachedPosition()) {
            View viewByPosition2 = this.a.getViewByPosition(i);
            if (this.a.mo913b(i) == b) {
                if (c != 0 || this.a.mo910a(viewByPosition2) != a) {
                    int left = viewByPosition2.getLeft() - c;
                    int top = (viewByPosition2.getTop() + a) - this.a.mo910a(viewByPosition2);
                    viewByPosition2.layout(left, top, viewByPosition2.getWidth() + left, viewByPosition2.getHeight() + top);
                }
                i++;
            } else {
                return;
            }
        }
    }
}

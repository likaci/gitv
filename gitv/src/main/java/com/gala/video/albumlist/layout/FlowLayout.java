package com.gala.video.albumlist.layout;

import android.view.View;
import com.gala.video.albumlist.widget.d.a;

public class FlowLayout extends BlockLayout {
    private Integer[] a = new Integer[1];

    public boolean appendAttachedItems(int start, int toLimit, boolean oneColumnMode) {
        if (a(toLimit) || start < 0) {
            return false;
        }
        int count = this.a.getCount();
        a a = a(start);
        boolean z = false;
        while (a.b < count && !isOutRang(a.b)) {
            int a2 = this.a.a(a, true, this.a);
            int a3 = a(a, z, toLimit, this.a);
            if (a3 == Integer.MIN_VALUE) {
                this.a.scrapView((View) this.a[0], true);
                return z;
            }
            Object obj = this.a[0];
            int i = a.b;
            a.b = i + 1;
            c(obj, i, a2, this.a[0].intValue(), a3);
            z = true;
        }
        return z;
    }

    public boolean prependAttachedItems(int start, boolean oneColumnMode) {
        if (b(0) || start < 0) {
            return false;
        }
        a a = a(start);
        boolean z = false;
        while (a.b >= 0) {
            if (isOutRang(a.b)) {
                a(a.b + 1);
                return z;
            }
            int a2 = this.a.a(a, true, this.a);
            int a3 = a(a, z, this.a);
            if (a3 == Integer.MIN_VALUE) {
                this.a.scrapView((View) this.a[0], true);
                return z;
            }
            Object obj = this.a[0];
            int i = a.b;
            a.b = i - 1;
            d(obj, i, a2, this.a[0].intValue(), a3 - a2);
            z = true;
        }
        a(0);
        return z;
    }

    protected boolean m102a(int i) {
        return this.a.e(getMaxViewPosition()) >= this.a.a() + i;
    }

    protected boolean b(int i) {
        return this.a.d(getMinViewPosition()) <= i - this.a.a();
    }

    private a a(int i) {
        a aVar = this.a;
        aVar.b = i;
        aVar.a = 0;
        return aVar;
    }

    private int a(a aVar, boolean z, int i, Integer[] numArr) {
        numArr[0] = Integer.valueOf(this.a.getPaddingStart());
        int paddingMin = this.a.getPaddingMin();
        if (this.a.getLastAttachedPosition() < 0) {
            return paddingMin;
        }
        if (getMinViewPosition() == -1) {
            return getViewMax(getMaxViewPosition()) + this.a.f(getMaxViewPosition());
        }
        View viewByPosition = this.a.getViewByPosition(aVar.b - 1);
        if (viewByPosition == null) {
            return paddingMin;
        }
        if ((((Integer) this.a[1]).intValue() + this.a.d(viewByPosition)) + getHorizontalMargin() <= this.a.getWidth() - this.a.getPaddingEnd()) {
            paddingMin = this.a.a(viewByPosition);
            numArr[0] = Integer.valueOf(this.a.d(viewByPosition) + getHorizontalMargin());
            return paddingMin;
        } else if (z && a(i)) {
            return Integer.MIN_VALUE;
        } else {
            return getViewMax(getMaxViewPosition()) + this.a.f(getMaxViewPosition());
        }
    }

    private int a(a aVar, boolean z, Integer[] numArr) {
        this.a[0] = Integer.valueOf(0);
        if (this.a.getFirstAttachedPosition() >= 0) {
            if (getMaxViewPosition() == -1) {
                return getViewMin(getMinViewPosition()) - this.a.f(aVar.b);
            }
            View viewByPosition = this.a.getViewByPosition(aVar.b + 1);
            if (viewByPosition != null) {
                int b;
                if ((viewByPosition.getLeft() - ((Integer) this.a[1]).intValue()) - getHorizontalMargin() >= this.a.getPaddingStart()) {
                    b = this.a.b(viewByPosition);
                    this.a[0] = Integer.valueOf((this.a.c(viewByPosition) - getHorizontalMargin()) - ((Integer) this.a[1]).intValue());
                    return b;
                } else if (z) {
                    a(aVar.b + 1);
                    if (b(0)) {
                        return Integer.MIN_VALUE;
                    }
                } else {
                    b = getViewMin(getMinViewPosition()) - this.a.f(aVar.b);
                    this.a[0] = Integer.valueOf(((this.a.getWidth() - getHorizontalMargin()) - ((Integer) this.a[1]).intValue()) - this.a.getPaddingEnd());
                    return b;
                }
            }
        }
        return 0;
    }

    private void m101a(int i) {
        int a = this.a.a(getMinViewPosition());
        View viewByPosition = this.a.getViewByPosition(i);
        int b = this.a.b(viewByPosition);
        int c = this.a.c(viewByPosition) - this.a.getPaddingStart();
        while (i <= this.a.getLastAttachedPosition()) {
            View viewByPosition2 = this.a.getViewByPosition(i);
            if (this.a.b(i) == b) {
                if (c != 0 || this.a.a(viewByPosition2) != a) {
                    int left = viewByPosition2.getLeft() - c;
                    int top = (viewByPosition2.getTop() + a) - this.a.a(viewByPosition2);
                    viewByPosition2.layout(left, top, viewByPosition2.getWidth() + left, viewByPosition2.getHeight() + top);
                }
                i++;
            } else {
                return;
            }
        }
    }
}

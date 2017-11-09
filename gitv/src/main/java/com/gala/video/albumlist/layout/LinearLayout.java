package com.gala.video.albumlist.layout;

import com.gala.video.albumlist.widget.d.a;

public class LinearLayout extends BlockLayout {
    public boolean appendAttachedItems(int start, int toLimit, boolean oneColumnMode) {
        if (b(toLimit) || start < 0) {
            return false;
        }
        int count = this.a.getCount();
        a aVar = new a();
        aVar.b = start;
        boolean z = false;
        while (!isOutRang(aVar.b) && aVar.b < count && !b(toLimit)) {
            int a = a(aVar);
            int a2 = this.a.a(aVar, true, this.a);
            Object obj = this.a[0];
            int i = aVar.b;
            aVar.b = i + 1;
            a(obj, i, a2, aVar.a, a);
            if (oneColumnMode) {
                return true;
            }
            z = true;
        }
        return z;
    }

    public boolean prependAttachedItems(int start, boolean oneColumnMode) {
        if (a(0) || start < 0) {
            return false;
        }
        a aVar = new a();
        aVar.b = start;
        boolean z = false;
        while (aVar.b >= 0 && !a(0) && !isOutRang(aVar.b)) {
            int f;
            int a = this.a.a(aVar, true, this.a);
            if (aVar.b == getLastPosition()) {
                f = (getLayoutRegion().bottom - a) - this.a.f(aVar.b);
            } else {
                f = (getViewMin(aVar.b + 1) - a) - this.a.f(aVar.b);
            }
            Object obj = this.a[0];
            int i = aVar.b;
            aVar.b = i - 1;
            b(obj, i, a, aVar.a, f);
            if (oneColumnMode) {
                return true;
            }
            z = true;
        }
        return z;
    }

    private int a(a aVar) {
        if (this.a.getLastAttachedPosition() == -1 || this.a.getFirstAttachedPosition() == -1) {
            return aVar.b == 0 ? this.a.getPaddingMin() : 0;
        } else {
            if (aVar.b == getFirstPosition()) {
                return getLayoutRegion().top + getPaddingTop();
            }
            int i = aVar.b - 1;
            return this.a.f(i) + getViewMax(i);
        }
    }

    protected final boolean a(int i) {
        if (this.a.getLastAttachedPosition() >= 0 && this.a.d(this.a.getFirstAttachedPosition()) <= i - this.a.a()) {
            return true;
        }
        return false;
    }

    protected boolean b(int i) {
        if (this.a.getLastAttachedPosition() >= 0 && this.a.e(this.a.getLastAttachedPosition()) >= this.a.a() + i) {
            return true;
        }
        return false;
    }
}

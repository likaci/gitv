package com.gala.video.albumlist.layout;

import com.gala.video.albumlist.widget.C0423d.C0421a;

public class LinearLayout extends BlockLayout {
    public boolean appendAttachedItems(int start, int toLimit, boolean oneColumnMode) {
        if (m866b(toLimit) || start < 0) {
            return false;
        }
        int count = this.a.getCount();
        C0421a c0421a = new C0421a();
        c0421a.f1577b = start;
        boolean z = false;
        while (!isOutRang(c0421a.f1577b) && c0421a.f1577b < count && !m866b(toLimit)) {
            int a = m864a(c0421a);
            int a2 = this.a.mo911a(c0421a, true, this.a);
            Object obj = this.a[0];
            int i = c0421a.f1577b;
            c0421a.f1577b = i + 1;
            m843a(obj, i, a2, c0421a.f1576a, a);
            if (oneColumnMode) {
                return true;
            }
            z = true;
        }
        return z;
    }

    public boolean prependAttachedItems(int start, boolean oneColumnMode) {
        if (m865a(0) || start < 0) {
            return false;
        }
        C0421a c0421a = new C0421a();
        c0421a.f1577b = start;
        boolean z = false;
        while (c0421a.f1577b >= 0 && !m865a(0) && !isOutRang(c0421a.f1577b)) {
            int f;
            int a = this.a.mo911a(c0421a, true, this.a);
            if (c0421a.f1577b == getLastPosition()) {
                f = (getLayoutRegion().bottom - a) - this.a.mo923f(c0421a.f1577b);
            } else {
                f = (getViewMin(c0421a.f1577b + 1) - a) - this.a.mo923f(c0421a.f1577b);
            }
            Object obj = this.a[0];
            int i = c0421a.f1577b;
            c0421a.f1577b = i - 1;
            m845b(obj, i, a, c0421a.f1576a, f);
            if (oneColumnMode) {
                return true;
            }
            z = true;
        }
        return z;
    }

    private int m864a(C0421a c0421a) {
        if (this.a.getLastAttachedPosition() == -1 || this.a.getFirstAttachedPosition() == -1) {
            return c0421a.f1577b == 0 ? this.a.getPaddingMin() : 0;
        } else {
            if (c0421a.f1577b == getFirstPosition()) {
                return getLayoutRegion().top + getPaddingTop();
            }
            int i = c0421a.f1577b - 1;
            return this.a.mo923f(i) + getViewMax(i);
        }
    }

    protected final boolean m865a(int i) {
        if (this.a.getLastAttachedPosition() >= 0 && this.a.mo919d(this.a.getFirstAttachedPosition()) <= i - this.a.mo908a()) {
            return true;
        }
        return false;
    }

    protected boolean m866b(int i) {
        if (this.a.getLastAttachedPosition() >= 0 && this.a.mo922e(this.a.getLastAttachedPosition()) >= this.a.mo908a() + i) {
            return true;
        }
        return false;
    }
}

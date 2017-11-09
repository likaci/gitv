package com.gala.video.albumlist4.widget;

import com.gala.video.albumlist4.widget.C0468d.C0466a;
import com.gala.video.albumlist4.widget.C0468d.C0467b;

class C0470f extends C0468d {
    private Object[] f1827a = new Object[1];

    public C0470f(C0467b c0467b) {
        super(c0467b);
    }

    protected boolean mo1042a(boolean z) {
        if (mo1040a(0)) {
            return false;
        }
        C0466a c0466a = new C0466a();
        c0466a.f1803c = m1449e();
        c0466a.f1801a = m1447d();
        boolean z2 = false;
        do {
            int f = this.a.mo1005f(c0466a.f1803c);
            c0466a.f1802b = f - 1;
            while (c0466a.f1802b >= 0) {
                if (c0466a.f1803c < 0 || mo1040a(0)) {
                    return z2;
                }
                int i;
                int a = this.a.mo997a(c0466a, false, this.f1827a);
                int i2;
                if (this.a < 0 || this.b < 0) {
                    i2 = c0466a.f1803c;
                    this.a = i2;
                    this.b = i2;
                    i = 0;
                } else {
                    i2 = (c0466a.f1803c + f) - c0466a.f1802b;
                    if (i2 > this.b) {
                        i2 = this.b;
                    }
                    i = (this.a.mo996a(i2) - a) - this.a.mo1004e(c0466a.f1803c);
                    this.a = c0466a.f1803c;
                }
                C0467b c0467b = this.a;
                Object obj = this.f1827a[0];
                int i3 = c0466a.f1803c;
                c0466a.f1803c = i3 - 1;
                c0467b.mo998a(obj, i3, a, c0466a.f1802b, i);
                z2 = true;
                c0466a.f1802b--;
            }
            c0466a.f1801a--;
        } while (!z);
        return z2;
    }

    protected boolean mo1041a(int i, boolean z) {
        if (m1433b(i)) {
            return false;
        }
        int count = this.a.getCount();
        C0466a c0466a = new C0466a();
        c0466a.f1803c = m1435a();
        c0466a.f1802b = m1442b();
        c0466a.f1801a = m1445c();
        boolean z2 = false;
        do {
            int f = this.a.mo1005f(c0466a.f1803c);
            while (c0466a.f1802b < f) {
                if (c0466a.f1803c >= count || m1433b(i)) {
                    return z2;
                }
                int i2;
                int a = this.a.mo997a(c0466a, true, this.f1827a);
                int paddingLow = this.a.getPaddingLow();
                if (this.a < 0 || this.b < 0) {
                    int i3 = c0466a.f1803c;
                    this.a = i3;
                    this.b = i3;
                    i2 = paddingLow;
                } else {
                    if (c0466a.f1803c > c0466a.f1802b && (c0466a.f1803c - c0466a.f1802b) - 1 >= this.a) {
                        paddingLow = (c0466a.f1803c - c0466a.f1802b) - 1;
                        paddingLow = this.a.mo1004e(paddingLow) + this.a.mo999b(paddingLow);
                    }
                    this.b = c0466a.f1803c;
                    i2 = paddingLow;
                }
                C0467b c0467b = this.a;
                Object obj = this.f1827a[0];
                int i4 = c0466a.f1803c;
                c0466a.f1803c = i4 + 1;
                c0467b.mo998a(obj, i4, a, c0466a.f1802b, i2);
                if (f != this.a.mo1005f(c0466a.f1803c)) {
                    c0466a.f1802b = 0;
                    z2 = true;
                    break;
                }
                c0466a.f1802b++;
                z2 = true;
            }
            c0466a.f1801a++;
            if (c0466a.f1802b == f) {
                c0466a.f1802b = 0;
                continue;
            }
        } while (!z);
        return z2;
    }

    public int mo1040a(int i) {
        return i - this.a.mo1018h(i);
    }
}

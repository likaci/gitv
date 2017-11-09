package com.gala.video.albumlist4.widget;

import com.gala.video.albumlist4.widget.d.a;
import com.gala.video.albumlist4.widget.d.b;

class f extends d {
    private Object[] a = new Object[1];

    public f(b bVar) {
        super(bVar);
    }

    protected boolean a(boolean z) {
        if (a(0)) {
            return false;
        }
        a aVar = new a();
        aVar.c = e();
        aVar.a = d();
        boolean z2 = false;
        do {
            int f = this.a.f(aVar.c);
            aVar.b = f - 1;
            while (aVar.b >= 0) {
                if (aVar.c < 0 || a(0)) {
                    return z2;
                }
                int i;
                int a = this.a.a(aVar, false, this.a);
                int i2;
                if (this.a < 0 || this.b < 0) {
                    i2 = aVar.c;
                    this.a = i2;
                    this.b = i2;
                    i = 0;
                } else {
                    i2 = (aVar.c + f) - aVar.b;
                    if (i2 > this.b) {
                        i2 = this.b;
                    }
                    i = (this.a.a(i2) - a) - this.a.e(aVar.c);
                    this.a = aVar.c;
                }
                b bVar = this.a;
                Object obj = this.a[0];
                int i3 = aVar.c;
                aVar.c = i3 - 1;
                bVar.a(obj, i3, a, aVar.b, i);
                z2 = true;
                aVar.b--;
            }
            aVar.a--;
        } while (!z);
        return z2;
    }

    protected boolean a(int i, boolean z) {
        if (b(i)) {
            return false;
        }
        int count = this.a.getCount();
        a aVar = new a();
        aVar.c = a();
        aVar.b = b();
        aVar.a = c();
        boolean z2 = false;
        do {
            int f = this.a.f(aVar.c);
            while (aVar.b < f) {
                if (aVar.c >= count || b(i)) {
                    return z2;
                }
                int i2;
                int a = this.a.a(aVar, true, this.a);
                int paddingLow = this.a.getPaddingLow();
                if (this.a < 0 || this.b < 0) {
                    int i3 = aVar.c;
                    this.a = i3;
                    this.b = i3;
                    i2 = paddingLow;
                } else {
                    if (aVar.c > aVar.b && (aVar.c - aVar.b) - 1 >= this.a) {
                        paddingLow = (aVar.c - aVar.b) - 1;
                        paddingLow = this.a.e(paddingLow) + this.a.b(paddingLow);
                    }
                    this.b = aVar.c;
                    i2 = paddingLow;
                }
                b bVar = this.a;
                Object obj = this.a[0];
                int i4 = aVar.c;
                aVar.c = i4 + 1;
                bVar.a(obj, i4, a, aVar.b, i2);
                if (f != this.a.f(aVar.c)) {
                    aVar.b = 0;
                    z2 = true;
                    break;
                }
                aVar.b++;
                z2 = true;
            }
            aVar.a++;
            if (aVar.b == f) {
                aVar.b = 0;
                continue;
            }
        } while (!z);
        return z2;
    }

    public int a(int i) {
        return i - this.a.h(i);
    }
}

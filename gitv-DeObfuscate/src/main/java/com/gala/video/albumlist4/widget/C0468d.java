package com.gala.video.albumlist4.widget;

abstract class C0468d {
    protected int f1804a = -1;
    protected C0467b f1805a;
    protected int f1806b = -1;
    protected int f1807c = 100;
    protected int f1808d;

    public static class C0466a implements Cloneable {
        public int f1801a;
        public int f1802b;
        public int f1803c;

        public /* synthetic */ Object clone() throws CloneNotSupportedException {
            return m1420a();
        }

        public C0466a m1420a() {
            try {
                return (C0466a) super.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }

    public interface C0467b {
        int mo996a(int i);

        int mo997a(C0466a c0466a, boolean z, Object[] objArr);

        void m1423a(int i);

        void mo998a(Object obj, int i, int i2, int i3, int i4);

        int mo999b(int i);

        void m1426b(int i);

        int mo1000c(int i);

        int mo1002d(int i);

        int mo1004e(int i);

        int mo1005f(int i);

        int mo1007g(int i);

        int getCount();

        int getFocusPosition();

        int getPaddingLow();

        int mo1018h(int i);
    }

    public abstract int mo1040a(int i);

    protected abstract boolean mo1041a(int i, boolean z);

    protected abstract boolean mo1042a(boolean z);

    public C0468d(C0467b c0467b) {
        this.f1805a = c0467b;
    }

    void m1438a(int i) {
        if (this.f1808d != i) {
            this.f1808d = i;
        }
    }

    protected final boolean m1439a(int i) {
        if (this.f1806b >= 0 && m1434c(this.f1804a) <= i - this.f1807c) {
            return true;
        }
        return false;
    }

    protected boolean m1444b(int i) {
        if (this.f1806b >= 0 && m1433b(this.f1806b) >= this.f1807c + i) {
            return true;
        }
        return false;
    }

    protected void m1443b(int i) {
        while (this.f1806b >= this.f1804a) {
            if ((this.f1805a.mo1002d(this.f1804a) <= i - this.f1807c ? 1 : null) != null) {
                this.f1805a.mo996a(this.f1804a);
                this.f1804a++;
            } else {
                return;
            }
        }
    }

    protected void m1446c(int i) {
        for (int i2 = this.f1804a; i2 <= this.f1806b; i2++) {
            if ((this.f1805a.mo1002d(i2) <= i ? 1 : null) != null) {
                this.f1805a.mo999b(i2);
            }
        }
    }

    protected void m1448d(int i) {
        while (this.f1806b >= this.f1804a) {
            if ((this.f1805a.mo1000c(this.f1806b) >= this.f1807c + i ? 1 : null) != null) {
                this.f1805a.mo996a(this.f1806b);
                this.f1806b--;
            } else {
                return;
            }
        }
    }

    protected void m1450e(int i) {
        for (int i2 = this.f1804a; i2 <= this.f1806b; i2++) {
            if ((this.f1805a.mo1000c(i2) >= i ? 1 : null) != null) {
                this.f1805a.mo999b(i2);
            }
        }
    }

    private int m1433b(int i) {
        int f = (i - this.f1805a.mo1005f(i)) + 1;
        if (this.f1804a > f) {
            f = this.f1804a;
        }
        return this.f1805a.mo1002d(f);
    }

    private int m1434c(int i) {
        int f = (this.f1805a.mo1005f(i) + i) - 1;
        if (f > this.f1806b) {
            f = this.f1806b;
        }
        return this.f1805a.mo1000c(f);
    }

    public void m1437a() {
        this.f1806b = -1;
        this.f1804a = -1;
    }

    protected int m1435a() {
        if (this.f1806b >= 0) {
            return this.f1806b + 1;
        }
        if (this.f1805a.getFocusPosition() >= 0) {
            return mo1040a(this.f1805a.getFocusPosition());
        }
        return 0;
    }

    protected int m1442b() {
        if (this.f1806b < 0) {
            return 0;
        }
        int h = this.f1805a.mo1018h(this.f1806b);
        if (h == this.f1805a.mo1005f(this.f1806b) - 1 || this.f1805a.mo1005f(this.f1806b) != this.f1805a.mo1005f(this.f1806b + 1)) {
            return 0;
        }
        return h + 1;
    }

    protected int m1445c() {
        if (this.f1806b >= 0) {
            int g = this.f1805a.mo1007g(this.f1806b);
            if (this.f1805a.mo1018h(this.f1806b) == this.f1805a.mo1005f(this.f1806b) - 1 || this.f1805a.mo1005f(this.f1806b) != this.f1805a.mo1005f(this.f1806b + 1)) {
                return g + 1;
            }
            return g;
        } else if (this.f1805a.getFocusPosition() >= 0) {
            return this.f1805a.mo1007g(this.f1805a.getFocusPosition());
        } else {
            return 0;
        }
    }

    protected int m1447d() {
        if (this.f1804a >= 0) {
            return this.f1805a.mo1007g(this.f1804a) - 1;
        }
        return this.f1805a.mo1007g(this.f1805a.getCount() - 1);
    }

    protected int m1449e() {
        if (this.f1804a >= 0) {
            return this.f1804a - 1;
        }
        return this.f1805a.getCount() - 1;
    }

    public int m1451f() {
        return this.f1804a;
    }

    public int m1453g() {
        return this.f1806b;
    }

    public void m1452f(int i) {
        this.f1807c = i;
    }
}

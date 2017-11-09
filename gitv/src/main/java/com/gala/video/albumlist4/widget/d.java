package com.gala.video.albumlist4.widget;

abstract class d {
    protected int a = -1;
    protected b f734a;
    protected int b = -1;
    protected int c = 100;
    protected int d;

    public static class a implements Cloneable {
        public int a;
        public int b;
        public int c;

        public /* synthetic */ Object clone() throws CloneNotSupportedException {
            return a();
        }

        public a a() {
            try {
                return (a) super.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }

    public interface b {
        int a(int i);

        int a(a aVar, boolean z, Object[] objArr);

        void m187a(int i);

        void a(Object obj, int i, int i2, int i3, int i4);

        int b(int i);

        void m188b(int i);

        int c(int i);

        int d(int i);

        int e(int i);

        int f(int i);

        int g(int i);

        int getCount();

        int getFocusPosition();

        int getPaddingLow();

        int h(int i);
    }

    public abstract int a(int i);

    protected abstract boolean a(int i, boolean z);

    protected abstract boolean a(boolean z);

    public d(b bVar) {
        this.f734a = bVar;
    }

    void m182a(int i) {
        if (this.d != i) {
            this.d = i;
        }
    }

    protected final boolean m183a(int i) {
        if (this.b >= 0 && c(this.a) <= i - this.c) {
            return true;
        }
        return false;
    }

    protected boolean m185b(int i) {
        if (this.b >= 0 && b(this.b) >= this.c + i) {
            return true;
        }
        return false;
    }

    protected void m184b(int i) {
        while (this.b >= this.a) {
            if ((this.f734a.d(this.a) <= i - this.c ? 1 : null) != null) {
                this.f734a.a(this.a);
                this.a++;
            } else {
                return;
            }
        }
    }

    protected void m186c(int i) {
        for (int i2 = this.a; i2 <= this.b; i2++) {
            if ((this.f734a.d(i2) <= i ? 1 : null) != null) {
                this.f734a.b(i2);
            }
        }
    }

    protected void d(int i) {
        while (this.b >= this.a) {
            if ((this.f734a.c(this.b) >= this.c + i ? 1 : null) != null) {
                this.f734a.a(this.b);
                this.b--;
            } else {
                return;
            }
        }
    }

    protected void e(int i) {
        for (int i2 = this.a; i2 <= this.b; i2++) {
            if ((this.f734a.c(i2) >= i ? 1 : null) != null) {
                this.f734a.b(i2);
            }
        }
    }

    private int b(int i) {
        int f = (i - this.f734a.f(i)) + 1;
        if (this.a > f) {
            f = this.a;
        }
        return this.f734a.d(f);
    }

    private int c(int i) {
        int f = (this.f734a.f(i) + i) - 1;
        if (f > this.b) {
            f = this.b;
        }
        return this.f734a.c(f);
    }

    public void m181a() {
        this.b = -1;
        this.a = -1;
    }

    protected int a() {
        if (this.b >= 0) {
            return this.b + 1;
        }
        if (this.f734a.getFocusPosition() >= 0) {
            return a(this.f734a.getFocusPosition());
        }
        return 0;
    }

    protected int b() {
        if (this.b < 0) {
            return 0;
        }
        int h = this.f734a.h(this.b);
        if (h == this.f734a.f(this.b) - 1 || this.f734a.f(this.b) != this.f734a.f(this.b + 1)) {
            return 0;
        }
        return h + 1;
    }

    protected int c() {
        if (this.b >= 0) {
            int g = this.f734a.g(this.b);
            if (this.f734a.h(this.b) == this.f734a.f(this.b) - 1 || this.f734a.f(this.b) != this.f734a.f(this.b + 1)) {
                return g + 1;
            }
            return g;
        } else if (this.f734a.getFocusPosition() >= 0) {
            return this.f734a.g(this.f734a.getFocusPosition());
        } else {
            return 0;
        }
    }

    protected int d() {
        if (this.a >= 0) {
            return this.f734a.g(this.a) - 1;
        }
        return this.f734a.g(this.f734a.getCount() - 1);
    }

    protected int e() {
        if (this.a >= 0) {
            return this.a - 1;
        }
        return this.f734a.getCount() - 1;
    }

    public int f() {
        return this.a;
    }

    public int g() {
        return this.b;
    }

    public void f(int i) {
        this.c = i;
    }
}

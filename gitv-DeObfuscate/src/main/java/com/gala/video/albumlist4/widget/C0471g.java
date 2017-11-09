package com.gala.video.albumlist4.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.gala.video.albumlist4.utils.C0431a;

public class C0471g {
    private static int f1828a;
    private Rect f1829a;
    private Drawable f1830a;
    private int f1831b;
    private int f1832c;
    private int f1833d;
    private int f1834e;
    private int f1835f;

    public C0471g(Context context) {
        this(context, null);
    }

    public C0471g(Context context, Drawable drawable) {
        this.f1829a = new Rect();
        this.f1830a = drawable;
        f1828a = C0431a.m1212a(context, 40.0f);
    }

    public void m1510a(Canvas canvas) {
        int max = Math.max(f1828a, Math.round((((float) this.f1834e) * ((float) this.f1834e)) / ((float) this.f1832c)));
        int i = this.f1835f - this.f1829a.right;
        int minimumWidth = i - this.f1830a.getMinimumWidth();
        int i2 = ((this.f1834e - this.f1829a.top) - this.f1829a.bottom) - max;
        i2 = (Math.round((((float) i2) * ((float) this.f1833d)) / ((float) (this.f1832c - this.f1834e))) + this.f1829a.top) + this.f1831b;
        this.f1830a.setBounds(minimumWidth, i2, i, max + i2);
        this.f1830a.draw(canvas);
    }

    public void m1507a(int i) {
        if (this.f1830a != null) {
            this.f1830a.setAlpha(i);
        }
    }

    public void m1509a(int i, int i2, int i3, int i4, int i5) {
        this.f1835f = i;
        this.f1831b = i2;
        this.f1832c = i3;
        this.f1833d = i4;
        this.f1834e = i5;
    }

    public void m1511a(Drawable drawable) {
        this.f1830a = drawable;
    }

    public void m1508a(int i, int i2, int i3, int i4) {
        this.f1829a.set(i, i2, i3, i4);
    }

    public Drawable m1506a() {
        return this.f1830a;
    }

    public Rect m1505a() {
        return this.f1830a.getBounds();
    }
}

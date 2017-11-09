package com.gala.video.albumlist.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.gala.video.albumlist.utils.C0384a;

public class C0425f {
    private static int f1601a;
    private Rect f1602a;
    private Drawable f1603a;
    private int f1604b;
    private int f1605c;
    private int f1606d;
    private int f1607e;
    private int f1608f;

    public C0425f(Context context) {
        this(context, null);
    }

    public C0425f(Context context, Drawable drawable) {
        this.f1602a = new Rect();
        this.f1603a = drawable;
        f1601a = C0384a.m873a(context, 40.0f);
    }

    public void m1201a(Canvas canvas) {
        int max = Math.max(f1601a, Math.round((((float) this.f1607e) * ((float) this.f1607e)) / ((float) this.f1605c)));
        int i = this.f1608f - this.f1602a.right;
        int minimumWidth = i - this.f1603a.getMinimumWidth();
        int i2 = ((this.f1607e - this.f1602a.top) - this.f1602a.bottom) - max;
        i2 = (Math.round((((float) i2) * ((float) this.f1606d)) / ((float) (this.f1605c - this.f1607e))) + this.f1602a.top) + this.f1604b;
        this.f1603a.setBounds(minimumWidth, i2, i, max + i2);
        this.f1603a.draw(canvas);
    }

    public void m1198a(int i) {
        if (this.f1603a != null) {
            this.f1603a.setAlpha(i);
        }
    }

    public void m1200a(int i, int i2, int i3, int i4, int i5) {
        this.f1608f = i;
        this.f1604b = i2;
        this.f1605c = i3;
        this.f1606d = i4;
        this.f1607e = i5;
    }

    public void m1202a(Drawable drawable) {
        this.f1603a = drawable;
    }

    public void m1199a(int i, int i2, int i3, int i4) {
        this.f1602a.set(i, i2, i3, i4);
    }

    public Drawable m1197a() {
        return this.f1603a;
    }

    public Rect m1196a() {
        return this.f1603a.getBounds();
    }
}

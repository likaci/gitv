package com.gala.video.albumlist.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.gala.video.albumlist.utils.a;

public class f {
    private static int a;
    private Rect f661a;
    private Drawable f662a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;

    public f(Context context) {
        this(context, null);
    }

    public f(Context context, Drawable drawable) {
        this.f661a = new Rect();
        this.f662a = drawable;
        a = a.a(context, 40.0f);
    }

    public void a(Canvas canvas) {
        int max = Math.max(a, Math.round((((float) this.e) * ((float) this.e)) / ((float) this.c)));
        int i = this.f - this.f661a.right;
        int minimumWidth = i - this.f662a.getMinimumWidth();
        int i2 = ((this.e - this.f661a.top) - this.f661a.bottom) - max;
        i2 = (Math.round((((float) i2) * ((float) this.d)) / ((float) (this.c - this.e))) + this.f661a.top) + this.b;
        this.f662a.setBounds(minimumWidth, i2, i, max + i2);
        this.f662a.draw(canvas);
    }

    public void a(int i) {
        if (this.f662a != null) {
            this.f662a.setAlpha(i);
        }
    }

    public void a(int i, int i2, int i3, int i4, int i5) {
        this.f = i;
        this.b = i2;
        this.c = i3;
        this.d = i4;
        this.e = i5;
    }

    public void a(Drawable drawable) {
        this.f662a = drawable;
    }

    public void a(int i, int i2, int i3, int i4) {
        this.f661a.set(i, i2, i3, i4);
    }

    public Drawable m134a() {
        return this.f662a;
    }

    public Rect a() {
        return this.f662a.getBounds();
    }
}

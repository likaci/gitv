package com.gala.video.albumlist4.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.gala.video.albumlist4.utils.a;

public class g {
    private static int a;
    private Rect f759a;
    private Drawable f760a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;

    public g(Context context) {
        this(context, null);
    }

    public g(Context context, Drawable drawable) {
        this.f759a = new Rect();
        this.f760a = drawable;
        a = a.a(context, 40.0f);
    }

    public void a(Canvas canvas) {
        int max = Math.max(a, Math.round((((float) this.e) * ((float) this.e)) / ((float) this.c)));
        int i = this.f - this.f759a.right;
        int minimumWidth = i - this.f760a.getMinimumWidth();
        int i2 = ((this.e - this.f759a.top) - this.f759a.bottom) - max;
        i2 = (Math.round((((float) i2) * ((float) this.d)) / ((float) (this.c - this.e))) + this.f759a.top) + this.b;
        this.f760a.setBounds(minimumWidth, i2, i, max + i2);
        this.f760a.draw(canvas);
    }

    public void a(int i) {
        if (this.f760a != null) {
            this.f760a.setAlpha(i);
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
        this.f760a = drawable;
    }

    public void a(int i, int i2, int i3, int i4) {
        this.f759a.set(i, i2, i3, i4);
    }

    public Drawable m189a() {
        return this.f760a;
    }

    public Rect a() {
        return this.f760a.getBounds();
    }
}

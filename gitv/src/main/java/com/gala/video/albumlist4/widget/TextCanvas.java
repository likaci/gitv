package com.gala.video.albumlist4.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class TextCanvas extends a {
    private int a = 13;
    private Context f744a;
    private Paint f745a;
    private Rect f746a = new Rect();
    private Drawable f747a;
    private String f748a;
    private int b = -1;
    private Rect f749b = new Rect();

    public TextCanvas(Context context) {
        this.f744a = context;
        this.f745a = new Paint();
        this.f745a.setAntiAlias(true);
        this.f745a.setStyle(Style.FILL);
    }

    public void setText(String text) {
        this.f748a = text;
    }

    public void setTextSize(int Size) {
        this.a = Size;
    }

    public void setTextColor(int color) {
        this.b = color;
    }

    public void setBackground(int resId) {
        this.f747a = this.f744a.getResources().getDrawable(resId);
    }

    public void draw(Canvas canvas) {
        canvas.save();
        this.f745a.setColor(this.b);
        this.f745a.setTextSize((float) this.a);
        this.f745a.getTextBounds(this.f748a, 0, this.f748a.length(), this.f746a);
        this.f747a.getPadding(this.f749b);
        int height = getHeight() > 0 ? getHeight() : this.f746a.height();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        this.f747a.setBounds(paddingLeft, paddingTop, ((this.f746a.width() + paddingLeft) + this.f749b.left) + getPaddingRight(), paddingTop + height);
        this.f747a.draw(canvas);
        canvas.drawText(this.f748a, (float) ((paddingLeft + this.f749b.left) - this.f746a.left), (float) ((((height - this.f746a.height()) / 2) + paddingTop) - this.f746a.top), this.f745a);
        canvas.restore();
    }
}

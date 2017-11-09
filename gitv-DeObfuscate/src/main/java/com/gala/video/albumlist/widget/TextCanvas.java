package com.gala.video.albumlist.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class TextCanvas extends C0406a {
    private int f1515a = 13;
    private Context f1516a;
    private Paint f1517a;
    private Rect f1518a = new Rect();
    private Drawable f1519a;
    private String f1520a;
    private int f1521b = -1;
    private Rect f1522b = new Rect();

    public TextCanvas(Context context) {
        this.f1516a = context;
        this.f1517a = new Paint();
        this.f1517a.setAntiAlias(true);
        this.f1517a.setStyle(Style.FILL);
    }

    public void setText(String text) {
        this.f1520a = text;
    }

    public void setTextSize(int Size) {
        this.f1515a = Size;
    }

    public void setTextColor(int color) {
        this.f1521b = color;
    }

    public void setBackground(int resId) {
        this.f1519a = this.f1516a.getResources().getDrawable(resId);
    }

    public void draw(Canvas canvas) {
        canvas.save();
        this.f1517a.setColor(this.f1521b);
        this.f1517a.setTextSize((float) this.f1515a);
        this.f1517a.getTextBounds(this.f1520a, 0, this.f1520a.length(), this.f1518a);
        this.f1519a.getPadding(this.f1522b);
        int height = getHeight() > 0 ? getHeight() : this.f1518a.height();
        this.f1519a.setBounds(0, 0, (((this.f1518a.width() + 0) + this.f1522b.left) + getPaddingLeft()) + getPaddingRight(), 0 + height);
        this.f1519a.draw(canvas);
        canvas.drawText(this.f1520a, (float) (((this.f1522b.left + 0) + getPaddingLeft()) - this.f1518a.left), (float) ((((height - this.f1518a.height()) / 2) + 0) - this.f1518a.top), this.f1517a);
        canvas.restore();
    }
}

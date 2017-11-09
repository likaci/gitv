package com.gala.video.albumlist4.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class TextCanvas extends C0452a {
    private int f1741a = 13;
    private Context f1742a;
    private Paint f1743a;
    private Rect f1744a = new Rect();
    private Drawable f1745a;
    private String f1746a;
    private int f1747b = -1;
    private Rect f1748b = new Rect();

    public TextCanvas(Context context) {
        this.f1742a = context;
        this.f1743a = new Paint();
        this.f1743a.setAntiAlias(true);
        this.f1743a.setStyle(Style.FILL);
    }

    public void setText(String text) {
        this.f1746a = text;
    }

    public void setTextSize(int Size) {
        this.f1741a = Size;
    }

    public void setTextColor(int color) {
        this.f1747b = color;
    }

    public void setBackground(int resId) {
        this.f1745a = this.f1742a.getResources().getDrawable(resId);
    }

    public void draw(Canvas canvas) {
        canvas.save();
        this.f1743a.setColor(this.f1747b);
        this.f1743a.setTextSize((float) this.f1741a);
        this.f1743a.getTextBounds(this.f1746a, 0, this.f1746a.length(), this.f1744a);
        this.f1745a.getPadding(this.f1748b);
        int height = getHeight() > 0 ? getHeight() : this.f1744a.height();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        this.f1745a.setBounds(paddingLeft, paddingTop, ((this.f1744a.width() + paddingLeft) + this.f1748b.left) + getPaddingRight(), paddingTop + height);
        this.f1745a.draw(canvas);
        canvas.drawText(this.f1746a, (float) ((paddingLeft + this.f1748b.left) - this.f1744a.left), (float) ((((height - this.f1744a.height()) / 2) + paddingTop) - this.f1744a.top), this.f1743a);
        canvas.restore();
    }
}

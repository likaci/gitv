package com.gala.video.albumlist.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class TextCanvas extends a {
    private int a = 13;
    private Context f646a;
    private Paint f647a;
    private Rect f648a = new Rect();
    private Drawable f649a;
    private String f650a;
    private int b = -1;
    private Rect f651b = new Rect();

    public TextCanvas(Context context) {
        this.f646a = context;
        this.f647a = new Paint();
        this.f647a.setAntiAlias(true);
        this.f647a.setStyle(Style.FILL);
    }

    public void setText(String text) {
        this.f650a = text;
    }

    public void setTextSize(int Size) {
        this.a = Size;
    }

    public void setTextColor(int color) {
        this.b = color;
    }

    public void setBackground(int resId) {
        this.f649a = this.f646a.getResources().getDrawable(resId);
    }

    public void draw(Canvas canvas) {
        canvas.save();
        this.f647a.setColor(this.b);
        this.f647a.setTextSize((float) this.a);
        this.f647a.getTextBounds(this.f650a, 0, this.f650a.length(), this.f648a);
        this.f649a.getPadding(this.f651b);
        int height = getHeight() > 0 ? getHeight() : this.f648a.height();
        this.f649a.setBounds(0, 0, (((this.f648a.width() + 0) + this.f651b.left) + getPaddingLeft()) + getPaddingRight(), 0 + height);
        this.f649a.draw(canvas);
        canvas.drawText(this.f650a, (float) (((this.f651b.left + 0) + getPaddingLeft()) - this.f648a.left), (float) ((((height - this.f648a.height()) / 2) + 0) - this.f648a.top), this.f647a);
        canvas.restore();
    }
}

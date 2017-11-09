package com.gala.video.albumlist.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import com.gala.video.albumlist.utils.C0384a;
import com.gala.video.albumlist.widget.LayoutManager.Orientation;
import java.util.List;
import java.util.Locale;

public class HorizontalGridView extends BlocksView {
    private static final int f1475a = Color.parseColor("#A0A0A0");
    private static int f1476b;
    private static int f1477c;
    private ColorStateList f1478a;
    private Paint f1479a;
    private Rect f1480a;
    private TextCanvas f1481a;
    private String f1482a;
    private List<String> f1483a;
    private Rect f1484b;
    private boolean f1485b;
    private int f1486d;
    private int f1487e;
    private int f1488f;
    private int f1489g;
    private int f1490h;
    private int f1491i;
    private int f1492j;

    public HorizontalGridView(Context context) {
        this(context, null);
    }

    public HorizontalGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.f1480a = new Rect();
        this.f1484b = new Rect();
        this.f1488f = 5;
        this.f1485b = true;
        m1030i();
        m1031j();
    }

    private void m1030i() {
        if (isHorizontalScrollBarEnabled()) {
            setWillNotDraw(false);
        }
        setOrientation(Orientation.HORIZONTAL);
    }

    private void m1031j() {
        f1477c = C0384a.m873a(getContext(), 4.5f);
        f1476b = C0384a.m873a(getContext(), 8.0f);
        this.f1479a = new Paint();
        this.f1479a.setAntiAlias(true);
        this.f1479a.setStyle(Style.FILL);
    }

    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (!(this.f1482a == null && this.f1483a == null)) {
            invalidate();
        }
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        m1027c(canvas);
        m1023a(canvas);
    }

    public void setExtraCount(int extraCount) {
        this.f1492j = extraCount;
    }

    public void setLabel(int resId) {
        setLabel(getResources().getString(resId));
    }

    public void setLabel(String label) {
        this.f1482a = label;
        setWillNotDraw(false);
    }

    public void setLabelPadding(int left, int top, int right, int bottom) {
        this.f1484b.set(left, top, right, bottom);
    }

    public void setLabelColor(int color) {
        this.f1486d = color;
    }

    public void setLabelSize(int size) {
        this.f1487e = size;
    }

    public void setTips(TextCanvas tips) {
        this.f1481a = tips;
    }

    public void setTipsLeftPadding(int padding) {
        this.f1488f = padding;
    }

    public void setTimeSize(int size) {
        this.f1490h = size;
    }

    public void setTimeList(List<String> list) {
        this.f1483a = list;
        setWillNotDraw(false);
    }

    public void setTimeColor(ColorStateList colors) {
        this.f1478a = colors;
    }

    public void setTimeColor(int resId) {
        this.f1478a = getResources().getColorStateList(resId);
    }

    public void setTimePadding(int padding) {
        this.f1489g = padding;
    }

    public void setTimeLineExtraPadding(int padding) {
        this.f1491i = padding;
    }

    public void showPositionInfo(boolean isShowPositionInfo) {
        this.f1485b = isShowPositionInfo;
    }

    private void m1023a(Canvas canvas) {
        canvas.save();
        if (!(this.f1482a == null || this.f1482a.isEmpty())) {
            this.f1479a.setColor(this.f1486d);
            this.f1479a.setTextSize((float) this.f1487e);
            this.f1479a.getTextBounds(this.f1482a, 0, this.f1482a.length(), this.f1480a);
            canvas.translate((float) getScrollX(), 0.0f);
            float f = (float) (this.f1484b.left - this.f1480a.left);
            float f2 = (float) (this.f1484b.top - this.f1480a.top);
            canvas.drawText(this.f1482a, f, f2, this.f1479a);
            if (this.f1481a != null) {
                canvas.translate((((float) this.f1480a.width()) + f) + ((float) this.f1488f), 0.0f);
                this.f1481a.draw(canvas);
            } else {
                int i = (hasFocus() || getScrollType() == 19) ? 1 : 0;
                int i2 = this.f1492j > 0 ? getFocusPosition() >= this.f1492j ? 1 : 0 : getFocusPosition() < getCount() + this.f1492j ? 1 : 0;
                if (!(!this.f1485b || r3 == 0 || i == 0)) {
                    i2 = getCount() - Math.abs(this.f1492j);
                    i = this.f1492j > 0 ? (getFocusPosition() + 1) - this.f1492j : getFocusPosition() + 1;
                    canvas.drawText(String.format(Locale.getDefault(), "%1$d/%2$d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2)}), ((float) (this.f1480a.width() + this.f1484b.right)) + f, f2, this.f1479a);
                }
            }
        }
        canvas.restore();
    }

    private float m1022a() {
        return this.f1479a.getFontMetrics().bottom + ((float) ((this.f1484b.top + this.f1484b.bottom) - this.f1480a.top));
    }

    private void m1025b(Canvas canvas) {
        float c = m1026c();
        float b = m1024b();
        float d = m1028d();
        float a = b + ((float) C0384a.m873a(this.mContext, 1.0f));
        canvas.save();
        this.f1479a.setColor(f1475a);
        canvas.drawRect(c, b, d, a, this.f1479a);
        canvas.restore();
    }

    private float m1024b() {
        return m1022a() + ((float) f1476b);
    }

    private float m1026c() {
        return getFirstAttachedPosition() == 0 ? (float) ((getViewByPosition(0).getLeft() + f1476b) + this.f1491i) : (float) getScrollX();
    }

    private float m1028d() {
        return getLastAttachedPosition() == getLastPosition() ? (float) (getViewByPosition(getLastAttachedPosition()).getRight() - this.f1491i) : (float) (getWidth() + getScrollX());
    }

    private void m1027c(Canvas canvas) {
        if (this.f1478a != null && this.f1483a != null) {
            m1025b(canvas);
            m1029d(canvas);
        }
    }

    private void m1029d(Canvas canvas) {
        float a = m1022a();
        for (int firstAttachedPosition = getFirstAttachedPosition(); firstAttachedPosition <= getLastAttachedPosition(); firstAttachedPosition++) {
            View viewByPosition = getViewByPosition(firstAttachedPosition);
            int defaultColor = this.f1478a.getDefaultColor();
            if (getFocusPosition() == firstAttachedPosition && hasFocus() && getScrollType() != 19) {
                defaultColor = this.f1478a.getColorForState(new int[]{16842908}, 0);
            }
            this.f1479a.setColor(defaultColor);
            canvas.save();
            float left = (float) ((this.f1491i + viewByPosition.getLeft()) + f1476b);
            float f = ((float) f1476b) + a;
            this.f1479a.setAlpha(77);
            canvas.drawCircle(left, f, (float) f1476b, this.f1479a);
            this.f1479a.setAlpha(255);
            canvas.drawCircle(left, f, (float) f1477c, this.f1479a);
            String str = (String) this.f1483a.get(firstAttachedPosition);
            this.f1479a.getTextBounds(str, 0, str.length(), this.f1480a);
            this.f1479a.setTextSize((float) this.f1490h);
            float left2 = (float) ((this.f1491i + viewByPosition.getLeft()) - this.f1480a.left);
            f = ((f + ((float) f1476b)) + ((float) this.f1489g)) - this.f1479a.getFontMetrics().top;
            canvas.clipRect(left2, this.f1479a.getFontMetrics().top + f, (float) (viewByPosition.getRight() - this.f1491i), this.f1479a.getFontMetrics().bottom + f);
            canvas.drawText(str, left2, f, this.f1479a);
            canvas.restore();
        }
    }

    public void setTypeface(Typeface typeface) {
        if (this.f1479a != null) {
            this.f1479a.setTypeface(typeface);
        }
    }
}

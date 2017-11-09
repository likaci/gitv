package com.gala.video.albumlist4.widget;

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
import com.gala.video.albumlist4.utils.C0431a;
import com.gala.video.albumlist4.widget.LayoutManager.Orientation;
import java.util.List;

public class HorizontalGridView extends RecyclerView {
    private static final int f1659a = Color.parseColor("#A0A0A0");
    private static int f1660b;
    private static int f1661c;
    private ColorStateList f1662a;
    private Paint f1663a;
    private Rect f1664a;
    private TextCanvas f1665a;
    private String f1666a;
    private List<String> f1667a;
    private Rect f1668b;
    private boolean f1669b;
    private int f1670d;
    private int f1671e;
    private int f1672f;
    private int f1673g;
    private int f1674h;
    private int f1675i;
    private int f1676j;

    public HorizontalGridView(Context context) {
        this(context, null);
    }

    public HorizontalGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.f1664a = new Rect();
        this.f1668b = new Rect();
        this.f1672f = 5;
        this.f1669b = true;
        m1301g();
        m1302h();
    }

    private void m1301g() {
        if (isHorizontalScrollBarEnabled()) {
            setWillNotDraw(false);
        }
        setOrientation(Orientation.HORIZONTAL);
    }

    private void m1302h() {
        f1661c = C0431a.m1212a(getContext(), 4.5f);
        f1660b = C0431a.m1212a(getContext(), 8.0f);
        this.f1663a = new Paint();
        this.f1663a.setAntiAlias(true);
        this.f1663a.setStyle(Style.FILL);
    }

    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (!(this.f1666a == null && this.f1667a == null)) {
            invalidate();
        }
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        m1298c(canvas);
        m1294a(canvas);
    }

    public void setExtraCount(int extraCount) {
        this.f1676j = extraCount;
    }

    public void setLabel(int resId) {
        setLabel(getResources().getString(resId));
    }

    public void setLabel(String label) {
        this.f1666a = label;
        setWillNotDraw(false);
    }

    public void setLabelPadding(int left, int top, int right, int bottom) {
        this.f1668b.set(left, top, right, bottom);
    }

    public void setLabelColor(int color) {
        this.f1670d = color;
    }

    public void setLabelSize(int size) {
        this.f1671e = size;
    }

    public void setTips(TextCanvas tips) {
        this.f1665a = tips;
    }

    public void setTipsLeftPadding(int padding) {
        this.f1672f = padding;
    }

    public void setTimeSize(int size) {
        this.f1674h = size;
    }

    public void setTimeList(List<String> list) {
        this.f1667a = list;
        setWillNotDraw(false);
    }

    public void setTimeColor(ColorStateList colors) {
        this.f1662a = colors;
    }

    public void setTimeColor(int resId) {
        this.f1662a = getResources().getColorStateList(resId);
    }

    public void setTimePadding(int padding) {
        this.f1673g = padding;
    }

    public void setTimeLineExtraPadding(int padding) {
        this.f1675i = padding;
    }

    public void showPositionInfo(boolean isShowPositionInfo) {
        this.f1669b = isShowPositionInfo;
    }

    private void m1294a(Canvas canvas) {
        int i = 1;
        canvas.save();
        if (!(this.f1666a == null || this.f1666a.isEmpty())) {
            this.f1663a.setColor(this.f1670d);
            this.f1663a.setTextSize((float) this.f1671e);
            this.f1663a.getTextBounds(this.f1666a, 0, this.f1666a.length(), this.f1664a);
            canvas.translate((float) getScrollX(), 0.0f);
            float f = (float) (this.f1668b.left - this.f1664a.left);
            float f2 = (float) (this.f1668b.top - this.f1664a.top);
            canvas.drawText(this.f1666a, f, f2, this.f1663a);
            if (this.f1665a != null) {
                canvas.translate((((float) this.f1664a.width()) + f) + ((float) this.f1672f), 0.0f);
                this.f1665a.draw(canvas);
            } else {
                int i2 = (hasFocus() || getScrollType() == 19) ? 1 : 0;
                if (this.f1676j > 0) {
                    if (getFocusPosition() < this.f1676j) {
                        i = 0;
                    }
                } else if (getFocusPosition() >= getCount() + this.f1676j) {
                    i = 0;
                }
                if (!(!this.f1669b || r2 == 0 || i2 == 0)) {
                    canvas.drawText((this.f1676j > 0 ? (getFocusPosition() + 1) - this.f1676j : getFocusPosition() + 1) + "/" + (getCount() - Math.abs(this.f1676j)), ((float) (this.f1664a.width() + this.f1668b.right)) + f, f2, this.f1663a);
                }
            }
        }
        canvas.restore();
    }

    private float m1293a() {
        return this.f1663a.getFontMetrics().bottom + ((float) ((this.f1668b.top + this.f1668b.bottom) - this.f1664a.top));
    }

    private void m1296b(Canvas canvas) {
        float c = m1297c();
        float b = m1295b();
        float d = m1299d();
        float a = b + ((float) C0431a.m1212a(this.mContext, 1.0f));
        canvas.save();
        this.f1663a.setColor(f1659a);
        canvas.drawRect(c, b, d, a, this.f1663a);
        canvas.restore();
    }

    private float m1295b() {
        return m1293a() + ((float) f1660b);
    }

    private float m1297c() {
        return getFirstAttachedPosition() == 0 ? (float) ((getViewByPosition(0).getLeft() + f1660b) + this.f1675i) : (float) getScrollX();
    }

    private float m1299d() {
        return getLastAttachedPosition() == getLastPosition() ? (float) (getViewByPosition(getLastAttachedPosition()).getRight() - this.f1675i) : (float) (getWidth() + getScrollX());
    }

    private void m1298c(Canvas canvas) {
        if (this.f1662a != null && this.f1667a != null) {
            m1296b(canvas);
            m1300d(canvas);
        }
    }

    private void m1300d(Canvas canvas) {
        float a = m1293a();
        for (int firstAttachedPosition = getFirstAttachedPosition(); firstAttachedPosition <= getLastAttachedPosition(); firstAttachedPosition++) {
            View viewByPosition = getViewByPosition(firstAttachedPosition);
            int defaultColor = this.f1662a.getDefaultColor();
            if (getFocusPosition() == firstAttachedPosition && hasFocus() && getScrollType() != 19) {
                defaultColor = this.f1662a.getColorForState(new int[]{16842908}, 0);
            }
            this.f1663a.setColor(defaultColor);
            canvas.save();
            float left = (float) ((this.f1675i + viewByPosition.getLeft()) + f1660b);
            float f = ((float) f1660b) + a;
            this.f1663a.setAlpha(77);
            canvas.drawCircle(left, f, (float) f1660b, this.f1663a);
            this.f1663a.setAlpha(255);
            canvas.drawCircle(left, f, (float) f1661c, this.f1663a);
            String str = (String) this.f1667a.get(firstAttachedPosition);
            this.f1663a.getTextBounds(str, 0, str.length(), this.f1664a);
            this.f1663a.setTextSize((float) this.f1674h);
            float left2 = (float) ((this.f1675i + viewByPosition.getLeft()) - this.f1664a.left);
            f = ((f + ((float) f1660b)) + ((float) this.f1673g)) - this.f1663a.getFontMetrics().top;
            canvas.clipRect(left2, this.f1663a.getFontMetrics().top + f, (float) (viewByPosition.getRight() - this.f1675i), this.f1663a.getFontMetrics().bottom + f);
            canvas.drawText(str, left2, f, this.f1663a);
            canvas.restore();
        }
    }

    public void setTypeface(Typeface typeface) {
        if (this.f1663a != null) {
            this.f1663a.setTypeface(typeface);
        }
    }
}

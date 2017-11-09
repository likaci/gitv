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
import com.gala.video.albumlist.utils.a;
import com.gala.video.albumlist.widget.LayoutManager.Orientation;
import java.util.List;
import java.util.Locale;

public class HorizontalGridView extends BlocksView {
    private static final int a = Color.parseColor("#A0A0A0");
    private static int b;
    private static int c;
    private ColorStateList f638a;
    private Paint f639a;
    private Rect f640a;
    private TextCanvas f641a;
    private String f642a;
    private List<String> f643a;
    private Rect f644b;
    private boolean f645b;
    private int d;
    private int e;
    private int f;
    private int g;
    private int h;
    private int i;
    private int j;

    public HorizontalGridView(Context context) {
        this(context, null);
    }

    public HorizontalGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.f640a = new Rect();
        this.f644b = new Rect();
        this.f = 5;
        this.f645b = true;
        i();
        j();
    }

    private void i() {
        if (isHorizontalScrollBarEnabled()) {
            setWillNotDraw(false);
        }
        setOrientation(Orientation.HORIZONTAL);
    }

    private void j() {
        c = a.a(getContext(), 4.5f);
        b = a.a(getContext(), 8.0f);
        this.f639a = new Paint();
        this.f639a.setAntiAlias(true);
        this.f639a.setStyle(Style.FILL);
    }

    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (!(this.f642a == null && this.f643a == null)) {
            invalidate();
        }
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        c(canvas);
        a(canvas);
    }

    public void setExtraCount(int extraCount) {
        this.j = extraCount;
    }

    public void setLabel(int resId) {
        setLabel(getResources().getString(resId));
    }

    public void setLabel(String label) {
        this.f642a = label;
        setWillNotDraw(false);
    }

    public void setLabelPadding(int left, int top, int right, int bottom) {
        this.f644b.set(left, top, right, bottom);
    }

    public void setLabelColor(int color) {
        this.d = color;
    }

    public void setLabelSize(int size) {
        this.e = size;
    }

    public void setTips(TextCanvas tips) {
        this.f641a = tips;
    }

    public void setTipsLeftPadding(int padding) {
        this.f = padding;
    }

    public void setTimeSize(int size) {
        this.h = size;
    }

    public void setTimeList(List<String> list) {
        this.f643a = list;
        setWillNotDraw(false);
    }

    public void setTimeColor(ColorStateList colors) {
        this.f638a = colors;
    }

    public void setTimeColor(int resId) {
        this.f638a = getResources().getColorStateList(resId);
    }

    public void setTimePadding(int padding) {
        this.g = padding;
    }

    public void setTimeLineExtraPadding(int padding) {
        this.i = padding;
    }

    public void showPositionInfo(boolean isShowPositionInfo) {
        this.f645b = isShowPositionInfo;
    }

    private void a(Canvas canvas) {
        canvas.save();
        if (!(this.f642a == null || this.f642a.isEmpty())) {
            this.f639a.setColor(this.d);
            this.f639a.setTextSize((float) this.e);
            this.f639a.getTextBounds(this.f642a, 0, this.f642a.length(), this.f640a);
            canvas.translate((float) getScrollX(), 0.0f);
            float f = (float) (this.f644b.left - this.f640a.left);
            float f2 = (float) (this.f644b.top - this.f640a.top);
            canvas.drawText(this.f642a, f, f2, this.f639a);
            if (this.f641a != null) {
                canvas.translate((((float) this.f640a.width()) + f) + ((float) this.f), 0.0f);
                this.f641a.draw(canvas);
            } else {
                int i = (hasFocus() || getScrollType() == 19) ? 1 : 0;
                int i2 = this.j > 0 ? getFocusPosition() >= this.j ? 1 : 0 : getFocusPosition() < getCount() + this.j ? 1 : 0;
                if (!(!this.f645b || r3 == 0 || i == 0)) {
                    i2 = getCount() - Math.abs(this.j);
                    i = this.j > 0 ? (getFocusPosition() + 1) - this.j : getFocusPosition() + 1;
                    canvas.drawText(String.format(Locale.getDefault(), "%1$d/%2$d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2)}), ((float) (this.f640a.width() + this.f644b.right)) + f, f2, this.f639a);
                }
            }
        }
        canvas.restore();
    }

    private float a() {
        return this.f639a.getFontMetrics().bottom + ((float) ((this.f644b.top + this.f644b.bottom) - this.f640a.top));
    }

    private void b(Canvas canvas) {
        float c = c();
        float b = b();
        float d = d();
        float a = b + ((float) a.a(this.mContext, 1.0f));
        canvas.save();
        this.f639a.setColor(a);
        canvas.drawRect(c, b, d, a, this.f639a);
        canvas.restore();
    }

    private float b() {
        return a() + ((float) b);
    }

    private float c() {
        return getFirstAttachedPosition() == 0 ? (float) ((getViewByPosition(0).getLeft() + b) + this.i) : (float) getScrollX();
    }

    private float d() {
        return getLastAttachedPosition() == getLastPosition() ? (float) (getViewByPosition(getLastAttachedPosition()).getRight() - this.i) : (float) (getWidth() + getScrollX());
    }

    private void c(Canvas canvas) {
        if (this.f638a != null && this.f643a != null) {
            b(canvas);
            d(canvas);
        }
    }

    private void d(Canvas canvas) {
        float a = a();
        for (int firstAttachedPosition = getFirstAttachedPosition(); firstAttachedPosition <= getLastAttachedPosition(); firstAttachedPosition++) {
            View viewByPosition = getViewByPosition(firstAttachedPosition);
            int defaultColor = this.f638a.getDefaultColor();
            if (getFocusPosition() == firstAttachedPosition && hasFocus() && getScrollType() != 19) {
                defaultColor = this.f638a.getColorForState(new int[]{16842908}, 0);
            }
            this.f639a.setColor(defaultColor);
            canvas.save();
            float left = (float) ((this.i + viewByPosition.getLeft()) + b);
            float f = ((float) b) + a;
            this.f639a.setAlpha(77);
            canvas.drawCircle(left, f, (float) b, this.f639a);
            this.f639a.setAlpha(255);
            canvas.drawCircle(left, f, (float) c, this.f639a);
            String str = (String) this.f643a.get(firstAttachedPosition);
            this.f639a.getTextBounds(str, 0, str.length(), this.f640a);
            this.f639a.setTextSize((float) this.h);
            float left2 = (float) ((this.i + viewByPosition.getLeft()) - this.f640a.left);
            f = ((f + ((float) b)) + ((float) this.g)) - this.f639a.getFontMetrics().top;
            canvas.clipRect(left2, this.f639a.getFontMetrics().top + f, (float) (viewByPosition.getRight() - this.i), this.f639a.getFontMetrics().bottom + f);
            canvas.drawText(str, left2, f, this.f639a);
            canvas.restore();
        }
    }

    public void setTypeface(Typeface typeface) {
        if (this.f639a != null) {
            this.f639a.setTypeface(typeface);
        }
    }
}

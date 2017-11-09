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
import com.gala.video.albumlist4.utils.a;
import com.gala.video.albumlist4.widget.LayoutManager.Orientation;
import java.util.List;

public class HorizontalGridView extends RecyclerView {
    private static final int a = Color.parseColor("#A0A0A0");
    private static int b;
    private static int c;
    private ColorStateList f736a;
    private Paint f737a;
    private Rect f738a;
    private TextCanvas f739a;
    private String f740a;
    private List<String> f741a;
    private Rect f742b;
    private boolean f743b;
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
        this.f738a = new Rect();
        this.f742b = new Rect();
        this.f = 5;
        this.f743b = true;
        g();
        h();
    }

    private void g() {
        if (isHorizontalScrollBarEnabled()) {
            setWillNotDraw(false);
        }
        setOrientation(Orientation.HORIZONTAL);
    }

    private void h() {
        c = a.a(getContext(), 4.5f);
        b = a.a(getContext(), 8.0f);
        this.f737a = new Paint();
        this.f737a.setAntiAlias(true);
        this.f737a.setStyle(Style.FILL);
    }

    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (!(this.f740a == null && this.f741a == null)) {
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
        this.f740a = label;
        setWillNotDraw(false);
    }

    public void setLabelPadding(int left, int top, int right, int bottom) {
        this.f742b.set(left, top, right, bottom);
    }

    public void setLabelColor(int color) {
        this.d = color;
    }

    public void setLabelSize(int size) {
        this.e = size;
    }

    public void setTips(TextCanvas tips) {
        this.f739a = tips;
    }

    public void setTipsLeftPadding(int padding) {
        this.f = padding;
    }

    public void setTimeSize(int size) {
        this.h = size;
    }

    public void setTimeList(List<String> list) {
        this.f741a = list;
        setWillNotDraw(false);
    }

    public void setTimeColor(ColorStateList colors) {
        this.f736a = colors;
    }

    public void setTimeColor(int resId) {
        this.f736a = getResources().getColorStateList(resId);
    }

    public void setTimePadding(int padding) {
        this.g = padding;
    }

    public void setTimeLineExtraPadding(int padding) {
        this.i = padding;
    }

    public void showPositionInfo(boolean isShowPositionInfo) {
        this.f743b = isShowPositionInfo;
    }

    private void a(Canvas canvas) {
        int i = 1;
        canvas.save();
        if (!(this.f740a == null || this.f740a.isEmpty())) {
            this.f737a.setColor(this.d);
            this.f737a.setTextSize((float) this.e);
            this.f737a.getTextBounds(this.f740a, 0, this.f740a.length(), this.f738a);
            canvas.translate((float) getScrollX(), 0.0f);
            float f = (float) (this.f742b.left - this.f738a.left);
            float f2 = (float) (this.f742b.top - this.f738a.top);
            canvas.drawText(this.f740a, f, f2, this.f737a);
            if (this.f739a != null) {
                canvas.translate((((float) this.f738a.width()) + f) + ((float) this.f), 0.0f);
                this.f739a.draw(canvas);
            } else {
                int i2 = (hasFocus() || getScrollType() == 19) ? 1 : 0;
                if (this.j > 0) {
                    if (getFocusPosition() < this.j) {
                        i = 0;
                    }
                } else if (getFocusPosition() >= getCount() + this.j) {
                    i = 0;
                }
                if (!(!this.f743b || r2 == 0 || i2 == 0)) {
                    canvas.drawText((this.j > 0 ? (getFocusPosition() + 1) - this.j : getFocusPosition() + 1) + "/" + (getCount() - Math.abs(this.j)), ((float) (this.f738a.width() + this.f742b.right)) + f, f2, this.f737a);
                }
            }
        }
        canvas.restore();
    }

    private float a() {
        return this.f737a.getFontMetrics().bottom + ((float) ((this.f742b.top + this.f742b.bottom) - this.f738a.top));
    }

    private void b(Canvas canvas) {
        float c = c();
        float b = b();
        float d = d();
        float a = b + ((float) a.a(this.mContext, 1.0f));
        canvas.save();
        this.f737a.setColor(a);
        canvas.drawRect(c, b, d, a, this.f737a);
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
        if (this.f736a != null && this.f741a != null) {
            b(canvas);
            d(canvas);
        }
    }

    private void d(Canvas canvas) {
        float a = a();
        for (int firstAttachedPosition = getFirstAttachedPosition(); firstAttachedPosition <= getLastAttachedPosition(); firstAttachedPosition++) {
            View viewByPosition = getViewByPosition(firstAttachedPosition);
            int defaultColor = this.f736a.getDefaultColor();
            if (getFocusPosition() == firstAttachedPosition && hasFocus() && getScrollType() != 19) {
                defaultColor = this.f736a.getColorForState(new int[]{16842908}, 0);
            }
            this.f737a.setColor(defaultColor);
            canvas.save();
            float left = (float) ((this.i + viewByPosition.getLeft()) + b);
            float f = ((float) b) + a;
            this.f737a.setAlpha(77);
            canvas.drawCircle(left, f, (float) b, this.f737a);
            this.f737a.setAlpha(255);
            canvas.drawCircle(left, f, (float) c, this.f737a);
            String str = (String) this.f741a.get(firstAttachedPosition);
            this.f737a.getTextBounds(str, 0, str.length(), this.f738a);
            this.f737a.setTextSize((float) this.h);
            float left2 = (float) ((this.i + viewByPosition.getLeft()) - this.f738a.left);
            f = ((f + ((float) b)) + ((float) this.g)) - this.f737a.getFontMetrics().top;
            canvas.clipRect(left2, this.f737a.getFontMetrics().top + f, (float) (viewByPosition.getRight() - this.i), this.f737a.getFontMetrics().bottom + f);
            canvas.drawText(str, left2, f, this.f737a);
            canvas.restore();
        }
    }

    public void setTypeface(Typeface typeface) {
        if (this.f737a != null) {
            this.f737a.setTypeface(typeface);
        }
    }
}

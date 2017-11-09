package com.gala.video.albumlist.layout;

import android.graphics.Rect;
import android.util.AndroidRuntimeException;
import android.view.View;
import com.gala.video.albumlist.utils.b;
import com.gala.video.albumlist.widget.d;
import com.gala.video.albumlist.widget.d.a;

public abstract class BlockLayout {
    protected int a = 1;
    protected Rect f573a = new Rect();
    protected b f574a = new b(-1, -1);
    protected a f575a = new a();
    protected d.b f576a;
    protected d f577a;
    protected Object[] f578a = new Object[3];
    protected int b = 0;
    private int c;
    private int d;
    private int e;
    private int f;
    private int g;
    private int h;
    private int i;
    private int j;
    private int k;
    private int l = 0;
    private int m = 0;

    public abstract boolean appendAttachedItems(int i, int i2, boolean z);

    public abstract boolean prependAttachedItems(int i, boolean z);

    public void setVerticalMargin(int margin) {
        this.m = margin;
    }

    public void setHorizontalMargin(int margin) {
        this.l = margin;
    }

    public int getVerticalMargin() {
        return this.m;
    }

    public int getHorizontalMargin() {
        return this.l;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        this.d = left;
        this.e = top;
        this.f = right;
        this.g = bottom;
    }

    public void setMargins(int left, int top, int right, int bottom) {
        this.h = left;
        this.i = top;
        this.j = right;
        this.k = bottom;
    }

    public int getMarginLeft() {
        return this.h;
    }

    public int getMarginTop() {
        return this.i;
    }

    public int getMarginRight() {
        return this.j;
    }

    public int getMarginBottom() {
        return this.k;
    }

    public int getPaddingLeft() {
        return this.d;
    }

    public int getPaddingTop() {
        return this.e;
    }

    public int getPaddingRight() {
        return this.f;
    }

    public int getPaddingBottom() {
        return this.g;
    }

    public int getItemCount() {
        return this.b;
    }

    public BlockLayout setItemCount(int itemCount) {
        this.b = itemCount;
        return this;
    }

    public int getFirstPosition() {
        return this.f574a.a();
    }

    public int getLastPosition() {
        return this.f574a.b();
    }

    public void setRang(int start, int end) {
        this.f574a.a(start, end);
    }

    public void setProvider(d.b provider) {
        this.f576a = provider;
    }

    public boolean isOutRang(int position) {
        return position < this.f574a.a() || position > this.f574a.b();
    }

    public void setGrid(d grid) {
        this.f577a = grid;
    }

    public void setNumRows(int numRows) {
        if (this.a != numRows) {
            this.a = numRows;
        }
    }

    public int getNumRows(int position) {
        int i = this.a;
        if (i != 0) {
            return i;
        }
        throw new AndroidRuntimeException("Row number can't be zero!!!");
    }

    public void setType(int type) {
        this.c = type;
    }

    public int getType() {
        return this.c;
    }

    protected void a(Object obj, int i, int i2, int i3, int i4) {
        a(obj, i, i2, i3, i4, true);
    }

    protected void b(Object obj, int i, int i2, int i3, int i4) {
        a(obj, i, i2, i3, i4, false);
    }

    protected void a(Object obj, int i, int i2, int i3, int i4, boolean z) {
        this.f576a.b(obj, i, i2, i3, i4, z);
    }

    protected void c(Object obj, int i, int i2, int i3, int i4) {
        b(obj, i, i2, i3, i4, true);
    }

    protected void d(Object obj, int i, int i2, int i3, int i4) {
        b(obj, i, i2, i3, i4, false);
    }

    protected void b(Object obj, int i, int i2, int i3, int i4, boolean z) {
        this.f576a.a(obj, i, i2, i3, i4, z);
    }

    public int getViewMin(int index) {
        return this.f576a.a(this.f577a.a(index).getMinViewPosition());
    }

    public int getViewMax(int index) {
        return this.f576a.b(this.f577a.a(index).getMaxViewPosition());
    }

    public void updateLayoutRegion(BlockLayout blockLayout, boolean isAppend) {
        if (blockLayout != null && this.f576a.getFirstAttachedPosition() != -1 && this.f576a.getLastAttachedPosition() != -1) {
            if (isAppend) {
                this.f573a.top = (blockLayout.getLayoutMax() + blockLayout.getMarginBottom()) + getMarginTop();
                return;
            }
            this.f573a.bottom = (blockLayout.getLayoutMin() - blockLayout.getMarginTop()) - getMarginBottom();
        }
    }

    public int getLayoutMin() {
        View viewByPosition = this.f576a.getViewByPosition(getMinViewPosition());
        if (viewByPosition != null) {
            return viewByPosition.getTop() - getPaddingTop();
        }
        return 0;
    }

    public int getLayoutMax() {
        View viewByPosition = this.f576a.getViewByPosition(getMaxViewPosition());
        if (viewByPosition != null) {
            return viewByPosition.getBottom() + getPaddingBottom();
        }
        return 0;
    }

    public int getMinViewPosition() {
        int max = Math.max(getFirstPosition(), this.f576a.getFirstAttachedPosition());
        int min = Math.min(getLastPosition(), this.f576a.getLastAttachedPosition());
        for (int i = max + 1; i <= min; i++) {
            View viewByPosition = this.f576a.getViewByPosition(i);
            View viewByPosition2 = this.f576a.getViewByPosition(max);
            if (viewByPosition != null) {
                if (viewByPosition.getTop() < viewByPosition2.getTop()) {
                    max = i;
                } else if (viewByPosition.getTop() >= viewByPosition2.getBottom()) {
                    break;
                }
            }
        }
        return max;
    }

    public int getMaxViewPosition() {
        int max = Math.max(getFirstPosition(), this.f576a.getFirstAttachedPosition());
        int min = Math.min(getLastPosition(), this.f576a.getLastAttachedPosition());
        for (int i = min - 1; i >= max; i--) {
            View viewByPosition = this.f576a.getViewByPosition(i);
            View viewByPosition2 = this.f576a.getViewByPosition(min);
            if (viewByPosition != null) {
                if (viewByPosition2 == null) {
                    return i;
                }
                if (viewByPosition.getBottom() > viewByPosition2.getBottom()) {
                    min = i;
                } else if (viewByPosition.getBottom() <= viewByPosition2.getTop()) {
                    break;
                }
            }
        }
        return min;
    }

    public int getColumn(int position) {
        int k = this.f576a.k(position);
        return k == -1 ? position % this.a : k;
    }

    public Rect getLayoutRegion() {
        return this.f573a;
    }
}

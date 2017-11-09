package com.gala.video.albumlist.layout;

import android.graphics.Rect;
import android.util.AndroidRuntimeException;
import android.view.View;
import com.gala.video.albumlist.utils.C0385b;
import com.gala.video.albumlist.widget.C0423d;
import com.gala.video.albumlist.widget.C0423d.C0421a;
import com.gala.video.albumlist.widget.C0423d.C0422b;

public abstract class BlockLayout {
    protected int f1359a = 1;
    protected Rect f1360a = new Rect();
    protected C0385b f1361a = new C0385b(-1, -1);
    protected C0421a f1362a = new C0421a();
    protected C0422b f1363a;
    protected C0423d f1364a;
    protected Object[] f1365a = new Object[3];
    protected int f1366b = 0;
    private int f1367c;
    private int f1368d;
    private int f1369e;
    private int f1370f;
    private int f1371g;
    private int f1372h;
    private int f1373i;
    private int f1374j;
    private int f1375k;
    private int f1376l = 0;
    private int f1377m = 0;

    public abstract boolean appendAttachedItems(int i, int i2, boolean z);

    public abstract boolean prependAttachedItems(int i, boolean z);

    public void setVerticalMargin(int margin) {
        this.f1377m = margin;
    }

    public void setHorizontalMargin(int margin) {
        this.f1376l = margin;
    }

    public int getVerticalMargin() {
        return this.f1377m;
    }

    public int getHorizontalMargin() {
        return this.f1376l;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        this.f1368d = left;
        this.f1369e = top;
        this.f1370f = right;
        this.f1371g = bottom;
    }

    public void setMargins(int left, int top, int right, int bottom) {
        this.f1372h = left;
        this.f1373i = top;
        this.f1374j = right;
        this.f1375k = bottom;
    }

    public int getMarginLeft() {
        return this.f1372h;
    }

    public int getMarginTop() {
        return this.f1373i;
    }

    public int getMarginRight() {
        return this.f1374j;
    }

    public int getMarginBottom() {
        return this.f1375k;
    }

    public int getPaddingLeft() {
        return this.f1368d;
    }

    public int getPaddingTop() {
        return this.f1369e;
    }

    public int getPaddingRight() {
        return this.f1370f;
    }

    public int getPaddingBottom() {
        return this.f1371g;
    }

    public int getItemCount() {
        return this.f1366b;
    }

    public BlockLayout setItemCount(int itemCount) {
        this.f1366b = itemCount;
        return this;
    }

    public int getFirstPosition() {
        return this.f1361a.m874a();
    }

    public int getLastPosition() {
        return this.f1361a.m876b();
    }

    public void setRang(int start, int end) {
        this.f1361a.m875a(start, end);
    }

    public void setProvider(C0422b provider) {
        this.f1363a = provider;
    }

    public boolean isOutRang(int position) {
        return position < this.f1361a.m874a() || position > this.f1361a.m876b();
    }

    public void setGrid(C0423d grid) {
        this.f1364a = grid;
    }

    public void setNumRows(int numRows) {
        if (this.f1359a != numRows) {
            this.f1359a = numRows;
        }
    }

    public int getNumRows(int position) {
        int i = this.f1359a;
        if (i != 0) {
            return i;
        }
        throw new AndroidRuntimeException("Row number can't be zero!!!");
    }

    public void setType(int type) {
        this.f1367c = type;
    }

    public int getType() {
        return this.f1367c;
    }

    protected void m843a(Object obj, int i, int i2, int i3, int i4) {
        m844a(obj, i, i2, i3, i4, true);
    }

    protected void m845b(Object obj, int i, int i2, int i3, int i4) {
        m844a(obj, i, i2, i3, i4, false);
    }

    protected void m844a(Object obj, int i, int i2, int i3, int i4, boolean z) {
        this.f1363a.mo915b(obj, i, i2, i3, i4, z);
    }

    protected void m847c(Object obj, int i, int i2, int i3, int i4) {
        m846b(obj, i, i2, i3, i4, true);
    }

    protected void m848d(Object obj, int i, int i2, int i3, int i4) {
        m846b(obj, i, i2, i3, i4, false);
    }

    protected void m846b(Object obj, int i, int i2, int i3, int i4, boolean z) {
        this.f1363a.mo912a(obj, i, i2, i3, i4, z);
    }

    public int getViewMin(int index) {
        return this.f1363a.mo909a(this.f1364a.m1125a(index).getMinViewPosition());
    }

    public int getViewMax(int index) {
        return this.f1363a.mo913b(this.f1364a.m1125a(index).getMaxViewPosition());
    }

    public void updateLayoutRegion(BlockLayout blockLayout, boolean isAppend) {
        if (blockLayout != null && this.f1363a.getFirstAttachedPosition() != -1 && this.f1363a.getLastAttachedPosition() != -1) {
            if (isAppend) {
                this.f1360a.top = (blockLayout.getLayoutMax() + blockLayout.getMarginBottom()) + getMarginTop();
                return;
            }
            this.f1360a.bottom = (blockLayout.getLayoutMin() - blockLayout.getMarginTop()) - getMarginBottom();
        }
    }

    public int getLayoutMin() {
        View viewByPosition = this.f1363a.getViewByPosition(getMinViewPosition());
        if (viewByPosition != null) {
            return viewByPosition.getTop() - getPaddingTop();
        }
        return 0;
    }

    public int getLayoutMax() {
        View viewByPosition = this.f1363a.getViewByPosition(getMaxViewPosition());
        if (viewByPosition != null) {
            return viewByPosition.getBottom() + getPaddingBottom();
        }
        return 0;
    }

    public int getMinViewPosition() {
        int max = Math.max(getFirstPosition(), this.f1363a.getFirstAttachedPosition());
        int min = Math.min(getLastPosition(), this.f1363a.getLastAttachedPosition());
        for (int i = max + 1; i <= min; i++) {
            View viewByPosition = this.f1363a.getViewByPosition(i);
            View viewByPosition2 = this.f1363a.getViewByPosition(max);
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
        int max = Math.max(getFirstPosition(), this.f1363a.getFirstAttachedPosition());
        int min = Math.min(getLastPosition(), this.f1363a.getLastAttachedPosition());
        for (int i = min - 1; i >= max; i--) {
            View viewByPosition = this.f1363a.getViewByPosition(i);
            View viewByPosition2 = this.f1363a.getViewByPosition(min);
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
        int k = this.f1363a.mo948k(position);
        return k == -1 ? position % this.f1359a : k;
    }

    public Rect getLayoutRegion() {
        return this.f1360a;
    }
}

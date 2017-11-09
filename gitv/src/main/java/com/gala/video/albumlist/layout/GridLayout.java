package com.gala.video.albumlist.layout;

import android.util.AndroidRuntimeException;
import com.gala.video.albumlist.widget.d.a;
import java.util.ArrayList;
import java.util.List;

public class GridLayout extends BlockLayout {
    private NumRowsController a;

    public interface CountCallback {
        int count();
    }

    public static class NumRowsController {
        public List<CountCallback> mCountList = new ArrayList();
        public List<Integer> mNumRowsList = new ArrayList();

        public NumRowsController add(int numRow, CountCallback callback) {
            this.mNumRowsList.add(Integer.valueOf(numRow));
            this.mCountList.add(callback);
            return this;
        }

        public int getNumRows(int position) {
            int i = 0;
            for (int i2 = 0; i2 < this.mCountList.size(); i2++) {
                i += ((CountCallback) this.mCountList.get(i2)).count();
                if (position < i) {
                    return ((Integer) this.mNumRowsList.get(i2)).intValue();
                }
            }
            return 0;
        }

        public int getColumn(int position) {
            int i = 0;
            for (int i2 = 0; i2 < this.mCountList.size(); i2++) {
                if (position < ((CountCallback) this.mCountList.get(i2)).count() + i) {
                    return (position - i) % ((Integer) this.mNumRowsList.get(i2)).intValue();
                }
                i += ((CountCallback) this.mCountList.get(i2)).count();
            }
            return 0;
        }
    }

    public boolean appendAttachedItems(int start, int toLimit, boolean oneColumnMode) {
        if (b(toLimit) || start < 0) {
            return false;
        }
        int count = this.a.getCount();
        a b = b(start);
        boolean z = false;
        while (true) {
            int numRows = getNumRows(b.b);
            int i = Integer.MIN_VALUE;
            while (b.a < numRows) {
                if (b.b >= count) {
                    return z;
                }
                int a = this.a.a(b, true, this.a);
                if (i == Integer.MIN_VALUE) {
                    i = b(b);
                }
                c(this.a[0], b.b, a, a(b), i);
                b.b++;
                if (isOutRang(b.b)) {
                    return true;
                }
                if (numRows != getNumRows(b.b)) {
                    z = true;
                    break;
                }
                b.a++;
                z = true;
            }
            if (oneColumnMode || b(toLimit)) {
                return z;
            }
            b.a = 0;
        }
    }

    public boolean prependAttachedItems(int start, boolean oneColumnMode) {
        if (a(0) || start < 0) {
            return false;
        }
        a a = a(start);
        boolean z = false;
        while (true) {
            int numRows = getNumRows(a.b);
            int i = Integer.MIN_VALUE;
            boolean z2 = z;
            while (a.a < numRows) {
                if (a.b < 0) {
                    return z2;
                }
                int a2 = this.a.a(a, false, this.a);
                if (i == Integer.MIN_VALUE) {
                    i = a(a, numRows) - a2;
                }
                d(this.a[0], a.b, a2, a(a), i);
                a.b++;
                z2 = true;
                if (isOutRang(a.b)) {
                    return true;
                }
                a.a++;
            }
            if (oneColumnMode || a(0)) {
                return z2;
            }
            int firstAttachedPosition = this.a.getFirstAttachedPosition() - 1;
            if (isOutRang(firstAttachedPosition)) {
                return z2;
            }
            a = a(firstAttachedPosition);
            z = z2;
        }
    }

    private int a(a aVar) {
        if (aVar.a == 0) {
            return getPaddingLeft();
        }
        int i = aVar.b - 1;
        return ((this.a.j(i) + this.a.c(i)) + getHorizontalMargin()) + this.a.i(aVar.b);
    }

    private a m103a(int i) {
        a aVar = this.a;
        aVar.b = i - getColumn(i);
        aVar.a = 0;
        return aVar;
    }

    private a m104b(int i) {
        a aVar = this.a;
        if (this.a.getLastAttachedPosition() >= 0) {
            aVar.b = i;
            aVar.a = getColumn(i);
        } else {
            aVar.b = i - getColumn(i);
            aVar.a = 0;
        }
        return aVar;
    }

    private int a(a aVar, int i) {
        if (getLastPosition() - aVar.b <= getColumn(getLastPosition())) {
            return getLayoutRegion().bottom - getPaddingBottom();
        }
        int minViewPosition = getMinViewPosition();
        return ((getViewMin(minViewPosition) - this.a.g(minViewPosition)) - this.a.h(aVar.b)) - getVerticalMargin();
    }

    private int b(a aVar) {
        if (this.a.getLastAttachedPosition() == -1 || this.a.getFirstAttachedPosition() == -1) {
            return aVar.b == 0 ? this.a.getPaddingMin() : 0;
        } else {
            if (aVar.b == getFirstPosition()) {
                return getLayoutRegion().top + getPaddingTop();
            }
            int maxViewPosition = getMaxViewPosition();
            return ((this.a.h(maxViewPosition) + getViewMax(maxViewPosition)) + this.a.g(aVar.b)) + getVerticalMargin();
        }
    }

    protected final boolean m105a(int i) {
        if (this.a.getLastAttachedPosition() >= 0 && b(this.a.getFirstAttachedPosition()) <= i - this.a.a()) {
            return true;
        }
        return false;
    }

    protected boolean m106b(int i) {
        if (this.a.getLastAttachedPosition() < 0) {
            return false;
        }
        int lastAttachedPosition = this.a.getLastAttachedPosition();
        if (isOutRang(lastAttachedPosition)) {
            if (this.a.e(lastAttachedPosition) < this.a.a() + i) {
                return false;
            }
            return true;
        } else if (a(this.a.getLastAttachedPosition()) < this.a.a() + i) {
            return false;
        } else {
            return true;
        }
    }

    private int a(int i) {
        int numRows = (i - getNumRows(i)) + 1;
        if (this.a.getFirstAttachedPosition() > numRows) {
            numRows = this.a.getFirstAttachedPosition();
        }
        return this.a.e(numRows);
    }

    private int b(int i) {
        int numRows = (getNumRows(i) + i) - 1;
        if (numRows > this.a.getLastAttachedPosition()) {
            numRows = this.a.getLastAttachedPosition();
        }
        return this.a.d(numRows);
    }

    public void setNumRowsController(NumRowsController numRowsController) {
        this.a = numRowsController;
    }

    public int getColumn(int position) {
        if (this.a != null) {
            return this.a.getColumn(position - getFirstPosition());
        }
        return position % this.a;
    }

    public int getNumRows(int position) {
        int i = 0;
        if (this.a != null) {
            i = this.a.getNumRows(position - getFirstPosition());
        }
        if (i == 0) {
            i = this.a;
            if (i == 0) {
                throw new AndroidRuntimeException("Row number can't be zero!!!");
            }
        }
        return i;
    }
}

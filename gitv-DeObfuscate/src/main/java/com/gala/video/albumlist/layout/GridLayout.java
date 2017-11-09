package com.gala.video.albumlist.layout;

import android.util.AndroidRuntimeException;
import com.gala.video.albumlist.widget.C0423d.C0421a;
import java.util.ArrayList;
import java.util.List;

public class GridLayout extends BlockLayout {
    private NumRowsController f1379a;

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
        if (m859b(toLimit) || start < 0) {
            return false;
        }
        int count = this.a.getCount();
        C0421a b = m859b(start);
        boolean z = false;
        while (true) {
            int numRows = getNumRows(b.f1577b);
            int i = Integer.MIN_VALUE;
            while (b.f1576a < numRows) {
                if (b.f1577b >= count) {
                    return z;
                }
                int a = this.a.mo911a(b, true, this.a);
                if (i == Integer.MIN_VALUE) {
                    i = m860b(b);
                }
                m847c(this.a[0], b.f1577b, a, m856a(b), i);
                b.f1577b++;
                if (isOutRang(b.f1577b)) {
                    return true;
                }
                if (numRows != getNumRows(b.f1577b)) {
                    z = true;
                    break;
                }
                b.f1576a++;
                z = true;
            }
            if (oneColumnMode || m859b(toLimit)) {
                return z;
            }
            b.f1576a = 0;
        }
    }

    public boolean prependAttachedItems(int start, boolean oneColumnMode) {
        if (m855a(0) || start < 0) {
            return false;
        }
        C0421a a = m855a(start);
        boolean z = false;
        while (true) {
            int numRows = getNumRows(a.f1577b);
            int i = Integer.MIN_VALUE;
            boolean z2 = z;
            while (a.f1576a < numRows) {
                if (a.f1577b < 0) {
                    return z2;
                }
                int a2 = this.a.mo911a(a, false, this.a);
                if (i == Integer.MIN_VALUE) {
                    i = m857a(a, numRows) - a2;
                }
                m848d(this.a[0], a.f1577b, a2, m856a(a), i);
                a.f1577b++;
                z2 = true;
                if (isOutRang(a.f1577b)) {
                    return true;
                }
                a.f1576a++;
            }
            if (oneColumnMode || m855a(0)) {
                return z2;
            }
            int firstAttachedPosition = this.a.getFirstAttachedPosition() - 1;
            if (isOutRang(firstAttachedPosition)) {
                return z2;
            }
            a = m855a(firstAttachedPosition);
            z = z2;
        }
    }

    private int m856a(C0421a c0421a) {
        if (c0421a.f1576a == 0) {
            return getPaddingLeft();
        }
        int i = c0421a.f1577b - 1;
        return ((this.a.mo947j(i) + this.a.mo916c(i)) + getHorizontalMargin()) + this.a.mo939i(c0421a.f1577b);
    }

    private C0421a m858a(int i) {
        C0421a c0421a = this.a;
        c0421a.f1577b = i - getColumn(i);
        c0421a.f1576a = 0;
        return c0421a;
    }

    private C0421a m861b(int i) {
        C0421a c0421a = this.a;
        if (this.a.getLastAttachedPosition() >= 0) {
            c0421a.f1577b = i;
            c0421a.f1576a = getColumn(i);
        } else {
            c0421a.f1577b = i - getColumn(i);
            c0421a.f1576a = 0;
        }
        return c0421a;
    }

    private int m857a(C0421a c0421a, int i) {
        if (getLastPosition() - c0421a.f1577b <= getColumn(getLastPosition())) {
            return getLayoutRegion().bottom - getPaddingBottom();
        }
        int minViewPosition = getMinViewPosition();
        return ((getViewMin(minViewPosition) - this.a.mo927g(minViewPosition)) - this.a.mo937h(c0421a.f1577b)) - getVerticalMargin();
    }

    private int m860b(C0421a c0421a) {
        if (this.a.getLastAttachedPosition() == -1 || this.a.getFirstAttachedPosition() == -1) {
            return c0421a.f1577b == 0 ? this.a.getPaddingMin() : 0;
        } else {
            if (c0421a.f1577b == getFirstPosition()) {
                return getLayoutRegion().top + getPaddingTop();
            }
            int maxViewPosition = getMaxViewPosition();
            return ((this.a.mo937h(maxViewPosition) + getViewMax(maxViewPosition)) + this.a.mo927g(c0421a.f1577b)) + getVerticalMargin();
        }
    }

    protected final boolean m862a(int i) {
        if (this.a.getLastAttachedPosition() >= 0 && m859b(this.a.getFirstAttachedPosition()) <= i - this.a.mo908a()) {
            return true;
        }
        return false;
    }

    protected boolean m863b(int i) {
        if (this.a.getLastAttachedPosition() < 0) {
            return false;
        }
        int lastAttachedPosition = this.a.getLastAttachedPosition();
        if (isOutRang(lastAttachedPosition)) {
            if (this.a.mo922e(lastAttachedPosition) < this.a.mo908a() + i) {
                return false;
            }
            return true;
        } else if (m855a(this.a.getLastAttachedPosition()) < this.a.mo908a() + i) {
            return false;
        } else {
            return true;
        }
    }

    private int m855a(int i) {
        int numRows = (i - getNumRows(i)) + 1;
        if (this.a.getFirstAttachedPosition() > numRows) {
            numRows = this.a.getFirstAttachedPosition();
        }
        return this.a.mo922e(numRows);
    }

    private int m859b(int i) {
        int numRows = (getNumRows(i) + i) - 1;
        if (numRows > this.a.getLastAttachedPosition()) {
            numRows = this.a.getLastAttachedPosition();
        }
        return this.a.mo919d(numRows);
    }

    public void setNumRowsController(NumRowsController numRowsController) {
        this.f1379a = numRowsController;
    }

    public int getColumn(int position) {
        if (this.f1379a != null) {
            return this.f1379a.getColumn(position - getFirstPosition());
        }
        return position % this.a;
    }

    public int getNumRows(int position) {
        int i = 0;
        if (this.f1379a != null) {
            i = this.f1379a.getNumRows(position - getFirstPosition());
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

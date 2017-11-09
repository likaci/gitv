package com.gala.video.albumlist4.widget;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.xbill.DNS.WKSRecord.Service;

public class C0465c {
    private static final ThreadLocal<C0465c> f1794a = new C04611();
    final Rect f1795a;
    private C0463a f1796a;
    final C0464b f1797a;
    private final ArrayList<View> f1798a;
    final Rect f1799b;
    final Rect f1800c;

    static class C04611 extends ThreadLocal<C0465c> {
        C04611() {
        }

        protected /* synthetic */ Object initialValue() {
            return m1389a();
        }

        protected C0465c m1389a() {
            return new C0465c();
        }
    }

    public enum C0463a {
        LEFT,
        CENTER,
        RIGHT;

        public static C0463a[] m1390a() {
            return (C0463a[]) f1787a.clone();
        }
    }

    private static final class C0464b implements Comparator<View> {
        private final Rect f1790a;
        private ViewGroup f1791a;
        private boolean f1792a;
        private final Rect f1793b;

        private C0464b() {
            this.f1790a = new Rect();
            this.f1793b = new Rect();
        }

        public /* synthetic */ int compare(Object x0, Object x1) {
            return m1392a((View) x0, (View) x1);
        }

        public void m1393a() {
            this.f1791a = null;
        }

        public void m1394a(ViewGroup viewGroup) {
            this.f1791a = viewGroup;
        }

        public void m1395a(boolean z) {
            this.f1792a = z;
        }

        public int m1392a(View view, View view2) {
            int i = 1;
            if (view == view2) {
                return 0;
            }
            m1391a(view, this.f1790a);
            m1391a(view2, this.f1793b);
            if (this.f1790a.top < this.f1793b.top) {
                return -1;
            }
            if (this.f1790a.top > this.f1793b.top) {
                return 1;
            }
            if (this.f1790a.left < this.f1793b.left) {
                if (!this.f1792a) {
                    i = -1;
                }
                return i;
            } else if (this.f1790a.left > this.f1793b.left) {
                if (this.f1792a) {
                    return -1;
                }
                return 1;
            } else if (this.f1790a.bottom < this.f1793b.bottom) {
                return -1;
            } else {
                if (this.f1790a.bottom > this.f1793b.bottom) {
                    return 1;
                }
                if (this.f1790a.right < this.f1793b.right) {
                    if (!this.f1792a) {
                        i = -1;
                    }
                    return i;
                } else if (this.f1790a.right <= this.f1793b.right) {
                    return 0;
                } else {
                    if (this.f1792a) {
                        return -1;
                    }
                    return 1;
                }
            }
        }

        private void m1391a(View view, Rect rect) {
            view.getDrawingRect(rect);
            this.f1791a.offsetDescendantRectToMyCoords(view, rect);
        }
    }

    public static C0465c m1401a() {
        return (C0465c) f1794a.get();
    }

    private C0465c() {
        this.f1795a = new Rect();
        this.f1799b = new Rect();
        this.f1800c = new Rect();
        this.f1797a = new C0464b();
        this.f1796a = C0463a.CENTER;
        this.f1798a = new ArrayList();
    }

    public final View m1411a(ViewGroup viewGroup, View view, int i, C0463a c0463a) {
        return m1399a(viewGroup, view, null, i, c0463a);
    }

    private View m1399a(ViewGroup viewGroup, View view, Rect rect, int i, C0463a c0463a) {
        View view2 = null;
        if (view != null) {
            view2 = m1398a(viewGroup, view, i);
        }
        if (view2 == null) {
            this.f1796a = c0463a;
            ArrayList arrayList = this.f1798a;
            try {
                arrayList.clear();
                viewGroup.addFocusables(arrayList, i);
                if (!arrayList.isEmpty()) {
                    view2 = m1400a(viewGroup, view, rect, i, arrayList);
                }
                arrayList.clear();
            } catch (Throwable th) {
                arrayList.clear();
            }
        }
        return view2;
    }

    private View m1398a(ViewGroup viewGroup, View view, int i) {
        View view2;
        try {
            Method declaredMethod = View.class.getDeclaredMethod("findUserSetNextFocus", new Class[]{View.class, Integer.TYPE});
            declaredMethod.setAccessible(true);
            view2 = (View) declaredMethod.invoke(view, new Object[]{viewGroup, Integer.valueOf(i)});
        } catch (Exception e) {
            e.printStackTrace();
            view2 = null;
        }
        if (view2 == null || !view2.isFocusable() || (view2.isInTouchMode() && !view2.isFocusableInTouchMode())) {
            return null;
        }
        return view2;
    }

    private View m1400a(ViewGroup viewGroup, View view, Rect rect, int i, ArrayList<View> arrayList) {
        Rect rect2;
        if (view != null) {
            if (rect == null) {
                rect = this.f1795a;
            }
            view.getFocusedRect(rect);
            viewGroup.offsetDescendantRectToMyCoords(view, rect);
            rect2 = rect;
        } else {
            if (rect == null) {
                rect = this.f1795a;
                switch (i) {
                    case 1:
                        m1402a(viewGroup, rect);
                        break;
                    case 2:
                        m1406b(viewGroup, rect);
                        rect2 = rect;
                        break;
                    case 17:
                    case 33:
                        m1402a(viewGroup, rect);
                        rect2 = rect;
                        break;
                    case 66:
                    case Service.CISCO_FNA /*130*/:
                        m1406b(viewGroup, rect);
                        rect2 = rect;
                        break;
                }
            }
            rect2 = rect;
        }
        switch (i) {
            case 1:
            case 2:
                return m1405b(arrayList, viewGroup, view, rect2, i);
            case 17:
            case 33:
            case 66:
            case Service.CISCO_FNA /*130*/:
                return m1412a((ArrayList) arrayList, viewGroup, view, rect2, i);
            default:
                throw new IllegalArgumentException("Unknown direction: " + i);
        }
    }

    private View m1405b(ArrayList<View> arrayList, ViewGroup viewGroup, View view, Rect rect, int i) {
        try {
            this.f1797a.m1394a(viewGroup);
            this.f1797a.m1395a(false);
            Collections.sort(arrayList, this.f1797a);
            int size = arrayList.size();
            switch (i) {
                case 1:
                    return C0465c.m1404b(view, (ArrayList) arrayList, size);
                case 2:
                    return C0465c.m1397a(view, (ArrayList) arrayList, size);
                default:
                    return (View) arrayList.get(size - 1);
            }
        } finally {
            this.f1797a.m1393a();
        }
    }

    private void m1402a(ViewGroup viewGroup, Rect rect) {
        int scrollY = viewGroup.getScrollY() + viewGroup.getHeight();
        int scrollX = viewGroup.getScrollX() + viewGroup.getWidth();
        rect.set(scrollX, scrollY, scrollX, scrollY);
    }

    private void m1406b(ViewGroup viewGroup, Rect rect) {
        int scrollY = viewGroup.getScrollY();
        int scrollX = viewGroup.getScrollX();
        rect.set(scrollX, scrollY, scrollX, scrollY);
    }

    View m1412a(ArrayList<View> arrayList, ViewGroup viewGroup, View view, Rect rect, int i) {
        this.f1800c.set(rect);
        switch (i) {
            case 17:
                this.f1800c.offset(rect.width() + 1, 0);
                break;
            case 33:
                this.f1800c.offset(0, rect.height() + 1);
                break;
            case 66:
                this.f1800c.offset(-(rect.width() + 1), 0);
                break;
            case Service.CISCO_FNA /*130*/:
                this.f1800c.offset(0, -(rect.height() + 1));
                break;
        }
        View view2 = null;
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            View view3 = (View) arrayList.get(i2);
            if (view3 != view) {
                if (view3 == viewGroup) {
                    view3 = view2;
                } else {
                    view3.getFocusedRect(this.f1799b);
                    viewGroup.offsetDescendantRectToMyCoords(view3, this.f1799b);
                    if (m1414a(i, rect, this.f1799b, this.f1800c)) {
                        this.f1800c.set(this.f1799b);
                    }
                }
                i2++;
                view2 = view3;
            }
            view3 = view2;
            i2++;
            view2 = view3;
        }
        return view2;
    }

    private static View m1397a(View view, ArrayList<View> arrayList, int i) {
        if (view != null) {
            int lastIndexOf = arrayList.lastIndexOf(view);
            if (lastIndexOf >= 0 && lastIndexOf + 1 < i) {
                return (View) arrayList.get(lastIndexOf + 1);
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return (View) arrayList.get(0);
    }

    private static View m1404b(View view, ArrayList<View> arrayList, int i) {
        if (view != null) {
            int indexOf = arrayList.indexOf(view);
            if (indexOf > 0) {
                return (View) arrayList.get(indexOf - 1);
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return (View) arrayList.get(i - 1);
    }

    boolean m1414a(int i, Rect rect, Rect rect2, Rect rect3) {
        if (!m1415a(rect, rect2, i)) {
            return false;
        }
        if (!m1415a(rect, rect3, i)) {
            return true;
        }
        if (m1417b(i, rect, rect2, rect3)) {
            return true;
        }
        if (m1417b(i, rect, rect3, rect2)) {
            return false;
        }
        if (m1418c(i, rect, rect2, rect3)) {
            return true;
        }
        int a = m1409a(m1410a(i, rect, rect2), m1419e(i, rect, rect2));
        int a2 = m1409a(m1410a(i, rect, rect3), m1419e(i, rect, rect3));
        if (a < 0 || a >= a2) {
            return false;
        }
        return true;
    }

    boolean m1417b(int i, Rect rect, Rect rect2, Rect rect3) {
        boolean a = m1410a(i, rect, rect2);
        if (m1410a(i, rect, rect3) || !a) {
            return false;
        }
        if (!m1403b(i, rect, rect3) || i == 17 || i == 66 || m1410a(i, rect, rect2) < C0465c.m1407c(i, rect, rect3)) {
            return true;
        }
        return false;
    }

    int m1409a(int i, int i2) {
        return ((i * 13) * i) + (i2 * i2);
    }

    boolean m1415a(Rect rect, Rect rect2, int i) {
        switch (i) {
            case 17:
                if ((rect.right > rect2.right || rect.left >= rect2.right) && rect.left > rect2.left) {
                    return true;
                }
                return false;
            case 33:
                if ((rect.bottom > rect2.bottom || rect.top >= rect2.bottom) && rect.top > rect2.top) {
                    return true;
                }
                return false;
            case 66:
                if ((rect.left < rect2.left || rect.right <= rect2.left) && rect.right < rect2.right) {
                    return true;
                }
                return false;
            case Service.CISCO_FNA /*130*/:
                return (rect.top < rect2.top || rect.bottom <= rect2.top) && rect.bottom < rect2.bottom;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    boolean m1418c(int i, Rect rect, Rect rect2, Rect rect3) {
        switch (i) {
            case 17:
                if (rect2.left < rect3.right || rect2.right > rect.left || rect2.top > rect3.top || rect2.bottom < rect3.bottom) {
                    return false;
                }
                return true;
            case 33:
                if (rect2.top < rect3.bottom || rect2.bottom > rect.top || rect2.left > rect3.left || rect2.right < rect3.right) {
                    return false;
                }
                return true;
            case 66:
                if (rect2.right > rect3.left || rect2.left < rect.right || rect2.top > rect3.top || rect2.bottom < rect3.bottom) {
                    return false;
                }
                return true;
            case Service.CISCO_FNA /*130*/:
                return rect2.bottom <= rect3.top && rect2.top >= rect.bottom && rect2.left <= rect3.left && rect2.right >= rect3.right;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    boolean m1413a(int i, Rect rect, Rect rect2) {
        switch (i) {
            case 17:
            case 66:
                if (rect2.bottom < rect.top || rect2.top > rect.bottom) {
                    return false;
                }
                return true;
            case 33:
            case Service.CISCO_FNA /*130*/:
                return rect2.right >= rect.left && rect2.left <= rect.right;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    boolean m1416b(int i, Rect rect, Rect rect2) {
        switch (i) {
            case 17:
                if (rect.left >= rect2.right) {
                    return true;
                }
                return false;
            case 33:
                if (rect.top < rect2.bottom) {
                    return false;
                }
                return true;
            case 66:
                if (rect.right > rect2.left) {
                    return false;
                }
                return true;
            case Service.CISCO_FNA /*130*/:
                return rect.bottom <= rect2.top;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    int m1410a(int i, Rect rect, Rect rect2) {
        return Math.max(0, C0465c.m1403b(i, rect, rect2));
    }

    static int m1403b(int i, Rect rect, Rect rect2) {
        switch (i) {
            case 17:
                return rect.left - rect2.right;
            case 33:
                return rect.top - rect2.bottom;
            case 66:
                return rect2.left - rect.right;
            case Service.CISCO_FNA /*130*/:
                return rect2.top - rect.bottom;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    static int m1407c(int i, Rect rect, Rect rect2) {
        return Math.max(1, C0465c.m1408d(i, rect, rect2));
    }

    static int m1408d(int i, Rect rect, Rect rect2) {
        switch (i) {
            case 17:
                return rect.left - rect2.left;
            case 33:
                return rect.top - rect2.top;
            case 66:
                return rect2.right - rect.right;
            case Service.CISCO_FNA /*130*/:
                return rect2.bottom - rect.bottom;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    int m1419e(int i, Rect rect, Rect rect2) {
        switch (i) {
            case 17:
            case 66:
                return Math.abs((rect.top + (rect.height() / 2)) - (rect2.top + (rect2.height() / 2)));
            case 33:
            case Service.CISCO_FNA /*130*/:
                return m1396a(rect, rect2);
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    private int m1396a(Rect rect, Rect rect2) {
        switch (this.f1796a) {
            case LEFT:
                return Math.abs(rect.left - rect2.left);
            case RIGHT:
                return Math.abs(rect.right - rect2.right);
            case CENTER:
                return Math.abs((rect.left + (rect.width() / 2)) - (rect2.left + (rect2.width() / 2)));
            default:
                throw new IllegalArgumentException("align must be one of {LEFT, RIGHT, CENTER}.");
        }
    }
}

package com.gala.video.albumlist.widget;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.xbill.DNS.WKSRecord.Service;

public class C0420c {
    private static final ThreadLocal<C0420c> f1569a = new C04161();
    final Rect f1570a;
    private C0418a f1571a;
    final C0419b f1572a;
    private final ArrayList<View> f1573a;
    final Rect f1574b;
    final Rect f1575c;

    static class C04161 extends ThreadLocal<C0420c> {
        C04161() {
        }

        protected /* synthetic */ Object initialValue() {
            return m1069a();
        }

        protected C0420c m1069a() {
            return new C0420c();
        }
    }

    public enum C0418a {
        LEFT,
        CENTER,
        RIGHT;

        public static C0418a[] m1070a() {
            return (C0418a[]) f1562a.clone();
        }
    }

    private static final class C0419b implements Comparator<View> {
        private final Rect f1565a;
        private ViewGroup f1566a;
        private boolean f1567a;
        private final Rect f1568b;

        private C0419b() {
            this.f1565a = new Rect();
            this.f1568b = new Rect();
        }

        public /* synthetic */ int compare(Object x0, Object x1) {
            return m1072a((View) x0, (View) x1);
        }

        public void m1073a() {
            this.f1566a = null;
        }

        public void m1074a(ViewGroup viewGroup) {
            this.f1566a = viewGroup;
        }

        public void m1075a(boolean z) {
            this.f1567a = z;
        }

        public int m1072a(View view, View view2) {
            int i = 1;
            if (view == view2) {
                return 0;
            }
            m1071a(view, this.f1565a);
            m1071a(view2, this.f1568b);
            if (this.f1565a.top < this.f1568b.top) {
                return -1;
            }
            if (this.f1565a.top > this.f1568b.top) {
                return 1;
            }
            if (this.f1565a.left < this.f1568b.left) {
                if (!this.f1567a) {
                    i = -1;
                }
                return i;
            } else if (this.f1565a.left > this.f1568b.left) {
                if (this.f1567a) {
                    return -1;
                }
                return 1;
            } else if (this.f1565a.bottom < this.f1568b.bottom) {
                return -1;
            } else {
                if (this.f1565a.bottom > this.f1568b.bottom) {
                    return 1;
                }
                if (this.f1565a.right < this.f1568b.right) {
                    if (!this.f1567a) {
                        i = -1;
                    }
                    return i;
                } else if (this.f1565a.right <= this.f1568b.right) {
                    return 0;
                } else {
                    if (this.f1567a) {
                        return -1;
                    }
                    return 1;
                }
            }
        }

        private void m1071a(View view, Rect rect) {
            view.getDrawingRect(rect);
            this.f1566a.offsetDescendantRectToMyCoords(view, rect);
        }
    }

    public static C0420c m1080a() {
        return (C0420c) f1569a.get();
    }

    private C0420c() {
        this.f1570a = new Rect();
        this.f1574b = new Rect();
        this.f1575c = new Rect();
        this.f1572a = new C0419b();
        this.f1571a = C0418a.CENTER;
        this.f1573a = new ArrayList();
    }

    public final View m1092a(ViewGroup viewGroup, View view, int i, C0418a c0418a) {
        return m1078a(viewGroup, view, null, i, c0418a);
    }

    private View m1078a(ViewGroup viewGroup, View view, Rect rect, int i, C0418a c0418a) {
        View view2 = null;
        if (view != null) {
            view2 = m1083b((View) viewGroup, view, i);
        }
        if (view2 == null) {
            this.f1571a = c0418a;
            ArrayList arrayList = this.f1573a;
            try {
                arrayList.clear();
                viewGroup.addFocusables(arrayList, i);
                if (!arrayList.isEmpty()) {
                    view2 = m1079a(viewGroup, view, rect, i, arrayList);
                }
                arrayList.clear();
            } catch (Throwable th) {
                arrayList.clear();
            }
        }
        return view2;
    }

    private View m1083b(View view, View view2, int i) {
        switch (i) {
            case 17:
                if (view2.getNextFocusLeftId() != -1) {
                    return m1091a(view, view2, view2.getNextFocusLeftId());
                }
                return null;
            case 33:
                if (view2.getNextFocusUpId() != -1) {
                    return m1091a(view, view2, view2.getNextFocusUpId());
                }
                return null;
            case 66:
                if (view2.getNextFocusRightId() != -1) {
                    return m1091a(view, view2, view2.getNextFocusRightId());
                }
                return null;
            case Service.CISCO_FNA /*130*/:
                return view2.getNextFocusDownId() != -1 ? m1091a(view, view2, view2.getNextFocusDownId()) : null;
            default:
                return null;
        }
    }

    public final View m1091a(View view, View view2, int i) {
        while (true) {
            View findViewById = view2.findViewById(i);
            if (findViewById != null || view2 == view) {
                return findViewById;
            }
            ViewParent parent = view2.getParent();
            if (parent != null && (parent instanceof View)) {
                view2 = (View) parent;
            }
        }
        return null;
    }

    private View m1079a(ViewGroup viewGroup, View view, Rect rect, int i, ArrayList<View> arrayList) {
        Rect rect2;
        if (view != null) {
            if (rect == null) {
                rect = this.f1570a;
            }
            view.getFocusedRect(rect);
            viewGroup.offsetDescendantRectToMyCoords(view, rect);
            rect2 = rect;
        } else {
            if (rect == null) {
                rect = this.f1570a;
                switch (i) {
                    case 1:
                        m1081a(viewGroup, rect);
                        break;
                    case 2:
                        m1086b(viewGroup, rect);
                        rect2 = rect;
                        break;
                    case 17:
                    case 33:
                        m1081a(viewGroup, rect);
                        rect2 = rect;
                        break;
                    case 66:
                    case Service.CISCO_FNA /*130*/:
                        m1086b(viewGroup, rect);
                        rect2 = rect;
                        break;
                }
            }
            rect2 = rect;
        }
        switch (i) {
            case 1:
            case 2:
                return m1085b(arrayList, viewGroup, view, rect2, i);
            case 17:
            case 33:
            case 66:
            case Service.CISCO_FNA /*130*/:
                return m1093a((ArrayList) arrayList, viewGroup, view, rect2, i);
            default:
                throw new IllegalArgumentException("Unknown direction: " + i);
        }
    }

    private View m1085b(ArrayList<View> arrayList, ViewGroup viewGroup, View view, Rect rect, int i) {
        try {
            this.f1572a.m1074a(viewGroup);
            this.f1572a.m1075a(false);
            Collections.sort(arrayList, this.f1572a);
            int size = arrayList.size();
            switch (i) {
                case 1:
                    return C0420c.m1084b(view, (ArrayList) arrayList, size);
                case 2:
                    return C0420c.m1077a(view, (ArrayList) arrayList, size);
                default:
                    return (View) arrayList.get(size - 1);
            }
        } finally {
            this.f1572a.m1073a();
        }
    }

    private void m1081a(ViewGroup viewGroup, Rect rect) {
        int scrollY = viewGroup.getScrollY() + viewGroup.getHeight();
        int scrollX = viewGroup.getScrollX() + viewGroup.getWidth();
        rect.set(scrollX, scrollY, scrollX, scrollY);
    }

    private void m1086b(ViewGroup viewGroup, Rect rect) {
        int scrollY = viewGroup.getScrollY();
        int scrollX = viewGroup.getScrollX();
        rect.set(scrollX, scrollY, scrollX, scrollY);
    }

    View m1093a(ArrayList<View> arrayList, ViewGroup viewGroup, View view, Rect rect, int i) {
        this.f1575c.set(rect);
        switch (i) {
            case 17:
                this.f1575c.offset(rect.width() + 1, 0);
                break;
            case 33:
                this.f1575c.offset(0, rect.height() + 1);
                break;
            case 66:
                this.f1575c.offset(-(rect.width() + 1), 0);
                break;
            case Service.CISCO_FNA /*130*/:
                this.f1575c.offset(0, -(rect.height() + 1));
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
                    view3.getFocusedRect(this.f1574b);
                    viewGroup.offsetDescendantRectToMyCoords(view3, this.f1574b);
                    if (m1095a(i, rect, this.f1574b, this.f1575c)) {
                        this.f1575c.set(this.f1574b);
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

    private static View m1077a(View view, ArrayList<View> arrayList, int i) {
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

    private static View m1084b(View view, ArrayList<View> arrayList, int i) {
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

    boolean m1095a(int i, Rect rect, Rect rect2, Rect rect3) {
        if (!m1096a(rect, rect2, i)) {
            return false;
        }
        if (!m1096a(rect, rect3, i)) {
            return true;
        }
        if (m1098b(i, rect, rect2, rect3)) {
            return true;
        }
        if (m1098b(i, rect, rect3, rect2) || m1100d(i, rect, rect2, rect3)) {
            return false;
        }
        if (m1099c(i, rect, rect2, rect3)) {
            return true;
        }
        int a = m1089a(m1090a(i, rect, rect2), m1101e(i, rect, rect2));
        int a2 = m1089a(m1090a(i, rect, rect3), m1101e(i, rect, rect3));
        if (a < 0 || a >= a2) {
            return false;
        }
        return true;
    }

    boolean m1098b(int i, Rect rect, Rect rect2, Rect rect3) {
        boolean a = m1090a(i, rect, rect2);
        if (m1090a(i, rect, rect3) || !a) {
            return false;
        }
        if (!m1082b(i, rect, rect3) || i == 17 || i == 66 || m1090a(i, rect, rect2) < C0420c.m1087c(i, rect, rect3)) {
            return true;
        }
        return false;
    }

    int m1089a(int i, int i2) {
        return ((i * 13) * i) + (i2 * i2);
    }

    boolean m1096a(Rect rect, Rect rect2, int i) {
        switch (i) {
            case 17:
                if ((rect.right > rect2.right || rect.left >= rect2.right) && rect2.top >= rect.top && rect2.bottom <= rect.bottom && rect.left > rect2.left) {
                    return true;
                }
                return false;
            case 33:
                if ((rect.bottom > rect2.bottom || rect.top >= rect2.bottom) && rect.top > rect2.top) {
                    return true;
                }
                return false;
            case 66:
                if ((rect.left < rect2.left || rect.right <= rect2.left) && rect2.top >= rect.top && rect2.bottom <= rect.bottom && rect.right < rect2.right) {
                    return true;
                }
                return false;
            case Service.CISCO_FNA /*130*/:
                return (rect.top < rect2.top || rect.bottom <= rect2.top) && rect.bottom < rect2.bottom;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    boolean m1099c(int i, Rect rect, Rect rect2, Rect rect3) {
        switch (i) {
            case 17:
                if (rect2.left < rect3.right || rect2.right > rect.left) {
                    return false;
                }
                return true;
            case 33:
                if (rect2.top < rect3.bottom || rect2.bottom > rect.top) {
                    return false;
                }
                return true;
            case 66:
                if (rect2.right > rect3.left || rect2.left < rect.right) {
                    return false;
                }
                return true;
            case Service.CISCO_FNA /*130*/:
                return rect2.bottom <= rect3.top && rect2.top >= rect.bottom;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    boolean m1100d(int i, Rect rect, Rect rect2, Rect rect3) {
        switch (i) {
            case 17:
                if (rect2.right <= rect3.left) {
                    return true;
                }
                return false;
            case 33:
                if (rect2.bottom > rect3.top) {
                    return false;
                }
                return true;
            case 66:
                if (rect2.left < rect3.right) {
                    return false;
                }
                return true;
            case Service.CISCO_FNA /*130*/:
                return rect2.top >= rect3.bottom;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    boolean m1094a(int i, Rect rect, Rect rect2) {
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

    boolean m1097b(int i, Rect rect, Rect rect2) {
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

    int m1090a(int i, Rect rect, Rect rect2) {
        return Math.max(0, C0420c.m1082b(i, rect, rect2));
    }

    static int m1082b(int i, Rect rect, Rect rect2) {
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

    static int m1087c(int i, Rect rect, Rect rect2) {
        return Math.max(1, C0420c.m1088d(i, rect, rect2));
    }

    static int m1088d(int i, Rect rect, Rect rect2) {
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

    int m1101e(int i, Rect rect, Rect rect2) {
        switch (i) {
            case 17:
            case 66:
                return Math.abs((rect.top + (rect.height() / 2)) - (rect2.top + (rect2.height() / 2)));
            case 33:
            case Service.CISCO_FNA /*130*/:
                return m1076a(rect, rect2);
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    private int m1076a(Rect rect, Rect rect2) {
        switch (this.f1571a) {
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

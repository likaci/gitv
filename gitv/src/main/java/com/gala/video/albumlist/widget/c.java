package com.gala.video.albumlist.widget;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.xbill.DNS.WKSRecord.Service;

public class c {
    private static final ThreadLocal<c> a = new ThreadLocal<c>() {
        protected /* synthetic */ Object initialValue() {
            return a();
        }

        protected c a() {
            return new c();
        }
    };
    final Rect f632a;
    private a f633a;
    final b f634a;
    private final ArrayList<View> f635a;
    final Rect b;
    final Rect c;

    public enum a {
        LEFT,
        CENTER,
        RIGHT;

        public static a[] a() {
            return (a[]) a.clone();
        }
    }

    private static final class b implements Comparator<View> {
        private final Rect a;
        private ViewGroup f636a;
        private boolean f637a;
        private final Rect b;

        private b() {
            this.a = new Rect();
            this.b = new Rect();
        }

        public /* synthetic */ int compare(Object x0, Object x1) {
            return a((View) x0, (View) x1);
        }

        public void a() {
            this.f636a = null;
        }

        public void a(ViewGroup viewGroup) {
            this.f636a = viewGroup;
        }

        public void a(boolean z) {
            this.f637a = z;
        }

        public int a(View view, View view2) {
            int i = 1;
            if (view == view2) {
                return 0;
            }
            a(view, this.a);
            a(view2, this.b);
            if (this.a.top < this.b.top) {
                return -1;
            }
            if (this.a.top > this.b.top) {
                return 1;
            }
            if (this.a.left < this.b.left) {
                if (!this.f637a) {
                    i = -1;
                }
                return i;
            } else if (this.a.left > this.b.left) {
                if (this.f637a) {
                    return -1;
                }
                return 1;
            } else if (this.a.bottom < this.b.bottom) {
                return -1;
            } else {
                if (this.a.bottom > this.b.bottom) {
                    return 1;
                }
                if (this.a.right < this.b.right) {
                    if (!this.f637a) {
                        i = -1;
                    }
                    return i;
                } else if (this.a.right <= this.b.right) {
                    return 0;
                } else {
                    if (this.f637a) {
                        return -1;
                    }
                    return 1;
                }
            }
        }

        private void a(View view, Rect rect) {
            view.getDrawingRect(rect);
            this.f636a.offsetDescendantRectToMyCoords(view, rect);
        }
    }

    public static c a() {
        return (c) a.get();
    }

    private c() {
        this.f632a = new Rect();
        this.b = new Rect();
        this.c = new Rect();
        this.f634a = new b();
        this.f633a = a.CENTER;
        this.f635a = new ArrayList();
    }

    public final View a(ViewGroup viewGroup, View view, int i, a aVar) {
        return a(viewGroup, view, null, i, aVar);
    }

    private View a(ViewGroup viewGroup, View view, Rect rect, int i, a aVar) {
        View view2 = null;
        if (view != null) {
            view2 = b((View) viewGroup, view, i);
        }
        if (view2 == null) {
            this.f633a = aVar;
            ArrayList arrayList = this.f635a;
            try {
                arrayList.clear();
                viewGroup.addFocusables(arrayList, i);
                if (!arrayList.isEmpty()) {
                    view2 = a(viewGroup, view, rect, i, arrayList);
                }
                arrayList.clear();
            } catch (Throwable th) {
                arrayList.clear();
            }
        }
        return view2;
    }

    private View b(View view, View view2, int i) {
        switch (i) {
            case 17:
                if (view2.getNextFocusLeftId() != -1) {
                    return a(view, view2, view2.getNextFocusLeftId());
                }
                return null;
            case 33:
                if (view2.getNextFocusUpId() != -1) {
                    return a(view, view2, view2.getNextFocusUpId());
                }
                return null;
            case 66:
                if (view2.getNextFocusRightId() != -1) {
                    return a(view, view2, view2.getNextFocusRightId());
                }
                return null;
            case Service.CISCO_FNA /*130*/:
                return view2.getNextFocusDownId() != -1 ? a(view, view2, view2.getNextFocusDownId()) : null;
            default:
                return null;
        }
    }

    public final View a(View view, View view2, int i) {
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

    private View a(ViewGroup viewGroup, View view, Rect rect, int i, ArrayList<View> arrayList) {
        Rect rect2;
        if (view != null) {
            if (rect == null) {
                rect = this.f632a;
            }
            view.getFocusedRect(rect);
            viewGroup.offsetDescendantRectToMyCoords(view, rect);
            rect2 = rect;
        } else {
            if (rect == null) {
                rect = this.f632a;
                switch (i) {
                    case 1:
                        a(viewGroup, rect);
                        break;
                    case 2:
                        b(viewGroup, rect);
                        rect2 = rect;
                        break;
                    case 17:
                    case 33:
                        a(viewGroup, rect);
                        rect2 = rect;
                        break;
                    case 66:
                    case Service.CISCO_FNA /*130*/:
                        b(viewGroup, rect);
                        rect2 = rect;
                        break;
                }
            }
            rect2 = rect;
        }
        switch (i) {
            case 1:
            case 2:
                return b(arrayList, viewGroup, view, rect2, i);
            case 17:
            case 33:
            case 66:
            case Service.CISCO_FNA /*130*/:
                return a((ArrayList) arrayList, viewGroup, view, rect2, i);
            default:
                throw new IllegalArgumentException("Unknown direction: " + i);
        }
    }

    private View b(ArrayList<View> arrayList, ViewGroup viewGroup, View view, Rect rect, int i) {
        try {
            this.f634a.a(viewGroup);
            this.f634a.a(false);
            Collections.sort(arrayList, this.f634a);
            int size = arrayList.size();
            switch (i) {
                case 1:
                    return b(view, (ArrayList) arrayList, size);
                case 2:
                    return a(view, (ArrayList) arrayList, size);
                default:
                    return (View) arrayList.get(size - 1);
            }
        } finally {
            this.f634a.a();
        }
    }

    private void a(ViewGroup viewGroup, Rect rect) {
        int scrollY = viewGroup.getScrollY() + viewGroup.getHeight();
        int scrollX = viewGroup.getScrollX() + viewGroup.getWidth();
        rect.set(scrollX, scrollY, scrollX, scrollY);
    }

    private void b(ViewGroup viewGroup, Rect rect) {
        int scrollY = viewGroup.getScrollY();
        int scrollX = viewGroup.getScrollX();
        rect.set(scrollX, scrollY, scrollX, scrollY);
    }

    View a(ArrayList<View> arrayList, ViewGroup viewGroup, View view, Rect rect, int i) {
        this.c.set(rect);
        switch (i) {
            case 17:
                this.c.offset(rect.width() + 1, 0);
                break;
            case 33:
                this.c.offset(0, rect.height() + 1);
                break;
            case 66:
                this.c.offset(-(rect.width() + 1), 0);
                break;
            case Service.CISCO_FNA /*130*/:
                this.c.offset(0, -(rect.height() + 1));
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
                    view3.getFocusedRect(this.b);
                    viewGroup.offsetDescendantRectToMyCoords(view3, this.b);
                    if (a(i, rect, this.b, this.c)) {
                        this.c.set(this.b);
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

    private static View a(View view, ArrayList<View> arrayList, int i) {
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

    private static View b(View view, ArrayList<View> arrayList, int i) {
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

    boolean a(int i, Rect rect, Rect rect2, Rect rect3) {
        if (!a(rect, rect2, i)) {
            return false;
        }
        if (!a(rect, rect3, i)) {
            return true;
        }
        if (b(i, rect, rect2, rect3)) {
            return true;
        }
        if (b(i, rect, rect3, rect2) || d(i, rect, rect2, rect3)) {
            return false;
        }
        if (c(i, rect, rect2, rect3)) {
            return true;
        }
        int a = a(a(i, rect, rect2), e(i, rect, rect2));
        int a2 = a(a(i, rect, rect3), e(i, rect, rect3));
        if (a < 0 || a >= a2) {
            return false;
        }
        return true;
    }

    boolean b(int i, Rect rect, Rect rect2, Rect rect3) {
        boolean a = a(i, rect, rect2);
        if (a(i, rect, rect3) || !a) {
            return false;
        }
        if (!b(i, rect, rect3) || i == 17 || i == 66 || a(i, rect, rect2) < c(i, rect, rect3)) {
            return true;
        }
        return false;
    }

    int a(int i, int i2) {
        return ((i * 13) * i) + (i2 * i2);
    }

    boolean a(Rect rect, Rect rect2, int i) {
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

    boolean c(int i, Rect rect, Rect rect2, Rect rect3) {
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

    boolean d(int i, Rect rect, Rect rect2, Rect rect3) {
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

    boolean m132a(int i, Rect rect, Rect rect2) {
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

    boolean m133b(int i, Rect rect, Rect rect2) {
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

    int a(int i, Rect rect, Rect rect2) {
        return Math.max(0, b(i, rect, rect2));
    }

    static int b(int i, Rect rect, Rect rect2) {
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

    static int c(int i, Rect rect, Rect rect2) {
        return Math.max(1, d(i, rect, rect2));
    }

    static int d(int i, Rect rect, Rect rect2) {
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

    int e(int i, Rect rect, Rect rect2) {
        switch (i) {
            case 17:
            case 66:
                return Math.abs((rect.top + (rect.height() / 2)) - (rect2.top + (rect2.height() / 2)));
            case 33:
            case Service.CISCO_FNA /*130*/:
                return a(rect, rect2);
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    private int a(Rect rect, Rect rect2) {
        switch (this.f633a) {
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

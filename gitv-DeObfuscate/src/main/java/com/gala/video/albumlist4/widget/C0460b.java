package com.gala.video.albumlist4.widget;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.view.View;
import android.view.ViewPropertyAnimator;
import com.gala.video.albumlist4.widget.RecyclerView.C0445c;
import com.gala.video.albumlist4.widget.RecyclerView.C0445c.C0444b;
import com.gala.video.albumlist4.widget.RecyclerView.C0446d;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@TargetApi(14)
public class C0460b extends C0445c {
    private ArrayList<ViewHolder> f1779a = new ArrayList();
    private ArrayList<ViewHolder> f1780b = new ArrayList();
    private ArrayList<C0459a> f1781c = new ArrayList();
    private ArrayList<ViewHolder> f1782d = new ArrayList();
    private ArrayList<ViewHolder> f1783e = new ArrayList();
    private ArrayList<ViewHolder> f1784f = new ArrayList();

    class C04541 implements Runnable {
        final /* synthetic */ C0460b f1758a;

        C04541(C0460b c0460b) {
            this.f1758a = c0460b;
        }

        public void run() {
            Iterator it = this.f1758a.f1781c.iterator();
            while (it.hasNext()) {
                C0459a c0459a = (C0459a) it.next();
                this.f1758a.m1370b(c0459a.f1775a, c0459a.f1774a, c0459a.f1776b, c0459a.f1777c, c0459a.f1778d);
            }
            this.f1758a.f1781c.clear();
        }
    }

    class C04552 implements Runnable {
        final /* synthetic */ C0460b f1759a;

        C04552(C0460b c0460b) {
            this.f1759a = c0460b;
        }

        public void run() {
            Iterator it = this.f1759a.f1780b.iterator();
            while (it.hasNext()) {
                this.f1759a.m1369b((ViewHolder) it.next());
            }
            this.f1759a.f1780b.clear();
        }
    }

    private static class C0459a {
        public int f1774a;
        public ViewHolder f1775a;
        public int f1776b;
        public int f1777c;
        public int f1778d;

        private C0459a(ViewHolder viewHolder, int i, int i2, int i3, int i4) {
            this.f1775a = viewHolder;
            this.f1774a = i;
            this.f1776b = i2;
            this.f1777c = i3;
            this.f1778d = i4;
        }
    }

    public void mo990a(ViewHolder viewHolder, C0446d c0446d, C0446d c0446d2) {
        viewHolder.itemView.setAlpha(0.0f);
        this.f1780b.add(viewHolder);
    }

    public void mo993b(ViewHolder viewHolder, C0446d c0446d, C0446d c0446d2) {
        this.f1779a.add(viewHolder);
    }

    public void mo995c(ViewHolder viewHolder, C0446d c0446d, C0446d c0446d2) {
        m1378a(viewHolder, c0446d.f1711a, c0446d.f1712b, c0446d2.f1711a, c0446d2.f1712b);
    }

    public void m1377a() {
        int i;
        int i2;
        int i3 = !this.f1779a.isEmpty() ? 1 : 0;
        if (this.f1781c.isEmpty()) {
            i = 0;
        } else {
            i = 1;
        }
        if (this.f1780b.isEmpty()) {
            i2 = 0;
        } else {
            i2 = 1;
        }
        if (i3 != 0 || i != 0 || i2 != 0) {
            Iterator it = this.f1779a.iterator();
            while (it.hasNext()) {
                m1365a((ViewHolder) it.next());
            }
            this.f1779a.clear();
            if (i != 0) {
                Runnable c04541 = new C04541(this);
                if (i3 != 0) {
                    ((C0459a) this.f1781c.get(0)).f1775a.itemView.postDelayed(c04541, mo992b());
                } else {
                    c04541.run();
                }
            }
            if (i2 != 0) {
                Runnable c04552 = new C04552(this);
                if (i3 == 0 && i == 0) {
                    c04552.run();
                    return;
                }
                long a;
                long b = i3 != 0 ? mo992b() : 0;
                if (i != 0) {
                    a = mo989a();
                } else {
                    a = 0;
                }
                ((ViewHolder) this.f1780b.get(0)).itemView.postDelayed(c04552, b + a);
            }
        }
    }

    private void m1365a(final ViewHolder viewHolder) {
        final View view = viewHolder.itemView;
        final ViewPropertyAnimator animate = view.animate();
        animate.setDuration(mo992b()).alpha(0.0f).setListener(new C0444b(this) {
            final /* synthetic */ C0460b f1763a;

            public void onAnimationEnd(Animator animator) {
                animate.setListener(null);
                view.setAlpha(1.0f);
                this.f1763a.f1784f.remove(viewHolder);
                this.f1763a.m1373c(viewHolder);
            }
        }).start();
        this.f1784f.add(viewHolder);
    }

    private void m1369b(final ViewHolder viewHolder) {
        final View view = viewHolder.itemView;
        final ViewPropertyAnimator animate = view.animate();
        animate.alpha(1.0f).setDuration(mo994c()).setListener(new C0444b(this) {
            final /* synthetic */ C0460b f1767a;

            public void onAnimationCancel(Animator animator) {
                view.setAlpha(1.0f);
            }

            public void onAnimationEnd(Animator animator) {
                animate.setListener(null);
                this.f1767a.f1782d.remove(viewHolder);
                this.f1767a.m1373c(viewHolder);
            }
        }).start();
        this.f1782d.add(viewHolder);
    }

    public void m1378a(ViewHolder viewHolder, int i, int i2, int i3, int i4) {
        View view = viewHolder.itemView;
        int translationX = (int) (((float) i) + viewHolder.itemView.getTranslationX());
        int translationY = (int) (((float) i2) + viewHolder.itemView.getTranslationY());
        int i5 = i3 - translationX;
        int i6 = i4 - translationY;
        if (i5 != 0 || i6 != 0) {
            if (i5 != 0) {
                view.setTranslationX((float) (-i5));
            }
            if (i6 != 0) {
                view.setTranslationY((float) (-i6));
            }
            if (!(i5 == 0 || i6 == 0)) {
                viewHolder.itemView.bringToFront();
            }
            this.f1781c.add(new C0459a(viewHolder, translationX, translationY, i3, i4));
        }
    }

    private void m1370b(ViewHolder viewHolder, int i, int i2, int i3, int i4) {
        final View view = viewHolder.itemView;
        final int i5 = i3 - i;
        final int i6 = i4 - i2;
        if (i5 != 0) {
            view.animate().translationX(0.0f);
        }
        if (i6 != 0) {
            view.animate().translationY(0.0f);
        }
        final ViewPropertyAnimator animate = view.animate();
        this.f1783e.add(viewHolder);
        final ViewHolder viewHolder2 = viewHolder;
        animate.setDuration(mo989a()).setListener(new C0444b(this) {
            final /* synthetic */ C0460b f1772a;

            public void onAnimationCancel(Animator animator) {
                if (i5 != 0) {
                    view.setTranslationX(0.0f);
                }
                if (i6 != 0) {
                    view.setTranslationY(0.0f);
                }
            }

            public void onAnimationEnd(Animator animator) {
                animate.setListener(null);
                this.f1772a.f1783e.remove(viewHolder2);
                this.f1772a.m1373c(viewHolder2);
            }
        }).start();
    }

    private final void m1373c(ViewHolder viewHolder) {
        if (this.a != null) {
            this.a.mo985a(viewHolder);
            if (!mo989a()) {
                this.a.mo984a();
            }
        }
    }

    public final void m1384b() {
        if (this.a != null && !mo989a()) {
            this.a.mo984a();
        }
    }

    public boolean m1382a() {
        return (this.f1780b.isEmpty() && this.f1781c.isEmpty() && this.f1779a.isEmpty() && this.f1783e.isEmpty() && this.f1784f.isEmpty() && this.f1782d.isEmpty()) ? false : true;
    }

    public void m1387c() {
        int size;
        for (size = this.f1781c.size() - 1; size >= 0; size--) {
            C0459a c0459a = (C0459a) this.f1781c.get(size);
            View view = c0459a.f1775a.itemView;
            view.setTranslationY(0.0f);
            view.setTranslationX(0.0f);
            this.f1781c.remove(size);
            m1373c(c0459a.f1775a);
        }
        for (size = this.f1779a.size() - 1; size >= 0; size--) {
            ViewHolder viewHolder = (ViewHolder) this.f1779a.get(size);
            this.f1779a.remove(size);
            m1373c(viewHolder);
        }
        for (size = this.f1780b.size() - 1; size >= 0; size--) {
            viewHolder = (ViewHolder) this.f1780b.get(size);
            viewHolder.itemView.setAlpha(1.0f);
            this.f1780b.remove(size);
            m1373c(viewHolder);
        }
        if (mo989a()) {
            m1381a(this.f1784f);
            m1381a(this.f1783e);
            m1381a(this.f1782d);
            mo992b();
        }
    }

    void m1381a(List<ViewHolder> list) {
        for (int size = list.size() - 1; size >= 0; size--) {
            ((ViewHolder) list.get(size)).itemView.animate().cancel();
        }
    }
}

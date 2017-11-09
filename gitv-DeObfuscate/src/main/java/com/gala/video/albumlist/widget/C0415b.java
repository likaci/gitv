package com.gala.video.albumlist.widget;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.view.View;
import android.view.ViewPropertyAnimator;
import com.gala.video.albumlist.widget.BlocksView.C0399c;
import com.gala.video.albumlist.widget.BlocksView.C0399c.C0398b;
import com.gala.video.albumlist.widget.BlocksView.C0400d;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@TargetApi(14)
public class C0415b extends C0399c {
    private ArrayList<ViewHolder> f1554a = new ArrayList();
    private ArrayList<ViewHolder> f1555b = new ArrayList();
    private ArrayList<C0414a> f1556c = new ArrayList();
    private ArrayList<ViewHolder> f1557d = new ArrayList();
    private ArrayList<ViewHolder> f1558e = new ArrayList();
    private ArrayList<ViewHolder> f1559f = new ArrayList();

    class C04081 implements Runnable {
        final /* synthetic */ C0415b f1532a;

        C04081(C0415b c0415b) {
            this.f1532a = c0415b;
        }

        public void run() {
            Iterator it = this.f1532a.f1556c.iterator();
            while (it.hasNext()) {
                C0414a c0414a = (C0414a) it.next();
                this.f1532a.m1050b(c0414a.f1550a, c0414a.f1549a, c0414a.f1551b, c0414a.f1552c, c0414a.f1553d);
            }
            this.f1532a.f1556c.clear();
        }
    }

    class C04092 implements Runnable {
        final /* synthetic */ C0415b f1533a;

        C04092(C0415b c0415b) {
            this.f1533a = c0415b;
        }

        public void run() {
            Iterator it = this.f1533a.f1555b.iterator();
            while (it.hasNext()) {
                this.f1533a.m1049b((ViewHolder) it.next());
            }
            this.f1533a.f1555b.clear();
        }
    }

    private class C0410b extends C0398b {
        final /* synthetic */ C0415b f1534b;

        private C0410b(C0415b c0415b) {
            this.f1534b = c0415b;
        }

        public void onAnimationCancel(Animator animator) {
            mo900a(true);
        }

        public void onAnimationEnd(Animator animator) {
            mo900a(false);
        }

        public void mo900a(boolean z) {
        }
    }

    private static class C0414a {
        public int f1549a;
        public ViewHolder f1550a;
        public int f1551b;
        public int f1552c;
        public int f1553d;

        private C0414a(ViewHolder viewHolder, int i, int i2, int i3, int i4) {
            this.f1550a = viewHolder;
            this.f1549a = i;
            this.f1551b = i2;
            this.f1552c = i3;
            this.f1553d = i4;
        }
    }

    public void mo902a(ViewHolder viewHolder, C0400d c0400d, C0400d c0400d2) {
        viewHolder.itemView.setAlpha(0.0f);
        this.f1555b.add(viewHolder);
    }

    public void mo905b(ViewHolder viewHolder, C0400d c0400d, C0400d c0400d2) {
        this.f1554a.add(viewHolder);
    }

    public void mo907c(ViewHolder viewHolder, C0400d c0400d, C0400d c0400d2) {
        m1058a(viewHolder, c0400d.f1405a, c0400d.f1406b, c0400d2.f1405a, c0400d2.f1406b);
    }

    public void m1064b() {
        int i;
        int i2;
        int i3 = !this.f1554a.isEmpty() ? 1 : 0;
        if (this.f1556c.isEmpty()) {
            i = 0;
        } else {
            i = 1;
        }
        if (this.f1555b.isEmpty()) {
            i2 = 0;
        } else {
            i2 = 1;
        }
        if (i3 != 0 || i != 0 || i2 != 0) {
            Iterator it = this.f1554a.iterator();
            while (it.hasNext()) {
                m1045a((ViewHolder) it.next());
            }
            this.f1554a.clear();
            if (i != 0) {
                Runnable c04081 = new C04081(this);
                if (i3 != 0) {
                    ((C0414a) this.f1556c.get(0)).f1550a.itemView.postDelayed(c04081, mo904b());
                } else {
                    c04081.run();
                }
            }
            if (i2 != 0) {
                Runnable c04092 = new C04092(this);
                if (i3 == 0 && i == 0) {
                    c04092.run();
                    return;
                }
                long c;
                long b = i3 != 0 ? mo904b() : 0;
                if (i != 0) {
                    c = mo906c();
                } else {
                    c = 0;
                }
                ((ViewHolder) this.f1555b.get(0)).itemView.postDelayed(c04092, b + c);
            }
        }
    }

    private void m1045a(final ViewHolder viewHolder) {
        final View view = viewHolder.itemView;
        final ViewPropertyAnimator animate = view.animate();
        animate.setDuration(mo904b()).alpha(0.0f).setListener(new C0410b(this) {
            final /* synthetic */ C0415b f1538a;

            public void mo900a(boolean z) {
                animate.setListener(null);
                view.setAlpha(1.0f);
                this.f1538a.f1559f.remove(viewHolder);
                this.f1538a.m1053c(viewHolder);
            }
        }).start();
        this.f1559f.add(viewHolder);
    }

    private void m1049b(final ViewHolder viewHolder) {
        final View view = viewHolder.itemView;
        final ViewPropertyAnimator animate = view.animate();
        animate.alpha(1.0f).setDuration(mo901a()).setListener(new C0410b(this) {
            final /* synthetic */ C0415b f1542a;

            public void mo900a(boolean z) {
                if (z) {
                    view.setAlpha(1.0f);
                }
                animate.setListener(null);
                this.f1542a.f1557d.remove(viewHolder);
                this.f1542a.m1053c(viewHolder);
            }
        }).start();
        this.f1557d.add(viewHolder);
    }

    public void m1058a(ViewHolder viewHolder, int i, int i2, int i3, int i4) {
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
            this.f1556c.add(new C0414a(viewHolder, translationX, translationY, i3, i4));
        }
    }

    private void m1050b(ViewHolder viewHolder, int i, int i2, int i3, int i4) {
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
        this.f1558e.add(viewHolder);
        final ViewHolder viewHolder2 = viewHolder;
        animate.setDuration(mo906c()).setListener(new C0410b(this) {
            final /* synthetic */ C0415b f1547a;

            public void mo900a(boolean z) {
                if (z) {
                    if (i5 != 0) {
                        view.setTranslationX(0.0f);
                    }
                    if (i6 != 0) {
                        view.setTranslationY(0.0f);
                    }
                }
                animate.setListener(null);
                this.f1547a.f1558e.remove(viewHolder2);
                this.f1547a.m1053c(viewHolder2);
            }
        }).start();
    }

    private final void m1053c(ViewHolder viewHolder) {
        if (this.a != null) {
            this.a.mo886a(viewHolder);
            if (!mo901a()) {
                this.a.mo885a();
            }
        }
    }

    public final void m1067c() {
        if (this.a != null && !mo901a()) {
            this.a.mo885a();
        }
    }

    public boolean m1062a() {
        return (this.f1555b.isEmpty() && this.f1556c.isEmpty() && this.f1554a.isEmpty() && this.f1558e.isEmpty() && this.f1559f.isEmpty() && this.f1557d.isEmpty()) ? false : true;
    }

    public void m1057a() {
        int size;
        for (size = this.f1556c.size() - 1; size >= 0; size--) {
            C0414a c0414a = (C0414a) this.f1556c.get(size);
            View view = c0414a.f1550a.itemView;
            view.setTranslationY(0.0f);
            view.setTranslationX(0.0f);
            this.f1556c.remove(size);
            m1053c(c0414a.f1550a);
        }
        for (size = this.f1554a.size() - 1; size >= 0; size--) {
            ViewHolder viewHolder = (ViewHolder) this.f1554a.get(size);
            this.f1554a.remove(size);
            m1053c(viewHolder);
        }
        for (size = this.f1555b.size() - 1; size >= 0; size--) {
            viewHolder = (ViewHolder) this.f1555b.get(size);
            viewHolder.itemView.setAlpha(1.0f);
            this.f1555b.remove(size);
            m1053c(viewHolder);
        }
        if (mo901a()) {
            m1061a(this.f1559f);
            m1061a(this.f1558e);
            m1061a(this.f1557d);
            mo906c();
        }
    }

    void m1061a(List<ViewHolder> list) {
        for (int size = list.size() - 1; size >= 0; size--) {
            ((ViewHolder) list.get(size)).itemView.animate().cancel();
        }
    }
}

package com.gala.video.albumlist.widget;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.view.View;
import android.view.ViewPropertyAnimator;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@TargetApi(14)
public class b extends c {
    private ArrayList<ViewHolder> a = new ArrayList();
    private ArrayList<ViewHolder> b = new ArrayList();
    private ArrayList<a> c = new ArrayList();
    private ArrayList<ViewHolder> d = new ArrayList();
    private ArrayList<ViewHolder> e = new ArrayList();
    private ArrayList<ViewHolder> f = new ArrayList();

    private class b extends b {
        final /* synthetic */ b b;

        private b(b bVar) {
            this.b = bVar;
        }

        public void onAnimationCancel(Animator animator) {
            a(true);
        }

        public void onAnimationEnd(Animator animator) {
            a(false);
        }

        public void a(boolean z) {
        }
    }

    private static class a {
        public int a;
        public ViewHolder f675a;
        public int b;
        public int c;
        public int d;

        private a(ViewHolder viewHolder, int i, int i2, int i3, int i4) {
            this.f675a = viewHolder;
            this.a = i;
            this.b = i2;
            this.c = i3;
            this.d = i4;
        }
    }

    public void a(ViewHolder viewHolder, d dVar, d dVar2) {
        viewHolder.itemView.setAlpha(0.0f);
        this.b.add(viewHolder);
    }

    public void b(ViewHolder viewHolder, d dVar, d dVar2) {
        this.a.add(viewHolder);
    }

    public void c(ViewHolder viewHolder, d dVar, d dVar2) {
        a(viewHolder, dVar.a, dVar.b, dVar2.a, dVar2.b);
    }

    public void b() {
        int i;
        int i2;
        int i3 = !this.a.isEmpty() ? 1 : 0;
        if (this.c.isEmpty()) {
            i = 0;
        } else {
            i = 1;
        }
        if (this.b.isEmpty()) {
            i2 = 0;
        } else {
            i2 = 1;
        }
        if (i3 != 0 || i != 0 || i2 != 0) {
            Iterator it = this.a.iterator();
            while (it.hasNext()) {
                a((ViewHolder) it.next());
            }
            this.a.clear();
            if (i != 0) {
                Runnable anonymousClass1 = new Runnable(this) {
                    final /* synthetic */ b a;

                    {
                        this.a = r1;
                    }

                    public void run() {
                        Iterator it = this.a.c.iterator();
                        while (it.hasNext()) {
                            a aVar = (a) it.next();
                            this.a.b(aVar.f675a, aVar.a, aVar.b, aVar.c, aVar.d);
                        }
                        this.a.c.clear();
                    }
                };
                if (i3 != 0) {
                    ((a) this.c.get(0)).f675a.itemView.postDelayed(anonymousClass1, b());
                } else {
                    anonymousClass1.run();
                }
            }
            if (i2 != 0) {
                Runnable anonymousClass2 = new Runnable(this) {
                    final /* synthetic */ b a;

                    {
                        this.a = r1;
                    }

                    public void run() {
                        Iterator it = this.a.b.iterator();
                        while (it.hasNext()) {
                            this.a.b((ViewHolder) it.next());
                        }
                        this.a.b.clear();
                    }
                };
                if (i3 == 0 && i == 0) {
                    anonymousClass2.run();
                    return;
                }
                long c;
                long b = i3 != 0 ? b() : 0;
                if (i != 0) {
                    c = c();
                } else {
                    c = 0;
                }
                ((ViewHolder) this.b.get(0)).itemView.postDelayed(anonymousClass2, b + c);
            }
        }
    }

    private void a(final ViewHolder viewHolder) {
        final View view = viewHolder.itemView;
        final ViewPropertyAnimator animate = view.animate();
        animate.setDuration(b()).alpha(0.0f).setListener(new b(this) {
            final /* synthetic */ b f667a;

            public void a(boolean z) {
                animate.setListener(null);
                view.setAlpha(1.0f);
                this.f667a.f.remove(viewHolder);
                this.f667a.c(viewHolder);
            }
        }).start();
        this.f.add(viewHolder);
    }

    private void b(final ViewHolder viewHolder) {
        final View view = viewHolder.itemView;
        final ViewPropertyAnimator animate = view.animate();
        animate.alpha(1.0f).setDuration(a()).setListener(new b(this) {
            final /* synthetic */ b f670a;

            public void a(boolean z) {
                if (z) {
                    view.setAlpha(1.0f);
                }
                animate.setListener(null);
                this.f670a.d.remove(viewHolder);
                this.f670a.c(viewHolder);
            }
        }).start();
        this.d.add(viewHolder);
    }

    public void a(ViewHolder viewHolder, int i, int i2, int i3, int i4) {
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
            this.c.add(new a(viewHolder, translationX, translationY, i3, i4));
        }
    }

    private void b(ViewHolder viewHolder, int i, int i2, int i3, int i4) {
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
        this.e.add(viewHolder);
        final ViewHolder viewHolder2 = viewHolder;
        animate.setDuration(c()).setListener(new b(this) {
            final /* synthetic */ b f674a;

            public void a(boolean z) {
                if (z) {
                    if (i5 != 0) {
                        view.setTranslationX(0.0f);
                    }
                    if (i6 != 0) {
                        view.setTranslationY(0.0f);
                    }
                }
                animate.setListener(null);
                this.f674a.e.remove(viewHolder2);
                this.f674a.c(viewHolder2);
            }
        }).start();
    }

    private final void c(ViewHolder viewHolder) {
        if (this.a != null) {
            this.a.a(viewHolder);
            if (!a()) {
                this.a.a();
            }
        }
    }

    public final void c() {
        if (this.a != null && !a()) {
            this.a.a();
        }
    }

    public boolean m135a() {
        return (this.b.isEmpty() && this.c.isEmpty() && this.a.isEmpty() && this.e.isEmpty() && this.f.isEmpty() && this.d.isEmpty()) ? false : true;
    }

    public void a() {
        int size;
        for (size = this.c.size() - 1; size >= 0; size--) {
            a aVar = (a) this.c.get(size);
            View view = aVar.f675a.itemView;
            view.setTranslationY(0.0f);
            view.setTranslationX(0.0f);
            this.c.remove(size);
            c(aVar.f675a);
        }
        for (size = this.a.size() - 1; size >= 0; size--) {
            ViewHolder viewHolder = (ViewHolder) this.a.get(size);
            this.a.remove(size);
            c(viewHolder);
        }
        for (size = this.b.size() - 1; size >= 0; size--) {
            viewHolder = (ViewHolder) this.b.get(size);
            viewHolder.itemView.setAlpha(1.0f);
            this.b.remove(size);
            c(viewHolder);
        }
        if (a()) {
            a(this.f);
            a(this.e);
            a(this.d);
            c();
        }
    }

    void a(List<ViewHolder> list) {
        for (int size = list.size() - 1; size >= 0; size--) {
            ((ViewHolder) list.get(size)).itemView.animate().cancel();
        }
    }
}

package com.gala.imageprovider.p000private;

import com.gala.afinal.FinalDb;

public class C0127c<M, O> {
    private FinalDb f546a;
    private Class<M> f547a;
    private M f548a;
    private boolean f549a = false;
    private Class<O> f550b;
    private Object f551b;
    private O f552c;

    public C0127c(M m, Class<M> cls, Class<O> cls2, FinalDb finalDb) {
        this.f548a = m;
        this.f547a = cls;
        this.f550b = cls2;
        this.f546a = finalDb;
    }

    public final O m314a() {
        if (this.f552c == null && !this.f549a) {
            this.f546a.loadManyToOne$482427bc(null, this.f548a, this.f547a, this.f550b);
            this.f549a = true;
        }
        return this.f552c;
    }

    public final void m315a(O o) {
        this.f552c = o;
    }

    public final Object m316b() {
        return this.f551b;
    }

    public final void m317b(Object obj) {
        this.f551b = obj;
    }
}

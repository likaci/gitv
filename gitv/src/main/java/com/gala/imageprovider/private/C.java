package com.gala.imageprovider.private;

import com.gala.afinal.FinalDb;

public class c<M, O> {
    private FinalDb a;
    private Class<M> f27a;
    private M f28a;
    private boolean f29a = false;
    private Class<O> b;
    private Object f30b;
    private O c;

    public c(M m, Class<M> cls, Class<O> cls2, FinalDb finalDb) {
        this.f28a = m;
        this.f27a = cls;
        this.b = cls2;
        this.a = finalDb;
    }

    public final O a() {
        if (this.c == null && !this.f29a) {
            this.a.loadManyToOne$482427bc(null, this.f28a, this.f27a, this.b);
            this.f29a = true;
        }
        return this.c;
    }

    public final void a(O o) {
        this.c = o;
    }

    public final Object b() {
        return this.f30b;
    }

    public final void b(Object obj) {
        this.f30b = obj;
    }
}

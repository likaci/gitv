package com.gala.imageprovider.p000private;

import java.util.ArrayList;
import java.util.List;

public final class C0152x {
    private List<Runnable> f605a = new ArrayList();

    public final synchronized void m386a(Runnable runnable) {
        this.f605a.add(runnable);
    }

    public final synchronized void m387b(Runnable runnable) {
        if (this.f605a.contains(runnable)) {
            this.f605a.remove(runnable);
        }
    }
}

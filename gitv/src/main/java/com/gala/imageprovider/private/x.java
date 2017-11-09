package com.gala.imageprovider.private;

import java.util.ArrayList;
import java.util.List;

public final class x {
    private List<Runnable> a = new ArrayList();

    public final synchronized void a(Runnable runnable) {
        this.a.add(runnable);
    }

    public final synchronized void b(Runnable runnable) {
        if (this.a.contains(runnable)) {
            this.a.remove(runnable);
        }
    }
}

package com.gala.video.api;

import java.util.List;

public class C0474a {
    private IApiFilter f1853a;

    public final void m1516a(IApiFilter iApiFilter) {
        this.f1853a = iApiFilter;
    }

    protected final List<String> m1515a(List<String> list) {
        if (this.f1853a != null) {
            return this.f1853a.onHeader(list);
        }
        return list;
    }
}

package com.gala.video.api;

import java.util.List;

public class a {
    private IApiFilter a;

    public final void a(IApiFilter iApiFilter) {
        this.a = iApiFilter;
    }

    protected final List<String> a(List<String> list) {
        if (this.a != null) {
            return this.a.onHeader(list);
        }
        return list;
    }
}

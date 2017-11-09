package com.gala.imageprovider.private;

import java.util.LinkedList;

public final class e {
    private String a;
    private LinkedList<Object> f26a;

    public final String a() {
        return this.a;
    }

    public final void a(String str) {
        this.a = str;
    }

    public final Object[] m9a() {
        if (this.f26a != null) {
            return this.f26a.toArray();
        }
        return null;
    }

    public final String[] m10a() {
        if (this.f26a == null) {
            return null;
        }
        String[] strArr = new String[this.f26a.size()];
        for (int i = 0; i < this.f26a.size(); i++) {
            strArr[i] = this.f26a.get(i).toString();
        }
        return strArr;
    }

    public final void a(Object obj) {
        if (this.f26a == null) {
            this.f26a = new LinkedList();
        }
        this.f26a.add(obj);
    }
}

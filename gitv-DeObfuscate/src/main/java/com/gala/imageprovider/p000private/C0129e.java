package com.gala.imageprovider.p000private;

import java.util.LinkedList;

public final class C0129e {
    private String f553a;
    private LinkedList<Object> f554a;

    public final String m319a() {
        return this.f553a;
    }

    public final void m321a(String str) {
        this.f553a = str;
    }

    public final Object[] m322a() {
        if (this.f554a != null) {
            return this.f554a.toArray();
        }
        return null;
    }

    public final String[] m323a() {
        if (this.f554a == null) {
            return null;
        }
        String[] strArr = new String[this.f554a.size()];
        for (int i = 0; i < this.f554a.size(); i++) {
            strArr[i] = this.f554a.get(i).toString();
        }
        return strArr;
    }

    public final void m320a(Object obj) {
        if (this.f554a == null) {
            this.f554a = new LinkedList();
        }
        this.f554a.add(obj);
    }
}

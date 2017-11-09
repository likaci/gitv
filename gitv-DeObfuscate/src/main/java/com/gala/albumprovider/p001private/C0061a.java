package com.gala.albumprovider.p001private;

import com.gala.tvapi.tv2.model.Album;
import java.util.ArrayList;
import java.util.List;

public class C0061a {
    private int f258a = 0;
    private List<Album> f259a = new ArrayList();
    private int f260b = 0;

    public void m96a(List<Album> list) {
        this.f259a.addAll(list);
    }

    public List<Album> m94a() {
        return this.f259a;
    }

    public void m95a(int i) {
        this.f258a = i;
    }

    public int m93a() {
        return this.f258a;
    }

    public void m99b(int i) {
        this.f260b = i;
    }

    public int m98b() {
        return this.f260b;
    }

    public boolean m97a() {
        if (this.f259a == null || this.f259a.size() <= 0) {
            return true;
        }
        return false;
    }
}

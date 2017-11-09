package com.gala.albumprovider.private;

import com.gala.tvapi.tv2.model.Album;
import java.util.ArrayList;
import java.util.List;

public class a {
    private int a = 0;
    private List<Album> f98a = new ArrayList();
    private int b = 0;

    public void a(List<Album> list) {
        this.f98a.addAll(list);
    }

    public List<Album> m25a() {
        return this.f98a;
    }

    public void a(int i) {
        this.a = i;
    }

    public int a() {
        return this.a;
    }

    public void b(int i) {
        this.b = i;
    }

    public int b() {
        return this.b;
    }

    public boolean m26a() {
        if (this.f98a == null || this.f98a.size() <= 0) {
            return true;
        }
        return false;
    }
}

package com.gala.albumprovider.private;

import com.gala.albumprovider.model.QLayoutKind;
import com.gala.tvapi.tv2.model.Album;
import java.util.ArrayList;
import java.util.List;

public class c {
    private int a;
    private QLayoutKind f112a;
    private String f113a;
    private List<Album> f114a = new ArrayList();

    public void a(int i) {
        this.a = i;
    }

    public int a() {
        return this.a;
    }

    public void a(String str) {
        this.f113a = str;
    }

    public String m28a() {
        return this.f113a;
    }

    public void a(QLayoutKind qLayoutKind) {
        this.f112a = qLayoutKind;
    }

    public QLayoutKind m27a() {
        return this.f112a;
    }

    public void a(List<Album> list) {
        this.f114a.addAll(list);
    }

    public List<Album> m29a() {
        return this.f114a;
    }
}

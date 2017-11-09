package com.gala.albumprovider.p001private;

import com.gala.albumprovider.model.QLayoutKind;
import com.gala.tvapi.tv2.model.Album;
import java.util.ArrayList;
import java.util.List;

public class C0063c {
    private int f275a;
    private QLayoutKind f276a;
    private String f277a;
    private List<Album> f278a = new ArrayList();

    public void m118a(int i) {
        this.f275a = i;
    }

    public int m114a() {
        return this.f275a;
    }

    public void m120a(String str) {
        this.f277a = str;
    }

    public String m116a() {
        return this.f277a;
    }

    public void m119a(QLayoutKind qLayoutKind) {
        this.f276a = qLayoutKind;
    }

    public QLayoutKind m115a() {
        return this.f276a;
    }

    public void m121a(List<Album> list) {
        this.f278a.addAll(list);
    }

    public List<Album> m117a() {
        return this.f278a;
    }
}

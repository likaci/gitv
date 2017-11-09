package com.gala.albumprovider.p001private;

import com.gala.tvapi.vrs.model.ChannelPlayListLabel;
import java.util.ArrayList;
import java.util.List;

public class C0065e {
    private int f285a = 0;
    private List<ChannelPlayListLabel> f286a = new ArrayList();

    public void m133a(int i) {
        this.f285a = i;
    }

    public int m131a() {
        return this.f285a;
    }

    public void m134a(List<ChannelPlayListLabel> list) {
        this.f286a.addAll(list);
    }

    public List<ChannelPlayListLabel> m132a() {
        return this.f286a;
    }

    public boolean m135a() {
        if (this.f286a == null || this.f286a.size() <= 0) {
            return true;
        }
        return false;
    }
}

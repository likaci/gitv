package com.gala.albumprovider.private;

import com.gala.tvapi.vrs.model.ChannelPlayListLabel;
import java.util.ArrayList;
import java.util.List;

public class e {
    private int a = 0;
    private List<ChannelPlayListLabel> f133a = new ArrayList();

    public void a(int i) {
        this.a = i;
    }

    public int a() {
        return this.a;
    }

    public void a(List<ChannelPlayListLabel> list) {
        this.f133a.addAll(list);
    }

    public List<ChannelPlayListLabel> m30a() {
        return this.f133a;
    }

    public boolean m31a() {
        if (this.f133a == null || this.f133a.size() <= 0) {
            return true;
        }
        return false;
    }
}

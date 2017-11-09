package com.gala.albumprovider.p001private;

import com.gala.tvapi.vrs.model.ChannelLabel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class C0066f {
    private String f287a;
    private Map<String, List<ChannelLabel>> f288a = new HashMap();
    private String f289b;

    public C0066f(String str, String str2) {
        this.f287a = str;
        this.f289b = str2;
    }

    public void m137a(String str, List<ChannelLabel> list) {
        this.f288a.put(str, list);
    }

    public List<ChannelLabel> m136a(String str) {
        return (List) this.f288a.get(str);
    }

    public boolean m138a(String str) {
        if (this.f288a.get(str) == null) {
            return true;
        }
        return false;
    }
}

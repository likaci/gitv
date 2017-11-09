package com.gala.albumprovider.private;

import com.gala.tvapi.vrs.model.ChannelLabel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class f {
    private String a;
    private Map<String, List<ChannelLabel>> f145a = new HashMap();
    private String b;

    public f(String str, String str2) {
        this.a = str;
        this.b = str2;
    }

    public void a(String str, List<ChannelLabel> list) {
        this.f145a.put(str, list);
    }

    public List<ChannelLabel> a(String str) {
        return (List) this.f145a.get(str);
    }

    public boolean m32a(String str) {
        if (this.f145a.get(str) == null) {
            return true;
        }
        return false;
    }
}

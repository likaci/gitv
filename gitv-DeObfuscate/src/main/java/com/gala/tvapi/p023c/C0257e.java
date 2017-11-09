package com.gala.tvapi.p023c;

import java.net.MalformedURLException;
import java.net.URL;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public final class C0257e {
    private String f909a = "";
    private URL f910a = null;
    private String[] f911a;

    public C0257e(String str) {
        this.f909a = str;
        try {
            this.f910a = new URL(this.f909a);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            this.f910a = null;
        }
    }

    public final String m624a(String str) {
        if (this.f910a == null) {
            return this.f909a;
        }
        this.f911a = this.f910a.getQuery().split("&");
        for (String str2 : this.f911a) {
            if (str2.contains(str)) {
                return str2;
            }
        }
        return str + SearchCriteria.EQ;
    }
}

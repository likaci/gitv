package com.gala.tvapi.c;

import java.net.MalformedURLException;
import java.net.URL;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public final class e {
    private String a = "";
    private URL f464a = null;
    private String[] f465a;

    public e(String str) {
        this.a = str;
        try {
            this.f464a = new URL(this.a);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            this.f464a = null;
        }
    }

    public final String a(String str) {
        if (this.f464a == null) {
            return this.a;
        }
        this.f465a = this.f464a.getQuery().split("&");
        for (String str2 : this.f465a) {
            if (str2.contains(str)) {
                return str2;
            }
        }
        return str + SearchCriteria.EQ;
    }
}

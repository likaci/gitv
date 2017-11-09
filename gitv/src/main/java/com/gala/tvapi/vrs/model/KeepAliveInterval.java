package com.gala.tvapi.vrs.model;

import com.gala.tvapi.b.a;

public class KeepAliveInterval extends Model {
    private static final long serialVersionUID = 1;
    public String authcookie;
    public int interval;

    public boolean isAuthCookiIsEmpty() {
        return a.a(this.authcookie);
    }
}

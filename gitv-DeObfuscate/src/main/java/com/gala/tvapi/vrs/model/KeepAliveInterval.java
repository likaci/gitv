package com.gala.tvapi.vrs.model;

import com.gala.tvapi.p008b.C0214a;

public class KeepAliveInterval extends Model {
    private static final long serialVersionUID = 1;
    public String authcookie;
    public int interval;

    public boolean isAuthCookiIsEmpty() {
        return C0214a.m592a(this.authcookie);
    }
}

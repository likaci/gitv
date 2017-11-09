package com.gala.tvapi;

import java.util.ArrayList;
import java.util.List;
import org.cybergarage.soap.SOAP;

public class TVApiHeader {
    private List<String> a;

    public TVApiHeader() {
        this.a = null;
        this.a = new ArrayList(5);
    }

    public void setHeader(String key, String value) {
        this.a.add(key + SOAP.DELIM + value);
    }

    public List<String> getHeaders() {
        return this.a;
    }
}

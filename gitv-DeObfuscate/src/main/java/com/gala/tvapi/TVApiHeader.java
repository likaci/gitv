package com.gala.tvapi;

import java.util.ArrayList;
import java.util.List;
import org.cybergarage.soap.SOAP;

public class TVApiHeader {
    private List<String> f880a;

    public TVApiHeader() {
        this.f880a = null;
        this.f880a = new ArrayList(5);
    }

    public void setHeader(String key, String value) {
        this.f880a.add(key + SOAP.DELIM + value);
    }

    public List<String> getHeaders() {
        return this.f880a;
    }
}

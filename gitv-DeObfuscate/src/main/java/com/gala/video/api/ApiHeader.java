package com.gala.video.api;

import java.util.LinkedList;
import java.util.List;
import org.cybergarage.soap.SOAP;

public class ApiHeader implements IApiHeader {
    private List<String> f1852a;

    public ApiHeader() {
        this.f1852a = null;
        this.f1852a = new LinkedList();
    }

    public ApiHeader(List<String> headers) {
        this.f1852a = null;
        this.f1852a = headers;
    }

    public void setHeader(String key, String value) {
        if (this.f1852a != null) {
            this.f1852a.add(key + SOAP.DELIM + value);
        }
    }

    public List<String> getHeader() {
        return this.f1852a;
    }
}

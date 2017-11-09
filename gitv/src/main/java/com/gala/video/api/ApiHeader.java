package com.gala.video.api;

import java.util.LinkedList;
import java.util.List;
import org.cybergarage.soap.SOAP;

public class ApiHeader implements IApiHeader {
    private List<String> a;

    public ApiHeader() {
        this.a = null;
        this.a = new LinkedList();
    }

    public ApiHeader(List<String> headers) {
        this.a = null;
        this.a = headers;
    }

    public void setHeader(String key, String value) {
        if (this.a != null) {
            this.a.add(key + SOAP.DELIM + value);
        }
    }

    public List<String> getHeader() {
        return this.a;
    }
}

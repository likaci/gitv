package com.gala.tvapi.log;

import java.io.Serializable;

public class TVApiLogModel implements Serializable {
    private String a = "";
    private String b = "";

    public void setUrl(String url) {
        this.a = url;
    }

    public void setResponse(String response) {
        this.b = response;
    }

    public String getUrl() {
        return this.a;
    }

    public String getResponse() {
        return this.b;
    }
}

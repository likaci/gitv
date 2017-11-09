package com.gala.tvapi.log;

import java.io.Serializable;

public class TVApiLogModel implements Serializable {
    private String f920a = "";
    private String f921b = "";

    public void setUrl(String url) {
        this.f920a = url;
    }

    public void setResponse(String response) {
        this.f921b = response;
    }

    public String getUrl() {
        return this.f920a;
    }

    public String getResponse() {
        return this.f921b;
    }
}

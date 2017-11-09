package com.gala.video.api;

import java.util.ArrayList;
import java.util.List;

public class ApiException extends Exception {
    private int a = 0;
    private String f72a = "";
    private List<Integer> f73a;
    private String b = "";
    private String c = "";
    private String d = "";
    private String e = "";
    private String f = "";
    private String g = "";

    public String getCode() {
        return this.f72a;
    }

    public ApiException(String message, String code) {
        super(message);
        this.f72a = code;
    }

    public ApiException(String message, String code, String httpCode, String url) {
        super(message);
        this.f72a = code;
        this.b = httpCode;
        this.c = url;
    }

    public ApiException(String message, String code, String httpCode, String url, String name) {
        super(message);
        this.f72a = code;
        this.b = httpCode;
        this.c = url;
        this.d = name;
    }

    public String getHttpCode() {
        return this.b;
    }

    public ApiException(String message) {
        super(message);
    }

    public String getUrl() {
        return this.c;
    }

    public String getExceptionClassName() {
        return this.d;
    }

    public void setDetailMessage(String msg) {
        this.e = msg;
    }

    public String getDetailMessage() {
        return this.e;
    }

    public void setRequestTimes(List<Integer> times) {
        if (times != null && times.size() > 0) {
            this.f73a = new ArrayList(times.size());
            for (Integer intValue : times) {
                this.f73a.add(Integer.valueOf(intValue.intValue()));
            }
        }
    }

    public List<Integer> getRequestTimes() {
        return this.f73a;
    }

    public void setParseTime(int time) {
        this.a = time;
    }

    public int getParseTime() {
        return this.a;
    }

    public void setCode2(String code) {
        this.f = code;
    }

    public String getCode2() {
        return this.f;
    }

    public void setApiName(String name) {
        this.g = name;
    }

    public String getApiName() {
        return this.g;
    }
}

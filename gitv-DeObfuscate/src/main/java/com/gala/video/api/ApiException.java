package com.gala.video.api;

import java.util.ArrayList;
import java.util.List;

public class ApiException extends Exception {
    private int f1843a = 0;
    private String f1844a = "";
    private List<Integer> f1845a;
    private String f1846b = "";
    private String f1847c = "";
    private String f1848d = "";
    private String f1849e = "";
    private String f1850f = "";
    private String f1851g = "";

    public String getCode() {
        return this.f1844a;
    }

    public ApiException(String message, String code) {
        super(message);
        this.f1844a = code;
    }

    public ApiException(String message, String code, String httpCode, String url) {
        super(message);
        this.f1844a = code;
        this.f1846b = httpCode;
        this.f1847c = url;
    }

    public ApiException(String message, String code, String httpCode, String url, String name) {
        super(message);
        this.f1844a = code;
        this.f1846b = httpCode;
        this.f1847c = url;
        this.f1848d = name;
    }

    public String getHttpCode() {
        return this.f1846b;
    }

    public ApiException(String message) {
        super(message);
    }

    public String getUrl() {
        return this.f1847c;
    }

    public String getExceptionClassName() {
        return this.f1848d;
    }

    public void setDetailMessage(String msg) {
        this.f1849e = msg;
    }

    public String getDetailMessage() {
        return this.f1849e;
    }

    public void setRequestTimes(List<Integer> times) {
        if (times != null && times.size() > 0) {
            this.f1845a = new ArrayList(times.size());
            for (Integer intValue : times) {
                this.f1845a.add(Integer.valueOf(intValue.intValue()));
            }
        }
    }

    public List<Integer> getRequestTimes() {
        return this.f1845a;
    }

    public void setParseTime(int time) {
        this.f1843a = time;
    }

    public int getParseTime() {
        return this.f1843a;
    }

    public void setCode2(String code) {
        this.f1850f = code;
    }

    public String getCode2() {
        return this.f1850f;
    }

    public void setApiName(String name) {
        this.f1851g = name;
    }

    public String getApiName() {
        return this.f1851g;
    }
}

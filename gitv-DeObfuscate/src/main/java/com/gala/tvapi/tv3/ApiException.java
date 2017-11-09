package com.gala.tvapi.tv3;

public class ApiException {
    private int f1057a;
    private Exception f1058a;
    private String f1059a;

    public ApiException(int httpCode, Exception e) {
        this.f1057a = httpCode;
        this.f1058a = e;
    }

    public ApiException(int httpCode, String code, Exception e) {
        this.f1057a = httpCode;
        this.f1059a = code;
        this.f1058a = e;
    }

    public int getHttpCode() {
        return this.f1057a;
    }

    public String getCode() {
        return this.f1059a;
    }

    public Exception getException() {
        return this.f1058a;
    }
}

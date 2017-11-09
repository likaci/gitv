package com.gala.tvapi.tv3;

public class ApiException {
    private int a;
    private Exception f508a;
    private String f509a;

    public ApiException(int httpCode, Exception e) {
        this.a = httpCode;
        this.f508a = e;
    }

    public ApiException(int httpCode, String code, Exception e) {
        this.a = httpCode;
        this.f509a = code;
        this.f508a = e;
    }

    public int getHttpCode() {
        return this.a;
    }

    public String getCode() {
        return this.f509a;
    }

    public Exception getException() {
        return this.f508a;
    }
}

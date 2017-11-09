package com.qiyi.tv.client;

public class Result<T> {
    public int code;
    public T data;

    public Result(int code, T data) {
        this.code = code;
        this.data = data;
    }
}

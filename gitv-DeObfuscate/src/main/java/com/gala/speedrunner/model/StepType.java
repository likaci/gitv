package com.gala.speedrunner.model;

public enum StepType {
    DNS_BEGIN(1),
    DNS_SUCCESS(2),
    DNS_FAILED(3),
    URL_VALID(4),
    URL_INVALID(5),
    GET_KEY_BEGIN(6),
    GET_KEY_SUCCESS(7),
    GET_KEY_FAILED(8),
    AUTHENTICATE_BEGIN(9),
    AUTHENTICATE_M3U8(10),
    AUTHENTICATE_M3U8_FAILED(11),
    AUTHENTICATE_SUCCESS(12),
    AUTHENTICATE_FAILED(13),
    PING_CACHE_BEGIN(14),
    PING_CACHE_END(15),
    TRACE_CACHE_BEGIN(16),
    TRACE_CACHE_END(17),
    DOWNLOAD_BEGIN(18),
    DOWNLOAD_END(19),
    COMPLETE(20);
    
    private int f748a;

    private StepType(int _mCode) {
        this.f748a = _mCode;
    }

    public final int getCode() {
        return this.f748a;
    }
}

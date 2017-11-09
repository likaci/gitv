package com.xiaomi.mistatistic.sdk.controller;

import java.io.FilterInputStream;
import java.io.InputStream;

public final class C2127r extends FilterInputStream {
    private boolean f2216a;

    public C2127r(InputStream inputStream) {
        super(inputStream);
    }

    public int read(byte[] bArr, int i, int i2) {
        if (!this.f2216a) {
            int read = super.read(bArr, i, i2);
            if (read != -1) {
                return read;
            }
        }
        this.f2216a = true;
        return -1;
    }
}

package com.xiaomi.mistatistic.sdk.controller;

import java.io.FilterInputStream;
import java.io.InputStream;

public final class r extends FilterInputStream {
    private boolean a;

    public r(InputStream inputStream) {
        super(inputStream);
    }

    public int read(byte[] bArr, int i, int i2) {
        if (!this.a) {
            int read = super.read(bArr, i, i2);
            if (read != -1) {
                return read;
            }
        }
        this.a = true;
        return -1;
    }
}

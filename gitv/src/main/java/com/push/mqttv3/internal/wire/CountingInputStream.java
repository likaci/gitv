package com.push.mqttv3.internal.wire;

import java.io.IOException;
import java.io.InputStream;

public class CountingInputStream extends InputStream {
    private int counter = 0;
    private InputStream in;

    public CountingInputStream(InputStream in) {
        this.in = in;
    }

    public int read() throws IOException {
        int i = this.in.read();
        if (i != -1) {
            this.counter++;
        }
        return i;
    }

    public int getCounter() {
        return this.counter;
    }

    public void resetCounter() {
        this.counter = 0;
    }
}

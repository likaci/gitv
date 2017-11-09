package com.gala.afinal.bitmap.core;

import java.util.ArrayList;

public class BytesBufferPool {
    private final int a;
    private final ArrayList<BytesBuffer> f33a;
    private final int b;

    public static class BytesBuffer {
        public byte[] data;
        public int length;
        public int offset;

        private BytesBuffer(int capacity) {
            this.data = new byte[capacity];
        }
    }

    public BytesBufferPool(int poolSize, int bufferSize) {
        this.f33a = new ArrayList(poolSize);
        this.a = poolSize;
        this.b = bufferSize;
    }

    public synchronized BytesBuffer get() {
        int size;
        size = this.f33a.size();
        return size > 0 ? (BytesBuffer) this.f33a.remove(size - 1) : new BytesBuffer(this.b);
    }

    public synchronized void recycle(BytesBuffer buffer) {
        if (buffer.data.length == this.b) {
            if (this.f33a.size() < this.a) {
                buffer.offset = 0;
                buffer.length = 0;
                this.f33a.add(buffer);
            }
        }
    }

    public synchronized void clear() {
        this.f33a.clear();
    }
}

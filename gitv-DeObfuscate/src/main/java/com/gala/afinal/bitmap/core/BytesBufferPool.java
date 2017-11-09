package com.gala.afinal.bitmap.core;

import java.util.ArrayList;

public class BytesBufferPool {
    private final int f21a;
    private final ArrayList<BytesBuffer> f22a;
    private final int f23b;

    public static class BytesBuffer {
        public byte[] data;
        public int length;
        public int offset;

        private BytesBuffer(int capacity) {
            this.data = new byte[capacity];
        }
    }

    public BytesBufferPool(int poolSize, int bufferSize) {
        this.f22a = new ArrayList(poolSize);
        this.f21a = poolSize;
        this.f23b = bufferSize;
    }

    public synchronized BytesBuffer get() {
        int size;
        size = this.f22a.size();
        return size > 0 ? (BytesBuffer) this.f22a.remove(size - 1) : new BytesBuffer(this.f23b);
    }

    public synchronized void recycle(BytesBuffer buffer) {
        if (buffer.data.length == this.f23b) {
            if (this.f22a.size() < this.f21a) {
                buffer.offset = 0;
                buffer.length = 0;
                this.f22a.add(buffer);
            }
        }
    }

    public synchronized void clear() {
        this.f22a.clear();
    }
}

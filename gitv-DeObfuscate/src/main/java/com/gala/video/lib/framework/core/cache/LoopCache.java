package com.gala.video.lib.framework.core.cache;

public class LoopCache<T> {
    private final Object[] mCache;
    private final int mCapacity;
    private int mIndex;

    public LoopCache(int capacity) {
        this.mCapacity = capacity;
        this.mCache = new Object[capacity];
    }

    public synchronized int add(T value) {
        int index;
        if (this.mIndex < 0 || this.mIndex >= this.mCapacity) {
            this.mIndex = 0;
        }
        this.mCache[this.mIndex] = value;
        index = this.mIndex;
        this.mIndex++;
        return index;
    }

    public int getCapacity() {
        return this.mCapacity;
    }

    public synchronized T get(int index) {
        T t;
        if (index >= 0) {
            if (index < this.mCapacity) {
                t = this.mCache[index];
            }
        }
        t = null;
        return t;
    }

    public synchronized T remove(int index) {
        T t = null;
        synchronized (this) {
            if (index >= 0) {
                if (index < this.mCapacity) {
                    t = this.mCache[index];
                    this.mCache[index] = null;
                }
            }
        }
        return t;
    }
}

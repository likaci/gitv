package com.gala.video.lib.share.uikit.resolver;

import android.util.SparseArray;
import com.gala.video.lib.share.uikit.protocol.Resolver;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseResolver<T, O> implements Resolver<T, O> {
    protected Map<T, Integer> mMap = new HashMap(64);
    protected SparseArray<T> mSparseArray = new SparseArray(64);

    public int size() {
        return this.mSparseArray.size();
    }

    public int type(T gen) {
        if (this.mMap.containsKey(gen)) {
            return ((Integer) this.mMap.get(gen)).intValue();
        }
        return -1;
    }

    public T get(int type) {
        return this.mSparseArray.get(type);
    }

    public void register(int type, T gen) {
        this.mMap.put(gen, Integer.valueOf(type));
        this.mSparseArray.put(type, gen);
    }
}

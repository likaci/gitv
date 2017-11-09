package com.gala.video.lib.framework.core.cache;

import java.util.Map;

public interface ICache<T> {

    public interface Visitor<T> {
        void visit(String str, T t);
    }

    void accept(Visitor<T> visitor);

    void clear();

    T get(String str);

    void put(String str, T t);

    void remove(String str);

    void update(Map<String, T> map);
}

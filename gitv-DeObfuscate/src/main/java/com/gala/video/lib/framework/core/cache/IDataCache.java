package com.gala.video.lib.framework.core.cache;

import java.util.List;

public interface IDataCache<T> {
    public static final int DISK = 3;
    public static final int MEMORY = 0;
    public static final int MEMORY_AND_DISK = 1;

    List<T> get(String str);

    void save(List<T> list, boolean z, String str);
}

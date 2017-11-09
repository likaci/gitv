package com.gala.sdk.utils;

import android.util.SparseArray;
import java.util.List;
import java.util.Map;

public class ListUtils {
    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isEmpty(SparseArray<?> array) {
        return array == null || array.size() == 0;
    }

    public static int getCount(List<?> list) {
        return list == null ? 0 : list.size();
    }

    public static boolean isEmpty(Object[] list) {
        return list == null || list.length <= 0;
    }
}

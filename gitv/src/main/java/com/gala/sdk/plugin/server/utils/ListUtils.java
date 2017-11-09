package com.gala.sdk.plugin.server.utils;

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

    public static int getCount(Map<?, ?> map) {
        return map == null ? 0 : map.size();
    }

    public static boolean isLegal(List<?> list, int pos) {
        return list != null && list.size() > 0 && pos >= 0 && pos < list.size();
    }

    public static boolean isLegal(Map<?, ?> map, int pos) {
        return map != null && map.size() > 0 && pos >= 0 && pos < map.size();
    }

    public static int getCount(SparseArray<?> sparseArray) {
        return sparseArray == null ? 0 : sparseArray.size();
    }
}

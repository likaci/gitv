package com.gala.video.lib.share.utils;

import java.util.List;
import java.util.Map;

public class Precondition {
    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isEmpty(List<?> list) {
        return isNull(list) || list.isEmpty();
    }

    public static boolean isEmpty(String[] strings) {
        return isNull(strings) || strings.length == 0;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return isNull(map) || map.size() == 0;
    }

    public static boolean isEmpty(int[] list) {
        return isNull(list) || list.length == 0;
    }
}

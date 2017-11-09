package com.gitv.tvappstore.utils;

import java.util.List;

public class CommonUtils {
    public static boolean isListEmpty(List<?> list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }
}

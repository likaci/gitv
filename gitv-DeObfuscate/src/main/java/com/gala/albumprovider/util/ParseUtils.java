package com.gala.albumprovider.util;

public class ParseUtils {
    public static int str2Int(String a) {
        try {
            return Integer.parseInt(a);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}

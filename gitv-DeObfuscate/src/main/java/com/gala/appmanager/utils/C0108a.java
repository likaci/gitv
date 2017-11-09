package com.gala.appmanager.utils;

import java.io.File;

public class C0108a {
    public static boolean m234a(String str) {
        if (C0111d.m242a(str)) {
            return false;
        }
        return new File(str).exists();
    }
}

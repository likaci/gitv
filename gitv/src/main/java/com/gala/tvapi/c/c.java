package com.gala.tvapi.c;

public final class c {
    private static int a = 0;
    private static c f463a;

    private c() {
    }

    public static c m84a() {
        if (f463a == null) {
            f463a = new c();
        }
        return f463a;
    }

    public static int a() {
        if (a == Integer.MAX_VALUE) {
            a = 0;
        }
        int i = a + 1;
        a = i;
        return i;
    }
}

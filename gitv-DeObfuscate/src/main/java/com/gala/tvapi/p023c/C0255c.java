package com.gala.tvapi.p023c;

public final class C0255c {
    private static int f906a = 0;
    private static C0255c f907a;

    private C0255c() {
    }

    public static C0255c m620a() {
        if (f907a == null) {
            f907a = new C0255c();
        }
        return f907a;
    }

    public static int m619a() {
        if (f906a == Integer.MAX_VALUE) {
            f906a = 0;
        }
        int i = f906a + 1;
        f906a = i;
        return i;
    }
}

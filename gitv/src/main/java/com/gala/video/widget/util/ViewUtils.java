package com.gala.video.widget.util;

import java.util.concurrent.atomic.AtomicInteger;

public class ViewUtils {
    private static final AtomicInteger mAtomicCounter = new AtomicInteger(1);

    public static int generateViewId() {
        return mAtomicCounter.incrementAndGet();
    }
}

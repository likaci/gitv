package com.gala.sdk.utils.performance;

public interface IPerformanceMonitor {
    void start();

    void stop();

    void updateContent(CharSequence charSequence);

    void updateTitle(String str);
}
